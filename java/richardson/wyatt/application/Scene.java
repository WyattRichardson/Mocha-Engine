package richardson.wyatt.application;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL30.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import richardson.wyatt.game_entities.entity.Camera;
import richardson.wyatt.game_entities.entity.Entity;
import richardson.wyatt.game_entities.entity.EntityComponent.Type;
import richardson.wyatt.game_entities.model.Model;
import richardson.wyatt.game_entities.terrain.Terrain;


public final class Scene { 
	public Camera activeCamera;

	private HashMap<Model, ArrayList<Entity>> entitiesWithModels;
	private ArrayList<Entity> entitiesWithoutModels;
	
	private ArrayList<Terrain> terrains;
	private ArrayList<Camera> cameras;
	private String id;
	private boolean hasTerrains = false;
	
	public Scene(String id) { 
		this.id = id;
		Window.addScene(this);
		cameras = new ArrayList<Camera>();
		entitiesWithModels = new HashMap<>();
		entitiesWithoutModels = new ArrayList<>();
		terrains = new ArrayList<Terrain>();
	}
	
	public void setActiveCamera(Camera c) {
		if(cameras.indexOf(c) != -1) {
			activeCamera = c;
		}else {
			throw new IllegalStateException("Camera passed to be set active is not in scene: " + id);
		}
	}
	
	public ArrayList<Entity> getEntitiesWithoutModels(){
		return this.entitiesWithoutModels;
	}
	public HashMap<Model, ArrayList<Entity>> getEntitiesWithModels(){
		return this.entitiesWithModels;
	}
	public ArrayList<Terrain> getTerrains(){
		return this.terrains;
	}
	
	public void addEntity(Entity entity) {
		if(entity.getClass().getSimpleName().equals("Camera")) {
			cameras.add((Camera)entity);
		}else if(entity.getClass().getSimpleName().equals("Terrain")) {
			terrains.add((Terrain)entity);
			hasTerrains = true;
		}else {
			if(entity.hasModel()){
				Model model = (Model) entity.getComponentsByType(Type.MODEL).get(0);
				if(!entitiesWithModels.containsKey(model)){
					entitiesWithModels.put(model, new ArrayList<Entity>());
				}
				entitiesWithModels.get(model).add(entity);
			}else {
				entitiesWithoutModels.add(entity);

			}
		}
	}
	public boolean hasTerrains() {
		return this.hasTerrains;
	}
	public void cleanUp() {
		for(Model m: entitiesWithModels.keySet()) {
			m.cleanUp();
		}
	}
	
	
}