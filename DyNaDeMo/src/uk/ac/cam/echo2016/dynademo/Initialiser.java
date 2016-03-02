package uk.ac.cam.echo2016.dynademo;

import static uk.ac.cam.echo2016.dynademo.MainApplication.HALFCHARHEIGHT;

import java.util.ArrayList;
import java.util.HashMap;

import com.jme3.bounding.BoundingBox;
import com.jme3.bounding.BoundingSphere;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.light.PointLight;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.shadow.PointLightShadowRenderer;

/**
 * @author tr393
 */
public class Initialiser {

    private static DemoScene tRoute;
    private static LocationEvent tLocEvent;
    private static InteractionEvent tInterEvent;
    private static SyncPointEvent tSyncPointEvent;
    private static String[] tLightNames;
    private static Vector3f[] tLightCoords;
    private static String[][] tLightAffected;
    private static ArrayList<Vector3f> locList = new ArrayList<>();
    private static ArrayList<Vector3f> dirList = new ArrayList<>();
    private static HashMap<String, DemoLight> lightMap;

    /**
     * @param app - the application to attach events and renderers to
     * @return
     */
    public static HashMap<String, DemoScene> initialiseRoutes(MainApplication app) {
        final HashMap<String, DemoScene> routes = new HashMap<>();
        addBedroomRoute(app, routes);
        addButtonRoute(app, routes);
        addChar1DeathRoute(app, routes);
        addChar2DeathRoute(app, routes);
        addDoorLeftRoute(app, routes);
        addDoorRightRoute(app, routes);
        addEscapeRoute(app, routes);
        addLeverRoute(app, routes);
        addObservationRoute(app, routes);
        addPuzzleRoute(app, routes);
        addEndingRoute(app, routes);
        return routes;
    }
    
    private static void addBedroomRoute(final MainApplication app, final HashMap<String, DemoScene> routes) {
        locList.clear();
        locList.add(new Vector3f(0, HALFCHARHEIGHT + 1.0f, 0));
        locList.add(new Vector3f(-65, HALFCHARHEIGHT + 1.0f,-15));
        
        dirList.clear();
        dirList.add(new Vector3f(1,0,0));
        dirList.add(new Vector3f(-1,0,0));
        tRoute = new DemoScene("BedroomRoute", "Scenes/BedroomRoute.j3o", locList, dirList);

        // LIGHTS
        tLightNames = new String[]{
            "RoomLight1", "RoomLight2", "RoomLight3", "RoomLight4",
            "RoomLight5", "RoomLight6", "CorridorLight1", "CorridorLight2",
            "MeetingRoomLight"};

        tLightCoords = new Vector3f[]{
            new Vector3f(0f, 6f, 0f), new Vector3f(25f, 6f, 0f), new Vector3f(25f, 6f, -30f),
            new Vector3f(0f, 6f, -30f), new Vector3f(-25f, 6f, -30f), new Vector3f(-25f, 6f, 0f),
            new Vector3f(25f, 6f, -15f), new Vector3f(-25f, 6f, -15f), new Vector3f(-70,8,-15)};

        tLightAffected = new String[][]{
            {"Room1"}, {"Room2"}, {"Room3"}, {"Room4"},
            {"Room5"}, {"Room6"}, {"Corridor"}, {"Corridor", "MeetingRoom"},{"MeetingRoom","Corridor"}};

        lightMap = addLights(app, tRoute, tLightNames, tLightCoords, tLightAffected);

        // EVENTS
        tLocEvent = new SyncPointEvent("Puzzle Or Observation", false, new BoundingBox(new Vector3f(-70, 1, -15), 5, 14, 5));
        tRoute.condEvents.add(tLocEvent);

        tRoute.startupTextSequence = new String[]{
            "Press 'e' on to scroll through this text...",
            "Pleas enjoy DyNaDeMo!"
        };

        routes.put(tRoute.getId(), tRoute);
    }

