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

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.cam.echo2016.dynademo.screens.GameScreen;
import uk.ac.cam.echo2016.multinarrative.GraphElementNotFoundException;

/**
 * @author tr393
 */
public class Initialiser {
    private static DemoRoute tRoute;
    private static LocationEvent tLocEvent;
    private static InteractionEvent tInterEvent;
    private static String[] tLightNames;
    private static Vector3f[] tLightCoords;
    private static String[][] tLightAffected;
    private static HashMap<String, DemoLight> lightMap;
    /**
     * @param app - the application to attach events and renderers to
     * @return
     */
    
    public static HashMap<String, DemoRoute> initialiseRoutes(MainApplication app) {
        final HashMap<String, DemoRoute> routes = new HashMap<String, DemoRoute>();
        addBedroomRoute(app, routes);
        addObservationRoute(app, routes);
        addPuzzleRoute(app, routes);
        addLeverRoute(app, routes);
        addButtonRoute(app, routes);
        return routes;
    }

    private static void addBedroomRoute(MainApplication app, HashMap<String, DemoRoute> routes) {        
        tRoute = new DemoRoute("BedroomRoute", "Scenes/BedroomRoute.j3o", new Vector3f(0, HALFCHARHEIGHT + 1.0f, 0),
                new Vector3f(1, 0, 0));

        // LIGHTS
        tLightNames = new String[]{
            "RoomLight1", "RoomLight2", "RoomLight3", "RoomLight4",
            "RoomLight5", "RoomLight6", "CorridorLight1", "CorridorLight2"};
        
        tLightCoords = new Vector3f[]{
            new Vector3f(0f, 6f, 0f), new Vector3f(25f, 6f, 0f), new Vector3f(25f, 6f, -30f),
            new Vector3f(0f, 6f, -30f), new Vector3f(-25f, 6f, -30f), new Vector3f(-25f, 6f, 0f),
            new Vector3f(25f, 6f, -15f), new Vector3f(-25f, 6f, -15f)};
        
        tLightAffected = new String[][]{
            {"Room1"}, {"Room2"}, {"Room3"}, {"Room4"},
            {"Room5"}, {"Room6"}, {"Corridor"}, {"Corridor"},};
        
        lightMap = addLights(app, tRoute, tLightNames, tLightCoords, tLightAffected);

        // EVENTS
        tLocEvent = new ExitRouteEvent("Node1", new BoundingBox(new Vector3f(-80, 1, -40), 40, 14, 50));
        tRoute.locEvents.add(tLocEvent);
        
        routes.put(tRoute.getId(), tRoute);
    }
    
    private static void addObservationRoute(MainApplication app, HashMap<String, DemoRoute> routes) {
        DemoRoute route;
        LocationEvent eLoc;

        route = new DemoRoute("ObservationRoute", "Scenes/ObservationRoute.j3o", new Vector3f(0, HALFCHARHEIGHT + 1.0f, 0),
                new Vector3f(1, 0, 0));

        // LIGHTS
        tLightNames = new String[] {"RoomLight", "CorridorLight1", "CorridorLight2"};
        
        tLightCoords = new Vector3f[] {
            new Vector3f(0,8,0), new Vector3f(-5, 8, -30), new Vector3f(-30,8,-5)
        };
        
        tLightAffected = new String[][]{
            {"Room"}, {"Corridor1"}, {"Corridor2"}
        };
        
        addLights(app, route, tLightNames, tLightCoords, tLightAffected);
        
        // EVENTS
        eLoc = new ExitRouteEvent("LeverOrButton", new BoundingBox(new Vector3f(-40,1,-5),5,14,5));
        route.locEvents.add(eLoc);
        
        routes.put(route.getId(), route);
    }
    
