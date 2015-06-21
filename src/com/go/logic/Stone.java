package com.go.logic;

public class Stone {
	static public final byte None = 0;
	static public final byte Black = 1;
	static public final byte White = 2;
	
	public static final int[][] directions = {
		{ 1, 0},
		{ 0, 1},
		{-1, 0},
		{ 0,-1}
	};
	
	public int x;
	public int y;
	public byte color;
	
	public Stone(int x, int y, byte color){
		this.x = x;
		this.y = y;
		this.color = color;
	}
}
