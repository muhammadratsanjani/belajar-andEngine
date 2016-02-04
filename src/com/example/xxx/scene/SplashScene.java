package com.example.xxx.scene;

import org.andengine.entity.sprite.Sprite;

import com.example.xxx.GameActivity;

public class SplashScene extends AbstractScene
{

	@Override
	public void populate() 
	{
		Sprite splashSprite = new Sprite(GameActivity.CAMERA_WIDTH/2, GameActivity.CAMERA_HEIGHT/2, res.splashTextureRegion, vbom);
		attachChild(splashSprite);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		
	}

}