    private static void addPuzzleRoute(MainApplication app, final HashMap<String, DemoRoute> routes) {
        DemoRoute route;
        LocationEvent eLoc;
        InteractionEvent eInter;
        BoundingBox bound;
        
        tRoute = new DemoRoute("PuzzleRoute", "Scenes/PuzzleRoute.j3o", new Vector3f(0, HALFCHARHEIGHT + 1.0f, -45),
                new Vector3f(0, 0, 1));
        // LIGHTS
        tLightNames = new String[] {
            "RoomLight", "CorridorLight1", "CorridorLight2", "TallCorridorLight1", "TallCorridorLight2"
        };
        
        tLightCoords = new Vector3f[] {
            new Vector3f(0, 8f, 0), new Vector3f(0,6,-35), new Vector3f(0,6,35),
            new Vector3f(20,10,5), new Vector3f(40,10,5)
        };
        
        tLightAffected = new String[][] {
            {"Room"}, {"Corridor1"}, {"Corridor2"}, {"TallCorridor1"}, {"TallCorridor2"}
        };

        lightMap = addLights(app, tRoute, tLightNames, tLightCoords, tLightAffected);
        
        // Crate
        Spatial crate = app.getAssetManager().loadModel("Models/Crate.j3o");
        bound = new BoundingBox(new Vector3f(0,0.75f,0), 1.5f, 1.5f, 1.5f);
        crate.setLocalTranslation(0, 0, -30);
        
        // object physics
        DynamicDemoObject crateObj = new DynamicDemoObject("crate", crate, 5f, true, bound, new Vector3f(0,0.75f,0));
        crateObj.getLights().add(lightMap.get("RoomLight"));
        tRoute.objects.add(crateObj);
        
        // object events
        tInterEvent = new InteractionEvent("crateInteraction", crateObj){
            @Override
            public void onDemoEvent(MainApplication app) {
                app.drag(getObject().getSpatial());
            }
        };
        tRoute.setInteractable(crate, tInterEvent);
        
        // PressurePlate
        Spatial pressPlate1 = app.getAssetManager().loadModel("Models/PressurePlate.j3o");
        Spatial pressPlate2 = app.getAssetManager().loadModel("Models/PressurePlate.j3o");
        bound = new BoundingBox(Vector3f.ZERO, 1.5f, 0.4f, 1.5f);
        pressPlate1.setLocalTranslation(-5f, 0, 5f);
        pressPlate2.setLocalTranslation(-5f, 0, -5f);
        KinematicDemoObject plateObj1 = new KinematicDemoObject("pressurePlate1", pressPlate1, 1f, true, bound, new Vector3f(0,0.4f,0));
        KinematicDemoObject plateObj2 = new KinematicDemoObject("pressurePlate2", pressPlate2, 1f, true, bound, new Vector3f(0,0.4f,0));
        plateObj1.getLights().add(lightMap.get("RoomLight"));
        plateObj2.getLights().add(lightMap.get("RoomLight"));
        tRoute.objects.add(plateObj1);
        tRoute.objects.add(plateObj2);
        
        tRoute.properties.putBoolean(plateObj1.getObjId(), false);
        tRoute.properties.putBoolean(plateObj2.getObjId(), false);
        
        bound = new BoundingBox(new Vector3f(-5f, 0.4f, 5f), 1.3f, 0.4f, 1.3f);
//        eLoc = new PressurePlateEvent("pressurePlate1", new Vector3f(-6.5f, 0f, 3.5f),  3.2f, 0.8f + HALFCHARHEIGHT, 3.2f, plateObj1);
        tLocEvent = new PressurePlateEvent("pressurePlate1", bound, plateObj1);
        // TODO bad code
        ((PressurePlateEvent)tLocEvent).activators.add(crateObj);
        tRoute.locEvents.add(tLocEvent);
        bound = new BoundingBox(new Vector3f(-5f, 0.4f, -5f), 1.3f, 0.4f, 1.3f);
//        eLoc = new PressurePlateEvent("pressurePlate2", new Vector3f(-6.5f, 0f, -6.5f), 3.2f, 0.8f + HALFCHARHEIGHT, 3.2f, plateObj2);
        tLocEvent = new PressurePlateEvent("pressurePlate2", bound, plateObj2);
        ((PressurePlateEvent)tLocEvent).activators.add(crateObj);
        tRoute.locEvents.add(tLocEvent);
        
        routes.put(tRoute.getId(), tRoute);
    }
    
