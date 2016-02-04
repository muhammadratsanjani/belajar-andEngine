package com.example.xxx;

import org.andengine.util.debug.Debug;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import com.example.xxx.scene.AbstractScene;
import com.example.xxx.scene.GameScene;
import com.example.xxx.scene.LoadingScene;
import com.example.xxx.scene.MenuSceneWrapper;
import com.example.xxx.scene.SplashScene;

public class SceneManager 
{
	private static final SceneManager INSTANCE = new SceneManager();
	public static final long SPLASH_DURATION = 2000;
	
	private ResourceManager res = ResourceManager.getInstance();
	
	private AbstractScene currentScene;
	
	private LoadingScene loadingScene = null;
	Handler handler = new Handler(Looper.getMainLooper());
	
	private SceneManager() {}
	
	public static SceneManager getInstance()
	{
		return INSTANCE;
	}
	
	public AbstractScene getCurrentScene()
	{
		return currentScene;
	}
	
	public void setCurrentScene(AbstractScene currentScene)
	{
		this.currentScene = currentScene;
		res.engine.setScene(currentScene);
		Debug.i("Current scene: " + currentScene.getClass().getName());
	}
	
	public AbstractScene showSplashAndMenuScene()
	{
		final SplashScene splashScene = new SplashScene();
		splashScene.populate();

		setCurrentScene(splashScene);
		
		handler.post(new Runnable() {			
			@Override
			public void run() {
				new AsyncTask<Void, Void, Void>()
				{
					@Override
					protected Void doInBackground(Void... arg0) {
						long timeStamp = System.currentTimeMillis();
						res.loadFont();
						res.loadGameAudio();
						res.loadGameGraphics();
						
						loadingScene = new LoadingScene();
						loadingScene.populate();
						
						AbstractScene nextScene = new MenuSceneWrapper();
						
						if(System.currentTimeMillis() - timeStamp < SPLASH_DURATION)
						{
							try
							{
								Thread.sleep(SPLASH_DURATION - (System.currentTimeMillis() - timeStamp));
							}
							catch(InterruptedException e)
							{
								Debug.e("Interupted", e);
							}
						}
						nextScene.populate();
						setCurrentScene(nextScene);
						splashScene.destroy();
						res.unloadSplashGraphics();
						return null;
					}
					
				}.execute();
				
			}
		});
		
/*		new AsyncTask<Void, Void, Void>() 
		{
			@Override
			protected Void doInBackground(Void... params) 
			{				
				long timeStamp = System.currentTimeMillis();
				res.loadFont();
				res.loadGameAudio();
				res.loadGameGraphics();
				
				LoadingScene loadingScene = new LoadingScene();
				loadingScene.populate();
				
				AbstractScene nextScene = new MenuSceneWrapper();
				
				if(System.currentTimeMillis() - timeStamp < SPLASH_DURATION)
				{
					try
					{
						Thread.sleep(SPLASH_DURATION - (System.currentTimeMillis() - timeStamp));
					}
					catch(InterruptedException e)
					{
						Debug.e("Interupted", e);
					}
				}
				nextScene.populate();
				setCurrentScene(nextScene);
				splashScene.destroy();
				res.unloadSplashGraphics();
				return null;
			}
			
		}.execute();*/	
		
		return splashScene;
	}
	
	public void showGameScene()
	{
		final AbstractScene previousScene = getCurrentScene();
		setCurrentScene(loadingScene);
		
		handler.post(new Runnable() {			
			@Override
			public void run() {
				new AsyncTask<Void, Void, Void>()
				{
					@Override
					protected Void doInBackground(Void... params) {
						try{
							Thread.sleep(1000);
						} catch(InterruptedException e){
							Debug.e("interrupted", e);
						}
						
						GameScene gameScene = new GameScene();
						gameScene.populate();
						previousScene.destroy();
						setCurrentScene(gameScene);
						
						return null;
					}			
				}.execute();
				// TODO Auto-generated method stub
				
			}
		});
/*		new AsyncTask<Void, Void, Void>()
		{
			@Override
			protected Void doInBackground(Void... params) {
				GameScene gameScene = new GameScene();
				gameScene.populate();
				previousScene.destroy();
				setCurrentScene(gameScene);
				
				return null;
			}			
		}.execute();*/
	}
	
	public void showMenuScene()
	{
		final AbstractScene previousScene = getCurrentScene();
		setCurrentScene(loadingScene);
		
		handler.post(new Runnable() {			
			@Override
			public void run() {
				new AsyncTask<Void, Void, Void>()
				{
					@Override
					protected Void doInBackground(Void... params) {
						MenuSceneWrapper menuSceneWrapper = new MenuSceneWrapper();
						menuSceneWrapper.populate();
						setCurrentScene(menuSceneWrapper);
						previousScene.destroy();
						return null;
					}			
				}.execute();
			}
		});
/*		new AsyncTask<Void, Void, Void>()
		{
			@Override
			protected Void doInBackground(Void... params) {
				MenuSceneWrapper menuSceneWrapper = new MenuSceneWrapper();
				menuSceneWrapper.populate();
				setCurrentScene(menuSceneWrapper);
				previousScene.destroy();
				return null;
			}			
		}.execute();*/
	}
}
