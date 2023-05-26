package models;

import java.io.File;
import java.util.ArrayList;
import java.util.Stack;

import models.enums.Pointers;
import models.enums.SigilCodes;

import models.processors.CardDataProcessor;
import models.processors.PointerProcessor;

import models.events.DrawEvent;
import models.events.SacrificeEvent;
import models.events.CharSummonEvent;
import models.events.SpellSummonEvent;
import models.events.AttackEvent;

import models.exceptions.ZeroHealthException;
import models.exceptions.BloodCountException;
import models.exceptions.DeadAvatarException;
import models.exceptions.DeadCharacterException;
import models.exceptions.EmptyDeckException;
import models.exceptions.FullSlotException;
import models.exceptions.InvalidSummonException;
import models.exceptions.MultipleDrawException;
import models.exceptions.PointerConversionException;
import models.exceptions.UndeadSacrificeException;

/**
 * <code>Avatar</code> represents a player in a game. All actions a player can do are defined here: drawing, playing, discarding, etc.
 */
public class Avatar implements Entity {
    /** 
     * The health of this avatar.
     */
    private int health = 20;
    /** 
     * How much blood this avatar has. Every card requires a certain amount of blood to summon, and a player can only summon a card if their avatar's blood count is greater than or equal to the card's blood cost.
     */
    private int blood_count = 5;
    /** 
     * A list of all sigils affecting this avatar.
     */
    private ArrayList<SigilCodes> sigils = new ArrayList<SigilCodes>();
    /** 
     * States whether this avatar has drawn a card in the current turn or not.
     */
    private boolean has_drawn = false;

    /** 
     * References the CSV file that lists the cards in this avatar's deck
     */
    private File data;
    /** 
    * The deck of cards this avatar can draw from.
    */
    private Stack<Card> deck = new Stack<Card>();
    /** 
    * A list containing all the cards in this avatar's hand
    */
    private ArrayList<Card> hand = new ArrayList<Card>();
    /** 
    * An array representing the four slots of an avatar on the playing board.
    */
    private Character[] slots = new Character[4];
    /** 
    * A list containing all the cards of this avatar that died or were sacrificed.
    */
    private Stack<Card> pile = new Stack<Card>();

    /** 
     * Creates a new avatar whose deck of cards is listed in a CSV file refereced by a string.
     * 
     * @param d             the path to the CSV file which lists the cards in this avatar's deck.
     */
    public Avatar(String d) {
        this.data = new File(d);
    }

    /** 
     * Initializes an avatar at the start of the game by drawing five cards.
     */
    public void initialize() {
        this.deck = CardDataProcessor.read(this.data);
        for (int i = 0; i < 5; i++)
            this.hand.add(this.deck.pop());
    }

    /**
     * Returns the health of this avatar.
     * 
     * @return              the value of <code>health</code>.
     */
    public int getHealth() {
        return this.health;
    }

    /**
     * Changes the health of this avatar and adds an <code>AttackEvent</code> to the <code>event_history</code> of the session associated with the running instance of this application.
     * 
     * @param hp            the amount by which the health of this avatar changes. A positive number indicates regeneration, while a negative number indicates dealt damage.
     * @param source        the card which caused the change in health.
     * @throws              ZeroHealthException     if the health of this avatar becomes less than or equal to zero.
     */
    public void changeHealth(int hp, Pointers source) throws ZeroHealthException, DeadAvatarException, DeadCharacterException, PointerConversionException {
        this.health += hp;
        App.getSession().addEvent(new AttackEvent(source, PointerProcessor.entityToPointer(this), -hp));
        
        if (this.health <= 0) {
            this.health = 0;
            throw new ZeroHealthException("avatar");
        }
    }

    /**
     * Changes the health of this avatar.
     * 
     * @param hp            the amount by which the health of this avatar changes. A positive number indicates regeneration, while a negative number indicates dealt damage.
     * @throws              ZeroHealthException     if the health of this avatar becomes less than or equal to zero.
     */
    public void changeHealth(int hp) throws ZeroHealthException {
        this.health += hp;
        
        if (this.health <= 0) {
            this.health = 0;
            throw new ZeroHealthException("avatar");
        }
    }