    private static void addButtonRoute(final MainApplication app, final HashMap<String, DemoScene> routes) {
        final SyncAfterChoiceEvent choiceHandler;
        locList.clear();
        locList.add(new Vector3f(-40, HALFCHARHEIGHT + 1.0f, 0));
        dirList.clear();
        dirList.add(new Vector3f(1, 0, 0));
                

        tRoute = new DemoScene("ButtonRoute", "Scenes/ButtonRoute.j3o", locList, dirList);

        // LIGHTS
        tLightNames = new String[]{"RoomLight", "CorridorLight1", "CorridorLight2"};

        tLightCoords = new Vector3f[]{
            new Vector3f(0, 8, 0), new Vector3f(-35, 8, 0), new Vector3f(35, 8, 0)
        };

        tLightAffected = new String[][]{
            {"Room"}, {"Corridor1"}, {"Corridor2"}
        };

        lightMap = addLights(app, tRoute, tLightNames, tLightCoords, tLightAffected);

        // OBJECTS

        Spatial button = app.getAssetManager().loadModel("Models/Button.j3o");
        button.setLocalTranslation(0f, 4f, -7f);
        button.setLocalRotation(new Quaternion().fromAngles(FastMath.PI / 4, 0f, 0f));
        button.move(new Vector3f(0f, 1f, 1f).normalize().mult(0.2f / (float) Math.sqrt(2f)));

        // EVENTS
        choiceHandler =
                new SyncAfterChoiceEvent("Third Select", false, new BoundingBox(new Vector3f(40, 1, 0), 10, 14, 5), "Button pressed", "Button not pressed");
        tRoute.condEvents.add(choiceHandler);
        
        // object physics
        ButtonObject buttonObj = new ButtonObject("button", button, 1f, true, null, choiceHandler);
        buttonObj.getLights().add(lightMap.get("RoomLight"));

        tInterEvent = new InteractionEvent("buttonInteraction", buttonObj);
        tRoute.setInteractable(button, tInterEvent);

        tRoute.objects.add(buttonObj);
        
        tRoute.startupTextSequence = new String[]{
            "It's so tempting when it's right there...",
            "But who knows if you should press it?"
        };
        
        routes.put(tRoute.getId(), tRoute);
    }

    private static void addChar1DeathRoute( final MainApplication app, final HashMap<String, DemoScene> routes) {
        locList.clear();
        locList.add(new Vector3f(0, HALFCHARHEIGHT + 1.0f, 5));
        dirList.clear();
        dirList.add(new Vector3f(0, 0, -1));
        
        tRoute = new DemoScene("Char1DeathRoute", "Scenes/Char1DeathRoute.j3o", locList, dirList);

        // LIGHTS
        tLightNames = new String[]{"MeetingRoomLight"};

        tLightCoords = new Vector3f[]{
            new Vector3f(0, 8, 0)
        };

        tLightAffected = new String[][]{
            {"MeetingRoom"}
        };

        lightMap = addLights(app, tRoute, tLightNames, tLightCoords, tLightAffected);
        
        // EVENTS
        tSyncPointEvent = new SyncPointEvent("To Endings", false, new BoundingBox(new Vector3f(40,1,40),0,14,0));
        
        Spatial pills = app.getAssetManager().loadModel("Models/Pills.j3o");
        PillsObject pillsObj = new PillsObject("head", pills, true, tSyncPointEvent);
        pillsObj.getLights().add(lightMap.get("MeetingRoomLight"));
        tInterEvent = new InteractionEvent("pillsInteraction", pillsObj);
        tRoute.setInteractable(pills, tInterEvent);

        tRoute.objects.add(pillsObj);        
        
        
        tRoute.startupTextSequence = new String[]{
            "This room.",
            "Where you had your first 'chat'...",
            "Will be the last you see."
        };
        
        routes.put(tRoute.getId(), tRoute);
    }
    
