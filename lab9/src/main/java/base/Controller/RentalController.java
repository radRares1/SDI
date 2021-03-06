package base.Controller;

import base.Entities.Client;
import base.Entities.Movie;
import base.Entities.RentAction;
import base.Validators.RentValidator;
import base.Validators.RentalException;
import base.Validators.ValidatorException;
import base.Repository.RentalRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

@Service
public class RentalController {

    public static final Logger log = LoggerFactory.getLogger(RentalController.class);

    @Autowired
    private RentalRepo repo;
    @Autowired
    private RentValidator validator;
    @Autowired
    private ClientController clientController;
    @Autowired
    private MovieController movieController;

    private HashMap<Integer,Integer> mostRentedMovie = new HashMap<Integer,Integer>();
    private HashMap<Integer,Integer> mostActiveClient = new HashMap<Integer,Integer>();
    private ArrayList<String> rentalOfMostActive = new ArrayList<String>();



    public List<RentAction> getAllRentals(){
        log.trace("getAllRentals - method entered");
        Iterable<RentAction> rentals = repo.findAll();
        log.trace("getAllRentals - method finished");
        return (List<RentAction>) rentals;
    }

    public void addRental(RentAction rentalToAdd) throws Exception {
        try {

            log.trace("addRental - method entered with rental",rentalToAdd);

            int clientID = rentalToAdd.getClientId();
            int movieID = rentalToAdd.getMovieId();

            Optional<Client> c = clientController.getById(clientID);
            Optional<Movie> m = movieController.getById(movieID);

            if(!c.isPresent())
                throw new ValidatorException("Client does not exist");

            if(!m.isPresent())
                throw new ValidatorException("Movie does not exist");

            validator.validate(rentalToAdd);

            repo.save(rentalToAdd);
            updateReports(rentalToAdd);

            log.trace("addRental - method finished");

        } catch (ValidatorException v) {
            throw new ValidatorException(v.getMessage());}
        catch (RentalException r){
            throw new RentalException(r.getMessage());
        }
    }

    @Transactional
    public void updateRental(RentAction rent) throws Exception {
        try{
            log.trace("updateRental - method entered with rental:",rent);
            int clientID = rent.getClientId();
            int movieID = rent.getMovieId();

            Optional<Client> c = clientController.getById(clientID);
            Optional<Movie> m = movieController.getById(movieID);

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
            } catch(Exception e)
            {
                throw new Exception(e.getMessage());
            }

            updateReports(rent);

        } catch (ValidatorException v) {
            throw new ValidatorException(v.getMessage());}
        catch (RentalException r){
                throw new RentalException(r.getMessage());
        }
    }


    public List<Integer> getMostActiveClient()
    {
        log.trace("getMostActiveClient - method entered");

        Map<Integer, Integer> mostActive = mostActiveClient.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                        LinkedHashMap::new));

        System.out.println(mostActive);
        log.trace("getMostActiveClient - method finished");
        return new ArrayList<>(mostActive.keySet());
    }

    public List<Integer> getMostRentedMovie()
    {
        log.trace("getMostRentedMovie - method entered");

        Map<Integer, Integer> mostRented = mostRentedMovie.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                        LinkedHashMap::new));

        System.out.println(mostRented);

        log.trace("getMostRentedMovie - method finished");
        return new ArrayList<>(mostRented.keySet());

    }

    public List<String> getRentedMoviesOfMostActiveClient() throws Exception{

        log.trace("getRentedMovieOfMostActiveClient - method entered");

        List<Integer> mostActive = getMostActiveClient();
        int clientId = mostActive.get(mostActive.size()-1);
        List<String> all =  (repo.findAll()).stream()
                .filter(e->e.getClientId()==clientId)
                .map(ra-> {
                    try {
                        return movieController.getById(ra.getMovieId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .filter(Optional::isPresent)
                .map(o->o.get().getTitle())
                .collect(Collectors.toList());

        log.trace("getRentedMovieOfMostActiveClient - method finished");
        return new ArrayList<String>(all);
    }

    public void updateTheReports() throws Exception {
        mostRentedMovie = new HashMap<Integer,Integer>();
        mostActiveClient = new HashMap<Integer,Integer>();
        rentalOfMostActive = new ArrayList<String>();
        List<RentAction> rents =  repo.findAll();
        for(RentAction r: rents){
            try{
                updateReports(r);
            }
            catch (SQLException e){
                throw new SQLException();
            }
        }
    }

    private void updateReports(RentAction rentalToAdd) throws Exception {
        int clientKey = rentalToAdd.getClientId();
        int movieKey = rentalToAdd.getMovieId();
        int clientAmount=0,movieAmount=0;
        if(mostActiveClient.containsKey(clientKey))
        {
            clientAmount = mostActiveClient.get(clientKey);
            mostActiveClient.replace(clientKey, clientAmount + 1);
        }
        else
            mostActiveClient.putIfAbsent(clientKey,1);

        if(mostRentedMovie.containsKey(movieKey))
        {
            movieAmount = mostRentedMovie.get(movieKey);
            mostRentedMovie.replace(movieKey, movieAmount + 1);
        }
        else
            mostRentedMovie.putIfAbsent(movieKey,1);

         rentalOfMostActive = (ArrayList<String>) getRentedMoviesOfMostActiveClient();
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
        clientController.deleteClient(clientToDelete);
        Iterable<RentAction> rentals = repo.findAll();
        rentals.forEach(Rent->{
            if(Rent.getClientId() == clientToDelete){
                this.deleteRent(Rent.getId());
            }
        });
    }

    public void deleteMovie(Integer movieToDelete) throws ValidatorException, SQLException {
        movieController.deleteMovie(movieToDelete);
        Iterable<RentAction> rentals = repo.findAll();
        rentals.forEach(Rent->{
            if(Rent.getMovieId() == movieToDelete){
                this.deleteRent(Rent.getId());
            }
        });
    }



}
