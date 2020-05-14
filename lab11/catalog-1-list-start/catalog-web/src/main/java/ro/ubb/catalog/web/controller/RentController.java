package ro.ubb.catalog.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.ubb.catalog.core.service.RentalService;
import ro.ubb.catalog.web.converter.RentConverter;
import ro.ubb.catalog.web.dto.ClientDto;
import ro.ubb.catalog.web.dto.RentDto;
import ro.ubb.catalog.web.dto.RentsDto;

import java.util.List;

@RestController
public class RentController {

    public static final Logger log= LoggerFactory.getLogger(RentController.class);

    @Autowired
    private RentalService rentalService;

    @Autowired
    private RentConverter rentConverter;

    @RequestMapping(value = "/rents", method = RequestMethod.GET)
    List<RentDto> getRents(){

        log.trace("getRents - method entered - web");
        return rentConverter.convertModelsToDtos(rentalService.getAllRentals());
    }

    @RequestMapping(value = "/rents", method = RequestMethod.POST)
    RentDto addRent(@RequestBody RentDto rentDto) throws Exception {

        System.out.println(rentDto.toString());
        log.trace("addRent - method entered - web");
        return rentConverter.convertModelToDto(rentalService.addRental(
                rentConverter.convertDtoToModel(rentDto)
        ));
    }

    @RequestMapping(value = "/rents/{id}", method = RequestMethod.PUT)
    RentDto updateClient(@RequestBody RentDto rentDto) throws Exception
    {

        log.trace("updateRent - method entered - web");
        return rentConverter.convertModelToDto( rentalService.updateRental(
                rentConverter.convertDtoToModel(rentDto)));
    }

    @RequestMapping(value = "/rents/{id}", method = RequestMethod.DELETE)
    ResponseEntity<?> deleteRent(@PathVariable Integer id){

        log.trace("deleteRent - method entered - web");
        rentalService.deleteRent(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }


}
