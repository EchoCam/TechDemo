package uk.ac.cam.echo2016.dynademo;


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

    /**
     * @param app - the application to attach events and renderers to
     * @return
     */
    
    public static HashMap<String, DemoRoute> initialiseRoutes(MainApplication app) {
        final HashMap<String, DemoRoute> routes = new HashMap<String, DemoRoute>();
        addBedroomRoute(app, routes);
        addPuzzleRoute(app, routes);
        addLeverRoute(app, routes);
        addButtonRoute(app, routes);
        return routes;
    }

    private static void addBedroomRoute(MainApplication app, HashMap<String, DemoRoute> routes) {
        DemoRoute route;
        LocationEvent eLoc;
        
        route = new DemoRoute("BedroomRoute", "Scenes/BedroomRoute.j3o", new Vector3f(0, HALFCHARHEIGHT + 1.0f, 0),
                new Vector3f(1, 0, 0));

        // LIGHTS
        Vector3f[] lightCoords = { new Vector3f(0f, 6f, 0f), new Vector3f(25f, 6f, 0f), new Vector3f(25f, 6f, -30f),
                new Vector3f(0f, 6f, -30f), new Vector3f(-25f, 6f, -30f), new Vector3f(-25f, 6f, 0f),

                new Vector3f(25f, 6f, -15f), new Vector3f(-25f, 6f, -15f),
        //
        // new Vector3f(-60f,6f,0f),
        // new Vector3f(-60f,6f,-30f)
        };
        String[][] spatialNames = { { "Room1" },// ,"Corridor"},
                { "Room2" },// ,"Corridor"},
                { "Room3" },// ,"Corridor"},
                { "Room4" },// ,"Corridor"},
                { "Room5" },// ,"Corridor"},
                { "Room6" },// ,"Corridor"}
                { "Corridor" }, { "Corridor" }, };
        for (int i = 0; i < spatialNames.length; ++i) {
            addLight(app, route, lightCoords[i], spatialNames[i]);
        }
        // EVENTS
        eLoc = new LocationEvent("Node1", new Vector3f(-80, 1, -40), 40, 14, 50) {

            @Override
            public void onDemoEvent(MainApplication app) {
                //TODO first meeting
                //could do...

                //routes.get(gameScreen.getRouteName());
                //to get the name of the route the player has selected                
                DemoRoute route = app.routes.get(app.getGameScreen().getRoute());
                try {
                    //Ending the route that was started to show the correct character select screen to the player
                    app.getNarrativeInstance().endRoute(app.getGameScreen().getRoute());
                } catch (GraphElementNotFoundException ex) {
                    Logger.getLogger(Initialiser.class.getName()).log(Level.SEVERE, null, ex);
                }
                app.nifty.gotoScreen("characterSelect");
//                app.loadRoute(route);
                app.loadRoute(app.routes.get("PuzzleRoute")); // temp functionality
                app.getGameScreen().setDialogueTextSequence(new String[]{"You are now in the puzzle room"});
            }
            
        };
        route.locEvents.add(eLoc);
        routes.put(route.getId(), route);
    }
    
    private static void addPuzzleRoute(MainApplication app, final HashMap<String, DemoRoute> routes) {
        DemoRoute route;
        LocationEvent eLoc;
        InteractionEvent eInter;
        DemoLight light;
        
        route = new DemoRoute("PuzzleRoute", "Scenes/PuzzleRoute.j3o", new Vector3f(0, HALFCHARHEIGHT + 1.0f, -45),
                new Vector3f(0, 0, 1));
        // LIGHTS
        light = addLight(app, route, new Vector3f(0, 8f, 0), new String[] {"Room"});
        addLight(app, route, new Vector3f(0,6,-35), new String[] {"Corridor1"});
        addLight(app, route, new Vector3f(0,6,35), new String[] {"Corridor2"});
        addLight(app, route, new Vector3f(20,10,5), new String[] {"TallCorridor1"});
        addLight(app, route, new Vector3f(40,10,5), new String[] {"TallCorridor2"});

        // Crate
        Spatial crate = app.getAssetManager().loadModel("Models/Crate.j3o");
        crate.setLocalTranslation(0, 0, -30);
        // object physics
        DynamicDemoObject crateObj = new DynamicDemoObject("crate", crate, 5f, true);
        crateObj.getLights().add(light);
        route.objects.add(crateObj);
        // object events
        eInter = new InteractionEvent("crateInteraction", crateObj){
            @Override
            public void onDemoEvent(MainApplication app) {
                app.drag(getObject().getSpatial());
            }
        };
        route.setInteractable(crate, eInter);
        
        // PressurePlate
        Spatial pressPlate1 = app.getAssetManager().loadModel("Models/PressurePlate.j3o");
        Spatial pressPlate2 = app.getAssetManager().loadModel("Models/PressurePlate.j3o");
        pressPlate1.setLocalTranslation(-5f, 0.1f, 5f);
        pressPlate2.setLocalTranslation(-5f, 0.1f, -5f);
        KinematicDemoObject plateObj1 = new KinematicDemoObject("pressurePlate1", pressPlate1, 1f, true);
        KinematicDemoObject plateObj2 = new KinematicDemoObject("pressurePlate2", pressPlate2, 1f, true);
        plateObj1.getLights().add(light);
        plateObj2.getLights().add(light);
        route.objects.add(plateObj1);
        route.objects.add(plateObj2);
        
        route.properties.putBoolean(plateObj1.getObjId(), false);
        route.properties.putBoolean(plateObj2.getObjId(), false);
        eLoc = new PressurePlateEvent("pressurePlate1", new Vector3f(-6.5f, 0.1f, 3.5f), 3f, 0.8f + HALFCHARHEIGHT, 3f, plateObj1);
        route.locEvents.add(eLoc);
        eLoc = new PressurePlateEvent("pressurePlate2", new Vector3f(-6.5f, 0.1f, -6.5f), 3f, 0.8f + HALFCHARHEIGHT, 3f, plateObj2);
        route.locEvents.add(eLoc);
        
        routes.put(route.getId(), route);
    }
    
    private static void addLeverRoute(MainApplication app, final HashMap<String, DemoRoute> routes) {
        DemoRoute route;
        DemoLight light;
        InteractionEvent eInter;
        
        route = new DemoRoute("LeverRoute", "Scenes/LeverRoute.j3o", new Vector3f(-40, HALFCHARHEIGHT + 1.0f, 0),
                new Vector3f(1, 0, 0));
        // LIGHTS
        light = addLight(app, route, new Vector3f(0, 0, 0), new String[] {"Room"});

        // OBJECTS
        
        Spatial leverRoot = app.getAssetManager().loadModel("Models/Lever.j3o");
        leverRoot.setLocalTranslation(0f, 5f, 0f);
        leverRoot.setLocalRotation(new Quaternion().fromAngles(0, 0f, FastMath.PI/2));
        // WARNING: Rigid body applied after this transform - axis offset
        
        // hacky but it works :)
        Spatial leverRod = ((Node) leverRoot).descendantMatches("Lever").get(0);
        
        // object physics
        StaticDemoObject leverBaseObj = new StaticDemoObject("leverBase", leverRoot, true);
        leverBaseObj.getLights().add(light);
        route.objects.add(leverBaseObj);
        
        KinematicDemoObject leverObj = new KinematicDemoObject("leverRod", leverRod, 1f, false);
        leverObj.getLights().add(light);
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
        light = addLight(app, route, new Vector3f(0f, 8f, 0f), new String[] {"Room"}); // LeverBase
        
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
        KinematicDemoObject buttonObj = new KinematicDemoObject("Button", button, 1f, true);
        buttonObj.getLights().add(light);
        route.objects.add(buttonObj);
        
        // object events
        eInter = new InteractionEvent("buttonInteraction", buttonObj) {
            @Override
            public void onDemoEvent(MainApplication app) {
                KinematicDemoObject kinematicObj = (KinematicDemoObject) getObject();
//                if (kinematicObj.getTasks().isEmpty())
                System.out.println("hi");
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    
    private static class LeverEvent extends InteractionEvent {

        public LeverEvent(String id, DemoObject object) {
            super(id, object);
        }

        @Override
        public void onDemoEvent(MainApplication app) {
            DemoRoute leverRoute = app.routes.get("LeverRoute");
            int leverCount = leverRoute.properties.getInt(getObject().getObjId());
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
        
        public PressurePlateEvent(String id, Vector3f loc, float width, float height, float depth, DemoObject object) {
            super(id, loc, width, height, depth, object);
        }

        @Override
        public void onDemoEvent(MainApplication app) {
            if (app.getPlayerControl().onGround()) {
                DemoRoute route = app.routes.get("PuzzleRoute");
                if (!(route.properties.containsKey(object.getObjId()))) {
                    throw new RuntimeException("Property not found.");
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
                    throw new RuntimeException("Illegal pressure plate state for: " + object.getObjId());
                } else {
                    DemoTask currentTask = kinematicObj.getTasks().getFirst();
                    if (currentTask instanceof KinematicTask) {
                        currentTask.resetTime();
                    } else if (currentTask instanceof TranslationTask) {
                        kinematicObj.getTasks().remove(currentTask);
                        float x = (-0.75f*currentTask.getCurrentTime()/currentTask.getCompletionTime());
                        object.getSpatial().move(0, x, 0);
                    } else if (currentTask instanceof AddPropertyTask) {
                        kinematicObj.getTasks().remove(currentTask);
                    }
                }
            }
        }
    };
    
}