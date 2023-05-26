/**
* AvatarDeathEvent.java
*
* this event handles the occasion that a player's health reacher 0. The player who remains alive is acquired using the pointer Killer, 
* and the player that died is acquired using the pointer Avatar. The killer is the winner of the session.
**/

package models.events;

import models.enums.Pointers;
import models.enums.EventTypes;
import models.patterns.Event;

public class AvatarDeathEvent extends Event {
    public AvatarDeathEvent(Pointers killer, Pointers avatar) {
        super(EventTypes.AVATAR_DEATH, killer, avatar);
    }
}
