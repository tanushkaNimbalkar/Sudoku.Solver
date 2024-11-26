package sudoku_Game;

import java.util.LinkedList;

// class Our_Queue
class Our_Queue {
	
	//ANSI code for colour
	final static String ANSI_GREEN = "\u001B[32m";
	final static String ANSI_RESET = "\u001B[0m";
	final static String ANSI_RED = "\u001B[31m";
	final static String ANSI_BOLD = "\u001B[1m";

	private LinkedList<int[][]> list;

	//Queue for entering all inputs for ananlyse purpose
	public Our_Queue() {
		list = new LinkedList<>();
	}

	//taking each change in the sudoku as input 
	public void enqueue(int[][] puzzle) {
		list.add(copy_users_sudoku(puzzle));
	}

	//Removes and returns the first element from this list.
	public int[][] dequeue() {
		if (isEmpty()) {
			return null;
		}
		return list.removeFirst();
	}

	public boolean isEmpty() {
		return list.isEmpty();
	}

	//copies the puzzle from the user and enqueues in queue
	private int[][] copy_users_sudoku(int[][] puzzle) {
		int[][] puzzleCopy = new int[9][9];
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				puzzleCopy[i][j] = puzzle[i][j];
			}
		}
		return puzzleCopy;
	}

	public void display() {
		System.out.println(ANSI_BOLD + "Analysis of your moves!" + ANSI_RESET);
		//printing analysis statement
		while (!isEmpty()) {
			int[][] puzzleState = dequeue();
			if (puzzleState != null) {
				System.out.println("Puzzle State:");
				for (int i = 0; i < 9; i++) {
					for (int j = 0; j < 9; j++) {
						if (puzzleState[i][j] == Sudoku.ans[i][j] && puzzleState[i][j] != 0
								&& puzzleState[i][j] != Sudoku.initial_puzzle[i][j]) {
							System.out.print(ANSI_BOLD + ANSI_GREEN + puzzleState[i][j] + " " + ANSI_RESET);
						} else if (puzzleState[i][j] != Sudoku.ans[i][j] && puzzleState[i][j] != 0) {
							System.out.print(ANSI_BOLD + ANSI_RED + puzzleState[i][j] + " " + ANSI_RESET);
						} else if (puzzleState[i][j] == 0) {
							System.out.print(puzzleState[i][j] + " ");
						} else if (puzzleState[i][j] == Sudoku.initial_puzzle[i][j]) {
							System.out.print(ANSI_BOLD + puzzleState[i][j] + " " + ANSI_RESET);
						}
					}
					System.out.println();
				}
				System.out.println();
			}
		}
	}

}