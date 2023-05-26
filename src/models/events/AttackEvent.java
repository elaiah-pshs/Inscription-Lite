/**
* AttackEvent.java
*
* This event takes the attack value of attacking entity at Pointer S, and the health of the defending 
* entity at Pointer T, and subtracts from that health by an integer D. 
**/

package models.events;

import models.enums.Pointers;
import models.enums.EventTypes;
import models.patterns.Event;

public class AttackEvent extends Event {
    private int health_change;

    public AttackEvent(Pointers s, Pointers t, int d) {
        super(EventTypes.ATTACK, s, t);
        this.health_change = -d;
    }

    
    /** 
     * @return int
     */
    public int getHealthChange() {
        return this.health_change;
    }
}
