package com.example.xxx;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.bitmap.BitmapTextureFormat;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;


import android.graphics.Typeface;

public class ResourceManager 
{
	private static ResourceManager INSTANCE = new ResourceManager();	
	////
	public GameActivity activity;
	public Engine engine;
	public Camera camera;
	public VertexBufferObjectManager vbom;	
	//textures
	public ITiledTextureRegion playerTextureRegion;
	public ITiledTextureRegion enemyTextureRegion;
	public ITextureRegion platformTextureRegion;
	public ITextureRegion cloud1TextureRegion;
	public ITextureRegion cloud2TextureRegion;	
	private BuildableBitmapTextureAtlas gameTextureAtlas;	
	// sounds
	public Sound soundFall;
	public Sound soundJump;	
	// music
	public Music music;	
	// font
	public Font font;
	
	public ITextureRegion splashTextureRegion;
	private BitmapTextureAtlas splashTextureAtlas;
	
	private ResourceManager()
	{}	
	public void create(GameActivity activity, Engine engine, Camera camera, VertexBufferObjectManager vbom)
	{
		this.activity = activity;
		this.engine = engine;
		this.camera = camera;
		this.vbom = vbom;
	}
	
	public static ResourceManager getInstance()
	{
		return INSTANCE;
	}
	
	public void loadGameGraphics()
	{
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		gameTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 512, BitmapTextureFormat.RGBA_8888, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		
		playerTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity.getAssets(), "player.png", 3, 1);
		enemyTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity.getAssets(), "enemy.png", 1, 2);
		platformTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity.getAssets(), "platform.png");
		cloud1TextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity.getAssets(), "cloud1.png");
		cloud2TextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity.getAssets(), "cloud2.png");
		
		try
		{
			gameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(2, 0, 2));
			gameTextureAtlas.load();
		}
		catch(final TextureAtlasBuilderException e)
		{
			throw new RuntimeException("Error when loading game texture", e);
		}
	}
	
	public void loadGameAudio()
	{		
		try {
			SoundFactory.setAssetBasePath("sfx/");
			soundJump = SoundFactory.createSoundFromAsset(activity.getSoundManager(), activity, "jump.ogg");
			soundFall = SoundFactory.createSoundFromAsset(activity.getSoundManager(), activity, "fall.ogg");
			
			MusicFactory.setAssetBasePath("mfx/");
			music = MusicFactory.createMusicFromAsset(activity.getMusicManager(), activity, "music.ogg");
		} catch (Exception e) {
			throw new RuntimeException("Error while loading audio", e);
		}
	}
	
	public void loadFont()
	{
		font = FontFactory.createStroke(activity.getFontManager(), activity.getTextureManager(), 512, 256, Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD), 50,
				true, Color.WHITE_ABGR_PACKED_INT, 2, Color.BLACK_ABGR_PACKED_INT);
		font.prepareLetters("01234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ.,!?".toCharArray());
		font.load();
		//font.unload();
	}
	
	public void loadSplashGraphics()
	{
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		splashTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, BitmapTextureFormat.RGBA_8888, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		
		splashTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, activity.getAssets(), "badge.png", 0, 0);
		
		splashTextureAtlas.load();
	}
	
	public void unloadSplashGraphics()
	{
		splashTextureAtlas.unload();
	}
}
