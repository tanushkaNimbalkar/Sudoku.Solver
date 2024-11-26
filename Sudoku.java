package sudoku_Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;


// class Sudoku
public class Sudoku {
	static int hint_count = 0;
	static int boardHeight = 800;
	static int boardWidth = 900;
	JFrame frame = new JFrame("Sudoku");
	JLabel heading = new JLabel();
	JPanel textPanel = new JPanel();
	JPanel boardPanel = new JPanel();
	JButton[][] board = new JButton[9][9];
	JPanel button_panel = new JPanel();
	JButton solve_button = new JButton("solve");
	JButton hint_button = new JButton("hint");
	JButton analyse_button = new JButton("analyse");
	JButton check_button = new JButton("check");
	Our_Queue moveHistoryQueue = new Our_Queue();
	static int no_of_moves = 0;

	//sudoku that updates with each user move
	static int[][] puzzle = { { 0, 3, 0, 6, 0, 0, 9, 1, 2 }, { 6, 0, 0, 1, 9, 5, 0, 0, 8 },
			{ 1, 0, 0, 3, 0, 2, 0, 6, 7 }, { 0, 0, 0, 7, 0, 1, 0, 2, 0 }, { 4, 2, 6, 0, 5, 0, 7, 0, 1 },
			{ 0, 1, 0, 9, 0, 4, 0, 5, 0 }, { 9, 6, 0, 5, 3, 0, 2, 8, 4 }, { 0, 8, 7, 0, 1, 9, 0, 3, 5 },
			{ 3, 0, 5, 2, 0, 6, 1, 0, 9 } };

	//initial sudoku matrix for display in jframe window
	static int[][] initial_puzzle = { { 0, 3, 0, 6, 0, 0, 9, 1, 2 }, { 6, 0, 0, 1, 9, 5, 0, 0, 8 },
			{ 1, 0, 0, 3, 0, 2, 0, 6, 7 }, { 0, 0, 0, 7, 0, 1, 0, 2, 0 }, { 4, 2, 6, 0, 5, 0, 7, 0, 1 },
			{ 0, 1, 0, 9, 0, 4, 0, 5, 0 }, { 9, 6, 0, 5, 3, 0, 2, 8, 4 }, { 0, 8, 7, 0, 1, 9, 0, 3, 5 },
			{ 3, 0, 5, 2, 0, 6, 1, 0, 9 } };

	//calculates the answer of sudoku from backtracking
	static int[][] ans = { { 0, 3, 0, 6, 0, 0, 9, 1, 2 }, { 6, 0, 0, 1, 9, 5, 0, 0, 8 }, { 1, 0, 0, 3, 0, 2, 0, 6, 7 },
			{ 0, 0, 0, 7, 0, 1, 0, 2, 0 }, { 4, 2, 6, 0, 5, 0, 7, 0, 1 }, { 0, 1, 0, 9, 0, 4, 0, 5, 0 },
			{ 9, 6, 0, 5, 3, 0, 2, 8, 4 }, { 0, 8, 7, 0, 1, 9, 0, 3, 5 }, { 3, 0, 5, 2, 0, 6, 1, 0, 9 } };

