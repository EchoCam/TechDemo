package uk.ac.cam.echo2016.dynademo.screens;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;

import uk.ac.cam.echo2016.dynademo.DemoDialogue;
import uk.ac.cam.echo2016.dynademo.MainApplication;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;

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
    
    public void advanceText() {
        if (!dialogue.hasOptions()) {
            String text = dialogue.getDialogueText();
            System.out.println("text = " + text);
            Screen screen = nifty.getScreen("dialogue");
            Element foreground = screen.findElementByName("foreground");
            Element container = foreground.findElementByName("dialogue-container").findElementByName("dialogue-panel");
            TextRenderer textRenderer = container.getRenderer(TextRenderer.class);
            textRenderer.setText(text);
            dialogue.moveToNextDialogue();
        }
        
        
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
