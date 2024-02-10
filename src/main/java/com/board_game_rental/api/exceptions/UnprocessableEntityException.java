package com.board_game_rental.api.exceptions;

public class UnprocessableEntityException extends RuntimeException{
    public UnprocessableEntityException(String message) {
        super(message);
    }
}
