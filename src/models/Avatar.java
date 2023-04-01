/*
 * Avatar.java
 * 
 * This model class represents the a player in the game. All the actions a
 * player can do is covered here: drawing, playing, discarding, etc. This class
 * is also in charge of reading and writing game data to and from the CSV files
 * that serve as the game's database.
 */

package models;

import java.io.File;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Collections;

import models.exceptions.ZeroHealthException;
import models.exceptions.BloodCountException;
import models.exceptions.EmptySlotException;
import models.exceptions.FullSlotException;
import models.exceptions.InvalidSummonException;
import models.exceptions.NullSessionException;

public class Avatar {
    private int health = 20;
    private int blood_count = 0;
    private ArrayList<SigilEffect> effects = new ArrayList<SigilEffect>();

    private File data;
    private Stack<Card> deck = new Stack<Card>();
    private ArrayList<Card> hand = new ArrayList<Card>();
    private Character[] slots = new Character[4];
    private Stack<Card> pile = new Stack<Card>();

    // placeholder variables
    private ArrayList<Card> shuffler = new ArrayList<Card>();
    private Card summoned;

    public Avatar(String d) {
        this.data = new File(d);
    }

    public void initialize() {
        // TODO: Read file referenced by this.data
        // TODO: Assign values stored in file to their respective fields
        
        // shuffle the cards in the deck
        for (int i = 0; i < 40; i++)
            shuffler.add(deck.pop());

        Collections.shuffle(shuffler);

        for (int i = 0; i < 40; i++)
            deck.push(shuffler.get(i));

        shuffler = new ArrayList<Card>();

        // draw 5 cards from the deck
        for (int j = 0; j < 5; j++) {
            this.draw();
        }
    }

    public void changeHealth(int hp) throws ZeroHealthException {
        this.health += hp;

        this.effects.forEach((e) -> {
            e.applyEffect();
        });

        if (this.health <= 0) {
            this.health = 0;
            throw new ZeroHealthException();
        }

        //for concedes: use this method with hp = -1000
    }

    public void changeBloodCount(int b) throws BloodCountException {
        this.blood_count += b;
        if (this.blood_count < 0) {
            this.health -= b;
            throw new BloodCountException();
        }
    }

    public void addEffect(SigilEffect s) {
        this.effects.add(s);
    }

    public Character getCharInSlot(int column) {
        return slots[column];
    }

    public void flip() {
        this.hand.forEach((c) -> {
            c.flip();
        });
    }

    public void draw() {
        this.hand.add(this.deck.pop());
    }

    public void summonChar(int hand_pos, int column) throws FullSlotException, BloodCountException {
        summoned = this.hand.get(hand_pos);

        if (this.slots[column] != null) {
            throw new FullSlotException();
        }
        else {
            this.changeBloodCount(-summoned.getCost());
            this.slots[column] = (Character)summoned;
            this.hand.remove(hand_pos);
        }        
    }

    public void summonSigil(int hand_pos, int column) throws InvalidSummonException, EmptySlotException, BloodCountException {
        summoned = this.hand.get(hand_pos);

        if (!((Sigil)summoned).appliesToChars()) {
            throw new InvalidSummonException("character");
        }
        else if (this.slots[column] == null) {
            throw new EmptySlotException();
        }
        else {
            this.changeBloodCount(-summoned.getCost());
            this.getCharInSlot(column).addEffect(((Sigil)summoned).getEffect());
            this.discardSigil(hand_pos);
            this.hand.remove(hand_pos);
        }
    }

    public void summonSigil(int hand_pos, char avatar) throws InvalidSummonException, BloodCountException, NullSessionException {
        summoned = this.hand.get(hand_pos);

        if (!((Sigil)summoned).appliesToAvatars()) {
            throw new InvalidSummonException("avatar");
        }

        this.changeBloodCount(-summoned.getCost());

        // if applied to the playing avatar
        if (avatar == 'p') {
            this.addEffect(((Sigil)summoned).getEffect());
        }
        // if applied to the observing avatar
        else {
            App.getSession().getObservingAvatar().addEffect(((Sigil)summoned).getEffect());
        }

        this.discardSigil(hand_pos);
        this.hand.remove(hand_pos);
    }

    public void discardChar(int column) {
        this.pile.push(this.slots[column]);
        this.slots[column] = null;

        ((Character)(this.pile.peek())).getEffects().forEach((e) -> {
            e.applyEffect();
        });
    }

    public void discardSigil(int hand_pos) {
        this.pile.push(this.hand.get(hand_pos));
        this.hand.remove(hand_pos);
    }

    public void sacrifice(int column) throws BloodCountException {
        this.changeBloodCount(this.slots[column].getHealth());
        this.discardChar(column);
    }

    public void attack() throws NullSessionException, ZeroHealthException {
        for (int i = 0; i < 4; i++) {
            this.slots[i].attack(i);
        }
    }

    public void reset() {
        this.health = 20;
        this.blood_count = 0;
        this.effects = new ArrayList<SigilEffect>();
        
        this.deck = new Stack<Card>();
        this.hand = new ArrayList<Card>();
        this.slots = new Character[4];
        this.pile = new Stack<Card>();
    }

    public void saveData() {
        // TODO: Write avatar data to file referenced by this.data
    }
}
