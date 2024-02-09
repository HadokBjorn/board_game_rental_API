package com.board_game_rental.api.services;

import java.util.List;

import org.springframework.stereotype.Service;
import com.board_game_rental.api.dtos.GameDto;
import com.board_game_rental.api.exceptions.ConflictException;
import com.board_game_rental.api.models.GameModel;
import com.board_game_rental.api.repositories.GameRepository;

@Service
public class GameService {
    final GameRepository gameRepository;
    GameService(GameRepository gameRepository){
        this.gameRepository = gameRepository;
    }

    public List<GameModel> findAll(){
        return gameRepository.findAll();
    }

    public GameModel create(GameDto dto){
        boolean gameWithSameName = this.gameRepository.existsByName(dto.getName());
        if (gameWithSameName) {
            throw new ConflictException("Game with name " + dto.getName() + " already exists");
        }
        GameModel game = new GameModel(dto);
        return this.gameRepository.save(game);
    }
}
