package richardson.wyatt.game_entities.entity;

public abstract class EntityController extends EntityComponent {
	public EntityController() {
		super(Type.CONTROLLER);
	}
	public abstract void tick(float dt);
}
				