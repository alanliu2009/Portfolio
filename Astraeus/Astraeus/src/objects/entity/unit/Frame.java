package objects.entity.unit;

import engine.Values;

public enum Frame 
{	
	LIGHT, MEDIUM, HEAVY, ASSAULT, COLOSSAL;
		
	public final static int BASE_FRAME_COST = 1;
	
	public final static int LIGHT_MINERAL_COST = BASE_FRAME_COST * 2;	
	public final static float LIGHT_SPEED = 75 * Values.SPEED;		
	public final static float LIGHT_ACCELERATION = 70 * 1f * Values.ACC;		
	public final static int LIGHT_STRUCTURE = 300;	
	public final static int LIGHT_IMAGE_SIZE = 24;		

	public final static int MEDIUM_MINERAL_COST = BASE_FRAME_COST * 3;		
	public final static float MEDIUM_SPEED = 60 * Values.SPEED;		
	public final static float MEDIUM_ACCELERATION = 40 * Values.ACC;			
	public final static int MEDIUM_STRUCTURE = 450;		
	public final static int MEDIUM_IMAGE_SIZE = 36;		
	
	public final static int HEAVY_MINERAL_COST = BASE_FRAME_COST * 4;	
	public final static float HEAVY_SPEED = 45 * Values.SPEED;		
	public final static float HEAVY_ACCELERATION = 21 * Values.ACC;				
	public final static int HEAVY_STRUCTURE = 600;	
	public final static int HEAVY_IMAGE_SIZE = 48;		
	
	public final static int ASSAULT_MINERAL_COST = BASE_FRAME_COST * 5;	
	public final static float ASSAULT_SPEED = 30 * Values.SPEED;		
	public final static float ASSAULT_ACCELERATION = 10 * Values.ACC;			
	public final static int ASSAULT_STRUCTURE = 750;		
	public final static int ASSAULT_IMAGE_SIZE = 72;		

	public final static int COLOSSAL_MINERAL_COST = BASE_FRAME_COST * 5;	
	public final static float COLOSSAL_SPEED = 1.2f * Values.SPEED;		
	public final static float COLOSSAL_ACCELERATION = .20f * Values.ACC;		
	public final static int COLOSSAL_STRUCTURE = 4000;		
	public final static int COLOSSAL_IMAGE_SIZE = 256;		
	
	public int getScrapAmountOnDeath()
	{
		return getCost() / 4;
	}
	
	public int getCost()
	{
		return getMineralCost();
	}
	
	public int getMineralCost()
	{
		switch (this)
        {
            case LIGHT: 	return LIGHT_MINERAL_COST;
            case MEDIUM: 	return MEDIUM_MINERAL_COST;
            case HEAVY: 	return HEAVY_MINERAL_COST;
            case ASSAULT: 	return ASSAULT_MINERAL_COST;
            case COLOSSAL: 	return COLOSSAL_MINERAL_COST;
            default: 		return Integer.MAX_VALUE;
        }
	}

	public int getComponentSlots()
	{
		switch (this)
        {
            case LIGHT: 	return 2;
            case MEDIUM: 	return 4;
            case HEAVY: 	return 6;
            case ASSAULT: 	return 8;
            case COLOSSAL: 	return 16;
            default: 		return 0;
        }
	}
	
	public int getStructure()
	{
		switch (this)
        {
            case LIGHT: 	return LIGHT_STRUCTURE;
            case MEDIUM: 	return MEDIUM_STRUCTURE;
            case HEAVY: 	return HEAVY_STRUCTURE;
            case ASSAULT: 	return ASSAULT_STRUCTURE;
            case COLOSSAL: 	return COLOSSAL_STRUCTURE;
            default: 		return Integer.MAX_VALUE;
        }
	}
	
	public float getSpeed()
	{
		switch (this)
        {
            case LIGHT: 	return LIGHT_SPEED;
            case MEDIUM: 	return MEDIUM_SPEED;
            case HEAVY: 	return HEAVY_SPEED;
            case ASSAULT: 	return ASSAULT_SPEED;
            case COLOSSAL: 	return COLOSSAL_SPEED;
            default: 		return Integer.MAX_VALUE;
        }
	}
	
	public float getAcc()
	{
		switch (this)
        {
            case LIGHT: 	return LIGHT_ACCELERATION;
            case MEDIUM:	return MEDIUM_ACCELERATION;
            case HEAVY: 	return HEAVY_ACCELERATION;
            case ASSAULT: 	return ASSAULT_ACCELERATION;
            case COLOSSAL: 	return COLOSSAL_ACCELERATION;
            default: 		return 0;
        }
	}
	
	public int getImageSize()
	{
		switch (this)
        {
            case LIGHT: 	return LIGHT_IMAGE_SIZE;
            case MEDIUM: 	return MEDIUM_IMAGE_SIZE;
            case HEAVY: 	return HEAVY_IMAGE_SIZE;
            case ASSAULT: 	return ASSAULT_IMAGE_SIZE;
            case COLOSSAL: 	return COLOSSAL_IMAGE_SIZE;
            default: 		return 0;
        }
	}
	
	public String toString()
	{
		switch (this)
        {
            case LIGHT: 	return "Light";
            case MEDIUM: 	return "Medium";
            case HEAVY: 	return "Heavy";
            case ASSAULT: 	return "Assault";
            case COLOSSAL: 	return "Colossal";
            default: 		return "?";
        }
	}
	
	

}
