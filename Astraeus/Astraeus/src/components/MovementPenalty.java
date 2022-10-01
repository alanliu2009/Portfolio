package components;

public enum MovementPenalty 
{
	NONE, LIGHT, MEDIUM, HEAVY, STOP;
	
	public static final float NONE_USE_MOVE_PENALTY = 0f;
	public static final float LIGHT_USE_MOVE_PENALTY = .25f;
	public static final float MEDIUM_USE_MOVE_PENALTY = .50f;
	public static final float HEAVY_USE_MOVE_PENALTY = .75f;
	public static final float STOP_USE_MOVE_PENALTY = 1f;

	public static final float NONE_PASSIVE_MOVE_PENALTY = 0f;
	public static final float LIGHT_PASSIVE_MOVE_PENALTY = .05f;
	public static final float MEDIUM_PASSIVE_MOVE_PENALTY = .10f;
	public static final float HEAVY_PASSIVE_MOVE_PENALTY = .15f;
	public static final float STOP_PASSIVE_MOVE_PENALTY = .20f;
	
	
//	public static final float LIGHT_MOVE_PENALTY = .15f;
//	public static final float MEDIUM_MOVE_PENALTY = .30f;
//	public static final float HEAVY_MOVE_PENALTY = .45f;
	
	public float getUseMovementPenalty()
	{
		switch (this)
        {
            case STOP: 		return STOP_USE_MOVE_PENALTY;
            case HEAVY: 	return HEAVY_USE_MOVE_PENALTY;
            case MEDIUM: 	return MEDIUM_USE_MOVE_PENALTY;
            case LIGHT: 	return LIGHT_USE_MOVE_PENALTY;
            default: 		return NONE_USE_MOVE_PENALTY;
        }
	}
	
	public float getPassiveMovementPenalty()
	{
		switch (this)
        {
            case STOP: 		return STOP_PASSIVE_MOVE_PENALTY;
            case HEAVY: 	return HEAVY_PASSIVE_MOVE_PENALTY;
            case MEDIUM: 	return MEDIUM_PASSIVE_MOVE_PENALTY;
            case LIGHT: 	return LIGHT_PASSIVE_MOVE_PENALTY;
            default: 		return NONE_PASSIVE_MOVE_PENALTY;
        }
	}
}
