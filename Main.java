import java.util.ArrayList;

/**
 * @author Zhecho Mitev
 * @author Kaspar Kallast
 * @author Lukas Padolevicius
 */

public class Main {

    private static ArrayList<Integer> values = new ArrayList<>();

    /**
     * The main logic that find the algorithm and executes it
     * @param userSettings dimensions of the container and values of the boxes
     * @param pentMode true if TPL, false if ABC
     * @param algorithmChoice type of the algoirthm
     * @return box of IDs that would be converted into 3D box using JavFX
     */
    public int[][][] compute(int[] userSettings, boolean pentMode, String algorithmChoice) {
        int depth = userSettings[5];
        int width = userSettings[3];
        int height = userSettings[4];

        String input;
        if(pentMode) {
            input = "lpt";
        } else {
            input = "abc";
        }
        char[] inputArray = input.toLowerCase().toCharArray();
        // Make the values array start in descending order(so the 1st value is the highest, 2nd value is 2nd highest and etc.)
        sortParcels(userSettings, input, inputArray);

        values.add(userSettings[0]);
        values.add(userSettings[1]);
        values.add(userSettings[2]);

        long startTime = System.nanoTime();

        Algorithm algorithm;
        switch (algorithmChoice) {
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

        int[][][] bestAnswer = algorithm.solve();
        long endTime = System.nanoTime();

        System.out.println("Answer found in " + (endTime - startTime) / 1000000 + " milliseconds");

        if (bestAnswer != null) {
            for (int i = 0; i < depth; i++) {
                for (int l = 0; l < height; l++) {
                    for (int j = 0; j < width; j++) {
                        System.out.print(bestAnswer[i][l][j] + " ");
                    }
                    System.out.println();
                }
                System.out.println();
            }
            System.out.println(algorithm.score);
        } else {
            bestAnswer = new int[1][1][1];
            bestAnswer[0][0][0] = 215789;
            System.out.println("No possible solution");
        }
        return bestAnswer;
    }

    /**
     * sorting parcels by their value
     * @param userSettings contains the values of the boxes
     * @param input the parcels not sorted
     * @param inputArray array containing the sorted parcels after execution of the method
     */
    private void sortParcels(int[] userSettings, String input, char[] inputArray) {
        if (userSettings[0] >= userSettings[1] && userSettings[0] >= userSettings[2]) {
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
    }

}
