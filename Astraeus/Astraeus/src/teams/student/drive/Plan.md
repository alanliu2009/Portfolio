Plan

MacroController
- Controls the entire strategy

UnitController
- Determines the best type of units to create based off of the MacroController strategy

PodController
- Assigned to a pod, determines behavior for that individual pod


Distress System?
- Pods can send distress signals to some controller to signal help

Okay so some changes I've been thinking of:

Upgrades:
- missile raiders, rangefinder
- snipers, rangefinder
- fighters, optimized algorithms
- tanks, nanobot hull

Units
I'm thinking we should move antitank to the fighter class, and add a specific "antitank" counter, mainly because with the extra upgrades, comes higher unit costs (so we'll have less units)

Production
Modify production with some boolean called "scaling" that is true when we can't get rushed by the enemy and lose (our attack value > theirs or something), and when true, we build scaling units like our bombing pod, supports, etc
Attack
If we can somehow adapt your "find best unit" code to our units that'd be good since they only target nearest, but in the sense that we don't want snipers to move in too much, and we want a priority units array list check before that (because maybe there's one unit in the middle of our pod targetting our supports we MUST get rid of)
Pandakidz2 — Today at 11:05 AM
The "scaling" imo is really important because in the beginning of the game, we'll probably want to conserve unit costs and so not use upgrades (so we can produce more units and not get rushed), but later game we'll want upgrades for a stronger attack
And if we really wanted to be dicks we could always consider adding more rangefinders (i think they stack) so our snipers literally cannot die, rn its
+17.5% max range with rangefinder and sniper boon, along with extra speed from scout boon so they're looking hella hard to catch