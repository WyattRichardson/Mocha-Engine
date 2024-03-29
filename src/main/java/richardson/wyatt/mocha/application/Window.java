package richardson.wyatt.mocha.application;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import richardson.wyatt.mocha.rendering.Renderer;
import richardson.wyatt.mocha.utils.KeyInput;
import richardson.wyatt.mocha.utils.MouseInput;

public final class Window {
	public static final String ENGINE_NAME = "Mocha Engine";
	public static final String ENGINE_VERSION = " 1.0 Alpha";
	public static final String WINDOW_TITLE = ENGINE_NAME + ENGINE_VERSION;
	public static final int MAX_FPS = 144;
	public static long id;
	public static GLFWVidMode currentVidMode = null;
	private static Scene activeScene;
	private static List<Scene> scenes = new ArrayList<>();
	private static int frames = 0;
	private static Renderer renderer;
	
	private Window() {}
	
	public static void init(int width, int height, float[] clearColor) {
		if(!glfwInit()) {
			System.out.println("FAILED TO INITILIZE GLFW!");
		}
		id = glfwCreateWindow(width, height, WINDOW_TITLE, 0, 0);
		GLFWErrorCallback.createPrint(System.err).set();
		glfwMakeContextCurrent(id);
		glfwSwapInterval(0); // Disable VSync
		glfwSetKeyCallback(id, new KeyInput());
		glfwSetInputMode(id, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
		currentVidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		try ( MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(id, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// Center the window
			glfwSetWindowPos(
				id,
				(vidmode.width() - pWidth.get(0)) / 2,
				(vidmode.height() - pHeight.get(0)) / 2
			);
		}
		GL.createCapabilities();
		glClearColor(clearColor[0], clearColor[1], clearColor[2], clearColor[3]);
		renderer = new Renderer();
	}
	
	public static void run() {
		loop();
		cleanUp();
	}
	
	private static void loop() {
		
		long lastTime = System.currentTimeMillis();
		float actualDt = 0;
		
		Timer fpsCounter = new Timer();
		fpsCounter.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				System.out.println("FPS: " + frames);
				frames = 0;
			}
		}, 0, 1000);
		
		
		while(!glfwWindowShouldClose(id)) {
			if(KeyInput.isKeyDown(GLFW_KEY_ESCAPE)) {
				glfwSetWindowShouldClose(id, true);
			}
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			glfwPollEvents();
			MouseInput.tick();
			actualDt = (float)(System.currentTimeMillis() - lastTime); 
			actualDt = actualDt / 1000; //Delta time in seconds.
			renderer.render(actualDt, activeScene);
			glfwSwapBuffers(id);
			lastTime = System.currentTimeMillis();
			int expectedDt = (int)(1000/MAX_FPS);
			frames += 1;
			try {
				Thread.sleep(expectedDt); // Sleep milliseconds to achieve desired FPS.
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
		fpsCounter.cancel();

	}
	protected static void addScene(Scene scene) {
		scenes.add(scene);
	}
	public static void setActiveScene(Scene scene) {
		activeScene = scenes.get(scenes.indexOf(scene));
	}
	
	private static void cleanUp() {
		glfwFreeCallbacks(id);
		glfwDestroyWindow(id);
		glfwTerminate();
		glfwSetErrorCallback(null).free();
		for(Scene s: scenes) {
			s.cleanUp();
		}
	}
}
