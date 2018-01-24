import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.transform.Rotate;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.geometry.Insets;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
/**
Class that displays the user input interface and the visual ouput
@author Kaspar Kallast
@author Lukas Padolevicius
*/

public class KnapsackInterface extends Application implements EventHandler<ActionEvent> {
    // Declare variables, buttons
    private TextField VAfield;
    private TextField VBfield;
    private TextField VCfield;
    private TextField LField;
    private TextField PField;
    private TextField TField;
    private TextField BlengthField;
    private TextField BwidthField;
    private TextField BheightField;
    private TextField PlengthField;
    private TextField PwidthField;
    private TextField PheightField;

    private Button button; // Confirm the choices on LPT menu.
    private Button backButton; // Used for going back to the main menu from the 3D viewing scene.
    private Button abc; // Pressing this enables user to work with ABC shapes.
    private Button lpt; // Pressing thus enables user to work with LPT shapes.
    private Button abcok; // Confirm choices on ABC menu.

    private ComboBox combbox; // Used for choosing what algorithm will be used. For the ABC menu.
    private ComboBox combbox1; // For the LPT menu.

    String combChoice; // The choice of the combobox.

    private Stage window;

    private Scene scene; // Scene for 3d display.
    private Scene scene1; // For A, B and C.
    private Scene scene2; // For L, P and T.
    private Scene scene3; // For choosing if you wanna use ABC or LPT.

    // These ints store the values chosen by the user for the values of ABC and LPT shapes.
    int VA1;
    int VB1;
    int VC1;
    int L1;
    int P1;
    int T1;
    double length;
    double width;
    double height;

    private int[] userInput = new int[6];
    private int[][][] resultArray;

    private static final int WINDOW_LENGTH = 1500;
    private static final int WINDOW_HEIGHT = 700;
    private static final int CameraViewDistance = 700;
    private static final int RotateAngle = 5;
    private static final int RESOLUTION = 50;
    private boolean pentMode;
    private String algorithm;
		/**
		the main method
		@param primaryStage the Stage for the Application
		*/
    public void start(Stage primaryStage)  {
        window = primaryStage;
        window.setTitle("Container Fitting");
        // From here on Stuff regarding the buttons and GUI
        // First menu where the user chooses if they wanna use abc or lpt shapes.
        // Create two combobxes to choose which algorithm will be used.
        combbox = new ComboBox();
        combbox.getItems().add("Greedy");
        combbox.getItems().add("BackTracking");
        combbox.getItems().add("Dynamic Programming");
        combbox.getItems().add("Divide and Conquer");
        // Set the first choice as default.
        combbox.getSelectionModel().selectFirst();
        combbox.setOnAction(this);

        combbox1 = new ComboBox();
        combbox1.getItems().add("Greedy");
        combbox1.getItems().add("BackTracking");
        combbox1.getItems().add("Dynamic Programming");
        combbox1.getItems().add("Divide and Conquer");
        // Set the first choice as default.
        combbox1.getSelectionModel().selectFirst();
        combbox1.setOnAction(this);


        // These 2 buttons are used for choosing if the user will work with LPT or ABC shapes.
        abc = new Button("A, B, and C");
        abc.setOnAction(this);

        lpt = new Button("L, P and T");
        lpt.setOnAction(this);

        // This gridPane contains the ABC and LPT buttons.
        GridPane grid1 = new GridPane();
        grid1.setPadding(new Insets(10, 10, 10, 10));
        grid1.setVgap(8);
        grid1.setHgap(10);
        GridPane.setConstraints(abc, 0, 0);
        GridPane.setConstraints(lpt, 1, 0);

        grid1.getChildren().addAll(abc, lpt);

        scene2 = new Scene(grid1, 300, 370);

        // ABC value and algorithm choosing menu.

        abcok = new Button("Ok");
        abcok.setOnAction(this);

        GridPane grid2 = new GridPane();
        grid2.setPadding(new Insets(10, 10, 10, 10));
        grid2.setVgap(8);
        grid2.setHgap(10);

        GridPane.setConstraints(combbox, 0, 0);

        Label VA = new Label("VA value:");
        GridPane.setConstraints(VA, 0, 1);

        VAfield = new TextField("0");
        GridPane.setConstraints(VAfield, 1, 1);

        Label VB = new Label("VB value:");
        GridPane.setConstraints(VB, 0, 2);

        VBfield = new TextField("0");
        GridPane.setConstraints(VBfield, 1, 2);

        Label VC = new Label("VC value:");
        GridPane.setConstraints(VC, 0, 3);

        VCfield = new TextField("0");
        GridPane.setConstraints(VCfield, 1, 3);

        Label len = new Label("Container length: ");
        GridPane.setConstraints(len, 0, 4);

        BlengthField = new TextField("16.5");
        GridPane.setConstraints(BlengthField, 1, 4);

        Label wid = new Label("Container width: ");
        GridPane.setConstraints(wid, 0, 5);

        BwidthField = new TextField("2.5");
        GridPane.setConstraints(BwidthField, 1, 5);

        Label hei = new Label("Container height: ");
        GridPane.setConstraints(hei, 0, 6);

        BheightField = new TextField("4.0");
        GridPane.setConstraints(BheightField, 1, 6);

        GridPane.setConstraints(abcok, 0, 7);

        grid2.getChildren().addAll(VA, VAfield, VB, VBfield, VC, VCfield, len, BlengthField, wid, BwidthField, hei, BheightField, abcok, combbox);

        scene3 = new Scene(grid2, 300, 370);



        // LPT value and algorithm choosing menu.

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        // Amounts of shapes textfields and labels

        GridPane.setConstraints(combbox1, 0, 0);

        Label L = new Label("L value:");
        GridPane.setConstraints(L, 0, 1);

        LField = new TextField("0");
        GridPane.setConstraints(LField, 1, 1);

        Label P = new Label("P value:");
        GridPane.setConstraints(P, 0, 2);

        PField = new TextField("0");
        GridPane.setConstraints(PField, 1, 2);

        Label T = new Label("T value:");
        GridPane.setConstraints(T, 0, 3);

        TField = new TextField("0");
        GridPane.setConstraints(TField, 1, 3);

        Label leng = new Label("Container length: ");
        GridPane.setConstraints(leng, 0, 4);

        PlengthField = new TextField("16.5");
        GridPane.setConstraints(PlengthField, 1, 4);

        Label widt = new Label("Container width: ");
        GridPane.setConstraints(widt, 0, 5);

        PwidthField = new TextField("2.5");
        GridPane.setConstraints(PwidthField, 1, 5);

        Label heig = new Label("Container height: ");
        GridPane.setConstraints(heig, 0, 6);

        PheightField = new TextField("4.0");
        GridPane.setConstraints(PheightField, 1, 6);

        VAfield.setOnAction(this);

        button = new Button();
        button.setText("Ok");
        GridPane.setConstraints(button, 0, 7);

        button.setOnAction(this); // So that handle will look in this class

        grid.getChildren().addAll(L, LField, P, PField, T, TField,  leng, PlengthField, widt, PwidthField, heig, PheightField, button, combbox1);

        // StackPane layout1 = new StackPane();
        // layout1.getChildren().add(button);
        // scene1 = new Scene(layout1, 480, 550);
        scene1 = new Scene(grid, 300, 370);
        primaryStage.setScene(scene2);

        // Stuff regarding buttons and GUI ends here.
        window.show();
    }
    /**
        Generate a new slider control initialized to the given value.
        @param value the initial value of the slider.
        @param helpText the tool tip text to use for the slider.
        @return the new slider.
       */
  