    /**
     * Returns the blood count of this avatar
     * 
     * @return              the value of <code>blood_count</code>.
     */
    public int getBlood() {
        return this.blood_count;
    }

    /**
     * Changes the blood count of this avatar.
     * 
     * @param b             the amount by which the blood count of this avatar changes. A positive number indicates blood gained, while a negative number indicates blood lost.
     * @throws              BloodCountException     if this avatar will have a negative blood count as a result of this function.
     */
    public void changeBloodCount(int b) throws BloodCountException {
        this.blood_count += b;
        if (this.blood_count < 0) {
            this.blood_count -= b;
            throw new BloodCountException();
        }
    }

    /**
     * Returns a list of all sigils affecting this avatar.
     * 
     * @return              the value of <code>sigils</code>.
     */
    public ArrayList<SigilCodes> getSigils() {
        return this.sigils;
    }

    /**
     * Determines whether this avatar has a certain sigil or not.
     * 
     * @param c             the code equivalent to the sigil of interest.
     * @return              <code>true</code> if the avatar has the sigil; <code>false</code> otherwise.
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
     * Returns the number of cards in this avatar's deck.
     * 
     * @return              the size of <code>deck</code> as a string.
     */
    public String getDeckSize() {
        return Integer.toString(this.deck.size());
    }

    /**
     * Returns the cards in this avatar's hand.
     * 
     * @return              the value of <code>hand</code>.
     */
    public ArrayList<Card> getHand() {
        return this.hand;
    }

    /**
     * Returns the number of cards in this avatar's discard pile.
     * 
     * @return              the size of <code>pile</code> as a string.
     */
    public String getPileSize() {
        return Integer.toString(this.pile.size());
    }

    /**
     * Returns the character in a slot in the board that belongs to this avatar.
     * 
     * @param column        the index of the slot of interest, with 0 being leftmost slot and 3 being rightmost slot.
     * @return              the <code>Character</code> in index <code>column</code> of <code>slots</code>.
     */
    public Character getCharInSlot(int column) {
        return slots[column];
    }

    /**
     * Flips all the cards in this avatar's hand.
     */
    public void flip() {
        this.hand.forEach((c) -> {
            c.flip();
        });
    }

    /**
     * Allows this avatar to draw a card.
     */
    public void allowDraw() {
        this.has_drawn = false;
    }

    /**
     * Transfers a card from this avatar's deck to its hand.
     * 
     * @return              the <code>Card</code> that was drawn.
     * @throws              EmptyDeckException      if the deck is empty.
     * @throws              MultipleDrawException   if a player attempts to draw more than once during their turn.
     */
    public Card draw() throws EmptyDeckException, DeadAvatarException, DeadCharacterException, PointerConversionException, ZeroHealthException, MultipleDrawException {
        if (this.deck.empty())
            throw new EmptyDeckException();
        else if (this.has_drawn)
            throw new MultipleDrawException();

        Card out = this.deck.pop();
        this.hand.add(out);
        this.has_drawn = true;
        App.getSession().addEvent(new DrawEvent());
        return out;
    }

