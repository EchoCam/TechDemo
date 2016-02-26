
package uk.ac.cam.echo2016.dynademo;

import static uk.ac.cam.echo2016.dynademo.MainApplication.CHARHEIGHT;


import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.shadow.PointLightShadowRenderer;
import java.util.HashMap;

/**
 * @author tr393
 */
public class Initialiser {
    
    /**
     * 
     * @param app - required for event subscribing and renderer attaching
     * @return 
     */
    public static HashMap<String, DemoRoute> initialiseRoutes(MainApplication app) {
        HashMap<String, DemoRoute> routes = new HashMap<String, DemoRoute>();
        
        DemoRoute route;
        DemoLocEvent eLoc;
        DemoInteractEvent eInter;

//////// Bedroom ////////
        route = new DemoRoute("Bedroom", "Scenes/Bedroom.j3o", new Vector3f(0, (CHARHEIGHT / 2) + 2.5f, 0), new Vector3f(1, 0, 0));
        
        // LIGHTS
        Vector3f[] lightCoords = {
            new Vector3f(0f,6f,0f),
            new Vector3f(25f,6f,0f),
            new Vector3f(25f,6f,-30f),
            new Vector3f(0f,6f,-30f),
            new Vector3f(-25f,6f,-30f),            
            new Vector3f(-25f,6f,0f),

            new Vector3f(25f,6f,-15f),
            new Vector3f(-25f,6f,-15f),
//            
//            new Vector3f(-60f,6f,0f),
//            new Vector3f(-60f,6f,-30f)
        };
        String[][] spatialNames = {
        	{"Room1"},//,"Corridor"},
        	{"Room2"},//,"Corridor"},
        	{"Room3"},//,"Corridor"},
        	{"Room4"},//,"Corridor"},
        	{"Room5"},//,"Corridor"},
        	{"Room6"},//,"Corridor"}
        	{"Corridor"},
        	{"Corridor"},
        };
        for(int i = 0; i< spatialNames.length; ++i) {
            addLight(app,route,lightCoords[i],spatialNames[i]);
        }
        // EVENTS
        eLoc = new DemoLocEvent("Node1", new Vector3f(-80, 1, -40), 40, 14, 50); 
        eLoc.addListener(app);
        route.events.add(eLoc);
        routes.put(route.getId(),route);

//////// Puzzle Room ////////
        route = new DemoRoute("PuzzleRoom", "Scenes/PuzzleRoom.j3o", new Vector3f(0, (CHARHEIGHT / 2) + 2.5f, 0), new Vector3f(0, 0, -1));

        // LIGHTS
        addLight(app, route, new Vector3f(0,8f,0), new String[]{"Room", "Crate"});
        // OBJECTS
        Spatial crate = app.getAssetManager().loadModel("Models/Crate.j3o");
//        Material crateMat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
//      crate.setMaterial(crateMat);
        crate.setLocalTranslation(0, 0, -30);
        
        // Pickup Event
        eInter = new DemoInteractEvent("crate", crate, 0);
        eInter.addListener(app);
        route.setInteractable(crate, eInter);
        route.dynamicObjects.add(crate);
        
        routes.put(route.getId(),route);
        
//////// Lever Room ////////
        route = new DemoRoute("LeverRoom", "Scenes/LeverRoom.j3o", new Vector3f(0, (CHARHEIGHT / 2) + 2.5f, 0), new Vector3f(1, 0, 0));
        
        // LIGHTS
        addLight(app, route, new Vector3f(0,0,0), new String[]{"Room"});
         
        // OBJECTS
//        Spatial lever = app.getAssetManager().loadModel("Models/Lever.j3o");
//        Material leverMat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
//        lever.setMaterial(leverMat);
//        lever.setLocalTranslation(0f, 0f, 0f);
        
//////// Button Room ////////
        
        route = new DemoRoute("ButtonRoom", "Scenes/ButtonRoom.j3o", new Vector3f(0, (CHARHEIGHT / 2) + 2.5f, 0), new Vector3f(1, 0, 0));
        
        // LIGHTS
        addLight(app, route, new Vector3f(0f,8f,0f), new String[]{"Room","LeverBase"});
        // OBJECTS
        Spatial lever = app.getAssetManager().loadModel("Models/Lever.j3o");
//        Material leverMat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
//        lever.setMaterial(leverMat);
        lever.setLocalTranslation(0f, 5f, 10f);
        lever.setLocalRotation(new Quaternion(-1f, 0f, 0f, 1f));
        route.staticObjects.add(lever);
        
        routes.put(route.getId(),route);
        
        return routes;
    }
    private static void addLight(MainApplication app, DemoRoute route, Vector3f loc, String[] spatialNames) {
            PointLight l = new PointLight();
            l.setColor(ColorRGBA.Gray);
            l.setPosition(loc);
            l.setRadius(1000f);
            
            route.lights.add(new DemoLight(l, spatialNames));
            
            PointLightShadowRenderer plsr = new PointLightShadowRenderer(app.getAssetManager(), 1024);
            plsr.setLight(l);
            plsr.setFlushQueues(false);
            plsr.setShadowIntensity(0.1f);
            route.shadowRenderers.add(plsr);
    }
}
