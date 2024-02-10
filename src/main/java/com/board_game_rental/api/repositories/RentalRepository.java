package com.board_game_rental.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.board_game_rental.api.models.RentalModel;

@Repository
public interface RentalRepository extends JpaRepository<RentalModel, Long>{
    @Query(value = "SELECT COUNT(*) FROM rentals WHERE game_id = :gameId AND return_date ISNULL",nativeQuery = true)
    Long rentedGame(@Param("gameId") Long gameId);

    @Query(value = "SELECT * FROM rentals ORDER BY id DESC",nativeQuery = true)
    List<RentalModel> allRentalsOrdened();
}
