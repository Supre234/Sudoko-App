package com.example.sudokoapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private int[][] sudokuBoard = {
            {5, 3, 0, 0, 7, 0, 0, 0, 0},
            {6, 0, 0, 1, 9, 5, 0, 0, 0},
            {0, 9, 8, 0, 0, 0, 0, 6, 0},
            {8, 0, 0, 0, 6, 0, 0, 0, 3},
            {4, 0, 0, 8, 0, 3, 0, 0, 1},
            {7, 0, 0, 0, 2, 0, 0, 0, 6},
            {0, 6, 0, 0, 0, 0, 2, 8, 0},
            {0, 0, 0, 4, 1, 9, 0, 0, 5},
            {0, 0, 0, 0, 8, 0, 0, 7, 9}
    };

    private SudokuSolver solver;
    private GridLayout grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        solver = new SudokuSolver(sudokuBoard);
        grid = findViewById(R.id.sudokuGrid);

        // Populate the grid dynamically
        populateGrid();

        // Solve Sudoku
        Button solveButton = findViewById(R.id.solveButton);
        solveButton.setOnClickListener(v -> solveSudoku());

        // Reset Sudoku
        Button resetButton = findViewById(R.id.resetButton);
        resetButton.setOnClickListener(v -> resetGrid());

        // Clear Sudoku Grid
        Button clearButton = findViewById(R.id.clearButton);
        clearButton.setOnClickListener(v -> clearGrid());

        // Load Default Puzzle
        Button loadDefaultButton = findViewById(R.id.loadDefaultButton);
        loadDefaultButton.setOnClickListener(v -> loadDefaultPuzzle());

        // Check Solution
        Button checkSolutionButton = findViewById(R.id.checkSolutionButton);
        checkSolutionButton.setOnClickListener(v -> checkSolution());
    }

    private void populateGrid() {
        grid.removeAllViews();
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                EditText cell = new EditText(this);
                cell.setLayoutParams(new GridLayout.LayoutParams());
                cell.setWidth(100);
                cell.setHeight(100);
                cell.setGravity(android.view.Gravity.CENTER);
                cell.setBackgroundResource(R.drawable.cell_border);
                cell.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
                cell.setMaxLines(1);

                if (sudokuBoard[row][col] != 0) {
                    cell.setText(String.valueOf(sudokuBoard[row][col]));
                    cell.setEnabled(false); // Lock predefined cells
                }
                grid.addView(cell);
            }
        }
    }

    private void solveSudoku() {
        if (solver.solve()) {
            int[][] solvedBoard = solver.getBoard();
            for (int i = 0; i < grid.getChildCount(); i++) {
                EditText cell = (EditText) grid.getChildAt(i);
                int row = i / 9, col = i % 9;
                cell.setText(String.valueOf(solvedBoard[row][col]));
            }
        } else {
            Toast.makeText(this, "No solution exists!", Toast.LENGTH_SHORT).show();
        }
    }

    private void resetGrid() {
        for (int i = 0; i < grid.getChildCount(); i++) {
            EditText cell = (EditText) grid.getChildAt(i);
            int row = i / 9, col = i % 9;
            if (sudokuBoard[row][col] == 0) {
                cell.setText("");
                cell.setEnabled(true);
            } else {
                cell.setEnabled(false);
            }
        }
    }

    private void clearGrid() {
        for (int i = 0; i < grid.getChildCount(); i++) {
            EditText cell = (EditText) grid.getChildAt(i);
            cell.setText("");
            cell.setEnabled(true);
        }
    }

    private void loadDefaultPuzzle() {
        populateGrid();
        Toast.makeText(this, "Default puzzle loaded!", Toast.LENGTH_SHORT).show();
    }

    private void checkSolution() {
        int[][] currentBoard = new int[9][9];
        boolean isValid = true;

        for (int i = 0; i < grid.getChildCount(); i++) {
            EditText cell = (EditText) grid.getChildAt(i);
            int row = i / 9, col = i % 9;
            String text = cell.getText().toString();
            currentBoard[row][col] = text.isEmpty() ? 0 : Integer.parseInt(text);
        }

        solver = new SudokuSolver(currentBoard);
        if (solver.solve()) {
            Toast.makeText(this, "Solution is correct!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Incorrect solution!", Toast.LENGTH_SHORT).show();
        }
    }
}