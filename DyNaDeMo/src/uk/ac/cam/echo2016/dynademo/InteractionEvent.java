package uk.ac.cam.echo2016.dynademo;

/**
 * @author tr393
 */
public abstract class InteractionEvent extends DemoEvent {
    private final DemoObject object;
    /**
     * This is used to store events occuring on interaction, e.g. opening doors, pressing a button, etc.
     * 
     * @param id
     *            - Final String id of the event used to identify it.
     * @param spatial
     *            - The Spatial object to be affected by the event
     */
    public InteractionEvent(String id, DemoObject object) {
        super(id);
        this.object = object;
    }

    public DemoObject getObject() {
        return object;
    }
}
