package com.board_game_rental.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.board_game_rental.api.models.GameModel;

@Repository
public interface GameRepository extends JpaRepository<GameModel, Long> {
    boolean existsByName(String name);
}
