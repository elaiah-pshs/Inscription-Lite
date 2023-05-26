/**
*
* @throws FullSlotException < when a card is placed on a board slot with a card already occupying it >
*
**/

package models.exceptions;

public class FullSlotException extends Exception {
    public FullSlotException() {
        super("That slot is taken!");
    }
}
