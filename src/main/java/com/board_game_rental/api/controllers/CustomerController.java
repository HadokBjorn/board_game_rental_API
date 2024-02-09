package com.board_game_rental.api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.board_game_rental.api.dtos.CustomerDto;
import com.board_game_rental.api.models.CustomerModel;
import com.board_game_rental.api.services.CustomerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    final CustomerService customerService;
    CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<CustomerModel> crateCustomer(@RequestBody @Valid CustomerDto body){
        CustomerModel customer = this.customerService.create(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(customer);
    }
    @GetMapping("/{id}")
    public ResponseEntity<CustomerModel> getCustomerById(@PathVariable("id") Long id){
        CustomerModel customer = this.customerService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(customer);
    }
}
