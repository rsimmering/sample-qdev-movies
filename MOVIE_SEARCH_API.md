# 🏴‍☠️ Movie Search API Documentation

Ahoy matey! Welcome to the Pirate's Movie Treasure Chest API documentation. This here document will guide ye through the new movie search and filtering capabilities that have been added to our movie service.

## Overview

The Movie Search API allows ye to search through our treasure chest of movies using various criteria. Whether ye be lookin' for a specific movie by name, huntin' for movies by genre, or searchin' by ID, this API has got ye covered!

## New Endpoints

### 1. REST API Search Endpoint

**Endpoint:** `GET /movies/search`

**Description:** Ahoy! This be the main search endpoint that returns JSON responses for API clients.

**Query Parameters:**
- `name` (optional): Movie name to search for (case-insensitive partial match)
- `id` (optional): Specific movie ID to find (exact match)
- `genre` (optional): Genre to filter by (case-insensitive partial match)

**Response Format:**
```json
{
  "success": true,
  "message": "Ahoy! Found 2 treasures matching yer search!",
  "movies": [
    {
      "id": 1,
      "movieName": "The Prison Escape",
      "director": "John Director",
      "year": 1994,
      "genre": "Drama",
      "description": "Two imprisoned men bond over a number of years...",
      "duration": 142,
      "imdbRating": 5.0
    }
  ]
}
```

### 2. HTML Form Search Endpoint

**Endpoint:** `GET /movies/search/form`

**Description:** This endpoint handles HTML form submissions and returns the movies template with search results.

**Query Parameters:** Same as the REST API endpoint

**Response:** Returns the `movies.html` template with filtered results and search form state preserved.

## Usage Examples

### Search by Movie Name

**Request:**
```
GET /movies/search?name=prison
```

**Response:**
```json
{
  "success": true,
  "message": "Ahoy! Found 1 treasure matching yer search!",
  "movies": [
    {
      "id": 1,
      "movieName": "The Prison Escape",
      "director": "John Director",
      "year": 1994,
      "genre": "Drama",
      "description": "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.",
      "duration": 142,
      "imdbRating": 5.0
    }
  ]
}
```

### Search by Genre

**Request:**
```
GET /movies/search?genre=action
```

**Response:**
```json
{
  "success": true,
  "message": "Ahoy! Found 3 treasures matching yer search!",
  "movies": [
    {
      "id": 3,
      "movieName": "The Masked Hero",
      "director": "Chris Moviemaker",
      "year": 2008,
      "genre": "Action/Crime",
      "description": "When a menacing villain wreaks havoc and chaos on the people of the city, a masked hero must accept one of the greatest psychological and physical tests.",
      "duration": 152,
      "imdbRating": 5.0
    }
  ]
}
```

### Search by Movie ID

**Request:**
```
GET /movies/search?id=5
```

**Response:**
```json
{
  "success": true,
  "message": "Ahoy! Found 1 treasure matching yer search!",
  "movies": [
    {
      "id": 5,
      "movieName": "Life Journey",
      "director": "Robert Filmmaker",
      "year": 1994,
      "genre": "Drama/Romance",
      "description": "The presidencies of Kennedy and Johnson, the Vietnam War, and other historical events unfold from the perspective of an Alabama man with an IQ of 75.",
      "duration": 142,
      "imdbRating": 4.0
    }
  ]
}
```

### Multiple Search Criteria

**Request:**
```
GET /movies/search?name=the&genre=drama
```

**Response:**
```json
{
  "success": true,
  "message": "Ahoy! Found 2 treasures matching yer search!",
  "movies": [
    {
      "id": 1,
      "movieName": "The Prison Escape",
      "director": "John Director",
      "year": 1994,
      "genre": "Drama",
      "description": "Two imprisoned men bond over a number of years...",
      "duration": 142,
      "imdbRating": 5.0
    }
  ]
}
```

## HTML Form Interface

The web interface now includes a pirate-themed search form with the following features:

### Form Fields
- **Movie Name**: Text input for partial movie name matching
- **Movie ID**: Number input for exact ID matching
- **Genre**: Dropdown select populated with all available genres

### Form Features
- **Search Treasure!** button to submit the search
- **Clear Search** button to reset and view all movies
- Form state preservation (search parameters are maintained after search)
- Pirate-themed labels and placeholders

### Search Messages
- Success messages: "Ahoy! Found X treasure(s) matching yer search!"
- No results: "Shiver me timbers! No movies found matching yer search criteria. Try castin' a wider net, ye savvy?"
- Error messages: "Arrr! That ID be invalid, matey! Must be a positive number."

## Error Handling

### Invalid ID Parameter

**Request:**
```
GET /movies/search?id=-1
```

**Response:**
```json
{
  "success": false,
  "message": "Arrr! That ID be invalid, matey! Must be a positive number.",
  "movies": []
}
```

### No Results Found

**Request:**
```
GET /movies/search?name=nonexistent
```

**Response:**
```json
{
  "success": true,
  "message": "Shiver me timbers! No movies found matching yer search criteria. Try castin' a wider net, ye savvy?",
  "movies": []
}
```

### Server Error

**Response:**
```json
{
  "success": false,
  "message": "Arrr! Something went wrong during the search. The kraken might have eaten our data!",
  "movies": []
}
```

## Search Behavior

### Case Sensitivity
- All text searches (name and genre) are **case-insensitive**
- "PRISON", "prison", and "Prison" will all return the same results

### Partial Matching
- Movie name searches support partial matching
- Genre searches support partial matching (useful for compound genres like "Crime/Drama")

### Multiple Criteria
- When multiple search parameters are provided, movies must match **ALL** criteria
- Empty or null parameters are ignored

### ID-Based Search
- When an ID is provided, the search first looks for that specific movie
- If found, it then checks if the movie also matches other provided criteria
- If other criteria don't match, no results are returned

## Available Genres

The following genres are available in our treasure chest:
- Action/Crime
- Action/Sci-Fi
- Adventure/Fantasy
- Adventure/Sci-Fi
- Crime/Drama
- Drama
- Drama/History
- Drama/Romance
- Drama/Thriller

## Technical Implementation

### Service Layer
- `MovieService.searchMovies(String name, Long id, String genre)`: Core search logic
- `MovieService.getAllGenres()`: Returns list of unique genres for form population

### Controller Layer
- `MoviesController.searchMoviesApi()`: REST API endpoint returning JSON
- `MoviesController.searchMoviesForm()`: HTML form endpoint returning template

### Template Integration
- Enhanced `movies.html` template with search form
- Pirate-themed styling and messaging
- Form state preservation and validation

## Testing

Comprehensive test suites have been implemented:
- `MoviesControllerTest`: Tests for both API and form endpoints
- `MovieServiceSearchTest`: Detailed tests for search logic and edge cases

Test coverage includes:
- Single and multiple search criteria
- Case sensitivity and partial matching
- Invalid parameters and edge cases
- Empty results and error conditions
- Form state preservation

---

**Arrr! That be all ye need to know about searchin' for movies in our treasure chest. Happy huntin', matey! 🏴‍☠️**