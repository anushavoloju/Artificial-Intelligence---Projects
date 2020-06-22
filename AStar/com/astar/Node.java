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
import java.util.List;

public class Node {
	private List<Node> childNodes;
	private int[][] locations;
	private int xPos;
	private int yPos;
	private int maxX;
	private int maxY;
	private String locationString;
	private String directionFromParent;
	private Node parentNode;
	private int costFromRoot;
	private int heuristicValue;
	private Heuristic heuristicType;

	// constructor to set the required variables for a node
	public Node(int[][] locations, int xPos, int yPos, int maxX, int maxY, String directionFromParent, Node parentNode,
			Heuristic heuristicType) {
		this.locations = locations;
		this.xPos = xPos;
		this.yPos = yPos;
		this.maxX = maxX;
		this.maxY = maxY;
		this.directionFromParent = directionFromParent;
		this.parentNode = parentNode;
		this.heuristicType = heuristicType;
		if (parentNode == null) {
			costFromRoot = 0;
		}
		setLocationString();
		setPathValues(heuristicType);
	}

	// to generate the location string for each node
	private void setLocationString() {
		StringBuilder sBuider = new StringBuilder();
		for (int i = 0; i < maxY; i++) {
			for (int j = 0; j < maxX; j++) {
				sBuider.append("" + this.locations[i][j] + ",");
			}
		}
		this.locationString = sBuider.toString();
	}

	// to get the child nodes of a given node
	public List<Node> getChildNodes() {
		if (this.childNodes == null) {
			this.childNodes = new ArrayList<>();
		}

		// up child
		if ((yPos - 1) >= 0) {
			int[][] updatedLocations = getChangedLocations(xPos, yPos - 1);
			childNodes.add(new Node(updatedLocations, xPos, yPos - 1, maxX, maxY, "U", this, heuristicType));
		}

		// down child
		if ((yPos + 1) < maxY) {
			int[][] updatedLocations = getChangedLocations(xPos, yPos + 1);
			childNodes.add(new Node(updatedLocations, xPos, yPos + 1, maxX, maxY, "D", this, heuristicType));
		}

		// left child
		if ((xPos - 1) >= 0) {
			int[][] updatedLocations = getChangedLocations(xPos - 1, yPos);
			childNodes.add(new Node(updatedLocations, xPos - 1, yPos, maxX, maxY, "L", this, heuristicType));
		}

		// right child
		if ((xPos + 1) < maxX) {
			int[][] updatedLocations = getChangedLocations(xPos + 1, yPos);
			childNodes.add(new Node(updatedLocations, xPos + 1, yPos, maxX, maxY, "R", this, heuristicType));
		}

		return this.childNodes;
	}

	// update the locations of the child nodes when the 0 or empty space is
	// moved
	private int[][] getChangedLocations(int x, int y) {
		int temp = this.locations[y][x];
		int[][] updatedLocations = new int[maxY][maxX];
		for (int i = 0; i < maxY; i++) {
			updatedLocations[i] = this.locations[i].clone();
		}
		updatedLocations[y][x] = 0;
		updatedLocations[yPos][xPos] = temp;
		return updatedLocations;
	}

	public int[][] getLocations() {
		return this.locations;
	}

	public String getLocationString() {
		return this.locationString;
	}

	public String getPathFromParentNode() {
		return this.directionFromParent;
	}

	public Node getParentNode() {
		return this.parentNode;
	}

	/**
	 * @return the expectedPathValue
	 */
	public int getCostFromRoot() {
		return costFromRoot;
	}

	/**
	 * @param expectedPathValue
	 *            the expectedPathValue to set
	 */
	private void setPathValues(Heuristic heuristicType) {
		if (this.parentNode == null) {
			this.costFromRoot = 0;
		} else {
			this.costFromRoot = this.parentNode.getCostFromRoot() + 1;
		}

		switch (heuristicType) {
		case MISPLACED:
			this.heuristicValue = Util.getMispalcedHeuristic(locations, maxX, maxY);
			break;
		case MANHATTEN:
			this.heuristicValue = Util.getManhattenHeuristic(locations, maxX, maxY);
			break;
		default:
			break;
		}

	}

	/**
	 * @return the heuristicValue
	 */
	public int getHeuristicValue() {
		return heuristicValue;
	}

}
