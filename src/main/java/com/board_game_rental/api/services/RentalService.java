package com.board_game_rental.api.services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;

import com.board_game_rental.api.dtos.RentalDto;
import com.board_game_rental.api.exceptions.NotFoundException;
import com.board_game_rental.api.exceptions.UnprocessableEntityException;
import com.board_game_rental.api.models.CustomerModel;
import com.board_game_rental.api.models.GameModel;
import com.board_game_rental.api.models.RentalModel;
import com.board_game_rental.api.repositories.CustomerRepository;
import com.board_game_rental.api.repositories.GameRepository;
import com.board_game_rental.api.repositories.RentalRepository;

@Service
public class RentalService {
    final RentalRepository rentalRepository;
    final GameRepository gameRepository;
    final CustomerRepository customerRepository;

    RentalService(RentalRepository rentalRepository, GameRepository gameRepository, CustomerRepository customerRepository){
        this.rentalRepository = rentalRepository;
        this.gameRepository = gameRepository;
        this.customerRepository = customerRepository;
    }

    public RentalModel create(RentalDto dto) {
        GameModel game = this.gameRepository.findById(dto.getGameId()).orElseThrow(
            ()-> new NotFoundException("Game not exists")
        );
        CustomerModel customer = this.customerRepository.findById(dto.getCustomerId()).orElseThrow(
            ()-> new NotFoundException("Customer not exists")
        );
        Long rentedGame = this.rentalRepository.rentedGame(dto.getGameId());
        if(rentedGame >= game.getStockTotal()) throw new UnprocessableEntityException("all games already rented");
        
        RentalModel rental = new RentalModel(dto, game, customer);

        return this.rentalRepository.save(rental);
    }

    public List<RentalModel> findAll(){
        List<RentalModel> rentals = this.rentalRepository.allRentalsOrdened();
        return rentals;
    }

    public RentalModel update(Long id){
        RentalModel rental = this.rentalRepository.findById(id).orElseThrow(
            () -> new NotFoundException("Rental not exists")
        );
        if(rental.getReturnDate() != null) throw new UnprocessableEntityException("rental already is finished");
        LocalDate returnDate = rental.getRentDate().plusDays(rental.getDaysRented());
        LocalDate today = LocalDate.now();
        rental.setReturnDate(today);

        Long days = ChronoUnit.DAYS.between(returnDate, today);
        if(days > 0){
            Long delayFee = days * rental.getGame().getPricePerDay();
            rental.setDelayFee(delayFee);
        }

        return this.rentalRepository.save(rental);
    }
}
