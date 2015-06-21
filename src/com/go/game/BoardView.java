package com.go.game;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewParent;
import android.widget.Button;

import com.go.logic.*;
import com.go.logic.exception.*;

public class BoardView extends View {

	public Game game;
	
	private static int White = Color.parseColor("#ffffff");
	private static int Black = Color.parseColor("#000000");
	private static int Grid = Color.parseColor("#000000");
	private static int Background = Color.parseColor("#ff0000");
	
	private static Paint paint = new Paint();
	
	public BoardView(Context context, Game game) {
        super(context);
		this.game = game;
    }
	
	private void drawBackground(Canvas canvas){
		paint.setColor(White);
		canvas.drawRect(0, 0, this.getWidth(), this.getHeight(), paint);
	}
	
	private void drawGrid(Canvas canvas){
		paint.setColor(Grid);
		
		Game game = this.game;
		Board board = game.board;
		
		int size = Math.min(this.getWidth(), this.getHeight());
		int boardWidth = board.width;
		int boardHeight = board.height;
		int stoneWidth = size / boardWidth;
		int stoneHeight = size / boardHeight;
		int halfWidth = (int) Math.round(stoneWidth / 2);
		int halfHeight = (int) Math.round(stoneHeight / 2);
		
		for (int i = 0; i < boardWidth; i++){
			int x = (int) Math.round((i + 0.5) * stoneWidth);
			canvas.drawLine(x, halfHeight, x, size - halfHeight, paint);
		}
		
		for (int i = 0; i < boardWidth; i++){
			int y = (int) Math.round((i + 0.5) * stoneHeight);
			canvas.drawLine(halfWidth, y, size - halfWidth, y, paint);
		}
	}
	
	private void drawStones(Canvas canvas) throws Exception {
		
		Game game = this.game;
		Board board = game.board;
		
		int size = Math.min(this.getWidth(), this.getHeight());
		int boardWidth = board.width;
		int boardHeight = board.height;
		int stoneWidth = size / boardWidth;
		int stoneHeight = size / boardHeight;
		int halfWidth = (int) Math.round(stoneWidth / 2);
		int halfHeight = (int) Math.round(stoneHeight / 2);
		
		for (int x = 0; x < board.width; x++){
			for (int y = 0; y < board.height; y++){
				Stone stone = board.get(x, y);
				
				int cx = (int) Math.round(x * stoneWidth + halfWidth);
				int cy = (int) Math.round(y * stoneHeight + halfHeight);
				int radius = (int) Math.min(stoneWidth / 2, stoneHeight / 2);
				
				switch (stone.color){
					case Stone.Black: 
						paint.setColor(Black);
						canvas.drawCircle(cx, cy, radius, paint);
					break;
					case Stone.White: 
						paint.setColor(Black);
						canvas.drawCircle(cx, cy, radius, paint);
						paint.setColor(White);
						canvas.drawCircle(cx, cy, radius-5, paint);
					break;
					default: continue;
				}
				
			}
		}		
		
	}
	
	public void onDraw(Canvas canvas){		
		drawBackground(canvas);
		drawGrid(canvas);
		
		try {
			drawStones(canvas);
		} catch(Exception e){}
		
		super.onDraw(canvas);
	}
	
	public void onMeasure(int width, int height){
		int size = Math.min(MeasureSpec.getSize(width), MeasureSpec.getSize(height));
		setMeasuredDimension(size, size);
	}
	
	public boolean onTouchEvent(MotionEvent event){
		
		int px = (int) Math.round(event.getX());
		int py = (int) Math.round(event.getY());
		
		Game game = this.game;
		Board board = game.board;
	
		int size = Math.min(this.getWidth(), this.getHeight());
		int boardWidth = board.width;
		int boardHeight = board.height;
		int stoneWidth = size / boardWidth;
		int stoneHeight = size / boardHeight;
		
		int x = (int) Math.floor(px / stoneWidth);
		int y = (int) Math.floor(py / stoneHeight);
		
		try {
			this.move(x, y);
		} catch (Exception e) {}
		
		return false;
		
	}
	
	public void move(int x, int y) throws Exception {
		
		Go.log("Move: " + x + "," + y);
		
		try {
			game.move(x, y);
			this.invalidate();
		} catch(FieldOccupied e){
			dialog("Field occupied");
		} catch(GameOver e){
			score();
			switch (e.type){
				case GameOver.Pass: dialog("Game over: Two passes in row"); break;
				case GameOver.Resign: dialog("Game over: Player resigned"); break;
			}
		} catch(OutOfStones e){
			dialog("Game Over: Player is out of stones");
		} catch(SuicidalMove e){
			dialog("Move is suicidal");
		} catch(KoRule e){
			dialog("KO rule obeyed");
		} catch (Exception e) {
			dialog("Unknown error");
		}
		
	}
	
	public void pass() throws Exception {
		
		Go.log("Pass");
		
		try {
			game.pass();
		} catch(GameOver e){
			score();
			switch (e.type){
				case GameOver.Pass: dialog("Game over: Two passes in row"); break;
				case GameOver.Resign: dialog("Game over: Player resigned"); break;
			}
		} catch (Exception e) {
			dialog("Unknown error");
		}
		
	}
	
	private void score() throws Exception {
		
		Game game = this.game;
		
		String message = "";
		
		int whiteScore = game.score(Stone.White);
		int blackScore = game.score(Stone.Black);
		
		message += "White score: " + whiteScore + "\n";
		message += "Black score: " + blackScore;
		
		dialog(message);
		
	}
	
	private void dialog(String message){
		Go.log(message);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
		
		builder.setMessage(message);
		
		builder.create().show();
	}
	
}
