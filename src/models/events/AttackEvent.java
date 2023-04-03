package models.events;

import models.enums.EventPointers;
import models.enums.EventTypes;
import models.patterns.Event;

public class AttackEvent extends Event {
    private int health_change;

    public AttackEvent(EventPointers s, EventPointers t, int d) {
        super(EventTypes.ATTACK, s, t);
        this.health_change = -d;
    }

    public int getHealthChange() {
        return this.health_change;
    }
}