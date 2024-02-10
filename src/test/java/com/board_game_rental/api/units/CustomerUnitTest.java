package com.board_game_rental.api.units;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.board_game_rental.api.dtos.CustomerDto;
import com.board_game_rental.api.exceptions.ConflictException;
import com.board_game_rental.api.exceptions.NotFoundException;
import com.board_game_rental.api.models.CustomerModel;
import com.board_game_rental.api.repositories.CustomerRepository;
import com.board_game_rental.api.services.CustomerService;

@SpringBootTest
class CustomerUnitTest {
    @InjectMocks
    private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;
    
    //  create tests
    @Test
    void givenCustomerDto_whenCreating_thenCrateCustomer(){
        //given
        CustomerDto customerDto = new CustomerDto("name", "012345678911");
        CustomerModel customer = new CustomerModel(customerDto);
        doReturn(false).when(this.customerRepository).existsByCpf(customerDto.getCpf());
        doReturn(customer).when(this.customerRepository).save(any());
        //when
        CustomerModel result = this.customerService.create(customerDto);
        //then
        verify(this.customerRepository, times(1)).existsByCpf(customerDto.getCpf());
        verify(this.customerRepository, times(1)).save(any());
        assertEquals(customer, result);
    }
    @Test
    void givenRepeatedCustomer_whenCreating_thenThrowError(){
        //given
        CustomerDto customerDto = new CustomerDto("name", "012345678911");
        doReturn(true).when(this.customerRepository).existsByCpf(customerDto.getCpf());
        //when
        ConflictException result = assertThrows(
            ConflictException.class,
            () -> this.customerService.create(customerDto)
        );
        //then
        verify(this.customerRepository, times(1)).existsByCpf(customerDto.getCpf());
        verify(this.customerRepository, times(0)).save(any());
        assertEquals("Customer with cpf "+customerDto.getCpf()+" already exists", result.getMessage());
    }

    //  Get By Id Tests
    @Test
    void givenVoid_whenGettingById_thenReturnCustomer(){
        //given
        CustomerDto customerDto = new CustomerDto("name", "012345678911");
        CustomerModel customer = new CustomerModel(customerDto);
        doReturn(Optional.of(customer)).when(this.customerRepository).findById(customer.getId());
        //when
        CustomerModel result =  this.customerService.findById(customer.getId());
        //then
        verify(this.customerRepository, times(1)).findById(customer.getId());
        assertEquals(customer, result);
    }

    @Test
    void givenVoid_whenGettingById_thenThrowError(){
        //given
        CustomerDto customerDto = new CustomerDto("name", "012345678911");
        CustomerModel customer = new CustomerModel(customerDto);
        doReturn(Optional.empty()).when(this.customerRepository).findById(customer.getId());
        //when
        NotFoundException result = assertThrows(
            NotFoundException.class,
            () -> this.customerService.findById(customer.getId())
        );
        //then
        verify(this.customerRepository, times(1)).findById(customer.getId());
        assertEquals("Customer with id "+customer.getId()+" not found", result.getMessage());
    }
}
