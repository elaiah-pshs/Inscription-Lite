/**
*
* @throws PointerConversionException < when a pointer such as the player's hand is attempted to be converted into an entity >
*
**/

package models.exceptions;

public class PointerConversionException extends Exception {
    public PointerConversionException(String reason) {
        super("Error: Cannot perform pointer conversion because " + reason + "!");
    }
}
