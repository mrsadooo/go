package com.go.logic;
import java.util.ArrayList;

public class Chain {
	
	public ArrayList<Stone> stones;
	private Board parent = null;
	
	public Chain(ArrayList<Stone> stones, Board parent){
		this.stones = stones;
		
		/*
		 * Parent board of all stones
		 */
		this.parent = parent;
	}
	
	public boolean hasLiberties(){
		ArrayList<Stone> stones = this.stones;
		Board board = this.parent;
		for (int i = 0; i < stones.size(); i++){
			Stone stone = stones.get(i);
			for (int k = 0; k < Stone.directions.length; k++){
				int[] vector = Stone.directions[k];
				
				try {
					Stone neighbour = board.get(stone.x + vector[0], stone.y + vector[1]);
					
					if (neighbour.color == Stone.None)
						return true;
				} catch(Exception e){
					
				}
			}
		}
		
		return false;
	}
	
	public void remove() throws Exception {
		this.remove(this.parent);
	}
	
	public void remove(Board board) throws Exception {
		
		ArrayList<Stone> stones = this.stones;
		
		for (int i = 0; i < stones.size(); i++){
			Stone stone = stones.get(i);
			board.unset(stone.x, stone.y);
		}
		
	}
	
	public boolean isTerritory(byte player){
		byte opponent = player == Stone.White ? Stone.Black : Stone.White;
		ArrayList<Stone> stones = this.stones;
		Board board = this.parent;
		for (int i = 0; i < stones.size(); i++){
			Stone stone = stones.get(i);
			// if any stone is not an empty stone, we resturn
			if (stone.color != Stone.None)
				return false;
			for (int k = 0; k < Stone.directions.length; k++){
				int[] vector = Stone.directions[k];
				
				try {
					Stone neighbour = board.get(stone.x + vector[0], stone.y + vector[1]);
					
					if (neighbour.color == opponent)
						return false;
				} catch(Exception e){}
			}
		}
		
		return true;
	}
	
	public int length(){
		return this.stones.size();
	}
	
	public String toString(){
		String result = "[";
		
		for (int i = 0; i < this.stones.size(); i++){
			Stone stone = this.stones.get(i);
			result += "["+stone.x+","+stone.y+"]";
		}
		result += "]";
		return result;
	}
	
}
