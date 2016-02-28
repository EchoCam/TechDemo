package uk.ac.cam.echo2016.dynademo;

import java.util.ArrayDeque;
import java.util.List;

import uk.ac.cam.echo2016.dynademo.screens.CharacterSelectScreen;
import uk.ac.cam.echo2016.dynademo.screens.GameScreen;
import uk.ac.cam.echo2016.dynademo.screens.MainMenuScreen;
import uk.ac.cam.echo2016.dynademo.screens.PauseMenuScreen;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.shadow.AbstractShadowRenderer;
import com.jme3.system.AppSettings;

import de.lessvoid.nifty.Nifty;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import uk.ac.cam.echo2016.multinarrative.InvalidGraphException;
import uk.ac.cam.echo2016.multinarrative.NarrativeInstance;
import uk.ac.cam.echo2016.multinarrative.NarrativeTemplate;
import uk.ac.cam.echo2016.multinarrative.io.SaveReader;

/**
 * @author tr393
 */
@SuppressWarnings("deprecation")
public class MainApplication extends SimpleApplication implements ActionListener {

    public final static float HALFCHARHEIGHT = 3;
    public HashMap<String, DemoRoute> routes = new HashMap<>();
    private Node playerNode;
    private BulletAppState bulletAppState;
    private RigidBodyControl landscape;
    private CharacterControl playerControl;
    private GhostControl billMurray;
    private Vector3f camDir = new Vector3f();
    private Vector3f camLeft = new Vector3f();
    private Vector3f walkDirection = new Vector3f();
    private Spatial draggedObject;
    private boolean keyLeft = false, keyRight = false, keyUp = false, keyDown = false;
    private boolean isPaused = false;
    NiftyJmeDisplay pauseDisplay;
    private ArrayDeque<LocationEvent> locEventBus = new ArrayDeque<>();
    private HashMap<String, ArrayDeque<DemoTask>> taskEventBus = new HashMap<>();
    private Spatial currentWorld;
    private DemoRoute currentRoute;
    // private currentRoute/Character
    public Nifty nifty;
    // Screens
    private MainMenuScreen mainMenuScreen;
    private CharacterSelectScreen characterSelectScreen;
    private PauseMenuScreen pauseMenuScreen;
    private GameScreen gameScreen;
    private NarrativeInstance narrativeInstance;

    public static void main(String[] args) {
        MainApplication app = new MainApplication();
        AppSettings m_Settings = new AppSettings(true);
        m_Settings.setFrameRate(75);
        app.setSettings(m_Settings);
        app.start();
    }

    public MainApplication() {
        super();
        try {
            InputStream is = this.getClass().getResourceAsStream("dynademo.dnm");
            NarrativeTemplate narrativeTemplate = SaveReader.loadNarrativeTemplate(is);
            narrativeInstance = narrativeTemplate.generateInstance();
        } catch (IOException | InvalidGraphException ex) {
            Logger.getLogger(MainApplication.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }

    }

    public NarrativeInstance getNarrativeInstance() {
        return narrativeInstance;
    }

    @Override
    public void simpleInitApp() {
        // Set-Up for the main menu //
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager, inputManager, audioRenderer, guiViewPort);
        nifty = niftyDisplay.getNifty();
        guiViewPort.addProcessor(niftyDisplay);

        nifty.fromXml("Interface/Nifty/mainMenu.xml", "mainMenu", new MainMenuScreen().init(stateManager, this));
        nifty.addXml("Interface/Nifty/characterSelect.xml");
        nifty.addXml("Interface/Nifty/pauseMenu.xml");
        nifty.addXml("Interface/Nifty/game.xml");

        mainMenuScreen = (MainMenuScreen) nifty.getScreen("mainMenu").getScreenController();
        characterSelectScreen = (CharacterSelectScreen) nifty.getScreen("characterSelect").getScreenController();
        pauseMenuScreen = (PauseMenuScreen) nifty.getScreen("pauseMenu").getScreenController();
        gameScreen = (GameScreen) nifty.getScreen("game").getScreenController();

        stateManager.attach(mainMenuScreen);
        stateManager.attach(characterSelectScreen);
        stateManager.attach(pauseMenuScreen);
        stateManager.attach(gameScreen);

        // TODO(tr395): find way to make it so that onStartScreen() isn't called until this point.
        nifty.gotoScreen("mainMenu");

        // Application related setup //
        viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
        setupKeys();

        // Setup world and locEvents //
        routes = Initialiser.initialiseRoutes(this);

        // Initialize physics engine //
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);

