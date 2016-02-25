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
import uk.ac.cam.echo2016.dynademo.MainApplication;

/**
 *
 * @author moosichu
 */
public class GameScreen extends AbstractAppState implements ScreenController {

    private Nifty nifty;
    private Screen screen;
    private MainApplication app;

    public GameScreen() {
        super();
    }
    
    public void setDialogueText(String text) {
        Element textElement = nifty.getCurrentScreen().findElementByName("dialogue_box_text");
        textElement.getRenderer(TextRenderer.class).setText(text);
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
        //Bind the mouse to the screen so it is used to rotate the camera
        app.getFlyByCamera().setDragToRotate(false);
        //TODO: load in maps based on data (eg, selected character etc.)
        setDialogueText("Hahahaaha");
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
