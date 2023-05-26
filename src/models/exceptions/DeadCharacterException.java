/**
*
* @throws DeadCharacterException < once a card's character has died. >
*
**/
package models.exceptions;

public class DeadCharacterException extends Exception {
    public DeadCharacterException() {
        super("Error: A character has died!");
    }
}

// catch this exception in the game controller
