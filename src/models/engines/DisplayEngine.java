package models.engines;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.text.Text;

import models.App;
import models.processors.LocationProcessor;

/**
 * <code>DisplayEngine</code> contains functions that control highlighting, error displays, and screen switching.
 */
public class DisplayEngine {
    /** 
    * The location processor of the game.
    */
   private LocationProcessor location_processor;

    /** 
    * The application window.
    */
    private Stage stage;
    /** 
    * The currently displayed screen.
    */
    private Scene scene;
    /** 
    * The root (highest) component in the currently displayed screen.
    */
    private Parent root;
    
    /** 
    * References the hihglighted <code>Node</code>.
    */
    private Node highlighted;
    /** 
    * References the spot where the error text for the home side goes.
    */
    private Text home_error;
    /** 
    * References the spot where the error text for the away side goes.
    */
    private Text away_error;

    /** 
     * Creates a new <code>DisplayEngine</code> instance that utilizes the application's location processor.
     * 
     * @param l             the applicaton's location processor.
     */
    public DisplayEngine(LocationProcessor l) {
        this.location_processor = l;
        this.home_error = (Text)this.location_processor.toNode(3, 0);
        this.away_error = (Text)this.location_processor.toNode(0, 0);
    }

    /** 
     * Creates a new <code>DisplayEngine</code> instance.
     */
    public DisplayEngine() {
        ;
    }

    /** 
     * Switches to a new screen.
     * 
     * @param path          the path to the FXML file representing the screen to go to.
     * @param trigger       the <code>MouseEvent</code> that triggered the switch screen action.
     */
    public void switchScreen(String path, MouseEvent trigger) throws IOException {
        root = FXMLLoader.load(getClass().getResource("../../views/" + path));
        stage = (Stage)((Node)trigger.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /** 
     * Switches to a new screen with a custom controller that has to be initialized.
     * 
     * @param path          the path to the FXML file representing the screen to go to.
     * @param trigger       the <code>MouseEvent</code> that triggered the switch screen action.
     * @param controller    an object representing the controller of the screen.
     */
    public void switchScreen(String path, MouseEvent trigger, Object controller) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../views/" + path));
        fxmlLoader.setController(controller);
        root = fxmlLoader.load();
        stage = (Stage)((Node)trigger.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /** 
     * Returns the highlighted node.
     * 
     * @return              the value of <code>highlighted</code>.
     */
    public Node getHighlighted() {
        return this.highlighted;
    }
    
    /** 
     * Highlights a node.
     * 
     * @param n             the <code>Node</code> to highlight.
     */
    public void highlight(Node n) {
        this.highlighted = n;
        this.highlighted.getStyleClass().add("pane");
    }

    /** 
     * Removes the highlight from a node.
     * 
     * @param n             the <code>Node</code> to unhighlight.
     */
    public void unhighlight() {
        try {
            this.highlighted.getStyleClass().remove("pane");
            this.highlighted = null;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 
     * Displays an error on the game UI.
     * 
     * @param e             the <code>Exception</code> to be displayed.
     */
    public void displayError(Exception e) {
        if (highlighted != null)
            unhighlight();

        switch (App.getSession().getPlayingSide()) {
            case 'h':
                home_error.setText(e.getMessage());
                break;
            case 'a':
                away_error.setText(e.getMessage());
                break;
        }
    }

    /** 
     * Displays an error on the game UI.
     * 
     * @param e             the <code>Exception</code> to be displayed.
     * @param side          the side (home or away) which caused the error.
     */
    public void displayError(Exception e, char side) {
        if (highlighted != null)
            unhighlight();

        switch (side) {
            case 'h':
                home_error.setText(e.getMessage());
                break;
            case 'a':
                away_error.setText(e.getMessage());
                break;
        }
    }

    /** 
     * Removes all errors displayed on the game UI.
     */
    public void removeErrors() {
        try {
            if (!home_error.getText().isEmpty())
                home_error.setText("");
            if (!away_error.getText().isEmpty())
                away_error.setText("");
        }
        catch (NullPointerException e) {
            ;
        }
    }
}
