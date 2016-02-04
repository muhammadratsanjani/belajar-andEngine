package com.example.xxx.factory;

import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.example.xxx.ResourceManager;
import com.example.xxx.entity.Enemy;

public class EnemyFactory 
{
	private static EnemyFactory INSTANCE = new EnemyFactory();
	public static final FixtureDef ENEMY_FIXTURE = PhysicsFactory.createFixtureDef(1f, 0f, 1f);
	private PhysicsWorld physicsWorld;
	private VertexBufferObjectManager vbom;
	
	private EnemyFactory()
	{}
	
	public static EnemyFactory getInstance()
	{
		return INSTANCE;
	}
	
	public void create(PhysicsWorld physicsWorld, VertexBufferObjectManager vbom)
	{
		this.physicsWorld = physicsWorld;
		this.vbom = vbom;
	}
	
	public Enemy createEnemy(float x, float y)
	{
		Enemy enemy = new Enemy(x, y, ResourceManager.getInstance().enemyTextureRegion, vbom);
		enemy.setZIndex(1);
		enemy.animate(75);
		
		Body enemyBody = PhysicsFactory.createBoxBody(physicsWorld, enemy, BodyType.KinematicBody, ENEMY_FIXTURE);
		enemyBody.setLinearVelocity(-1, 0);
		enemyBody.setUserData(enemy);		
		enemy.setBody(enemyBody);
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(enemy, enemyBody));	
		
		return enemy;		
	}
}
