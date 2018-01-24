import java.util.Arrays;

/**
 Creates an array of parcels
 @author Zhecho Mitev
 @author Christie Courtnage
 */

public class RotationsFactory {


    public static final int ALL_POSSIBLE_ROTATIONS = 24;
    public static final int ALL_POSSIBLE_FIGURES = 120;
    /**
     *	Constructor
     *	Creates an array of pentomino objects each with a unique matrix
     *	@param inputLetters A char array of the pentominoes that should be represented
     *	@return An array of unique pentomino objects
     */
    public static Parcel[] createLetters(char[] inputLetters){

        int[][][] baseRotation; //Original form of letter
        int count = 0; // Counter for all unique orientations of the input pentominoes

        Parcel[] AllVar = new Parcel[ALL_POSSIBLE_FIGURES];
        Parcel[] currentParcel = new Parcel[ALL_POSSIBLE_ROTATIONS];// array for all rotations and flips of the pentomino

        if(Arrays.toString(inputLetters).contains("a") || Arrays.toString(inputLetters).contains("b") || Arrays.toString(inputLetters).contains("c")) {
            for (int i = 0; i < inputLetters.length; i++) {

                int currentCounter = 0;
                baseRotation = createPemtomino(inputLetters[i]);

                currentParcel[currentCounter] = new Parcel(baseRotation, inputLetters[i]);
                currentCounter++;
                if (baseRotation.length == baseRotation[0].length && baseRotation[0].length == baseRotation[0][0].length) {
                    //do nothing because there just 1 rotation
                } else if (baseRotation.length == baseRotation[0].length ||
                        baseRotation[0].length == baseRotation[0][0].length ||
                        baseRotation.length == baseRotation[0][0].length) {
                    for (int j = 1; j < 3; j++) {
                        baseRotation = rotate3D(baseRotation);
                        currentParcel[j] = new Parcel(baseRotation, inputLetters[i]);
                    }
                    currentCounter = 3;
                } else {
                    for (int j = 1; j < 3; j++) {
                        baseRotation = rotate3D(baseRotation);
                        currentParcel[j] = new Parcel(baseRotation, inputLetters[i]);
                        currentCounter++;
                    }
                    baseRotation = rotate2D(baseRotation);
                    for (int j = 3; j < 6; j++) {
                        currentParcel[j] = new Parcel(baseRotation, inputLetters[i]);
                        currentCounter++;
                        baseRotation = rotate3D(baseRotation);
                    }
                    currentCounter = 6;
                }
                for (int k = 0; k < currentCounter; k++) {
                    AllVar[count] = currentParcel[k];
                    currentParcel[k] = null;
                    count++;
                }
            }
            return Arrays.copyOf(AllVar, count);
        }

        for (int i = 0; i < inputLetters.length; i++){//for every char
            baseRotation = createPemtomino(inputLetters[i]);
            int currentCounter = 0;

            for (int j = 0; j < 4; j++){
                if (isAllDifferent(baseRotation, currentParcel)){
                    currentParcel[currentCounter] = new Parcel(baseRotation,inputLetters[i]);
                    currentCounter++;
                }
                baseRotation = rotateXy(baseRotation);
            }

            if(isDifferent(flipXyPlane(baseRotation),baseRotation)){

                baseRotation = flipXyPlane(baseRotation);

                for (int j = 0; j < 4; j++){
                    if (isAllDifferent(baseRotation, currentParcel)){
                        currentParcel[currentCounter] = new Parcel(baseRotation,inputLetters[i]);
                        currentCounter++;
                    }
                    baseRotation = rotateXy(baseRotation);
                }
            }

            baseRotation = rotate3D(baseRotation);

            for (int j = 0; j < 4; j++){
                currentParcel[currentCounter] = new Parcel(baseRotation,inputLetters[i]);
                currentCounter++;
                baseRotation = rotateZy(baseRotation);
            }

            if(isDifferent(flipZyPlane(baseRotation),baseRotation)){

                baseRotation = flipZyPlane(baseRotation);

                for (int j = 0; j < 4; j++){
                    currentParcel[currentCounter] = new Parcel(baseRotation,inputLetters[i]);
                    currentCounter++;

                    baseRotation = rotateZy(baseRotation);
                }
            }

            baseRotation = rotate3D(baseRotation);

            for (int j = 0; j < 4; j++){
                currentParcel[currentCounter] = new Parcel(baseRotation,inputLetters[i]);
                currentCounter++;

                baseRotation = rotateXz(baseRotation);
            }

            if(isDifferent(flipXzPlane(baseRotation),baseRotation)){

                baseRotation = flipXzPlane(baseRotation);

                for (int j = 0; j < 4; j++){
                    currentParcel[currentCounter] = new Parcel(baseRotation,inputLetters[i]);
                    currentCounter++;

                    baseRotation = rotateXz(baseRotation);
                }
            }

            for (int k = 0; k < currentCounter; k++){
                AllVar[count] = currentParcel[k];
                currentParcel[k] = null;
                count++;
            }
        }
        return Arrays.copyOf(AllVar, count);
    }


