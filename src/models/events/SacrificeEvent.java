/**
* SacrificeEvent.java
*
* this event handles the sacrifice of a card, by using a pointer to acquire the location of a card, and then 
* the pointer PG (active player's graveyard) is edited.
**/

package models.events;

import models.patterns.Event;
import models.enums.Pointers;
import models.enums.EventTypes;

public class SacrificeEvent extends Event {
    public SacrificeEvent(Pointers pointer) {
        super(EventTypes.SACRIFICE, pointer, Pointers.PG);
    }
}
