package main;
import static org.lwjgl.opengl.GL30.*;

import org.joml.Vector3f;
import static org.lwjgl.glfw.GLFW.*;

import core.application.Scene;
import core.gameobjects.camera.Camera;
import core.gameobjects.entity.Entity;
import core.gameobjects.entity.EntityComponent;
import core.gameobjects.entity.EntityController;
import core.gameobjects.entity.Transform;
import core.gameobjects.entity.EntityComponent.Type;
import core.gameobjects.lighting.Light;
import core.gameobjects.model.Model;
import core.utils.KeyInput;

public class Main {
	
	public static final int WINDOW_WIDTH = 1920;
	public static final int WINDOW_HEIGHT = 1080;
	public static final String WINDOW_TITLE = "Java Game Engine";
	public static final float[] CLEAR_COLOR = {0.3f,0.3f,0.3f,1};
	
	
	
	
	public static void main(String[] args) {
		
		Scene testScene = new Scene(WINDOW_WIDTH, WINDOW_HEIGHT, WINDOW_TITLE, CLEAR_COLOR);
		
		
		Entity mando = new Entity("Mando");
		EntityComponent mandoModel = new Model(Type.MODEL, "Mandalorian.obj", GL_TRIANGLES);
		EntityComponent mandoTransform = new Transform(Type.TRANSFORM, 0, 0, -100, 0, 0, 0, 1);
		EntityComponent mandoController = new EntityController(Type.CONTROLLER) {

			@Override
			public void tick(float dt) {
				float turnSpeed = 180;
				Transform trans = (Transform) mandoTransform;
				if(KeyInput.isKeyDown(GLFW_KEY_W)){
					trans.getRotation().x += turnSpeed * dt;
				}
				if(KeyInput.isKeyDown(GLFW_KEY_A)){
					trans.getRotation().y -= turnSpeed * dt;
				}
				if(KeyInput.isKeyDown(GLFW_KEY_S)){
					trans.getRotation().x -= turnSpeed * dt;
				}
				if(KeyInput.isKeyDown(GLFW_KEY_D)){
					trans.getRotation().y += turnSpeed * dt;
				}
				
			}
			
		};
		mando.addComponent(mandoModel);
		mando.addComponent(mandoTransform);
		mando.addComponent(mandoController);
		testScene.addEntity(mando);
		

		

		Entity sun = new Light("Sun", new Vector3f(1f,1f,1f));
		EntityComponent sunTransform = new Transform(Type.TRANSFORM, 0, 200, 0, 0, 0, 0, 1);
		EntityComponent sunController = new EntityController(Type.CONTROLLER) {

			@Override
			public void tick(float dt) {
				Transform transform = (Transform) sunTransform;
				float speed = 100;
				if(KeyInput.isKeyDown(GLFW_KEY_LEFT)){
					transform.getPosition().x -= (speed * dt);
				}
				if(KeyInput.isKeyDown(GLFW_KEY_RIGHT)) {
					transform.getPosition().x += (speed * dt);
				}
				if(KeyInput.isKeyDown(GLFW_KEY_UP)) {
					transform.getPosition().z -= (speed * dt);
				}
				if(KeyInput.isKeyDown(GLFW_KEY_DOWN)) {
					transform.getPosition().z += (speed * dt);
				}
			}

			
		};
		sun.addComponent(sunTransform);
		sun.addComponent(sunController);
		testScene.addEntity(sun);

		
		Entity mainCam = new Camera("MainCamera");
		EntityComponent mainCamTransform = new Transform(Type.TRANSFORM, 0, 0, 0, 0, 0, 0, 1);
		EntityComponent mainCamController = new EntityController(Type.CONTROLLER) {

			@Override
			public void tick(float dt) {
				// TODO Auto-generated method stub
				
			}
			
		};
		mainCam.addComponent(mainCamTransform);
		mainCam.addComponent(mainCamController);
		((Camera) mainCam).setActive(true);
		testScene.addEntity(mainCam);
	
		testScene.init(); 
		
	}
}
