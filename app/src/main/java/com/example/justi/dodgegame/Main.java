package com.example.justi.dodgegame;

import com.example.justi.dodgegame.GameBoard;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.app.Activity;
import java.util.Random;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.graphics.Point;


public class Main extends Activity implements OnClickListener
{

    private Handler frame = new Handler();

    private int score;

    // 50 Frames per second
    private static final int FRAME_RATE = 20;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Handler h = new Handler();
        ((Button)findViewById(R.id.reset_button)).setOnClickListener(this);
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                initGfx();
            }
        }, 1000);
    }

    // Main Sprite point location
    private Point getMainPoint()
    {
        // Center of screen
        int x = (findViewById(R.id.the_canvas).getWidth() - ((GameBoard)findViewById(R.id.the_canvas)).getMainSpriteWidth())/2;
        // Very bottom of screen
        int y = findViewById(R.id.the_canvas).getHeight() - ((GameBoard)findViewById(R.id.the_canvas)).getMainSpriteHeight();

        return new Point (x,y);
    }

    // Mini Sprite point location
    private Point getRandomMiniPoint()
    {
        Random r = new Random();

        // Random x value
        int minX = 0;
        int x = 0;
        int maxX = findViewById(R.id.the_canvas).getWidth() - ((GameBoard)findViewById(R.id.the_canvas)).getMiniSpriteWidth();
        x = r.nextInt(maxX-minX+1)+minX;

        // Start at the top
        int y = 0;

        return new Point (x,y);
    }

    // Initializes graphics (called at beginning / when pressing reset)
    synchronized public void initGfx()
    {
        // Points for initial sprite placement
        Point p1, p2, p3;

        p1 = getMainPoint();
        p2 = getRandomMiniPoint();
        p3 = getRandomMiniPoint();

        // Reset score
        score = 0;

        // Reset text for score
        ((TextView)findViewById(R.id.the_score_label)).setText("SCORE: 0");

        // Set main sprite
        ((GameBoard)findViewById(R.id.the_canvas)).setMainSprite(p1.x, p1.y);

        // Set mini sprites
        ((GameBoard)findViewById(R.id.the_canvas)).setMiniSprite(p2.x, p2.y);
        ((GameBoard)findViewById(R.id.the_canvas)).setMiniSprite2(p3.x, p3.y);

        // Set game over sprite to outside screen
        ((GameBoard) findViewById(R.id.the_canvas)).setOverSprite(-2000, -2000);

        // Enable reset button
        ((Button)findViewById(R.id.reset_button)).setEnabled(true);

        frame.removeCallbacks(frameUpdate);
        ((GameBoard)findViewById(R.id.the_canvas)).invalidate();
        frame.postDelayed(frameUpdate, FRAME_RATE);
    }

    @Override
    synchronized public void onClick(View v) {
        initGfx();
    }

    // Updates position of sprites
    private Runnable frameUpdate = new Runnable()
    {
        @Override
        synchronized public void run()
        {
            // If there is collision, get rid of sprites and display game over sprite
            if (((GameBoard)findViewById(R.id.the_canvas)).wasCollisionDetected())
            {
                ((GameBoard) findViewById(R.id.the_canvas)).setMainSprite(-500, -500);
                ((GameBoard) findViewById(R.id.the_canvas)).setMiniSprite(-200, -200);
                ((GameBoard) findViewById(R.id.the_canvas)).setMiniSprite2(-200, -200);
                ((GameBoard) findViewById(R.id.the_canvas)).setOverSprite(-25, -25);
                ((GameBoard)findViewById(R.id.the_canvas)).invalidate();
                return;
            }

            frame.removeCallbacks(frameUpdate);

            // Points of sprites
            Point mainSprite = new Point
                    (((GameBoard)findViewById(R.id.the_canvas)).getMainSpriteX(),
                            ((GameBoard)findViewById(R.id.the_canvas)).getMainSpriteY()) ;

            Point miniSprite = new Point
                    (((GameBoard)findViewById(R.id.the_canvas)).getMiniSpriteX(),
                            ((GameBoard)findViewById(R.id.the_canvas)).getMiniSpriteY()) ;

            Point miniSprite2 = new Point
                    (((GameBoard)findViewById(R.id.the_canvas)).getMiniSprite2X(),
                            ((GameBoard)findViewById(R.id.the_canvas)).getMiniSprite2Y()) ;

            // Have mini sprites move downwards
            miniSprite.y = miniSprite.y + 60;
            miniSprite2.y = miniSprite2.y + 40;


            // If mini sprite reaches end, reset it to the top continuously
            if (miniSprite.y >= 2000)
            {
                // Update score
                score += 10;
                ((TextView)findViewById(R.id.the_score_label)).setText("SCORE: " + Integer.toString(score));
                miniSprite = getRandomMiniPoint();
            }

            if (miniSprite2.y >= 2000)
            {
                miniSprite2 = getRandomMiniPoint();
            }

            ((GameBoard)findViewById(R.id.the_canvas)).setMainSprite(mainSprite.x, mainSprite.y);
            ((GameBoard) findViewById(R.id.the_canvas)).setMiniSprite(miniSprite.x, miniSprite.y);
            ((GameBoard) findViewById(R.id.the_canvas)).setMiniSprite2(miniSprite2.x, miniSprite2.y);
            ((GameBoard)findViewById(R.id.the_canvas)).invalidate();
            frame.postDelayed(frameUpdate, FRAME_RATE);
        }
    };
}
