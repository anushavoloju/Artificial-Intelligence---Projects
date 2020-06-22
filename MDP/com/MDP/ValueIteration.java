/**
 * 
 */
package com.MDP;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Anusha
 *
 */
public class ValueIteration {

	// reward in non-terminal states
	private double reward = 0;

	// discount rate
	private double gamma = 0.9;

	// transition probabilities
	private double probUp = 0.80;
	private double probLeft = 0.10;
	private double probRight = 0.10;
	private double probDown = 0;

	private int N = 10000;

	private double deltaMin = 0.00011;

	private double U[][];
	private double Udash[][];
	private double R[][]; // instantaneous reward
	private char Policy[][]; // policy

	private int rows = 3, cols = 4;

	public static String Input[];

	private void setRows(String rows) {
		this.rows = Integer.parseInt(rows);
	}

	private void setColumns(String cols) {
		this.cols = Integer.parseInt(cols);
	}

	private void initialize() {
		// initialize policy
		this.Policy = new char[rows][cols];

		// initialize U'
		this.Udash = new double[rows][cols];
		int r, c;
		for (r = 0; r < rows; r++) {
			for (c = 0; c < cols; c++) {
				this.Udash[r][c] = 0.00;
			}
		}

		this.U = new double[this.rows][this.cols];

		// initialize R with value as reward for each state
		this.R = new double[this.rows][this.cols];
	}

	private void setWall(int x, int y) {
		this.Policy[this.rows - x][y - 1] = '#';
	}

	private void setTerminalState(int x, int y, int r) {
		this.R[this.rows - x][y - 1] = r;
		if (r > 0) {
			this.Policy[this.rows - x][y - 1] = '+';
		} else {
			this.Policy[this.rows - x][y - 1] = '-';
		}
	}

	private void setReward(double reward) {
		this.reward = reward;
		for (int r = 0; r < this.rows; r++) {
			for (int c = 0; c < this.cols; c++) {
				if (this.Policy[r][c] != '#' && this.Policy[r][c] != '+' && this.Policy[r][c] != '-') {
					this.R[r][c] = reward;
				}
			}
		}
	}

	private void setTransitionProbabilities(double up, double left, double right, double down) {
		this.probUp = up;
		this.probLeft = left;
		this.probRight = right;
		this.probDown = down;
	}

	private void setEpsilon(double epsilon) {
		this.deltaMin = epsilon * (1 - this.gamma) / this.gamma;
	}

	private void setDiscountRate(double gamma) {
		this.gamma = gamma;
	}

	public static void main(String[] args) {

		try {

			File file = new File("./txtfiles/Input.txt");

			BufferedReader br = new BufferedReader(new FileReader(file));
			ValueIteration valueIteration = new ValueIteration();
			String st;
			while ((st = br.readLine()) != null) {
				// System.out.println(st);
				int index = 0;
				if (st.indexOf("#") == 0) {
					continue;
				}
				if (st.contains("size")) {
					index = st.indexOf(":");
					String[] tokens = st.substring(index + 1).trim().replaceAll(" +", " ").split(" ");
					valueIteration.setColumns(tokens[0]);
					valueIteration.setRows(tokens[1]);
					valueIteration.initialize();
				} else if (st.contains("walls")) {
					index = st.indexOf(":");
					String[] tokens = st.substring(index + 1).trim().replaceAll(" +", " ").split(",");
					for (int i = 0; i < tokens.length; i++) {
						String[] wallCoords = tokens[i].trim().split(" ");
						int y = Integer.parseInt(wallCoords[0]);
						int x = Integer.parseInt(wallCoords[1]);
						valueIteration.setWall(x, y);
					}
				} else if (st.contains("terminal_states")) {
					index = st.indexOf(":");
					String[] tokens = st.substring(index + 1).trim().replaceAll(" +", " ").split(",");
					for (int i = 0; i < tokens.length; i++) {
						String[] rewards = tokens[i].trim().split(" ");
						int y = Integer.parseInt(rewards[0]);
						int x = Integer.parseInt(rewards[1]);
						int reward = Integer.parseInt(rewards[2]);
						valueIteration.setTerminalState(x, y, reward);
					}
				} else if (st.contains("reward")) {
					index = st.indexOf(":");
					String[] tokens = st.substring(index + 1).trim().replaceAll(" +", " ").split(",");
					double reward = Double.parseDouble(tokens[0]);
					valueIteration.setReward(reward);
				} else if (st.contains("transition_probabilities")) {
					index = st.indexOf(":");
					String[] tokens = st.substring(index + 1).trim().replaceAll(" +", " ").split(" ");
					valueIteration.setTransitionProbabilities(Double.parseDouble(tokens[0]),
							Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2]),
							Double.parseDouble(tokens[3]));
				} else if (st.contains("discount_rate")) {
					index = st.indexOf(":");
					String[] tokens = st.substring(index + 1).trim().replaceAll(" +", " ").split(",");
					valueIteration.setDiscountRate(Double.parseDouble(tokens[0]));
				} else if (st.contains("epsilon")) {
					index = st.indexOf(":");
					String[] tokens = st.substring(index + 1).trim().replaceAll(" +", " ").split(",");
					valueIteration.setEpsilon(Double.parseDouble(tokens[0]));
				}
			}

