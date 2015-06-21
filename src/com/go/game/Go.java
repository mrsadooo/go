package com.go.game;

// Config for an application
import com.go.game.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
//import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.util.Log;

import com.go.logic.*;
import com.go.logic.exception.GameOver;

/**
 * This class provides a basic demonstration of how to write an Android
 * activity. Inside of its window, it places a single view: an EditText that
 * displays and edits some internal text.
 */
public class Go extends Activity {
    
    public Go() {
    }

    /** Called with the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
        	home();
        } catch (Exception e){}
    }
    
    public static void log(String message){
    	Log.i("log", message);
    }
    
    /*
     * Display home screen
     */
    private void home(){
    	// Inflate our UI from its XML layout description.
        setContentView(R.layout.menu);
        
        Go.log("Activity started");
        
        // Hook up button presses to the appropriate event handler.
        ((Button) findViewById(R.id.play9)).setOnClickListener(playListener);
        ((Button) findViewById(R.id.play19)).setOnClickListener(playListener);
        
    }
    
    /*
     * Display game screen
     */
    private void game(int size) throws Exception {
		
    	Board board = new Board(size, size);
		final Game game = new Game(board);
    	final BoardView gameView = new BoardView(this, game);
    	
    	LinearLayout view = new LinearLayout(this);
    	view.setOrientation(LinearLayout.VERTICAL);
    	view.addView(gameView);
    	
    	Button pass = new Button(this);
    	
    	pass.setText("Pass");
    	pass.setOnClickListener(new OnClickListener() {
		        @Override
		        public void onClick(View arg0){
		        	try {
		        		gameView.pass();
		        	} catch(Exception e){}
		        }
		});
    	
    	view.addView(pass);
    	
    	Button exit = new Button(this);
    	exit.setText("Exit");
    	exit.setOnClickListener(new OnClickListener() {
		        @Override
		        public void onClick(View arg0){
		        	try {
		        		home();
		        	} catch(Exception e){}
		        }
		});
    	view.addView(exit);
    	
    	// Set a game layout
        setContentView(view);
        
    }

    /**
     * Called when the activity is about to start interacting with the user.
     */
    @Override
    protected void onResume() {
        super.onResume();
    }
    
    /*@Override
    public void onBackPressed(){
    	super.onBackPressed();
    }*/

    /**
     * A call-back for when the user presses the play button.
     */
    OnClickListener playListener = new OnClickListener() {
        public void onClick(View v) {
        	try {
        		int size = Integer.parseInt(v.getTag().toString());
        		game(size);
        	} catch (Exception e){
        		
        	}
        }
    };
}
