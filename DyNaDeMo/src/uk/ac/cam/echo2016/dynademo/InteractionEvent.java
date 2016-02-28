package uk.ac.cam.echo2016.dynademo;

/**
 * @author tr393
 */
public class InteractionEvent extends DemoEvent {
    private final InteractableObject object;
    /**
     * This is used to store events occuring on interaction, e.g. opening doors, pressing a button, etc.
     * 
     * @param id
     *            - Final String id of the event used to identify it.
     * @param spatial
     *            - The Spatial object to be affected by the event
     */
    public InteractionEvent(String id, InteractableObject object) {
        super(id);
        this.object = object;
    }
    
    @Override
    public void onDemoEvent(MainApplication app) {
        object.interact(app);
    }

    public InteractableObject getObject() {
        return object;
    }
}
