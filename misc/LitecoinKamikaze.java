import java.util.Random;

/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */

/**
 * @author Eric
 *
 */
public class LitecoinKamikaze {

	private double[] rewardFactor = {1.2, 1.4, 1.6, 2, 2.5, 3, 4, 5, 6};
	private int width = 6;
	private int length = rewardFactor.length;
	private int[][] table;
	private Random r = new Random();
	private int columnToChoose = 0;

	private void generateTable() {
		table = new int[length][width];
		for(int i = 0; i < table.length; i++) {
			table[i][r.nextInt(width)] = -1;
		}
	}
	public void run() {
		
		int totalLength = 0;
		int numTries = 0;
		double startingBalance = 1;
		double betValue = .1;
		double reward = 0;
		int maxLengthToGo = 9;
		int maxNumTries = 100000;
		while(numTries < maxNumTries) {
			generateTable();
			if(startingBalance < betValue)
				break;
			
			startingBalance -= betValue;
			reward = 0;
			for(int i = 0; i < maxLengthToGo; i++) {
				if(table[i][columnToChoose] != -1)
					reward = betValue * rewardFactor[i];
				else {
					reward = 0;
					totalLength += i+1;
					break;
				}
			}
			startingBalance += reward;
			numTries++;
//			System.out.println("Step\t" + numTries + "\tBalance\t" + startingBalance);
		}
		System.out.println(startingBalance);
	}
	public static void main(String[] args) {
		int i = 0;
		while(i < 1000) {
			new LitecoinKamikaze().run();
			i++;
		}
	}
}