    private static void addLeverRoute(MainApplication app, final HashMap<String, DemoRoute> routes) {
        DemoRoute route;
        DemoLight light;
        InteractionEvent eInter;
        
        route = new DemoRoute("LeverRoute", "Scenes/LeverRoute.j3o", new Vector3f(-40, HALFCHARHEIGHT + 1.0f, 0),
                new Vector3f(1, 0, 0));
        // LIGHTS
        tLightNames = new String[] {"RoomLight", "CorridorLight"};
        
        tLightCoords = new Vector3f[] {
            new Vector3f(-10,8,0), new Vector3f(-35,8,0)
        };
        
        tLightAffected = new String[][] {
            {"Room"}, {"Corridor"}
        };
        
        lightMap = addLights(app, route, tLightNames, tLightCoords, tLightAffected);

        // OBJECTS
        
        Spatial leverRoot = app.getAssetManager().loadModel("Models/Lever.j3o");
        leverRoot.setLocalTranslation(0f, 5f, 0f);
        leverRoot.setLocalRotation(new Quaternion().fromAngles(0, 0f, FastMath.PI/2));
        // WARNING: Rigid body applied after this transform - axis offset
        
        // hacky but it works :)
        Spatial leverRod = ((Node) leverRoot).descendantMatches("Lever").get(0);
        
        // object physics
        StaticDemoObject leverBaseObj = new StaticDemoObject("leverBase", leverRoot, true);
        leverBaseObj.getLights().add(lightMap.get("Room"));
        route.objects.add(leverBaseObj);
        
        // TODO bounding box actually required? see button
        KinematicDemoObject leverObj = new KinematicDemoObject("leverRod", leverRod, 1f, false, null, null);
        leverObj.getLights().add(lightMap.get("Room"));
        route.objects.add(leverObj);
        
        // object events
        route.properties.putInt(leverObj.getObjId(), 0);
        eInter = new LeverEvent("leverInteraction", leverObj);
        route.setInteractable(leverRoot, eInter);
        
        routes.put(route.getId(), route);
    }
    
    private static void addButtonRoute(MainApplication app, final HashMap<String, DemoRoute> routes) {
        DemoRoute route;
        InteractionEvent eInter;
        DemoLight light;
        
        route = new DemoRoute("ButtonRoute", "Scenes/ButtonRoute.j3o", new Vector3f(0, HALFCHARHEIGHT + 1.0f, 0),
               
                new Vector3f(1, 0, 0));

        // LIGHTS
        tLightNames = new String[] {"RoomLight", "CorridorLight1", "CorridorLight2"};
        
        tLightCoords = new Vector3f[] {
            new Vector3f(0,8,0), new Vector3f(-35,8,0), new Vector3f(35,8,0)
        };
        
        tLightAffected = new String[][] {
            {"Room"}, {"Corridor1"}, {"Corridor2"}
        };
        
        lightMap = addLights(app, route, tLightNames, tLightCoords, tLightAffected);
        
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
        button.setLocalTranslation(0f,4f,-7f);
        button.setLocalRotation(new Quaternion().fromAngles(FastMath.PI/4, 0f,0f));
        button.move(new Vector3f(0f,1f,1f).normalize().mult(0.2f/(float)Math.sqrt(2f)));
        
        // object physics
        KinematicDemoObject buttonObj = new KinematicDemoObject("Button", button, 1f, true, null, null);
        buttonObj.getLights().add(lightMap.get("Room"));
        route.objects.add(buttonObj);
        
        // object events
        eInter = new InteractionEvent("buttonInteraction", buttonObj) {
            public final static int DELAY = 1;
            public Vector3f displacement = new Vector3f(0f,1f,1f).normalize().mult(0.2f/(float)Math.sqrt(2f));
            @Override
            public void onDemoEvent(MainApplication app) {
                // TODO hack removal
                DemoRoute route = app.routes.get("ButtonRoute");
                KinematicDemoObject kinematicObj = (KinematicDemoObject) getObject();
                
                // TODO different property/affect?
                route.properties.putBoolean(getObject().getObjId(), true);
                
                if (kinematicObj.getTasks().isEmpty()) {
                    getObject().getSpatial().move(displacement.negate());
                    kinematicObj.queueDelay(app, DELAY);
                    kinematicObj.queueDisplacement(app, 0.1f, displacement, displacement.length());
                }
            }
        };
        route.setInteractable(button, eInter);
        
        routes.put(route.getId(), route);
    }
    
    private static DemoLight addLight(MainApplication app, DemoRoute route, Vector3f loc, String[] spatialNames) {
        PointLight l = new PointLight();
        l.setColor(ColorRGBA.Gray);
        l.setPosition(loc);
        l.setRadius(1000f);
        
        DemoLight dLight =  new DemoLight(l, spatialNames);
        route.lights.add(dLight);

        PointLightShadowRenderer plsr = new PointLightShadowRenderer(app.getAssetManager(), 1024);
        plsr.setLight(l);
        plsr.setFlushQueues(false);
        plsr.setShadowIntensity(0.1f);
        route.shadowRenderers.add(plsr);
        
        return dLight;
    }
    