			br.close();
			valueIteration.processMDP();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void processMDP() {
		double delta = 0.00;
		int r, c;
		int n = 0;

		do {
			// set U = Udash, then compute changes in Udash using previous value
			// of U.
			CopyUdashToU(Udash, U); // src, dest
			n++;
			delta = 0;
			for (r = 0; r < rows; r++) {
				for (c = 0; c < cols; c++) {
					updateUdash(r, c);
					double diff = Math.abs(Udash[r][c] - U[r][c]);
					if (diff > delta)
						delta = diff;
				}
			}

			System.out.println("\n Iteration: " + n + "\n");
			for (r = 0; r < rows; r++) {
				for (c = 0; c < cols; c++) {
					if (Policy[r][c] == '#') {
						System.out.printf("None\t");
					} else {
						System.out.printf("% .2f\t", U[r][c]);
					}
				}
				System.out.print("\n");
			}
		} while (delta > deltaMin && n < N);

		System.out.println("\nBest policy:\n");
		for (r = 0; r < rows; r++) {
			for (c = 0; c < cols; c++) {
				System.out.print(Policy[r][c] + "   ");
			}
			System.out.print("\n");
		}
	}

	public void updateUdash(int r, int c) {
		// IMPORTANT: this modifies the value of Up, using values in U.

		double expValue[] = new double[4]; // 4 actions

		// If at a sink state or unreachable state, use that value
		if (Policy[r][c] == '#' || Policy[r][c] == '+' || Policy[r][c] == '-') {
			Udash[r][c] = R[r][c];
		} else {
			expValue[0] = moveUp(r, c) * probUp + moveLeft(r, c) * probLeft + moveRight(r, c) * probRight;
			expValue[1] = moveDown(r, c) * probUp + moveLeft(r, c) * probRight + moveRight(r, c) * probLeft;
			expValue[2] = moveLeft(r, c) * probUp + moveDown(r, c) * probLeft + moveUp(r, c) * probRight;
			expValue[3] = moveRight(r, c) * probUp + moveDown(r, c) * probRight + moveUp(r, c) * probLeft;

			int best = maxValue(expValue);

			Udash[r][c] = R[r][c] + gamma * expValue[best];

			// update policy
			Policy[r][c] = (best == 0 ? 'U' : (best == 1 ? 'D' : (best == 2 ? 'L' : 'R')));
		}
	}

	public int maxValue(double expValue[]) {
		int index = 0;
		for (int i = 1; i < expValue.length; i++)
			index = (expValue[index] > expValue[i]) ? index : i;
		return index;
	}

	public double moveUp(int r, int c) {
		// can't go up if at row 0 or if in cell (2,1)
		if ((r == 0))
			return U[r][c];
		else if (Policy[r - 1][c] == '#') {
			return U[r][c];
		}
		return U[r - 1][c];
	}

	public double moveDown(int r, int c) {
		// can't go down if at row 2 or if in cell (0,1)
		if ((r == rows - 1))
			return U[r][c];
		else if (Policy[r + 1][c] == '#') {
			return U[r][c];
		}
		return U[r + 1][c];
	}

	public double moveLeft(int r, int c) {
		// can't go left if at col 0 or if in cell (1,2)
		if ((c == 0))
			return U[r][c];
		else if (Policy[r][c - 1] == '#') {
			return U[r][c];
		}
		return U[r][c - 1];
	}

	public double moveRight(int r, int c) {
		// can't go right if at col 3 or if in cell (1,0)
		if ((c == cols - 1))
			return U[r][c];
		else if (Policy[r][c + 1] == '#') {
			return U[r][c];
		}
		return U[r][c + 1];
	}

	public void CopyUdashToU(double[][] Udash, double[][] U) {
		for (int x = 0; x < Udash.length; x++) {
			for (int y = 0; y < Udash[x].length; y++) {
				U[x][y] = Udash[x][y];
			}
		}
	}

}
