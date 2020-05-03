package clientBase.Ui;

import coreBase.Entities.Client;
import coreBase.Entities.Movie;
import coreBase.Entities.RentAction;
import coreBase.Validators.RentalException;
import coreBase.Validators.ValidatorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import webBase.dto.*;

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


    private RestTemplate rest;
    public static final String ClientURL = "http://localhost:8080/api/clients";
    public static final String MovieURL = "http://localhost:8080/api/movies";
    public static final String RentURL = "http://localhost:8080/api/rents";


    public UserInterface(RestTemplate restTemplate) {
        rest = restTemplate;
    }

    public void runConsole() throws Exception {


        while (true) {
            System.out.println("Commands: ");
            System.out.println(" 1 - add Client |  2 - show Clients | 3 - Update Client | 4 - delete Client");
            System.out.println(" 5 - add Movie | 6 - show Movies | 7 - Update Movie | 8 - delete Movie");
            System.out.println(" 9 - add Rent | 10 - show Rents | 11 - Update Rent | 12 - delete Rent");
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


    }

    private void printAllClients() throws Exception {
        ClientsDto allClients = rest.getForObject(ClientURL, ClientsDto.class);
        List<ClientDto> list = allClients.getClientDtos();
        list.forEach(System.out::println);
    }

    private void printAllMovies() throws Exception {

        MoviesDto allMovies = rest.getForObject(MovieURL,MoviesDto.class);
        List<MovieDto> list = allMovies.getMovieDtos();
        list.forEach(System.out::println);

    }

    private void printAllRents() throws SQLException {

        RentsDto allRents = rest.getForObject(RentURL,RentsDto.class);
        List<RentDto> list = allRents.getRentsDtos();
        list.forEach(System.out::println);
    }


    private void addClient() {

        ClientDto client = readClient();
        try {
            ClientDto clientToSave = rest.postForObject(
                    ClientURL,
                    client,
                    ClientDto.class);
            System.out.println("Client" + clientToSave.toString() + "was added");
        } catch (ValidatorException e) {
            System.out.println(e.getMessage());
        }

    }

    private void addMovie() {

        MovieDto movie = readMovie();
        try {
            MovieDto movieToSave = rest.postForObject(MovieURL,movie,MovieDto.class);

            System.out.println("Movie" + movieToSave.toString() + "was added");
        } catch (ValidatorException e) {
            System.out.println(e.getMessage());
        }

    }

    private void addRent() throws Exception {

        RentDto rent = readRent();
        try {
            RentDto rentSave = rest.postForObject(RentURL,rent,RentDto.class);
            System.out.println("Rent" + rentSave.toString() + "was added");

        } catch (RentalException e) {
            System.out.println(e.getMessage());
        }

    }

    private ClientDto readClient() {
        System.out.println("Read client {name, age}");

        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {

            String name = bufferRead.readLine();
            int age = Integer.parseInt(bufferRead.readLine());// ...

            return new ClientDto(name, age);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private MovieDto readMovie() {
        System.out.println("Read movie {title, price}");

        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {

            String title = bufferRead.readLine();
            int price = Integer.parseInt(bufferRead.readLine());// ...

            return new MovieDto( title, price);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private RentDto readRent() {
        System.out.println("Read rental {clientId, movieId}");

        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {

            int id1 = Integer.parseInt(bufferRead.readLine());// ...
            int id2 = Integer.parseInt(bufferRead.readLine());// ...

            return new RentDto(id1, id2);
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

            rest.delete(ClientURL + "/{id}",id);

        } catch (IOException  e) {
            e.printStackTrace();
        }
    }

    private void deleteMovie() {
        System.out.println("Type movie id you want to delete: ");
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));

        try {

            int id = Integer.parseInt(bufferRead.readLine());
            rest.delete(MovieURL+ "/{id}",id);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteRent() {
        System.out.println("Type rent id you want to delete: ");
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));

        try {
            int id = Integer.parseInt(bufferRead.readLine());

            rest.delete(RentURL + "/{id}",id);
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

            ClientDto newCLient = new ClientDto(name, age);
            newCLient.setId(id);

            rest.put(ClientURL , newCLient);

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
            MovieDto movie = new MovieDto(title, price);
            movie.setId(id);

            rest.put(MovieURL,movie);


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
            RentDto rent = new RentDto(id1, id2);
            rent.setId(id);

            rest.put(RentURL,rent);

        } catch (ValidatorException e) {
            System.out.println(e.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;

    }

    private void filterOddIdClient() throws Exception {


        String URL2 = "http://localhost:8080/api/clients/filter";
        ClientsDto allClients = rest.getForObject(URL2, ClientsDto.class);
        assert allClients != null;
        List<ClientDto> list = allClients.getClientDtos();
        list.forEach(System.out::println);

    }

    private void filterEvenMovie() throws Exception {
       String URL2 = "http://localhost:8080/api/movies/filter";
       MoviesDto allMovies = rest.getForObject(URL2, MoviesDto.class);
       assert allMovies !=null;
       List<MovieDto> list = allMovies.getMovieDtos();
       list.forEach(System.out::println);
    }

    private void filterMovieByNameLength() throws Exception {
        System.out.println("Type title length: ");

        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));


        try {
            int length = Integer.parseInt(bufferRead.readLine());// ...

            String URL2 = "http://localhost:8080/api/movies/filter";
            MoviesDto allClients = rest.postForObject(URL2,length,MoviesDto.class);
            List<MovieDto> list = allClients.getMovieDtos();
            list.forEach(System.out::println);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void filterClientByNameLength() throws Exception {
        System.out.println("Type name length: ");
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            int length = Integer.parseInt(bufferRead.readLine());// ...

            String URL2 = "http://localhost:8080/api/clients/filter";
            ClientsDto allClients = rest.postForObject(URL2,length,ClientsDto.class);
            List<ClientDto> list = allClients.getClientDtos();
            list.forEach(System.out::println);
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}


