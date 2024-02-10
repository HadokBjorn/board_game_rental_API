package com.board_game_rental.api.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.board_game_rental.api.dtos.RentalDto;
import com.board_game_rental.api.models.RentalModel;
import com.board_game_rental.api.services.RentalService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/rentals")
public class RentalController {
    final RentalService rentalService;
    RentalController(RentalService rentalService){
        this.rentalService = rentalService;
    }
    @PostMapping
    public ResponseEntity<RentalModel> createRental(@RequestBody @Valid RentalDto body){
        RentalModel rental = this.rentalService.create(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(rental);
    }
    @GetMapping
    public ResponseEntity<List<RentalModel>> getRentals(){
        List<RentalModel> rentals = this.rentalService.findAll();
        return ResponseEntity.ok(rentals);
    }
    @PutMapping("/{id}/return")
    public ResponseEntity<RentalModel> updateRental(@PathVariable("id") Long id){
        RentalModel updatedRental = this.rentalService.update(id);
        return ResponseEntity.ok(updatedRental);
    }
}
