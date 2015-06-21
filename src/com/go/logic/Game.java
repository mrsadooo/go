package com.go.logic;

import com.go.logic.exception.*;

import java.util.ArrayList;

public class Game {

	public Player white;
	public Player black;
	public Board board;
	public Player current;
	public ArrayList<byte[][]> history;
	
	public Game(Board board) throws Exception {	
		this.board = board;
		int stones = (int) Math.floor(board.width * board.height / 2);
		this.white = new Player(Stone.White, stones);
		this.black = new Player(Stone.Black, stones);
		this.current = this.black;
		this.history = new ArrayList<byte[][]>();
	}
	
	public void reset() throws Exception {
		
		this.current = this.black;
		this.board.reset();
		this.white.reset();
		this.black.reset();
		this.history.clear();
				
	}
	
	public void move(int x, int y) throws Exception {
		
		if (this.current.stones == 0)
			throw new OutOfStones();
		
		Board board = this.board;
		
		// 1. check if field is occupied 
		if (board.isOccupied(x, y))
			throw new FieldOccupied(x, y);
		
		// create a clone  of a board to test a move
		Board state = new Board(this.board.getState());
		byte color = this.current.color;
		
		// set a stone
		state.set(x, y, color);
		
		// 2. Check if neighbors were captured
		ArrayList<Stone> neighbors = state.getNeighbours(x, y);
		
		Chain chain;
		
		// iterate neighbors to check if their chains were captured
		for (int i = 0; i < neighbors.size(); i++){
			Stone n = neighbors.get(i);
			n = state.get(n.x, n.y);
			
			if (n.color == color)
				continue;
			
			chain = state.getChain(n.x, n.y);
			
			if (!chain.hasLiberties()){
				chain.remove();
				this.current.prisoners += chain.stones.size();
			}
				
		}

		// 3. Check if move is not suicidal
		chain = state.getChain(x, y);
		
		if (!chain.hasLiberties())
			throw new SuicidalMove(x, y);
		
		// 4. Check if it is a KO move
		ArrayList<byte[][]> history = this.history;
		Board historyBoard = new Board(state.width, state.height);
		for (int i = 0; i < history.size(); i++){
			historyBoard.setState(history.get(i));
			if (historyBoard.isEqual(state))
				throw new KoRule();
		}
		
		// move was valid, so we save the state and switch the player
		this.history.add(this.board.getState());
		this.board.setState(state.getState());
		this.current.moves.add(board.get(x, y));
		this.current.stones--;
		this.current = this.current == this.white ? this.black : this.white;
		
	}
	
	public void pass() throws GameOver {
		// player passed the move, so we switch the player
		this.current.moves.add(null);
		this.current.prisoners = Math.max(0, this.current.prisoners - 1);
		this.current = this.current == this.white ? this.black : this.white;
		this.validate();
	}
	
	public void resign() throws GameOver {
		throw new GameOver(GameOver.Resign);
	}
	
	private void validate() throws GameOver {
		
		// check if last moves were passed
		ArrayList<Stone> whiteMoves = this.white.moves;
		ArrayList<Stone> blackMoves = this.black.moves;
		
		// if last moves were passes, we throw a game over exception
		if (whiteMoves.size() > 0 && blackMoves.size() > 0 && 
			whiteMoves.get(whiteMoves.size() - 1) == null && blackMoves.get(blackMoves.size() - 1) == null){
			
			throw new GameOver(GameOver.Pass);
		}

	}
	
	public int score(byte color) throws Exception {
		
		Player player = color == Stone.White ? this.white : this.black;
		int result = player.prisoners;
		
		Board board = this.board;
		boolean[][] visited = new boolean[board.width][board.height];
		
		for (int x = 0; x < board.width; x++){
			for (int y = 0; y < board.height; y++){
				if (visited[x][y])
					continue;
				
				Chain chain = board.getChain(x, y);
				
				if (chain.isTerritory(color))
					result += chain.length();
				
				ArrayList<Stone> stones = chain.stones;
				
				for (int i = 0; i < stones.size(); i++){
					Stone s = stones.get(i);
					visited[s.x][s.y] = true;
				}
			}
		}
		
		return result;
		
	}
	
}
