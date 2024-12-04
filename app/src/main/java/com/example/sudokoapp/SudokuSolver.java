package com.example.sudokoapp;

public class SudokuSolver {
    private int[][] board;

    public SudokuSolver(int[][] initialBoard) {
        this.board = initialBoard;
    }

    public boolean solve() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board[row][col] == 0) {
                    for (int num = 1; num <= 9; num++) {
                        if (isValid(row, col, num)) {
                            board[row][col] = num;
                            if (solve()) return true;
                            board[row][col] = 0; // Backtrack
                        }
                    }
                    return false; // No valid number
                }
            }
        }
        return true; // Solved
    }

    private boolean isValid(int row, int col, int num) {
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == num || board[i][col] == num ||
                    board[row / 3 * 3 + i / 3][col / 3 * 3 + i % 3] == num) {
                return false;
            }
        }
        return true;
    }

    public int[][] getBoard() {
        return board;
    }
}
