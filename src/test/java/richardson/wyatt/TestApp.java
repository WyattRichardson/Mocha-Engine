package richardson.wyatt;

import static org.lwjgl.opengl.GL30.*;

import org.joml.Math;
import org.joml.Vector3f;

import richardson.wyatt.application.Camera;
import richardson.wyatt.application.Scene;
import richardson.wyatt.application.Window;
import richardson.wyatt.game_entities.entity.Entity;
import richardson.wyatt.game_entities.entity.EntityComponent;
import richardson.wyatt.game_entities.entity.EntityController;
import richardson.wyatt.game_entities.entity.Transform;
import richardson.wyatt.game_entities.lighting.Light;
import richardson.wyatt.game_entities.model.Model;
import richardson.wyatt.game_entities.terrain.Terrain;
import richardson.wyatt.game_entities.textures.ModelTexture;
import richardson.wyatt.utils.KeyInput;
import richardson.wyatt.utils.MouseInput;

import static org.lwjgl.glfw.GLFW.*;

public class TestApp {

	public static final int WINDOW_WIDTH = 1920;
	public static final int WINDOW_HEIGHT = 1080;
	public static final float[] WINDOW_CLEAR_COLOR = {0f, .25f, 1f, 1 };

	public static void main(String[] args) {
		Window.init(WINDOW_WIDTH, WINDOW_HEIGHT, WINDOW_CLEAR_COLOR);
		Scene testScene = new Scene("Main_Scene");
		
		
		Entity mando = new Entity("Mando");
		Model mandoModel = new Model("Mandalorian.obj", GL_TRIANGLES);
		EntityComponent mandoTransform = new Transform(0, 0, -10, 0, 0, 0, 0.2f);
		EntityComponent mandoController = new EntityController() {

			@Override
			public void tick(float dt) {
			}
			
		};
		ModelTexture mandoTex = new ModelTexture("metal.png", GL_TEXTURE0);
		mandoModel.setTexture(mandoTex);
		mando.addComponent(mandoModel);
		mando.addComponent(mandoTransform);
		mando.addComponent(mandoController);
		testScene.addEntity(mando);

		
		Entity lowPolyCharacter = new Entity("Low_Poly_Character");
		Model lPCModel = new Model("LowPolyCharacter.dae", GL_TRIANGLES);
		EntityComponent lPCTransform = new Transform(0, 0, -1, 0, 0, 0, 1);
		EntityComponent lPCController = new EntityController() {

			@Override
			public void tick(float dt) {
				Transform transform = (Transform) lPCTransform;
				transform.getRotation().y += dt * 90;
			}
			
		};

		lowPolyCharacter.addComponent(lPCModel);
		lowPolyCharacter.addComponent(lPCTransform);
		lowPolyCharacter.addComponent(lPCController);
		testScene.addEntity(lowPolyCharacter);
		
		Entity sun = new Entity("Sun");
		EntityComponent sunLight = new Light(new Vector3f(.95f,.5f,.2f));
		EntityComponent sunTransform = new Transform(0, 1000, 0, 0, 0, 0, 1);
		sun.addComponent(sunLight);
		sun.addComponent(sunTransform);
		testScene.addEntity(sun);
		
		Entity spotLight1 = new Entity("Spot_Light_1");
		EntityComponent spotLight = new Light(new Vector3f(0.3f, 0.3f, 0.3f));
		EntityComponent spotLight1Transform = new Transform(-10, 3, 10, 0, 0, 0, 1);
		EntityComponent spotLight1Controller = new EntityController() {
			@Override
			public void tick(float dt) {
				Transform transform = (Transform) spotLight1Transform;
				float speed = 50;
				if(KeyInput.isKeyDown(GLFW_KEY_LEFT)){
					transform.getPosition().x -= (speed * dt);
				}
				if(KeyInput.isKeyDown(GLFW_KEY_RIGHT)) {
					transform.getPosition().x += (speed * dt);
				}
				if(KeyInput.isKeyDown(GLFW_KEY_UP)) {
					transform.getPosition().y += (speed * dt);
				}
				if(KeyInput.isKeyDown(GLFW_KEY_DOWN)) {
					transform.getPosition().y -= (speed * dt);
				}
			}
		};
		spotLight1.addComponent(spotLight1Transform);
		spotLight1.addComponent(spotLight);
		spotLight1.addComponent(spotLight1Controller);
		testScene.addEntity(spotLight1);
		

		
		Camera mainCam = new Camera("Main_Camera");
		EntityComponent mainCamTransform = new Transform(0, 0, 10, 0, 0, 0, 1);
		EntityComponent mainCamController = new EntityController() {
			public static final int BASE_SPEED = 10;
			private int speed;
			private float turnSpeed = 3;
			@Override
			public void tick(float dt) {
				Transform transform = (Transform) mainCamTransform;
				transform.getRotation().y += MouseInput.deltaX * turnSpeed * dt;
				transform.getRotation().x += MouseInput.deltaY * turnSpeed * dt;
				if(transform.getRotation().x > 45) {
					transform.getRotation().x = 45;
				}else if(transform.getRotation().x < -45) {
					transform.getRotation().x = -45;
				}
				
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
				
				if(KeyInput.isKeyDown(GLFW_KEY_SPACE)) {
					speed = BASE_SPEED;
					transform.getPosition().y += speed * dt;
				}
				if(KeyInput.isKeyDown(GLFW_KEY_LEFT_CONTROL)) {
					speed = -BASE_SPEED;
					transform.getPosition().y += speed * dt;
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
		testScene.addEntity(mainCam);
		testScene.setActiveCamera(mainCam);
		
		Terrain terrainOne = new Terrain("Terrain_1");
		Model terrainModel = Model.getRandomTerrainModel(100, 100, 1);
		Transform terrainTransform = new Transform(0,0,0,0,0,0,1);
		terrainOne.addComponent(terrainTransform);
		terrainOne.addComponent(terrainModel);
		testScene.addEntity(terrainOne);
		
		
		Window.setActiveScene(testScene);
		Window.run();
	}
}
