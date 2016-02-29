package uk.ac.cam.echo2016.dynademo;

import android.os.BaseBundle;
import java.util.HashMap;

public class AddPropertyTask extends DemoTask {

    BaseBundle properties;
    String key;
    Object property;

    public AddPropertyTask(String taskQueueId, float completionTime, BaseBundle properties, String key, Object property) {
        super(taskQueueId, completionTime);
        this.properties = properties;
        this.key = key;
        this.property = property;
    }

    @Override
    public void onComplete() {
        HashMap map = new HashMap<>();
        map.put(key, property);
        properties.putAll(map);
    }
}
