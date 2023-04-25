package richardson.wyatt.game_entities.entity;

public abstract class EntityComponent{
	public enum Type {
		TRANSFORM, 
		MODEL, 
		CONTROLLER,
		TEXTURE,
		ANIMATION, 
		LIGHT
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
