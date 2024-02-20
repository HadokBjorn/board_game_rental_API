package com.board_game_rental.api.models;

import java.time.LocalDate;

import com.board_game_rental.api.dtos.RentalDto;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "rentals")
public class RentalModel {

    public RentalModel(RentalDto dto, GameModel game, CustomerModel customer){
        this.game = game;
        this.customer = customer;
        this.rentDate = LocalDate.now();
        this.daysRented = dto.getDaysRented();
        this.originalPrice = dto.getDaysRented() * game.getPricePerDay();
        this.returnDate = null;
        this.delayFee = (long) 0;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate rentDate;

    @Column(nullable = false)
    private Long daysRented;

    @Column(nullable = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate returnDate;
    
    @Column(nullable = false)
    private Long originalPrice;
    
    @Column(nullable = false)
    private Long delayFee;
    
    @ManyToOne
    @JoinColumn(name = "customerId")
    private CustomerModel customer;
    
    @ManyToOne
    @JoinColumn(name = "gameId")
    private GameModel game;
}
