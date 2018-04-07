package com.example.justi.dodgegame;

/**
 * Created by justi on 2/20/2017.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Matrix;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;



public class GameBoard extends View{
    private Paint p;

    // Coordinates of rectangle
    private Rect mainSpriteBounds = new Rect(0,0,0,0);
    private Rect miniSpriteBounds = new Rect(0,0,0,0);
    private Rect miniSprite2Bounds = new Rect(0,0,0,0);

    // Sprite points
    private Point mainSprite;
    private Point miniSprite;
    private Point miniSprite2;
    private Point overSprite;

    //Bitmaps hold the sprite images
    private Bitmap bm1 = null;
    private Bitmap bm2 = null;
    private Bitmap bm2_2 = null;
    private Bitmap bm3 = null;

    // Matrices for rotation
    private Matrix m = null;
    private Matrix m2 = null;

    // For rotation
    private int miniSpriteRotation = 0;
    private int miniSprite2Rotation = 0;

    // Collision check
    private boolean collision = false;


    // Game over sprite setter
    synchronized public void setOverSprite(int x, int y)
    {
        overSprite = new Point(x,y);
    }

    // Main Sprite setter
    synchronized public void setMainSprite(int x, int y)
    {
        mainSprite = new Point(x,y);
    }

    // Main sprite get X
    synchronized public int getMainSpriteX()
    {
        return mainSprite.x;
    }

    // Main sprite get Y
    synchronized public int getMainSpriteY()
    {
        return mainSprite.y;
    }

    // Mini sprite setter
    synchronized public void setMiniSprite(int x, int y)
    {
        miniSprite = new Point(x,y);
    }

    // Mini sprite get X
    synchronized public int getMiniSpriteX()
    {
        return miniSprite.x;
    }

    // Mini sprite get Y
    synchronized public int getMiniSpriteY()
    {
        return miniSprite.y;
    }

    // Mini sprite 2 setter
    synchronized public void setMiniSprite2(int x, int y)
    {
        miniSprite2 = new Point(x,y);
    }

    // Mini sprite 2 get X
    synchronized public int getMiniSprite2X()
    {
        return miniSprite2.x;
    }

    // Mini sprite 2 get Y
    synchronized public int getMiniSprite2Y()
    {
        return miniSprite2.y;
    }

    // Get main sprite width
    synchronized public int getMainSpriteWidth()
    {
        return mainSpriteBounds.width();
    }

    // Get main sprite height
    synchronized public int getMainSpriteHeight()
    {
        return mainSpriteBounds.height();
    }

    // Get mini sprite width
    synchronized public int getMiniSpriteWidth()
    {
        return miniSpriteBounds.width();
    }

    // Get mini sprite height
    synchronized public int getMiniSpriteHeight()
    {
        return miniSpriteBounds.height();
    }

    //Get mini sprite 2 width
    synchronized public int getMiniSprite2Width()
    {
        return miniSprite2Bounds.width();
    }

    // Get mini sprite 2 height
    synchronized public int getMiniSprite2Height() {
        return miniSprite2Bounds.height();
    }

    // Returns collision check
    synchronized public boolean wasCollisionDetected()
    {
        return collision;
    }

    public GameBoard(Context context, AttributeSet aSet)
    {
        super(context, aSet);

        p = new Paint();

        // Initialize sprite locations
        mainSprite = new Point(-1,-1);
        miniSprite = new Point(-1,-1);
        miniSprite2 = new Point(-1,-1);

        // Make sure game over sprite is not seen
        overSprite = new Point(-2000,-2000);

        // 3 x 3 Matrix for coordinates
        m = new Matrix();
        m2 = new Matrix();


        // Initialize main sprite bitmap
        bm1 = BitmapFactory.decodeResource(getResources(), R.drawable.main);

        // Initialize mini sprite bitmap
        bm2 = BitmapFactory.decodeResource(getResources(), R.drawable.mini);

        // Initialize mini sprite 2 bitmap
        bm2_2 = BitmapFactory.decodeResource(getResources(), R.drawable.mini);

        // Initialize game over bitmap
        bm3 = BitmapFactory.decodeResource(getResources(), R.drawable.over);

        // Initialize bounds for sprites
        mainSpriteBounds = new Rect(0,0, bm1.getWidth(), bm1.getHeight());
        miniSpriteBounds = new Rect(0,0, bm2.getWidth(), bm2.getHeight());
        miniSprite2Bounds = new Rect(0,0, bm2_2.getWidth(), bm2_2.getHeight());
    }

    // IMPROVE SO THAT IT CHECKS BYTES
    private boolean checkCollision()
    {
        // If sprites are outside of boundaries
        if ((mainSprite.x<0 && miniSprite.x<0 && mainSprite.y<0 && miniSprite.y<0) ||
                (mainSprite.x<0 && miniSprite2.x<0 && mainSprite.y<0 && miniSprite2.y<0)) return false;

        // Main sprite rectangle bounds
        Rect r1 = new Rect(mainSprite.x, mainSprite.y, mainSprite.x + mainSpriteBounds.width(),  mainSprite.y + mainSpriteBounds.height());

        // Mini sprite rectangle bounds
        Rect r2 = new Rect(miniSprite.x, miniSprite.y, miniSprite.x + miniSpriteBounds.width(), miniSprite.y + miniSpriteBounds.height());

        // Mini sprite 2 rectangle bounds
        Rect r2_2 = new Rect(miniSprite2.x, miniSprite2.y, miniSprite2.x + miniSprite2Bounds.width(), miniSprite2.y + miniSprite2Bounds.height());

        // Check intersection of bounds
        if(r1.intersect(r2))
        {
            return true;
        }
        else if(r1.intersect(r2_2))
        {
            return true;
        }
        // Otherwise no collision
        return false;
    }

    // Drawing on canvas
    @Override
    synchronized public void onDraw(Canvas canvas) {
        // Blue Canvas
        int myColor = ContextCompat.getColor(getContext(), R.color.lightBlue);
        p.setColor(myColor);
        p.setAlpha(255);
        p.setStrokeWidth(1);
        canvas.drawRect(0, 0, getWidth(), getHeight(), p);

        // Draw sprites last
        if (mainSprite.x >= 0)
        {
            canvas.drawBitmap(bm1, mainSprite.x, mainSprite.y, null);
        }

        if (miniSprite.x >= 0)
        {
            m.reset();

            // Translating matrix
            m.postTranslate((float)(miniSprite.x), (float)(miniSprite.y));

            // Rotating matrix
            m.postRotate(miniSpriteRotation, (float)(miniSprite.x+miniSpriteBounds.width()/2.0),
                    (float)(miniSprite.y+miniSpriteBounds.width()/2.0));

            // Draw on canvas
            canvas.drawBitmap(bm2, m, null);

            // Update rotation
            miniSpriteRotation += 5;
            if (miniSpriteRotation >= 360){
                miniSpriteRotation = 0;
            }
        }

        if (miniSprite2.x >= 0) {
            m2.reset();

            // Translating matrix
            m2.postTranslate((float)(miniSprite2.x), (float)(miniSprite2.y));

            // Rotating matrix
            m2.postRotate(miniSprite2Rotation,(float)(miniSprite2.x+miniSprite2Bounds.width()/2.0),
                    (float)(miniSprite2.y+miniSprite2Bounds.width()/2.0));

            // Draw on canvas
            canvas.drawBitmap(bm2_2, m2, null);

            // Update rotation
            miniSprite2Rotation += 5;
            if (miniSprite2Rotation >= 360){
                miniSprite2Rotation = 0;
            }
        }

        // Draw game over sprite
        canvas.drawBitmap(bm3, overSprite.x, overSprite.y, null);

        // Check collision
        collision = checkCollision();
    }

    // Response to user touch
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // X value of touch
        int x = (int)event.getX();

        // If outside right bound, place sprite at end
        if(x > (findViewById(R.id.the_canvas).getWidth() - getMainSpriteWidth()))
        {
            mainSprite.x = findViewById(R.id.the_canvas).getWidth() - getMainSpriteWidth();
        }
        // If outside left bound, place sprite at start
        else if(x < 0)
        {
            mainSprite.x = 0;
        }
        else
        {
            mainSprite.x = x;
        }
        return true;
    }
}