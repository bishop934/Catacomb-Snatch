package com.mojang.mojam.entity.building;

import java.util.Set;

import com.mojang.mojam.MojamComponent;
import com.mojang.mojam.entity.Entity;
import com.mojang.mojam.entity.animation.LargeBombExplodeAnimation;
import com.mojang.mojam.entity.mob.Mob;
import com.mojang.mojam.entity.mob.Team;
import com.mojang.mojam.screen.Art;
import com.mojang.mojam.screen.Bitmap;

public class Bomb extends Building {

	public static final double BOMB_DISTANCE = 50;
	private boolean hit = false;

	public Bomb(double x, double y, int localTeam) {
		super(x, y, Team.Neutral,localTeam);
		setStartHealth(8);
		yOffs = 2;
		setSize(7, 7);
		doShowHealthBar = false;
	}

	public void die() {
		level.addEntity(new LargeBombExplodeAnimation(pos.x, pos.y));
		MojamComponent.soundPlayer.playSound("/sound/Explosion 2.wav",
				(float) pos.x, (float) pos.y);

		Set<Entity> entities = level.getEntities(pos.x - BOMB_DISTANCE, pos.y
				- BOMB_DISTANCE, pos.x + BOMB_DISTANCE, pos.y + BOMB_DISTANCE,
				Mob.class);
		for (Entity e : entities) {
			double distSqr = pos.distSqr(e.pos);
			if (distSqr < (BOMB_DISTANCE * BOMB_DISTANCE)) {
				((Mob) e).hurt(this, 5);
			}
		}
	}

	@Override
	public boolean isNotFriendOf(Mob m) {
	    return true;
	}

	@Override
	public void onPickup(Mob mob) {
	    super.onPickup(mob);
	    yOffs = 4;
	}

	@Override
	public void onDrop() {
	    super.onDrop();
	    yOffs = 2;
	}

	public void tick() {
	    if (hit) {
	        if (freezeTime <= 0) {
	            hurtTime = 40;
	            freezeTime = 5;
	            health-=2;
	        }
	        if (health < 0) health = 0;
	        }

	    if (health <= 0) {
			if (--hurtTime <= 0) {
				die();
				remove();
				if (isCarried()) carriedBy.drop();
			}
			return;
		}

		super.tick();
		if (--freezeTime > 0)
			return;

	}

	public Bitmap getSprite() {
		return Art.bomb;
	}

	@Override
	public void hurt(Entity source, float damage) {
	}

	public void hit() {
		hit = true;;
	}
}
