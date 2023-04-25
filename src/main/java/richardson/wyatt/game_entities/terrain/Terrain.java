package richardson.wyatt.game_entities.terrain;

import richardson.wyatt.game_entities.entity.Entity;
import richardson.wyatt.game_entities.model.Model;

public class Terrain extends Entity{
	private float offset, size;
	private int seed, amplitude;

	public Terrain(String id, int amplitude, int seed, float size) {
		super(id);
		this.amplitude = amplitude;
		this.seed = seed;
		this.size = size;
	}

	
	
	public float getOffset() {
		return offset;
	}



	public void setOffset(float offset) {
		this.offset = offset;
	}



	public float getSize() {
		return size;
	}



	public void setSize(float size) {
		this.size = size;
	}



	public int getSeed() {
		return seed;
	}



	public void setSeed(int seed) {
		this.seed = seed;
	}



	public int getAmplitude() {
		return amplitude;
	}



	public void setAmplitude(int amplitude) {
		this.amplitude = amplitude;
	}



	public float getHeight(float x, float z) {
		return 0;
	}

}
