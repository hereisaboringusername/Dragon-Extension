/**
 * DragonSprite class to be used with DragonAnimationDemo3
 * 
 * Uses dragon_small texture atlas.
 * 
 * See important note below on setBounds().
 */

package com.gamefromscratch.graphicsdemo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class DragonSprite extends Sprite {
    private TextureAtlas textureAtlas;
    private Animation animation;
    // Save flying dragon sprite frames in an Array for later:
    private Array<TextureAtlas.AtlasRegion> flyingFrames;
    // Store dead dragon sprite frame for later:
    private TextureRegion deadDragon;
    // Dragon sprite status:
    private boolean alive;
    private Sound dragonDeathSound;

    public DragonSprite() {
        textureAtlas = new TextureAtlas(Gdx.files.internal("za_dragon/dragon_small.atlas"));
        // When constructing the animation from dragon.atlas, tell textureAtlas to 
        // find only regions (frames) that are "flying", which should omit the
        // "dead" frame:
        flyingFrames = textureAtlas.findRegions("flying");
        animation = new Animation(1/5f, flyingFrames);
        // Store "dead" dragon frame from textureAtlas:
        deadDragon = textureAtlas.findRegion("dead");
        // ==> A Sprite is not drawable until its texture (region) and bounds are set
        //     (per http://bit.ly/2fzJove). So, the next line is essential:
        setBounds(0, 0, deadDragon.getRegionWidth(), deadDragon.getRegionHeight());
        // Could have also used
        // setBounds(0, 0, flyingFrames.get(0).getRegionWidth(), flyingFrames.get(0).getRegionHeight());
        // since all frames of the dragon sprite sheet are the same width and height.
        
        // Dragon sprite will start with the first flying frame (a TextureRegion) in the Array:
        setRegion(flyingFrames.get(0)); // Using super.setRegion()
        alive = true;
        // Choose one of two death sounds randomly
    	if (MathUtils.random(0, 1) == 0) {
    		dragonDeathSound = Gdx.audio.newSound(Gdx.files.internal("za_dragon/dragon_death1.wav"));
    	}
    	else {
    		dragonDeathSound = Gdx.audio.newSound(Gdx.files.internal("za_dragon/dragon_death2.wav"));
    	}
    }
    
    public boolean isAlive() {
    	return alive;
    }
    
    public void kill() {
    	alive = false;
    	setColor(Color.ORANGE);
    	dragonDeathSound.play();
    }
    
  //3a
  //Project 2: Ashley Zingillioglu s1310999
    public void update(float elapsedTime) {
    
        // Set the next frame for the DragonSprite:
    	if (alive) {
        //Assume running 60 frames per second
          if (elapsedTime % 60 == 0) {
            float random = MathUtils.random(1,4);
          if ( random == 1) {
            //Activate turbo mode. 
          }
          }
        
        	// Set DragonSprite's next frame from the animation:
    		// setRegion(animation.getKeyFrame(elapsedTime, true));
    		// getKeyFrame() may return Object in some instances, so would need
        	// to type cast to TextureRegion:
        	setRegion((TextureRegion) animation.getKeyFrame(elapsedTime, true));
    	}
    	else {
        	setRegion(deadDragon);
    	}
    }

}
