package uk.ac.cam.echo2016.dynademo.screens;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import uk.ac.cam.echo2016.dynademo.MainApplication;

/**
 *
 */
public class PauseMenuScreen extends AbstractAppState implements ScreenController {

    private Nifty nifty;
    private Screen screen;
    private MainApplication app;

    public PauseMenuScreen() {
        super();
    }

    public void resumeGame() {
        nifty.gotoScreen("game");
    }

    public void goToMenu() {
        //TODO: present some kind of warning saying that unsaved data will be lost
        //Cleanup running game (terminate it)
        nifty.gotoScreen("mainMenu");
    }

    // ScreenContoller methods //
    /**
     *
     * @param nifty
     * @param screen
     */
    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }

    @Override
    public void onStartScreen() {
        app.getFlyByCamera().setDragToRotate(true);
        app.setIsPaused(true);
    }

    @Override
    public void onEndScreen() {
        app.setIsPaused(false);
    }

    // AbstractAppState methods //
    /**
     *
     * @param stateManager
     * @param app
     */
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (MainApplication) app;
    }
}
