package com.amazonaws.samples.qdevmovies.movies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for MovieService search functionality
 * Arrr! These tests be makin' sure our treasure huntin' methods work ship-shape!
 */
public class MovieServiceSearchTest {

    private MovieService movieService;

    @BeforeEach
    public void setUp() {
        movieService = new MovieService();
    }

    @Test
    public void testSearchMoviesByNamePartialMatch() {
        List<Movie> results = movieService.searchMovies("Prison", null, null);
        
        assertFalse(results.isEmpty());
        assertTrue(results.stream().anyMatch(movie -> 
            movie.getMovieName().toLowerCase().contains("prison")));
    }

    @Test
    public void testSearchMoviesByNameCaseInsensitive() {
        List<Movie> results1 = movieService.searchMovies("PRISON", null, null);
        List<Movie> results2 = movieService.searchMovies("prison", null, null);
        List<Movie> results3 = movieService.searchMovies("Prison", null, null);
        
        assertEquals(results1.size(), results2.size());
        assertEquals(results2.size(), results3.size());
        assertFalse(results1.isEmpty());
    }

    @Test
    public void testSearchMoviesByGenrePartialMatch() {
        List<Movie> results = movieService.searchMovies(null, null, "Drama");
        
        assertFalse(results.isEmpty());
        assertTrue(results.stream().allMatch(movie -> 
            movie.getGenre().toLowerCase().contains("drama")));
    }

    @Test
    public void testSearchMoviesByGenreCaseInsensitive() {
        List<Movie> results1 = movieService.searchMovies(null, null, "DRAMA");
        List<Movie> results2 = movieService.searchMovies(null, null, "drama");
        List<Movie> results3 = movieService.searchMovies(null, null, "Drama");
        
        assertEquals(results1.size(), results2.size());
        assertEquals(results2.size(), results3.size());
        assertFalse(results1.isEmpty());
    }

    @Test
    public void testSearchMoviesById() {
        List<Movie> results = movieService.searchMovies(null, 1L, null);
        
        assertEquals(1, results.size());
        assertEquals(1L, results.get(0).getId());
    }

    @Test
    public void testSearchMoviesByIdNotFound() {
        List<Movie> results = movieService.searchMovies(null, 999L, null);
        
        assertTrue(results.isEmpty());
    }

    @Test
    public void testSearchMoviesByIdInvalid() {
        List<Movie> results1 = movieService.searchMovies(null, -1L, null);
        List<Movie> results2 = movieService.searchMovies(null, 0L, null);
        
        assertTrue(results1.isEmpty());
        assertTrue(results2.isEmpty());
    }

    @Test
    public void testSearchMoviesMultipleCriteria() {
        // Search for a movie that matches both name and genre
        List<Movie> results = movieService.searchMovies("Prison", null, "Drama");
        
        assertFalse(results.isEmpty());
        assertTrue(results.stream().allMatch(movie -> 
            movie.getMovieName().toLowerCase().contains("prison") &&
            movie.getGenre().toLowerCase().contains("drama")));
    }

    @Test
    public void testSearchMoviesMultipleCriteriaNoMatch() {
        // Search for a combination that shouldn't exist
        List<Movie> results = movieService.searchMovies("Prison", null, "Comedy");
        
        assertTrue(results.isEmpty());
    }

    @Test
    public void testSearchMoviesIdWithOtherCriteria() {
        // When ID is specified, other criteria should also be checked
        List<Movie> results = movieService.searchMovies("NonExistent", 1L, null);
        
        assertTrue(results.isEmpty(), "Should be empty because movie with ID 1 doesn't match 'NonExistent' name");
    }

    @Test
    public void testSearchMoviesIdWithMatchingCriteria() {
        // When ID is specified and other criteria match
        Optional<Movie> movie = movieService.getMovieById(1L);
        assertTrue(movie.isPresent());
        
        String movieName = movie.get().getMovieName();
        String movieGenre = movie.get().getGenre();
        
        List<Movie> results = movieService.searchMovies(movieName.substring(0, 3), 1L, movieGenre);
        
        assertEquals(1, results.size());
        assertEquals(1L, results.get(0).getId());
    }

    @Test
    public void testSearchMoviesEmptyParameters() {
        List<Movie> results = movieService.searchMovies(null, null, null);
        
        // Should return all movies when no criteria specified
        List<Movie> allMovies = movieService.getAllMovies();
        assertEquals(allMovies.size(), results.size());
    }

    @Test
    public void testSearchMoviesEmptyStringParameters() {
        List<Movie> results = movieService.searchMovies("", null, "");
        
        // Should return all movies when empty string criteria specified
        List<Movie> allMovies = movieService.getAllMovies();
        assertEquals(allMovies.size(), results.size());
    }

    @Test
    public void testSearchMoviesWhitespaceParameters() {
        List<Movie> results = movieService.searchMovies("   ", null, "   ");
        
        // Should return all movies when whitespace-only criteria specified
        List<Movie> allMovies = movieService.getAllMovies();
        assertEquals(allMovies.size(), results.size());
    }

    @Test
    public void testSearchMoviesNonExistentName() {
        List<Movie> results = movieService.searchMovies("NonExistentMovieName", null, null);
        
        assertTrue(results.isEmpty());
    }

    @Test
    public void testSearchMoviesNonExistentGenre() {
        List<Movie> results = movieService.searchMovies(null, null, "NonExistentGenre");
        
        assertTrue(results.isEmpty());
    }

    @Test
    public void testGetAllGenres() {
        List<String> genres = movieService.getAllGenres();
        
        assertNotNull(genres);
        assertFalse(genres.isEmpty());
        
        // Check that genres are unique and sorted
        for (int i = 1; i < genres.size(); i++) {
            assertTrue(genres.get(i).compareTo(genres.get(i-1)) >= 0, 
                "Genres should be sorted alphabetically");
        }
        
        // Check that all genres from movies are included
        List<Movie> allMovies = movieService.getAllMovies();
        for (Movie movie : allMovies) {
            assertTrue(genres.contains(movie.getGenre()), 
                "Genre '" + movie.getGenre() + "' should be in the genres list");
        }
    }

    @Test
    public void testSearchMoviesPartialGenreMatch() {
        // Test partial matching for compound genres like "Crime/Drama"
        List<Movie> results = movieService.searchMovies(null, null, "Crime");
        
        assertFalse(results.isEmpty());
        assertTrue(results.stream().allMatch(movie -> 
            movie.getGenre().toLowerCase().contains("crime")));
    }

    @Test
    public void testSearchMoviesComplexScenario() {
        // Test a complex search scenario with realistic data
        List<Movie> allMovies = movieService.getAllMovies();
        assertFalse(allMovies.isEmpty());
        
        // Get the first movie and search for it using partial criteria
        Movie firstMovie = allMovies.get(0);
        String partialName = firstMovie.getMovieName().substring(0, 
            Math.min(3, firstMovie.getMovieName().length()));
        
        List<Movie> results = movieService.searchMovies(partialName, null, null);
        
        assertFalse(results.isEmpty());
        assertTrue(results.stream().anyMatch(movie -> 
            movie.getId() == firstMovie.getId()));
    }
}