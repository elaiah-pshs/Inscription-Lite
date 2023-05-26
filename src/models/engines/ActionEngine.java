package models.engines;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.scene.input.MouseEvent;

import models.App;
import models.Session;
import models.Avatar;
import models.Card;
import models.Character;

import models.patterns.Event;

import models.enums.Pointers;
import models.enums.Locations;
import models.enums.SigilCodes;

import models.processors.PointerProcessor;
import models.processors.LocationProcessor;

import models.exceptions.WrongSlotException;

/**
 * <code>ActionEngine</code> contains wrapper functions for <code>Avatar.draw()</code>, <code>Avatar.summon()</code>, and <code>Avatar.sacrifice()</code> that update the UI according to these three functions.
 */
public class ActionEngine {
    /** 
     * The session this engine acts in.
     */
    private Session session = App.getSession();
    /** 
     * The avatar in the home side of the game.
     */
    private Avatar home = session.getHome();
    /** 
     * The avatar in the away side of the game.
     */
    private Avatar away = session.getAway();
    /** 
     * The location processor of the game.
     */
    private LocationProcessor location_processor;
    /** 
     * The board engine of the game.
     */
    private BoardEngine board_engine;
    /** 
     * The card engine of the game.
     */
    private CardEngine card_engine;
    /** 
     * The display engine of the game.
     */
    private DisplayEngine display_engine;
    /** 
     * The event handler assigned to every card created in the game UI.
     */
    private EventHandler<MouseEvent> event_handler;
    
    /** 
     * Creates a new <code>ActionEngine</code> instance that utilizes the application's location processor, board engine, card engine, and display engine.
     * 
     * @param l             the applicaton's location processor.
     * @param b             the applicaton's board engine.
     * @param c             the applicaton's card engine.
     * @param d             the applicaton's display engine.
     * @param e             the event handler to be assigned to every card created in the game UI.
     */
    public ActionEngine(LocationProcessor l, BoardEngine b, CardEngine c, DisplayEngine d, EventHandler<MouseEvent> e) {
        location_processor = l;
        board_engine = b;
        card_engine = c;
        display_engine = d;
        event_handler = e;
    }

    /** 
     * Determines whether the a <code>MouseEvent</code> is equivalent to a player drawing a card.
     * 
     * @param event         the <code>MouseEvent</code> to evaluate.
     * @return              <code>true</code> if the <code>MouseEvent</code> is equivalent to a player drawing a card; <code>false</code> otherwise.
     */
    public boolean isDraw(MouseEvent event) {
        Node source = (Node)event.getSource();
        boolean source_is_deck = source.getStyleClass().contains("deck");
        boolean double_clicked = source == display_engine.getHighlighted();
        return source_is_deck && double_clicked;
    }

    /** 
     * Draws a card for the playing avatar, then displays this event in the UI.
     * 
     * @param event         the <code>MouseEvent</code> representing the player drawing a card.
     */
    public void draw(MouseEvent event) {
        try {
            display_engine.unhighlight();

            Card drawn = session.getPlayingAvatar().draw();
            String location = (session.getPlayingSide() + "h").toUpperCase();
            StackPane card = card_engine.renderCard(drawn, Locations.valueOf(location));
            card.setOnMouseClicked(event_handler);

            board_engine.updateDeckCount(session.getPlayingSide());

            System.out.println("DRAW: " + drawn.getName());
        }
        catch (Exception e) {
            display_engine.displayError(e);
        }
    }

    /** 
     * Determines whether the a <code>Node</code> has a certain style class.
     * 
     * @param element       the <code>Node</code> to evaluate.
     * @param property      the style class to check for.
     * @return              <code>true</code> if the <code>element</code> has the style class in question; <code>false</code> otherwise.
     */
    public boolean nodeHasClass(Node element, String property) {
        return element.getStyleClass().contains(property);
    }

    /** 
     * Determines whether the a <code>MouseEvent</code> is equivalent to a player summoning a card.
     * 
     * @param event         the <code>MouseEvent</code> to evaluate.
     * @return              <code>true</code> if the <code>MouseEvent</code> is equivalent to a player summoning a card; <code>false</code> otherwise.
     */
    public boolean isSummon(MouseEvent event) {
        Node source = (Node)event.getSource();
        Node highlighted = display_engine.getHighlighted();

        boolean source_is_slot = nodeHasClass(source, "slot");
        boolean source_is_card = nodeHasClass(source, "card");
        boolean highlighted_is_slot = nodeHasClass(highlighted, "slot");
        boolean highlighted_is_card = nodeHasClass(highlighted, "card");

        boolean valid_1 = source_is_slot && highlighted_is_card;
        boolean valid_2 = source_is_card && highlighted_is_slot;

        return valid_1 || valid_2;
    }

