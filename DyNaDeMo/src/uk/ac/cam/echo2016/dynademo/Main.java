package uk.ac.cam.echo2016.dynademo;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 * @author tr93
 */
public class Main extends SimpleApplication implements ActionListener {
    
    private BulletAppState bulletAppState;
    private RigidBodyControl landscape;
    private CharacterControl playerControl;
    private Vector3f camDir = new Vector3f();
    private Vector3f camLeft = new Vector3f();
    private Vector3f walkDirection = new Vector3f();
    private boolean keyLeft = false, keyRight = false, keyUp = false, keyDown = false;
    
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    
    private void setupKeys() {
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
        
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_DOWN));
        
        inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(this, "Left");
        inputManager.addListener(this, "Right");
        inputManager.addListener(this, "Up");
        inputManager.addListener(this, "Down");
        inputManager.addListener(this, "Jump");
    }
    
    @Override
    public void simpleInitApp() {
//        /**
//         * Activate the Nifty-JME integration: 
//         */
//        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(
//                assetManager, inputManager, audioRenderer, guiViewPort);
//        Nifty nifty = niftyDisplay.getNifty();
//        guiViewPort.addProcessor(niftyDisplay);
//        flyCam.setDragToRotate(true); // you need the mouse for clicking now    
//        //nifty.setDebugOptionPanelColors(true);
//        nifty.fromXml("Interface/tutorial/screen2.xml", "start");
        
        
        
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        
        viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
//        flyCam.setMoveSpeed(40f);
        
        setupKeys();
//        Spatial scene = assetManager.loadModel("Models/Scene1.j3o");
//        scene.scale(10f);
//        rootNode.attachChild(scene);

//        Spatial scene = assetManager.loadModel("Models/Scene_v102.j3o");
        Spatial scene = assetManager.loadModel("Textures/Table.mesh.xml.scene");
        scene.scale(15f);
        rootNode.attachChild(scene);
        
        
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(1.3f));
        rootNode.addLight(al);

        DirectionalLight dl = new DirectionalLight();
        dl.setColor(ColorRGBA.White);
        dl.setDirection(new Vector3f(2.8f, -2.8f, -2.8f).normalizeLocal());
        rootNode.addLight(dl);
        
        DirectionalLight d2 = new DirectionalLight();
        d2.setColor(ColorRGBA.White);
        
        d2.setDirection(new Vector3f(0f, -2.8f, 0f).normalizeLocal());
        rootNode.addLight(d2);

        
        CollisionShape sceneShape = CollisionShapeFactory.createMeshShape(scene);
        landscape = new RigidBodyControl(sceneShape, 0f);
        scene.addControl(landscape);
        
        CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(2f, 4f, 1);
        playerControl = new CharacterControl(capsuleShape, 0.2f);
        playerControl.setJumpSpeed(20);
        playerControl.setFallSpeed(30);
        playerControl.setGravity(30);
        playerControl.setPhysicsLocation(new Vector3f(0, 3f, 0));
        bulletAppState.getPhysicsSpace().add(landscape);
        bulletAppState.getPhysicsSpace().add(playerControl);
        
    }
    
    @Override
    public void simpleUpdate(float tpf) {
        camDir.set(cam.getDirection().multLocal(.4f));
        camLeft.set(cam.getLeft().multLocal(.4f));
        walkDirection.set(0, 0, 0);
        if (keyLeft) {
            walkDirection.addLocal(camLeft);
        }
        if (keyRight) {
            walkDirection.addLocal(camLeft.negate());
        }
        if (keyUp) {
            walkDirection.addLocal(camDir.x, 0, camDir.z);
        }
        if (keyDown) {
            walkDirection.addLocal(-camDir.x, 0, -camDir.z);
        }
        playerControl.setWalkDirection(walkDirection);
        cam.setLocation(playerControl.getPhysicsLocation().add(0, 4f, 0));
    }
    
    @Override
    public void onAction(String keyName, boolean isPressed, float tpf) {
        if (keyName.equals("Left")) {
            keyLeft = isPressed;
        } else if (keyName.equals("Right")) {
            keyRight = isPressed;
        } else if (keyName.equals("Up")) {
            keyUp = isPressed;
        } else if (keyName.equals("Down")) {
            keyDown = isPressed;
        } else if (keyName.equals("Jump")) {
            if (isPressed) playerControl.jump();
        }
    }

//    @Override
//    public void simpleRender(RenderManager rm) {
//        //TODO: add render code
//    }
}
