package richardson.wyatt.game_entities.animation;

import org.joml.Quaterniond;
import org.joml.Vector3f;

public class JointTransform {
	private Vector3f position;
	private Quaterniond rotation;
	public JointTransform(Vector3f position, Quaterniond rotation) {
		this.position = position;
		this.rotation = rotation;
	}
	
}
