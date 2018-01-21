import javafx.application.Application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Main {

    private static Pentomino[] parcelRotations;

    private static int[][][] everyShape;

    private static boolean usingPents = false;
    private static ArrayList<Integer> values = new ArrayList<>();

    public static void main(String[] args) {

        //Application.launch(Input.class, args);
        /*System.out.println("gufsd");
        ArrayList<Double> dimensions = new ArrayList<>();
        String thisLine;
        String algorithmCommand = "";
        try
        {
            FileReader fR = new FileReader("values.txt");
            BufferedReader br = new BufferedReader(fR);
            int lineCounter = 0;
            while ((thisLine = br.readLine()) != null) {
                if(lineCounter < 3) {
                    values.add(Integer.parseInt(thisLine));
                }
                else if(lineCounter == 3){
                    algorithmCommand = thisLine;
                }
                else if(lineCounter == 4)
                {
                    usingPents = Objects.equals(thisLine, "LPT");
                }
                else
                {
                    dimensions.add(Double.parseDouble(thisLine));
                }
                lineCounter++;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        System.out.println(algorithmCommand);*/
        //Start input
        Scanner sc = new Scanner(System.in);
        System.out.println("Width, height and depth of the cargoSpace: ");
        String boardSize = sc.nextLine();
        String[] dimension = boardSize.split(" ");

        int width = Integer.parseInt(dimension[0]);
        int height = Integer.parseInt(dimension[1]);
        int depth = Integer.parseInt(dimension[2]);

        /*int width = (int)(dimensions.get(0)*2);
        int height = (int)(dimensions.get(1)*2);
        int depth = (int)(dimensions.get(2)*2);*/

        System.out.println("Write letters without spaces:  ");
        String input = sc.nextLine();

        char[] inputArray = input.toLowerCase().toCharArray();//usingPents ? new char[] {'t','p','l'} : new char[] {'a','b','c'};

        parcelRotations = PentominoFactory.createLetters(inputArray);

        values.add(3);
        values.add(4);
        values.add(5);
        assignValues();

        everyShape = new int[depth][height][width];

       /* for(Pentomino currentPentomino: parcelRotations)
            System.out.println(Arrays.deepToString(currentPentomino.getPositioningInSpace()));
        System.out.println(parcelRotations.length);*/

        long startTime = System.nanoTime();

        Algorithm algorithm;
        String algorithmCommand = "Greedy";
        switch (algorithmCommand) {
            case "Greedy":
                algorithm = new Greedy(width, height, depth, values, inputArray);
                break;
            case "Backtracking":
                algorithm = new Backtracking(width, height, depth, values, inputArray);
                break;
            case "Dynamic Programming":
                algorithm = new DynamicProgramming(width, height, depth, values, inputArray);
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

            Truck.insertArray(everyShape, usingPents);
            Truck.launch(Truck.class, args);

        } else {
            System.out.println("No possible solution");
        }


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