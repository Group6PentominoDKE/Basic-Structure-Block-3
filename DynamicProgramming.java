import java.util.ArrayList;
/**
 * Class that implements the Dynamic Programming Algorithm
 * @author Christie Courtnage
 *
 */

public class DynamicProgramming extends Algorithm {

    private int[][][][] pointsArray;
    private Filled[][][][] filledArray;
    final private int[] DIMENSIONSOFFINALBOX = {8,5,33};
    private int ID = 1;

    /**
     * Constructor
     * @param inputArray the types of parcels that will be used i.e a, b, c or l,t,p
     */
    public DynamicProgramming(int width, int height, int depth, ArrayList<Integer> values, char[] inputArray)
    {
        super(width,height,depth,values,inputArray);
        DIMENSIONSOFFINALBOX[0] = width;
        DIMENSIONSOFFINALBOX[1] = height;
        DIMENSIONSOFFINALBOX[2] = depth;

        pointsArray = new int[2][DIMENSIONSOFFINALBOX[2]][DIMENSIONSOFFINALBOX[1]][DIMENSIONSOFFINALBOX[0]];
        filledArray = new Filled[2][DIMENSIONSOFFINALBOX[2]][DIMENSIONSOFFINALBOX[1]][DIMENSIONSOFFINALBOX[0]];
        intializeLevel(pointsArray[0], filledArray[0]);
        intializeLevel(pointsArray[1], filledArray[1]);

        for (int i = 0; i < parcelRotations.length ; i++) {
            for (int z = 0; z< DIMENSIONSOFFINALBOX[2]; z++)
                for (int y = 0; y< DIMENSIONSOFFINALBOX[1]; y++)
                    for (int x = 0; x< DIMENSIONSOFFINALBOX[0]; x++) {
                        String[][][] currentBox = filledArray[1][z][y][x].getFilled();
                        if (boxFits(parcelRotations[i].getStringRepresentation(ID),currentBox)) {
                            String[][][] firstBox = parcelRotations[i].getStringRepresentation(ID);
                            int[] startCoords = {0,0,currentBox.length-firstBox.length};
                            placeSmallerArrayInArray(currentBox, firstBox, startCoords);
                            score = (int)fillBox(currentBox,1, parcelRotations[i], ID);
                            if (pointsArray[0][z][y][x] <= score) {
                                pointsArray[1][z][y][x] = score;
                            } else {
                                pointsArray[1][z][y][x] = pointsArray[0][z][y][x];
                                filledArray[1][z][y][x] = new Filled(filledArray[0][z][y][x].getFilled());
                            }

                        } else {
                            pointsArray[1][z][y][x] = pointsArray[0][z][y][x];
                            filledArray[1][z][y][x] = new Filled(filledArray[0][z][y][x].getFilled());
                        }
                    }
            copyLevel();
            intializeLevel(pointsArray[1], filledArray[1]);
        }
    }
    @Override
    public int[][][] solve() {
        score = getMaxPoints();
        return this.getMaxFilled();
    }




    /**
     * Accessor method for the Last element in the last array which is the maximum points
     * @return the maximum points gotten by the algorithm
     */
    private int getMaxPoints(){
        int z = pointsArray[0].length-1;
        int y = pointsArray[0][0].length-1;
        int x = pointsArray[0][0][0].length-1;
        return pointsArray[0][z][y][x];
    }

    /**
     * Accesor method for the representation of the cargo space with the maximum points. This method converts the unique string identifier for each parcel to a unique integer.
     * @return an integer representation of the cargo space with the maximum points
     */
    private int[][][] getMaxFilled(){
        int depth = filledArray[0].length;
        int height = filledArray[0][0].length;
        int width = filledArray[0][0][0].length;

        String[][][] maxFilled =  filledArray[0][depth-1][height-1][width-1].getFilled();
        int[][][] returnFilled = new int[depth][height][width];

        int ID = 1;

        class Table {
            private String stringID;
            private int intID;

            private Table(String inStr, int inInt) {
                stringID = inStr;
                intID = inInt;
            }

            private String getStringID() {
                return stringID;
            }

            private int getIntID() {
                return intID;
            }

        };

        Table[] table = new Table[300];

        for (int z = 0; z < depth; z++)
            for (int y = 0; y< height; y++)
                for(int x = 0; x < width; x++) {
                    String currentString = maxFilled[z][y][x];
                    if (currentString != "0") {
                        int i = 0;
                        while (table[i] != null && currentString.equals(table[i].getStringID()) == false) {
                            i++;
                        }
                        if (i+1 >= ID) {
                            table[i] = new Table(currentString, ID);
                            ID++;
                        }
                        returnFilled[z][y][x] = table[i].getIntID();
                    } else {
                        returnFilled[z][y][x] = 0;
                    }

                }

        return returnFilled;
    }

