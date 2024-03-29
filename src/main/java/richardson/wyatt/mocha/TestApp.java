package richardson.wyatt.mocha;

import static org.lwjgl.opengl.GL30.*;

import java.util.Random;

import org.joml.Math;
import org.joml.Vector3f;

import richardson.wyatt.mocha.application.Scene;
import richardson.wyatt.mocha.application.Window;
import richardson.wyatt.mocha.game_entities.entity.Camera;
import richardson.wyatt.mocha.game_entities.entity.Entity;
import richardson.wyatt.mocha.game_entities.entity.EntityComponent;
import richardson.wyatt.mocha.game_entities.entity.EntityController;
import richardson.wyatt.mocha.game_entities.entity.Transform;
import richardson.wyatt.mocha.game_entities.lighting.Light;
import richardson.wyatt.mocha.game_entities.model.Model;
import richardson.wyatt.mocha.game_entities.terrain.Terrain;
import richardson.wyatt.mocha.game_entities.textures.ModelTexture;
import richardson.wyatt.mocha.utils.KeyInput;
import richardson.wyatt.mocha.utils.MouseInput;

import static org.lwjgl.glfw.GLFW.*;

public class TestApp {

	public static final int WINDOW_WIDTH = 1080;
	public static final int WINDOW_HEIGHT = 720;
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
		EntityComponent lPCTransform = new Transform(0, 100, 0, 0, 0, 0, 100);
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
		EntityComponent sunLight = new Light(new Vector3f(1,.8f,.5f));
		EntityComponent sunTransform = new Transform(500, 1000, 0, 0, 0, 0, 1);
		EntityComponent sunController = new EntityController() {
			@Override
			public void tick(float dt) {
				Transform transform = (Transform) sunTransform;
				float speed = 1000;
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
		sun.addComponent(sunLight);
		sun.addComponent(sunTransform);
		sun.addComponent(sunController);
		testScene.addEntity(sun);
		
//		Entity spotLight1 = new Entity("Spot_Light_1");
//		EntityComponent spotLight = new Light(new Vector3f(0.3f, 0.3f, 0.3f));
//		EntityComponent spotLight1Transform = new Transform(50, 50, 0, 0, 0, 0, 1);
//		spotLight1.addComponent(spotLight1Transform);
//		spotLight1.addComponent(spotLight);
//      testScene.addEntity(spotLight1);
		
		int seed = new Random().nextInt(100000);
		Terrain terrainOne = new Terrain("Terrain_1",200,seed,1000);
		Model terrainModel = Model.getRandomTerrainModel(terrainOne, 100000, 1);
		Transform terrainTransform = new Transform(0, 0 ,0,0,0,0,1);
		ModelTexture grassTex = new ModelTexture("MC_Grass.jpg", GL_TEXTURE0);
		ModelTexture sandTex = new ModelTexture("MC_Sand.jpeg", GL_TEXTURE0);
		//terrainModel.setTexture(grassTex);
		terrainModel.setTexture(grassTex);
		terrainOne.addComponent(terrainTransform);
		terrainOne.addComponent(terrainModel);
		testScene.addEntity(terrainOne);
		
		Camera mainCam = new Camera("Main_Camera");
		EntityComponent mainCamTransform = new Transform(100, 100, -100, 0, 0, 0, 1);
		EntityComponent mainCamController = new EntityController(terrainOne) {
			static final int BASE_SPEED = 20;
			static final float JUMP_POWER = 20;
			static final float GRAVITY = 50;
			float verticalVelocity = 0;
			int currentSpeed;
			float turnSpeed = 3;
			Terrain terrain = (Terrain) entities.get(0);
			boolean inAir = true;
			
			@Override
			public void tick(float dt) {
				Transform transform = (Transform) mainCamTransform;
				transform.getRotation().y += MouseInput.deltaX * turnSpeed * dt;
				transform.getRotation().x += MouseInput.deltaY * turnSpeed * dt;
				if(transform.getRotation().x > 90) {
					transform.getRotation().x = 90;
				}else if(transform.getRotation().x < -90) {
					transform.getRotation().x = -90;
				}
				
				if(KeyInput.isKeyDown(GLFW_KEY_A)){
					currentSpeed = BASE_SPEED;
					transform.getRotation().y -= 90;
					findNextPos(dt, transform);
					transform.getRotation().y += 90;
				}
				if(KeyInput.isKeyDown(GLFW_KEY_D)){
					currentSpeed = BASE_SPEED;
					transform.getRotation().y += 90;
					findNextPos(dt, transform);
					transform.getRotation().y -= 90;
				}
				if(KeyInput.isKeyDown(GLFW_KEY_W)) {
					currentSpeed = BASE_SPEED;
					findNextPos(dt, transform);
				}
				if(KeyInput.isKeyDown(GLFW_KEY_S)) {
					currentSpeed = -BASE_SPEED;
					findNextPos(dt, transform);
				}
				
				if(KeyInput.isKeyDown(GLFW_KEY_SPACE) && !inAir) {
					verticalVelocity = JUMP_POWER;
					inAir = true;
				}
				if(KeyInput.isKeyDown(GLFW_KEY_LEFT_CONTROL)) {
					currentSpeed = -BASE_SPEED;
					transform.getPosition().y += currentSpeed * dt;
				}
				
			
				float height = terrain.getHeight(transform.getPosition().x, transform.getPosition().z);
				if(inAir) {
					verticalVelocity -= GRAVITY * dt;
				}else {
					verticalVelocity = -GRAVITY;
				}
				transform.getPosition().y += verticalVelocity * dt;
				if(transform.getPosition().y < height+5) {
					transform.getPosition().y = height+5;
					inAir = false;
				}
			}
			
			private void findNextPos(float dt, Transform transform) {
				float distanceTraveled = dt * currentSpeed;
				transform.getPosition().x += Math.sin(Math.toRadians(transform.getRotation().y)) * distanceTraveled;
				transform.getPosition().z -= Math.cos(Math.toRadians(transform.getRotation().y)) * distanceTraveled;
				//transform.getPosition().y -= Math.sin(Math.toRadians(transform.getRotation().x)) * distanceTraveled;
			}
			
		};
		mainCam.addComponent(mainCamTransform);
		mainCam.addComponent(mainCamController);
		testScene.addEntity(mainCam);
		testScene.setActiveCamera(mainCam);
		

		
		
		Window.setActiveScene(testScene);
		Window.run();
	}
}
