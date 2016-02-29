package uk.ac.cam.echo2016.dynademo;

import com.jme3.bounding.BoundingBox;
import static uk.ac.cam.echo2016.dynademo.MainApplication.HALFCHARHEIGHT;

import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.shadow.PointLightShadowRenderer;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.cam.echo2016.multinarrative.GameChoice;
import uk.ac.cam.echo2016.multinarrative.GraphElementNotFoundException;
import uk.ac.cam.echo2016.multinarrative.Route;

/**
 * @author tr393
 */
public class Initialiser {

    private static DemoScene tRoute;
    private static LocationEvent tLocEvent;
    private static InteractionEvent tInterEvent;
    private static String[] tLightNames;
    private static Vector3f[] tLightCoords;
    private static String[][] tLightAffected;
    private static ArrayList<Vector3f> locList= new ArrayList<Vector3f>();
    private static ArrayList<Vector3f> dirList= new ArrayList<Vector3f>();
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
        tLocEvent = new SyncPointEvent("Puzzle Or Observation", new BoundingBox(new Vector3f(-70, 1, -15), 5, 14, 5));
        tRoute.locEvents.add(tLocEvent);

        tRoute.startupTextSequence = new String[]{
            "Press 'e' on to scroll through this text...",
            "Pleas enjoy DyNaDeMo!"
        };