    private static void addChar2DeathRoute(final MainApplication app, final HashMap<String, DemoScene> routes) {
        locList.clear();
        locList.add(new Vector3f(-15, HALFCHARHEIGHT + 1.0f, 0));
        dirList.clear();
        dirList.add(new Vector3f(1, 0, 0));
        
        tRoute = new DemoScene("Char2DeathRoute", "Scenes/Char2DeathRoute.j3o", locList, dirList);

        // LIGHTS
        tLightNames = new String[]{"RoomLight"};

        tLightCoords = new Vector3f[]{
            new Vector3f(0, 8, 0)
        };

        tLightAffected = new String[][]{
            {"Room", "Monitor1", "Monitor2"}
        };

        lightMap = addLights(app, tRoute, tLightNames, tLightCoords, tLightAffected);

//        Spatial heWhoMustNotBeNamed = app.getAssetManager().loadModel("Models/Head.j3o");
//        final HeadObject ohGreatOne = new HeadObject("ohGreatOne", heWhoMustNotBeNamed, true);
//        ohGreatOne.getLights().add(lightMap.get("RoomLight"));
        
        class HeadLookAtEvent extends ConditionEvent {
            private final Vector3f centre = new Vector3f(-10, 0, -10);
            private final float radius = 2f;
            private HeadObject head;
            public HeadLookAtEvent(String id, boolean onceOnly , HeadObject head) {
                super(id, onceOnly);
                this.head = head;
                
            }
            
            @Override
            public boolean checkCondition(MainApplication app) {
                return isLookedAt(app, new BoundingSphere(radius, centre));
            }
            
            @Override
            public void performAction(MainApplication app) {
                
                app.setFlickering(true);
                // initiate move sequence for head
                // start event polling for contact
                app.addTask(new HeadMoveTask(head.getObjId(),100f, head, app));
            }
        };
                
        tLocEvent = new LocationEvent(("HeadSpawn"), true, new BoundingBox(new Vector3f(7.5f,5f,7.5f),15f,5f,15f)) {
            private final Vector3f centre = new Vector3f(-1, 0, -1);
            private final float radius = 2f;
            
            @Override
            public boolean checkCondition(MainApplication app) {
                boolean temp = isLookedAt(app, new BoundingSphere(radius, centre));
                
                BoundingSphere playerBound =
                        new BoundingSphere(MainApplication.HALFCHARHEIGHT, app.getPlayerControl().getPhysicsLocation());
                System.out.println(temp);
                return (bound.intersects(playerBound) && temp);
            }

            @Override
            public void performAction(MainApplication app) {
                Spatial theUnnamed = app.getAssetManager().loadModel("Models/Head.j3o");
                theUnnamed.setLocalTranslation(-10f, HALFCHARHEIGHT, -10f);
                theUnnamed.rotate(-0, FastMath.PI*3/4, -0);
                HeadObject theUnnamedObj = new HeadObject("ohGreatOne", theUnnamed, true);
                theUnnamedObj.getLights().add(lightMap.get("RoomLight"));
                app.getCurrentScene().objects.add(theUnnamedObj);
                app.getRootNode().attachChild(theUnnamed);
//                RigidBodyControl rbc = new RigidBodyControl(0);
//                app.getBulletAppState().getPhysicsSpace().add(rbc);
                app.getPollEventBus().add(new HeadLookAtEvent("HeadSpawnLookAt", true, theUnnamedObj));
            }
        };
        tRoute.condEvents.add(tLocEvent);
        
        // EVENTS
//        tInterEvent = new InteractionEvent("headInteraction", ohGreatOne);
//        tRoute.setInteractable(heWhoMustNotBeNamed, tInterEvent);

//        tRoute.objects.add(ohGreatOne);
        
//        tSyncPointEvent = new SyncPointEvent("To Endings", new BoundingBox(new Vector3f(15,1,15), 5,14,5));
//        tRoute.condEvents.add(tSyncPointEvent);
        
        routes.put(tRoute.getId(), tRoute);
    }