    /**
     * Create a toggle group of buttons where one toggle will always remain switched on.
     */
    
		/**
		Event handler for all user input
		@param event the event to handle
		*/
    public void handle(ActionEvent event) {
        if (event.getSource() == abc) {
            pentMode = false;
            window.setScene(scene3);
        }
        if (event.getSource() == lpt) {
            pentMode = true;
            window.setScene(scene1);
        }
        if (event.getSource() == abcok || event.getSource() == button) {
            Main main = new Main();

            if(pentMode == false) {

                algorithm = (String)combbox.getValue();
                userInput[0] = Integer.parseInt(VAfield.getText());
                userInput[1] = Integer.parseInt(VBfield.getText());
                userInput[2] = Integer.parseInt(VCfield.getText());

                userInput[3] = (int)(Double.parseDouble(BwidthField.getText())*2);
                userInput[4] = (int)(Double.parseDouble(BheightField.getText())*2);
                userInput[5] = (int)(Double.parseDouble(BlengthField.getText())*2);
            } else {

                algorithm = (String)combbox1.getValue();
                userInput[0] = Integer.parseInt(LField.getText());
                userInput[1] = Integer.parseInt(PField.getText());
                userInput[2] = Integer.parseInt(TField.getText());

                userInput[3] = (int)(Double.parseDouble(PwidthField.getText())*2);
                userInput[4] = (int)(Double.parseDouble(PheightField.getText())*2);
                userInput[5] = (int)(Double.parseDouble(PlengthField.getText())*2);
            }
            resultArray = main.compute(userInput, pentMode, algorithm);
            if(resultArray[0][0][0] != 215789) {
                Group root = new Group();
                Scene sceneCargo = new Scene(root, WINDOW_LENGTH, WINDOW_HEIGHT, true);
                window.setScene(sceneCargo);
                window.setMaximized(true);
                root.setTranslateX((WINDOW_LENGTH - (resultArray.length-1)* RESOLUTION)/2);
                root.setTranslateY((WINDOW_HEIGHT - (resultArray[0].length-1)* RESOLUTION)/2);
                root.setTranslateZ((-resultArray[0][0].length+1)* RESOLUTION /2);
                CargoStacker cargo = new CargoStacker(root, resultArray, pentMode);
                cargo.stack();

                PerspectiveCamera camera = new PerspectiveCamera(false);
                camera.setTranslateZ(-CameraViewDistance);
                sceneCargo.setCamera(camera);

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
                sceneCargo.addEventHandler(KeyEvent.KEY_PRESSED, handler);
            }
        }
    }

}