    /**
     * Initialised a level of the arrays
     * @param points The points array level that will be initialised
     * @param filled The filled array level that will be initialised
     */
    private void intializeLevel(int[][][] points, Filled[][][] filled){
        for (int z = 0; z< DIMENSIONSOFFINALBOX[2]; z++)
            for (int y = 0; y< DIMENSIONSOFFINALBOX[1]; y++)
                for (int x = 0; x < DIMENSIONSOFFINALBOX[0]; x++) {
                    points[z][y][x] = 0;
                    filled[z][y][x] = new Filled(x,y,z);
                }
    }

    /**
     * Copies the points and filled array level 1 to level 0
     */
    private void copyLevel(){
        for (int z = 0; z < DIMENSIONSOFFINALBOX[2]; z++)
            for (int y = 0; y< DIMENSIONSOFFINALBOX[1]; y++)
                for (int x = 0; x < DIMENSIONSOFFINALBOX[0]; x++) {
                    pointsArray[0][z][y][x] = pointsArray[1][z][y][x];
                    String[][][] original = filledArray[1][z][y][x].getFilled();
                    String[][][] copy = new String[original.length][original[0].length][original[0][0].length];
                    for (int i = 0; i < original.length; i++)
                        for (int j = 0; j < original[0].length; j++) {
                            System.arraycopy(original[i][j], 0, copy[i][j] , 0, original[0][0].length);
                        }
                    filledArray[0][z][y][x] = new Filled(copy);
                }
    }

    /**
     * Test whether the smallBox will fit inside bigBox
     * @param smallBox the possibly smaller box
     * @param bigBox the possibly bigger box
     * @return whether the smallBox will fit inside bigBox
     */
    private boolean boxFits(String[][][] smallBox, String[][][] bigBox) {
        if (smallBox.length > bigBox.length)
            return false;
        if (smallBox[0].length > bigBox[0].length)
            return false;
        if (smallBox[0][0].length > bigBox[0][0].length)
            return false;
        return true;
    }

    /**
     * Find the dimensions and starting coordinates of every possible smaller rectangular prism that will fit inside the cargo space
     * @param inBox the cargo space
     * @return A string array containing every starting coordinates and dimension of every possible empty rectangular prism
     */
    private String[][] findDimensions(String[][][] inBox){
        int height = inBox.length;
        int length = inBox[0].length;
        int width = inBox[0][0].length;
        String[][] tempReturnStringArray = new String[1000][2]; // start coordinates, dimensions
        int counter = 0;
        for (int z = height -1; z >= 0; z--)
            for (int y = 0; y < length; y++)
                for (int x = 0; x < width; x++){
                    if (inBox[z][y][x] == "0"){
                        String[] possibleAddition = new String[2];
                        possibleAddition[0] = x+"x"+y+"x"+z;
						possibleAddition[1] =  findEmptyRectangleX(x, y, z, inBox);
						counter = addToArray(counter, tempReturnStringArray, possibleAddition);
						possibleAddition[1] =  findEmptyRectangleY(x, y, z, inBox);
						counter = addToArray(counter, tempReturnStringArray, possibleAddition);
						possibleAddition[1] =  findEmptyRectangleZ(x, y, z, inBox);
						counter = addToArray(counter, tempReturnStringArray, possibleAddition);
                    }
                }

        String[][] finalReturnStringArray = new String[counter][2];
        for (int i = 0; i < counter; i++) {
            System.arraycopy(tempReturnStringArray[i], 0, finalReturnStringArray[i], 0, 2);
        }
        return finalReturnStringArray;
    }

