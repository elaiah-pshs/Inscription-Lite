package models;

import java.util.LinkedList;

import models.patterns.Event;
import models.enums.EventTypes;
import models.enums.Pointers;
import models.exceptions.BloodCountException;
import models.exceptions.DeadAvatarException;
import models.exceptions.DeadCharacterException;
import models.exceptions.PointerConversionException;

/**
 * <code>Session</code> represents a game being played in the application. It dictates the general flow of a game by initializing the avatars, logging the players' moves, and facilitating the transfer of turns from one player to another.
 * 
 * A game consists of two avatars: one in the home side (the bottom of the screen) representing player 1, and one in the away side (the top of the screen) representing player 2. At a given point in time, one of these players will be playing and the other will not be playing (observing). A turn is counted when both the home and away sides have played already, in that order.
 */
public class Session {
    /** 
     * An array containing all the Avatars a player can use.
     */
    private Avatar[] avatars = {
        new Avatar("src/static/data/card_data.csv"),
        new Avatar("src/static/data/card_data.csv"),
    };

    /** 
     * States whether the game was just opened or not. This field is merely a corrector so that the home side's cards are flipped up and the away side's cards are flipped down, not the other way around.
     */
    private boolean new_init = true;
    /** 
     * The index of the home side's avatar in <code>avatars</code>.
     */
    private int home_index;
    /** 
     * The index of the away side's avatar in <code>avatars</code>.
     */
    private int away_index;
    /** 
     * The number of turns that were played in the game so far. The first turn of both players shall not be counted by the turn number since their cards will not attack yet. Hence, this variable is initialized with a value of 0.
     */
    private int turn_number = 0;
    /** 
     * States which side is currently playing (home, represented by <code>'h'</code>, or away, represented by <code>'a'</code>).
     */
    private char playing_side = 'h';
    /** 
     * Stores the avatar of the playing side.
     */
    private Avatar playing_avatar;
    /** 
     * Stores the avatar of the observing side.
     */
    private Avatar observing_avatar;
    /** 
     * Lists all game events.
     */
    private LinkedList<Event> event_history = new LinkedList<Event>();

    /** 
     * Sets up a new game. The values of <code>playing_avatar</code> and <code>observing_avatar</code> are assigned, then the avatars are initialized.
     * 
     * @param p1            index of the first player's avatar in <code>avatars</code>.
     * @param p2            index of the second player's avatar in <code>avatars</code>.
     */
    public void open(int p1, int p2) {
        this.home_index = p1;
        this.away_index = p2;
        this.playing_avatar = avatars[this.home_index];
        this.observing_avatar = avatars[this.away_index];

        this.playing_avatar.initialize();
        this.observing_avatar.initialize();
    }

    /** 
     * Determines whether the game was just opened or not.
     * 
     * @return              the value of <code>new_init</code>.
     */
    public boolean isNewInit() {
        return this.new_init;
    }

    /** 
     * Returns a character corresponding to which side is playing.
     * 
     * @return              the value of <code>playing_side</code>.
     */
    public char getPlayingSide() {
        return this.playing_side;
    }

    /** 
     * Returns a reference to the playing side's avatar.
     * 
     * @return              the value of <code>playing_avatar</code>.
     */
    public Avatar getPlayingAvatar() {
        return this.playing_avatar;
    }

    /** 
     * Returns a reference to the observing side's avatar.
     * 
     * @return              the value of <code>observing_avatar</code>.
     */
    public Avatar getObservingAvatar() {
        return this.observing_avatar;
    }

    /**
     * Returns a reference to the home side's avatar.
     * 
     * @return              the <code>Avatar</code> at index <code>home_index</code> of <code>avatars</code>.
     */
    public Avatar getHome() {
        return avatars[this.home_index];
    }

    /** 
     * Returns a reference to the away side's avatar.
     * 
     * @return              the <code>Avatar</code> at index <code>away_index</code> of <code>avatars</code>.
     */
    public Avatar getAway() {
        return avatars[this.away_index];
    }

    /** 
     * Returns the current turn number.
     * 
     * @return              the value of <code>turn_number</code>.
     */
    public int getTurnNumber() {
        return this.turn_number;
    }

    /** 
     * Returns the last recorded game event.
     * 
     * @return              the <code>Event</code> object at the last index of <code>event_history</code>.
     */
    public Event peekLastEvent() {
        return this.event_history.getLast();
    }

    /** 
     * Returns the target of the last recorded game event.
     * 
     * @return              the value of the <code>target</code> field of the <code>Event</code> object at the last index of <code>event_history</code>.
     */
    public Pointers getLastTarget() {
        return this.event_history.getLast().getTarget();
    }

    /** 
     * Returns and removes the first recorded game event.
     * 
     * @return              the <code>Event</code> object at index 0 of <code>event_history</code>.
     */
    public Event pullFirstEvent() {
        return this.event_history.poll();
    }

    /** 
     * Adds an event to <code>event_history</code>.
     * 
     * @param e             the event to add to <code>event_history</code>.
     * @throws              DeadAvatarException     if value of the <code>type</code> field of <code>e</code> is <code>EventTypes.AVATAR_DEATH</code>.
     * @throws              DeadCharacterException  if value of the <code>type</code> field of <code>e</code> is <code>EventTypes.CHAR_DEATH</code>.
     */
    public void addEvent(Event e) throws DeadCharacterException, DeadAvatarException, PointerConversionException {
        this.event_history.add(e);

        if (e.getType().equals(EventTypes.AVATAR_DEATH))
            throw new DeadAvatarException();
        
        if (e.getType().equals(EventTypes.CHAR_DEATH))
            throw new DeadCharacterException();
    }

    /** 
     * Transitions the game from one player to another by switching the values of <code>playing_avatar</code> and <code>observing_avatar</code>, flipping all cards in the hands of the avatars, and conducting attacks (if both players have played).
     * 
     * @return              the new value of <code>playing_side</code> after <code>playing_avatar</code> and <code>observing_avatar</code> switched values.
     */
    public char nextPlayer() throws BloodCountException {
        if (!this.new_init) {
            if (this.playing_avatar == avatars[this.home_index]) {
                this.playing_avatar = avatars[this.away_index];
                this.observing_avatar = avatars[this.home_index];
                this.playing_side = 'a';
            }
            else {
                this.playing_avatar = avatars[this.home_index];
                this.observing_avatar = avatars[this.away_index];
                this.turn_number += 1;
                this.playing_avatar.changeBloodCount(1);
                this.observing_avatar.changeBloodCount(1);
                this.playing_side = 'h';
            }

            this.playing_avatar.allowDraw();
            this.playing_avatar.flip();
        }
        else {
            this.new_init = false;
            this.observing_avatar.flip();
        }
        
        return this.playing_side;
    }

    /** 
     * Ends the game and resets all values of this session and the avatars used in the game.
     */
    public void close() {
        this.playing_avatar.reset();
        this.observing_avatar.reset();

        this.new_init = true;
        this.turn_number = 0;
        this.event_history.clear();;
    }
}
