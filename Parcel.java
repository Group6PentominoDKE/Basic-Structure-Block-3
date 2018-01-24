import javafx.geometry.Point3D;

/**
 A single pentomino object
 @author Zhecho Mitev
 @author Christie Courtnage
 */

public class Parcel {

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
    public Parcel(int[][][] positioningInSpace, char c){
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

    /**
     * Finds where is the starting point(used for the pentomino shapes)
     */
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

    public String[][][] getStringRepresentation(int ID){
        int z = positioningInSpace.length;
        int y = positioningInSpace[0].length;
        int x = positioningInSpace[0][0].length;
        String[][][] tempReturn = new String[z][y][x];

        for (int z1 = 0; z1 < z; z1++)
            for (int y1 = 0; y1 < y; y1++)
                for (int x1 = 0; x1 < x; x1++){
                    if (positioningInSpace[z1][y1][x1] == 1) {
                        tempReturn[z1][y1][x1] = Integer.toString(ID);
                    } else {
                        tempReturn[z1][y1][x1] = "0";
                    }
                }

        return tempReturn;
    }

    /**
     * Return type passed to the constructor.
     * @return Character */
    public char getType(){
        return this.type;
    }

    /**
     Method that determines which z index in the array is considered the starting point
     @return the plane(sheet) index of the first non-empty cell
     */
    public int zIndexOfStartPoint(){
        return (int)startingPoint.getZ();
    }

    /**
     Method that determines which x index in the array is considered the starting point
     @return the row index of the first non-empty cell
     */
    public int xIndexOfStartPoint(){
        return (int)startingPoint.getX();
    }
    /**
     Method that determines which y index in the array is considered the starting point
     @return the column index of the first non-empty cell
     */
    public int yIndexOfStartPoint(){
        return (int)startingPoint.getY();
    }

}