    private static void addDoorLeftRoute(final MainApplication app, final HashMap<String, DemoScene> routes) {
        locList.clear();
        locList.add(new Vector3f(-30, HALFCHARHEIGHT + 1.0f, 0));
        dirList.clear();
        dirList.add(new Vector3f(1, 0, 0));

        tRoute = new DemoScene("DoorLeft", "Scenes/DoorLeftRoute.j3o", locList, dirList);

        //LIGHTS
        tLightNames = new String[]{"RoomLight", "CorridorLight", "CorridorLeftLight"};

        tLightCoords = new Vector3f[]{
            new Vector3f(0, 18, 0), new Vector3f(-35, 8, 0), new Vector3f(0, 8, -35)
        };

        tLightAffected = new String[][]{
            {"Room", "BlankDoorLeft", "BlankDoorRight"}, {"Corridor"}, {"CorridorLeft"}
        };

        lightMap = addLights(app, tRoute, tLightNames, tLightCoords, tLightAffected);

        // EVENTS
        tSyncPointEvent = new SyncPointEvent("Fate Decider", false, new BoundingBox(new Vector3f(0, 1, -45), 5, 14, 5));
        tRoute.condEvents.add(tSyncPointEvent);
        
        tRoute.startupTextSequence = new String[]{
            "You have found me.",
            "But I found you long ago..."
        };

        routes.put(tRoute.getId(), tRoute);
    }

    private static void addDoorRightRoute(final MainApplication app, final HashMap<String, DemoScene> routes) {
        locList.clear();
        locList.add(new Vector3f(-30, HALFCHARHEIGHT + 1.0f, 0));
        dirList.clear();
        dirList.add(new Vector3f(1, 0, 0));

        tRoute = new DemoScene("DoorRight", "Scenes/DoorRightRoute.j3o", locList, dirList);

        //LIGHTS
        tLightNames = new String[]{"RoomLight", "CorridorLight", "CorridorRightLight"};

        tLightCoords = new Vector3f[]{
            new Vector3f(0, 18, 0), new Vector3f(-35, 8, 0), new Vector3f(0, 8, 35)
        };

        tLightAffected = new String[][]{
            {"Room", "BlankDoorLeft", "BlankDoorRight"}, {"Corridor"}, {"CorridorRight"}
        };

        lightMap = addLights(app, tRoute, tLightNames, tLightCoords, tLightAffected);

        // EVENTS
        tSyncPointEvent = new SyncPointEvent("Fate Decider", false, new BoundingBox(new Vector3f(0, 1, 45), 5, 14, 5));
        tRoute.condEvents.add(tSyncPointEvent);

        routes.put(tRoute.getId(), tRoute);
    }

    private static void addEscapeRoute(final MainApplication app, final HashMap<String, DemoScene> routes) {
        locList.clear();
        locList.add(new Vector3f(25, HALFCHARHEIGHT + 1.0f, 0));
        locList.add(new Vector3f(-25, HALFCHARHEIGHT + 1.0f, 0));
        dirList.clear();
        dirList.add(new Vector3f(-1, 0, 0));
        dirList.add(new Vector3f(1,0,0));
                

        tRoute = new DemoScene("EscapeRoute", "Scenes/EscapeRoute.j3o", locList, dirList);

        // LIGHTS
        tLightNames = new String[]{"RoomLight", "CorridorLight", "Corridor1Light", "Corridor2Light"};

        tLightCoords = new Vector3f[]{
            new Vector3f(0, 8, 0), new Vector3f(0, 8, 25), new Vector3f(-25, 8, 0), new Vector3f(25, 8, 0)
        };

        tLightAffected = new String[][]{
            {"Room"}, {"Corridor"}, {"Corridor1"}, {"Corridor2"}
        };

        lightMap = addLights(app, tRoute, tLightNames, tLightCoords, tLightAffected);

        // EVENTS
        tSyncPointEvent = new SyncPointEvent("To Endings", false, new BoundingBox(new Vector3f(0, 1, 15), 5, 14, 5));
        tRoute.condEvents.add(tSyncPointEvent);

        routes.put(tRoute.getId(), tRoute);
    }

