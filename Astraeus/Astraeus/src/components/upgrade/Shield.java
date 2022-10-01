package components.upgrade;

import objects.entity.unit.Unit;

public class Shield extends Upgrade
{
	public static final int SIZE = 1;
	public static final int MINERAL_COST = 1;
	public static final int SHIELD = 300;
	public static final float SHIELD_REGEN_PER_SECOND = 3f;	
	public static final float DEFENSE = .02f;

	public Shield(Unit owner) 
	{
		super(owner, SIZE, MINERAL_COST);
	}
	
	public void onAddition()
	{
		super.onAddition();
		getOwner().increaseMaxShield((SHIELD));
		getOwner().increaseShieldRegen(SHIELD_REGEN_PER_SECOND);
		getOwner().decreaseDamageTakenMultiplier(DEFENSE);

	}
	


}