    /**
     * Summons a card.
     * 
     * @param source        the index of the card in this avatar's <code>hand</code> array.
     * @param target        the <code>Pointer</code> linked to the location where the player wants to play the card.
     * @return              the summoned <code>Card</code>.
     * @throws              FullSlotException       if the card to be summoned is a character and the slot where the player tried to play the card is already filled by another character.
     * @throws              InvalidSummonException  if the player attempts to cast a spell on something the spell cannot affect.
     */
    public Card summon(int source, Pointers target) throws FullSlotException, BloodCountException, DeadCharacterException, DeadAvatarException, InvalidSummonException, PointerConversionException, ZeroHealthException {
        Card summoned = this.hand.get(source);
        int target_slot = PointerProcessor.toInt(target) - 1;

        if (summoned instanceof Character) {
            if (this.slots[target_slot] != null)
                throw new FullSlotException();

            this.changeBloodCount(-summoned.getCost());
            this.slots[target_slot] = (Character)summoned;
            this.hand.remove(source);
            App.getSession().addEvent(new CharSummonEvent(target, summoned.getCost(), summoned.getImage()));
        }
        else {
            this.changeBloodCount(-summoned.getCost());

            switch (target) {
                case PA: 
                case OA:
                    if (!((Spell)summoned).appliesToAvatars())
                        throw new InvalidSummonException("avatar");
                    else
                        break;
                case P1:
                case P2:
                case P3:
                case P4:
                case O1:
                case O2:
                case O3:
                case O4:
                    if (!((Spell)summoned).appliesToChars())
                        throw new InvalidSummonException("character");
                    else
                        break;
                default:
                    throw new InvalidSummonException("non-entity");
            }

            this.changeBloodCount(-summoned.getCost());
            ((Spell)summoned).applyEffect(target);
            this.discardSpell(source);
            App.getSession().addEvent(new SpellSummonEvent(target, summoned.getCost(), summoned.getImage()));
        }

        return summoned;
    }

    /**
     * Moves a spell card from this avatar's hand to its discard pile.
     * 
     * @param hand_pos      the index of the card in this avatar's <code>hand</code>.
     */
    public void discardSpell(int hand_pos) {
        this.pile.push(this.hand.get(hand_pos));
        this.hand.remove(hand_pos);
    }

    /**
     * Moves a dead character from its slot to the discard pile of this avatar.
     * 
     * @param pointer       the <code>Pointer</code> linked to the slot of the character that died.
     */
    public void killChar(Pointers pointer) {
        int column = PointerProcessor.toInt(pointer) - 1;
        Character dead_char = this.slots[column];
        this.slots[column] = null;
        dead_char.reset();

        if (dead_char.hasSigil(SigilCodes.RESURRECTION) && !dead_char.hasSigil(SigilCodes.SACRIFICED) && !dead_char.hasSigil(SigilCodes.NINE_LIVES)) {
            dead_char.flip();
            this.hand.add(dead_char);
        }
        else
            this.pile.push(this.slots[column]);
    }

    /**
     * Sacrifies a character.
     * 
     * @param pointer       the <code>Pointer</code> linked to the slot of the character that shall be sacrificed.
     * @throws              UndeadSacrificeException    if the player attempts to sacrifice a character with the <code>SigilCodes.UNDEAD</code> sigil.
     */
    public Character sacrifice(Pointers pointer) throws BloodCountException, DeadAvatarException, DeadCharacterException, PointerConversionException, ZeroHealthException, UndeadSacrificeException {
        Character sacrifice = (Character)PointerProcessor.toEntity(pointer);

        if (sacrifice.hasSigil(SigilCodes.WORTHY_SACRIFICE))
            this.changeBloodCount(3);
        else
            this.changeBloodCount(1);

        if (sacrifice.hasSigil(SigilCodes.UNDEAD))
            throw new UndeadSacrificeException();
        else if (sacrifice.hasSigil(SigilCodes.NINE_LIVES)) {
            sacrifice.addSigil(SigilCodes.UNDEAD);
            sacrifice.removeSigil(SigilCodes.NINE_LIVES);
        }
        else {
            if (sacrifice.hasSigil(SigilCodes.RESURRECTION)) {
                sacrifice.addSigil(SigilCodes.SACRIFICED);            
                sacrifice.removeSigil(SigilCodes.RESURRECTION);
            }

            this.killChar(pointer);
        }
            
        
        App.getSession().addEvent(new SacrificeEvent(pointer));
        return sacrifice;
    }

    /**
     * Resets the attributes of this avatar to the default.
     */
    public void reset() {
        this.health = 20;
        this.blood_count = 5;
        this.sigils = new ArrayList<SigilCodes>();
        
        this.deck = new Stack<Card>();
        this.hand = new ArrayList<Card>();
        this.slots = new Character[4];
        this.pile = new Stack<Card>();
    }
}
