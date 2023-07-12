package richardson.wyatt.mocha.game_entities.entity;

import java.util.ArrayList;
import java.util.List;

public abstract class EntityController extends EntityComponent {
	public List<Entity> entities = new ArrayList<Entity>();
	public EntityController() {
		super(Type.CONTROLLER);
	}
	/**
	 * 
	 * @param entities is any number of external entities passed to this class to allow this controller to interact with other entities.
	 */
	public EntityController(Entity...entities) {
		super(Type.CONTROLLER);
		for(Entity entity: entities) {
			this.entities.add(entity);
		}
	}
	public abstract void tick(float dt);
}
				