    /** 
     * Summons a card for the playing avatar, then displays this event in the UI.
     * 
     * @param event         the <code>MouseEvent</code> representing the player summoning a card.
     * @param source        the <code>Node</code> that triggered the event.
     * @param highlighted   the <code>Node</code> that is currently highlighted.
     */
    public void summon(MouseEvent event, Node source, Node highlighted) {
        Node card, slot;

        if (nodeHasClass(source, "slot")) {
            slot = source;
            card = highlighted;
        }
        else {
            slot = highlighted;
            card = source;
        }

        try {
            display_engine.unhighlight();

            Locations slot_location = (Locations)slot.getProperties().get("location");
            char slot_side = slot_location.toString().toLowerCase().charAt(0);
            if (!(slot_side == session.getPlayingSide()))
                throw new WrongSlotException("You cannot summon a character in your enemy's slots!");


            int source_index = home.getHand().indexOf(card.getProperties().get("card"));
            if (session.getPlayingSide() == 'a')
                source_index = away.getHand().indexOf(card.getProperties().get("card"));

            Locations target_location = (Locations)slot.getProperties().get("location");
            Card summoned = session.getPlayingAvatar().summon(source_index, location_processor.toPointer(target_location));
            
            if (summoned instanceof Character) {
                if (session.getPlayingSide() == 'h')
                    ((HBox)location_processor.toNode(Locations.HH)).getChildren().remove(card);
                else
                    ((HBox)location_processor.toNode(Locations.AH)).getChildren().remove(card);

                ((StackPane)slot).getChildren().add(card);
                card.setOnMouseClicked(null);
            }

            Event summon_event = session.peekLastEvent();
            Pointers summon_target = summon_event.getTarget();
            Locations summon_location = PointerProcessor.toLocation(summon_target);
            char summon_avatar = summon_location.toString().toLowerCase().charAt(0);
            board_engine.updateAvatarBlood(summon_avatar);

            System.out.println("SUMMON: " + summoned.getName());
        }
        catch (Exception e) {
            display_engine.displayError(e);
        }
    }

    /** 
     * Determines whether the a <code>MouseEvent</code> is equivalent to a player sacrificing a character.
     * 
     * @param event         the <code>MouseEvent</code> to evaluate.
     * @return              <code>true</code> if the <code>MouseEvent</code> is equivalent to a player sacrificing a character; <code>false</code> otherwise..
     */
    public boolean isSacrifice(MouseEvent event) {
        Node source = (Node)event.getSource();
        Node highlighted = display_engine.getHighlighted();

        boolean source_is_slot = nodeHasClass(source, "slot");
        boolean source_is_pile = nodeHasClass(source, "graveyard");
        boolean highlighted_is_slot = nodeHasClass(highlighted, "slot");
        boolean highlighted_is_pile = nodeHasClass(highlighted, "graveyard");

        boolean valid_1 = source_is_slot && highlighted_is_pile;
        boolean valid_2 = source_is_pile && highlighted_is_slot;

        return valid_1 || valid_2;
    }

    /** 
     * Sacrifices a character for the playing avatar, then displays this event in the UI.
     * 
     * @param event         the <code>MouseEvent</code> representing the player sacrificng a character.
     * @param source        the <code>Node</code> that triggered the event.
     * @param highlighted   the <code>Node</code> that is currently highlighted.
     */
    public void sacrifice(MouseEvent event, Node source, Node highlighted) {
        StackPane slot;

        if (nodeHasClass(source, "slot"))
            slot = (StackPane)source;
        else
            slot = (StackPane)highlighted;

        try {
            display_engine.unhighlight();

            Locations slot_location = (Locations)slot.getProperties().get("location");
            char slot_side = slot_location.toString().toLowerCase().charAt(0);
            if (!(slot_side == session.getPlayingSide()))
                throw new WrongSlotException("You cannot sacrifice your enemy's character!");
            
            Character sacrificed = session.getPlayingAvatar().sacrifice(location_processor.toPointer(slot_location));
            if (!sacrificed.hasSigil(SigilCodes.NINE_LIVES))
                slot.getChildren().clear();

            char sacrifice_avatar = slot_location.toString().toLowerCase().charAt(0);
            board_engine.updateGraveyardCount(sacrifice_avatar);
            board_engine.updateAvatarBlood(sacrifice_avatar);

            System.out.println("SACRIFICE: " + sacrificed.getName());
            System.out.println(slot.getChildren());
        }
        catch (Exception e) {
            display_engine.displayError(e);
        }
    }
}
