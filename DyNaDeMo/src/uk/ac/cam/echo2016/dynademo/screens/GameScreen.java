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

/**
 *
 * @author moosichu
 */
public class GameScreen extends AbstractAppState implements ScreenController {

    private Nifty nifty;
    private Screen screen;
    private MainApplication app;
    private String character; // temp variable just to show variable passing
    private boolean textShowing = false;
    private Deque<String> dialogueDeque = new ArrayDeque<>();

    public GameScreen() {
        super();
    }

    /**
     * Sets the text that is displayed in the dialogue box.
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

    public void setDialogueTextSequence(String[] textSequence) {
        for (int i = 0; i < textSequence.length; i++) {
            dialogueDeque.addLast(textSequence[i]);
        }
        if (!isTextShowing()) {
            progressThroughText();
        }
    }

    /**
     * Flushes all the text queued and removes text being displayed.
     */
    public void flushDialogueTextSequence() {
        dialogueDeque.clear();
        progressThroughText();
    }

    public boolean isTextShowing() {
        return textShowing;
    }

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

    // ScreenController methods //
    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }

    /**
     * This method is run every time this screen is selected.
     */
    @Override
    public void onStartScreen() {
        // Bind the mouse to the screen so it is used to rotate the camera
        app.getFlyByCamera().setDragToRotate(false);
        // TODO: load in maps based on data (eg, selected character etc.)
        setDialogueTextSequence(new String[] { "You are playing as " + character, "Please enjoy DyNaDeMo" });
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
    }

    @Override
    public void update(float tpf) {
        if (isEnabled()) {
        }
    }
}
