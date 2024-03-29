package com.board_game_rental.api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerDto {
    @NotBlank
    @Size(max = 150)
    private String name;
    
    @NotBlank
    @Size(max = 11)
    private String cpf;
}
