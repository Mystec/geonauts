package com.me.geonauts.model.entities.enemies;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.me.geonauts.model.entities.enemies.AbstractEnemy.State;
import com.me.geonauts.model.entities.heroes.Hero;

public class FireMob extends AbstractEnemy {

	
	// Rotation stuff
	public float ROTATION_SPEED = 2f; // angles per second??
	private float PITCH;
	

	public static final float SPEED = 0.4f;	// unit per second
	public final int DIRECTION = -1;
	private static Vector2 SIZE = new Vector2((74/64)/1.5f, (71/60)/1.5f);
	private static int health = 50;
	private static int damage = 10;
	
	private float lastStateTime = 0;
	private static final float STATE_CHANGE_TIME = 1;
	
	// Textures
	public static TextureRegion[] enemyFrames;
	
	// 
	protected Hero hero;
	
	/**
	 * 
	 * @param pos
	 * @param SIZE
	 */
	public FireMob(Vector2 pos, Hero hero) {
		super(pos, SIZE, SPEED, health, damage);
		PITCH = rand.nextInt(25-15) + 15;
		ROTATION_SPEED = ROTATION_SPEED * (float)rand.nextDouble();
	
		this.hero = hero;
		//System.out.println(ROTATION_SPEED);

	}


	@Override
	public void update(float delta) {		
		stateTime += delta;

		// Update state based on hero position
		// Hero is above mob
		if (position.y > hero.position.y ) {
			state = State.FALLING;
		} else if (position.y < hero.position.y ) {
			state = State.FLYING;
		}
	
		// Update angle based State
		if (state == State.FLYING) 
			angle -= ROTATION_SPEED;
		else if ( state == State.FALLING || state == State.DYING)
			angle += ROTATION_SPEED;
			
		// Make sure angle isn't too big.
		if (angle > PITCH) angle = PITCH;
		else if (angle < -PITCH) angle = -PITCH;
		
		// Set acceleration
		acceleration.x = SPEED * DIRECTION;
		acceleration.y = (float) (SPEED * angle) * DIRECTION;
		
	}

	@Override
	public TextureRegion[] getFrames() {
		return enemyFrames;
	}

	
}