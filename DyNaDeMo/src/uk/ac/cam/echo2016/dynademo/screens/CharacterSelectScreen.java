/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.cam.echo2016.dynademo.screens;

import android.os.BaseBundle;
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
import uk.ac.cam.echo2016.dynademo.MainApplication;
import uk.ac.cam.echo2016.multinarrative.NarrativeInstance;
import uk.ac.cam.echo2016.multinarrative.Route;

/**
 *
 * @author moosichu
 */
public class CharacterSelectScreen extends AbstractAppState implements ScreenController {

    private Nifty nifty;
    private Screen screen;
    private MainApplication app;
    private NarrativeInstance narrativeInstance;

    public CharacterSelectScreen() {
        super();
    }

    public void selectRoute(String routeName, String character) {
        System.out.println("Playing as " + character);
        // TODO: Choose location loaded based on route
        GameScreen gameScreen = (GameScreen) nifty.getScreen("game").getScreenController();
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

        ArrayList<Route> currentRoutes = narrativeInstance.getPlayableRoutes();

        // Get the bottom panel so we can insert character buttons
        Element bottomPanel = nifty.getCurrentScreen().findElementByName("panel_bottom");

        for (final Route route : currentRoutes) {
            final String routeName = route.toString();
            
            //TODO: null check, and get character name from properties
            BaseBundle b = route.getProperties();
            if(b == null) {
                throw new RuntimeException("The route: " + routeName + " doesn't have any properties.");
            }
            
            System.out.println(b.get("Char1") +" + "+ b.get("Char2"));
            if(b.containsKey("Char1")) System.out.println(b.get("Char1").getClass().getSimpleName());
            if(b.containsKey("Char2")) System.out.println(b.get("Char2").getClass().getSimpleName());

            boolean char1 = b.getBoolean("Char1");
            boolean char2 = b.getBoolean("Char2");
            final String character = char1 ? char2? "Timangelise and Tojamobin" : "Timangelise" : char2?  "Tojamobin": "None";

            
            // Add character button to the screen
            PanelBuilder p = new PanelBuilder("route_" + routeName) {
                {
                    childLayoutCenter();
                    valignCenter();
                    backgroundColor("#0008");
                    height("25%");
                    width("25%");

                    control(new ButtonBuilder("button_route_" + routeName, character) {
                        {
                            alignCenter();
                            valignCenter();
                            height("50%");
                            width("50%");
                            visibleToMouse(true);

                            interactOnClick("selectRoute(" + routeName + ", " + character + ")");
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
        this.narrativeInstance = this.app.getNarrativeInstance();
    }
}
