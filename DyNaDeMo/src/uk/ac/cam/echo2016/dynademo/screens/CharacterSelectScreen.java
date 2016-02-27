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
 * @author tr395
 */
public class CharacterSelectScreen extends AbstractAppState implements ScreenController {

    private Nifty nifty;
    private Screen screen;
    private MainApplication app;
    private NarrativeInstance narrativeInstance;

    public CharacterSelectScreen() {
        super();
    }

    /**
     * This function is called by clicking on one of the character buttons in this screen.
     * 
     * What it does at the moment, is simply tell the player what character they are playing as,
     * as well as passing the information to GameScreen. GameScreen then actually calls
     * narrativeInstance.startRoute(routeName).
     * 
     * At the moment it just displays whatever level has been loaded by MainApplication. The functionality
     * of actually loading a level based on the choice still needs to be implemented.
     * 
     * @param routeName The canonical name of the route selected by the player
     * @param character The name of the character being played as
     */
    public void selectRoute(String routeName, String character) {
        // TODO: Choose location loaded based on route
        GameScreen gameScreen = (GameScreen) nifty.getScreen("game").getScreenController();
        gameScreen.setCharacter(character);
        gameScreen.setRoute(routeName);
        nifty.gotoScreen("game");
    }

    // ScreenController methods //
    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }

    /**
     * This function is called every time the character screen is displayed.
     * 
     * It looks at the narrativeInstance and gets all the playable routes, then it
     * simply iterates through them and displays them to the player.
     * 
     * It also looks at the character properties of the shown routes, and then based on
     * that decides which name to show the player. As the character property is a boolean
     * in dnm file, the names are hardcoded and chosen based on the values of that.
     */
    @Override
    public void onStartScreen() {
        app.getFlyByCamera().setEnabled(false);

        ArrayList<Route> currentRoutes = narrativeInstance.getPlayableRoutes();

        // Get the bottom panel so we can insert character buttons
        Element bottomPanel = nifty.getCurrentScreen().findElementByName("panel_bottom");
        
        // Flush it of all old character buttons from last time shown
        for(Element e: bottomPanel.getElements()) {
            e.markForRemoval();
        }
        bottomPanel.layoutElements();

        // Add new routes to show
        for (final Route route : currentRoutes) {
            final String routeName = route.toString();
            
            //TODO: null check, and get character name from properties
            BaseBundle b = route.getProperties();
            if(b == null) {
                throw new RuntimeException("Error: The route: " + routeName + " doesn't have any properties.");
            }

            // Get which character is playable on each route and show based on that
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
        app.chooseRoute(app.getGameScreen().getRoute());
    }

    // AbstractAppState methods //
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (MainApplication) app;
        this.narrativeInstance = this.app.getNarrativeInstance();
    }
}
