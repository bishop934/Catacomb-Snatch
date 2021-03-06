package com.mojang.mojam.entity.mob;

import com.mojang.mojam.network.TurnSynchronizer;
import com.mojang.mojam.screen.Art;
import com.mojang.mojam.screen.Bitmap;

public class Scarab extends Mob {
	public int facing;
	public int walkTime;
	public int stepTime;

	public Scarab(double x, double y, int localTeam) {
		super(x, y, Team.Neutral, localTeam);
		setPos(x, y);
		setStartHealth(5);
		dir = TurnSynchronizer.synchedRandom.nextDouble() * Math.PI * 2;
		minimapColor = 0xffff0000;
		yOffs = 10;
		facing = TurnSynchronizer.synchedRandom.nextInt(4);
		deathPoints = 4;
		strength = 2;
	}

	public void tick() {
		super.tick();
		if (freezeTime > 0)
			return;

		double speed = 0.7;
		if (facing == 0)
			yd += speed;
		if (facing == 1)
			xd -= speed;
		if (facing == 2)
			yd -= speed;
		if (facing == 3)
			xd += speed;
		walkTime++;

		if (walkTime / 12 % 4 != 0) {
			stepTime++;
			if (!move(xd, yd)
					|| (walkTime > 10 && TurnSynchronizer.synchedRandom
							.nextInt(200) == 0)) {
				facing = TurnSynchronizer.synchedRandom.nextInt(4);
				walkTime = 0;
			}
		}
		xd *= 0.2;
		yd *= 0.2;
	}

	public void die() {
		super.die();
	}

	public Bitmap getSprite() {
		return Art.scarab[((stepTime / 6) & 3)][(facing + 3) & 3];
	}

	@Override
	public String getDeathSound() {
		return "/sound/Enemy Death 1.wav";
	}
}
