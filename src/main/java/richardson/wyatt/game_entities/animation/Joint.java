package richardson.wyatt.game_entities.animation;

import java.util.List;

import richardson.wyatt.game_entities.entity.Transform;

public class Joint {
	private final String name;
	private final List<Joint> children;
	private int renderIndex;
	private Transform animatedMSTransform;


	
	public Joint(List<Joint> children, int renderIndex, Transform transform, String name) {
		this.children = children;
		this.renderIndex = renderIndex;
		animatedMSTransform = transform;
		this.name = name;
	}
	
}
