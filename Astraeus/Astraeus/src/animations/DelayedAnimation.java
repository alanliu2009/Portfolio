package animations;

public class DelayedAnimation
{
	private Animation animation;
	private int timeLeft;
	
	public DelayedAnimation(Animation animation, int timeLeft)
	{
		this.timeLeft = timeLeft;
		this.animation = animation;
	}
		
	public void update()
	{
		if(timeLeft == 0)
		{
			AnimationManager.add(animation);
		}
		
		timeLeft--;
	}
	
	public boolean isDone()
	{
		return timeLeft < 0;
	}

}
