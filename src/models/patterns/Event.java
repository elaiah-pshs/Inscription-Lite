/*
 * Event.java
 * 
 * This model class serves as the template for every event that will be
 * generated throughout a session. When an action happens in the game (e.g.,
 * a player draws a card, a sigil is played, a character attacks), an event
 * will be created before that action is executed. Events will be added to the
 * event history directly after creation as anonymous subclasses of this
 * class. We did this to make future development easier, as updates to the
 * game will inevitably add more kinds of events.
 */

package models.patterns;

import models.enums.EventTypes;
import models.enums.EventPointers;

public abstract class Event {
    protected EventTypes type;
    protected EventPointers source;
    protected EventPointers target;

    public Event(EventTypes t, EventPointers p1, EventPointers p2) {
        this.type = t;
        this.source = p1;
        this.target = p2;
    }

    public EventTypes getType() {
        return this.type;
    }

    public EventPointers getSource() {
        return this.source;
    }

    public EventPointers getTarget() {
        return this.target;
    }
}