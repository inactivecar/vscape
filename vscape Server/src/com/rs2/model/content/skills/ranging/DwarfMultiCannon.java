package com.rs2.model.content.skills.ranging;

import com.rs2.model.Entity;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.content.combat.hit.Hit;
import com.rs2.model.content.combat.hit.HitDef;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.content.combat.projectile.ProjectileDef;
import com.rs2.model.content.combat.projectile.ProjectileTrajectory;
import com.rs2.model.npcs.Npc;
import com.rs2.model.objects.GameObject;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.players.Player;
import com.rs2.model.players.container.inventory.Inventory;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;

public class DwarfMultiCannon {

	private final int BASE_ITEM = 6;
	private final int CANNON_BALL = 2;
	private final ProjectileDef CANNON_PROJECTILE = new ProjectileDef(53, ProjectileTrajectory.DART);
	private final int MAX_DAMAGE = 30;
	private final int MAX_RANGE = 5;
    private final int MIN_BUILD_DISTANCE = 10;
    
    private final static int[] IGNORE = {
    	1266,
    	1268
    };
	
	private Player player;

	public DwarfMultiCannon(Player player) {
		this.player = player;
	}
	
	private void setupCannon()
	{
		if(player.isDead())
		{
			return;
		}
		if(hasCannon())
		{
			player.getActionSender().sendMessage("You already have a Dwarf Cannon setup!");
			return;
		}
		
		final Inventory inv = player.getInventory();
		if(!inv.playerHasItem(6) || !inv.playerHasItem(8) || !inv.playerHasItem(10) || !inv.playerHasItem(12)){
			player.getActionSender().sendMessage("You don't have the required items to build a Dwarf Cannon.");
			return;
		}
		
		Position pos = player.getPosition();
		GameObject p = ObjectHandler.getInstance().getObject(pos.getX(), pos.getY(), pos.getZ());
		if (p != null || player.inBank() || player.isInDuelArea() || player.inDuelArena() || player.isWarriorsGuild() || player.inSlayerTower()) {
			player.getActionSender().sendMessage("You can't place a Dwarf Cannon here.");
			return;
		}
		if(!canBuild(pos))
		{
			player.getActionSender().sendMessage("You can't place a Dwarf Cannon this close to another Dwarf Cannon.");
			return;
		}
		setHasCannon(true);
		setCannonPos(new Position(pos.getX(),pos.getY(),pos.getZ()));
		setInMulti(player.inMulti());
		if (player.canMove(-1, 0)) {
			player.getActionSender().walkTo(-1, 0, false);
		} else {
			player.getActionSender().walkTo(1, 0, false);
        }
		player.getUpdateFlags().sendFaceToDirection(new Position(getCannonPos().getX(), getCannonPos().getY()));
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			int stage = 0;
			Position cannonPos = getCannonPos();
			@Override
			public void execute(CycleEventContainer container) {
				player.setStopPacket(true);
				player.getUpdateFlags().sendAnimation(827);
				stage++;
				setCannonStage(stage);
				GameObject cannonObj = ObjectHandler.getInstance().getObject(cannonPos.getX(), cannonPos.getY(), cannonPos.getZ());
				removeCannonObj(cannonObj);
				switch (stage) {
				    case 1:
				    	inv.removeItem(new Item(6,1));
						SetCannonObject(new GameObject(7, cannonPos.getX(), cannonPos.getY(), cannonPos.getZ(), -1, 10, -1, 99999));
						World.registerCannon(getCannonObject());
					break;
				    case 2:
				    	if(isCannonObj(cannonObj)){
				    		inv.removeItem(new Item(8,1));
				    		SetCannonObject(new GameObject(8, cannonPos.getX(), cannonPos.getY(), cannonPos.getZ(), -1, 10, -1, 99999));
							World.registerCannon(getCannonObject());
				    	}
					break;
				    case 3:
				    	if(isCannonObj(cannonObj)){
				    		inv.removeItem(new Item(10,1));
				    		SetCannonObject(new GameObject(9, cannonPos.getX(), cannonPos.getY(), cannonPos.getZ(), -1, 10, -1, 99999));
							World.registerCannon(getCannonObject());
				    	}
					break;
				    case 4:
				    	if(isCannonObj(cannonObj)){
				    		inv.removeItem(new Item(12,1));
				    		SetCannonObject(new GameObject(6, cannonPos.getX(), cannonPos.getY(), cannonPos.getZ(), -1, 10, -1, 99999));
							World.registerCannon(getCannonObject());
				    	}
				    	container.stop();
					break;
				}
			}
			@Override
			public void stop() {
				player.setStopPacket(false);
				player.getUpdateFlags().sendAnimation(-1);
				stage = 0;
			}
		}, 2);
	}
	
	public void pickupCannon(int x, int y, int z)
	{
		if(!checkCannonOwner(x, y, z)){
			return;
		}
		removeCannonObj(getCannonObject());
		switch (getCannonStage()) {
			case 1:
				player.getInventory().addItemOrBank(new Item(6,1));
				break;
			case 2:
				player.getInventory().addItemOrBank(new Item(6,1));
				player.getInventory().addItemOrBank(new Item(8,1));
				break;
			case 3:
				player.getInventory().addItemOrBank(new Item(6,1));
				player.getInventory().addItemOrBank(new Item(8,1));
				player.getInventory().addItemOrBank(new Item(10,1));
				break;
			case 4:
				player.getInventory().addItemOrBank(new Item(6,1));
				player.getInventory().addItemOrBank(new Item(8,1));
				player.getInventory().addItemOrBank(new Item(10,1));
				player.getInventory().addItemOrBank(new Item(12,1));
				break;
		}
		if(getAmmo() > 0){
			player.getInventory().addItemOrBank(new Item(CANNON_BALL, getAmmo()));
		}
		setCannonStage(0);
		setHasCannon(false);
		SetCannonObject(null);
		setCannonPos(null);
		setFiring(false);
		setAmmo(0);
		setCannonDirection(0);
	}
	
	public void pickupCannon()
	{
		pickupCannon(getCannonPos().getX(), getCannonPos().getY(), getCannonPos().getZ());
	}
	
	private void removeCannonObj(GameObject cannonObj)
	{
		if(isCannonObj(cannonObj)) {
			if (getCannonObject() == cannonObj){
				World.unregisterCannon(getCannonObject());
				ObjectHandler.getInstance().removeObject(cannonObj.getDef().getPosition().getX(), cannonObj.getDef().getPosition().getY(), cannonObj.getDef().getPosition().getZ(), cannonObj.getDef().getType());
			}
		}
	}
	
	private void processCannon(int x, int y, int z) {
		if(!checkCannonOwner(x, y, z)){
			return;
		}
		if(isFiring())
		{
			player.getActionSender().sendMessage("This Dwarf Cannon is already firing.");
			return;
		}
		if(getAmmo() <= 0)
		{
			player.getActionSender().sendMessage("This Dwarf Cannon does not have any ammo!");
			return;
		}
		setFiring(true);
		//CYCLE INSTEAD OF WORLD TICK SINCE IT'S PLAYER SPECIFIC 
		//AND WILL GET REMOVED IF THE PLAYER LEAVES
		//I DONT THINK IT REALLY MATTERS IF ITS A CYCLE EVENT
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
                if (player == null || !isFiring() || (getAmmo() <= 0) || !hasCannon()) {
        			setFiring(false);
        			setAmmo(0);
                    container.stop();
                    return;
                }
                setRotation();
                fireCannon();
			}
			@Override
			public void stop() {
			}
		}, (getInMulti() ? 2 : 4));
	}
	
	private void setRotation()
	{
		int dir = getCannonDirection();
		int animId = 515;
		switch(dir)
		{
			case 0:
				animId = 515;
			break;
			case 1:
				animId = 516;
			break;
			case 2:
				animId = 517;
			break;
			case 3:
				animId = 518;
			break;
			case 4:
				animId = 519;
			break;
			case 5:
				animId = 520;
			break;
			case 6:
				animId = 521;
			break;
			case 7:
				animId = 514;
				dir = -1;
			break;
		}
		player.getActionSender().animateObjectRadius2(getCannonPos().getX(), getCannonPos().getY(), getCannonPos().getZ(), animId);
		dir++;
		setCannonDirection(dir);
	}
	
	private void fireCannon()
	{
		if(getAmmo() <= 0)
		{
			setFiring(false);
			setAmmo(0);
			return;
		}
		Npc target = getTarget(getCannonPos());
		if(target != null && !target.isDead())
		{
			if(getInMulti())
			{
				CombatManager.attack(target, getOwner());
				hit(MAX_DAMAGE, HitType.NORMAL, getOwner(), target);
			}else{
				if(target.isAttacking() && target.getCombatingEntity() != getOwner())
					return;
				if(player.getCombatingEntity() != null && player.getCombatingEntity() != target)
					return;
				
				CombatManager.attack(target, getOwner());
				hit(MAX_DAMAGE, HitType.NORMAL, getOwner(), target);
			}
		}
	}
	// oh dear lord
	public void hit(int damage, HitType hitType, Entity attacker, Entity victim) {
		ProjectileTrajectory projectileTrajectory = CANNON_PROJECTILE.getProjectileTrajectory();
		final byte offsetX = (byte)(getCannonPos().getY() - victim.getPosition().getY());
		final byte offsetY = (byte)(getCannonPos().getX() - victim.getPosition().getX());
		int distance = Misc.getDistance(getCannonPos(), victim.getPosition());
		int slowness = projectileTrajectory.getSlowness();
		final int duration = projectileTrajectory.getDelay() + slowness + distance * 5;
		World.sendProjectile(getCannonPos(), 1, victim.getIndex() + 1, offsetX, offsetY, CANNON_PROJECTILE.getProjectileId(), projectileTrajectory.getDelay(), duration, projectileTrajectory.getStartHeight(), projectileTrajectory.getEndHeight(), projectileTrajectory.getCurve());
		HitDef hitDef = new HitDef(null, hitType, damage).setUnblockable(true).setDoBlock(false);
		hitDef.randomizeDamage();
		hitDef.setDamageDelay(calculateHitDelay(getCannonPos(), victim.getPosition(), CANNON_PROJECTILE));
		Hit hit = new Hit(attacker, victim, hitDef);
		hit.initialize();
		setAmmo(getAmmo() - 1);
		if(getAmmo() <= 0)
		{
			player.getActionSender().sendMessage("Your Dwarf Cannon has ran out of ammo!");
		}
	}
	// oh dear lord (from hit def)
	public int calculateHitDelay(Position start, Position end, ProjectileDef projectileDef) {
		int hitDelay = 0;
		if (start != null && projectileDef != null) {
			int distance = Misc.getDistance(start, end);
			ProjectileTrajectory trajectory = projectileDef.getProjectileTrajectory();
			double delay = trajectory.getDelay() + trajectory.getSlowness() + distance * 5d;
			delay = Math.ceil((delay * 12d) / 600d);
			if (distance > 1)
				delay += 1;
			hitDelay += (int) delay;
		} else
			hitDelay += 1;
		return hitDelay;
	}
	
	private Npc getTarget(Position pos)
	{
		for (Npc npc : World.getNpcs()) {
			if(npc == null)
				continue;
			if(npc.getDefinition() == null)
				continue;
			boolean IgnoredNpc = false;
			for (int id : IGNORE){
				if(id == npc.getDefinition().getId()){
					IgnoredNpc = true;
					break;
				}
			}
			if(IgnoredNpc)
				continue;
			
			if(!Misc.checkClip(pos, npc.getPosition(), false))
				continue;
			
			if(!getInMulti()){
				if(npc.isAttacking() && npc.getCombatingEntity() != getOwner())
					continue;
				if(player.getCombatingEntity() != null && player.getCombatingEntity() != npc)
					continue;
			}
			
            int cannonX = pos.getX();
            int cannonY = pos.getY();
            int npcX = npc.getPosition().getX();
            int npcY = npc.getPosition().getY();
            
            int dir = getCannonDirection();
            
            if(npc.getDefinition().isAttackable() && !npc.isDead() && npc.getPosition().getZ() == pos.getZ() && npc.getPosition().withinDistance(pos, MAX_RANGE)){
        		switch(dir)
        		{
        			case 0:
                        if (npcX < cannonX && npcY >= cannonY - 1 && npcY <= cannonY + 1)
                            return npc;
        			break;
        			case 1:
                        if (npcX <= cannonX - 1 && npcY >= cannonY + 1)
                            return npc;
        			break;
        			case 2:
                        if (npcY > cannonY && npcX >= cannonX - 1 && npcX <= cannonX + 1)
                            return npc;
        			break;
        			case 3:
                        if (npcX >= cannonX + 1 && npcY >= cannonY + 1)
                            return npc;
        			break;
        			case 4:
                        if (npcX > cannonX && npcY >= cannonY - 1 && npcY <= cannonY + 1)
                            return npc;
        			break;
        			case 5:
                        if (npcY <= cannonY - 1 && npcX >= cannonX + 1)
                            return npc;
        			break;
        			case 6:
                        if (npcY < cannonY && npcX >= cannonX - 1 && npcX <= cannonX + 1)
                            return npc;
        			break;
        			case 7:
                        if (npcX <= cannonX - 1 && npcY <= cannonY - 1)
                            return npc;
        			break;
        		}
            }
		}
		return null;
	}
	
	private void loadCannon(int item, int slot, int x, int y, int z)
	{
		if(!checkCannonOwner(x, y, z)){
			return;
		}
		if(getAmmo() >= maxAmmo)
		{
			player.getActionSender().sendMessage("This Dwarf Cannon has full ammo.");
			return;
		}
		int count = player.getInventory().getItemContainer().get(slot).getCount();
		int ammoToAdd = maxAmmo - getAmmo();
		if(ammoToAdd > count)
		{
			ammoToAdd = count;
		}
		if(ammoToAdd > 0){
			if(player.getInventory().playerHasItem(item, ammoToAdd)){
				player.getInventory().removeItemSlot(new Item(item, ammoToAdd), slot);
				player.getActionSender().sendMessage("You load the Dwarf Cannon with " + ammoToAdd + " cannonball"
					    + (ammoToAdd == 1 ? "" : "s") + ".");
				setAmmo(getAmmo() + ammoToAdd);
			}
		}
	}
	
	private boolean isCannonObj(GameObject cannonObj)
	{
		return cannonObj != null && (cannonObj.getDef().getId() == 6 || cannonObj.getDef().getId() == 7 || cannonObj.getDef().getId() == 8 || cannonObj.getDef().getId() == 9);
	}
	
	/*private boolean isCannonPosition(Position pos)
	{
		return pos.getX() == getCannonPos().getX() && pos.getY() == getCannonPos().getY() && pos.getZ() == getCannonPos().getZ();
	}*/
	
	private boolean checkCannonOwner(int x, int y, int z)
	{
		GameObject cannonObj = ObjectHandler.getInstance().getObject(x, y, z);
		if(!isCannonObj(cannonObj)) {
			return false;
		}
		if (getCannonObject() != cannonObj) {
			player.getActionSender().sendMessage("You don't own this Dwarf Cannon.");
			return false;
		}
		if(!hasCannon()){
			player.getActionSender().sendMessage("You don't own a Dwarf Cannon.");
			return false;
		}
		return true;
	}
	
	private boolean canBuild(Position pos){
		
		for (GameObject c : World.getCannons()) {
			if(c == null)
				continue;
			
			Position objPos = c.getDef().getPosition();
			if(pos.withinDistance(new Position(objPos.getX(),objPos.getY(),objPos.getZ()), MIN_BUILD_DISTANCE))
			{
				return false;
			}
		}
		return true;
	}
	
	public boolean itemFirstClick(final int item, int slot)  
	{
		switch(item)
		{
			case BASE_ITEM:
				setupCannon();
			return true;
		}
		return false;
	}
	
	public boolean itemOnObject(final int item, final int slot, final int object, int x, int y, int z) 
	{
		if(item == CANNON_BALL && object == 6)
		{
			loadCannon(CANNON_BALL, slot, x, y, z);
			return true;
		}
		return false;
	}
	
	public boolean objectFirstClick(final int object, int x, int y, int z) 
	{
		switch(object)
		{
			case 7:
			case 8:
			case 9:
				pickupCannon(x, y, z);
			return true;
			case 6:
				processCannon(x, y, z);
			return true;
		}
		return false;
	}
	
	public boolean objectSecondClick(final int object, int x, int y, int z) 
	{
		switch(object)
		{
			case 6:
				pickupCannon(x, y, z);
			return true;
		}
		return false;
	}
	
	
	public Player getOwner()
	{
		return player;
	}
	
	public void setCannonStage(int stage)
	{
		cannonStage = stage;
	}
	
	public int getCannonStage()
	{
		return cannonStage;
	}
	
	public void setHasCannon(boolean state)
	{
		hasCannon = state;
	}
	
	public boolean hasCannon()
	{
		return hasCannon;
	}
	
	public void setCannonPos(Position pos)
	{
		cannonPosition = pos;
	}
	
	public Position getCannonPos()
	{
		return cannonPosition;
	}
	
	public void SetCannonObject(GameObject obj)
	{
		cannonObject = obj;
	}
	
	public GameObject getCannonObject()
	{
		return cannonObject;
	}
	
	public void setCannonDirection(int dir)
	{
		cannonDirection = dir;
	}
	
	public int getCannonDirection()
	{
		return cannonDirection;
	}
	
	public void setInMulti(boolean state)
	{
		inMulti = state;
	}
	
	public boolean getInMulti()
	{
		return inMulti;
	}
	
	public void setFiring(boolean state)
	{
		firing = state;
	}
	
	public boolean isFiring()
	{
		return firing;
	}
	
	public void setAmmo(int amt)
	{
		curAmmo = amt;
	}
	
	public int getAmmo()
	{
		return curAmmo;
	}
	
	private int cannonStage = 0;
	private boolean hasCannon = false;
	private Position cannonPosition = null;
	private GameObject cannonObject = null;
	private int cannonDirection = 0;
	private boolean inMulti = false;
	private boolean firing = false;
	private int curAmmo = 0;
	private final int maxAmmo = 30;
}
