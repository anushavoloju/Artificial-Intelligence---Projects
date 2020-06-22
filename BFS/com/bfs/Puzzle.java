package com.bfs;

/**
 * @author Anusha Voloju
 * UID : 677775723
 * Net Id : avoloj2
 * University of Illinois, Chicago
 * CS 411, Fall 2018
 * Homework 4
 *
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Puzzle {

	public String inputString = "";

	private String expectedOutput;

	private final static String defaultExpectedOutput = "1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 0";

	private boolean solutionFound = false;

	private List<String> expandedNodes = new ArrayList<>();

	// to store the position(x,y) of 0 or empty space in the puzzle
	private Integer xPos;
	private Integer yPos;

	private Node inputNode;

	private Node outputNode;

	private List<Node> nodesToExpand = new ArrayList<>();

	private Node solutionNode;

	// Constructor to initialize the input and output nodes
	public Puzzle(String inputString, String expectedOutput) {
		this.inputString = inputString;
		this.expectedOutput = expectedOutput;
		this.inputNode = new Node(convertToIntMatrix(this.inputString), xPos, yPos, 4, 4, null, null);
		this.outputNode = new Node(convertToIntMatrix(this.expectedOutput), xPos, yPos, 4, 4, null, null);
	}

	public Puzzle(String inputString) {
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

	// the function that call applyBfs function and prints the output
	public void findBFSSolution() {

		long startTime = System.currentTimeMillis();

		try {
			this.applyBfs(this.inputNode, startTime);
		} catch (StackOverflowError e) {

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

	// perform bfs logic
	private void applyBfs(Node currentNode, long startTime) {
		long currentTime = System.currentTimeMillis();
		// check if the time elapsed is more than 30 minutes
		if ((currentTime - startTime) < 1800000) {
			expandedNodes.add(currentNode.getLocationString());
			if (checkIfGoalReached(currentNode)) {
				solutionFound = true;
				this.solutionNode = currentNode;
				return;
			}
			List<Node> childNodes = currentNode.getChildNodes();
			for (Node childNode : childNodes) {
				String locationString = childNode.getLocationString();
				// check if the node is already visited
				if (!expandedNodes.contains(locationString)) {
					nodesToExpand.add(childNode);
				}
			}
			childNodes.clear();
			if (nodesToExpand.size() != 0) {
				// check if there are more nodes to be expanded
				applyBfs(nodesToExpand.remove(0), startTime);
			} else {
				return;
			}

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
		// create an instance of puzzle with given input and perform bfs
		Puzzle puzzle = new Puzzle(str);
		input.close();
		puzzle.findBFSSolution();
	}
}
