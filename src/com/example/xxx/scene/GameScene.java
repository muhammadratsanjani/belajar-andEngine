package com.example.xxx.scene;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.collision.CollisionHandler;
import org.andengine.engine.handler.collision.ICollisionCallback;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.EntityBackground;
import org.andengine.entity.shape.IShape;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.AutoWrap;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.debugdraw.DebugRenderer;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.input.sensor.acceleration.AccelerationData;
import org.andengine.input.sensor.acceleration.IAccelerationListener;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.adt.color.Color;
import org.andengine.util.debug.Debug;
import org.andengine.util.modifier.ease.EaseExponentialIn;

import android.hardware.SensorManager;
import android.widget.Toast;

import com.badlogic.gdx.math.Vector2;
import com.example.xxx.GameActivity;
import com.example.xxx.R;
import com.example.xxx.ResourceManager;
import com.example.xxx.SceneManager;
import com.example.xxx.entity.CollidableEntity;
import com.example.xxx.entity.Enemy;
import com.example.xxx.entity.Platform;
import com.example.xxx.entity.Player;
import com.example.xxx.factory.EnemyFactory;
import com.example.xxx.factory.PlatformFactory;
import com.example.xxx.factory.PlayerFactory;

public class GameScene extends AbstractScene implements IAccelerationListener, IOnSceneTouchListener
{	
	private PhysicsWorld physicsWorld;
	
	private Player player;
	private Text scoreText;
	private Text endGameText;
	
	Random rand = new Random();
	private LinkedList<Platform> platforms = new LinkedList<Platform>();
	private LinkedList<Enemy> enemies = new LinkedList<Enemy>();
	
	private static final float MIN = 50f;
	private static final float MAX = 250f;
	
	private int score;
	
	public GameScene()
	{
		super();
		physicsWorld = new PhysicsWorld(new Vector2(0, - SensorManager.GRAVITY_EARTH), true);
		PlayerFactory.getInstance().create(physicsWorld,vbom);
		PlatformFactory.getInstance().create(physicsWorld, vbom);
		EnemyFactory.getInstance().create(physicsWorld, vbom);
	}
	@Override
	public void populate() 
	{
		createBackground();
		createPlayer();
		
		camera.setChaseEntity(player);
		
		createHUD();		
		addPlatform(240, 50, false);
		addPlatform(340, 400, false);
		addEnemy(140, 600);
		
		engine.enableAccelerationSensor(activity, this);
		
		engine.registerUpdateHandler(physicsWorld);
		
		physicsWorld.setContactListener(new MyContactListener(player));
		
		setOnSceneTouchListener(this);
		
		DebugRenderer dr = new DebugRenderer(physicsWorld, vbom);
		dr.setZIndex(999);
		attachChild(dr);
/*		//enemy = new AnimatedSprite(240, 200, res.enemyTextureRegion, vbom);
		enemy = new AnimatedSprite(240, 200, res.enemyTextureRegion, vbom){
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);
				if(collidesWith(player))
				{
					setScale(2);
				}
				else
				{
					setScale(1);
				}
			}
		};
				
		enemy.animate(125);
		//enemy.registerEntityModifier(new RotationModifier(2, 0, 360));
		//enemy.registerEntityModifier(new LoopEntityModifier(new RotationModifier(2, 0, 360)));
		enemy.registerEntityModifier(new LoopEntityModifier(new RotationModifier(2, 0, 360, EaseExponentialIn.getInstance())));
		attachChild(enemy);
		
		setOnSceneTouchListener(new IOnSceneTouchListener() {			
			@Override
			public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
				if(pSceneTouchEvent.isActionDown())
				{
					player.clearEntityModifiers();
					player.registerEntityModifier(new MoveModifier(1, player.getX(), player.getY(), pSceneTouchEvent.getX(), pSceneTouchEvent.getY()));
					return true;
				}
				return false;
			}
		});		
		
		registerTouchArea(player); //onAreaTouched player
*/				
		/*//Collision Handler
		ICollisionCallback myCollisionCallback = new ICollisionCallback() {			
			@Override
			public boolean onCollision(IShape pCheckShape, IShape pTargetShape) {
				enemy.setColor(Color.RED);
				return false;
			}
		};
		CollisionHandler myCollisionHandler = new CollisionHandler(myCollisionCallback, enemy, player);
		registerUpdateHandler(myCollisionHandler);*/
	}
	@Override
	protected void onManagedUpdate(float pSecondsElapsed) 
	{
		super.onManagedUpdate(pSecondsElapsed);
		boolean added = false;
		
		while(camera.getYMax() > platforms.getLast().getY())
		{
			float tx = rand.nextFloat() * GameActivity.CAMERA_WIDTH;
			float ty = platforms.getLast().getY() + MIN + rand.nextFloat() * (MAX - MIN);
			if(rand.nextFloat() < 0.1)
			{
				addEnemy(tx, ty);
			}
			boolean moving = rand.nextBoolean();
			addPlatform(tx, ty, moving);
			added = true;
		}
		if(added)
		{
			sortChildren();
		}
		cleanEntities(platforms, camera.getYMin());
		cleanEntities(enemies, camera.getYMin());	
		
		if(player.getY() < platforms.getFirst().getY())
		{
			player.die();
		}
		
		calculateScore();		
		
		if(player.isDead())
		{
			endGameText.setVisible(true);
		}
	}

	@Override
	public void onPause() {
		engine.disableAccelerationSensor(activity);
	}

	@Override
	public void onResume() {
		engine.enableAccelerationSensor(activity, this);
	}
	
