import javafx.scene.Group;
import javafx.scene.shape.Box;
import javafx.scene.paint.*;
/**
 This Class makes an ordered collection of parcels
 @author Lukas Padolevicius
 */
public class CargoStacker {

    private Group pent;
    private Group cargoSpace;
    private int[][][] array;
    private PentominoTester testerPent;
    private BoxTester testerBox;
    private boolean pentMode;
    /**
     The Constructor connects the Object to the Group taken,
     and then turns the array into a collection of 3D parcels
     @param space the Group where the parcels are built
     @param input integer array containing unique ID's for each parcel
     @param pento boolean indicating if the parcels are pentominoes
     */
    public CargoStacker (Group space, int[][][] input, boolean pento) {
        array = input;
        cargoSpace = space;
        testerPent = new PentominoTester(input);
        testerBox = new BoxTester(input);
        pentMode = pento;
    }
    /**
     Turns the recorded array of parcels into a
     3D representation of the parcels in the attached Group
     */
    public void stack () {
        pent = new Group();
        for(int i=0; i<array.length; i++) {
            for(int j=0; j<array[0].length; j++) {
                for(int k=0; k<array[0][0].length; k++) {
                    if(array[i][j][k] != 0) {
                        if(pentMode == true) {
                            array = testerPent.search(i,j,k,array[i][j][k]);
                            cargoSpace.getChildren().add(testerPent.getPent());
                        } else {
                            array = testerBox.search(i,j,k,array[i][j][k]);
                            cargoSpace.getChildren().add(testerBox.getBox());
                        }
                    }
                }
            }
        }
    }
}
