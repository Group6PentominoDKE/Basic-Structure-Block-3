import javafx.scene.shape.Box;
import javafx.scene.Group;
import javafx.scene.paint.*;
/**
 This Class builds the box shaped parcels one by one and attaches them to the Object
 */
public class BoxTester {

    private static final int RESOLUTION = 50;
    private int[][][] array;
    private Group parcel;
    /**
     Constructs the Object for a specific array
     */
    public BoxTester (int[][][] inArray) {
        array = inArray;
    }

    /**
     Finds the dimensions of a single parcel
     @param i searched cell's coordinate in the array's first dimension
     @param j searched cell's coordinate in the array's second dimension
     @param k searched cell's coordinate in the array's third dimension
     @param ID number of the parcel
     @return gives back the array with the parcels array cells cleared
     */
    public int[][][] search (int i, int j, int k, int ID) {
        int TEMPL = 0;
        int TEMPH = 0;
        int TEMPW = 0;
        for(int a=i; a<array.length && array[a][j][k] == ID; a++) {
            TEMPL++;
        }
        for(int b=j; b<array[0].length && array[i][b][k] == ID; b++) {
            TEMPH++;
        }
        for(int c=k; c<array[0][0].length && array[i][j][c] == ID; c++) {
            TEMPW++;
        }
        build(TEMPL, TEMPH, TEMPW, i, j, k);
        for(int a=i; a-i < TEMPL; a++) {
            for(int b=j; b-j < TEMPH; b++) {
                for(int c=k; c-k < TEMPW; c++) {
                    array[a][b][c] = 0;
                }
            }
        }
        return array;
    }
    /**
     Builds and attaches a 3D shape of a random color of the searched parcel to this Object,
     then moves it into its proper place in the cargo space
     @param i searched cell's coordinate in the array's first dimension
     @param j searched cell's coordinate in the array's second dimension
     @param k searched cell's coordinate in the array's third dimension
     @param x length of the parcel
     @param y height of the parcel
     @param z width of the parcel
     */
    public void build(int x, int y, int z, int i, int j, int k) {
        PhongMaterial material = new PhongMaterial(new Color(Math.random(), Math.random(), Math.random(),1.0));
        parcel = new Group();
        for(int a=0; a < x; a++) {
            for(int b=0; b < y; b++) {
                for(int c=0; c < z; c++) {
                    Box cube = new Box(RESOLUTION, RESOLUTION, RESOLUTION);
                    cube.setMaterial(material);
                    cube.setTranslateX(a*RESOLUTION);
                    cube.setTranslateY(b*RESOLUTION);
                    cube.setTranslateZ(c*RESOLUTION);
                    parcel.getChildren().add(cube);
                }
            }
        }
        parcel.setTranslateX(i*RESOLUTION);
        parcel.setTranslateY(j*RESOLUTION);
        parcel.setTranslateZ(k*RESOLUTION);
    }
    /**
     Returns the 3D parcel currently attached to this Object
     @return the box parcel
     */
    public Group getBox() {
        return parcel;
    }
}