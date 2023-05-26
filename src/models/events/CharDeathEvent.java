/**
* CharDeathEvent.java
*
* this event handles the death of a card on the game board. The surviving card's data is acquired using the pointer Killer,
* and the card to be sent to the graveyard after death is acquired using the pointer Character.
**/ 

package models.events;

import models.enums.Pointers;
import models.enums.EventTypes;
import models.patterns.Event;

public class CharDeathEvent extends Event {
    public CharDeathEvent(Pointers killer, Pointers character) {
        super(EventTypes.CHAR_DEATH, killer, character);
    }
}
