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

public class Util {
	public static int getManhattenHeuristic(int[][] locations, int maxX, int maxY) {
		int heuristic = 0;
		for(int x = 0; x < maxX; x++) {
			for(int y = 0; y < maxY; y++) {
				int value = x * maxX + y + 1;
				if (value == 16) {
					continue;
				}
				if (locations[x][y] != value) {
					heuristic += getManhattenDistance(x, y, locations[x][y], maxY);
				}
			}
		}
		return heuristic;
	}
	
	public static int getMispalcedHeuristic(int[][] locations, int maxX, int maxY) {
		int heuristic = 0;
		for(int x = 0; x < maxX; x++) {
			for(int y = 0; y < maxY; y++) {
				int value = x * maxX + y + 1;
				if (value == 16) {
					continue;
				}
				if (locations[x][y] != value) {
					heuristic ++;
				}
			}
		}
		return heuristic;
	}

	private static int getManhattenDistance(int x, int y, int value, int maxY) {
		int actualX = (value - 1) / maxY;
		int actualY = (value - 1) % maxY;
		
		return mod(actualX - x) + mod(actualY - y);
	}

	private static int mod(int value) {
		return value > 0 ? value : 0 - value;
	}
}
