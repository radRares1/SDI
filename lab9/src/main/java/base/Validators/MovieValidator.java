package base.Validators;

        import base.Entities.Movie;
        import org.springframework.stereotype.Component;

/**
 * @author Rares.
 */
@Component
public class MovieValidator implements Validator<Movie> {

    @Override
    public void validate(Movie movie) throws ValidatorException {

        if(movie.getTitle().equals(""))
            throw new ValidatorException("Empty Title!");

        if(movie.getPrice()<0)
            throw new ValidatorException("Negative price!");
    }
}
