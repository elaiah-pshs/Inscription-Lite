package models.engines;

import javafx.scene.text.Text;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import models.App;
import models.Avatar;
import models.Session;
import models.enums.Locations;
import models.processors.LocationProcessor;

/**
 * <code>BoardEngine</code> contains functions to render and update the board.
 */
public class BoardEngine {
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
     * Creates a new <code>BoardEngine</code> instance that utilizes the application's location processor.
     * 
     * @param l             the applicaton's location processor.
     */
    public BoardEngine(LocationProcessor l) {
        this.location_processor = l;
    }
    
    /** 
     * Sets the location property of each component in the board.
     */
    public void setProperties() {
        for (Locations l : Locations.values())
            location_processor.toNode(l).getProperties().put("location", l);
    }

    /** 
     * Disables certain elements on the board based on which side is playing.
     */
    public void setDisables() {
        if (session.getPlayingSide() == 'h') {
            location_processor.toNode(Locations.HD).setDisable(false);
            location_processor.toNode(Locations.HH).setDisable(false);
            location_processor.toNode(Locations.HG).setDisable(false);

            location_processor.toNode(Locations.AD).setDisable(true);
            location_processor.toNode(Locations.AH).setDisable(true);
            location_processor.toNode(Locations.AG).setDisable(true);
        }
        else if (session.getPlayingSide() == 'a') {
            location_processor.toNode(Locations.HD).setDisable(true);
            location_processor.toNode(Locations.HH).setDisable(true);
            location_processor.toNode(Locations.HG).setDisable(true);

            location_processor.toNode(Locations.AD).setDisable(false);
            location_processor.toNode(Locations.AH).setDisable(false);
            location_processor.toNode(Locations.AG).setDisable(false);
        }
    }

    
    /** 
     * Updates the deck count of an avatar.
     * 
     * @param updated_avatar    a character referencing the avatar whose deck changed.
     */
    public void updateDeckCount(char updated_avatar) {
        if (updated_avatar == 'h') {
            VBox avatar = (VBox)this.location_processor.toNode(2, 5);
            Text avatar_deck_count = (Text)avatar.getChildren().get(1);
            avatar_deck_count.setText(home.getDeckSize());
        }
        else {
            VBox avatar = (VBox)this.location_processor.toNode(1, 5);
            Text avatar_deck_count = (Text)avatar.getChildren().get(0);
            avatar_deck_count.setText(away.getDeckSize());
        }
    }

    /** 
     * Updates the discard pile count of an avatar.
     * 
     * @param updated_avatar    a character referencing the avatar whose discard pile changed.
     */
    public void updateGraveyardCount(char updated_avatar) {
        if (updated_avatar == 'h') {
            VBox avatar = (VBox)this.location_processor.toNode(2, 0);
            Text avatar_deck_count = (Text)avatar.getChildren().get(1);
            avatar_deck_count.setText(home.getPileSize());
        }
        else {
            VBox avatar = (VBox)this.location_processor.toNode(1, 0);
            Text avatar_deck_count = (Text)avatar.getChildren().get(0);
            avatar_deck_count.setText(away.getPileSize());
        }
    }

    /** 
     * Updates the health count of an avatar.
     * 
     * @param updated_avatar    a character referencing the avatar whose health changed.
     */
    public void updateAvatarHealth(char updated_avatar) {
        if (updated_avatar == 'h') {
            HBox avatar = (HBox)this.location_processor.toNode(3, 1);
            VBox avatar_stats = (VBox)avatar.getChildren().get(1);
            StackPane avatar_health_icon = (StackPane)avatar_stats.getChildren().get(0);
            Text avatar_health = (Text)avatar_health_icon.getChildren().get(0);
            avatar_health.setText(Integer.toString(home.getHealth()));
        }
        else {
            HBox avatar = (HBox)this.location_processor.toNode(0, 1);
            VBox avatar_stats = (VBox)avatar.getChildren().get(1);
            StackPane avatar_health_icon = (StackPane)avatar_stats.getChildren().get(0);
            Text avatar_health = (Text)avatar_health_icon.getChildren().get(0);
            avatar_health.setText(Integer.toString(away.getHealth()));
        }
    }

    /** 
     * Updates the blood count count of an avatar.
     * 
     * @param updated_avatar    a character referencing the avatar whose blood count changed.
     */
    public void updateAvatarBlood(char updated_avatar) {
        if (updated_avatar == 'h') {
            HBox avatar = (HBox)this.location_processor.toNode(3, 1);
            VBox avatar_stats = (VBox)avatar.getChildren().get(1);
            StackPane avatar_blood_icon = (StackPane)avatar_stats.getChildren().get(1);
            Text avatar_blood= (Text)avatar_blood_icon.getChildren().get(0);
            avatar_blood.setText(Integer.toString(home.getBlood()));
        }
        else {
            HBox avatar = (HBox)this.location_processor.toNode(0, 1);
            VBox avatar_stats = (VBox)avatar.getChildren().get(1);
            StackPane avatar_blood_icon = (StackPane)avatar_stats.getChildren().get(1);
            Text avatar_blood = (Text)avatar_blood_icon.getChildren().get(0);
            avatar_blood.setText(Integer.toString(away.getBlood()));
        }
    }

    /** 
     * Updates all changing quantities in the game UI.
     */
    public void updateCounts() {
        StackPane turn_number_parent = (StackPane)this.location_processor.toNode(0, 5);
        Text turn_number = (Text)turn_number_parent.getChildren().get(1);
        turn_number.setText("Turn No. " + session.getTurnNumber());

        this.updateDeckCount('h');
        this.updateDeckCount('a');
        this.updateGraveyardCount('h');
        this.updateGraveyardCount('a');

        this.updateAvatarHealth('h');
        this.updateAvatarHealth('a');
        this.updateAvatarBlood('h');
        this.updateAvatarBlood('a');
    }
}
