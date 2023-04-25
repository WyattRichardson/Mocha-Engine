package richardson.wyatt.game_entities.entity;

import java.util.ArrayList;
import java.util.List;

public abstract class EntityController extends EntityComponent {
	public List<Entity> entities = new ArrayList<Entity>();
	public EntityController() {
		super(Type.CONTROLLER);
	}
	public EntityController(Entity...entity) {
		super(Type.CONTROLLER);
		for(Entity entitiy: entity) {
			this.entities.add(entitiy);
		}
	}
	public abstract void tick(float dt);
}
				