        // Add global Lights //

        AmbientLight al = new AmbientLight(); // No current effect on blender scene
        al.setColor(new ColorRGBA(0.1f, 0.1f, 0.1f, 1f));
        rootNode.addLight(al);
        rootNode.setShadowMode(ShadowMode.CastAndReceive);

        // Placeholder routes for later initialization
        currentWorld = new Geometry("placeholder", new Box(1,1,1));
        rootNode.attachChild(currentWorld);
        landscape = new RigidBodyControl();//sceneShape, 0f);
        currentWorld.addControl(landscape);
        bulletAppState.getPhysicsSpace().add(landscape);
        currentRoute = new DemoRoute("", "", null, null);

        // Load character //
        playerNode = new Node("playerNode");
        rootNode.attachChild(playerNode);

        // Attach physic control to the character
        CapsuleCollisionShape capsule = new CapsuleCollisionShape(HALFCHARHEIGHT / 2, HALFCHARHEIGHT, 1);
        playerControl = new CharacterControl(capsule, 0.25f);
        playerControl.setJumpSpeed(20f);
        playerControl.setFallSpeed(30);
        playerControl.setGravity(50);
        playerControl.setPhysicsLocation(new Vector3f(0, HALFCHARHEIGHT + 1.0f, 0)); // 1.0f off the ground
        playerNode.addControl(playerControl);
        bulletAppState.getPhysicsSpace().add(playerControl);

        // Attach ghost control to the character
        billMurray = new GhostControl(capsule);
        playerNode.addControl(billMurray);
        bulletAppState.getPhysicsSpace().add(billMurray);

        // Start at Area 0 //
        loadRoute(routes.get("BedroomRoute"));

