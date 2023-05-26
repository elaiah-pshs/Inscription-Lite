/**
*
* @throws BloodCountException < when a player's blood count is less than the summon cost of a card being summoned. >
*
**/

package models.exceptions;

public class BloodCountException extends Exception {
    public BloodCountException() {
        super("You do not have enough blood to summon this card!");
    }
}
