package uk.ac.cam.echo2016.dynademo;

import android.os.BaseBundle;

public class AddPropertyTask extends DemoTask {
    Object property;
    
    public AddPropertyTask(String taskQueueId, float completionTime, BaseBundle properties, Object property) {
        super(taskQueueId, completionTime);
        this.property = property;
    }
    
    @Override
    public void complete() {
        
    }
}
