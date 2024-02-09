package com.board_game_rental.api.exceptions;

public class ConflictException extends RuntimeException {
    public ConflictException(String message){
        super(message);
    }
}
