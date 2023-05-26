/**
*
* @throws EmptyDeckException < when a player's deck has run out of cards >
*
**/
package models.exceptions;

public class EmptyDeckException extends Exception {
    public EmptyDeckException() {
        super("Error: The deck is empty!");
    }
}
