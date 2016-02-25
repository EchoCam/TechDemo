
package uk.ac.cam.echo2016.dynademo;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import static uk.ac.cam.echo2016.dynademo.MainApplication.CHARHEIGHT;

import java.util.ArrayList;

import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.shadow.PointLightShadowRenderer;

/**
 * @author tr393
 */
public class Initialiser {
    
    /**
     * 
     * @param app - required for event subscribing and renderer attaching
     * @return 
     */
    public static ArrayList<DemoRoute> initialiseRoutes(MainApplication app) {
        ArrayList<DemoRoute> routes = new ArrayList<DemoRoute>();
        
        DemoRoute route;
        DemoLocEvent e;

        // BEDROOM ROUTE
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
            PointLight l = new PointLight();
            l.setColor(ColorRGBA.Gray);
            l.setPosition(lightCoords[i]);
            l.setRadius(1000f);
            
            route.lights.add(new DemoLight(l, spatialNames[i]));
            
            PointLightShadowRenderer plsr = new PointLightShadowRenderer(app.getAssetManager(), 1024);
            plsr.setLight(l);
            plsr.setFlushQueues(false);
            plsr.setShadowIntensity(0.1f);
            route.shadowRenderers.add(plsr);
//            viewPort.addProcessor(plsr);
        }
        
        // OBJECTS
        Spatial crate = app.getAssetManager().loadModel("Models/Crate.j3o");
        Material mat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        crate.setMaterial(mat);
        crate.setLocalTranslation(0, 0, -30);
        route.objects.add(crate);
        
        // EVENTS
        e = new DemoLocEvent(0, new Vector3f(-80, 1, -40), 40, 14, 50); 
        e.listeners.add(app);
        route.events.add(e);
        routes.add(route);

        // Second Route
        route = new DemoRoute("Parkour", "Scenes/Scene2.j3o", new Vector3f(0, (CHARHEIGHT / 2) + 2.5f, 0), new Vector3f(-1, 0, 0));
        routes.add(route);
        
        return routes;
    }
}
