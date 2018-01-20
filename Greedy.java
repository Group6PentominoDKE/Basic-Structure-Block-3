import java.util.ArrayList;

public class Greedy extends Algorithm{

    public Greedy(int width, int height, int depth, ArrayList<Integer> values, char[] inputArray)
    {
        super(width,height,depth,values,inputArray);
    }

    @Override
    public int[][][] solve() {
        maximizeGreedy(0,0,0);
        return this.boxOfIDs;
    }


    private void maximizeGreedy(int x, int y, int z) {
        for (Pentomino currentPentomino : parcelRotations) {

            if (currentFits(x, y, z, currentPentomino)) {

                //calculatePosition();
                ID++;
                putBlockOnTheBoard(x, y, z, currentPentomino);

                score+= currentPentomino.getValue();

                boolean freeCellFound = false;

                for (int k = 0; k < depth; k++) { //checking for free cell
                    for (int j = 0; j < height; j++) {
                        for (int i = 0; i < width; i++) {

                            if (cargoSpace[k][j][i] == ' ') {

                                //freeCellFound = true;
                                maximizeGreedy(k, j, i);/// go into the recursion

                                if (solutionFound) //if the condition is true the program will go out of all recursions
                                {
                                    return;
                                }
                                else
                                {
                                    cargoSpace[k][j][i] = 'i';
                                    freeCellFound = false;
                                }

                            }
                        }
                    }
                }

                if (!freeCellFound) {
                    for (int i = 0; i < depth; i++) {
                        for (int l = 0; l < height; l++) {
                            for (int j = 0; j < width; j++) {
                                System.out.print(cargoSpace[i][l][j] + " ");
                            }
                            System.out.println();
                        }
                        System.out.println();
                    }
                    System.out.println();
                    solutionFound = true;
                    return;

                }
            }
        }
    }
}
