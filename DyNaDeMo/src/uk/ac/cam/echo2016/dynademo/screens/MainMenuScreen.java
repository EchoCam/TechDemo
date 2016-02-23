package uk.ac.cam.echo2016.dynademo.screens;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 *
 */
public class MainMenuScreen extends AbstractAppState implements ScreenController {

  private Nifty nifty;
  private Screen screen;
  
  private SimpleApplication app;


  public MainMenuScreen() {
      super();
  }

  public void startGame() {
    app.getFlyByCamera().setDragToRotate(false);
    nifty.gotoScreen("game");
  }

  public void quitGame() {
    app.stop();
  }

  public String getPlayerName() {
    return System.getProperty("user.name");
  }

  /** Nifty GUI ScreenControl methods */
  public void bind(Nifty nifty, Screen screen) {
    this.nifty = nifty;
    this.screen = screen;
  }

  public void onStartScreen() {
  }

  public void onEndScreen() {
  }

  /** jME3 AppState methods */
  @Override
  public void initialize(AppStateManager stateManager, Application app) {
    super.initialize(stateManager, app);
    this.app = (SimpleApplication) app;
  }

  @Override
  public void update(float tpf) {
    if (screen.getScreenId().equals("hud")) {
      Element niftyElement = nifty.getCurrentScreen().findElementByName("score");
      // Display the time-per-frame -- this field could also display the score etc...
      niftyElement.getRenderer(TextRenderer.class).setText((int)(tpf*100000) + ""); 
    }
  }
}

