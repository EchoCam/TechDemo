package uk.ac.cam.echo2016.dynademo;

import com.jme3.bounding.BoundingBox;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.cam.echo2016.multinarrative.GraphElementNotFoundException;

public class SyncPointEvent extends LocationEvent {

        public SyncPointEvent(String id, BoundingBox bound) {
            super(id, bound);
        }

        @Override
        public void onDemoEvent(MainApplication app) {
            try {
                //Ending the route that was started to show the correct character select screen to the player
                app.getNarrativeInstance().startRoute(app.getGameScreen().getRoute());
                app.getNarrativeInstance().endRoute(app.getGameScreen().getRoute());
            } catch (GraphElementNotFoundException ex) {
                Logger.getLogger(Initialiser.class.getName()).log(Level.SEVERE, null, ex);
            }
            app.nifty.gotoScreen("characterSelect");
        }
    }