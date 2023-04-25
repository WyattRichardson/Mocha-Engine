package richardson.wyatt.game_entities.terrain;

import richardson.wyatt.game_entities.entity.Entity;
import richardson.wyatt.game_entities.model.Model;

public class Terrain extends Entity{
	private float offset, size;
	private int seed, amplitude;

	public Terrain(String id, int amplitude, int seed, float offset, float size) {
		super(id);
		this.amplitude = amplitude;
		this.seed = seed;
		this.size = size;
		this.offset = offset;
	}

	public float getHeight(float x, float z) {
		return 0;
	}

}
