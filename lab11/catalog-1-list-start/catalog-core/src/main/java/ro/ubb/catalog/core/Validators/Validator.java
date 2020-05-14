package ro.ubb.catalog.core.Validators;

/**
 * @param <T>
 * @author Rares.
 */
public interface Validator<T> {
    void validate(T entity) throws ValidatorException;
}
