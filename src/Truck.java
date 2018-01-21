import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.PerspectiveCamera;
import javafx.scene.transform.Rotate;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 This Class is an application which displays
 a rotateable 3D representation of a 3-Dimensional Array
 */
public class Truck extends Application {

    private static final int WINDOW_LENGTH = 1500;
    private static final int WINDOW_HEIGHT = 700;
    private static final int CameraViewDistance = 700;
    private static final int RotateAngle = 5;
    private static final int RESOLUTION = 50;
    private static int[][][] array;
    private static boolean pentMode;
    /**
     Inputs the array to be represented and
     whether it will use Box parcels or pentomino parcels
     @param input integer array containing unique ID's for each parcel
     @param pent boolean indicating if the cargo uses pentomino prcels
     */
    public static void insertArray(int[][][] input, boolean pent) {
        array = input;
        pentMode = pent;
    }
    /**
     Launches the application, which opens a window containing the collection of stacked parcels,
     which can then be rotated by the user using M, N and the arrow keys
     @param stage the top level container of the application
     */
    public void start(Stage stage) {

        Group root = new Group();
        Scene scene = new Scene(root, WINDOW_LENGTH, WINDOW_HEIGHT, true);
        root.setTranslateX((WINDOW_LENGTH - (array.length-1)* RESOLUTION)/2);
        root.setTranslateY((WINDOW_HEIGHT - (array[0].length-1)* RESOLUTION)/2);
        root.setTranslateZ((-array[0][0].length+1)* RESOLUTION /2);
        CargoStacker cargo = new CargoStacker(root, array, pentMode);
        cargo.stack();

        PerspectiveCamera camera = new PerspectiveCamera(false);
        camera.setTranslateZ(-CameraViewDistance);
        scene.setCamera(camera);

        EventHandler<KeyEvent> handler = new EventHandler<KeyEvent>() {
            public void handle(KeyEvent e) {

                if (e.getCode() == KeyCode.UP) {
                    camera.getTransforms().add(new Rotate(RotateAngle,WINDOW_LENGTH/2,WINDOW_HEIGHT/2,CameraViewDistance,Rotate.X_AXIS));
                }
                if (e.getCode() == KeyCode.DOWN) {
                    camera.getTransforms().add(new Rotate(-RotateAngle,WINDOW_LENGTH/2,WINDOW_HEIGHT/2,CameraViewDistance,Rotate.X_AXIS));
                }
                if (e.getCode() == KeyCode.LEFT) {
                    camera.getTransforms().add(new Rotate(-RotateAngle,WINDOW_LENGTH/2,WINDOW_HEIGHT/2,CameraViewDistance,Rotate.Y_AXIS));
                }
                if (e.getCode() == KeyCode.RIGHT) {
                    camera.getTransforms().add(new Rotate(RotateAngle,WINDOW_LENGTH/2,WINDOW_HEIGHT/2,CameraViewDistance,Rotate.Y_AXIS));
                }
                if (e.getCode() == KeyCode.N) {
                    camera.getTransforms().add(new Rotate(RotateAngle,WINDOW_LENGTH/2,WINDOW_HEIGHT/2,CameraViewDistance,Rotate.Z_AXIS));
                }
                if (e.getCode() == KeyCode.M) {
                    camera.getTransforms().add(new Rotate(-RotateAngle,WINDOW_LENGTH/2,WINDOW_HEIGHT/2,CameraViewDistance,Rotate.Z_AXIS));
                }
            }
        };
        scene.addEventHandler(KeyEvent.KEY_PRESSED, handler);
        stage.setTitle("Truck");
        stage.setScene(scene);
        stage.show();
    }
}

