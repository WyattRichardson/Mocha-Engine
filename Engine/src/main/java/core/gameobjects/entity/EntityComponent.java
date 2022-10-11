package core.gameobjects.entity;

public abstract class EntityComponent{
	public enum Type {
		TRANSFORM, 
		MODEL, 
		CONTROLLER
	};
	private Type type = null;
	public EntityComponent(Type type) {
		this.type = type;
	}
	public Type getType() {
		return this.type;
	}
	public void setType(Type type) {
		this.type = type;
	}
}
