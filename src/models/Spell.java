package models;

import models.patterns.SpellEffect;
import models.enums.Pointers;
import models.processors.SpellProcessor;

/**
 * <code>Spell</code> represents a spell a player can cast onto the entities in the game.
 * 
 * <strong>Note: this class is still in development and is not completed! It was excluded from the first release of this project as our group determined that we do not have enough time to complete this class and its functions.</strong>
 */

public class Spell extends Card {
    /** 
     * States whether this spell can be applied to characters or not.
     */
    private boolean atc;
    /** 
     * States whether this spell can be applied to avatars or not.
     */
    private boolean ata;
    /** 
     * A code corresponding to the effect of this spell.
     */
    private SpellEffect effect;

    /** 
     * Creates a new spell.
     * 
     * @param n             the name of this spell.
     * @param i             the path to the image representing this spell.
     * @param c             the summoning cost of this spell.
     * @param atc           whether this spell can be applied to characters or not.
     * @param ata           whether this spell can be applied to avatars or not.
     * @param e             the effect of this spell.
     */
    public Spell(String n, String i, int c, boolean atc, boolean ata, String e) {
        super(n, i, c);
        this.atc = atc;
        this.ata = ata;
        this.effect = SpellProcessor.assignEffect(e);
    }

    /**
     * Determines whether this spell can be applied to characters or not.
     * 
     * @return              the value of <code>atc</code>.
     */
    public boolean appliesToChars() {
        return this.atc;
    }

    /**
     * Determines whether this spell can be applied to avatars or not.
     * 
     * @return              the value of <code>ata</code>.
     */
    public boolean appliesToAvatars() {
        return this.ata;
    }

    /**
     * Applies the effect of the spell to the target specified by a <code>Pointer</code>.
     */
    public void applyEffect(Pointers pointer) {
        effect.applyEffect(pointer);
    }
}
