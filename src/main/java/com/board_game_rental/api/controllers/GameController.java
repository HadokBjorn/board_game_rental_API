package com.board_game_rental.api.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.board_game_rental.api.dtos.GameDto;
import com.board_game_rental.api.models.GameModel;
import com.board_game_rental.api.services.GameService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/games")
public class GameController {

    final GameService gameService;
    
    GameController(GameService gameService){
        this.gameService = gameService;
    }

    @PostMapping
    public ResponseEntity<GameModel> createGame(@RequestBody @Valid GameDto body){
        GameModel game = this.gameService.create(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(game);
    }

    @GetMapping
    public ResponseEntity<List<GameModel>> getGames(){
        List<GameModel> games = this.gameService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(games);
    }
}
