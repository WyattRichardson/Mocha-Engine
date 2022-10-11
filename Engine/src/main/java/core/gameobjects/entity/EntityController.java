package core.gameobjects.entity;

public abstract class EntityController extends EntityComponent {
	public EntityController(Type type) {
		super(type);
	}
	public abstract void tick(float dt);
}
				