        routes.put(tRoute.getId(), tRoute);
    }

    private static void addButtonRoute(final MainApplication app, final HashMap<String, DemoScene> routes) {
        InteractionEvent eInter;
        final SyncAfterChoiceEvent cpe;
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

//        Spatial lever = app.getAssetManager().loadModel("Models/Lever.j3o");
//        lever.setLocalTranslation(0f, 5f, 10f);
//        lever.setLocalRotation(new Quaternion().fromAngles(-FastMath.PI/2, 0f,0f));
//        // WARNING: Rigid body applied after this transform - axis offset
//        
//        // hacky but it works :)
//        Spatial leverRod = ((Node) lever).descendantMatches("Lever").get(0);
//        
//        // object physics
//        StaticDemoObject leverBaseObj = new StaticDemoObject("LeverBase", lever, true);
//        leverBaseObj.getLights().add(light);
//        route.objects.add(leverBaseObj);
//        
//        KinematicDemoObject leverObj = new KinematicDemoObject("leverRod", leverRod, 1f, false);
//        leverObj.getLights().add(light);
//        route.objects.add(leverObj);
//        
//        // object events
//        route.properties.putInt(leverObj.getObjId(), 0);
//        eInter = new LeverEvent("lever", leverObj);
//        route.setInteractable(lever, eInter);


        Spatial button = app.getAssetManager().loadModel("Models/Button.j3o");
        button.setLocalTranslation(0f, 4f, -7f);
        button.setLocalRotation(new Quaternion().fromAngles(FastMath.PI / 4, 0f, 0f));
        button.move(new Vector3f(0f, 1f, 1f).normalize().mult(0.2f / (float) Math.sqrt(2f)));

        // EVENTS
        cpe =
                new SyncAfterChoiceEvent("Third Select", new BoundingBox(new Vector3f(40, 1, 0), 10, 14, 5), "Button pressed", "Button not pressed");
        tRoute.locEvents.add(cpe);
        
        // object physics
        ButtonObject buttonObj = new ButtonObject("button", button, 1f, true, null, cpe);
        buttonObj.getLights().add(lightMap.get("RoomLight"));

        eInter = new InteractionEvent("buttonInteraction", buttonObj);
        tRoute.setInteractable(button, eInter);

        tRoute.objects.add(buttonObj);
        
        tRoute.startupTextSequence = new String[]{
            "It's so tempting when it's right there...",
            "But who knows if you should press it?"
        };
        
        routes.put(tRoute.getId(), tRoute);
    }

    private static void addChar1DeathRoute( final MainApplication app, final HashMap<String, DemoScene> routes) {
        SyncPointEvent spe;
        InteractionEvent eInter;
        
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
        spe = new SyncPointEvent("To Endings", new BoundingBox(new Vector3f(40,1,40),0,14,0));
        
        Spatial pills = app.getAssetManager().loadModel("Models/Pills.j3o");
        PillsObject pillsObj = new PillsObject("head", pills, true, spe);
        pillsObj.getLights().add(lightMap.get("MeetingRoomLight"));
        eInter = new InteractionEvent("pillsInteraction", pillsObj);
        tRoute.setInteractable(pills, eInter);

        tRoute.objects.add(pillsObj);        
        
        
        tRoute.startupTextSequence = new String[]{
            "This room.",
            "Where you had your first 'chat'...",
            "Will be the last you see."
        };
        
        routes.put(tRoute.getId(), tRoute);
    }
    
    private static void addChar2DeathRoute(final MainApplication app, final HashMap<String, DemoScene> routes) {
        SyncPointEvent spe;
        InteractionEvent eInter;
        
        locList.clear();
        locList.add(new Vector3f(-30, HALFCHARHEIGHT + 1.0f, 0));
        dirList.clear();
        dirList.add(new Vector3f(1, 0, 0));
        
        tRoute = new DemoScene("Char2DeathRoute", "Scenes/Char2DeathRoute.j3o", locList, dirList);

        // LIGHTS
        tLightNames = new String[]{"RoomLight", "CorridorLight"};

        tLightCoords = new Vector3f[]{
            new Vector3f(0, 8, 0), new Vector3f(-25, 8, 0)
        };

        tLightAffected = new String[][]{
            {"Room"}, {"Corridor"}
        };

        lightMap = addLights(app, tRoute, tLightNames, tLightCoords, tLightAffected);

        Spatial head = app.getAssetManager().loadModel("Models/Head.j3o");
        HeadObject headObj = new HeadObject("head", head, true);
        headObj.getLights().add(lightMap.get("RoomLight"));
       
        
        // EVENTS
        eInter = new InteractionEvent("headInteraction", headObj);
        tRoute.setInteractable(head, eInter);

        tRoute.objects.add(headObj);
        
        spe = new SyncPointEvent("To Endings", new BoundingBox(new Vector3f(10,1,0), 5,14,5));
        tRoute.locEvents.add(spe);
        
        routes.put(tRoute.getId(), tRoute);
    }

    private static void addDoorLeftRoute(final MainApplication app, final HashMap<String, DemoScene> routes) {
        SyncPointEvent spe;
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
        spe = new SyncPointEvent("Fate Decider", new BoundingBox(new Vector3f(0, 1, -45), 5, 14, 5));
        tRoute.locEvents.add(spe);
        
        tRoute.startupTextSequence = new String[]{
            "You have found me.",
            "But I found you long ago..."
        };

        routes.put(tRoute.getId(), tRoute);
    }

    private static void addDoorRightRoute(final MainApplication app, final HashMap<String, DemoScene> routes) {
        SyncPointEvent spe;
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
        spe = new SyncPointEvent("Fate Decider", new BoundingBox(new Vector3f(0, 1, 45), 5, 14, 5));
        tRoute.locEvents.add(spe);

        routes.put(tRoute.getId(), tRoute);
    }

    private static void addEscapeRoute(final MainApplication app, final HashMap<String, DemoScene> routes) {
        SyncPointEvent spe;
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
        spe = new SyncPointEvent("To Endings", new BoundingBox(new Vector3f(0, 1, 15), 5, 14, 5));
        tRoute.locEvents.add(spe);

        routes.put(tRoute.getId(), tRoute);
    }

    private static void addLeverRoute(final MainApplication app, final HashMap<String, DemoScene> routes) {
        InteractionEvent eInter;
        final SyncAfterChoiceEvent cpe;
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

        cpe =
                new SyncAfterChoiceEvent("LeverMovedLeft", new BoundingBox(new Vector3f(-45, 1, 0), 5, 14, 5), "Choose left", "Choose right");

        // TODO bounding box actually required? see button
        LeverObject leverObj = new LeverObject("leverRod", leverRod, 1f, false, null, cpe);
        leverObj.getLights().add(lightMap.get("RoomLight"));

        // EVENTS
        tRoute.locEvents.add(cpe);

        // object events
        tRoute.properties.putInt(leverObj.getObjId(), 0);
        eInter = new InteractionEvent("leverInteraction", leverObj);
        tRoute.setInteractable(leverRoot, eInter);
        
        tRoute.startupTextSequence = new String[]{
            "Left or Right?",
            "Let's hope you make the RIGHT choice..."
        };

        routes.put(tRoute.getId(), tRoute);
    }
    
    private static void addObservationRoute(final MainApplication app, final HashMap<String, DemoScene> routes) {
        LocationEvent eLoc;
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

        // EVENTS
        eLoc = new SyncPointEvent("LeverOrButton", new BoundingBox(new Vector3f(-40, 1, -5), 5, 14, 5));
        tRoute.locEvents.add(eLoc);
        
        tRoute.startupTextSequence = new String[]{
            "Seem familiar?",
            "Perhaps not to you, Tojamobin...",
            "But to YOU."
        };

        routes.put(tRoute.getId(), tRoute);
    }

    private static void addPuzzleRoute(final MainApplication app, final HashMap<String, DemoScene> routes) {
        SyncPointEvent spe;
        ConditionalSyncPointEvent cspe1;
        ConditionalSyncPointEvent cspe2;
        ConditionalSyncPointEvent cspe3;
        SyncAfterChoiceEvent ctspe;
        
        BoundingBox bound;
        locList.clear();
        locList.add(new Vector3f(0, HALFCHARHEIGHT + 1.0f, -55));
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

        pressPlate1.setLocalTranslation(-5f, 0, -15f);
        pressPlate2.setLocalTranslation(-5f, 0, 5f);

        final PressurePlateObject plateObj1 = new PressurePlateObject("pressurePlate1", pressPlate1, 1f, true, bound, new Vector3f(-5f, 0, -15f), new Vector3f(-5f, -0.8f, -15f)) {
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

        bound = new BoundingBox(new Vector3f(-5f, 0.4f, -15f), 1.3f, 0.4f, 1.3f);
        
        tLocEvent = new ProximityEvent("pressurePlate1", bound, plateObj1) {
            @Override
            public void onDemoEvent(MainApplication app) {
                plateObj1.activate(app);
            }
        };

        ((ProximityEvent) tLocEvent).activators.add(crateObj1);
        ((ProximityEvent) tLocEvent).activators.add(crateObj2);
        tRoute.locEvents.add(tLocEvent);
        bound = new BoundingBox(new Vector3f(-5f, 0.4f, 5f), 1.3f, 0.4f, 1.3f);
        
        tLocEvent = new ProximityEvent("pressurePlate2", bound, plateObj2) {
            @Override
            public void onDemoEvent(MainApplication app) {
                plateObj2.activate(app);
            }
        };

        ((ProximityEvent) tLocEvent).activators.add(crateObj1);
        ((ProximityEvent) tLocEvent).activators.add(crateObj2);
        tRoute.locEvents.add(tLocEvent);

        tRoute.startupTextSequence = new String[]{
            "Press 'e' to interact with objects."
        };

        // EVENTS
        ctspe = new SyncAfterChoiceEvent("FateDecider", new BoundingBox(new Vector3f(100,100,100),0,0,0), "Puzzle solved", "Puzzle not solved");
        
        spe = new ExitChoiceEvent("PuzzleSolvedExit", new BoundingBox(new Vector3f(45, 1, 5), 5, 14, 5), "Puzzle again", ctspe, true);
        cspe1 =
                new ConditionalSyncPointEvent("FirstExitEvent", new BoundingBox(new Vector3f(0, 1, 45), 5, 14, 5), "See puzzle first time");
        cspe2 =
                new ExitChoiceEvent("PuzzleUnsolvedEvent", new BoundingBox(new Vector3f(0, 1, -45), 5, 14, 5), "Puzzle again", ctspe, false);
        cspe3 =
                new ConditionalSyncPointEvent("PuzzleUnsolvableEvent", new BoundingBox(new Vector3f(0, 1, -45), 5, 14, 5), "Puzzle unsolvable");
        
        tRoute.locEvents.add(spe);
        tRoute.locEvents.add(cspe1);
        tRoute.locEvents.add(cspe2);
        tRoute.locEvents.add(cspe3);

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
        l.setColor(ColorRGBA.Gray);
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
}
