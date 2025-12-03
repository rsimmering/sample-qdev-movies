package com.amazonaws.samples.qdevmovies.movies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.ui.ExtendedModelMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class MoviesControllerTest {

    private MoviesController moviesController;
    private Model model;
    private MovieService mockMovieService;
    private ReviewService mockReviewService;

    @BeforeEach
    public void setUp() {
        moviesController = new MoviesController();
        model = new ExtendedModelMap();
        
        // Create mock services
        mockMovieService = new MovieService() {
            @Override
            public List<Movie> getAllMovies() {
                return Arrays.asList(
                    new Movie(1L, "Test Movie", "Test Director", 2023, "Drama", "Test description", 120, 4.5),
                    new Movie(2L, "Action Movie", "Action Director", 2022, "Action", "Action description", 110, 4.0),
                    new Movie(3L, "Comedy Film", "Comedy Director", 2021, "Comedy", "Comedy description", 95, 3.5)
                );
            }
            
            @Override
            public Optional<Movie> getMovieById(Long id) {
                if (id == 1L) {
                    return Optional.of(new Movie(1L, "Test Movie", "Test Director", 2023, "Drama", "Test description", 120, 4.5));
                } else if (id == 2L) {
                    return Optional.of(new Movie(2L, "Action Movie", "Action Director", 2022, "Action", "Action description", 110, 4.0));
                }
                return Optional.empty();
            }
            
            @Override
            public List<Movie> searchMovies(String name, Long id, String genre) {
                List<Movie> allMovies = getAllMovies();
                List<Movie> results = new ArrayList<>();
                
                for (Movie movie : allMovies) {
                    boolean matches = true;
                    
                    if (name != null && !name.trim().isEmpty()) {
                        if (!movie.getMovieName().toLowerCase().contains(name.toLowerCase())) {
                            matches = false;
                        }
                    }
                    
                    if (genre != null && !genre.trim().isEmpty()) {
                        if (!movie.getGenre().toLowerCase().contains(genre.toLowerCase())) {
                            matches = false;
                        }
                    }
                    
                    if (id != null && id > 0) {
                        if (!movie.getId().equals(id)) {
                            matches = false;
                        }
                    }
                    
                    if (matches) {
                        results.add(movie);
                    }
                }
                
                return results;
            }
            
            @Override
            public List<String> getAllGenres() {
                return Arrays.asList("Action", "Comedy", "Drama");
            }
        };
        
        mockReviewService = new ReviewService() {
            @Override
            public List<Review> getReviewsForMovie(long movieId) {
                return new ArrayList<>();
            }
        };
        
        // Inject mocks using reflection
        try {
            java.lang.reflect.Field movieServiceField = MoviesController.class.getDeclaredField("movieService");
            movieServiceField.setAccessible(true);
            movieServiceField.set(moviesController, mockMovieService);
            
            java.lang.reflect.Field reviewServiceField = MoviesController.class.getDeclaredField("reviewService");
            reviewServiceField.setAccessible(true);
            reviewServiceField.set(moviesController, mockReviewService);
        } catch (Exception e) {
            throw new RuntimeException("Failed to inject mock services", e);
        }
    }

    @Test
    public void testGetMovies() {
        String result = moviesController.getMovies(model);
        assertNotNull(result);
        assertEquals("movies", result);
        assertTrue(model.containsAttribute("movies"));
        assertTrue(model.containsAttribute("genres"));
    }

    @Test
    public void testGetMovieDetails() {
        String result = moviesController.getMovieDetails(1L, model);
        assertNotNull(result);
        assertEquals("movie-details", result);
    }

    @Test
    public void testGetMovieDetailsNotFound() {
        String result = moviesController.getMovieDetails(999L, model);
        assertNotNull(result);
        assertEquals("error", result);
    }

    @Test
    public void testMovieServiceIntegration() {
        List<Movie> movies = mockMovieService.getAllMovies();
        assertEquals(3, movies.size());
        assertEquals("Test Movie", movies.get(0).getMovieName());
    }

    // New tests for search functionality

    @Test
    public void testSearchMoviesApiWithName() {
        ResponseEntity<Map<String, Object>> response = moviesController.searchMoviesApi("Test", null, null);
        
        assertEquals(200, response.getStatusCodeValue());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertTrue((Boolean) body.get("success"));
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) body.get("movies");
        assertEquals(1, movies.size());
        assertEquals("Test Movie", movies.get(0).getMovieName());
    }

    @Test
    public void testSearchMoviesApiWithGenre() {
        ResponseEntity<Map<String, Object>> response = moviesController.searchMoviesApi(null, null, "Action");
        
        assertEquals(200, response.getStatusCodeValue());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertTrue((Boolean) body.get("success"));
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) body.get("movies");
        assertEquals(1, movies.size());
        assertEquals("Action Movie", movies.get(0).getMovieName());
    }

    @Test
    public void testSearchMoviesApiWithId() {
        ResponseEntity<Map<String, Object>> response = moviesController.searchMoviesApi(null, 2L, null);
        
        assertEquals(200, response.getStatusCodeValue());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertTrue((Boolean) body.get("success"));
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) body.get("movies");
        assertEquals(1, movies.size());
        assertEquals("Action Movie", movies.get(0).getMovieName());
    }

    @Test
    public void testSearchMoviesApiWithInvalidId() {
        ResponseEntity<Map<String, Object>> response = moviesController.searchMoviesApi(null, -1L, null);
        
        assertEquals(400, response.getStatusCodeValue());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertFalse((Boolean) body.get("success"));
        assertTrue(body.get("message").toString().contains("invalid"));
    }

    @Test
    public void testSearchMoviesApiNoResults() {
        ResponseEntity<Map<String, Object>> response = moviesController.searchMoviesApi("NonExistent", null, null);
        
        assertEquals(200, response.getStatusCodeValue());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertTrue((Boolean) body.get("success"));
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) body.get("movies");
        assertTrue(movies.isEmpty());
        assertTrue(body.get("message").toString().contains("No movies found"));
    }

    @Test
    public void testSearchMoviesApiMultipleCriteria() {
        ResponseEntity<Map<String, Object>> response = moviesController.searchMoviesApi("Action", null, "Action");
        
        assertEquals(200, response.getStatusCodeValue());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertTrue((Boolean) body.get("success"));
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) body.get("movies");
        assertEquals(1, movies.size());
        assertEquals("Action Movie", movies.get(0).getMovieName());
    }

    @Test
    public void testSearchMoviesFormWithName() {
        String result = moviesController.searchMoviesForm("Test", null, null, model);
        
        assertEquals("movies", result);
        assertTrue(model.containsAttribute("movies"));
        assertTrue(model.containsAttribute("searchName"));
        assertTrue(model.containsAttribute("searchMessage"));
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) model.getAttribute("movies");
        assertEquals(1, movies.size());
        assertEquals("Test Movie", movies.get(0).getMovieName());
    }

    @Test
    public void testSearchMoviesFormWithInvalidId() {
        String result = moviesController.searchMoviesForm(null, -1L, null, model);
        
        assertEquals("movies", result);
        assertTrue(model.containsAttribute("error"));
        assertTrue(model.getAttribute("error").toString().contains("invalid"));
    }

    @Test
    public void testSearchMoviesFormNoResults() {
        String result = moviesController.searchMoviesForm("NonExistent", null, null, model);
        
        assertEquals("movies", result);
        assertTrue(model.containsAttribute("searchMessage"));
        assertTrue(model.getAttribute("searchMessage").toString().contains("No movies found"));
    }

    @Test
    public void testSearchMoviesFormPreservesSearchParameters() {
        String result = moviesController.searchMoviesForm("Test", 1L, "Drama", model);
        
        assertEquals("movies", result);
        assertEquals("Test", model.getAttribute("searchName"));
        assertEquals(1L, model.getAttribute("searchId"));
        assertEquals("Drama", model.getAttribute("searchGenre"));
    }
}
