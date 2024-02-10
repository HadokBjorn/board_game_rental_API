package com.board_game_rental.api.units;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.board_game_rental.api.dtos.GameDto;
import com.board_game_rental.api.exceptions.ConflictException;
import com.board_game_rental.api.models.GameModel;
import com.board_game_rental.api.repositories.GameRepository;
import com.board_game_rental.api.services.GameService;

@SpringBootTest
class GameUnitTest {
    @InjectMocks
    private GameService gameService;

    @Mock
    private GameRepository gameRepository;
    //Create Tests
    @Test
    void givenRepeatedGame_whenCreatingGame_thenThrowError(){
        //given
        GameDto game = new GameDto("name","http://image.png", 3L, 1500L);
        doReturn(true).when(this.gameRepository).existsByName(game.getName());
        //when
        ConflictException exception = assertThrows(
            ConflictException.class,
            () -> this.gameService.create(game)
            );
        //then
        verify(this.gameRepository, times(1)).existsByName(game.getName());
        verify(this.gameRepository, times(0)).save(any());
        assertEquals("Game with name " + game.getName() + " already exists", exception.getMessage());
    }

    @Test
    void givenCorrectGame_whenCreatingGame_thenCreateGame(){
        //given
        GameDto game = new GameDto("name","http://image.png", 3L, 1500L);
        GameModel newGame = new GameModel(game);
        doReturn(false).when(this.gameRepository).existsByName(game.getName());
        doReturn(newGame).when(this.gameRepository).save(any());
        //when
        GameModel result = this.gameService.create(game);
        //then
        verify(this.gameRepository, times(1)).existsByName(game.getName());
        verify(this.gameRepository, times(1)).save(any());
        assertEquals(newGame, result);
    }

    //Get tests
    @Test
    void givenVoid_whenGettingGames_thenReturnListGames(){
        //given
        GameDto gameOne = new GameDto("name one","http://image1.png", 3L, 1500L);
        GameDto gameTwo = new GameDto("name two","http://image2.png", 4L, 1900L);
        GameModel newGameOne = new GameModel(gameOne);
        GameModel newGameTwo = new GameModel(gameTwo);
        List<GameModel> listGames = List.of(newGameOne, newGameTwo);
        doReturn(listGames).when(this.gameRepository).findAll();
        //when
        List<GameModel> result = this.gameService.findAll();
        //then
        verify(this.gameRepository, times(1)).findAll();
        assertEquals(listGames, result);
        assertEquals(listGames.size(), 2);
    }

    @Test
    void givenVoid_whenGettingGames_thenReturnEmptyListGames(){
        //given
        List<GameModel> listGames = List.of();
        doReturn(listGames).when(this.gameRepository).findAll();
        //when
        List<GameModel> result = this.gameService.findAll();
        //then
        verify(this.gameRepository, times(1)).findAll();
        assertEquals(listGames, result);
        assertEquals(listGames.size(), 0);
    }
}
