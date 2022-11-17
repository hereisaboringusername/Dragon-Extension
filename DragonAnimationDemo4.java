/**
 * This demo, based on DragonAnimationDemo3, uses DragonSprite (a Sprite subclass)
 * for the dragon sprite.
 *
 * This would serve as a game core mechanic demo, if the game was a duck hunt type game
 * (https://en.wikipedia.org/wiki/Duck_Hunt), but with dragons instead of ducks.
 */

package com.gamefromscratch.graphicsdemo;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

//Project 2: Ashley Zingillioglu s1310999
import java.util.Random;


public class DragonAnimationDemo4 implements ApplicationListener {
    private SpriteBatch batch;
    //Project 2: Ashley Zingillioglu s1310999
    Array<DragonSprite> dragons;
    private float elapsedTime;
    // Make a dragon sprite
    private DragonSprite dragonSprite;
    //Project 2: Ashley Zingillioglu s1310999
    long lastDragonTime;
    int numberOfDragons= 0;
    // Can't match mouse click position to dragon sprite rectangle without camera
    OrthographicCamera camera;
    // Needed to unproject mouse click position in render
    Vector3 mousePosition;
    // Cursor change code per http://tiny.cc/a061tz
    // (original: http://www.martinrohwedder.dk/2016/02/libgdx-tutorial-how-to-change-the-mouse-cursor-image/)
    Pixmap cursorPixmap;



    @Override
    public void create() {

        batch = new SpriteBatch();
        // Construct dragon sprite
        dragonSprite = new DragonSprite();

        // Create the camera & mousePosition vector
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1000, 1000); // Same width & height in DesktopLauncher
        mousePosition = new Vector3();

        //Project 2:Ashley Zingillioglu s1310999 -- //character needs to have a speed value
        //translate this to Dragon Sprite 
        Random rand = new Random();
        int startingSide = rand.nextInt(1);
        if (startingSide == 0) {
            dragonSprite.setPosition(-100, 500);
        }
        else if (startingSide == 1) {
                dragonSprite.setPosition(1100, 500); //change to this.setPosition //add flip method
                dragonSprite.flip(true, false);
        }


        // Start dragonSprite off screen
        dragonSprite.setPosition(-100, 500);

        // alienXhairs.png, when loaded as a Pixmap, must have dimensions that are a power of 2,
        // e.g, 32, 64, 128 ...
        cursorPixmap = new Pixmap( Gdx.files.internal("cursor/alienXhairs.png") );
        Gdx.graphics.setCursor( Gdx.graphics.newCursor(cursorPixmap, cursorPixmap.getWidth() / 2,
                cursorPixmap.getHeight() / 2) );

        //Project 2:Ashley Zingillioglu

        int randomSpawnY= rand.nextInt(350);
        randomSpawnY += 500;
        dragonSprite.setPosition( dragonSprite.getX() , randomSpawnY);
        int turboChances = rand.nextInt(3);

        if (turboChances==0) {
         dragonSprite.turboMode();
        }

        //Project 2: Ashley Zingillioglu s1310999
        dragons = new Array<DragonSprite>();
        spawnDragon();

    }

    @Override
    public void dispose() {
        batch.dispose();
        cursorPixmap.dispose();
    }

    //Project 2: Ashley Zingillioglu s1310999
    private void spawnDragon() {
        if (numberOfDragons >= 10)
            return;

        DragonSprite dragon = new DragonSprite();
        float x = MathUtils.random(0, 800 - 64);
        float y = 400;
        dragon.setPosition(x, y);
        dragons.add(dragon);
        lastDragonTime = TimeUtils.nanoTime();
        numberOfDragons++;
    } // end spawnDragon()


    @Override
    public void render() {
        //Project 2 Ashley Zingillioglu s1310999 - 1
        Gdx.gl.glClearColor(164/255f, 219/255f, 232/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Tell the camera to update its matrices.
        camera.update();
        // Tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        dragonSprite.draw(batch);
        batch.end();

        elapsedTime += Gdx.graphics.getDeltaTime();

        //Project 2: Ashley Zingillioglu s1310999
        Iterator<DragonSprite> iter = dragons.iterator();
        while (iter.hasNext()) {
            DragonSprite dragon = iter.next();

            //Project 2: Ashley Zingillioglu s1310999
            // Move dragonSprite across screen horizontally, or vertically...
            if (dragon.isAlive()) {
                dragon.setX(dragon.getX() + 5);
                // If dragon leaves screen horizontally, it will come around again
                if (dragon.getX() > 1100) {
                    dragon.setPosition(-100, 500);
                }
            }
            else {
                dragon.setY(dragon.getY() - 10);
                float y = dragon.getY();
                if (y + 64 < 0) //replace 64 with size of sprite
                    iter.remove();
            }
        }
        // Set the next frame for the dragonSprite
        dragonSprite.update(elapsedTime);

        // If mouse click on dragon, kill dragon
        if (Gdx.input.isTouched()) {
            // Camera unprojection of mouse click position explained at
            // https://cssegit.monmouth.edu/jchung/libgdx.wiki/wikis/a-simple-game
            mousePosition.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(mousePosition);

            if (dragonSprite.getBoundingRectangle().contains(mousePosition.x, mousePosition.y)
                    && dragonSprite.isAlive()) {
                dragonSprite.kill();
            }
        }

        if (TimeUtils.nanoTime() - lastDragonTime > 1000000000)
            spawnDragon();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }
}
