package com.astar;

/**
 * @author Anusha Voloju
 * UID : 677775723
 * Net Id : avoloj2
 * University of Illinois, Chicago
 * CS 411, Fall 2018
 * Homework 6
 *
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * @author Anusha
 *
 */
public class AStar {

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

	private Map<Integer, List<Node>> nodesToExpand = new HashMap<>();

	private Node solutionNode;

	private Integer shortestValue = 0;

	// Constructor to initialize the input and output nodes
	public AStar(String inputString, String expectedOutput, Heuristic heuristicType) {
		this.inputString = inputString;
		this.expectedOutput = expectedOutput;
		this.inputNode = new Node(convertToIntMatrix(this.inputString), xPos, yPos, 4, 4, null, null, heuristicType);
		this.outputNode = new Node(convertToIntMatrix(this.expectedOutput), xPos, yPos, 4, 4, null, null,
				heuristicType);
	}

	public AStar(String inputString, Heuristic heuristicType) {
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
	public void findAStarSolution() {

		long startTime = System.currentTimeMillis();

		try {
			shortestValue = this.inputNode.getHeuristicValue();
			this.applyAStar(this.inputNode, startTime);
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

	// perform AStar logic
	private void applyAStar(Node currentNode, long startTime) {
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
					Integer totalValue = childNode.getCostFromRoot() + childNode.getHeuristicValue();
					List<Node> nodesList = nodesToExpand.get(totalValue);
					if (nodesList == null) {
						nodesList = new ArrayList<Node>();
						nodesList.add(childNode);
						nodesToExpand.put(totalValue, nodesList);
					} else {
						nodesList.add(childNode);
					}

					if (totalValue < shortestValue) {
						shortestValue = totalValue;
					}
				}
			}
			childNodes.clear();
			if (nodesToExpand.size() != 0) {
				List<Node> nodesList = nodesToExpand.get(shortestValue);
				while ((nodesList == null || nodesList.isEmpty()) && nodesToExpand.size() != 0) {
					if (nodesList != null && nodesList.isEmpty()) {
						nodesToExpand.remove(shortestValue);
						nodesList.clear();
					}
					shortestValue++;
					nodesList = nodesToExpand.get(shortestValue);
				}

				if (nodesList == null || nodesList.isEmpty()) {
					return;
				}
				applyAStar(nodesList.remove(0), startTime);
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
		input.close();
		// create an instance of puzzle with given input and perform heuristic
		System.out.println("********************* MISPLACED Heuristic *********************");
		AStar puzzle = new AStar(str, Heuristic.MISPLACED);
		puzzle.findAStarSolution();
		System.out.println("********************* MANHATTEN Heuristic *********************");
		AStar puzzle2 = new AStar(str, Heuristic.MANHATTEN);
		puzzle2.findAStarSolution();

	}

}
