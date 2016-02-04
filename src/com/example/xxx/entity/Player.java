package com.example.xxx.entity;

import org.andengine.entity.sprite.TiledSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.physics.box2d.Body;
import com.example.xxx.ResourceManager;

public class Player extends TiledSprite implements CollidableEntity
{
	boolean dead = false;
	private Body body;
	public static final String TYPE = "Player";
	
	public Player(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager)
	{
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
	}
	
/*	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,float pTouchAreaLocalX, float pTouchAreaLocalY) 
	{
		if(pSceneTouchEvent.isActionDown())
		{
			clearEntityModifiers();
			return true;
		}
		else if(pSceneTouchEvent.isActionMove())
		{
			setPosition(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
			return true;
		}
		return false;
	}*/
	
	
	public boolean isDead()
	{
		return dead;
	}
	public void setDead(boolean dead)
	{
		this.dead = dead;
	}
	
	public void turnLeft()
	{
		setFlippedHorizontal(true);
	}
	public void turnRight()
	{
		setFlippedHorizontal(false);
	}
	public void fly()
	{
		setCurrentTileIndex(0);
	}
	public void fall()
	{
		setCurrentTileIndex(1);
	}
	public void die()
	{
		if(!dead)
		{
			ResourceManager.getInstance().soundFall.play();
		}
		setDead(true);
		setCurrentTileIndex(2);
	}

	@Override
	public void setBody(Body body) {
		this.body = body;
	}

	@Override
	public Body getBody() {
		return body;
	}

	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);
		Utils.wraparound(this);
		if(getCurrentTileIndex() < 2)
		{
			if(body.getLinearVelocity().y < 0)
			{
				fall();
			}
			else
			{
				fly();
			}
		}
	}
}