    private static void addLeverRoute(final MainApplication app, final HashMap<String, DemoScene> routes) {
        final SyncAfterChoiceEvent choiceHandler;
        locList.clear();
        locList.add(new Vector3f(-35, HALFCHARHEIGHT + 1.0f, 0));
        dirList.clear();
        dirList.add(new Vector3f(1, 0, 0));

        tRoute = new DemoScene("LeverRoute", "Scenes/LeverRoute.j3o", locList, dirList);
        // LIGHTS
        tLightNames = new String[]{"RoomLight", "CorridorLight"};

        tLightCoords = new Vector3f[]{
            new Vector3f(-10, 8, 0), new Vector3f(-35, 8, 0)
        };

        tLightAffected = new String[][]{
            {"Room"}, {"Corridor"}
        };

        lightMap = addLights(app, tRoute, tLightNames, tLightCoords, tLightAffected);

        // OBJECTS
        
        Spatial leverRoot = app.getAssetManager().loadModel("Models/Lever.j3o");
        leverRoot.setLocalTranslation(0f, 5f, 0f);
        leverRoot.setLocalRotation(new Quaternion().fromAngles(0, 0f, FastMath.PI / 2));
        // WARNING: Rigid body applied after this transform - axis offset

        // hacky but it works :)
        Spatial leverRod = ((Node) leverRoot).descendantMatches("Lever").get(0);

        // object physics
        StaticDemoObject leverBaseObj = new StaticDemoObject("leverBase", leverRoot, true);
        leverBaseObj.getLights().add(lightMap.get("RoomLight"));
        tRoute.objects.add(leverBaseObj);

        choiceHandler =
                new SyncAfterChoiceEvent("LeverMovedLeft", false, new BoundingBox(new Vector3f(-45, 1, 0), 5, 14, 5), "Choose left", "Choose right");

        // TODO bounding box actually required? see button
        LeverObject leverObj = new LeverObject("leverRod", leverRod, 1f, false, null, choiceHandler);
        leverObj.getLights().add(lightMap.get("RoomLight"));

        // EVENTS
        tRoute.condEvents.add(choiceHandler);

        // object events
        tRoute.properties.putInt(leverObj.getObjId(), 0);
        tInterEvent = new InteractionEvent("leverInteraction", leverObj);
        tRoute.setInteractable(leverRoot, tInterEvent);
        
        tRoute.startupTextSequence = new String[]{
            "Left or Right?",
            "Let's hope you make the right choice..."
        };

        routes.put(tRoute.getId(), tRoute);
    }
    
