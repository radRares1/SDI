package ro.ubb.catalog.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.ubb.catalog.core.service.ClientService;
import ro.ubb.catalog.web.converter.ClientConverter;
import ro.ubb.catalog.web.dto.ClientDto;
import ro.ubb.catalog.web.dto.ClientsDto;

import java.util.List;

@RestController
public class ClientController {

    public static final Logger log= LoggerFactory.getLogger(ClientController.class);

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientConverter clientConverter;

    @RequestMapping(value = "/clients",method = RequestMethod.GET)
    List<ClientDto> getClients(){
        log.trace("getClients - method entered - web");
        return  clientConverter.convertModelsToDtos(clientService.getAllClients());
    }

    @RequestMapping(value = "/clients", method = RequestMethod.POST)
    ClientDto addClient(@RequestBody ClientDto clientDto) {
        log.trace("addClient - method entered - web");
        return clientConverter.convertModelToDto(clientService.addClient(
                clientConverter.convertDtoToModel(clientDto)
        ));
    }


    @RequestMapping(value = "/clients/{id}", method = RequestMethod.PUT)
    ClientDto updateClient(@RequestBody ClientDto clientDto) throws Exception
    {

        log.trace("updateClient - method entered - web");
        return clientConverter.convertModelToDto( clientService.updateClient(
                clientConverter.convertDtoToModel(clientDto)));
    }

    @RequestMapping(value = "/clients/{id}", method = RequestMethod.DELETE)
    ResponseEntity<?> deleteClient(@PathVariable Integer id){


        log.trace("deleteClient - method entered - web");
        clientService.deleteClient(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @RequestMapping(value = "/clients/filter", method = RequestMethod.GET)
    List<ClientDto> filterOddId() throws Exception {


        log.trace("filterOddClients - method entered - web");
        return clientConverter.convertModelsToDtos(clientService.filterOddId());
    }

    @RequestMapping(value = "/clients/filter", method = RequestMethod.POST)
    List<ClientDto> filterClientsWithNameLessThan(@RequestBody Integer length) throws Exception {


        log.trace("filterClientsWithNameLessThan - method entered - web");
        return clientConverter.convertModelsToDtos(clientService.filterClientsWithNameLessThan(length));
    }



}
