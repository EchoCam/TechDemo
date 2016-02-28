/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.cam.echo2016.dynademo.screens;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.util.ArrayDeque;
import java.util.Deque;
import uk.ac.cam.echo2016.dynademo.MainApplication;
import uk.ac.cam.echo2016.multinarrative.GraphElementNotFoundException;
import uk.ac.cam.echo2016.multinarrative.NarrativeInstance;

/**
 *
 * @author tr395
 */
public class GameScreen extends AbstractAppState implements ScreenController {

    private Nifty nifty;
    private Screen screen;
    private MainApplication app;
    private String character; //currently playable character
    private String routeName; //The canonical name of the route the player selected from the character select screen
    private String location;
    private boolean textShowing = false;
    private Deque<String> dialogueDeque = new ArrayDeque<>();
    private NarrativeInstance narrativeInstance;

    public GameScreen() {
        super();
    }

    /**
     * Sets the text that is displayed in the dialogue box.
     * 
     * If the string is empty the textShowing bool will be set to false.
     *
     * @param text
     */
    private void setDialogueText(String text) {
        Element textElement = nifty.getCurrentScreen().findElementByName("dialogue_box_text");
        textElement.getRenderer(TextRenderer.class).setText(text);
        if (text.equals("")) {
            textShowing = false;
        } else {
            textShowing = true;
        }
    }

    /**
     * The array of Strings that are passed to this function are queued for displaying to the player.
     * 
     * If no text is being shown, the first item in the array is displayed immediately. Otherwise
     * this array is queued to the end of what is already being shown. The player can hit the
     * interaction button to "click" through text.
     * @param textSequence 
     */
    public void setDialogueTextSequence(String[] textSequence) {
        for (int i = 0; i < textSequence.length; i++) {
            dialogueDeque.addLast(textSequence[i]);
        }
        if (!isTextShowing()) {
            progressThroughText();
        }
    }

    /**
     * Flushes all the text queued and removes text currently being displayed.
     */
    public void flushDialogueTextSequence() {
        dialogueDeque.clear();
        progressThroughText();
    }

    /**
     * Simply returns if there is some text currently showing.
     * @return 
     */
    public boolean isTextShowing() {
        return textShowing;
    }

    /**
     * Simply pops the newest piece of text of the text queue and displays it.
     */
    public void progressThroughText() {
        if (!dialogueDeque.isEmpty()) {
            setDialogueText(dialogueDeque.pollFirst());
        } else {
            setDialogueText("");
        }
    }

    // temp functino to show variable passing
    public void setCharacter(String character) {
        this.character = character;
    }
    
    public void setRoute(String routeName) {
        this.routeName = routeName;
    }
    
    public String getRoute() {
        return routeName;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public String getLocation() {
        return location;
    }

    // ScreenController methods //
    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }

    /**
     * This method is run every time this screen is selected.
     * 
     * At the moment, it simply locks the mouse to the screen and tells the player
     * to enjoy DyNaDeMo. It also calls the startRoute method on narrativeInstance
     * and giving it the canonical name of the route that the player selected
     * from the character select screen.
     */
    @Override
    public void onStartScreen() {
        // Bind the mouse to the screen so it is used to rotate the camera
        app.getFlyByCamera().setEnabled(true);
        app.getFlyByCamera().setDragToRotate(false); // tr393  - I don't know why we need this
        
        // TODO: load in maps based on data (eg, selected character etc.)
        setDialogueTextSequence(new String[] { "Press \"e\" to scroll through text.", "You are playing as " + character + ".", "Please enjoy DyNaDeMo!" });
    }

    /**
     * This method is run every time this screen is deselected.
     */
    @Override
    public void onEndScreen() {
    }

    // AbstractAppState methods //
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (MainApplication) app;
        narrativeInstance = this.app.getNarrativeInstance();
    }

    @Override
    public void update(float tpf) {
        if (isEnabled()) {
        }
    }
}
