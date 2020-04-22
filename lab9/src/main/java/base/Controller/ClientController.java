package base.Controller;

import base.Entities.Client;
import base.Validators.ClientValidator;
import base.Validators.ValidatorException;
import base.Repository.ClientRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * @author Rares.
 */
@Service
public class ClientController {

    public static final Logger log = LoggerFactory.getLogger(ClientController.class);


    @Autowired
    private ClientRepo repo;
    @Autowired
    private ClientValidator validator ;



    public Optional<Client> getById(Integer clientId) {
        return repo.findById(clientId);

    }

    public List<Client> getAllClients() {
        log.trace("getAllClients - method entered");
        Iterable<Client> clients = repo.findAll();
        log.trace("getAllClients - method finished");
        return (List<Client>) clients;
    }

    public void addClient(Client clientToSave) {
        try
        {

            log.trace("addClient - method entered with client :",clientToSave);
            validator.validate(clientToSave);
            repo.save(clientToSave);
            log.trace("addClient - method finished");
        }
        catch(ValidatorException v)
        {
            throw new ValidatorException(v.getMessage());
        }

    }

    public void deleteClient(Integer clientToDelete) throws ValidatorException {
        try{
            log.trace("deleteClient - method entered with id :",clientToDelete);
            repo.deleteById(clientToDelete);
            log.trace("deleteClient - method finished");
        }
        catch (ValidatorException v){
            throw  new ValidatorException((v.getMessage()));
        }
    }

    @Transactional
    public void updateClient(Client UpdatedClient) throws Exception {

        log.trace("updateClient - method entered with client :",UpdatedClient);

            repo.findById(UpdatedClient.getId())
                    .ifPresent(u -> {
                        u.setName(UpdatedClient.getName());
                        u.setAge(UpdatedClient.getAge());
                        log.debug("updateClient - updated: client={}", u);
                    });

        log.trace("updateClient - method finished");
    }

    public List<Client> filterOddId() throws Exception {

        log.trace("filterOddClients - method entered");
        List<Client> all = (List<Client>) repo.findAll();
        all.removeIf(client->client.getId()%2!=0);

        log.trace("filterOddClients - method finished");
        return all;
    }

    /*
    filters clients that have the name length less than some number
     */

    public List<Client> filterClientsWithNameLessThan(int length) throws Exception {
        log.trace("filterClientsWithNameLessThan - method entered");
        List<Client> all = (List<Client>) repo.findAll();
        all.removeIf(client->client.getName().length() < length);
        log.trace("filterClientsWithNameLessThan - method finished");
        return all;
    }
}
