package models;

/**
 * <code>Card</code> represents a card that an avatar can play. All attributes common among all types of cards are declared here.
 */

public abstract class Card {
    /** 
     * The name of this card.
     */
    protected String name;
    /** 
     * The path to the image representing this card.
     */
    protected String image;
    /** 
     * How much blood the avatar needs to summon this card.
     */
    protected int cost;
    /** 
     * States whether the card is facing up or down.
     */
    protected boolean is_facing_up = true;

    /** 
     * Creates a new card with a name, image, and summoning cost.
     * 
     * @param n             the name of this character.
     * @param i             the path to the image representing this character.
     * @param c             the summoning cost of this character.
     */
    public Card(String n, String i, int c) {
        this.name = n;
        this.image = i;
        this.cost = c;
    }
    
    /**
     * Returns the name of this card.
     * 
     * @return              the value of <code>name</code>.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the path to the image representing this card.
     * 
     * @return              the value of <code>image</code>.
     */
    public String getImage() {
        return this.image;
    }

    /**
     * Returns the summoning cost of this card.
     * 
     * @return              the value of <code>cost</code>.
     */
    public int getCost() {
        return this.cost;
    }

    /**
     * Determines whether this card is facing up or down.
     * 
     * @return              the value of <code>is_facing_up</code>.
     */
    public boolean isFacingUp() {
        return this.is_facing_up;
    }

    /**
     * Flips the card by setting <code>is_facing_up</code> to its opposite.
     */
    public void flip() {
        this.is_facing_up = !(this.is_facing_up);
    }
}
