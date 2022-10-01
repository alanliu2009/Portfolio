package components.upgrade;

import objects.entity.unit.Unit;

public class TitanCore extends Upgrade
{
	public static final int SIZE = 10;
	public static final int MINERAL_COST = 0;
	public static final float USE_TIME_SCALAR = .33f;
	public static final float COOLDOWN_SCALAR = .33f;
	public static final int PLATING = 4000;
	public static final float ACCURACY_SCALAR = 1.35f;
	public static final float MAX_RANGE_SCALAR = 1;
	public static final int SHIELD = 4000;
	public static final float SHIELD_REGEN_PER_SECOND = 15f;	
	public static final float BLOCK = .4f;	

	public TitanCore(Unit owner) 
	{
		super(owner, SIZE, MINERAL_COST);
	}
	
	public void onAddition()
	{
		super.onAddition();
		
		getOwner().decreaseDamageTakenMultiplier(BLOCK);
		getOwner().increaseMaxPlating(PLATING);		
		getOwner().increaseMaxShield(SHIELD);
		getOwner().increaseShieldRegen(SHIELD_REGEN_PER_SECOND);
		getOwner().applyUseMultiplier(USE_TIME_SCALAR);
		getOwner().applyCooldownTimeMultiplier(COOLDOWN_SCALAR);
		getOwner().applyAccuracyMultiplier((ACCURACY_SCALAR));
		getOwner().applyMaxRangeMultiplier(MAX_RANGE_SCALAR);

	}

}
