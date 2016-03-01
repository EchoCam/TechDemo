/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.cam.echo2016.dynademo;

import com.jme3.bounding.BoundingBox;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.cam.echo2016.multinarrative.GameChoice;
import uk.ac.cam.echo2016.multinarrative.GraphElementNotFoundException;
import uk.ac.cam.echo2016.multinarrative.Route;

    /**
     * As in this game, all choicepoints are immediatlely followed by a SyncPoint from a game-play perspective, this
     * represents a combination of both.
     * 
     * 
     */
public class SyncAfterChoiceEvent extends LocationEvent {

        private boolean actionTaken = false;
        private String routeIfTrue;
        private String routeIfFalse;

        public SyncAfterChoiceEvent(String id, BoundingBox bound, String RouteIfActionTaken, String RouteOtherwise) {
            super(id, bound);
            routeIfTrue = RouteIfActionTaken;
            routeIfFalse = RouteOtherwise;
        }

        public boolean getActionTaken() {
            return actionTaken;
        }

        public void setActionTaken(boolean isAction) {
            actionTaken = isAction;
            System.out.println("actionTaken is now: " + actionTaken);
        }

        @Override
        public void onDemoEvent(MainApplication app) {
            try {
                app.getNarrativeInstance().startRoute(app.getGameScreen().getRoute());
                GameChoice gameChoice = app.getNarrativeInstance().endRoute(app.getGameScreen().getRoute());
                
                List<Route> options = gameChoice.getOptions();
                boolean routeIfTrueFromOptions = false;
                boolean routeIfFalseFromOptions = false;
                
                //Iterate through all the options and see if they match up with the routes we think they should be
                for(Route option: options) {
                    if(option.getId().equals(routeIfTrue)) {
                        routeIfTrueFromOptions = true;
                    } else if(option.getId().equals(routeIfFalse)) {
                        routeIfFalseFromOptions = true;
                    }
                }
                if(!routeIfTrueFromOptions || !routeIfFalseFromOptions)
                    System.out.println("The choices available don't match the routes that we think are avaialble.");
                if (actionTaken) {
                    app.getNarrativeInstance().startRoute(routeIfTrue);
                    app.getNarrativeInstance().endRoute(routeIfTrue);
                } else {
                    app.getNarrativeInstance().startRoute(routeIfFalse);
                    app.getNarrativeInstance().endRoute(routeIfFalse);
                }
            } catch (GraphElementNotFoundException ex) {
                Logger.getLogger(Initialiser.class.getName()).log(Level.SEVERE, null, ex);
            }
            app.getNifty().gotoScreen("characterSelect");
        }
    }

