/**
*
* @throws DeadEntityException < a card at a certain location on the board has died >
*
**/
package models.exceptions;

import models.enums.Pointers;

public class DeadEntityException extends Exception {
    private Pointers location;

    public DeadEntityException(Pointers loc) {
        super("Error: The entity at location " + loc.name() + " has died!");
        this.location = loc;
    }

    
    /** 
     * @return Pointers
     */
    public Pointers getLocation() {
        return this.location;
    }
}
