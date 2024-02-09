package com.board_game_rental.api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class GameDto {
    @NotBlank
    @Size(max = 150, message = "Maximum length for name is 150 characters!")
    private String name;

    @NotBlank
    private String image;

    @NotNull
    @Positive
    private Long stockTotal;

    @NotNull
    @Positive
    private Long pricePerDay;
}
