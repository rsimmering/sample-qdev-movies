# 🏴‍☠️ Pirate's Movie Treasure Chest - Spring Boot Demo Application

Ahoy matey! Welcome to the Pirate's Movie Treasure Chest - a swashbuckling movie catalog web application built with Spring Boot, demonstrating Java application development best practices with a pirate flair!

## Features

- **Movie Catalog**: Browse 12 classic movies with detailed information
- **🔍 Movie Search & Filtering**: Search for movies by name, ID, or genre with our new pirate-themed search interface
- **Movie Details**: View comprehensive information including director, year, genre, duration, and description
- **Customer Reviews**: Each movie includes authentic customer reviews with ratings and avatars
- **REST API**: JSON endpoints for programmatic access to movie data and search functionality
- **Responsive Design**: Mobile-first design that works on all devices
- **Modern UI**: Dark theme with gradient backgrounds, smooth animations, and pirate-themed styling

## 🆕 New Search Features

### Web Interface Search
- **Pirate-themed search form** with movie name, ID, and genre filters
- **Real-time form validation** with pirate language error messages
- **Search state preservation** - form remembers your search criteria
- **Empty state handling** with helpful pirate messages

### REST API Search
- **Flexible search endpoint** `/movies/search` supporting multiple criteria
- **Case-insensitive partial matching** for names and genres
- **Comprehensive error handling** with pirate-themed responses
- **JSON responses** perfect for API integration

## Technology Stack

- **Java 8**
- **Spring Boot 2.0.5**
- **Maven** for dependency management
- **Thymeleaf** for server-side templating
- **Log4j 2.20.0**
- **JUnit 5.8.2**

## Quick Start

### Prerequisites

- Java 8 or higher
- Maven 3.6+

### Run the Application

```bash
git clone https://github.com/<youruser>/sample-qdev-movies.git
cd sample-qdev-movies
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Access the Application

- **Movie List with Search**: http://localhost:8080/movies
- **Movie Details**: http://localhost:8080/movies/{id}/details (where {id} is 1-12)
- **Search API**: http://localhost:8080/movies/search?name=prison&genre=drama

## Building for Production

```bash
mvn clean package
java -jar target/sample-qdev-movies-0.1.0.jar
```

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/amazonaws/samples/qdevmovies/
│   │       ├── movies/
│   │       │   ├── MoviesApplication.java    # Main Spring Boot application
│   │       │   ├── MoviesController.java     # REST controller with search endpoints
│   │       │   ├── MovieService.java         # Business logic with search functionality
│   │       │   ├── Movie.java                # Movie data model
│   │       │   ├── Review.java               # Review data model
│   │       │   └── ReviewService.java        # Review business logic
│   │       └── utils/
│   │           ├── MovieIconUtils.java       # Movie icon utilities
│   │           └── MovieUtils.java           # Movie validation utilities
│   └── resources/
│       ├── templates/
│       │   ├── movies.html                   # Enhanced with search form
│       │   └── movie-details.html            # Movie details template
│       ├── static/css/                       # Stylesheets
│       ├── application.yml                   # Application configuration
│       ├── movies.json                       # Movie data
│       ├── mock-reviews.json                 # Mock review data
│       └── log4j2.xml                        # Logging configuration
└── test/                                     # Comprehensive unit tests
    └── java/
        └── com/amazonaws/samples/qdevmovies/movies/
            ├── MoviesControllerTest.java     # Controller tests with search
            ├── MovieServiceSearchTest.java   # Detailed search functionality tests
            └── MovieTest.java                # Movie model tests
```

## API Endpoints

### Get All Movies
```
GET /movies
```
Returns an HTML page displaying all movies with ratings, basic information, and the new search form.

### 🆕 Search Movies (REST API)
```
GET /movies/search
```
Returns JSON response with movies matching search criteria.

**Query Parameters:**
- `name` (optional): Movie name to search for (case-insensitive partial match)
- `id` (optional): Specific movie ID to find (exact match)
- `genre` (optional): Genre to filter by (case-insensitive partial match)

**Example Requests:**
```bash
# Search by name
curl "http://localhost:8080/movies/search?name=prison"

# Search by genre
curl "http://localhost:8080/movies/search?genre=action"

# Search by ID
curl "http://localhost:8080/movies/search?id=5"

# Multiple criteria
curl "http://localhost:8080/movies/search?name=the&genre=drama"
```

**Example Response:**
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
      "description": "Two imprisoned men bond over a number of years...",
      "duration": 142,
      "imdbRating": 5.0
    }
  ]
}
```

### 🆕 Search Movies (HTML Form)
```
GET /movies/search/form
```
Returns the movies HTML template with search results and form state preserved.

**Query Parameters:** Same as REST API

### Get Movie Details
```
GET /movies/{id}/details
```
Returns an HTML page with detailed movie information and customer reviews.

**Parameters:**
- `id` (path parameter): Movie ID (1-12)

**Example:**
```
http://localhost:8080/movies/1/details
```

## 🔍 Search Features Documentation

For detailed information about the search functionality, including usage examples, error handling, and technical implementation details, see [MOVIE_SEARCH_API.md](MOVIE_SEARCH_API.md).

## Testing

The application includes comprehensive test coverage:

### Running Tests
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=MovieServiceSearchTest

# Run with coverage
mvn test jacoco:report
```

### Test Coverage
- **MoviesControllerTest**: Tests for all controller endpoints including new search functionality
- **MovieServiceSearchTest**: Comprehensive tests for search logic, edge cases, and error conditions
- **MovieTest**: Basic model validation tests

## Troubleshooting

### Port 8080 already in use

Run on a different port:
```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```

### Build failures

Clean and rebuild:
```bash
mvn clean compile
```

### Search not working

Check the logs for pirate-themed error messages:
```bash
tail -f logs/application.log
```

## Contributing

This project is designed as a demonstration application. Feel free to:
- Add more movies to the treasure chest
- Enhance the pirate-themed UI/UX
- Add new search features like advanced filtering
- Improve the responsive design
- Add more pirate language throughout the application

## Recent Updates

### Version 1.1.0 - Movie Search Feature
- ✅ Added comprehensive movie search and filtering API
- ✅ Implemented pirate-themed HTML search form interface
- ✅ Added REST API endpoints for programmatic access
- ✅ Enhanced error handling with pirate language
- ✅ Comprehensive test coverage for all new functionality
- ✅ Updated documentation with usage examples

## License

This sample code is licensed under the MIT-0 License. See the LICENSE file.

---

**Arrr! Happy treasure huntin' in our movie collection, ye savvy? 🏴‍☠️**