    /**
	 * Find the dimension of any rectangle from a specific starting point checking x first
	 * @param x the starting x coordinate
	 * @param y the starting y coordinate
	 * @param z the starting z coordinate
	 * @param inBox the current cargo space that is being filled
	 * @return a sting of the dimensions of the empty rectangular prism seperated by "x"
	 */
	private String findEmptyRectangleX(int x, int y, int z, String[][][] inBox){
		int tempX = 0;
		int tempY = 0;
		int tempZ = 0;

		do {
			tempX++;
		} while(x+tempX < inBox[0][0].length && inBox[z][y][x+tempX] == "0");
		
		do {
			tempY++;
		} while(y+tempY < inBox[0].length && inBox[z][y+tempY][x] == "0");
		
		do {
			tempZ++;
		} while(z+tempZ < inBox.length && inBox[z+tempZ][y][x] == "0");
		
		
		for (int z1 = z; z1 < z+tempZ; z1++)
			for (int y1 = y; y1 < y+tempY; y1++)
				for (int x1 = x; x1 < x+tempX; x1++) {
					
					if (x1+1 < inBox[0][0].length && z1 < inBox.length && y1 < inBox[0].length && inBox[z1][y1][x1+1] != "0"){
						tempX = x1-x+1;
					}	


					if (y1+1 < inBox[0].length && x1 < inBox[0][0].length && z1 < inBox.length &&inBox[z1][y1+1][x1] != "0"){
						tempY = y1-y+1;
					}
					
					if (z1+1 < inBox.length && y1 < inBox[0].length && x1 < inBox[0][0].length && inBox[z1+1][y1][x1] != "0"){
						tempZ = z1-z+1;
					}						
				}
		
		return tempX+"x"+tempY+"x"+tempZ;
	} 
	
	/**
	 * Find the dimension of any rectangle from a specific starting point checking Y first
	 * @param x the starting x coordinate
	 * @param y the starting y coordinate
	 * @param z the starting z coordinate
	 * @param inBox the current cargo space that is being filled
	 * @return a sting of the dimensions of the empty rectangular prism seperated by "x"
	 */
	private static String findEmptyRectangleY(int x, int y, int z, String[][][] inBox){
		int tempX = 0;
		int tempY = 0;
		int tempZ = 0;

		do {
			tempX++;
		} while(x+tempX < inBox[0][0].length && inBox[z][y][x+tempX] == "0");
		
		do {
			tempY++;
		} while(y+tempY < inBox[0].length && inBox[z][y+tempY][x] == "0");
		
		do {
			tempZ++;
		} while(z+tempZ < inBox.length && inBox[z+tempZ][y][x] == "0");
		
		
		for (int z1 = z; z1 < z+tempZ; z1++)
			for (int x1 = x; x1 < x+tempX; x1++)			
				for (int y1 = y; y1 < y+tempY; y1++) {
					
					if (y1+1 < inBox[0].length && x1 < inBox[0][0].length && z1 < inBox.length &&inBox[z1][y1+1][x1] != "0"){
						tempY = y1-y+1;
					}
					
					if (x1+1 < inBox[0][0].length && z1 < inBox.length && y1 < inBox[0].length && inBox[z1][y1][x1+1] != "0"){
						tempX = x1-x+1;
					}	
					
					if (z1+1 < inBox.length && y1 < inBox[0].length && x1 < inBox[0][0].length && inBox[z1+1][y1][x1] != "0"){
						tempZ = z1-z+1;
					}						
				}
		
		return tempX+"x"+tempY+"x"+tempZ;
		
	} 
	
	/**
	 * Find the dimension of any rectangle from a specific starting point checking Z first
	 * @param x the starting x coordinate
	 * @param y the starting y coordinate
	 * @param z the starting z coordinate
	 * @param inBox the current cargo space that is being filled
	 * @return a sting of the dimensions of the empty rectangular prism seperated by "x"
	 */
	private static String findEmptyRectangleZ(int x, int y, int z, String[][][] inBox){
		int tempX = 0;
		int tempY = 0;
		int tempZ = 0;

		do {
			tempX++;
		} while(x+tempX < inBox[0][0].length && inBox[z][y][x+tempX] == "0");
		
		do {
			tempY++;
		} while(y+tempY < inBox[0].length && inBox[z][y+tempY][x] == "0");
		
		do {
			tempZ++;
		} while(z+tempZ < inBox.length && inBox[z+tempZ][y][x] == "0");	
		
		for (int x1 = x; x1 < x+tempX; x1++)			
			for (int y1 = y; y1 < y+tempY; y1++) 
				for (int z1 = z; z1 < z+tempZ; z1++){
					
					if (z1+1 < inBox.length && y1 < inBox[0].length && x1 < inBox[0][0].length && inBox[z1+1][y1][x1] != "0"){
						tempZ = z1-z+1;
					}
					
					if (y1+1 < inBox[0].length && x1 < inBox[0][0].length && z1 < inBox.length &&inBox[z1][y1+1][x1] != "0"){
						tempY = y1-y+1;
					}
					
					if (x1+1 < inBox[0][0].length && z1 < inBox.length && y1 < inBox[0].length && inBox[z1][y1][x1+1] != "0"){
						tempX = x1-x+1;
					}						
				}
		
		return tempX+"x"+tempY+"x"+tempZ;
		
	}

