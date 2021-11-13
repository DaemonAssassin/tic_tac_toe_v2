package com.mateenmehmood.tictactoev2;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    //declaring statusBar display textView ref var
    TextView statusBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initializing statusBar
        statusBar = findViewById(R.id.statusBar);
    }

    /**
     * creating variable for activePlayer.
     * PLAYER'S REPRESENTATION:
     * 0 --> 0,
     * X --> 1
     */
    int activePlayer = 0; //zero is active

    /**
     * creating count variable to count how many cells are filled
     */
    static int count = 0;

    /**
     * creating boolean variables to control flow
     */
    boolean isGameActive = true;
    boolean isFirstTurn = true;

    /**
     * creating a 1D array for gameState. e.g.
     * PLAYER STATE REPRESENTATION:
     * 0 --> 0,
     * 1 --> X,
     * 2 --> Empty/Null.
     * Initially all possible states/positions/cells are empty(2)
     */
    int[] gameState = new int[]{2, 2, 2, 2, 2, 2, 2, 2, 2};

    /**
     * creating a 2D array for all possible winning positions
     */
    int[][] winningPositions = new int[][]{
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
            {0, 4, 8}, {2, 4, 6}
    };

    /**
     * this method will call when someone tap on on any cell (imageview) on grid. this
     * method is common for all ImageViews.
     * @param view view is that view which is click (by onclick) like imageView
     */
    public void tapPlayer(View view) {

        //calling method to check is game is active
        checkGameActive();

        //condition to check is this the first turn or not
        if (!isFirstTurn) {
            checkGameFirstTurn();
            return;
        }

        //getting the view which is clicked/tapped
        //Now we have ImageView which is tapped on the UI grid/cells
        ImageView image = (ImageView) view;

        //getting tag of that imageView which is tapped
        int tappedImage = Integer.parseInt(image.getTag().toString());

        // Method to check the gameState
        // Method to apply imageViews (0 or X) to proper cells
        checkGameState (tappedImage, image);

        // Method to check if any player won the game or not
        checkWinnerOrDraw();

    }

    /**
     * this method is to check the gameState that the cell is empty or not and to set the
     * active player
     * @param tappedImage it is that imageview that has been tapped
     */
    private void checkGameState (int tappedImage, ImageView image) {
        if (gameState[tappedImage] == 2) {

            //setting active player at gameState[tappedImage] to start game
            gameState[tappedImage] = activePlayer;

            // Method to apply imageViews (0 or X) to proper cells
            setImageResources(tappedImage, image);
        }
    }

    /**
     * this method is to set the appropriate ImageViews to appropriate cells/locations.
     * @param tappedImage it is that imageview cell position that has been tapped
     * @param image image is the Original ImageView that has been tapped
     */
    private void setImageResources (int tappedImage, ImageView image) {
        //condition to check who's turn
        if (gameState[tappedImage] == 0) {
            image.setImageResource(R.drawable.o);

            //changing player turn
            activePlayer = 1;

            //setting status text to statusBar
            statusBar.setText(R.string.x_player_turn);
        } else {
            image.setImageResource(R.drawable.x);

            //changing player turn
            activePlayer = 0;

            //setting status text to statusBar
            statusBar.setText(R.string.zero_player_turn);
        }

        //increasing count for no of elements
        count++;
    }

    /**
     * this method is to check that anyone won the game or the game is drawn
     */
    private void checkWinnerOrDraw () {
        //loop to check if any player has won or game is drawn
        //loop to traverse/check winningPositions array
        for (int i = 0; i < winningPositions.length; i++) {

            // method to check winner
            checkWinnerPlayer (i);

            //condition to break the loop if someone won the game
            if (!isGameActive) {
                break;
            }

            // method to check is game is drawn?
            checkGameDrawn (i);
        }
    }

    /**
     * this method is to find the winner player
     * @param i it is the index value of the 1D objects in array [winningPositions]
     */
    @SuppressLint("ResourceAsColor")
    private void checkWinnerPlayer(int i) {

        //creating boolean variable for condition
        boolean isWon = gameState[winningPositions[i][0]] == gameState[winningPositions[i][1]] &&
                        gameState[winningPositions[i][1]] == gameState[winningPositions[i][2]] &&
                        gameState[winningPositions[i][0]] != 2;

        //condition to check anybody won?
        if (isWon) {

            //updating text on statusBar
            statusBar.setTextColor(R.color.purple_700);

            // method to play winning sound
            playWinningSound();

            // condition to check if 0's player is won or X
            if (gameState[winningPositions[i][0]] == 0) {
                statusBar.setText(R.string.zero_player_won);
            }
            else {
                statusBar.setText(R.string.x_player_won);
            }

            //if someone won the game then change isGameActive to false to reset the game
            isGameActive = false;
        }
    }

    /**
     * this method is to check if the game is drawn
     * @param i i it is the index value of the 1D objects in array [winningPositions]
     */
    @SuppressLint("ResourceAsColor")
    private void checkGameDrawn(int i) {
        //condition for game draw if all elements are filled
        if (i == 7 && count == 9) {

            //creating boolean variable for condition
            boolean isDraw = ((gameState[winningPositions[i][0]] == gameState[winningPositions[i][1]]) &&
                             (gameState[winningPositions[i][1]] != gameState[winningPositions[i][2]])) ||
                             ((gameState[winningPositions[i][0]] != gameState[winningPositions[i][1]]) &&
                             (gameState[winningPositions[i][1]] != gameState[winningPositions[i][2]])) ||
                             ((gameState[winningPositions[i][0]] != gameState[winningPositions[i][1]]) &&
                             (gameState[winningPositions[i][1]] == gameState[winningPositions[i][2]]));

            //condition to check if game is drawn?
            if (isDraw) {

                //updating text on statusBar
                statusBar.setTextColor(R.color.purple_700);
                statusBar.setText(R.string.game_draw);

                // method to play draw sound
                playDrawSound();

                //if game is drawn then change isGameActive to false to reset the game
                isGameActive = false;
            }
        }
    }

    /**
     * this method is to check the state of the game is it active or not
     */
    private void checkGameActive () {
        if (!isGameActive) {
            resetGame();
        }
    }

    /**
     * this method is to check that this is the first turn of the game after resetting
     */
    private void checkGameFirstTurn () {
            isGameActive = true;
            isFirstTurn = true;
    }

    /**
     * this method is to reset the game
     */
    @SuppressLint("ResourceAsColor")
    private void resetGame () {

        //changing isGameActive to true now
        isFirstTurn = false;

        //resetting count to 0
        count = 0;

        //changing gameState array to 2 (empty)
        Arrays.fill(gameState, 2);
        //gameState = new int[]{2, 2, 2, 2, 2, 2, 2, 2, 2};

        //resetting imageResources to null
        ((ImageView) findViewById(R.id.zero_x_image1)).setImageResource(0);
        ((ImageView) findViewById(R.id.zero_x_image2)).setImageResource(0);
        ((ImageView) findViewById(R.id.zero_x_image3)).setImageResource(0);
        ((ImageView) findViewById(R.id.zero_x_image4)).setImageResource(0);
        ((ImageView) findViewById(R.id.zero_x_image5)).setImageResource(0);
        ((ImageView) findViewById(R.id.zero_x_image6)).setImageResource(0);
        ((ImageView) findViewById(R.id.zero_x_image7)).setImageResource(0);
        ((ImageView) findViewById(R.id.zero_x_image8)).setImageResource(0);
        ((ImageView) findViewById(R.id.zero_x_image9)).setImageResource(0);

        //resetting statusBar
        statusBar.setTextColor(R.color.black);
        statusBar.setText(R.string.status_bar);

    }

    /**
     * this method is to play winning sound
     */
    private void playWinningSound() {
        MediaPlayer ring = MediaPlayer.create(MainActivity.this, R.raw.winning_sound);
        ring.start();
    }

    /**
     * this method is to play draw sound
     */
    private void playDrawSound() {
        MediaPlayer ring = MediaPlayer.create(MainActivity.this, R.raw.draw_sound);
        ring.start();
    }
}