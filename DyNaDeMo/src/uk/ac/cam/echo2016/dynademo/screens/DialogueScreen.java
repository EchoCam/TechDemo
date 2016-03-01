package uk.ac.cam.echo2016.dynademo.screens;

import org.w3c.dom.NodeList;

import uk.ac.cam.echo2016.dynademo.DemoDialogue;
import uk.ac.cam.echo2016.dynademo.MainApplication;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import uk.ac.cam.echo2016.dynademo.DialogueEvent;

public class DialogueScreen extends AbstractAppState implements ScreenController {

    private Nifty nifty;
    private MainApplication app;
    private Screen screen;
    private DemoDialogue dialogue;
    private DialogueEvent eventOnClose;

    public DialogueScreen() {
        super();
        
    }
    
    public void setEventOnClose(DialogueEvent theEvent) {
        eventOnClose = theEvent;
    }

    public void setDialogue(String dialogue_location) {
        dialogue = new DemoDialogue(this.getClass().getResourceAsStream("dialogues/" + dialogue_location + ".xml"));
        eventOnClose = null;
    }
    
    public void jumpToDialogue(String dialogId) {
        dialogue.jumpToDialogue(dialogId);
    }

    public void chooseOption(String index) {
        int i = Integer.parseInt(index);
        System.out.println("Choose option: " + i);
        if (dialogue.hasOptions()) {
            NodeList options = dialogue.getDialogueOptionsNodes();
            if (i < options.getLength()) {
                org.w3c.dom.Element elem = (org.w3c.dom.Element) options.item(i);
                String id = elem.getAttribute("nextID");
                System.out.println(id);
                dialogue.moveToNextDialogue(id);        
                advanceText();
            }
        }
    }
    
    public boolean isEnd() {
        return dialogue.isEnd();
        
    }
    
    public String process(String text) {
        return text;
    }
 
    public void advanceText() {
        if (this.isEnd()) {
            if (eventOnClose != null) {
                eventOnClose.onDemoEvent(app);
            } else {
                nifty.gotoScreen("game");
            }
        }
        String text = process(dialogue.getDialogueText());
        String chara = process(dialogue.getSpeakingCharacter());

        if (dialogue.hasOptions()) {
            final NodeList options = dialogue.getDialogueOptionsNodes();
            System.out.println(options.getLength());
            for (int i = 0; i < options.getLength(); i++) {
                Element opt = nifty.getScreen("dialogue").findElementByName("option" + i);
                opt.getRenderer(TextRenderer.class).setText(process(options.item(i).getTextContent()));
            }   
        } else {
            for (int i = 0; i < 3; i++) {
                Element opt = nifty.getScreen("dialogue").findElementByName("option" + i);
                opt.getRenderer(TextRenderer.class).setText("");
            }
        }
        Element container = nifty.getScreen("dialogue").findElementByName("dialogue-container");
        Element textpanel = container.findElementByName("diag-bottom")
                .findElementByName("dialogue-panel");
        textpanel.getRenderer(TextRenderer.class).setText(text);
        Element charnamepanel = container.findElementByName("diag-top").findElementByName("character-name-panel");
        charnamepanel.getRenderer(TextRenderer.class).setText(chara);
        if (!dialogue.hasOptions()) {
            dialogue.moveToNextDialogue();
        }
        

    }

    public void setCharacter(String name) {
        dialogue.setCharacter(name);
        //advanceText();
    }

    // ScreenController methods //
    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }

    @Override
    public void onEndScreen() {
        app.chooseLocation(app.getGameScreen().getLocation());
        app.unPauseDemo();
    }

    @Override
    public void onStartScreen() {
        app.getFlyByCamera().setEnabled(false);
        app.getFlyByCamera().setDragToRotate(true);
        app.pauseDemo();
        advanceText();

    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (MainApplication) app;
    }
}