        // Debug Options//
//        bulletAppState.setDebugEnabled(true);
//
//        Geometry g = new Geometry("wireframe cube", new WireBox(HALFCHARHEIGHT / 2, HALFCHARHEIGHT, HALFCHARHEIGHT / 2));
//        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
//        mat.getAdditionalRenderState().setWireframe(true);
//        mat.setColor("Color", ColorRGBA.Green);
//        g.setMaterial(mat);
//        playerNode.attachChild(g);
    }
    public void loadRoute(DemoRoute route) {
        // Unload old route (currentRoute)
        currentWorld.removeControl(landscape);
        currentWorld.removeFromParent();
        bulletAppState.getPhysicsSpace().remove(landscape);
        for (DemoObject object : currentRoute.objects) {
            // TODO clean up physicsSpace (save info?)
            if (object.isIsMainParent())
                rootNode.detachChild(object.getSpatial());

            bulletAppState.getPhysicsSpace().remove(object.getSpatial());

            for(DemoLight dLight : object.getLights()) {
                object.getSpatial().removeLight(dLight.light);
            }
        }
        for (DemoLight l : currentRoute.lights) { // FIXME should do a search
            rootNode.removeLight(l.light);
        }
        for (AbstractShadowRenderer plsr : currentRoute.shadowRenderers) {
            viewPort.removeProcessor(plsr);
        }
        for (LocationEvent oldEvent : currentRoute.locEvents) {
            locEventBus.remove(oldEvent);
        }

        // Load new route (route)
        currentRoute = route;
        currentWorld = assetManager.loadModel(route.getSceneFile());
        currentWorld.scale(10f);
        rootNode.attachChild(currentWorld);

        // Load route objects and add rigidbodycontrols
        for (DemoObject object : route.objects) {
            if (object.isIsMainParent())
                rootNode.attachChild(object.getSpatial());

            RigidBodyControl rbc = new RigidBodyControl(object.getMass());
            object.getSpatial().addControl(rbc);
            if (object instanceof KinematicDemoObject) rbc.setKinematic(true);
            if (object instanceof DynamicDemoObject) rbc.setFriction(1.5f);
            bulletAppState.getPhysicsSpace().add(rbc);
            for (DemoLight dLight : object.getLights()) {
                object.getSpatial().addLight(dLight.light);
            }
        }
        for (DemoLight l : route.lights) {
            for (String roomName : l.affectedRooms) {
                // TODO hacky
                List<Spatial> list = rootNode.descendantMatches(roomName);
                if (list.isEmpty()) {
                    throw new RuntimeException("Spatial not found!");
                }
                Spatial room = list.get(0);
                room.addLight(l.light);
            }
        }
//        for (AbstractShadowRenderer plsr : route.shadowRenderers) {
//             viewPort.addProcessor(plsr); // Disabled shadows for now
//        }
        for (LocationEvent newEvent : route.locEvents) {
            locEventBus.add(newEvent);
        }

        CollisionShape sceneShape = CollisionShapeFactory.createMeshShape(currentWorld);
        landscape = new RigidBodyControl(sceneShape, 0f);
        currentWorld.addControl(landscape);
        bulletAppState.getPhysicsSpace().add(landscape);

        // TODO freeze for a second
        playerControl.setPhysicsLocation(route.getStartLoc());
        cam.lookAtDirection(route.getStartDir(), Vector3f.UNIT_Y);
        // TODO other initializations

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
        inputManager.addMapping("Interact", new KeyTrigger(KeyInput.KEY_E));
        inputManager.deleteMapping(INPUT_MAPPING_EXIT); //TODO replace with pause
        inputManager.addMapping("Pause", new KeyTrigger(KeyInput.KEY_ESCAPE));

        inputManager.addListener(this, "Left");
        inputManager.addListener(this, "Right");
        inputManager.addListener(this, "Up");
        inputManager.addListener(this, "Down");
        inputManager.addListener(this, "Interact");
        inputManager.addListener(this, "Jump");
        inputManager.addListener(this, "Pause");
    }

    @Override
    public void simpleUpdate(float tpf) {
//         if (!rootNode.descendantMatches("Models/Crate.blend").isEmpty()) {
//         Spatial spat = rootNode.descendantMatches("Models/Crate.blend").get(0);
//         System.out.println(spat.getName());
//         System.out.println(spat.getWorldTranslation().x);
//         System.out.println(spat.getWorldTranslation().y);
//         System.out.println(spat.getWorldTranslation().z);
//         System.out.println();
//         }
        System.out.println(playerControl.getPhysicsLocation());
        if (!isPaused) {
            // Find direction of camera (and rotation)
            camDir.set(cam.getDirection().normalize());
            camLeft.set(cam.getLeft().normalize());
            // Calculate distance to move
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
            playerControl.setWalkDirection(walkDirection.normalize().mult(25f * tpf));
            // Move camera to correspond to player
            cam.setLocation(playerControl.getPhysicsLocation().add(0, HALFCHARHEIGHT*3/4, 0));

            // Position carried items appropriately
            if (draggedObject != null) {
                float distance = draggedObject.getLocalTranslation().length();
                Vector3f newLoc = camDir.mult(distance);
                draggedObject.setLocalTranslation(newLoc);
                draggedObject.setLocalRotation(cam.getRotation());
            }

            // Check character for collisions
            for (PhysicsCollisionObject object : billMurray.getOverlappingObjects()) {
                if (object instanceof RigidBodyControl) {
                    ((RigidBodyControl) object).applyCentralForce(camDir.mult(1000f));
                }
            }
            // Check global location event queue
            for (LocationEvent e : locEventBus) {
                // TODO cleanup
                e.checkAndFireEvent(this, playerControl.getPhysicsLocation());
//                if (e.checkCondition(playerControl.getPhysicsLocation())) {
//                    e.onDemoEvent(this);
//                }
            }
            // Update task queue
            ArrayDeque<ArrayDeque<DemoTask>> x = new ArrayDeque<>(taskEventBus.values());
            for (ArrayDeque<DemoTask> queue : x) {
                DemoTask task = queue.peek();
                task.updateTime(tpf);
                task.onTimeStep(tpf);
                if (task.isFinished()) {
                    System.out.println("TaskType: " + task.getClass() + " from queue: " + task.getTaskQueueId() + " completed");
                    task.complete();
                    queue.pop();
                    if (queue.isEmpty()) taskEventBus.remove(task.getTaskQueueId());
                }
            }
        }
    }

    @Override
    public void onAction(String keyName, boolean isPressed, float tpf) {
        switch (keyName) {
        case "Left":
            keyLeft = isPressed;
            break;
        case "Right":
            keyRight = isPressed;
            break;
        case "Up":
            keyUp = isPressed;
            break;
        case "Down":
            keyDown = isPressed;
            break;
        case "Interact":
            if (isPressed) {
                if (gameScreen.isTextShowing() && gameScreen.isEnabled()) {
                    gameScreen.progressThroughText();
                } else if (draggedObject != null) {
                    // Drop current Object held
                    Vector3f location = draggedObject.getWorldTranslation();
                    bulletAppState.getPhysicsSpace().add(draggedObject);
                    draggedObject.removeFromParent();
                    rootNode.attachChild(draggedObject);
                    draggedObject.setLocalTranslation(location);
                    ((RigidBodyControl) draggedObject.getControl(0)).setPhysicsLocation(location);
                    draggedObject = null;
                } else {
                    // Ray Casting (checking for first interactable object)
                    Ray ray = new Ray(cam.getLocation(), cam.getDirection());
                    CollisionResults results = new CollisionResults();
                    rootNode.collideWith(ray, results);
                    CollisionResult closest = results.getClosestCollision();

                    // Gets the closest geometry (if it exists) and attempts to interact with it
                    if (closest != null) {
                        System.out.println(closest.getGeometry().getName() + " found!");
                        if (!currentRoute.interactWith(this, closest.getGeometry())) {
                            System.out.println(closest.getGeometry().getName() + " is not responding...");
                        }
                    }
                }
            }
            break;
        case "Jump":
            if (isPressed) {
                playerControl.jump();
            }
            break;
        case "Pause":
            if (isPressed) {
                if (!isPaused) {
                    nifty.gotoScreen("pauseMenu");
                } else {
                    nifty.gotoScreen("game");
                }
            }
            break;
        }
    }

    public void pauseDemo() {
        this.isPaused = true;
        bulletAppState.setEnabled(false);
        flyCam.setEnabled(false);
    }

    public void unPauseDemo() {
        this.isPaused = false;
        bulletAppState.setEnabled(true);
        flyCam.setEnabled(true);
    }

    public void drag(Spatial spatial) {
        // Remove it from the physics space
        bulletAppState.getPhysicsSpace().remove(spatial);
        // Attatch it to the player
        playerNode.attachChild(spatial);
        draggedObject = spatial;
    }

    public void addTask(String taskQueueId, ArrayDeque<DemoTask> task) {
        if (!taskEventBus.containsKey(taskQueueId)) {
            taskEventBus.put(taskQueueId, task);
        }
    }

    public CharacterControl getPlayerControl() {
        return playerControl;
    }

    public MainMenuScreen getMainMenuScreen() {
        return mainMenuScreen;
    }

    public PauseMenuScreen getPauseMenuScreen() {
        return pauseMenuScreen;
    }

    public CharacterSelectScreen getCharacterSelectScreen() {
        return characterSelectScreen;
    }

    public GameScreen getGameScreen() {
        return gameScreen;
    }

    @Override
    public AppStateManager getStateManager() {
        return stateManager;
    }

    public void chooseRoute(String routeName) {
        DemoRoute route = routes.get(routeName);
        if (route == null) {
            throw new RuntimeException("Error: No route found with name: " + routeName);
        }
        loadRoute(route);
    }
}
