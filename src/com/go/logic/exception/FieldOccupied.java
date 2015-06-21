package com.go.logic.exception;

public class FieldOccupied extends Exception {
	
	public int x;
	public int y;
	
	public FieldOccupied(int x, int y){
		this.x = x;
		this.y = y;
	}
}
