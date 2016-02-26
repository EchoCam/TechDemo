package uk.ac.cam.echo2016.dynademo.screens;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;

import uk.ac.cam.echo2016.dynademo.DemoDialogue;
import uk.ac.cam.echo2016.dynademo.MainApplication;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class DialogueScreen extends AbstractAppState implements ScreenController {
    
    private Nifty nifty;
    private Screen screen;
    private MainApplication app;
    private DemoDialogue dialogue;
    
    public DialogueScreen() {
        super();
    }
    
    public void setDialogue(String filepath) {
        dialogue = new DemoDialogue(filepath);
    }
    
    public void setCharacter(String name) {
        dialogue.setCharacter(name);
    }
    
 // ScreenController methods //
    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }

    @Override
    public void onEndScreen() {    
    }

    @Override
    public void onStartScreen() {
        app.getFlyByCamera().setDragToRotate(true);
        
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (MainApplication) app;
    }

}
