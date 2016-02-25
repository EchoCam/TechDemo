package uk.ac.cam.echo2016.dynademo;

import com.jme3.light.Light;

public class DemoLight {
	public Light light;
	public String[] spatialNames;
	DemoLight(Light l, String[] ns) {
		light = l;
		spatialNames = ns;	
	}
}	
