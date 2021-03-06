package coreBase.Controller;

import coreBase.Entities.Client;
import coreBase.Repository.ClientRepo;
import coreBase.Validators.ClientValidator;
import coreBase.Validators.ValidatorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author Rares.
 */
@Service
public class ClientService {

    public static final Logger log = LoggerFactory.getLogger(ClientService.class);


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

    public Client addClient(Client clientToSave) {
        try
        {

            log.trace("addClient - method entered with client :",clientToSave);
            validator.validate(clientToSave);

            log.trace("addClient - method finished");
            return repo.save(clientToSave);
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
    public Client updateClient(Client UpdatedClient) throws Exception {

        log.trace("updateClient - method entered with client :",UpdatedClient);

        repo.findById(UpdatedClient.getId())
                    .ifPresent(u -> {
                        u.setName(UpdatedClient.getName());
                        u.setAge(UpdatedClient.getAge());
                        log.debug("updateClient - updated: client={}", u);
                    });

        log.trace("updateClient - method finished");
        return repo.findById(UpdatedClient.getId()).get();

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
