package core.gameobjects.entity;

public abstract class EntityComponent {

	public static final int TYPE_LIGHT = 1;
	public static final int TYPE_TRANSFORM = 2;
	public static final int TYPE_MODEL = 3;
	public static final int TYPE_CAMERA = 4;
	
	protected int type = 0;
	public EntityComponent() {
		setComponentType();
	}
	
	public abstract void setComponentType();

}
