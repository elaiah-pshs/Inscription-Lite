/**
*
* @throws UndeadSacrificeException < when an undead character tries to get sacrificed >
*
**/

package models.exceptions;

public class UndeadSacrificeException extends Exception {
    public UndeadSacrificeException() {
        super("You cannot sacrifice this character since it is undead!");
    }
}
