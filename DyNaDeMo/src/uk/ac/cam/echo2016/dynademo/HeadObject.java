package uk.ac.cam.echo2016.dynademo;

import com.jme3.bounding.BoundingVolume;
import com.jme3.scene.Spatial;

/**
 *
 * @author tr393
 */
public class HeadObject implements InteractableObject {

    @Override
    public void interact(MainApplication app) {
        app.getDialogueScreen();
        //TODO: get dialogue screen, load Char2 Dies.xml and use "You1" as entry point
    }
}
