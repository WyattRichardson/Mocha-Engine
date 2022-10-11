package core.gameobjects.camera;

import org.joml.Vector3f;

import core.gameobjects.entity.Entity;
import core.gameobjects.entity.Transform;

public class Camera {
	
	public Transform transform;
	private Entity anchorEntity;
	private CameraController controller = null;
	
	public boolean isActive = false;
	public boolean hasAnchor;
	public float FOV;
	public float distanceFromAnchor;

	public Camera(Entity anchorEntity) {
		this.anchorEntity = anchorEntity;
	}
	public Camera(Vector3f position, Vector3f rotation) {
		this.transform.position = position;
		this.transform.rotation = rotation;
		
	}
	public Vector3f getPosition() {
		return transform.position;
	}
	public Vector3f getRotation() {
		return transform.rotation;
	}
	public void setController(CameraController controller) {
		this.controller = controller;
	}
	public Entity getAnchor() {
		return anchorEntity;
	}

}
