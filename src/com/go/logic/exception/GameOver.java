package com.go.logic.exception;

public class GameOver extends Exception {
	
	public static final byte Pass = 1;
	public static final byte Resign = 2;
	
	public byte type;
	
	public GameOver(byte type){
		this.type = type;
	}
}