    private static HashMap<String, DemoLight> addLights(MainApplication app, DemoRoute route, String[] lightNames, Vector3f[] lightCoords, String[][] spatialNames) {
        HashMap<String, DemoLight> result = new HashMap<>();
        
        if (lightNames.length != lightCoords.length || lightNames.length != spatialNames.length)
            throw new RuntimeException("Error: incorrect number of light properties");
        
        for (int i = 0; i < spatialNames.length; ++i) {
            result.put(lightNames[i], addLight(app, route, lightCoords[i], spatialNames[i]));
        }
        return result;
    }
    
    private static class LeverEvent extends InteractionEvent {
        private int leverCount;

        public LeverEvent(String id, DemoObject object) {
            super(id, object);
        }

        @Override
        public void onDemoEvent(MainApplication app) {
            DemoRoute leverRoute = app.routes.get("LeverRoute");
//            int leverCount = leverRoute.properties.getInt(getObject().getObjId());
            KinematicDemoObject kinematicObj = (KinematicDemoObject) getObject();
            if (leverCount < 10) {
                if (leverCount % 2 == 0) {
                    kinematicObj.queueRotation(app, 0.2f, new Vector3f(1f, 0f, 0), -FastMath.PI / 2);
                } else {
                    kinematicObj.queueRotation(app, 0.2f, new Vector3f(1f, 0f, 0), FastMath.PI / 2);
                }
            } else {
                app.getGameScreen().setDialogueTextSequence(new String[]{"You broke it. Well done."});
            }
            ++leverCount;
            leverRoute.properties.putInt(getObject().getObjId(), leverCount);
        }
    };
    
    private static class PressurePlateEvent extends ProximityEvent {
        public final static int DELAY = 1;
        
        public PressurePlateEvent(String id, BoundingBox bound, DemoObject object) {
            super(id, bound, object);
        }

        @Override
        public void onDemoEvent(MainApplication app) {
            DemoRoute route = app.routes.get("PuzzleRoute");
            if (!(route.properties.containsKey(object.getObjId()))) {
                throw new RuntimeException("Error: Property not found.");
            }
            Boolean plateDown = route.properties.getBoolean(object.getObjId());
            // TODO again hacky like leverRod mesh
            KinematicDemoObject kinematicObj = (KinematicDemoObject) object;
            if (!plateDown) {
                object.getSpatial().move(0, -0.75f, 0);
                route.properties.putBoolean(object.getObjId(), true);

                kinematicObj.queueDelay(app, DELAY);
                kinematicObj.queueDisplacement(app, 0.1f, Vector3f.UNIT_Y, 0.75f);
                kinematicObj.queueProperty(app, 0.0f, route.properties, object.getObjId(), false);
            }
            if (kinematicObj.getTasks().isEmpty()) {
                throw new RuntimeException("Error: Illegal pressure plate state for: " + object.getObjId());
            } else {
                DemoTask currentTask = kinematicObj.getTasks().getFirst();
                if (currentTask instanceof KinematicTask) {
                    currentTask.resetTime();
                } else if (currentTask instanceof TranslationTask) {
                    kinematicObj.getTasks().remove(currentTask);
                    float x = (-0.75f * currentTask.getCurrentTime() / currentTask.getCompletionTime());
                    object.getSpatial().move(0, x, 0);
                } else if (currentTask instanceof AddPropertyTask) {
                    kinematicObj.getTasks().remove(currentTask);
                }
            }
        }
    };
    
    private static class ExitRouteEvent extends LocationEvent {
        public ExitRouteEvent(String id, BoundingBox bound) {
            super(id, bound);
        }

        @Override
        public void onDemoEvent(MainApplication app) {
            try {
                //Ending the route that was started to show the correct character select screen to the player
                app.getNarrativeInstance().endRoute(app.getGameScreen().getRoute());
            } catch (GraphElementNotFoundException ex) {
                Logger.getLogger(Initialiser.class.getName()).log(Level.SEVERE, null, ex);
            }
            app.nifty.gotoScreen("characterSelect");
            app.getGameScreen().setDialogueTextSequence(new String[]{"Are you now in the puzzle room?"});        }
    }
}
