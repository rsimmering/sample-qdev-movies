package com.amazonaws.samples.qdevmovies.movies;

import com.amazonaws.samples.qdevmovies.utils.MovieIconUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class MoviesController {
    private static final Logger logger = LogManager.getLogger(MoviesController.class);

    @Autowired
    private MovieService movieService;

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/movies")
    public String getMovies(org.springframework.ui.Model model) {
        logger.info("Fetching movies");
        model.addAttribute("movies", movieService.getAllMovies());
        model.addAttribute("genres", movieService.getAllGenres());
        return "movies";
    }

    /**
     * Ahoy matey! This be the movie search endpoint that handles both HTML and JSON requests.
     * Search for movies by name, ID, or genre - or any combination ye desire!
     * 
     * @param name Movie name to search for (optional, case-insensitive partial match)
     * @param id Specific movie ID to find (optional)
     * @param genre Genre to filter by (optional, case-insensitive partial match)
     * @param model Spring model for HTML responses
     * @return JSON response for API calls or HTML template for browser requests
     */
    @GetMapping("/movies/search")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> searchMoviesApi(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String genre) {
        
        logger.info("Ahoy! API search request received - name: '{}', id: '{}', genre: '{}'", name, id, genre);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validate ID parameter if provided
            if (id != null && id <= 0) {
                response.put("success", false);
                response.put("message", "Arrr! That ID be invalid, matey! Must be a positive number.");
                response.put("movies", List.of());
                return ResponseEntity.badRequest().body(response);
            }
            
            List<Movie> searchResults = movieService.searchMovies(name, id, genre);
            
            response.put("success", true);
            response.put("movies", searchResults);
            
            if (searchResults.isEmpty()) {
                response.put("message", "Shiver me timbers! No movies found matching yer search criteria. Try castin' a wider net, ye savvy?");
            } else {
                response.put("message", String.format("Ahoy! Found %d treasure%s matching yer search!", 
                    searchResults.size(), searchResults.size() == 1 ? "" : "s"));
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Blimey! Error occurred during movie search: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", "Arrr! Something went wrong during the search. The kraken might have eaten our data!");
            response.put("movies", List.of());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * HTML form search endpoint for browser-based searches
     * Returns the movies template with search results
     */
    @GetMapping("/movies/search/form")
    public String searchMoviesForm(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String genre,
            org.springframework.ui.Model model) {
        
        logger.info("Ahoy! HTML form search request - name: '{}', id: '{}', genre: '{}'", name, id, genre);
        
        try {
            // Validate ID parameter if provided
            if (id != null && id <= 0) {
                model.addAttribute("error", "Arrr! That ID be invalid, matey! Must be a positive number.");
                model.addAttribute("movies", movieService.getAllMovies());
                model.addAttribute("genres", movieService.getAllGenres());
                return "movies";
            }
            
            List<Movie> searchResults = movieService.searchMovies(name, id, genre);
            
            model.addAttribute("movies", searchResults);
            model.addAttribute("genres", movieService.getAllGenres());
            
            // Add search parameters back to the model for form state
            model.addAttribute("searchName", name);
            model.addAttribute("searchId", id);
            model.addAttribute("searchGenre", genre);
            
            if (searchResults.isEmpty()) {
                model.addAttribute("searchMessage", "Shiver me timbers! No movies found matching yer search criteria. Try castin' a wider net, ye savvy?");
            } else {
                model.addAttribute("searchMessage", String.format("Ahoy! Found %d treasure%s matching yer search!", 
                    searchResults.size(), searchResults.size() == 1 ? "" : "s"));
            }
            
        } catch (Exception e) {
            logger.error("Blimey! Error occurred during HTML form search: {}", e.getMessage(), e);
            model.addAttribute("error", "Arrr! Something went wrong during the search. The kraken might have eaten our data!");
            model.addAttribute("movies", movieService.getAllMovies());
            model.addAttribute("genres", movieService.getAllGenres());
        }
        
        return "movies";
    }

    @GetMapping("/movies/{id}/details")
    public String getMovieDetails(@PathVariable("id") Long movieId, org.springframework.ui.Model model) {
        logger.info("Fetching details for movie ID: {}", movieId);
        
        Optional<Movie> movieOpt = movieService.getMovieById(movieId);
        if (!movieOpt.isPresent()) {
            logger.warn("Movie with ID {} not found", movieId);
            model.addAttribute("title", "Movie Not Found");
            model.addAttribute("message", "Movie with ID " + movieId + " was not found.");
            return "error";
        }
        
        Movie movie = movieOpt.get();
        model.addAttribute("movie", movie);
        model.addAttribute("movieIcon", MovieIconUtils.getMovieIcon(movie.getMovieName()));
        model.addAttribute("allReviews", reviewService.getReviewsForMovie(movie.getId()));
        
        return "movie-details";
    }
}