    private static void addObservationRoute(final MainApplication app, final HashMap<String, DemoScene> routes) {
        locList.clear();
        locList.add(new Vector3f(-5, HALFCHARHEIGHT + 1.0f, -30));
        dirList.clear();
        dirList.add(new Vector3f(0, 0, 1));

        tRoute = new DemoScene("ObservationRoute", "Scenes/ObservationRoute.j3o", locList, dirList);
        
        // LIGHTS
        tLightNames = new String[]{"RoomLight", "CorridorLight1", "CorridorLight2"};

        tLightCoords = new Vector3f[]{
            new Vector3f(0, 8, 0), new Vector3f(-5, 8, -30), new Vector3f(-30, 8, -5)
        };

        tLightAffected = new String[][]{
            {"Room", "Monitor1", "Screen1", "Monitor2", "Screen2"}, {"Corridor1"}, {"Corridor2"}
        };

        addLights(app, tRoute, tLightNames, tLightCoords, tLightAffected);
        
        // OBJECTS
        Spatial door1 = extractDoor(app, 1);
        door1.setLocalTranslation(-15f, 0, -7.5f);
        DoorObject doorObj1 = new DoorObject("doorObj1", door1, 1f, true, null, FastMath.PI*2/3);
        doorObj1.getLights().add(lightMap.get("RoomLight"));
        tRoute.objects.add(doorObj1);
        
        tInterEvent = new InteractionEvent("doorInteraction", doorObj1);
        tRoute.setInteractable(door1, tInterEvent);
                
        // EVENTS
        
        
        tLocEvent = new SyncPointEvent("LeverOrButton", false, new BoundingBox(new Vector3f(-40, 1, -5), 5, 14, 5));
        tRoute.condEvents.add(tLocEvent);
        
        tRoute.startupTextSequence = new String[]{
            "Seem familiar?",
            "Perhaps not to you, " + app.CHAR_2,
            "But to YOU.",
            "Press 'e' to open doors."
        };

        routes.put(tRoute.getId(), tRoute);
    }

