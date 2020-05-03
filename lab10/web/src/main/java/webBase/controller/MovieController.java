package webBase.controller;

import coreBase.Controller.MovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import webBase.converter.MovieConverter;
import webBase.dto.ClientsDto;
import webBase.dto.MovieDto;
import webBase.dto.MoviesDto;

@RestController
public class MovieController {

    public static final Logger log= LoggerFactory.getLogger(MovieController.class);

    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieConverter movieConverter;

    @RequestMapping(value = "/movies",method = RequestMethod.GET)
    MoviesDto getMovies() throws Exception {

        log.trace("getMovies - method entered - web");
        return new MoviesDto(movieConverter.convertModelsToDtos(movieService.getAllMovies()));
    }

    @RequestMapping(value = "/movies", method = RequestMethod.POST)
    MovieDto addMovie(@RequestBody MovieDto movieDto)
    {

        log.trace("addMovie - method entered - web");
        return movieConverter.convertModelToDto(movieService.addMovie(
                movieConverter.convertDtoToModel(movieDto)
        ));
    }

    @RequestMapping(value = "/movies", method = RequestMethod.PUT)
    MovieDto updateMovie(@RequestBody MovieDto movieDto) throws Exception {


        log.trace("updateMovie - method entered - web");
        return movieConverter.convertModelToDto(movieService.updateMovie(
                movieConverter.convertDtoToModel(movieDto)
        ));
    }

    @RequestMapping(value = "/movies/{id}", method = RequestMethod.DELETE)
    ResponseEntity<?> deleteMovie(@PathVariable Integer id)
    {
        log.trace("deleteMovie - method entered - web");

        movieService.deleteMovie(id);


        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/movies/filter", method = RequestMethod.GET)
    MoviesDto filterEvenId() throws Exception {

        log.trace("filterEvenMovie - method entered - web");

        return new  MoviesDto(movieConverter.convertModelsToDtos(movieService.filterEvenId()));
    }

    @RequestMapping(value = "/movies/filter", method = RequestMethod.POST)
    MoviesDto filterMoviesWithTitleLessThan(@RequestBody Integer length) throws Exception {

        log.trace("filterMoviesWithTitleLessThan - method entered - web");

        return new  MoviesDto(movieConverter.convertModelsToDtos(movieService.filterMoviesWithTitleLessThan(length)));
    }

}