    /**
     *	Creates a matrix that corresponds to which pentomino it should represent
     *	@param letter the char that the matrix must represent
     *	@return A integer matrix that represents the Char input where 1 represents a filled block and 0 represents an empty block
     */
    private static int[][][] createPemtomino(char letter){
        int[][][] result;
        if (letter == 'p')
            result =  new int[][][]{{{1,1},{1,1},{1,0}}};//P
        else if (letter == 't')
            result =  new int[][][]{{{1,1,1},{0,1,0},{0,1,0}}};//T
        else if (letter == 'c')
        {
            result =  new int[3][3][3];
            fill(result);//Z
        }
        else if (letter == 'a')
        {
            result =  new int[2][2][4];
            fill(result);
        }
        //U
        else if (letter == 'b')
        {
            result =  new int[2][3][4];
            fill(result);
        }
        else if(letter == 'l')
            result =  new int[][][]{{{0,0,0,1},{1,1,1,1}}};//L
        else result = null;

        return result;
    }

    private static void fill(int[][][] result) {
        for (int l = 0; l < result.length; l++) {
            for (int j = 0; j < result[0].length; j++) {
                for (int i = 0; i < result[0][0].length; i++) {
                    result[l][j][i] = 1;
                }
            }
        }
    }

    /**
     *	Creates a mirror image
     *	@param shape The original shape
     *	@return A mirror image of the of the front plane, mirrored on a Xy axis
     */
    private static int[][][] flipXyPlane(int[][][] shape){

        int height = shape[0].length;
        int width = shape[0][0].length;
        int[][][] newShape = new int[1][height][width];

        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                newShape[0][i][width-j-1] = shape[0][i][j];
            }
        }


        return newShape;
    }

    /**
     *	Creates a mirror image
     *	@param shape The original shape
     *	@return A mirror image of the of the front plane, mirrored on a Xz axis
     */
    private static int[][][] flipXzPlane(int[][][] shape){

        int height = shape[0].length;
        int planes = shape.length;
        int[][][] newShape = new int[planes][height][1];

        for (int i = 0; i < planes; i++){
            for (int j = 0; j < height; j++){
                newShape[i][height-j-1][0] = shape[i][j][0];
            }
        }


        return newShape;
    }

    /**
     *	Creates a mirror image
     *	@param shape The original shape
     *	@return A mirror image of the of the front plane, mirrored on a Zy axis
     */
    private static int[][][] flipZyPlane(int[][][] shape){

        int planes = shape.length;
        int width = shape[0][0].length;
        int[][][] newShape = new int[planes][1][width];

        for (int i = 0; i < planes; i++){
            for (int j = 0; j < width; j++){
                newShape[i][0][width-j-1] = shape[i][0][j];
            }
        }


        return newShape;
    }

    /**
     *	Creates a copy of an inputted int matrix rotated 90degrees clockwise by the X-Y axis
     *	@param shape The original shape
     *	@return A 3D array that is rotated 90 degrees clockwise by the X-Y axis from the original shape
     */
    private static int[][][] rotateXy(int[][][] shape){
        int height = shape[0].length;
        int width = shape[0][0].length;
        int[][][] newShape = new int[1][width][height];
        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                newShape[0][j][height-1-i] = shape[0][i][j];
            }
        }
        return newShape;
    }

    /**
     *	Creates a copy of an inputted int matrix rotated 90degrees clockwise by the Y-Z axis
     *	@param shape The original shape
     *	@return A 3D array that is rotated 90 degrees clockwise by the Y-Z axis from the original shape
     */
    private static int[][][] rotateZy(int[][][] shape){
        int planes = shape.length;
        int width = shape[0][0].length;
        int[][][] newShape = new int[width][1][planes];
        for (int i = 0; i < planes; i++){
            for (int j = 0; j < width; j++){
                newShape[j][0][planes-1-i] = shape[i][0][j];
            }
        }
        return newShape;
    }
    /**
     *	Creates a copy of an inputted int matrix rotated 90degrees clockwise by the X-Z axis
     *	@param shape The original shape
     *	@return A 3D array that is rotated 90 degrees clockwise by the X-Z axis from the original shape
     */
    private static int[][][] rotateXz(int[][][] shape){
        int height = shape[0].length;
        int planes = shape.length;
        int[][][] newShape = new int[height][planes][1];
        for (int i = 0; i < planes; i++){
            for (int j = 0; j < height; j++){
                newShape[j][planes-1-i][0] = shape[i][j][0];
            }
        }
        return newShape;
    }

    /**
     * Switches the dimensions of the box
     * @param preRotatedBox the box that should be rotated
     * @return a box with the new switched dimensions
     */
    private static int[][][] rotate3D(int[][][] preRotatedBox) {
        int depth = preRotatedBox.length;
        int height = preRotatedBox[0].length;
        int width = preRotatedBox[0][0].length;
        int[][][] newMatrix = new int[width][depth][height];
        for (int i = 0; i < depth; i++) {
            for (int j = 0; j < height; j++) {
                for (int k = 0; k < width; k++) {
                    newMatrix[k][i][j] = preRotatedBox[i][j][k];
                }
            }
        }

        return newMatrix;
    }

    /**
     * Switches the dimensions of the box
     * @param preRotatedBox the box that should be rotated
     * @return a box with the new switched dimensions
     */
    private static int[][][] rotate2D(int[][][] preRotatedBox) {
        int depth = preRotatedBox.length;
        int height = preRotatedBox[0].length;
        int width = preRotatedBox[0][0].length;
        int[][][] newMatrix = new int[width][height][depth];
        for (int i = 0; i < depth; i++) {
            for (int j = 0; j < height; j++) {
                for (int k = 0; k < width; k++) {
                    newMatrix[k][j][i] = preRotatedBox[i][j][k];
                }
            }
        }

        return newMatrix;
    }

    /**
     *	Tests whether an inputted matrix is different to every pentomino in an inputted array of pentominoes
     *	@param Mat The matrix that is being tested
     *	@param Arr An array of pentominoes
     *	@return Whether the inputted matrix is different from all the other pentominoes
     */
    private static boolean isAllDifferent(int[][][] Mat, Parcel[] Arr){
        int i = 0;
        while (Arr[i] != null){
            if (isDifferent(Mat,Arr[i].getPositioningInSpace()) == false)
                return false;
            i++;
        }
        return true;
    }


    /**
     *	Tests whether an inputted matrix is different to another inputted matrix
     *	@param Mat1 The first matrix that is being tested
     *	@param Mat2 The second matrix that is being tested
     *	@return Whether the two inputted arrays are different
     */
    private static boolean isDifferent(int[][][] Mat1, int[][][] Mat2){
        if (Mat1[0].length != Mat2[0].length || Mat1[0][0].length != Mat2[0][0].length)
            return true;
        else {
            for (int i = 0; i < Mat1[0].length; i++)
                for (int j = 0; j < Mat1[0][0].length; j++){
                    if (Mat1[0][i][j] != Mat2[0][i][j])
                        return true;
                }
        }
        return false;
    }
}
