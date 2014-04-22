package com.me.geonauts.model.entities.missiles;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.me.geonauts.model.entities.enemies.AbstractEnemy;

public class YellowLaser extends Missile {	
	
	public static Vector2 SIZE = new Vector2((22f/64f), (26/60f));
	private static float SPEED = 12f;
	
	public static TextureRegion[] frames;
	
	/**
	 * 
	 * @param pos
	 * @param target
	 */
	public YellowLaser(Vector2 pos, AbstractEnemy target, int damage) {
		super(pos, SIZE, target, SPEED, damage);		
	}	
	
	/**
	 * Update the Missile's position, and check for collision with target
	 * @param delta
	 */
	@Override
	public void update(float delta) {
		// Target - Position
		Vector2 V = target.position.cpy().sub(position);
		// Unit vector = V / magnitude of V
		Vector2 unitVec = V.div(V.len());
		
		// Get difference in x and y from target to position
		float x_diff = target.position.cpy().x - position.x;
		float y_diff = target.position.cpy().y - position.y;
		
		// If the target is infront of the missile, update acceleration with unit vec
		if (x_diff > 0) {
			//if (x_diff < 1) 
				velocity = unitVec.scl(SPEED);
			// else 
				// Set acceleration to the unit vector * speed
			//	acceleration = unitVec.scl(SPEED);
			
			// Update direction based on x_diff
			if (x_diff < 0) DIRECTION = -1;
			else DIRECTION = 1;
			
			// Angle = sin^-1 (opposite / Hypotenuse)
			angle = (float) Math.toDegrees( Math.atan(y_diff / x_diff) );
		}

		
	}

	
	@Override
	public TextureRegion[] getFrames() {
		return frames;
	}
}