package com.iddfs;

/**
 * @author Anusha Voloju
 * UID : 677775723
 * Net Id : avoloj2
 * University of Illinois, Chicago
 * CS 411, Fall 2018
 * Homework 5
 *
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Puzzle_IDDFS {

	public String inputString = "";

	private String expectedOutput;

	private final static String defaultExpectedOutput = "1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 0";

	private boolean solutionFound = false;

	private boolean timeElapsed = false;

	private List<String> expandedNodes = new ArrayList<>();

	// to store the position(x,y) of 0 or empty space in the puzzle
	private Integer xPos;
	private Integer yPos;

	private Node inputNode;

	private Node outputNode;

	private Node solutionNode;

	// Constructor to initialize the input and output nodes
	public Puzzle_IDDFS(String inputString, String expectedOutput) {
		this.inputString = inputString;
		this.expectedOutput = expectedOutput;
		this.inputNode = new Node(convertToIntMatrix(this.inputString), xPos, yPos, 4, 4, null, null);
		this.outputNode = new Node(convertToIntMatrix(this.expectedOutput), xPos, yPos, 4, 4, null, null);
	}

	public Puzzle_IDDFS(String inputString) {
		this(inputString, defaultExpectedOutput);
	}

	// the function that converts the input or output string to 4x4 matrix
	private int[][] convertToIntMatrix(String str) {
		int[][] convertedIntMatrix = new int[4][4];
		this.xPos = null;
		this.yPos = null;
		String[] tokens = str.split(" ");
		int index = 0;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				convertedIntMatrix[i][j] = Integer.parseInt(tokens[index]);
				if (convertedIntMatrix[i][j] == 0) {
					xPos = j;
					yPos = i;
				}
				index++;
			}
		}
		return convertedIntMatrix;
	}

	// the function that call applyIDDfs function and prints the output
	public void findIDDFSSolution() {

		long startTime = System.currentTimeMillis();

		try {

			for (int depth = 0; depth >= 0; depth++) {
				if (solutionFound) {
					System.out.println("Solution found at depth: " + (depth - 1));
					break;
				} else if (timeElapsed) {
					break;
				} else {
					expandedNodes.clear();
					expandedNodes = new ArrayList<>();
					this.applyIDDfs(this.inputNode, depth, startTime);
				}
			}

		} catch (StackOverflowError e) {
			System.out.println("Stack Overflow Error " + e.getMessage());
		}

		long endTime = System.currentTimeMillis();
		long memoryUsed = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

		if (solutionFound) {
			printMoves();
			System.out.println("Number of Nodes expanded: " + expandedNodes.size());
			System.out.println("Time Taken: " + (endTime - startTime) + "ms");
			System.out.println("Memory : " + memoryUsed / 1000 + "kb");

		}

		else {
			System.out.println("Solution cannot be found");
		}

	}

	private void printMoves() {
		String moves = getReversePathFromSolutionNode(this.solutionNode, "");
		System.out.println("Moves to reach the solution: " + moves);
	}

	// this function gets the path or moves from initial to goal state
	private String getReversePathFromSolutionNode(Node currentNode, String pathToSolutionNode) {
		Node parentNode = currentNode.getParentNode();
		if (parentNode == null) {
			return pathToSolutionNode;
		} else {
			return getReversePathFromSolutionNode(parentNode, currentNode.getPathFromParentNode() + pathToSolutionNode);
		}
	}

	// perform iterative deepening DFS logic
	private void applyIDDfs(Node currentNode, int depth, long startTime) {
		long currentTime = System.currentTimeMillis();
		// check if the time elapsed is more than 30 minutes
		if ((currentTime - startTime) < 1800000) {
			if (depth >= 0) {
				if (checkIfGoalReached(currentNode)) {
					solutionFound = true;
					this.solutionNode = currentNode;
					return;
				}
				if (!expandedNodes.contains(currentNode.getLocationString())) {
					expandedNodes.add(currentNode.getLocationString());
				}
				List<Node> childNodes = currentNode.getChildNodes();
				for (Node childNode : childNodes) {
					applyIDDfs(childNode, depth - 1, startTime);
				}
				childNodes.clear();
			}
		} else {
			timeElapsed = true;
		}
	}

	// to check if the current node is same as output/goal node
	private boolean checkIfGoalReached(Node currentNode) {
		for (int i = 0; i < 4; i++) {
			if (!Arrays.equals(currentNode.getLocations()[i], outputNode.getLocations()[i])) {
				return false;
			}
		}
		return true;
	}

	public static void main(String[] args) {
		System.out.println("Please enter an input string..");
		Scanner input = new Scanner(System.in);
		String str = input.nextLine();
		// create an instance of puzzle with given input and perform IDDfs
		Puzzle_IDDFS puzzle = new Puzzle_IDDFS(str);
		input.close();
		puzzle.findIDDFSSolution();
	}

}
