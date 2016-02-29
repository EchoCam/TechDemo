package uk.ac.cam.echo2016.dynademo.screens;

import org.w3c.dom.NodeList;

import uk.ac.cam.echo2016.dynademo.DemoDialogue;
import uk.ac.cam.echo2016.dynademo.MainApplication;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
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

    public void chooseOption(int i) {
        NodeList options = dialogue.getDialogueOptionsNodes();
        org.w3c.dom.Element elem = (org.w3c.dom.Element) options.item(i);
        String id = elem.getAttribute("nextID");
        dialogue.moveToNextDialogue(id);
    }

    public void advanceText() {
        String text = dialogue.getDialogueText();
        String chara = dialogue.getSpeakingCharacter();

        if (dialogue.hasOptions()) {
            final NodeList options = dialogue.getDialogueOptionsNodes();
            System.out.println(options.getLength());
            Element optionz = nifty.getScreen("dialogue").findElementByName("diag-options");
            for (int i = 0; i < options.getLength(); i++) {
                final int index = i;
                TextBuilder textbuild = new TextBuilder(Integer.toString(index)) {
                    {
                        text(options.item(index).getTextContent());
                        width("100%");
                        font("Interface/Fonts/Default.fnt");
                        visibleToMouse(true);
                        interactOnClick("chooseOption(" + index + ")");
                    }
                };
                optionz.add(textbuild.build(nifty, screen, optionz));
            }
        }

        Element container = 
                nifty.getScreen("dialogue").findElementByName("foreground").findElementByName("dialogue-container");
        Element textpanel = container.findElementByName("diag-bottom").findElementByName("dialogue-panel");
        textpanel.getRenderer(TextRenderer.class).setText(text);
        Element charnamepanel = container.findElementByName("diag-top").findElementByName("character-name-panel");
        charnamepanel.getRenderer(TextRenderer.class).setText(chara);
        if (!dialogue.hasOptions()) {
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
