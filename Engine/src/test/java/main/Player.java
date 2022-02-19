
package main;
import static org.lwjgl.glfw.GLFW.*;

import core.gameobjects.entity.EntityController;
import core.gameobjects.entity.Entity;
import core.gameobjects.entity.Transform;
import core.gameobjects.model.Model;
import core.utils.KeyInput;

public class Player extends Entity implements EntityController{
	public float turnSpeed = 180;
	public float speed = 10;
	public Player(String entityID) {
		super(entityID);
	}
	
	@Override
	public void tick(float dt) {
		updateTransform(dt);
				
	}
	
	public void updateTransform(float dt) {
		/*if(KeyInput.isKeyDown(GLFW_KEY_W)){
			transform.position.y += (speed * dt);
		}
		if(KeyInput.isKeyDown(GLFW_KEY_S)){
			transform.position.y -= (speed * dt);
		}
		if(KeyInput.isKeyDown(GLFW_KEY_A)){
			transform.position.x -= (speed * dt);
		}
		if(KeyInput.isKeyDown(GLFW_KEY_D)){
			transform.position.x += (speed * dt);
		}*/
		if(KeyInput.isKeyDown(GLFW_KEY_LEFT)){
			transform.rotation.y -= (turnSpeed * dt);
		}
		if(KeyInput.isKeyDown(GLFW_KEY_RIGHT)) {
			transform.rotation.y += (turnSpeed * dt);
		}
		if(KeyInput.isKeyDown(GLFW_KEY_UP)) {
			transform.rotation.x -= (turnSpeed * dt);
		}
		if(KeyInput.isKeyDown(GLFW_KEY_DOWN)) {
			transform.rotation.x += (turnSpeed * dt);
		}
	}


	
	


}
