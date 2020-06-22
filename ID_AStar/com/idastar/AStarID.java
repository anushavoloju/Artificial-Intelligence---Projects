package com.idastar;

/**
 * @author Anusha Voloju
 * UID : 677775723
 * Net Id : avoloj2
 * University of Illinois, Chicago
 * CS 411, Fall 2018
 * Homework 7
 *
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class AStarID {

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

	private Integer shortestValue = 0;

	private Integer nextThreshold = -1;

	// Constructor to initialize the input and output nodes
	public AStarID(String inputString, String expectedOutput, Heuristic heuristicType) {
		this.inputString = inputString;
		this.expectedOutput = expectedOutput;
		this.inputNode = new Node(convertToIntMatrix(this.inputString), xPos, yPos, 4, 4, null, null, heuristicType);
		this.outputNode = new Node(convertToIntMatrix(this.expectedOutput), xPos, yPos, 4, 4, null, null,
				heuristicType);
	}

	public AStarID(String inputString, Heuristic heuristicType) {
		this(inputString, defaultExpectedOutput, heuristicType);
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

	// the function that call applyAStar function and prints the output
	public void findAStarIDSolution() {
		System.out.println("Final path : " + expectedOutput);
		long startTime = System.currentTimeMillis();

		try {
			shortestValue = this.inputNode.getHeuristicValue();
			int threshold = shortestValue;
			while (!solutionFound) {
				expandedNodes.clear();
				nodesToExpand.clear();
				nextThreshold = -1;
				threshold = this.applyAStarID(this.inputNode, startTime, threshold);
			}
		} catch (StackOverflowError e) {
			System.out.println("Number of Nodes expanded: " + expandedNodes.size());
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
		String moves = this.solutionNode.getPathFromParentNode();
		System.out.println("Moves to reach the solution: " + moves);
	}

	private void addChild(int totalValue, Node childNode) {
		int i = 0;
		for (i = 0; i < nodesToExpand.size(); i++) {
			if (nodesToExpand.get(i).getCostFromRoot() + nodesToExpand.get(i).getHeuristicValue() > totalValue) {
				break;
			}
		}
		if (i < nodesToExpand.size()) {
			nodesToExpand.add(i, childNode);
		} else {
			nodesToExpand.add(childNode);
		}
	}

	// perform AStar logic
	private int applyAStarID(Node currentNode, long startTime, int threshold) {
		Integer totalValue = currentNode.getCostFromRoot() + currentNode.getHeuristicValue();
		if (threshold < totalValue) {
			return totalValue;
		}
		long currentTime = System.currentTimeMillis();
		// check if the time elapsed is more than 30 minutes
		if ((currentTime - startTime) < 1800000) {
			expandedNodes.add(currentNode.getLocationString());
			if (checkIfGoalReached(currentNode)) {
				solutionFound = true;
				this.solutionNode = currentNode;
				return currentNode.getCostFromRoot();
			}
			List<Node> childNodes = currentNode.getChildNodes();
			for (Node childNode : childNodes) {
				String locationString = childNode.getLocationString();
				// check if the node is already visited
				if (!expandedNodes.contains(locationString)) {
					totalValue = childNode.getCostFromRoot() + childNode.getHeuristicValue();
					if (totalValue > threshold) {
						if (nextThreshold == -1) {
							nextThreshold = totalValue;
						} else {
							nextThreshold = nextThreshold < totalValue ? nextThreshold : totalValue;
						}
						if (totalValue < shortestValue) {
							shortestValue = totalValue;
						}
					} else {
						addChild(totalValue, childNode);
					}
				} else {
					childNode = null;
				}
			}
			childNodes.clear();
			if (nodesToExpand.size() != 0) {
				return applyAStarID(nodesToExpand.remove(0), startTime, threshold);
			}
		}
		return nextThreshold;
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
		input.close();
		// create an instance of puzzle with given input and perform bfs
		System.out.println("***************************************************************");
		System.out.println("********************* MISPLACED Heuristic *********************");
		System.out.println("***************************************************************");
		AStarID puzzle = new AStarID(str, Heuristic.MISPLACED);
		puzzle.findAStarIDSolution();
		System.out.println();
		System.out.println();
		System.out.println("***************************************************************");
		System.out.println("********************* MANHATTEN Heuristic *********************");
		System.out.println("***************************************************************");
		AStarID puzzle2 = new AStarID(str, Heuristic.MANHATTEN);
		puzzle2.findAStarIDSolution();
		System.out.println("***************************************************************");

	}

}
