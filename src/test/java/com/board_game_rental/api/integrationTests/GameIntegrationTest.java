package com.board_game_rental.api.integrationTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.board_game_rental.api.dtos.GameDto;
import com.board_game_rental.api.exceptions.ConflictException;
import com.board_game_rental.api.exceptions.NotFoundException;
import com.board_game_rental.api.models.GameModel;
import com.board_game_rental.api.repositories.CustomerRepository;
import com.board_game_rental.api.repositories.GameRepository;
import com.board_game_rental.api.repositories.RentalRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class GameIntegrationTest {
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

    //post a game
    @Test
    void givenGameDto_whenCreatingGame_thenReturnGame(){
        //Given
        GameDto gameDto = new GameDto("name","http://image.png", 3L, 1500L);
        HttpEntity<GameDto> body = new HttpEntity<>(gameDto);
        //when
        ResponseEntity<GameModel> response = restTemplate.exchange(
            "/games",
            HttpMethod.POST,
            body,
            GameModel.class);
        //then
        Optional<GameModel> createdGame = this.gameRepository.findById(response.getBody().getId());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(Optional.of(response.getBody()), createdGame);
        assertEquals(this.gameRepository.count(), 1);
    }

    @Test
    void givenRepeatedGameDto_whenCreatingGame_thenThrowError(){
        //Given
        GameDto gameDto = new GameDto("name","http://image.png", 3L, 1500L);
        GameModel gameModel = new GameModel(gameDto);
        GameModel createdGame = this.gameRepository.save(gameModel);
        HttpEntity<GameDto> body = new HttpEntity<>(gameDto);
        //when
        ResponseEntity<String> response = restTemplate.exchange(
            "/games", 
            HttpMethod.POST,
            body, 
            String.class);
        //then
        List<GameModel> games = this.gameRepository.findAll();
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(this.gameRepository.count(), 1);
        assertEquals(createdGame, games.get(0));
    }

    @Test
    void givenVoid_whenGettingGame_thenReturnGameList(){
        //Given
        GameDto gameDtoOne = new GameDto("name one","http://imageOne.png", 3L, 1500L);
        GameDto gameDtoTwo = new GameDto("name two","http://imageTwo.png", 4L, 1800L);
        GameModel gameModelOne = new GameModel(gameDtoOne);
        GameModel gameModelTwo = new GameModel(gameDtoTwo);
        this.gameRepository.save(gameModelOne);
        this.gameRepository.save(gameModelTwo);
        //when
        // Cria um objeto RequestEntity com a URL da rota e o método HTTP GET
        RequestEntity<Void> requestEntity = RequestEntity.get("/games").build();
        // Executa a solicitação e obtem a resposta como ResponseEntity<List<GameModel>>
        ResponseEntity<List<GameModel>> response = restTemplate.exchange(
                requestEntity,
                new ParameterizedTypeReference<List<GameModel>>() {} // Tipo de resposta
        );
        //then
        List<GameModel> gamesFromRepository = this.gameRepository.findAll();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(this.gameRepository.count(), 2);
        assertEquals(response.getBody(), gamesFromRepository);
    }

    @Test
    void givenVoid_whenGettingGame_thenReturnEmptyList(){
        //Given
        
        //when
        // Cria um objeto RequestEntity com a URL da rota e o método HTTP GET
        RequestEntity<Void> requestEntity = RequestEntity.get("/games").build();
        // Executa a solicitação e obtem a resposta como ResponseEntity<List<GameModel>>
        ResponseEntity<List<GameModel>> response = restTemplate.exchange(
                requestEntity,
                new ParameterizedTypeReference<List<GameModel>>() {} // Tipo de resposta
        );
        //then
        List<GameModel> gamesFromRepository = this.gameRepository.findAll();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(this.gameRepository.count(), 0);
        assertEquals(response.getBody(), gamesFromRepository);
    }
}
