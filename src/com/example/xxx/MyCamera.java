package com.example.xxx;

import org.andengine.engine.camera.SmoothCamera;
import org.andengine.entity.IEntity;
import org.andengine.util.debug.Debug;

public class MyCamera extends SmoothCamera
{
	private IEntity chaseEntity;
	private boolean gameOver = false;
	
	public MyCamera(float pX, float pY, float pWidth, float pHeight) 
	{
		super(pX, pY, pWidth, pHeight, 300f, 1000f,1f);
		// TODO Auto-generated constructor stub
	}	
	
	@Override
	public void setChaseEntity(IEntity pChaseEntity) {
		// TODO Auto-generated method stub
		super.setChaseEntity(pChaseEntity);
		this.chaseEntity = pChaseEntity;
	}
	
	@Override
	public void updateChaseEntity() {
		// TODO Auto-generated method stub
		if(chaseEntity != null)
		{
			if(chaseEntity.getY() > getCenterY())
			{
				setCenter(getCenterX(), chaseEntity.getY());
			}
			else if(chaseEntity.getY() < getYMin() && !gameOver)
			{
				setCenter(getCenterX(), chaseEntity.getY() - getHeight());
				gameOver = true;
			}
		}
	}
	
	@Override
	public void reset() 
	{
		// TODO Auto-generated method stub
		super.reset();
		gameOver = false;
		set(0, 0, GameActivity.CAMERA_WIDTH, GameActivity.CAMERA_HEIGHT);
		setCenterDirect(GameActivity.CAMERA_WIDTH/2, GameActivity.CAMERA_HEIGHT/2);
	}
}