    private static void addPuzzleRoute(final MainApplication app, final HashMap<String, DemoScene> routes) {
        ConditionalSyncPointEvent cspe1;
        ConditionalSyncPointEvent cspe2;
        ConditionalSyncPointEvent cspe3;
        SyncAfterChoiceEvent ctspe;
        
        BoundingBox bound;
        locList.clear();
        locList.add(new Vector3f(0, HALFCHARHEIGHT + 1.0f, -45));
        locList.add(new Vector3f(0, HALFCHARHEIGHT + 1.0f, 45));
        dirList.clear();
        dirList.add(new Vector3f(0, 0, 1));
        dirList.add(new Vector3f(0, 0, -1));

        tRoute = new DemoScene("PuzzleRoute", "Scenes/PuzzleRoute.j3o", locList, dirList);
        // LIGHTS
        tLightNames = new String[]{
            "RoomLight", "CorridorLight1", "CorridorLight2", "TallCorridorLight1", "TallCorridorLight2"
        };

        tLightCoords = new Vector3f[]{
            new Vector3f(0, 8f, 0), new Vector3f(0, 6, -45), new Vector3f(0, 6, 35),
            new Vector3f(20, 10, 5), new Vector3f(40, 10, 5)
        };

        tLightAffected = new String[][]{
            {"Room"}, {"Corridor1"}, {"Corridor2"}, {"TallCorridor1"}, {"TallCorridor2"}
        };

        lightMap = addLights(app, tRoute, tLightNames, tLightCoords, tLightAffected);

        // OBJECTS
        // Crate
        Spatial crate1 = app.getAssetManager().loadModel("Models/Crate.j3o");
        Spatial crate2 = app.getAssetManager().loadModel("Models/Crate.j3o");
        bound = new BoundingBox(new Vector3f(0, 0.75f, 0), 1.5f, 1.5f, 1.5f);
        crate1.setLocalTranslation(0, 0, -30);
        crate2.setLocalTranslation(0, 5f, 0);

        // object physics
        CrateObject crateObj1 = new CrateObject("crate1", crate1, 5f, true, bound);
        CrateObject crateObj2 = new CrateObject("crate2", crate2, 5f, true, bound);
        crateObj1.getLights().add(lightMap.get("RoomLight"));
        crateObj2.getLights().add(lightMap.get("RoomLight"));
        tRoute.objects.add(crateObj1);
        tRoute.objects.add(crateObj2);

        // object events
        tInterEvent = new InteractionEvent("crateInteraction1", crateObj1);
        tRoute.setInteractable(crate1, tInterEvent);
        tInterEvent = new InteractionEvent("crateInteraction2", crateObj2);
        tRoute.setInteractable(crate2, tInterEvent);

        // Door
        Spatial door1 = extractDoor(app, 0);

        door1.setLocalTranslation(9.99f, 0f, 2.5f);
        final PanelObject doorObj1 = new PanelObject("door1", door1, 1f, true, null, new Vector3f(10f, 9f, 2.5f), new Vector3f(10f, 0f, 2.5f));
        doorObj1.getLights().add(lightMap.get("RoomLight"));
        tRoute.objects.add(doorObj1);
        
        Spatial door2 = extractDoor(app, 0);
        door2.setLocalTranslation(29.9f, 0f, 2.5f);
        final PanelObject doorObj2 = new PanelObject("door2", door2, 1f, true, null, new Vector3f(30f, 9f, 2.5f), new Vector3f(30f, 0f, 2.5f));
        doorObj2.getLights().add(lightMap.get("TallCorridorLight1"));
        tRoute.objects.add(doorObj2);

        // PressurePlate
        Spatial pressPlate1 = app.getAssetManager().loadModel("Models/PressurePlate.j3o");
        Spatial pressPlate2 = app.getAssetManager().loadModel("Models/PressurePlate.j3o");
        bound = new BoundingBox(new Vector3f(0, 0.4f, 0), 1.5f, 0.4f, 1.5f);

        pressPlate1.setLocalTranslation(-5f, 0, -5f);
        pressPlate2.setLocalTranslation(-5f, 0, 5f);

        final PressurePlateObject plateObj1 = new PressurePlateObject("pressurePlate1", pressPlate1, 1f, true, bound, new Vector3f(-5f, 0, -5f), new Vector3f(-5f, -0.8f, -5f)) {
            @Override
            public void onPressed() {
                doorObj1.open(app);
            }
            @Override
            public void onRelease() {
                doorObj1.close(app);
            }
        };
        bound = new BoundingBox(new Vector3f(0, 0.4f, 0), 1.5f, 0.4f, 1.5f) {
            
        };
        final PressurePlateObject plateObj2 = new PressurePlateObject("pressurePlate2", pressPlate2, 1f, true, bound, new Vector3f(-5f, 0, 5f), new Vector3f(-5f, -0.8f, 5f)) {

            @Override
            public void onPressed() {
                doorObj2.open(app);
            }

            @Override
            public void onRelease() {
                doorObj2.close(app);
            }
        };
        plateObj1.getLights().add(lightMap.get("RoomLight"));
        plateObj2.getLights().add(lightMap.get("RoomLight"));
        tRoute.objects.add(plateObj1);
        tRoute.objects.add(plateObj2);

        tRoute.properties.putBoolean(plateObj1.getObjId(), false);
        tRoute.properties.putBoolean(plateObj2.getObjId(), false);

        bound = new BoundingBox(new Vector3f(-5f, 0.4f, -5f), 1.3f, 0.4f, 1.3f);
        
        tLocEvent = new ProximityEvent("pressurePlate1", false, bound, plateObj1) {
            @Override
            public void performAction(MainApplication app) {
                plateObj1.activate(app);
            }
        };

        ((ProximityEvent) tLocEvent).activators.add(crateObj1);
        ((ProximityEvent) tLocEvent).activators.add(crateObj2);
        tRoute.condEvents.add(tLocEvent);
        bound = new BoundingBox(new Vector3f(-5f, 0.4f, 5f), 1.3f, 0.4f, 1.3f);
        
        tLocEvent = new ProximityEvent("pressurePlate2", false, bound, plateObj2) {
            @Override
            public void performAction(MainApplication app) {
                plateObj2.activate(app);
            }
        };

        ((ProximityEvent) tLocEvent).activators.add(crateObj1);
        ((ProximityEvent) tLocEvent).activators.add(crateObj2);
        tRoute.condEvents.add(tLocEvent);

        tRoute.startupTextSequence = new String[]{
            "Press 'e' to interact with objects."
        };

        // EVENTS
        ctspe = new SyncAfterChoiceEvent("FateDecider", false, new BoundingBox(new Vector3f(100,100,100),0,0,0), "Puzzle solved", "Puzzle not solved");
        
        tSyncPointEvent = new ExitChoiceEvent("PuzzleSolvedExit", false, new BoundingBox(new Vector3f(45, 1, 5), 5, 14, 5), "Puzzle again", ctspe, true);
        cspe1 =
                new ConditionalSyncPointEvent("FirstExitEvent", false, new BoundingBox(new Vector3f(0, 1, 45), 5, 14, 5), "See puzzle first time");
        cspe2 =
                new ExitChoiceEvent("PuzzleUnsolvedEvent", false, new BoundingBox(new Vector3f(0, 1, -45), 5, 14, 5), "Puzzle again", ctspe, false);
        cspe3 =
                new ConditionalSyncPointEvent("PuzzleUnsolvableEvent", false, new BoundingBox(new Vector3f(0, 1, -45), 5, 14, 5), "Puzzle unsolvable");
        
        tRoute.condEvents.add(tSyncPointEvent);
        tRoute.condEvents.add(cspe1);
        tRoute.condEvents.add(cspe2);
        tRoute.condEvents.add(cspe3);

        routes.put(tRoute.getId(), tRoute);
    }

