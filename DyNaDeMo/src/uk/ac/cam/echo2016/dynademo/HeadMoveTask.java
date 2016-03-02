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
    private SyncPointEvent callBack;
    
    public HeadMoveTask(String taskQueueId, float completionTime, HeadObject headOjb, MainApplication app, SyncPointEvent callBack) {
        super(taskQueueId, completionTime);
        this.headObj = headOjb;
        this.app = app;
        this.callBack = callBack;
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
            Vector3f displacement3 = app.getPlayerControl().getPhysicsLocation().subtract(headObj.getSpatial().getLocalTranslation()).add(new Vector3f(0,MainApplication.HALFCHARHEIGHT*3/4,0));
            Vector3f upDir = displacement3.cross(Vector3f.UNIT_Y);
            upDir.crossLocal(displacement3);
            System.out.println(upDir);
            app.getCamera().lookAtDirection(displacement3.negate(), upDir);
            app.getFlyByCamera().setEnabled(false);
            app.setSpeed(0);
            app.getInputManager().setCursorVisible(false);
            break;
        default:
            app.setFlickering(false);
            app.setSpeed(1);
            app.getInputManager().setCursorVisible(true);
            toMove.multLocal(0f);
            callBack.performAction(app);
        }
        headObj.getSpatial().move(toMove);
        
        Vector3f displacement2 = app.getPlayerControl().getPhysicsLocation().subtract(headObj.getSpatial().getLocalTranslation());
        float angle = FastMath.atan(displacement2.z/-displacement2.x);
                
        if (displacement2.x > 0) {
            angle = FastMath.PI + angle;
        }
        headObj.getSpatial().setLocalRotation(new Quaternion().fromAngleAxis(angle, Vector3f.UNIT_Y));

        moveCount++;
    }

}
