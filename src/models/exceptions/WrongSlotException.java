/**
*
* @throws WrongSlotException < when a card is placed in a slot of the opposing player >
*
**/

package models.exceptions;

public class WrongSlotException extends Exception {
    public WrongSlotException(String msg) {
        super(msg);
    }
}
