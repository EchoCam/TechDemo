package uk.ac.cam.echo2016.dynademo;

import uk.ac.cam.echo2016.dynademo.screens.DialogueScreen;

import com.jme3.scene.Spatial;

/**
 *
 * @author tr393
 */
public class HeadObject extends KinematicDemoObject implements InteractableObject {
    
    public HeadObject(String objId, Spatial spatial, boolean isMainParent) {
        super(objId, spatial, 1000f, isMainParent, null);
    }

    @Override
    public void interact(MainApplication app) {
        DialogueScreen dialoguescreen = app.getDialogueScreen();
        //TODO: get dialogue screen, load Char2 Dies.xml and use "You1" as entry point
        dialoguescreen.setDialogue("Char2dies");
        dialoguescreen.setCharacter("You");
        dialoguescreen.jumpToDialogue("Head1");
        app.getNifty().gotoScreen("dialogue");
    }
}
