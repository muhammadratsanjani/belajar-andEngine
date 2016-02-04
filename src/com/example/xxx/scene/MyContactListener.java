package com.example.xxx.scene;

import org.andengine.util.debug.Debug;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.example.xxx.ResourceManager;
import com.example.xxx.entity.CollidableEntity;
import com.example.xxx.entity.Enemy;
import com.example.xxx.entity.Platform;
import com.example.xxx.entity.Player;

public class MyContactListener implements ContactListener
{
	Player player;
	
	public MyContactListener(Player player)
	{
		this.player = player;
	}
	@Override
	public void beginContact(Contact contact) {
		if(checkContact(contact, Player.TYPE, Enemy.TYPE))
		{
			player.die();
		}
	}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) 
	{
		if(checkContact(contact, Player.TYPE, Platform.TYPE))
		{

			if(!player.isDead() && player.getBody().getLinearVelocity().y < 0)
			{
				player.getBody().setLinearVelocity(new Vector2(0, 40));
				ResourceManager.getInstance().soundJump.play();
			}
			else
			{
				contact.setEnabled(false);
			}
		}
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}
	
	
	private boolean checkContact(Contact contact, String typeA, String typeB)
	{
		if(contact.getFixtureA().getBody().getUserData() instanceof CollidableEntity &&
				contact.getFixtureB().getBody().getUserData() instanceof CollidableEntity)
		{
			CollidableEntity ceA = (CollidableEntity) contact.getFixtureA().getBody().getUserData();
			CollidableEntity ceB = (CollidableEntity) contact.getFixtureB().getBody().getUserData();
			
			//Debug.d("andEngine", ceA.getType() +"+"+ ceB.getType()+"-----"+typeA+"+"+typeB);

			if((typeA.equals(ceA.getType()) && typeB.equals(ceB.getType())) ||
					(typeA.equals(ceB.getType()) && typeB.equals(ceA.getType())))
			{
				return true;
			}
		}
		return false;
	}
}
