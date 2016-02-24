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
 */
public class MainMenuScreen extends AbstractAppState implements ScreenController {

    private Nifty nifty;
    private Screen screen;
    private MainApplication app;

    public MainMenuScreen() {
        super();
    }

    public MainMenuScreen init(AppStateManager stateManager, Application app) {
        initialize(stateManager, app);
        return this;
    }

    public void startGame() {
        nifty.gotoScreen("game");
    }

    public void quitGame() {
        app.stop();
    }

    public String getPlayerName() {
        return System.getProperty("user.name");
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
    }

    @Override
    public void onEndScreen() {
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
