/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.cam.echo2016.dynademo.screens;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.util.ArrayList;
import java.util.List;
import uk.ac.cam.echo2016.dynademo.MainApplication;
import uk.ac.cam.echo2016.multinarrative.io.SaveReader;
import uk.ac.cam.echo2016.multinarrative.NarrativeInstance;
import uk.ac.cam.echo2016.multinarrative.NarrativeTemplate;

/**
 *
 * @author moosichu
 */
public class CharacterSelectScreen extends AbstractAppState implements
        ScreenController {

    private Nifty nifty;
    private Screen screen;
    private MainApplication app;
    private List<String> currentChars;

    public CharacterSelectScreen() {
        super();
    }

    public void selectCharacter(String character) {
        System.out.println("Playing as " + character);
        // TODO: choose route based on character and start the game...
        GameScreen gameScreen = (GameScreen) nifty.getScreen("game")
                .getScreenController();
        gameScreen.setCharacter(character);
        nifty.gotoScreen("game");
    }

    // ScreenController methods //
    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }

    @Override
    public void onStartScreen() {
        app.getFlyByCamera().setDragToRotate(true);
        currentChars = new ArrayList<>();

        // This piece of code should be loading in routes from dynamic narrative
        {
            currentChars.add("Brandon");
            currentChars.add("Dooby");
        }

        // Get the bottom panel so we can insert character buttons
        Element bottomPanel = nifty.getCurrentScreen().findElementByName(
                "panel_bottom");

        for (final String character : currentChars) {
            // Add character button to the screen
            PanelBuilder p = new PanelBuilder("character_" + character) {
                {
                    childLayoutCenter();
                    valignCenter();
                    backgroundColor("#0008");
                    height("25%");
                    width("25%");

                    control(new ButtonBuilder("button_character_" + character,
                            character) {
                        {
                            alignCenter();
                            valignCenter();
                            height("50%");
                            width("50%");
                            visibleToMouse(true);

                            interactOnClick("selectCharacter(" + character
                                    + ")");
                        }
                    });
                }
            };
            bottomPanel.add(p.build(nifty, screen, bottomPanel));
        }
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
}
