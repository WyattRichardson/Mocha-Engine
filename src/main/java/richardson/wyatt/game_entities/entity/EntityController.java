package richardson.wyatt.game_entities.entity;

public abstract class EntityController extends EntityComponent {
	public EntityController(Type type) {
		super(type);
	}
	public abstract void tick(float dt);
}
				