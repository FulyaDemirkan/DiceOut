package sheridan.demirkaf.diceout;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    // UI properties
    TextView mRollResult;
    TextView mScoreText;

    // Properties
    int mScore;
    int mDie1;
    int mDie2;
    int mDie3;
    ArrayList<Integer> mDice;
    ArrayList<ImageView> mDiceImageViews;

    Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Roll button
        FloatingActionButton fabRoll = findViewById(R.id.fabRoll);
        fabRoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rollDice(view);
            }
        });

        // Reset button
        FloatingActionButton fabReset = findViewById(R.id.reset);
        fabReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset(view);
            }
        });

        mScore = 0;
        mDice = new ArrayList<Integer>();
        mDiceImageViews = new ArrayList<ImageView>();
        random = new Random();

        mRollResult = findViewById(R.id.rollResultText);
        mScoreText = findViewById(R.id.scoreText);

        ImageView die1Image = findViewById(R.id.die1Image);
        ImageView die2Image = findViewById(R.id.die2Image);
        ImageView die3Image = findViewById(R.id.die3Image);

        mDiceImageViews.add(die1Image);
        mDiceImageViews.add(die2Image);
        mDiceImageViews.add(die3Image);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // for preventing app crush if the orientation changes even before rolling once
        if(mDice.isEmpty())
        {
            mDice.add(mDie1);
            mDice.add(mDie2);
            mDice.add(mDie3);
        }

        outState.putIntegerArrayList("diceState", mDice);
        outState.putString("scoreState", mScoreText.getText().toString());
        outState.putString("rollResultState", mRollResult.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mDice = savedInstanceState.getIntegerArrayList("diceState");
        setImageView(mDice);
        mScoreText.setText(savedInstanceState.getString("scoreState"));
        mRollResult.setText(savedInstanceState.getString("rollResultState"));
    }

    public void rollDice(View view) {
        mDie1 = random.nextInt(6) + 1;
        mDie2 = random.nextInt(6) + 1;
        mDie3 = random.nextInt(6) + 1;

        mDice.clear();
        mDice.add(mDie1);
        mDice.add(mDie2);
        mDice.add(mDie3);

        setImageView(mDice);

        String message =  "";

        if(mDie1 == mDie2 && mDie1 == mDie3)
        {
            int rollScore = mDie1 * 100;
            message = "You rolled a triple " + mDie1 + " for " + rollScore + " points!";
            mScore += rollScore;
        }
        else if (mDie1 == mDie2 || mDie1 == mDie3 || mDie2 == mDie3)
        {
            mScore += 50;
            message = "You rolled doubles for 50 points!";
        }
        else
        {
            message = "You did not score this roll. Try again!";
        }

        mRollResult.setText(message);
        mScoreText.setText("Score: " + mScore);
    }

    public void reset(View view)
    {
        mScoreText.setText(R.string.scoreText);
        mRollResult.setText(R.string.resultText);

        mScore = 0;

        // for preventing app crush if orientation changes right after reset
        mDice.clear();
        mDice.add(1);
        mDice.add(1);
        mDice.add(1);

        setImageView(mDice);

        Toast.makeText(getApplicationContext(), "You reset the game!", Toast.LENGTH_SHORT).show();
    }

    public void setImageView(ArrayList<Integer> dice)
    {
        for (int dieOfSet = 0; dieOfSet < 3; dieOfSet++)
        {
            String imageName = "die_" + dice.get(dieOfSet) + ".png";
            try
            {
                // reaches the assets folder and gets the item with the same name
                InputStream stream = getAssets().open(imageName);

                Drawable drawable = Drawable.createFromStream(stream, null);

                mDiceImageViews.get(dieOfSet).setImageDrawable(drawable);
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