	Sudoku() {
		frame.setVisible(true);
		frame.setSize(boardWidth, boardHeight);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setLayout(new BorderLayout());
		frame.add(textPanel, BorderLayout.CENTER);

		heading.setBackground(Color.darkGray);
		heading.setForeground(Color.white);
		heading.setFont(new Font("Arial", Font.ITALIC, 50));
		heading.setHorizontalAlignment(JLabel.CENTER);
		heading.setText("Sudoku");
		heading.setOpaque(true);

		textPanel.setLayout(new BorderLayout());
		textPanel.add(heading);
		frame.add(textPanel, BorderLayout.NORTH);

		setupButtons();

		boardPanel.setBackground(Color.darkGray);
		frame.setResizable(false);
		frame.add(boardPanel);
		boardPanel.setLayout(new GridLayout(9, 9));

		SudokuSolver sudoku = new SudokuSolver();
		sudoku.solveSudoku(ans);

		//sudoku layout printing
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				JButton tile = new JButton();
				board[i][j] = tile;
				boardPanel.add(tile);

				tile.setBackground(Color.darkGray);
				tile.setFocusable(false);
				tile.setForeground(Color.white);
				tile.setFont(new Font("Arial", Font.ITALIC, 35));

				//different colour system for already input values and empty places
				if (puzzle[i][j] != 0) {
					tile.setText(String.valueOf(puzzle[i][j]));
					tile.setBackground(Color.black);
					tile.setEnabled(false);
				} else {
					tile.setText("");
					tile.setEnabled(true);
				}

				tile.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						//accepting values from 1-9 from the user after clicking on each tile
						if (tile.isEnabled()) {
							String input = JOptionPane.showInputDialog(null, "Enter a number (1-9):");
							if (input != null && input.matches("[1-9]")) {
								//reads the text input and updates the empty postion
								tile.setText(input);
								int row = -1, col = -1;
								for (int i = 0; i < 9; i++) {
									for (int j = 0; j < 9; j++) {
										if (board[i][j] == tile) {
											row = i;
											col = j;
											break;
										}
									}
								}
								if (row != -1 && col != -1) {
									//changing to integer input
									puzzle[row][col] = Integer.parseInt(input);

									//calculates the moves utilized
									no_of_moves++;
									
									//inputs each input in the sudoku queue
									moveHistoryQueue.enqueue(puzzle);
								}
							}
						}
					}
				});
			}
		}

		//function call for inserting values
		insertValuesIntoTiles();
	}

	public void insertValuesIntoTiles() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (puzzle[i][j] != 0) {
					board[i][j].setText(String.valueOf(puzzle[i][j]));
				}
			}
		}
	}

	public void setupButtons() {
		
		//GUI layout for JFrame window

		Font font = new Font("Arial", Font.ROMAN_BASELINE, 20);
		Dimension buttonSize = new Dimension(107, 40);

		button_panel.setLayout(new BoxLayout(button_panel, BoxLayout.Y_AXIS));
		button_panel.setBackground(Color.darkGray);
		button_panel.setPreferredSize(new Dimension(150, boardHeight));

		Color buttons_background_color = Color.LIGHT_GRAY;
		Color buttons_foreground_color = Color.black;

		//hint button display
		hint_button.setPreferredSize(buttonSize);
		hint_button.setFocusable(false);
		hint_button.setBackground(buttons_background_color);
		hint_button.setForeground(buttons_foreground_color);
		hint_button.setMaximumSize(buttonSize);
		hint_button.setFont(font);
		hint_button.setAlignmentX(Component.CENTER_ALIGNMENT);
		button_panel.add(hint_button);
		button_panel.add(Box.createVerticalStrut(30));

		//check button display
		check_button.setPreferredSize(buttonSize);
		check_button.setFocusable(false);
		check_button.setBackground(buttons_background_color);
		check_button.setForeground(buttons_foreground_color);
		check_button.setMaximumSize(buttonSize);
		check_button.setFont(font);
		check_button.setAlignmentX(Component.CENTER_ALIGNMENT);
		button_panel.add(check_button);
		button_panel.add(Box.createVerticalStrut(30));
		
		//analyse button display
		analyse_button.setPreferredSize(buttonSize);
		analyse_button.setFocusable(false);
		analyse_button.setBackground(buttons_background_color);
		analyse_button.setForeground(buttons_foreground_color);
		analyse_button.setMaximumSize(buttonSize);
		analyse_button.setFont(font);
		analyse_button.setAlignmentX(Component.CENTER_ALIGNMENT);
		button_panel.add(analyse_button);
		button_panel.add(Box.createVerticalStrut(30));
		
		//solve button display
		solve_button.setPreferredSize(buttonSize);
		solve_button.setFocusable(false);
		solve_button.setBackground(buttons_background_color);
		solve_button.setForeground(buttons_foreground_color);
		solve_button.setMaximumSize(buttonSize);
		solve_button.setFont(font);
		solve_button.setAlignmentX(Component.CENTER_ALIGNMENT);
		button_panel.add(solve_button);
		button_panel.add(Box.createVerticalStrut(20));


		frame.add(button_panel, BorderLayout.EAST);

		//solve button for printing the answer matrix in another window
		solve_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame solvedFrame = new JFrame("Solution");
				solvedFrame.setSize(900, 800);
				solvedFrame.setLocationRelativeTo(null);
				solvedFrame.setVisible(true);

				JPanel solvedPanel = new JPanel();
				solvedPanel.setLayout(new GridLayout(9, 9));
				solvedPanel.setBackground(Color.darkGray);
				solvedFrame.add(solvedPanel);

				//printing layout of the solution matrix from backtracking
				for (int i = 0; i < 9; i++) {
					for (int j = 0; j < 9; j++) {
						JButton solvedButton = new JButton();
						if (initial_puzzle[i][j] != 0) {
							solvedButton.setBackground(Color.black);
							solvedButton.setForeground(Color.white);
							solvedButton.setText(String.valueOf(ans[i][j]));
						} else {
							solvedButton.setBackground(Color.darkGray);
							solvedButton.setForeground(Color.white);
							solvedButton.setText(String.valueOf(ans[i][j]));
						}
						solvedButton.setEnabled(false);
						solvedButton.setFont(new Font("Arial", Font.PLAIN, 30));
						solvedPanel.add(solvedButton);

					}
				}
			}
		});

		//hint button function
		hint_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Stack<Integer> not_filled = new Stack<>();
				hint_count++;
				int[] arr = checkRandom();
				int a = arr[0];
				int b = arr[1];

				//checks the condition of whether input is 0 and total hints taken are less than 20
				if (puzzle[a][b] == 0 && hint_count < 20) {
					not_filled.push(a);
					not_filled.push(b);

					// pushes position in Stack
					int space_j = not_filled.pop();
					int space_i = not_filled.pop();

					int hint = ans[space_i][space_j];

					board[space_i][space_j].setText(String.valueOf(hint));
					board[space_i][space_j].setBackground(Color.cyan);
					board[space_i][space_j].setForeground(Color.black);
					board[space_i][space_j].setEnabled(false);
					puzzle[space_i][space_j] = hint;
				}
				else {
					JOptionPane.showMessageDialog(frame, "Too many hints used \nNo more hints available");
				}
			}

			
			//generates the random positon of hint entering
			int[] checkRandom() {
				Random random = new Random();
				int[] arr = new int[2];
				int a = random.nextInt(9);
				int b = random.nextInt(9);
				if (puzzle[a][b] == 0) {
					arr[0] = a;
					arr[1] = b;
					return arr;
				}
					return checkRandom();
			}
		});

		//analyse button function
		analyse_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//		        JFrame analyse_frame = new JFrame("Analysis of your moves");