    /**
     * Method that either adds to the array, replaces and element in the array or does nothing depending on the size of the potential addition string compared to the other dimensions in the array.
     * i.e it adds to the array if there are no similar dimensions, replaces an element if the potential addition is greater that another dimension or does nothing if the potential addition is smaller than all the other dimensions
     * @param counter the number of non null elements in the array
     * @param array the array of dimensions
     * @param potentialAddition the potential addition to the array
     * @return the number of non null elements in the array after the method has been executed
     */
    private int addToArray(int counter, String[][] array, String[] potentialAddition) {
        if (counter > 0){
            int[] potAdd = splitStringToInt(potentialAddition[1]);
            boolean addOrStop = true;
            for (int i = 0; i < counter; i++) {
                int[] comp1 = splitStringToInt(array[i][1]);
                if (isBiggerBox(comp1,potAdd)){
                    for (int j = 0; j < 2; j++) {
                        array[i][j] = potentialAddition[j];
                    }
                    addOrStop = false;
                    break;
                } else if (isSmallerBox(comp1,potAdd)){
                    addOrStop = false;
                    break;
                }
            }
            if (addOrStop) {
                for (int j = 0; j < 2; j++) {
                    array[counter][j] = potentialAddition[j];
                }
                counter++;
            }
        } else {
            for (int j = 0; j < 2; j++) {
                array[counter][j] = potentialAddition[j];
            }
            counter++;
        }
        return counter;
    }

