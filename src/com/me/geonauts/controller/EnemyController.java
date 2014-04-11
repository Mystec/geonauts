package com.me.geonauts.controller;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.me.geonauts.model.World;
import com.me.geonauts.model.entities.Block;
import com.me.geonauts.model.entities.Entity;
import com.me.geonauts.model.entities.enemies.AbstractEnemy;

public class EnemyController {

	// Collidable blocks.
	private Array<Block> collidable = new Array<Block>();
	
	// Model objects
	private World world;
	private AbstractEnemy enemy;
	
	/**
	 * Constructor to make the Controller for enemy
	 * @param world
	 */
	public EnemyController(World world, AbstractEnemy e) {
		this.world = world;
		this.enemy = e;
	}
	
	// This is the rectangle pool used in collision detection
	// Good to avoid instantiation each frame
	private Pool<Rectangle> rectPool = new Pool<Rectangle>() {
		@Override
		protected Rectangle newObject() {
			return new Rectangle();
		}
	};
	
	
	
	
	/**
	 * Update the Enemy's position and check collisions
	 * @param delta
	 */
	public void update(float delta) {
		// Convert acceleration to frame time
		enemy.acceleration.scl(delta);

		// apply acceleration to change velocity
		enemy.velocity.add(enemy.acceleration.x, enemy.acceleration.y);
		
		// checking collisions with the surrounding blocks depending on enemy's velocity
		checkCollisionWithBlocks(delta);

		// apply damping to halt enemy nicely
		//enemy.velocity.scl(DAMP);
		//enemy.velocity.y *= enemy.getDAMP();
		
		// ensure terminal X velocity is not exceeded
		if (enemy.velocity.x > enemy.MAX_VEL.x) 
			enemy.velocity.x = enemy.MAX_VEL.x;
		
		// ensure terminal Y velocity
		if (enemy.velocity.y >  enemy.MAX_VEL.y) 
			enemy.velocity.y = enemy.MAX_VEL.y;
		else if (enemy.velocity.y <  -enemy.MAX_VEL.y) 
			enemy.velocity.y = -enemy.MAX_VEL.y;
				
		
		//System.out.println(enemy.acceleration.toString());
		// simply updates the state time
		enemy.update(delta);

	}

	/** Collision checking **/
	private void checkCollisionWithBlocks(float delta) {
		// scale velocity to frame units
		enemy.velocity.scl(delta);

		// Obtain the rectangle from the pool instead of instantiating it
		Rectangle enemyRect = rectPool.obtain();
		// set the rectangle to enemy's bounding box
		enemyRect.set(enemy.getBounds().x, enemy.getBounds().y,
				enemy.getBounds().width, enemy.getBounds().height);

		
		
		
		
		// we first check the movement on the horizontal X axis
		int startX, endX;
		int startY = (int) enemy.getBounds().y;
		int endY = (int) (enemy.getBounds().y + enemy.getBounds().height);
		// if enemy is heading left then we check if he collides with the block on
		// his left
		// we check the block on his right otherwise
		if (enemy.velocity.x < 0) {
			startX = endX = (int) Math.floor(enemy.getBounds().x + enemy.velocity.x);
		} else {
			startX = endX = (int) Math.floor(enemy.getBounds().x + enemy.velocity.x + enemy.getBounds().width );
		}
		
		// get the block(s) enemy can collide with
		populateCollidableBlocks(startX, startY, endX, endY);

		// simulate enemy's movement on the X
		enemyRect.x += enemy.velocity.x;

		// clear collision boxes in world for debug
		world.getCollisionRects().clear();

		// if enemy collides, set state to dying
		for (Block block : collidable) {
			if (block == null)  continue;
			if (enemyRect.overlaps(block.getBounds())) {
				enemy.state = AbstractEnemy.State.DYING;
				world.getEnemyControllers().remove(this);
				world.getCollisionRects().add(block.getBounds()); // for debug
				break;
			}
		}

		

		// reset the x position of the collision box to check Y /////////////////////////////////////////////////////////
		enemyRect.x = enemy.position.x;

		// the same thing but on the vertical Y axis
		startX = (int) enemy.getBounds().x;
		endX = (int) (enemy.getBounds().x + enemy.getBounds().width);
		if (enemy.velocity.y < 0) {
			startY = endY = (int) Math.floor(enemy.getBounds().y + enemy.velocity.y );
		} else {
			startY = endY = (int) Math.floor(enemy.getBounds().y + enemy.velocity.y + enemy.getBounds().height );
		}

		populateCollidableBlocks(startX, startY, endX, endY);

		enemyRect.y += enemy.velocity.y;

		for (Block block : collidable) {
			if (block == null) 	continue;
			if (enemyRect.overlaps(block.getBounds())) {
				System.out.println("Enemy Collision @ " + block.position.toString());
				enemy.state = AbstractEnemy.State.DYING;
				world.getEnemyControllers().remove(this);
				world.getCollisionRects().add(block.getBounds());
				break;
			}
		}
		// reset the collision box's position on Y
		enemyRect.y = enemy.position.y;

		// update enemy's position
		enemy.position.add(enemy.velocity);
		enemy.getBounds().x = enemy.position.x;
		enemy.getBounds().y = enemy.position.y;

		// un-scale velocity (not in frame time)
		enemy.velocity.scl(1 / delta);

	}
	
	/** populate the collidable array with the blocks found in the enclosing coordinates **/
	private void populateCollidableBlocks(int startX, int startY, int endX,	int endY) {
		collidable.clear();
		for (int x = startX; x <= endX; x++) {
			for (int y = startY; y <= endY; y++) {
				if (x >= 0 && y >= 0) {
					collidable.add(world.getBlock(x, y) );
				}
			}
		}
	}
	
	/**
	 * Get the frames that belong to this Enemy. 
	 * @return
	 */
	public TextureRegion[] getFrames() {
		return enemy.getFrames();
	}

	public Entity getEnemyEntity() {
		return enemy;
	}
	
	
}