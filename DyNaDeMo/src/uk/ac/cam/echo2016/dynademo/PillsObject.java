/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.cam.echo2016.dynademo;

import com.jme3.scene.Spatial;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.cam.echo2016.multinarrative.GraphElementNotFoundException;

/**
 *
 * @author Rjmcf
 */
public class PillsObject extends StaticDemoObject implements InteractableObject {
    SyncPointEvent spe;
    
    public PillsObject(String objId, Spatial spatial, boolean isMainParent, SyncPointEvent spe) {
        super(objId, spatial, isMainParent);
        this.spe = spe;
    }

    @Override
    public void interact(MainApplication app) {
        spe.performAction(app);
    }
    
}
