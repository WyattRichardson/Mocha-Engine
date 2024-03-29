package richardson.wyatt.mocha.game_entities.entity;
import java.util.ArrayList;
import java.util.List;

import richardson.wyatt.mocha.game_entities.entity.EntityComponent.Type;

public class Entity {
	public static final int TEXTURE_INDEX = 0;

	private List<EntityComponent> components;
	private String id;
	private boolean hasController = false;
	private boolean hasModel = false;
	private boolean hasTransform = false;
	private boolean hasLight = false;
	private boolean hasAnimation = false;
	private boolean hasTexture = false;

	public Entity(String id) {
		components = new ArrayList<EntityComponent>();
		this.id = id;
	}
	public List<EntityComponent> getComponentsByType(Type type) {
		List<EntityComponent> batch = new ArrayList<EntityComponent>();
		for(EntityComponent component: components) {
			if(component.getType() == type) {
				batch.add(component);
			}
		}
		return batch;
	}
	public EntityComponent getComponentByType(Type type) {
 		for(EntityComponent component: components) {
			if(component.getType() == type) {
				return component;
			}
		}
		System.err.println("COULD NOT FIND COMPONENT OF TYPE: " + type + " IN ENTITY: " + id);
		throw new NullPointerException(); //If end of list is reached and no component is found with matching type, throw nullpointer to crash system.
	}
	public void addComponent(EntityComponent component){
		switch(component.getType()){
			case MODEL:
				hasModel = true;
				break;
			case CONTROLLER:
				hasController = true;
				break; 
			case TRANSFORM:
				hasTransform = true;
				break;
			case LIGHT:
				hasLight = true;
				break;
			case ANIMATION:
				hasAnimation = true;
				break;
			case TEXTURE:
				hasTexture = true;
		}
		components.add(component);
	}
	public boolean hasController(){
		return hasController;
	}
	public boolean hasLight() {
		return hasLight;
	}
	public boolean hasModel(){
		return hasModel;
	}
	public boolean hasTransform(){
		return hasTransform;
	}
	public boolean hasAnimation() {
		return hasAnimation;
	}
	public boolean hasTexture() {
		return hasTexture;
	}
	public String getId(){
		return this.id;
	}
	
	
}
