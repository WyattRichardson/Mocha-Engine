package richardson.wyatt.mocha.game_entities.animation;

import richardson.wyatt.mocha.game_entities.entity.EntityComponent;

public class Animation extends EntityComponent{
	private KeyFrame[] keyFrames;

	public Animation(KeyFrame[] keyFrames) {
		super(Type.ANIMATION);
		this.keyFrames = keyFrames;
	}
	
}
