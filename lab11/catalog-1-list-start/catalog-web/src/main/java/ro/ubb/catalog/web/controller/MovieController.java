package ro.ubb.catalog.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.ubb.catalog.core.service.MovieService;
import ro.ubb.catalog.web.converter.MovieConverter;
import ro.ubb.catalog.web.dto.ClientDto;
import ro.ubb.catalog.web.dto.MovieDto;
import ro.ubb.catalog.web.dto.MoviesDto;

import java.util.List;

@RestController
public class MovieController {

    public static final Logger log= LoggerFactory.getLogger(MovieController.class);

    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieConverter movieConverter;

    @RequestMapping(value = "/movies", method = RequestMethod.GET)
    List<MovieDto> getMovies() throws Exception {

        log.trace("getMovies - method entered - web");
        return movieConverter.convertModelsToDtos(movieService.getAllMovies());
    }

    @RequestMapping(value = "/movies", method = RequestMethod.POST)
    MovieDto addMovie(@RequestBody MovieDto movieDto)
    {

        log.trace("addMovie - method entered - web");
        return movieConverter.convertModelToDto(movieService.addMovie(
                movieConverter.convertDtoToModel(movieDto)
        ));
    }

    @RequestMapping(value = "/movies/{id}", method = RequestMethod.PUT)
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
    List<MovieDto> filterEvenId() throws Exception {

        log.trace("filterEvenMovie - method entered - web");

        return movieConverter.convertModelsToDtos(movieService.filterEvenId());
    }

    @RequestMapping(value = "/movies/filter", method = RequestMethod.POST)
    List<MovieDto> filterMoviesWithTitleLessThan(@RequestBody Integer length) throws Exception {

        log.trace("filterMoviesWithTitleLessThan - method entered - web");

        return movieConverter.convertModelsToDtos(movieService.filterMoviesWithTitleLessThan(length));
    }

}
