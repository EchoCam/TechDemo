package uk.ac.cam.echo2016.dynademo;

import uk.ac.cam.echo2016.dynademo.screens.DialogueScreen;

import com.jme3.bounding.BoundingVolume;
import com.jme3.scene.Spatial;

/**
 *
 * @author tr393
 */
public class HeadObject implements InteractableObject {

    @Override
    public void interact(MainApplication app) {
        DialogueScreen dialoguescreen = app.getDialogueScreen();
        //TODO: get dialogue screen, load Char2 Dies.xml and use "You1" as entry point
        dialoguescreen.setDialogue("Char2 Dies");
        dialoguescreen.jumpToDialogue("You1");
        app.getNifty().gotoScreen("dialogue");
    }
}
