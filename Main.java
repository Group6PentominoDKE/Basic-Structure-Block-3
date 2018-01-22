import java.util.ArrayList;
import java.util.Scanner;
import javafx.stage.Stage;

public class Main {

    private static Pentomino[] parcelRotations;

    private static int[][][] everyShape;

    private static boolean usingPents = false;
    private static ArrayList<Integer> values = new ArrayList<>();

    public int[][][] compute(int[] userSettings, boolean pentMode, String algorithmChoice) {
        int width = userSettings[3];
        int height = userSettings[4];
        int depth = userSettings[5];

        System.out.println("Write letters without spaces:  ");
        String input = null;
        if(pentMode == true) {
          input = "lpt";
        } else {
          input = "abc";
        }
        char[] inputArray = input.toLowerCase().toCharArray();
        // Make the values array start in descending order(so the 1st value is the highest, 2nd value is 2nd highest and etc.)
        
		if (userSettings[0] >= userSettings[1] && userSettings[0] >= userSettings[2]) {
			/*
			values.add(userSettings[0]);
			if (userSettings[1] >= userSettings[2]) {
				values.add(userSettings[1]);
				values.add(userSettings[2]);
			}
			else {
				values.add(userSettings[2]);
				values.add(userSettings[1]);
			}
			*/
			
				inputArray[0] = input.charAt(0);
				if (userSettings[1] >= userSettings[2]) {
					inputArray[1] = input.charAt(1);
					inputArray[2] = input.charAt(2);
				}
				else {
					inputArray[1] = input.charAt(2);
					inputArray[2] = input.charAt(1);
				}
		}
		else if (userSettings[1] > userSettings[0] && userSettings[1] >= userSettings[2]) {
			/*
			values.add(userSettings[1]);
			if (userSettings[0] >= userSettings[2]) {
				values.add(userSettings[0]);
				values.add(userSettings[2]);
			}
			else {
				values.add(userSettings[2]);
				values.add(userSettings[0]);
			}     
			*/
		
				inputArray[0] = input.charAt(1);
				if (userSettings[0] >= userSettings[2]) {
					inputArray[1] = input.charAt(0);
					inputArray[2] = input.charAt(2);
				}
				else {
					inputArray[1] = input.charAt(2);
					inputArray[2] = input.charAt(0);
				}
			}
		
		
		else if (userSettings[2] > userSettings[0] && userSettings[2] > userSettings[1]) {
			/*
			values.add(userSettings[2]);
			if (userSettings[0] >= userSettings[1]) {
				values.add(userSettings[0]);
				values.add(userSettings[1]);
			}
			else {
				values.add(userSettings[1]);
				values.add(userSettings[0]);
			}
			*/
				inputArray[0] = input.charAt(2);
				if (userSettings[0] >= userSettings[1]) {
					inputArray[1] = input.charAt(0);
					inputArray[2] = input.charAt(1);
				}
				else {
					inputArray[1] = input.charAt(1);
					inputArray[2] = input.charAt(0);
				}
			}
		
		parcelRotations = PentominoFactory.createLetters(inputArray);
		
		
		
        values.add(userSettings[0]);
        values.add(userSettings[1]);
        values.add(userSettings[2]);
		
        assignValues();
           // values.get(0);
        everyShape = new int[depth][height][width];
        long startTime = System.nanoTime();
        String command = algorithmChoice;
        Algorithm algorithm;
        System.out.println(algorithmChoice);
        switch (command) {
            case "Greedy":
                algorithm = new Greedy(width, height, depth, values, inputArray);
                break;
            case "BackTracking":
                algorithm = new Backtracking(width, height, depth, values, inputArray);
                break;
            case "Dynamic Programming":
                algorithm = new DynamicProgramming(width, height, depth, values, inputArray);
                break;
            case "Divide and Conquer":
                algorithm = new DivideAndConquer(width, height, depth, values, inputArray);
                break;
            default:
                algorithm = new DivideAndConquer(width, height, depth, values, inputArray);
                break;
        }

        everyShape = algorithm.solve();
        long endTime = System.nanoTime();

        System.out.println("Answer found in " + (endTime - startTime) / 1000000 + " milliseconds");

        if (everyShape != null) {
            for (int i = 0; i < depth; i++) {
                for (int l = 0; l < height; l++) {
                    for (int j = 0; j < width; j++) {
                        System.out.print(everyShape[i][l][j] + " ");
                    }
                    System.out.println();
                }
                System.out.println();
            }
            System.out.println(algorithm.score);
        } else {
            everyShape = new int[1][1][1];
            everyShape[0][0][0] = 215789;
            System.out.println("No possible solution");
        }
        return everyShape;
    }

    private static void assignValues() {
        for (int i = 0; i < parcelRotations.length; i++) {
            if (parcelRotations[i].getType() == 'a' || parcelRotations[i].getType() == 'l') {
                parcelRotations[i].setValue(values.get(0));
            } else if (parcelRotations[i].getType() == 'b' || parcelRotations[i].getType() == 'p') {
                parcelRotations[i].setValue(values.get(1));
            } else if (parcelRotations[i].getType() == 'c' || parcelRotations[i].getType() == 't') {
                parcelRotations[i].setValue(values.get(2));
            }
        }
    }

}
