package richardson.wyatt.application;

import static org.lwjgl.glfw.GLFW.*;

import static org.lwjgl.opengl.GL30.*;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.openvr.VREventEditingCameraSurface;

import richardson.wyatt.game_entities.entity.Entity;
import richardson.wyatt.rendering.*;
import richardson.wyatt.utils.KeyInput;
import richardson.wyatt.utils.MouseInput;

import static org.lwjgl.glfw.Callbacks.*;

public final class Scene { 
	public Renderer renderer;
	public Camera activeCamera;

	private List<Camera> cameras;
	private String id;
	
	public Scene(String id) { 
		this.id = id;
		Window.addScene(this);
		renderer = new Renderer();
		cameras = new ArrayList<Camera>();
	}
	
	public void init() {
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		glEnable(GL_DEPTH_TEST);
	}
	
	public void loop(float dt) {
		
		switchCamera();
		renderer.render(dt, activeCamera);
	}
	
	private void switchCamera() {
		for(Camera c: cameras) {
			if(c.isActive()) {
				activeCamera = c;
				return;
			}
		}
		System.err.println("NO ACTIVE CAMERA!");
		throw new IllegalStateException();
	}
	
	
	public void addEntity(Entity entity) {
		if(entity.getClass().getSimpleName().equals("Camera")) {
			cameras.add((Camera)entity);
		}else {
			renderer.addEntity(entity);
		}
	}
	public void cleanUp() {
		renderer.cleanUp();
	}
	
	
}