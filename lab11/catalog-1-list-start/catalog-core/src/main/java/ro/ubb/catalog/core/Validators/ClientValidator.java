package ro.ubb.catalog.core.Validators;

import org.springframework.stereotype.Component;
import ro.ubb.catalog.core.model.Client;

/**
 * @author Rares.
 */
@Component
public class ClientValidator implements Validator<Client> {

    @Override
    public void validate(Client client) throws ValidatorException {

        if(client.getName().equals(""))
            throw new ValidatorException("Empty Name!");

        if(client.getAge()<0)
            throw new ValidatorException("Negative Age!");
    }
}
