import javafx.geometry.Point3D;

/**
 A single pentomino object
 */

public class Pentomino {

    private int[][][] positioningInSpace;
    private char type;
    private Point3D startingPoint;
    private double value;

    public void setValue(double value) {
        this.value = value;
    }

    public double getValue() {

        return value;
    }

    /**
     Constructor
     @param positioningInSpace the position of the pentomino in the space
     @param c the type the pentomino represents
     */
    public Pentomino(int[][][] positioningInSpace, char c){
        this.positioningInSpace = positioningInSpace;
        this.type = c;
        setStartingPoint();
    }



    /**
     * Return LetterMatrix passed to the constructor.
     * @return LetterMatrix
     */
    public int[][][] getPositioningInSpace(){
        return this.positioningInSpace;
    }

    private void setStartingPoint() {
        for (int i = 0; i < positioningInSpace.length; i++) {
            for (int j = 0; j < positioningInSpace[0].length; j++) {
                for (int k = 0; k < positioningInSpace[0][0].length; k++) {
                    if(this.positioningInSpace[i][j][k] == 1) {
                        startingPoint = new Point3D(i,j,k);
                        return;
                    }
                }
            }
        }
    }


    /**
     * Return type passed to the constructor.
     * @return Character */
    public char getType(){
        return this.type;
    }

    /**
     Method that determines which index in the array is considered the starting point for placing the pentomino on the board.
     @return the column index of the first cell in the first row of the matrix that is 1 i.e. not empty
     */
    public int zIndexOfStartPoint(){
        return (int)startingPoint.getZ();
    }

    /**
     Method that determines which index in the array is considered the starting point for placing the pentomino on the board.
     @return the column index of the first cell in the first row of the matrix that is 1 i.e. not empty
     */
    public int xIndexOfStartPoint(){
        return (int)startingPoint.getX();
    }
    /**
     Method that determines which index in the array is considered the starting point for placing the pentomino on the board.
     @return the column index of the first cell in the first row of the matrix that is 1 i.e. not empty
     */
    public int yIndexOfStartPoint(){
        return (int)startingPoint.getY();
    }

}
