/**
* SpellSummonEvent.java
*
* this method is unused in our project. it is an unfinished method.
**/

package models.events;

import models.enums.Pointers;
import models.enums.EventTypes;
import models.patterns.Event;

public class SpellSummonEvent extends Event {
    private int blood_change;
    private String card_image;

    public SpellSummonEvent(Pointers target, int cost, String image) {
        super(EventTypes.SPELL_SUMMON, Pointers.PA, target);
        this.blood_change = -cost;
        this.card_image = image;
    }

    
    /** 
     * @return int
     */
    public int getBloodChange() {
        return this.blood_change;
    }

    public String getCardImage() {
        return this.card_image;
    }
}
