package com.go.logic.exception;

public class InvalidPosition extends Exception {
	
	public int x;
	public int y;
	
	public InvalidPosition(int x, int y){
		this.x = x;
		this.y = y;
	}
}
