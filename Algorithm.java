import java.util.ArrayList;

/**
 * @author Zhecho Mitev
 * Abstract class that contains abstract method solve and other helpful methods for executing every algorithm
 */


public abstract class Algorithm {

    public abstract int[][][] solve();


    protected Parcel[] parcelRotations;

    protected int width;
    protected int height;
    protected int depth;

    protected ArrayList<Integer> values;

    protected int ID;
    protected boolean solutionFound;

    protected char[][][] cargoSpace;
    protected int[][][] boxOfIDs;

    protected int score;

    /**
     * Constructor that sets the needed environment for every algorithm
     * @param width width of the container
     * @param height height of the container
     * @param depth depth of the container
     * @param values values that are going to be assigned to the parcels
     * @param inputArray character representation of the sorted parcels by value
     */
    public Algorithm(int width, int height, int depth, ArrayList<Integer> values, char[] inputArray)
    {
        this.width = width;
        this.height = height;
        this.depth = depth;

        boxOfIDs = new int[depth][height][width];
        initCargoSpace();

        score = 0;
        solutionFound = false;

        parcelRotations = RotationsFactory.createLetters(inputArray);
        this.values = values;
        assignValues();
    }

    /**
     * initializing all the cells of the cargo with ' '(space)
     */
    private void initCargoSpace() {
        cargoSpace = new char[depth][height][width];
        for (int l = 0; l < depth; l++) {
            for (int j = 0; j < height; j++) {
                for (int i = 0; i < width; i++) {
                    cargoSpace[l][j][i] = ' ';
                }
            }
        }
    }


    /**
     * each parcels receives its value here
     */
    private void assignValues() {
        for (int i = 0; i < parcelRotations.length; i++) {
            if(parcelRotations[i].getType() == 'a' || parcelRotations[i].getType() == 'l')
            {
                parcelRotations[i].setValue(values.get(0));
            }
            else if(parcelRotations[i].getType() == 'b' || parcelRotations[i].getType() == 'p')
            {
                parcelRotations[i].setValue(values.get(1));
            }
            else if(parcelRotations[i].getType() == 'c' || parcelRotations[i].getType() == 't')
            {
                parcelRotations[i].setValue(values.get(2));
            }
        }
    }

    /**
     * The method gets the shape of the current parcel, puts it on the cargoSpace and assign the current cell as filled
     * The method also makes the position of the parcel in boolean array true, so that it cannot be used again
     * @param x coordinate of x axis
     * @param y coordinate of y axis
     * @param z coordinate of z axis
     * @param parcel the current parcel
     */
    protected void putBlockOnTheBoard(int x, int y, int z, Parcel parcel) {
        int xPosition;
        int yPosition;
        int zPosition;

        int planes = parcel.getPositioningInSpace().length;
        int rows = parcel.getPositioningInSpace()[0].length;
        int cols = parcel.getPositioningInSpace()[0][0].length;

        int startX = parcel.xIndexOfStartPoint();
        int startY = parcel.yIndexOfStartPoint();
        int startZ = parcel.zIndexOfStartPoint();

        for (int k = 0; k < planes; k++) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (parcel.getPositioningInSpace()[k][i][j] == 1) {
                        xPosition = x + k - startX;
                        yPosition = y + i - startY;
                        zPosition = z + j - startZ;
                        cargoSpace[xPosition][yPosition][zPosition] = parcel.getType();
                        boxOfIDs[xPosition][yPosition][zPosition] = ID;
                    }
                }

            }
        }
    }

    /**
     * The method gets the shape of the current parcel and only checks if it could fit on the cargoSpace.
     * @param x coordinate of x axis
     * @param y coordinate of y axis
     * @param z coordinate of z axis
     * @param parcel the current parcel
     */
    protected boolean currentFits(int x, int y, int z, Parcel parcel) {
        int xPosition;
        int yPosition;
        int zPosition;

        int planes = parcel.getPositioningInSpace().length;
        int rows = parcel.getPositioningInSpace()[0].length;
        int cols = parcel.getPositioningInSpace()[0][0].length;

        int startX = parcel.xIndexOfStartPoint();
        int startY = parcel.yIndexOfStartPoint();
        int startZ = parcel.zIndexOfStartPoint();

        for (int k = 0; k < planes; k++) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (parcel.getPositioningInSpace()[k][i][j] == 1) {
                        xPosition = x + k - startX;
                        yPosition = y + i - startY;
                        zPosition = z + j - startZ;

                        if (zPosition < 0 || xPosition < 0 || yPosition < 0 ||
                                xPosition >= depth || yPosition >= height || zPosition >= width ||
                                cargoSpace[xPosition][yPosition][zPosition] != ' ')
                        {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    /**
     * The method gets the shape of the current parcel, removes it from the cargoSpace and assign the current cell as empty(not filled)
     * The method also makes the position of the parcel in boolean array false, so that it can be used again
     * @param x coordinate of x axis
     * @param y coordinate of y axis
     * @param z coordinate of z axis
     * @param parcel the current parcel
     */
    protected void removeBlock(int x, int y, int z, Parcel parcel) {
        int xPosition;
        int yPosition;
        int zPosition;

        int planes = parcel.getPositioningInSpace().length;
        int rows = parcel.getPositioningInSpace()[0].length;
        int cols = parcel.getPositioningInSpace()[0][0].length;

        int startX = parcel.xIndexOfStartPoint();
        int startY = parcel.yIndexOfStartPoint();
        int startZ = parcel.zIndexOfStartPoint();

        for (int k = 0; k < planes; k++) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if(parcel.getPositioningInSpace()[k][i][j] == 1) {
                        xPosition = x + k - startX;
                        yPosition = y + i - startY;
                        zPosition = z + j - startZ;;
                        cargoSpace[xPosition][yPosition][zPosition] = ' ';
                    }
                }

            }
        }
    }
}
