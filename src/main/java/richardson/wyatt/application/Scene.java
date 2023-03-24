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
	
	static { //Initialize GLFW statically to allow for multiple Scenes
		
		if(!glfwInit()) {
			System.out.println("FAILED TO INITILIZE GLFW!");
		}
		
	
	}
	
	public long windowID;
	public Renderer renderer;
	public Camera activeCamera;
	public static GLFWVidMode currentVidMode = null;

	private List<Camera> cameras;
	
	
	public Scene(int width, int height, String title, float[] clearColor) { 
		windowID = glfwCreateWindow(width, height, title, 0, 0);
		GLFWErrorCallback.createPrint(System.err).set();
		glfwMakeContextCurrent(windowID);
		glfwSwapInterval(1);
		glfwSetKeyCallback(windowID, new KeyInput());
		glfwSetCursorPosCallback(windowID, new MouseInput());
		glfwSetInputMode(windowID, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
		currentVidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		System.out.println();
		GL.createCapabilities();
		glClearColor(clearColor[0], clearColor[1], clearColor[2], clearColor[3]);
		renderer = new Renderer();
		cameras = new ArrayList<Camera>();
	}
	
	public void init() {
		// Initialization logic.
				
		run();
		cleanUp();
	}
	
	public void run() {
		long lastTime = System.currentTimeMillis();
		float dt = 0;
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		glEnable(GL_DEPTH_TEST);
		while(!KeyInput.isKeyDown(GLFW_KEY_ESCAPE) && !glfwWindowShouldClose(windowID)) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			dt = (float)(System.currentTimeMillis() - lastTime) / 1000f;//(delta time in seconds)
			glfwPollEvents();
			MouseInput.checkForStillMouse();
			switchCamera();
			renderer.render(dt, activeCamera);
			lastTime = System.currentTimeMillis();
			glfwSwapBuffers(windowID);
		}
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
		glfwFreeCallbacks(windowID);
		glfwDestroyWindow(windowID);
		glfwTerminate();
		glfwSetErrorCallback(null).free();
		renderer.cleanUp();
	}
	
	
}