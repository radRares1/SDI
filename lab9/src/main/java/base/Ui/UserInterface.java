package base.Ui;

import base.Controller.ClientController;
import base.Controller.MovieController;
import base.Controller.RentalController;
import base.Entities.Client;
import base.Entities.Movie;
import base.Entities.RentAction;
import base.Validators.RentalException;
import base.Validators.ValidatorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Rares2.
 */
@Component
public class UserInterface {

    @Autowired
    private ClientController clientController;
    @Autowired
    private MovieController movieController;
    @Autowired
    private RentalController rentalController;


    public void runConsole() throws Exception {


        while (true) {
            System.out.println("Commands: ");
            System.out.println(" 1 - add Client |  2 - show Clients | 3 - Update Client | 4 - delete Client");
            System.out.println(" 5 - add Movie | 6 - show Movies | 7 - Update Movie | 8 - delete Movie");
            System.out.println(" 9 - add Rent | 10 - show Rents | 11 - Update Rents | 12 - delete Rents");
            System.out.println(" 13 - filter client by odd Id");
            System.out.println(" 14 - filter movie by title length");
            System.out.println(" 15 - filter movie by even ID");
            System.out.println(" 16 - filter client by name length");
            System.out.println(" 17 - reports");
            System.out.println(" 0 - exit");
            System.out.println("Input command: ");
            BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));

            try {
                int command = Integer.parseInt(bufferRead.readLine());
                if (command == 1) {
                    addClient();
                } else if (command == 2) {
                    printAllClients();
                } else if (command == 3) {
                    updateClient();
                } else if (command == 4) {
                    deleteClient();
                } else if (command == 5) {
                    addMovie();
                } else if (command == 6) {
                    printAllMovies();
                } else if (command == 7) {
                    updateMovie();
                } else if (command == 8) {
                    deleteMovie();
                } else if (command == 9) {
                    addRent();
                } else if (command == 10) {
                    printAllRents();
                } else if (command == 11) {
                    updateRental();
                } else if (command == 12) {
                    deleteRent();
                } else if (command == 13) {
                    filterOddIdClient();
                } else if (command == 14) {
                    filterMovieByNameLength();
                } else if (command == 15) {
                    filterEvenMovie();
                } else if (command == 16) {
                    filterClientByNameLength();
                } else if (command == 17) {
                    getReports();
                } else break;

            } catch (IOException | SQLException ex) {
                ex.printStackTrace();
            }

        }
    }

    private void getReports() throws Exception {

        rentalController.updateTheReports();
        try {

            List<Integer> mostActive = (List<Integer>) rentalController.getMostActiveClient();
            System.out.println("The most active client is:" + Integer.toString((Integer) mostActive.get(mostActive.size() - 1)));

            List<Integer> mostRented = (List<Integer>) rentalController.getMostRentedMovie();
            System.out.println("The most rented movie is:" + Integer.toString((Integer) mostRented.get(mostRented.size() - 1)));

            List<String> repeatedRentals = rentalController.getRentedMoviesOfMostActiveClient();
            System.out.println("The movies of the most active client are:");
            System.out.println(repeatedRentals);
        }
        catch (Exception e)
        {
            System.out.println("Empty reports");
        }

    }

    private void printAllClients() throws Exception {
        List<Client> clients = clientController.getAllClients();
        clients.forEach(System.out::println);
    }

    private void printAllMovies() throws Exception {
        List<Movie> students = movieController.getAllMovies();
        students.forEach(System.out::println);
    }

    private void printAllRents() throws SQLException {
        List<RentAction> rents = rentalController.getAllRentals();
        rents.forEach(System.out::println);
    }


    private void addClient() {

        Client client = readClient();
        try {
            clientController.addClient(client);
        } catch (ValidatorException e) {
            System.out.println(e.getMessage());
        }

    }

    private void addMovie() {

        Movie movie = readMovie();
        try {
            movieController.addMovie(movie);
        } catch (ValidatorException e) {
            System.out.println(e.getMessage());
        }

    }

    private void addRent() throws Exception {

        RentAction rent = readRent();
        try {
            assert rent != null;
            rentalController.addRental(rent);
        } catch (RentalException e) {
            System.out.println(e.getMessage());
        }

    }

    private Client readClient() {
        System.out.println("Read client {name, age}");

        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {

            String name = bufferRead.readLine();
            int age = Integer.parseInt(bufferRead.readLine());// ...

            return new Client(name, age);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private Movie readMovie() {
        System.out.println("Read movie {title, price}");

        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {

            String title = bufferRead.readLine();
            int price = Integer.parseInt(bufferRead.readLine());// ...

            return new Movie( title, price);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private RentAction readRent() {
        System.out.println("Read rental {clientId, movieId}");

        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {

            int id1 = Integer.parseInt(bufferRead.readLine());// ...
            int id2 = Integer.parseInt(bufferRead.readLine());// ...

            return new RentAction(id1, id2);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * deletes a client
     */
    private void deleteClient() {
        System.out.println("Type client id you want to delete: ");
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));

        try {
            int id = Integer.parseInt(bufferRead.readLine());
            rentalController.deleteClient(id);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteMovie() {
        System.out.println("Type movie id you want to delete: ");
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));

        try {
            int id = Integer.parseInt(bufferRead.readLine());
            rentalController.deleteMovie(id);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteRent() {
        System.out.println("Type rent id you want to delete: ");
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));

        try {
            int id = Integer.parseInt(bufferRead.readLine());
            rentalController.deleteRent(id);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Client updateClient() {
        System.out.println("Read new clients attributes {id,name, age}");

        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            int id = Integer.parseInt(bufferRead.readLine());// ...
            String name = bufferRead.readLine();
            int age = Integer.parseInt(bufferRead.readLine());// ...

            Client newCLient = new Client(name, age);
            newCLient.setId(id);

            clientController.updateClient(newCLient);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;

    }

    private Movie updateMovie() throws Exception {
        System.out.println("Read new movie attributes {id,title,price}");

        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            int id = Integer.parseInt(bufferRead.readLine());// ...
            String title = bufferRead.readLine();
            int price = Integer.parseInt(bufferRead.readLine());// ...
            Movie movie = new Movie(title, price);
            movie.setId(id);

            movieController.updateMovie(movie);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;

    }

    private Client updateRental() {
        System.out.println("Read new rental {id,clientId, movieId}");

        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            int id = Integer.parseInt(bufferRead.readLine());// ...
            int id1 = Integer.parseInt(bufferRead.readLine());// ...
            int id2 = Integer.parseInt(bufferRead.readLine());// ...
            RentAction rent = new RentAction(id1, id2);
            rent.setId(id);

            rentalController.updateRental(rent);
        } catch (ValidatorException e) {
            System.out.println(e.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;

    }

    private void filterOddIdClient() throws Exception {
        List<Client> filtered = clientController.filterOddId();

        filtered.forEach(System.out::println);
    }

    private void filterEvenMovie() throws Exception {
        List<Movie> filtered = movieController.filterEvenId();
        filtered.forEach(System.out::println);


    }

    private void filterMovieByNameLength() throws Exception {
        System.out.println("Type title length: ");

        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        List<Movie> filtered = movieController.filterMoviesWithTitleLessThan(Integer.parseInt(bufferRead.readLine()));
        filtered.forEach(System.out::println);
    }

    private void filterClientByNameLength() throws Exception {
        System.out.println("Type name length: ");
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        List<Client> filtered = clientController.filterClientsWithNameLessThan(Integer.parseInt(bufferRead.readLine()));
        filtered.forEach(System.out::println);
    }
}


