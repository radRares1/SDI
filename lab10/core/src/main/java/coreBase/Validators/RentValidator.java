package coreBase.Validators;

import coreBase.Entities.RentAction;
import org.springframework.stereotype.Component;

@Component
public class RentValidator implements Validator<RentAction> {

    @Override
    public void validate(RentAction rent) throws ValidatorException {

        if(rent.getMovieId()<0)
            throw new ValidatorException("Invalid Client Id!");

        if(rent.getClientId()<0)
            throw new ValidatorException("Invalid Movie Id!");
    }
}