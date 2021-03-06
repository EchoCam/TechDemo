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
 * @author tr395
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
        // TODO: present some kind of warning saying that unsaved data will be lost
        // Cleanup running game (terminate it)
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

    /**
     * This method is run every time the screen is selected.
     */
    @Override
    public void onStartScreen() {
        app.getFlyByCamera().setEnabled(false);
        app.pauseDemo();
    }

    /**
     * This method is run every time the screen deselected.
     */
    @Override
    public void onEndScreen() {
        app.unPauseDemo();
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
