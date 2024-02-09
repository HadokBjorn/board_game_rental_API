package com.board_game_rental.api.services;

import org.springframework.stereotype.Service;

import com.board_game_rental.api.dtos.CustomerDto;
import com.board_game_rental.api.exceptions.ConflictException;
import com.board_game_rental.api.exceptions.NotFoundException;
import com.board_game_rental.api.models.CustomerModel;
import com.board_game_rental.api.repositories.CustomerRepository;

@Service
public class CustomerService {
    final CustomerRepository customerRepository;
    CustomerService(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }

    public CustomerModel create(CustomerDto dto){
        boolean cpfAlreadyExists = customerRepository.existsByCpf(dto.getCpf());
        if(cpfAlreadyExists) throw new ConflictException("Customer with cpf "+dto.getCpf()+" already exists");
        CustomerModel customer = new CustomerModel(dto);
        return this.customerRepository.save(customer);
    }

    public CustomerModel findById(Long id){
        CustomerModel customer = this.customerRepository.findById(id).orElseThrow(
            () -> new NotFoundException("Customer with id "+id+" not found")
        );
        return customer; 
    }
}
