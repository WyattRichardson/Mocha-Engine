package richardson.wyatt;

import static org.lwjgl.opengl.GL30.*;

import org.joml.Math;
import org.joml.Vector3f;

import richardson.wyatt.application.Scene;
import richardson.wyatt.game_entities.camera.Camera;
import richardson.wyatt.game_entities.entity.Entity;
import richardson.wyatt.game_entities.entity.EntityComponent;
import richardson.wyatt.game_entities.entity.EntityController;
import richardson.wyatt.game_entities.entity.Transform;
import richardson.wyatt.game_entities.entity.EntityComponent.Type;
import richardson.wyatt.game_entities.lighting.Light;
import richardson.wyatt.game_entities.model.Model;
import richardson.wyatt.game_entities.textures.ModelTexture;
import richardson.wyatt.utils.KeyInput;
import richardson.wyatt.utils.MouseInput;

import static org.lwjgl.glfw.GLFW.*;

public class Main {

	public static final int WINDOW_WIDTH = 1920;
	public static final int WINDOW_HEIGHT = 1080;
	public static final String WINDOW_TITLE = "Java Game Engine";
	public static final float[] CLEAR_COLOR = { 0.3f, 0.3f, 0.3f, 1 };

	public static void main(String[] args) {
		
		Scene testScene = new Scene(WINDOW_WIDTH, WINDOW_HEIGHT, WINDOW_TITLE, CLEAR_COLOR);
		
		
		Entity mando = new Entity("Mando");
		Model mandoModel = new Model("Mandalorian.obj", GL_TRIANGLES);
		EntityComponent mandoTransform = new Transform(0, 0, -10, 0, 0, 0, 0.2f);
		EntityComponent mandoController = new EntityController() {

			@Override
			public void tick(float dt) {
//				float turnSpeed = 180;
//				Transform trans = (Transform) mandoTransform;
//				if(KeyInput.isKeyDown(GLFW_KEY_W)){
//					trans.getRotation().x += turnSpeed * dt;
//				}
//				if(KeyInput.isKeyDown(GLFW_KEY_A)){
//					trans.getRotation().y -= turnSpeed * dt;
//				}
//				if(KeyInput.isKeyDown(GLFW_KEY_S)){
//					trans.getRotation().x -= turnSpeed * dt;
//				}
//				if(KeyInput.isKeyDown(GLFW_KEY_D)){
//					trans.getRotation().y += turnSpeed * dt;
//				}
				
			}
			
		};
		ModelTexture mandoTex = new ModelTexture("metal.png", GL_TEXTURE0);
		mandoModel.setTexture(mandoTex);
		mando.addComponent(mandoModel);
		mando.addComponent(mandoTransform);
		mando.addComponent(mandoController);
		testScene.addEntity(mando);
		

		

		Entity sun = new Light("Sun", new Vector3f(1f,1f,1f));
		EntityComponent sunTransform = new Transform(0, 200, 200, 0, 0, 0, 1);
		EntityComponent sunController = new EntityController() {

			@Override
			public void tick(float dt) {
//				Transform transform = (Transform) sunTransform;
//				float speed = 1000;
//				if(KeyInput.isKeyDown(GLFW_KEY_LEFT)){
//					transform.getPosition().x -= (speed * dt);
//				}
//				if(KeyInput.isKeyDown(GLFW_KEY_RIGHT)) {
//					transform.getPosition().x += (speed * dt);
//				}
//				if(KeyInput.isKeyDown(GLFW_KEY_UP)) {
//					transform.getPosition().y += (speed * dt);
//				}
//				if(KeyInput.isKeyDown(GLFW_KEY_DOWN)) {
//					transform.getPosition().y -= (speed * dt);
//				}
			}

			
		};
		sun.addComponent(sunTransform);
		sun.addComponent(sunController);
		testScene.addEntity(sun);

		
		Entity mainCam = new Camera("MainCamera");
		EntityComponent mainCamTransform = new Transform(0, 0, 10, 0, 0, 0, 1);
		EntityComponent mainCamController = new EntityController() {
			public static final int BASE_SPEED = 100;
			private int speed;
			private float turnSpeed = 10;
			@Override
			public void tick(float dt) {
				Transform transform = (Transform) mainCamTransform;
				transform.getRotation().y += MouseInput.deltaX * turnSpeed * dt;
				transform.getRotation().x += MouseInput.deltaY * turnSpeed * dt;
				
				if(KeyInput.isKeyDown(GLFW_KEY_A)){
					speed = BASE_SPEED;
					transform.getRotation().y -= 90;
					findNextPos(dt, transform);
					transform.getRotation().y += 90;
				}
				if(KeyInput.isKeyDown(GLFW_KEY_D)){
					speed = BASE_SPEED;
					transform.getRotation().y += 90;
					findNextPos(dt, transform);
					transform.getRotation().y -= 90;
				}
				if(KeyInput.isKeyDown(GLFW_KEY_W)) {
					speed = BASE_SPEED;
					findNextPos(dt, transform);
				}
				if(KeyInput.isKeyDown(GLFW_KEY_S)) {
					speed = -BASE_SPEED;
					findNextPos(dt, transform);
				}	
			}
			
			private void findNextPos(float dt, Transform transform) {
				float distanceTraveled = dt * speed;
				transform.getPosition().x += Math.sin(Math.toRadians(transform.getRotation().y)) * distanceTraveled;
				transform.getPosition().z -= Math.cos(Math.toRadians(transform.getRotation().y)) * distanceTraveled;
				//transform.getPosition().y -= Math.sin(Math.toRadians(transform.getRotation().x)) * distanceTraveled;
			}
			
		};
		mainCam.addComponent(mainCamTransform);
		mainCam.addComponent(mainCamController);
		((Camera) mainCam).setActive(true);
		testScene.addEntity(mainCam);
	
		testScene.init(); 
		
	}
}
