package engine;

import territory.Territory;
import territory.basic.Battlefield;

public class Settings 
{
	/*************************User Settings *************************/
	
	// Gameplay Settings
	public static int gameSpeed = 2;							// Scale of 1 to 20
	public static boolean penalizeLatency = false;				// Toggles latency penalty
	
	// Map Settings
	public static Class<? extends Territory> lockedMap = Battlefield.class;	// Selected map as a class.  Ex:  "ShatteredSpace.class"
	public static boolean includeBasicMaps = true;				// Include basic maps in the map pool
	public static boolean includeTrialMaps = true;				// Include the Olympian Trial maps in the map pool
	public static boolean includeTournamentMaps = true;			// Include official tournament maps in the map pool
	public static boolean forceTrialMaps = true;				// When a god is on the right, battle in their home map

	// Preset Overrides
	public static GraphicsMode graphicsMode = GraphicsMode.CUSTOM;	// CUSTOM, FAST, or FANCY
	public static InfoMode infoMode = InfoMode.CUSTOM;				// CUSTOM, DETAILED, BALANCED, or CINEMATIC
	
	// Audio
	public static boolean sfxOn = true; 					// Enable sound effects
	public static boolean musicOn = true;					// Enables music.  
	public static float musicVolume = 0.5f;					// Music volume
	public static float soundVolume = 0.5f;					// Sound Volume
	
	// Information 
	public static boolean showPlayerOneInfo = true;				// Turns on left player's diagnostic graphics by default
	public static boolean showPlayerTwoInfo = true;				// Turns on right player's diagnostic graphics by default
	
	public static boolean showNodeHealthbars = true;			// Node Health bars are always visible
	public static boolean smartNodeHealthbars = true;			// Node health bars only visible for damaged nodes
	public static boolean showUnitHealthbars = true;			// Unit Health bars are always visible
	public static boolean smartUnitHealthbars = false;			// Unit health bars only visible for damaged units
	public static boolean showCapacitybars = true;				// Capacity bars are visible only for units with a collector
	public static boolean showUsebars = true;					// Use bars are visible
	public static boolean smartUsebars = false;					// Use bars only visible for units with long activation
	public static boolean showMapBorders = true;				// Map Borders are clearly marked
	public static boolean showGridlines = false;				// Shows gridlines
	public static boolean showUnitList = true;					// Shows unit % and # in the bottom corners
	
	// Graphics and Animation
	public static boolean gradiantHealthbars = true;			// Health bars are drawn as a gradient effect
	public static boolean showBackground = true;				// Shows background skybox image
	public static boolean showAmbient = true;					// Shows ambient background objects 
	public static boolean showAmbientMovement = false;			// Makes ambient background move
	public static boolean showAnimations = true;				// Shows cosmetic animations (smoke, mining, mineral details)
	public static float backgroundBrightness = .4f;//.4f;		// Sets brightness of background image.  (Recommended at .6f in lab)
	public static CameraMode cameraStart = CameraMode.LEFT;		// Starts camera on LEFT, RIGHT, or RANDOM
	
	// Controls
	public static boolean invertCameraDrag = true;
	
	// Engine Debugging Tools
	public static boolean dbgShowWeaponAim = false;				// Shows a thin line when a weapon is activated toward target
	public static boolean dbgShowDamageOnHit = false;			// Uses unit message system to display damage taken on hit
	public static boolean dbgShowDodge = false;					// Uses unit message system to display when it dodges a shot

	public static boolean dbgShowUnitConditions = true;			// Uses unit message system to display all active conditions on units
	public static boolean dbgShowUnitCargo = false;				// Uses unit message system to display min and max cargo

	/************************* Preset Management *************************/
	
	public static void loadPresets()
	{	
		if(graphicsMode == GraphicsMode.FANCY)
		{
			setFancyGraphics();
		}
		else if(graphicsMode == GraphicsMode.FAST)
		{
			setFastGraphics();
		}		
		
		if(infoMode == InfoMode.HIGH)
		{
			setHighInfo();
		}
		else if(infoMode == InfoMode.MEDIUM)
		{
			setMediumInfo();
		}
		else if(infoMode == InfoMode.LOW)
		{
			setLowInfo();
		}
	}
		
	public static void setFancyGraphics()
	{
		gradiantHealthbars = true;
		showBackground = true;		
		showAmbient = true;
		showAmbientMovement = true;
		showAnimations = true;
	}
		
	public static void setFastGraphics()
	{
		gradiantHealthbars = false;
		showBackground = false;
		showAmbient = false;
		showAmbientMovement = false;
		showAnimations = false;
	}
	
	public static void setHighInfo()
	{
		showPlayerOneInfo = true;	
		showPlayerTwoInfo = true;	
		showUnitHealthbars = true;	
		smartUnitHealthbars = false;
		showNodeHealthbars = true;	
		smartNodeHealthbars = false;
		showCapacitybars = true;
		showUsebars = true;
		smartUsebars = false;
		showMapBorders = true;		
		showGridlines = true;
		showPlayerOneInfo = true;
		showPlayerTwoInfo = true;
		showUnitList = true;
		
	}
	
	public static void setMediumInfo()
	{
		showPlayerOneInfo = false;	
		showPlayerTwoInfo = false;	
		showUnitHealthbars = true;	
		smartUnitHealthbars = false;	
		showNodeHealthbars = true;	
		smartNodeHealthbars = true;
		showCapacitybars = true;
		showUsebars = true;
		smartUsebars = false;
		showMapBorders = true;	
		showGridlines = false;
		showPlayerOneInfo = false;
		showPlayerTwoInfo = false;
		showUnitList = true;
	}
		
	public static void setLowInfo()
	{
		showPlayerOneInfo = false;	
		showPlayerTwoInfo = false;	
		showUnitHealthbars = true;	
		smartUnitHealthbars = true;	
		showNodeHealthbars = false;	
		smartNodeHealthbars = false;
		showUsebars = false;
		smartUsebars = false;
		showMapBorders = false;	
		showGridlines = false;
		showPlayerOneInfo = false;
		showPlayerTwoInfo = false;
		showUnitList = false;

	}
	
	public enum GraphicsMode
	{
		FAST, FANCY, CUSTOM;
	}
	
	public enum InfoMode
	{
		LOW, MEDIUM, HIGH, CUSTOM;
	}
	
	public enum CameraMode
	{
		LEFT, RIGHT, RANDOM;
	}
}


