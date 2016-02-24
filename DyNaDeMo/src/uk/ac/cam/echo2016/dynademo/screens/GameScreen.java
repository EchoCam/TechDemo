/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.cam.echo2016.dynademo.screens;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import de.lessvoid.nifty.Nifty;
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

    // ScreenController methods //
    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }

    @Override
    public void onStartScreen() {
        app.getFlyByCamera().setDragToRotate(false);
        //TODO: load in maps based on data (eg, selected character etc.)
    }

    @Override
    public void onEndScreen() {
    }

    // AbstractAppState methods //
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (MainApplication) app;
    }
    //@Override
    //public void update(float tpf) {
    //    if (screen.getScreenId().equals("hud")) {
    //        Element niftyElement = nifty.getCurrentScreen().findElementByName("score");
    //        // Display the time-per-frame -- this field could also display the score etc...
    //        niftyElement.getRenderer(TextRenderer.class).setText((int) (tpf * 100000) + "");
    //    }
    //}
}