	private void createBackground()
	{
		Entity background = new Entity();
		//background.setColor(0.44f, 0.56f, 0.9f);
		Sprite cloud1 = new Sprite(200, 300, res.cloud1TextureRegion, vbom);
		Sprite cloud2 = new Sprite(300, 600, res.cloud2TextureRegion, vbom);
		background.attachChild(cloud1);
		background.attachChild(cloud2);		
		setBackground(new EntityBackground(0.82f, 0.96f, 0.97f, background));
	}
	private void createPlayer()
	{
		player = PlayerFactory.getInstance().createPlayer(240, 400);
		attachChild(player);
	}
	
	private void createHUD()
	{
		HUD hud = new HUD();
		scoreText = new Text(16, 784, res.font, "0123456789", new TextOptions(HorizontalAlign.LEFT), vbom);
		scoreText.setAnchorCenter(0, 1);
		score = 0;
		scoreText.setText(String.valueOf(score));
		hud.attachChild(scoreText);
		
		//String rString = res.activity.getString(R.string.app_name);
		//scoreText.setText(rString);
		
		endGameText = new Text(GameActivity.CAMERA_WIDTH / 2, GameActivity.CAMERA_HEIGHT / 2, res.font, 
				"GAME OVER! TAP TO CONTINUE", new TextOptions(HorizontalAlign.CENTER), vbom);
		endGameText.setAutoWrap(AutoWrap.WORDS);
		endGameText.setAutoWrapWidth(300f);
		endGameText.setVisible(false);
		hud.attachChild(endGameText);
		
		camera.setHUD(hud);
	}
	
	private void addPlatform(float tx, float ty, boolean moving)
	{
		Platform platform;
		if(moving)
		{
			platform = PlatformFactory.getInstance().createMovingPlatform(tx, ty, (rand.nextFloat() - 0.5f)*10f);
		}
		else
		{
			platform = PlatformFactory.getInstance().createPlatform(tx, ty);
		}
		attachChild(platform);
		platforms.add(platform);
	}	
	
	private void addEnemy(float tx, float ty)
	{
		Enemy enemy = EnemyFactory.getInstance().createEnemy(tx, ty);
		attachChild(enemy);
		enemies.add(enemy);
	}
	
	private void cleanEntities(List<? extends CollidableEntity> list, float bound)
	{
		Iterator <? extends CollidableEntity> iter = list.iterator();
		while(iter.hasNext())
		{
			CollidableEntity ce = iter.next();
			if(ce.getY() < bound)
			{
				iter.remove();
				ce.detachSelf();
				physicsWorld.destroyBody(ce.getBody());
			}
		}
	}
	
	private void calculateScore()
	{
		if(camera.getYMin() > score)
		{
			score = Math.round(camera.getYMin());
			scoreText.setText(String.valueOf(score));
		}
	}
	
	
	
	@Override
	public void onAccelerationAccuracyChanged(AccelerationData pAccelerationData) {
		// TODO Auto-generated method stub
		
	}
	
	float lastX = 0;
	@Override
	public void onAccelerationChanged(AccelerationData pAccelerationData) 
	{
		if(Math.abs(pAccelerationData.getX()-lastX) > 0.5)
		{
			if(pAccelerationData.getX() > 0)
			{
				player.turnRight();
			}
			else
			{
				player.turnLeft();
			}
			lastX = pAccelerationData.getX();
		}
		//player.setX(player.getX() + pAccelerationData.getX());
		
/*		final Vector2 gravity = Vector2Pool.obtain(pAccelerationData.getX()*8, - SensorManager.GRAVITY_EARTH*4);
		this.physicsWorld.setGravity(gravity);
		Vector2Pool.recycle(gravity);*/
		
/*		final Vector2 force = Vector2Pool.obtain(pAccelerationData.getX()*50, 0);
		final Vector2 point = player.getBody().getWorldCenter();
		player.getBody().applyForce(force, point);
		Vector2Pool.recycle(force);*/
		
/*		final Vector2 impulse = Vector2Pool.obtain(pAccelerationData.getX() * 5, 0);
		final Vector2 point = player.getBody().getWorldCenter();
		player.getBody().applyLinearImpulse(impulse, point);
		Vector2Pool.recycle(impulse);*/
	
		final Vector2 velocity = Vector2Pool.obtain(pAccelerationData.getX() * 2,
		player.getBody().getLinearVelocity().y);
		player.getBody().setLinearVelocity(velocity);
		Vector2Pool.recycle(velocity);
/*		
		final Vector2 gravity = Vector2Pool.obtain(pAccelerationData.getX() * 8, -SensorManager.GRAVITY_EARTH * 4);
		this.physicsWorld.setGravity(gravity);
		Vector2Pool.recycle(gravity);		*/	
	}
	
	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) 
	{
		if(pSceneTouchEvent.isActionUp() && player.isDead())
		{
			restartGame();
			return true;
		}
		return false;
	}
	
	@Override
	public void onBackKeyPressed() {
		SceneManager.getInstance().showMenuScene();
		super.onBackKeyPressed();
	}
	
	private void restartGame()
	{
		setIgnoreUpdate(true);
		unregisterUpdateHandler(physicsWorld);
		enemies.clear();
		platforms.clear();
		physicsWorld.clearForces();
		physicsWorld.clearPhysicsConnectors();
		
		while(physicsWorld.getBodies().hasNext())
		{
			physicsWorld.destroyBody(physicsWorld.getBodies().next());
		}
		camera.reset();
		camera.setHUD(null);
		camera.setChaseEntity(null);
		
		detachChildren();
		populate();
		setIgnoreUpdate(false);
	}
	
}
