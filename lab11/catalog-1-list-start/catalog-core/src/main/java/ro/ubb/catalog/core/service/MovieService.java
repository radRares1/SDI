package ro.ubb.catalog.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.ubb.catalog.core.Validators.MovieValidator;
import ro.ubb.catalog.core.Validators.ValidatorException;
import ro.ubb.catalog.core.model.Movie;
import ro.ubb.catalog.core.repository.MovieRepo;

import java.util.List;
import java.util.Optional;

/**
 * @author Rares.
 */
@Service
public class MovieService {

    public static final Logger log = LoggerFactory.getLogger(MovieService.class);


    @Autowired
    private MovieRepo repo;
    @Autowired
    private MovieValidator validator;



    public Optional<Movie> getById(Integer movieId){
        return repo.findById(movieId);
    }

    public List<Movie> getAllMovies()  {
        log.trace("getAllMovies - method entered");
        Iterable<Movie> movies = repo.findAll();
        log.trace("getAllMovies - method finished");
        return (List<Movie>) movies;
    }

    public Movie addMovie(Movie movieToAdd) throws ValidatorException {
        try {
            log.trace("addMovie - method entered with movie:",movieToAdd);
            validator.validate(movieToAdd);
            log.trace("addMovie - method finished");
            return repo.save(movieToAdd);

        } catch (ValidatorException v) {
            throw new ValidatorException(v.getMessage());
        }
    }

    public void deleteMovie(Integer movieToDelete) throws ValidatorException{
        try{
            log.trace("deleteMovie - method entered with movie:",movieToDelete);
            repo.deleteById(movieToDelete);
            log.trace("deleteMovie - method finished");
        }
        catch (ValidatorException v){
            throw  new ValidatorException((v.getMessage()));
        }
    }

    @Transactional
    public Movie updateMovie(Movie updatedMovie) throws Exception {
        try{
            log.trace("updateMovie - method entered with movie:",updatedMovie);
            repo.findById(updatedMovie.getId())
                    .ifPresent(u -> {
                        u.setTitle(updatedMovie.getTitle());
                        u.setPrice(updatedMovie.getPrice());
                        log.debug("updateClient - updated: movie={}", u);
                    });
            log.trace("updateMovie - method finished");
            return repo.findById(updatedMovie.getId()).get();
        } catch(Exception e)
        {
            throw new Exception(e.getMessage());
        }
    }

    public List<Movie> filterEvenId() throws Exception {
        log.trace("filterEvenMovies - method entered");
        List<Movie> all = (List<Movie>) repo.findAll();
        all.removeIf(movie->movie.getId()%2==0);
        log.trace("filterEvenMovies - method finished");
        return all;
    }

    /*
    filters movies that have the title length less than some number
     */

    public List<Movie> filterMoviesWithTitleLessThan(int length) throws Exception {
        log.trace("filterMoviesWithTitleLessThan - method entered");
        List<Movie> all = (List<Movie>) repo.findAll();
        all.removeIf(movie->movie.getTitle().length() < length);
        log.trace("filterMoviesWithTitleLessThan - method finished");
        return all;
    }
}
