package com.go.logic;

import com.go.logic.exception.*;

import java.util.ArrayList;
import java.util.Arrays;

public class Board {

	public int width = 19;
	public int height = 19;
	private byte[][] map;
	
	/*
	 * Supply one argument 
	 */
	public Board(byte[][] board){
		this.width = board.length;
		this.height = board[0].length;
		
		this.map = new byte[this.height][this.width];
		
		for (int x = 0; x < this.width; x++)
			for (int y = 0; y < this.height; y++)
				this.map[y][x] = board[y][x];
	}
	
	/*
	 * Supply one argument 
	 */
	public Board(int size){
		this.width = size;
		this.height = size;

		this.map = new byte[this.height][this.width];
	}
	
	/*
	 * Supply two arguments 
	 */
	public Board(int width, int height){
		this.width = width;
		this.height = height;

		this.map = new byte[this.height][this.width];
	}
	
	/*
	 * With no arguments 
	 */
	public Board(){
		this.map = new byte[this.height][this.width];
	}
	
	/*
	 * Returns a stone from a given field
	 */
	public Stone get(int x, int y) throws Exception {
		if (!isFieldValid(x, y))
			throw new InvalidPosition(x, y);
		return new Stone(x, y, this.map[y][x]);
	}
	
	/*
	 * Sets a stone on a given field
	 */
	public void set(int x, int y, byte stone) throws Exception {
		if (!isFieldValid(x, y))
			throw new InvalidPosition(x, y);
		this.map[y][x] = stone;
	}
	
	public void unset(int x, int y) throws Exception {
		if (!isFieldValid(x, y))
			throw new InvalidPosition(x, y);
		this.map[y][x] = Stone.None;
	}
	
	private boolean isFieldValid(int x, int y){
		return x >= 0 && x < this.width && y >= 0 && y < this.height;
	}

	public Chain getChain(int x, int y) throws Exception {
		
		Stone origin = this.get(x, y);
		
		ArrayList<Stone> list = new ArrayList<Stone>();
		
		// list of visited fields during search
		boolean[][] visited = new boolean[this.width][this.height];
		
		// list of stones to check
		ArrayList<Stone> opened = new ArrayList<Stone>();
		
		opened.add(origin);
		visited[x][y] = true;
		
		while (opened.size() > 0){
			Stone current = opened.remove(0);
			
			if (current.color != origin.color)
				continue;
			
			list.add(current);
			
			// look for neighbours
			for (int i = 0; i < Stone.directions.length; i++){
				int[] vector = Stone.directions[i];
				int nx = current.x + vector[0], 
				    ny = current.y + vector[1];

				if (!this.isFieldValid(nx, ny))
					continue;
				if (visited[nx][ny]){
					continue;
				}
				
				visited[nx][ny] = true;
				
				Stone next = this.get(nx, ny);
				
				opened.add(next);
			}
		}
		
		return new Chain(list, this);
		
	}

	public byte[][] getState(){
		return this.map.clone();
	}
	
	public void setState(byte[][] state){
		this.map = state;
	}
	
	public ArrayList<Stone> getNeighbours(int x, int y){
		ArrayList<Stone> stones = new ArrayList<Stone>();
		
		int[][] directions = Stone.directions;
				
		for (int i = 0; i < directions.length; i++){
			int[] vector = directions[i];
			
			int nx = x + vector[0];
			int ny = y + vector[1];
			
			try {
				Stone stone = get(nx, ny);
				if (stone.color != Stone.None)
					stones.add(get(nx, ny));
			} catch(Exception e){}
		}
		
		return stones;
	}
	
	public boolean isEqual(Board board) {
		return Arrays.deepEquals(this.map, board.getState());
	}
	
	public boolean isOccupied(int x, int y) throws Exception {
		if (!isFieldValid(x, y))
			throw new InvalidPosition(x, y);
		return this.map[y][x] != Stone.None;
	}
	
	public void reset() throws Exception {
		for (int x = 0; x < this.width; x++){
			for (int y = 0; y < this.height; y++){
				this.unset(x, y);
			}
		}
	}
	
}

