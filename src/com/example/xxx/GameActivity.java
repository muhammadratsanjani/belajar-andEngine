package com.example.xxx;


import java.io.IOException;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.engine.options.resolutionpolicy.IResolutionPolicy;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.debug.Debug;

import android.content.SharedPreferences;
import android.view.KeyEvent;

import com.example.xxx.scene.AbstractScene;
import com.example.xxx.scene.GameScene;



public class GameActivity extends BaseGameActivity {

	public static final int CAMERA_WIDTH= 480;
	public static final int CAMERA_HEIGHT = 800;
	
	private final String KEY_SOUND = "Sound";
	private final String KEY_HISCORE = "Hiscore";
	SharedPreferences settings;

	@Override
	public EngineOptions onCreateEngineOptions() 
	{
		Camera camera = new MyCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		IResolutionPolicy resolutionPolicy = new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT);
		//IResolutionPolicy resolutionPolicy = new FillResolutionPolicy();
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, resolutionPolicy, camera);
		
		engineOptions.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
		engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
		engineOptions.getRenderOptions().getConfigChooserOptions().setRequestedAlphaSize(8);
		engineOptions.getRenderOptions().getConfigChooserOptions().setRequestedRedSize(8);
		engineOptions.getRenderOptions().getConfigChooserOptions().setRequestedGreenSize(8);
		engineOptions.getRenderOptions().getConfigChooserOptions().setRequestedBlueSize(8);
		Debug.i("Engine Configured");
		return engineOptions;
	}

	@Override
	public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws IOException
	{
		ResourceManager.getInstance().create(this, getEngine(), getEngine().getCamera(), getVertexBufferObjectManager());
/*		ResourceManager.getInstance().loadGameGraphics();
		ResourceManager.getInstance().loadGameAudio();
		ResourceManager.getInstance().loadFont();*/
		
		ResourceManager.getInstance().loadSplashGraphics();
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws IOException
	{
		/*Scene scene = new GameScene();
		pOnCreateSceneCallback.onCreateSceneFinished(scene);*/
		
		pOnCreateSceneCallback.onCreateSceneFinished(null);
	}

	@Override
	public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws IOException
	{
		/*AbstractScene scene = (AbstractScene)pScene;
		scene.populate();*/
		
		SceneManager.getInstance().showSplashAndMenuScene();
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
	 
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		if(keyCode == KeyEvent.KEYCODE_BACK)
		{
			SceneManager.getInstance().getCurrentScene().onBackKeyPressed();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
