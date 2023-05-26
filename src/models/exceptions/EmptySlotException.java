/**
*
* @throws EmptySlotException < when a sigil effect is applied to a position on the board with no card in it >
*
**/

package models.exceptions;

public class EmptySlotException extends Exception {
    public EmptySlotException() {
        super("Error: Player cannot summon a sigil here as the slot is empty!");
    }
}