//		        analyse_frame.setSize(900, 800);
//		        analyse_frame.setLocationRelativeTo(null);
//		        analyse_frame.setVisible(true);

//		        JPanel analyse_Panel = new JPanel();
//		        analyse_Panel.setLayout(new GridLayout(9, 9));
//		        analyse_Panel.setBackground(Color.darkGray);
//		        analyse_frame.add(analyse_Panel);
				
				//retrieves value in the queue
				moveHistoryQueue.enqueue(puzzle);
				//displays the analysis from the queue
				moveHistoryQueue.display();

				JOptionPane.showMessageDialog(frame, "No of hints taken: " + hint_count + "\nNo of moves taken:" + no_of_moves);
			}
		});

		//check button function
		check_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < puzzle.length; i++) {
					for (int j = 0; j < puzzle[i].length; j++) {
						JButton currentButton = board[i][j];

						//checks if the entered value matches with the answer sudoku
						if (puzzle[i][j] == initial_puzzle[i][j]) {
							currentButton.setEnabled(false);
						}

						//sets background colour depending on the correction of position
						if (puzzle[i][j] == ans[i][j] && puzzle[i][j] != 0 && puzzle[i][j] != initial_puzzle[i][j]) {
//		                	currentButton.setBackground(Color.black);
							currentButton.setBackground(Color.GREEN);
							currentButton.setForeground(Color.BLACK);
							//doesn't accept another input from the user for correct position
							currentButton.setEnabled(false);
						} else if (puzzle[i][j] != ans[i][j] && puzzle[i][j] != 0) {
							currentButton.setBackground(Color.RED);
							currentButton.setForeground(Color.WHITE);
							//accept another input from the user for wrong position
							currentButton.setEnabled(true);
						} else if (puzzle[i][j] == 0) {
							currentButton.setBackground(Color.darkGray);
//		                    currentButton.setBackground(Color.darkGray); 
							currentButton.setForeground(Color.lightGray);
							//accept another input from the user for empty position
							currentButton.setEnabled(true);
						}
					}
				}
			}
		});

	}

}

