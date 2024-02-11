package com.board_game_rental.api.integrationTests;


import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.board_game_rental.api.dtos.CustomerDto;
import com.board_game_rental.api.models.CustomerModel;
import com.board_game_rental.api.repositories.CustomerRepository;
import com.board_game_rental.api.repositories.GameRepository;
import com.board_game_rental.api.repositories.RentalRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CustomerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RentalRepository rentalRepository;

    @AfterEach
    void cleanUpDatabase() {
        this.rentalRepository.deleteAll();
        this.customerRepository.deleteAll();
        this.gameRepository.deleteAll();
    }

    //post a customer
    @Test
    void givenCustomerDto_whenCreatingCustomer_thenReturnCustomer(){
        //Given
        CustomerDto customerDto = new CustomerDto("name","12345678911");
        HttpEntity<CustomerDto> body = new HttpEntity<>(customerDto);
        //when
        ResponseEntity<CustomerModel> response = restTemplate.exchange(
            "/customers",
            HttpMethod.POST,
            body,
            CustomerModel.class);
        //then
        Optional<CustomerModel> createdCustomer = this.customerRepository.findById(response.getBody().getId());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(Optional.of(response.getBody()), createdCustomer);
        assertEquals(this.customerRepository.count(), 1);
    }

    @Test
    void givenRepeatedCustomerDto_whenCreatingCustomer_thenThrowError(){
        //Given
        CustomerDto customerDto = new CustomerDto("name","12345678911");
        CustomerModel customerModel = new CustomerModel(customerDto);
        CustomerModel createdCustomer = this.customerRepository.save(customerModel);
        HttpEntity<CustomerDto> body = new HttpEntity<>(customerDto);
        //when
        ResponseEntity<String> response = restTemplate.exchange(
            "/customers", 
            HttpMethod.POST,
            body, 
            String.class);
        //then
        List<CustomerModel> customers = this.customerRepository.findAll();
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(this.customerRepository.count(), 1);
        assertEquals(createdCustomer, customers.get(0));
    }

    @Test
    void givenVoid_whenGettingCustomerById_thenReturnCustomer(){
        //Given
        CustomerDto customerDto = new CustomerDto("name","12345678911");
        CustomerModel customerModel = new CustomerModel(customerDto);
        CustomerModel createdCustomer = this.customerRepository.save(customerModel);
        //when
        // Cria um objeto RequestEntity com a URL da rota e o método HTTP GET
        RequestEntity<Void> requestEntity = RequestEntity.get("/customers/"+createdCustomer.getId()).build();
        // Executa a solicitação e obtem a resposta como ResponseEntity<List<GameModel>>
        ResponseEntity<CustomerModel> response = restTemplate.exchange(
            requestEntity,
            CustomerModel.class
        );
        //then
        Optional<CustomerModel> customerFromRepository = this.customerRepository.findById(createdCustomer.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(this.customerRepository.count(), 1);
        assertEquals(Optional.of(response.getBody()), customerFromRepository);
    }

    @Test
    void givenVoid_whenGettingCustomerById_thenThrowError(){
        //Given
        CustomerDto customerDto = new CustomerDto("name","12345678911");
        CustomerModel customerModel = new CustomerModel(customerDto);
        CustomerModel createdCustomer = this.customerRepository.save(customerModel);
        this.customerRepository.deleteById(createdCustomer.getId());
        //when
        // Cria um objeto RequestEntity com a URL da rota e o método HTTP GET
        RequestEntity<Void> requestEntity = RequestEntity.get("/customers/"+createdCustomer.getId()).build();
        // Executa a solicitação e obtem a resposta como ResponseEntity<List<GameModel>>
        ResponseEntity<String> response = restTemplate.exchange(
            requestEntity,
            String.class
        );
        //then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(this.customerRepository.count(), 0);
        assertEquals("Customer with id "+createdCustomer.getId()+" not found", response.getBody());
    }
}