package richardson.wyatt.application;
import richardson.wyatt.game_entities.entity.Entity;
public class Camera extends Entity{
	private Entity anchorEntity;
	private boolean hasAnchor = false;
	private float FOV;
	private float distanceFromAnchor;

	public Camera(String entityId) {
		super(entityId);
	}
	public boolean getHasAnchor() {
		return this.hasAnchor;
	}

	public void setHasAnchor(boolean hasAnchor) {
		this.hasAnchor = hasAnchor;
	}

	public float getFOV() {
		return this.FOV;
	}

	public void setFOV(float FOV) {
		this.FOV = FOV;
	}

	public float getDistanceFromAnchor() {
		return this.distanceFromAnchor;
	}

	public void setDistanceFromAnchor(float distanceFromAnchor) {
		this.distanceFromAnchor = distanceFromAnchor;
	}


	public void setAnchorEntity(Entity e){
		anchorEntity = e;
	}
	public Entity getAnchorEntity() {
		return anchorEntity;
	}
}
