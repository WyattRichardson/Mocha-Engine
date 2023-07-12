package richardson.wyatt.mocha.game_entities.entity;
import org.joml.Vector3f;
public class Transform extends EntityComponent{
	
	private Vector3f position;
	private Vector3f rotation;
	private float scale;
	
	public Transform(float x, float y, float z, float rx, float ry, float rz, float scale) {
		super(Type.TRANSFORM);
		position = new Vector3f(x, y, z);
		rotation = new Vector3f(rx, ry, rz);
		this.scale = scale;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	

}
