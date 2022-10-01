package player;

public enum Boon 
{
	// Offensive
	BLASTER, LANCER, GUNNER, BRAWLER, SNIPER,
	
	// Defensive
	GUARDIAN, ARMORER, ENGINEER, MECHANIC, PILOT,
	
	// Support
	MINER, TRADER, SCOUT, EXPERT;
	
	public static final float EXPERT_USE_AND_COOLDOWN_MULTIPLIER = 0.80f;
	
	public static final float MINER_MINING_SPEED_MULTIPLIER = .10f;
	public static final float SCOUT_SPEED_BONUS = 0.06f;
	public static final float SNIPER_MAX_RANGE_MULTIPLIER_BONUS = 1.075f;
	public static final float SNIPER_MIN_RANGE_MULTIPLIER_PENALTY = 2.0f;
	public static final float MECHANIC_REPAIR_EFFICIENCY_BONUS = .30f;		
	public static final float TRADER_CAPACITY_MULTIPLIER = 1.2f;
	public static final float TRADER_SPEED_BONUS = .03f;
	public static final float PILOT_DODGE_MULTIPLIER = 1.15f;

	public static final float DEFENSE_MULTIPLIER = 1.05f;
	public static final float OFFENSIVE_DAMAGE_BONUS = .05f;
		
	public String toString()
	{
		switch(this)
		{
			// Offensive
			case GUNNER: 		return "Gunner";
			case LANCER: 		return "Lancer";
			case BLASTER: 		return "Blaster";
			case BRAWLER: 		return "Brawler";
			case SNIPER: 		return "Sniper";
		
			// Defensive
			case GUARDIAN: 		return "Guardian";
			case ARMORER: 		return "Armorer";
			case ENGINEER: 		return "Engineer";
			case PILOT: 		return "Pilot";

			// Support
			case MINER: 		return "Miner";
			case TRADER: 		return "Trader";
			case SCOUT: 		return "Scout";			
			case EXPERT: 		return "Expert";
			case MECHANIC: 		return "Mechanic";

			default: 			return "None";
		}
	}
	
}
