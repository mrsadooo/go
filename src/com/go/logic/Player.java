package com.go.logic;

import java.util.ArrayList;

public class Player {
	
	public byte color;
	public int stones;
	public int prisoners;
	public ArrayList<Stone> moves;
	
	public Player(byte color, int stones){
		this.color = color;
		this.stones = stones;
		this.prisoners = 0;
		this.moves = new ArrayList<Stone>();
	}
	
	public void reset(){
		this.stones = 0;
		this.prisoners = 0;
		this.moves.clear();
	}
	
}
