package sudoku_Game;

// class Sudoku solver (backtracking algorithm)
class SudokuSolver {

	//function to check whether the number selected is correct in that position
	public static boolean isValid(int[][] board, int row, int col, int num) {
		//for each row correction
		for (int i = 0; i < 9; i++) {
			if (board[row][i] == num) {
				return false;
			}
		}

		//for each column correction
		for (int i = 0; i < 9; i++) {
			if (board[i][col] == num) {
				return false;
			}
		}

		//for each 3x3 grid collection
		int startRow = row - row % 3;
		int startCol = col - col % 3;
		for (int i = startRow; i < startRow + 3; i++) {
			for (int j = startCol; j < startCol + 3; j++) {
				if (board[i][j] == num) {
					return false;
				}
			}
		}
		return true;
	}


	public static boolean solveSudoku(int[][] ans) {
		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
				//entering number at unused position 
				if (ans[row][col] == 0) {
					//checking for every number between 1-9
					for (int num = 1; num <= 9; num++) {
						if (isValid(ans, row, col, num)) {
							ans[row][col] = num;
							//check for next empty position
							if (solveSudoku(ans)) {
								return true;
							}
							//if invalid position, backtrack and initialize to 0
							ans[row][col] = 0;
						}
					}
					return false;
				}
			}
		}
		return true;
	}
}


