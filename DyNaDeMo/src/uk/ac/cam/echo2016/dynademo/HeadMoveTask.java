package uk.ac.cam.echo2016.dynademo;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

public class HeadMoveTask extends DemoTask {
    private HeadObject headObj;
    private boolean prevLight = false;
    private int prevLightOffCount = 0;
    private int moveCount = 0;
    private MainApplication app;
    
    public HeadMoveTask(String taskQueueId, float completionTime, HeadObject headOjb, MainApplication app) {
        super(taskQueueId, completionTime);
        this.headObj = headOjb;
        this.app = app;
    }
    
    @Override
    public void onTimeStep(float timePassed){
        if (app.getLightsOn() && prevLight == false && prevLightOffCount > 1) {
            System.out.println(prevLightOffCount);
            moveHead();
        }
        resetTime();
        prevLight = app.getLightsOn();
        prevLightOffCount = app.getLightsOffCount();
    }
    public void moveHead() {
        Vector3f displacement = app.getPlayerControl().getPhysicsLocation().subtract(headObj.getSpatial().getLocalTranslation());
        System.out.println("Displacement" + displacement);
        
        float angle = FastMath.asin(displacement.z/displacement.x);
        System.out.println("angle " + angle);
        if (displacement.z*displacement.x > 0) {
            angle = 180 - angle;
        }
        headObj.getSpatial().setLocalRotation(new Quaternion().fromAngleAxis(angle, Vector3f.UNIT_Y));
        
        Vector3f toMove = displacement.clone();
        switch (moveCount) {
        case 0:
            toMove.multLocal(1f/5f);
            break;
        case 1:
            toMove.multLocal(1f/4f);
            break;
        case 2:
            toMove.multLocal(1f/3f);
            break;
        case 3:
            toMove.multLocal(1f/2f);
            break;
        default:
            toMove.multLocal(0f);
        }
        headObj.getSpatial().move(toMove);
            
        moveCount++;
    }

}