    private static void addEndingRoute (final MainApplication app, final HashMap<String, DemoScene> routes) {
        locList.clear();
        locList.add(new Vector3f(0,HALFCHARHEIGHT + 1.0f ,0));
        dirList.clear();
        dirList.add(new Vector3f(1,0,0));
        
        tRoute = new DemoScene("Ending", "Scenes/Ending.j3o", locList, dirList);
        
        routes.put(tRoute.getId(), tRoute);
        
    }
    
    private static DemoLight addLight(final MainApplication app, DemoScene route, Vector3f loc, String[] spatialNames) {
        PointLight l = new PointLight();
        l.setColor(MainApplication.LIGHTCOLOUR);
        l.setPosition(loc);
        l.setRadius(1000f);

        DemoLight dLight = new DemoLight(l, spatialNames);
        route.lights.add(dLight);

        PointLightShadowRenderer plsr = new PointLightShadowRenderer(app.getAssetManager(), 1024);
        plsr.setLight(l);
        plsr.setFlushQueues(false);
        plsr.setShadowIntensity(0.1f);
        route.shadowRenderers.add(plsr);

        return dLight;
    }

    private static HashMap<String, DemoLight> addLights(final MainApplication app, DemoScene route, String[] lightNames, Vector3f[] lightCoords, String[][] spatialNames) {
        HashMap<String, DemoLight> result = new HashMap<>();

        if (lightNames.length != lightCoords.length || lightNames.length != spatialNames.length) {
            throw new RuntimeException("Error: incorrect number of light properties");
        }

        for (int i = 0; i < spatialNames.length; ++i) {
            result.put(lightNames[i], addLight(app, route, lightCoords[i], spatialNames[i]));
        }
        return result;
    }

    /**
     *
     * @param type - 0
     * @return
     */
    private static Spatial extractDoor(final MainApplication app, int numberOfHandles) {
        Spatial doors = app.getAssetManager().loadModel("Models/Doors.j3o");
        switch (numberOfHandles) {
        case 0:
            return ((Node) doors).descendantMatches("BlankDoor").get(0);
        case 1:
            return ((Node) doors).descendantMatches("BlankDoor").get(0);
        case 2:
            return ((Node) doors).descendantMatches("BlankDoor").get(0);
        default:
            throw new RuntimeException("Too many door handles");
        }
    }
    private static boolean isLookedAt(MainApplication app, BoundingSphere bSphere) {
        boolean result = false;
        int checkPlane = bSphere.getCheckPlane();
        bSphere.setCheckPlane(0);
        switch (app.getCamera().contains(bSphere)) {
        case Inside:
            result = true;
            break;
        default:
        }
        bSphere.setCheckPlane(checkPlane);

        return result;
    }
}
