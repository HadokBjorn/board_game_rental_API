package com.board_game_rental.api.models;

import com.board_game_rental.api.dtos.GameDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "games")
public class GameModel {
    public GameModel(GameDto dto){
        this.image = dto.getImage();
        this.name = dto.getName();
        this.stockTotal = dto.getStockTotal();
        this.pricePerDay = dto.getPricePerDay();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 150, unique=true)
    private String name;
    
    @Column(nullable = false)
    private String image;
    
    @Column(nullable = false)
    private Long stockTotal;
    
    @Column(nullable = false)
    private Long pricePerDay;
}
