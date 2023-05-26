package models;

import java.util.ArrayList;

import models.enums.Pointers;
import models.enums.SigilCodes;

import models.processors.PointerProcessor;

import models.events.AttackEvent;
import models.events.AvatarDeathEvent;
import models.events.CharDeathEvent;

import models.exceptions.DeadAvatarException;
import models.exceptions.DeadCharacterException;
import models.exceptions.PointerConversionException;
import models.exceptions.ZeroHealthException;

/**
 * <code>Character</code> represents a character a player can place on the board in their avatar's slots.
 */

public class Character extends Card implements Entity {
    /** 
     * How much health this character has.
     */
    private int health;
    /** 
     * How much damage this character can deal.
     */
    private int attack;
    /** 
     * A list of all sigils affecting this character.
     */
    private ArrayList<SigilCodes> sigils = new ArrayList<SigilCodes>();

    /** 
     * How much health this character has by default. This value is effectively final as it is only modified at the constructor.
     */
    private int def_health;
    /** 
     * How much damage this character can deal by default. This value is effectively final as it is only modified at the constructor.
     */
    private int def_attack;
    /** 
     * A list of all sigils affecting this character by default. This value is effectively final as it is only modified at the constructor.
     */
    private ArrayList<SigilCodes> def_sigils = new ArrayList<SigilCodes>();

    /** 
     * Creates a new character with no default sigils.
     * 
     * @param n             the name of this character.
     * @param i             the path to the image representing this character.
     * @param c             the summoning cost of this character.
     * @param h             the health of this character.
     * @param a             the attack of this character.
     */
    public Character(String n, String i, int c, int h, int a) {
        super(n, i, c);
        this.health = h;
        this.attack = a;

        this.def_health = h;
        this.def_attack = a;
    }

    /** 
     * Creates a new character with one default sigil.
     * 
     * @param n             the name of this character.
     * @param i             the path to the image representing this character.
     * @param c             the summoning cost of this character.
     * @param h             the health of this character.
     * @param n             the attack of this character.
     * @param s             the code of the sigil this character is supposed to have, expressed as a string.
     * 
     */
    public Character(String n, String i, int c, int h, int a, String s) {
        super(n, i, c);
        this.health = h;
        this.attack = a;
        this.sigils.add(SigilCodes.valueOf(s));

        this.def_health = h;
        this.def_attack = a;
        this.def_sigils.add(SigilCodes.valueOf(s));
    }

    
    /**
     * Returns the health of this character.
     * 
     * @return              the value of <code>health</code>.
     */
    public int getHealth() {
        return this.health;
    }

    /**
     * Changes the health of this character and adds an <code>AttackEvent</code> to the <code>event_history</code> of the session associated with the running instance of this application.
     * 
     * @param hp            the amount by which the health of this character changes. A positive number indicates regeneration, while a negative number indicates dealt damage.
     * @param source        the card which caused the change in health.
     * @throws              ZeroHealthException     if the health of this character becomes less than or equal to zero.
     */
    public void changeHealth(int hp, Pointers source) throws ZeroHealthException, DeadAvatarException, DeadCharacterException, PointerConversionException {
        Pointers target = PointerProcessor.entityToPointer(this);
        Entity source_ety = PointerProcessor.toEntity(source);
        Avatar target_avatar = (Avatar)PointerProcessor.getAvatar(target);

        if (source_ety.hasSigil(SigilCodes.TOUCH_OF_DEATH))
            this.health += -1000;
        else
            this.health += hp;
        
        if (source_ety.hasSigil(SigilCodes.SNIPER) && !this.hasSigil(SigilCodes.STONE_OCEAN))
            target_avatar.changeHealth(hp);
        
        App.getSession().addEvent(new AttackEvent(source, target, -hp));
        
        if (this.health <= 0) {
            this.health = 0;
            throw new ZeroHealthException("character");
        }

        if (this.hasSigil(SigilCodes.SHARP_QUILLS)) {
            source_ety.changeHealth(-1);
        }
    }

    /**
     * Changes the health of this character.
     * 
     * @param hp            the amount by which the health of this character changes. A positive number indicates regeneration, while a negative number indicates dealt damage.
     * @throws              ZeroHealthException     if the health of this character becomes less than or equal to zero.
     */
    public void changeHealth(int hp) throws ZeroHealthException {
        this.health += hp;

        if (this.health <= 0) {
            this.health = 0;
            throw new ZeroHealthException("character");
        }
    }

    /**
     * Returns the attack of this character.
     * 
     * @return              the value of <code>attack</code>.
     */
    public int getAttack() {
        return this.attack;
    }

    /**
     * Changes the attack of this character.
     * 
     * @param a             the amount by which the attack of this character changes. A positive number indicates a buff, while a negative number indicates a nerf.
     */
    public void changeAttack(int a) {
        this.attack += a;

        if (this.attack <= 0) 
            this.attack = 0;
    }

    /**
     * Returns a list of all sigils affecting this character.
     * 
     * @return              the value of <code>sigils</code>.
     */
    public ArrayList<SigilCodes> getSigils() {
        return this.sigils;
    }

    /**
     * Determines whether this character has a certain sigil or not.
     * 
     * @param c             the code equivalent to the sigil of interest.
     * @return              <code>true</code> if the character has the sigil; <code>false</code> otherwise.
     */
    public boolean hasSigil(SigilCodes c) {
        return this.sigils.contains(c);
    }

    /**
     * Adds a sigil to <code>sigils</code>.
     * 
     * @param c             the code equivalent to the sigil to add.
     */
    public void addSigil(SigilCodes c) {
        this.sigils.add(c);
    }

    /**
     * Remove a sigil from <code>sigils</code>.
     * 
     * @param c             the code equivalent to the sigil to remove.
     */
    public void removeSigil(SigilCodes c) {
        this.sigils.remove(c);
    }

    /**
     * Makes this character perform an attack.
     */
    public void attack() throws DeadAvatarException, DeadCharacterException, PointerConversionException {
        Pointers source = PointerProcessor.entityToPointer(this);
        Pointers opposite = PointerProcessor.getOppositePointer(source);
        Entity target = PointerProcessor.toEntity(opposite);

        System.out.println("ATTACK: " + source + " to " + opposite);


        try {
            target.changeHealth(-this.attack, source);
        }
        catch (ZeroHealthException e) {
            if (target instanceof Avatar)
                App.getSession().addEvent(new AvatarDeathEvent(source, opposite));
            else if (target instanceof Character)
                App.getSession().addEvent(new CharDeathEvent(source, opposite));
        }
    }

    /**
     * Resets the attributes of this character to the default.
     */
    public void reset() {
        this.health = this.def_health;
        this.attack = this.def_attack;
        this.sigils = this.def_sigils;
    }
}
