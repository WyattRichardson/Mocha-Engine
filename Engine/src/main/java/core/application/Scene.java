package core.application;

import static org.lwjgl.glfw.GLFW.*;

import static org.lwjgl.opengl.GL30.*;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.*;

import core.gameobjects.entity.Entity;
import core.rendering.*;
import core.utils.KeyInput;

public final class Scene { 
	
	static { //Initialize GLFW statically to allow for multiple Scenes
		
		if(!glfwInit()) {
			System.out.println("FAILED TO INITILIZE GLFW!");
		}
		
	
	}
	
	public long windowID;
	
	public EntityRenderer entityRenderer;
	
	public TerrainRenderer terrainRenderer;
		
	public static GLFWVidMode currentVidMode = null;
	
	
	public Scene(int width, int height, String title, float[] clearColor) { 
		windowID = glfwCreateWindow(width, height, title, 0, 0);
		GLFWErrorCallback.createPrint(System.err).set();
		glfwMakeContextCurrent(windowID);
		glfwSwapInterval(1);
		glfwSetKeyCallback(windowID, new KeyInput());
		currentVidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		System.out.println();
		GL.createCapabilities();
		glClearColor(clearColor[0], clearColor[1], clearColor[2], clearColor[3]);
		entityRenderer = new EntityRenderer();
	}
	
	public void init() {
		//Initialization logic
				
		run();
		glfwFreeCallbacks(windowID);
		glfwDestroyWindow(windowID);
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}
	
	public void run() {
		long lastTime = System.currentTimeMillis();
		float dt = 0;
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		glEnable(GL_DEPTH_TEST);
		while(!glfwWindowShouldClose(windowID)) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			dt = (float)(System.currentTimeMillis() - lastTime) / 1000f;//(delta time in seconds)
			glfwPollEvents();
			
			entityRenderer.render(dt);
			
			lastTime = System.currentTimeMillis();
			glfwSwapBuffers(windowID);
		}
	}
	
	public void addEntity(Entity entity) {
		entityRenderer.addEntity(entity);
	}
	public void cleanUp() {
		entityRenderer.cleanUp();
	}
	
	
}