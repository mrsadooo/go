package com.go.logic.exception;

public class SuicidalMove extends Exception {
	
	public int x;
	public int y;
	
	public SuicidalMove(int x, int y){
		this.x = x;
		this.y = y;
	}
}
