package com.example.xxx.scene;

import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.TextMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ColorMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.util.adt.color.Color;

import com.example.xxx.SceneManager;

public class MenuSceneWrapper extends AbstractScene implements IOnMenuItemClickListener
{
	private IMenuItem playMenuItem;
	
	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) 
	{
		switch(pMenuItem.getID()){
		case 0:
			SceneManager.getInstance().showGameScene();
			return true;
		default:
			return false;
		}
	}

	@Override
	public void populate() 
	{
		MenuScene menuScene = new MenuScene(camera);
		menuScene.getBackground().setColor(0.82f, 0.96f, 0.97f);
		
		playMenuItem = new ColorMenuItemDecorator(new TextMenuItem(0, res.font, "PLAY", vbom), Color.CYAN, Color.WHITE);
		menuScene.addMenuItem(playMenuItem);
		menuScene.buildAnimations();
		menuScene.setBackgroundEnabled(true);
		
		menuScene.setOnMenuItemClickListener(this);
		
		Sprite player = new Sprite(240, 280, res.playerTextureRegion, vbom);
		menuScene.attachChild(player);
		
		setChildScene(menuScene);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onBackKeyPressed() {
		activity.finish();
	}
	
}
