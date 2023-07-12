package richardson.wyatt.mocha.game_entities.terrain;

import richardson.wyatt.mocha.game_entities.entity.Entity;
import richardson.wyatt.mocha.game_entities.model.Model;
import richardson.wyatt.mocha.utils.Math;

public class Terrain extends Entity{
	private float size;
	private int offset, seed, amplitude, width;
	private float[] heights;
	public Terrain(String id, int amplitude, int seed, float size) {
		super(id);
		this.amplitude = amplitude;
		this.seed = seed;
		this.size = size;
	}

	public float getHeight(float x, float z) {
		float fracX = (x % offset);
		float fracZ = (z % offset);
		int gridX = (int) (x - fracX);
		int gridZ = (int) (z - fracZ);
		int index = (-gridZ/offset) * width + (gridX/offset);
		float bottomLeftHeight = heights[index];
		float bottomRightHeight = heights[index+1];
		float topLeftHeight = heights[index + width];
		float topRightHeight = heights[index + width + 1];
		float top = Math.linearInterpolate(topLeftHeight, topRightHeight, fracX/offset);
		float bottom = Math.linearInterpolate(bottomLeftHeight, bottomRightHeight, fracX/offset);
		float middle = Math.linearInterpolate(bottom, top, -fracZ/offset);
		return middle;
	}
	
	
	
	public void setHeights(float[] heights) {
		this.heights = heights;
	}
	
	public float getOffset() {
		return offset;
	}

	public void setWidth(int width) {
		this.width = width;
	}


	public void setOffset(int offset) {
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





}