    /**
     * Tests whether the second box is bigger than the first
     * @param comp1 the first box
     * @param comp2 the second box
     * @return whether the second box is bigger than the first
     */
    private boolean isBiggerBox(int[]comp1, int[] comp2) {
		if (comp1[0] == comp2[0] && comp1[1] == comp2[1] && comp1[2] == comp2[2]) {
			return true;
		}
        if (comp1[0] == comp2[0]) {
            if (comp1[1] < comp2[1] && comp1[2] <= comp2[2]){
                return true;
            } else {
                if (comp1[2] < comp2[2] && comp1[1] == comp2[1]) {
                    return true;
                }
            }
        } else if (comp1[1] == comp2[1]) {
            if (comp1[0] < comp2[0] && comp1[2] <= comp2[2]){
                return true;
            } else {
                if (comp1[2] < comp2[2] && comp1[0] == comp2[0]) {
                    return true;
                }
            }
        } else if (comp1[2] == comp2[2]) {
            if (comp1[0] < comp2[0] && comp1[1] <= comp2[1]){
                return true;
            } else {
                if (comp1[1] < comp2[1] && comp1[0] == comp2[0]) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Tests whether the second box is smaller than the first
     * @param comp1 the first box
     * @param comp2 the second box
     * @return whether the second box is smaller than the first
     */
    private boolean isSmallerBox(int[]comp1, int[] comp2) {
        if (comp1[0] == comp2[0]) {
            if (comp1[1] > comp2[1] && comp1[2] >= comp2[2]){
                return true;
            } else {
                if (comp1[2] > comp2[2] && comp1[1] == comp2[1]) {
                    return true;
                }
            }
        } else if (comp1[1] == comp2[1]) {
            if (comp1[0] > comp2[0] && comp1[2] >= comp2[2]){
                return true;
            } else {
                if (comp1[2] > comp2[2] && comp1[0] == comp2[0]) {
                    return true;
                }
            }
        } else if (comp1[2] == comp2[2]) {
            if (comp1[0] > comp2[0] && comp1[1] >= comp2[1]){
                return true;
            } else {
                if (comp1[1] > comp2[1] && comp1[0] == comp2[0]) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Recursive method that finds the dimension that will yield the maximum points
     * @param coOrds all the possible dimensions
     * @param max the current maximum
     * @param currentLevel the level of the points and filled array
     * @return the position of the maximum yield dimension in the original array anf the points it will yield
     */
    private double[] findMaxPoints(String[][] coOrds, double[] max, int currentLevel){
        //Position , Points
        if (coOrds.length == 0) {
            double[] ret = {0,0};
            return ret;
        }
        if (coOrds.length == 1) {
            int[] coOrd = splitStringToInt(coOrds[0][1]);
            double points = pointsArray[currentLevel][coOrd[2]-1][coOrd[1]-1][coOrd[0]-1];
            if (points > max[1]) {
                max[0] = 0;
                max[1] = points;
            }
            return max;
        } else {
            int[] coOrd = splitStringToInt(coOrds[coOrds.length-1][1]);
            if (coOrd[2]-1 >= 0 && coOrd[1]-1 >= 0 && coOrd[0]-1 >= 0){
                double coOrdPoints = pointsArray[currentLevel][coOrd[2]-1][coOrd[1]-1][coOrd[0]-1];
                if (coOrdPoints > max[1]) {
                    max[0] = coOrds.length-1;
                    max[1] = coOrdPoints;
                }
            }
            String[][] newCoOrds = new String[coOrds.length-1][2];
            System.arraycopy(coOrds, 0, newCoOrds, 0, coOrds.length-1);
            return findMaxPoints(newCoOrds, max, currentLevel);
        }
    }

    /**
     * Splits a string seperated by "x" int an integer array
     * @param coOrds
     * @return and int array of dimensions
     */
    private int[] splitStringToInt(String coOrds) {
        String[] stringCoOrd = coOrds.split("x");
        int[] intCoOrd = new int[3];
        for (int i = 0; i < 3; i++ ) {
            intCoOrd[i] = Integer.parseInt(stringCoOrd[i]);
        }
        return intCoOrd;
    }

    /**
     * Copies a smaller array into a bigger array from a starting point
     * @param bigArray the array that is being copied to
     * @param smallArray the array that is being coppied
     * @param startCoords the position in the big array where the copying will start
     */
    private void placeSmallerArrayInArray(String[][][] bigArray, String[][][] smallArray, int[] startCoords) {
        int height = smallArray.length;
        int length = smallArray[0].length;
        int width = smallArray[0][0].length;

        for (int z = startCoords[2]; z < startCoords[2]+height; z++)
            for (int y = startCoords[1]; y < startCoords[1]+length; y++) {
                System.arraycopy(smallArray[z-startCoords[2]][y-startCoords[1]], 0, bigArray[z][y], startCoords[0], width);
            }
    }

    /**
     * Recursively fills a cargo space with previously calculated points
     * @param box the current cargo space
     * @param currentLevel the current level in the points and filled array
     * @param currentPent the current parcel that is being places
     * @param ID the uniquely identifying number for this iteration
     * @return the number of points the box contains
     */
    private double fillBox(String[][][] box, int currentLevel, Pentomino currentPent, int ID) {
        String[][] newDimensions = findDimensions(box);
        if (newDimensions.length > 0) {
            double[] max = {0,0};
            double[] maxPoints = findMaxPoints(newDimensions,max,currentLevel);
            if (max[1] == 0.0) {
                return currentPent.getValue();
            } else {
                int[] intDimension = splitStringToInt(newDimensions[(int) maxPoints[0]][1]);
                String[][][] maxSmallBox = filledArray[currentLevel][intDimension[2]-1][intDimension[1]-1][intDimension[0]-1].getFilled();
                addIDToBox(maxSmallBox);
                int[] intStartCoords = splitStringToInt(newDimensions[(int) maxPoints[0]][0]);
                placeSmallerArrayInArray(box,maxSmallBox,intStartCoords);
                return maxPoints[1] + fillBox(box,currentLevel, currentPent, ID);
            }
        }else {
            return currentPent.getValue();
        }
    }

    /**
     * Adds the current id to the already existing IDs in the box
     * @param box the current cargo space
     * @param id the uniquely identifying number for this iteration
     */
    private void addIDToBox(String[][][] box){
        for (int z = 0; z < box.length; z++)
            for ( int y = 0; y < box[0].length; y++)
                for (int x = 0; x < box[0][0].length; x++){
                    if (box[z][y][x] != "0") {
                        String addition = box[z][y][x];
                        addition = ID+addition;
                        box[z][y][x] = addition;
                    }
                }
        ID++;
    }

    private void printMatrix(int[][][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                for (int k =  0; k < matrix[0][0].length; k++) {
                    System.out.print(matrix[i][j][k]);
                }
                System.out.println();
            }
            System.out.println();
        }
    }


}
