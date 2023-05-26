package models.engines;

import java.io.File;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import models.App;
import models.Avatar;
import models.Card;
import models.Session;
import models.enums.Locations;
import models.processors.LocationProcessor;

/**
 * <code>CardEngine</code> contains functions to render and update the cards used in a game.
 */
public class CardEngine {
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
     * Creates a new <code>CardEngine</code> instance that utilizes the application's location processor.
     * 
     * @param l             the applicaton's location processor.
     */
    public CardEngine(LocationProcessor l) {
        this.location_processor = l;
    }
    
    /** 
     * Creates a new card that can be displayed in the game UI.
     * 
     * @param c             the <code>Card</code> object to create a UI out of.
     * @return              a <code>StackPane</code> representing the displayable card.
     */
    public StackPane createCard(Card c) {
        File file = new File("src/static/images/backing.png");

        if (c.isFacingUp())
            file = new File(c.getImage());
        
        Image image = new Image(file.toURI().toString());
        ImageView card = new ImageView(image);
        card.setFitWidth(55.6);
        card.setFitHeight(80);
        StackPane wrapper = new StackPane(card);
        wrapper.getStyleClass().add("card");
        wrapper.getProperties().put("card", c);

        return wrapper;
    }

    /** 
     * Renders a <code>Card</code> at a certain slot on the board.
     * 
     * @param c             the <code>Card</code> to render.
     * @param l             the location where this card shall be rendered.
     * @return              a <code>StackPane</code> representing the displayable card.
     */
    public StackPane renderCard(Card c, Locations l) {
        StackPane card = createCard(c);

        switch (l) {
            case HH:
                ((HBox)location_processor.toNode(Locations.HH)).getChildren().add(card);
                break;
            case AH:
                ((HBox)location_processor.toNode(Locations.AH)).getChildren().add(card);
                break;
            case H1:
                ((StackPane)location_processor.toNode(Locations.H1)).getChildren().add(card);
                break;
            case H2:
                ((StackPane)location_processor.toNode(Locations.H2)).getChildren().add(card);
                break;
            case H3:
                ((StackPane)location_processor.toNode(Locations.H3)).getChildren().add(card);
                break;
            case H4:
                ((StackPane)location_processor.toNode(Locations.H4)).getChildren().add(card);
                break;
            case A1:
                ((StackPane)location_processor.toNode(Locations.A1)).getChildren().add(card);
                break;
            case A2:
                ((StackPane)location_processor.toNode(Locations.A2)).getChildren().add(card);
                break;
            case A3:
                ((StackPane)location_processor.toNode(Locations.A3)).getChildren().add(card);
                break;
            case A4:
                ((StackPane)location_processor.toNode(Locations.A4)).getChildren().add(card);
            default:
                ;
        }

        return card;
    }

    /** 
     * Renders all cards in both avatars' hand.
     */
    public void renderHands() {
        home.getHand().forEach((Card c) -> {
            try {
                this.renderCard(c, Locations.HH);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });

        away.getHand().forEach((Card c) -> {
            try {
                this.renderCard(c, Locations.AH);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /** 
     * Renders all characters in both avatars' slots.
     */
    public void renderSlots() {
        for (int i = 0; i < 4; i++) {
            try {
                Card c = home.getCharInSlot(i);
                Locations l = Locations.valueOf("H" + Integer.toString(i + 1));
                renderCard(c, l); 
            }
            catch (NullPointerException e) {
                ;
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            try {
                Card c = away.getCharInSlot(i);
                Locations l = Locations.valueOf("A" + Integer.toString(i + 1));
                renderCard(c, l);
            }
            catch (NullPointerException e) {
                ;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
