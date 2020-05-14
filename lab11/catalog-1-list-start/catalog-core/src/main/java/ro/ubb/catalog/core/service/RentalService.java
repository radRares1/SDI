package ro.ubb.catalog.core.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.ubb.catalog.core.Validators.RentValidator;
import ro.ubb.catalog.core.Validators.RentalException;
import ro.ubb.catalog.core.Validators.ValidatorException;
import ro.ubb.catalog.core.model.Client;
import ro.ubb.catalog.core.model.Movie;
import ro.ubb.catalog.core.model.RentAction;
import ro.ubb.catalog.core.repository.RentalRepo;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

@Service
public class RentalService {

    public static final Logger log = LoggerFactory.getLogger(RentalService.class);

    @Autowired
    private RentalRepo repo;
    @Autowired
    private RentValidator validator;
    @Autowired
    private ClientService clientService;
    @Autowired
    private MovieService movieService;



    public List<RentAction> getAllRentals(){
        log.trace("getAllRentals - method entered");
        Iterable<RentAction> rentals = repo.findAll();
        log.trace("getAllRentals - method finished");
        return (List<RentAction>) rentals;
    }

    public RentAction addRental(RentAction rentalToAdd) throws Exception {
        try {

            log.trace("addRental - method entered with rental",rentalToAdd);

            int clientID = rentalToAdd.getClientId();
            System.out.println(clientID);
            int movieID = rentalToAdd.getMovieId();

            Optional<Client> c = clientService.getById(clientID);
            Optional<Movie> m = movieService.getById(movieID);

            if(!c.isPresent())
                throw new ValidatorException("Client does not exist");

            if(!m.isPresent())
                throw new ValidatorException("Movie does not exist");

            validator.validate(rentalToAdd);

            RentAction result = repo.save(rentalToAdd);

            log.trace("addRental - method finished");
            return result;

        } catch (ValidatorException v) {
            throw new ValidatorException(v.getMessage());}
        catch (RentalException r){
            throw new RentalException(r.getMessage());
        }
    }

    @Transactional
    public RentAction updateRental(RentAction rent) throws Exception {
        try{
            log.trace("updateRental - method entered with rental:",rent);
            int clientID = rent.getClientId();
            int movieID = rent.getMovieId();

            Optional<Client> c = clientService.getById(clientID);
            Optional<Movie> m = movieService.getById(movieID);

            if(!c.isPresent())
                throw new ValidatorException("Client does not exist");

            if(!m.isPresent())
                throw new ValidatorException("Movie does not exist");

            validator.validate(rent);

            try{
                repo.findById(rent.getId())
                        .ifPresent(u -> {
                            u.setClientId(rent.getClientId());
                            u.setMovieId(rent.getMovieId());
                            log.debug("updateClient - updated: rental={}", u);
                        });
                log.trace("updateRental - method finished");

                return repo.findById(rent.getId()).get();
            } catch(Exception e)
            {
                throw new Exception(e.getMessage());
            }


        } catch (ValidatorException v) {
            throw new ValidatorException(v.getMessage());}
        catch (RentalException r){
                throw new RentalException(r.getMessage());
        }
    }




    public void deleteRent(Integer rentToDelete) throws ValidatorException {
        try{

            repo.deleteById(rentToDelete);
            log.trace("deleteRent - method finished");
        }
        catch (ValidatorException v){
            throw  new ValidatorException((v.getMessage()));
        }
    }

    public void deleteClient(Integer clientToDelete) throws ValidatorException, SQLException {
        clientService.deleteClient(clientToDelete);
        Iterable<RentAction> rentals = repo.findAll();
        rentals.forEach(Rent->{
            if(Rent.getClientId() == clientToDelete){
                this.deleteRent(Rent.getId());
            }
        });
    }

    public void deleteMovie(Integer movieToDelete) throws ValidatorException, SQLException {
        movieService.deleteMovie(movieToDelete);
        Iterable<RentAction> rentals = repo.findAll();
        rentals.forEach(Rent->{
            if(Rent.getMovieId() == movieToDelete){
                this.deleteRent(Rent.getId());
            }
        });
    }



}
