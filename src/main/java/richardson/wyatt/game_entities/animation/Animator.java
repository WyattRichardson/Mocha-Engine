package richardson.wyatt.game_entities.animation;

public class Animator {
	private Animation currentAnimation;
	private float animationTime;
	
	
	
	
	public Animation getCurrentAnimation() {
		return currentAnimation;
	}
	public void setCurrentAnimation(Animation currentAnimation) {
		this.currentAnimation = currentAnimation;
	}
	public float getAnimationTime() {
		return animationTime;
	}
	public void setAnimationTime(float animationTime) {
		this.animationTime = animationTime;
	}
	
}
