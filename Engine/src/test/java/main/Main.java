package main;
import static org.lwjgl.opengl.GL30.*;

import org.joml.Vector3f;

import core.application.Scene;
import core.gameobjects.camera.Camera;
import core.gameobjects.entity.Entity;
import core.gameobjects.lighting.Light;
import core.gameobjects.model.Model;

public class Main {
	
	public static final int WINDOW_WIDTH = 1080;
	public static final int WINDOW_HEIGHT = 720;
	public static final String WINDOW_TITLE = "Java Game Engine";
	public static final float[] CLEAR_COLOR = {0.3f,0.3f,0.3f,1};
	
	
	
	
	public static void main(String[] args) {
		
		Scene sandbox = new Scene(WINDOW_WIDTH, WINDOW_HEIGHT, WINDOW_TITLE, CLEAR_COLOR);
		Model squareModel = new Model("Square.obj", GL_TRIANGLES);
		Model mando = new Model("Mandalorian.obj", GL_TRIANGLES);
		Light sunLight = new Light(new Vector3f(0,200,0), new Vector3f(0,0,0), new Vector3f(1,1,1));
		Light light_2Light = new Light(new Vector3f(-200,200,0), new Vector3f(0,0,0), new Vector3f(1,0,0));
		Light light_3Light = new Light(new Vector3f(200,200,0), new Vector3f(0,0,0), new Vector3f(0,0,1));
		Light light_4Light = new Light(new Vector3f(0,200,-200), new Vector3f(0,0,0), new Vector3f(0,1,0));
		
		Player player_1 = new Player("player_1");
		player_1.addComponent(mando);
		player_1.hasController = true;
		player_1.setPosition(0, 0, -100);
		sandbox.addEntity(player_1);
		
		Camera playerCam = new Camera(player_1);
		playerCam.isActive = true;
		PlayerCameraController playerCamController = new PlayerCameraController(playerCam);
		playerCam.setController(playerCamController);
		sandbox.addCamera(playerCam);
		
		MovingLight sun = new MovingLight("Sun_Light");
		sun.addComponent(sunLight);
		sun.hasController = true;
		sandbox.addLightEntity(sun);
		
		Entity light_2 = new Entity("light_2");
		light_2.addComponent(light_2Light);
		sandbox.addLightEntity(light_2);
		
		Entity light_3 = new Entity("light_3");
		light_3.addComponent(light_3Light);
		sandbox.addLightEntity(light_3);
		
		Entity light_4 = new Entity("light_4");
		light_4.addComponent(light_4Light);
		sandbox.addLightEntity(light_4); 
		
		sandbox.init(); 
		
	}
}
