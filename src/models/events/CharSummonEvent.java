/**
* CharSummonEvent.java
*
* this event handles the summoning of a character card onto the game board. the pointer Target obtains the location on the board.
* the card subtracts the blood cost from the active player's blood count using blood_change, which is obtained through the active player
* pointer, (PA) and the cost of the card, which is subtracted from the PA blood count. The card is then rendered onto the board using the
* string of its filepath, under the variable card_image.
**/

package models.events;

import models.enums.Pointers;
import models.enums.EventTypes;
import models.patterns.Event;

public class CharSummonEvent extends Event {
    private int blood_change;
    private String card_image;

    public CharSummonEvent(Pointers target, int cost, String image) {
        super(EventTypes.CHAR_SUMMON, Pointers.PA, target);
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
