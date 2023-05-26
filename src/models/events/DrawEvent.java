/**
* DrawEvent.java
*
* this event occurs when the active player draws a card, and obtains a card from the deck-- acquiring its value
* using the pointer PD. (active player's deck) That card is then placed into the active player's hand, by adding 
* the card into the pointer PH. (active player's hand)
**/

package models.events;

import models.enums.Pointers;
import models.enums.EventTypes;
import models.patterns.Event;

public class DrawEvent extends Event {
    public DrawEvent() {
        super(EventTypes.DRAW, Pointers.PD, Pointers.PH);
    }
}
