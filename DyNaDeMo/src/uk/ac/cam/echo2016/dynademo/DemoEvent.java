package uk.ac.cam.echo2016.dynademo;

/**
 *
 * @author tr393
 */
public abstract class DemoEvent {

    private final String id;

    public DemoEvent(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public abstract void onDemoEvent(MainApplication app);
}
