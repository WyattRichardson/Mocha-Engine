package main;

import static org.lwjgl.glfw.GLFW.*;

import core.gameobjects.entity.Entity;
import core.gameobjects.entity.EntityController;
import core.gameobjects.lighting.Light;
import core.utils.KeyInput;

public class MovingLight extends Entity implements EntityController {

	private float speed = 1000;

	public MovingLight(String EntityID) {
		super(EntityID);
	}

	@Override
	public void tick(float dt) {
		updateTransform(dt);

	}

	@Override
	public void updateTransform(float dt) {
		Light light = (Light)components.get(0);
		if(KeyInput.isKeyDown(GLFW_KEY_W)){
		    light.transform.position.y += (speed * dt);
		}
		if(KeyInput.isKeyDown(GLFW_KEY_S)){
			light.transform.position.y -= (speed * dt);
		}
		if(KeyInput.isKeyDown(GLFW_KEY_A)){
			light.transform.position.x -= (speed * dt);
		}
		if(KeyInput.isKeyDown(GLFW_KEY_D)){
			light.transform.position.x += (speed * dt);
		}
		
	}





}
