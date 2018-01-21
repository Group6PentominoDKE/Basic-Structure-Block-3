import javafx.application.Application;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeType;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.geometry.Insets;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;

public class Input extends Application implements EventHandler<ActionEvent> {
    // Declare variables, buttons and stuff
    TextField VAfield;
    TextField VBfield;
    TextField VCfield;
    TextField LField;
    TextField PField;
    TextField TField;
    TextField lengthField;
    TextField widthField;
    TextField heightField;

    Button button; // Confirm the choices on LPT menu.
    Button backButton; // Used for going back to the main menu from the 3D viewing scene.
    Button abc; // Pressing this enables user to work with ABC shapes.
    Button lpt; // Pressing thus enables user to work with LPT shapes.
    Button abcok; // Confirm choices on ABC menu.

    ComboBox combbox; // Used for choosing what algorithm will be used. For the ABC menu.
    ComboBox combbox1; // For the LPT menu.

    String combChoice; // The choice of the combobox.

    Stage window;

    Scene scene; // Scene for 3d display.
    Scene scene1; // For A, B and C.
    Scene scene2; // For L, P and T.
    Scene scene3; // For choosing if you wanna use ABC or LPT.

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



    public void start(Stage primaryStage)  {
        window = primaryStage;
        window.setTitle("Container Fitting");



        // Create controls to alter the x and y pivot points of the rotation.
        final Slider xPivotSlider = createSlider(50, "Best values are 0, 50 or 100");
        final Slider yPivotSlider = createSlider(50, "Best values are 0, 50 or 100");
        final Slider zPivotSlider = createSlider(0,  "Won't do anything until you use an X or Y axis of rotation");
        // A Toggle represents a control that can be toggled between selected and non-selected states. A Toggle can be assigned a ToggleGroup, which manages all assigned Toggles such that only a single Toggle within the ToggleGroup may be selected at any one time.
        final ToggleGroup  axisToggleGroup = new PersistentButtonToggleGroup();
        final ToggleButton xAxisToggleButton = new ToggleButton("X Axis");
        final ToggleButton yAxisToggleButton = new ToggleButton("Y Axis");
        final ToggleButton zAxisToggleButton = new ToggleButton("Z Axis");
        xAxisToggleButton.setToggleGroup(axisToggleGroup);
        yAxisToggleButton.setToggleGroup(axisToggleGroup);
        zAxisToggleButton.setToggleGroup(axisToggleGroup);

        // Create new box with parameters.
        Box container = new Box();
        container.setDrawMode(DrawMode.LINE); // Make only outlines of container appear.
        container.setWidth(1650.0);
        container.setHeight(250.0);
        container.setDepth(400.0);

        // Set position of box
        container.setTranslateX(200);
        container.setTranslateY(150);

        // Add colors to container to make it look more realistic in 3D space.
        PhongMaterial material1 = new PhongMaterial();
        material1.setDiffuseColor(Color.BLUE);
        material1.setSpecularColor(Color.LIGHTBLUE);
        material1.setSpecularPower(10.0);
        container.setMaterial(material1);

        // Make light sources.
        AmbientLight light = new AmbientLight(Color.AQUA);
        light.setTranslateX(-180);
        light.setTranslateY(-90);
        light.setTranslateZ(-120);
        light.getScope().addAll(container);

        PointLight light2 = new PointLight(Color.AQUA);
        light2.setTranslateX(180);
        light2.setTranslateY(190);
        light2.setTranslateZ(180);
        light2.getScope().addAll(container);

        // Create a rotation transform starting at 0 egrees, rotating about pivot point 50, 50.
        final Rotate rotationTransform = new Rotate(0, 50, 50);
        container.getTransforms().add(rotationTransform);

        // Rotate the container using the Timeline attached to the rotation transform`s angle property.
        final Timeline rotationAnimation = new Timeline();
        rotationAnimation.getKeyFrames().add(
                new KeyFrame(
                        Duration.seconds(5),
                        new KeyValue(
                                rotationTransform.angleProperty(),
                                360
                        )
                )
        );
        rotationAnimation.setCycleCount(Animation.INDEFINITE);
        rotationAnimation.play();

        // Bind the transforms pivot points to our slider controls.
        rotationTransform.pivotXProperty().bind(xPivotSlider.valueProperty());
        rotationTransform.pivotYProperty().bind(yPivotSlider.valueProperty());
        rotationTransform.pivotZProperty().bind(zPivotSlider.valueProperty());

        // Allow our toggle controls to choose the axis of rotation.
        xAxisToggleButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                rotationTransform.setAxis(Rotate.X_AXIS);
            }
        });
        yAxisToggleButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                rotationTransform.setAxis(Rotate.Y_AXIS);
            }
        });
        zAxisToggleButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                rotationTransform.setAxis(Rotate.Z_AXIS);
            }
        });
        zAxisToggleButton.fire();

        // Display a crosshair to mark the current pivot point.
        final Line verticalLine   = new Line(0, -10, 0, 10); verticalLine.setStroke(Color.FIREBRICK);   verticalLine.setStrokeWidth(3);   verticalLine.setStrokeLineCap(StrokeLineCap.ROUND);
        final Line horizontalLine = new Line(-10, 0, 10, 0); horizontalLine.setStroke(Color.FIREBRICK); horizontalLine.setStrokeWidth(3); verticalLine.setStrokeLineCap(StrokeLineCap.ROUND);
        Group pivotMarker = new Group(verticalLine, horizontalLine);
        pivotMarker.translateXProperty().bind(xPivotSlider.valueProperty().subtract(50));
        pivotMarker.translateYProperty().bind(yPivotSlider.valueProperty().subtract(50));

        // Display a dashed square border outline to mark the original location of the square.
        final Rectangle squareOutline = new Rectangle(100, 100);
        squareOutline.setFill(Color.TRANSPARENT);
        squareOutline.setOpacity(0.7);
        squareOutline.setMouseTransparent(true);
        squareOutline.setStrokeType(StrokeType.INSIDE);
        squareOutline.setStrokeWidth(1);
        squareOutline.setStrokeLineCap(StrokeLineCap.BUTT);
        squareOutline.setStroke(Color.DARKGRAY);
        squareOutline.setStrokeDashOffset(5);
        squareOutline.getStrokeDashArray().add(10.0);

        Group root = new Group(container);
        // Rotate root group.
        root.setRotate(105);

        // layout the scene.
        final HBox xPivotControl = new HBox(5);
        xPivotControl.getChildren().addAll(new Label("X Pivot Point"), xPivotSlider);
        HBox.setHgrow(xPivotSlider, Priority.ALWAYS);

        final HBox yPivotControl = new HBox(5);
        yPivotControl.getChildren().addAll(new Label("Y Pivot Point"), yPivotSlider);
        HBox.setHgrow(yPivotSlider, Priority.ALWAYS);

        final HBox zPivotControl = new HBox(5);
        zPivotControl.getChildren().addAll(new Label("Z Pivot Point"), zPivotSlider);
        HBox.setHgrow(zPivotSlider, Priority.ALWAYS);

        final HBox axisControl = new HBox(20);
        axisControl.getChildren().addAll(new Label("Axis of Rotation"), xAxisToggleButton, yAxisToggleButton, zAxisToggleButton);
        axisControl.setAlignment(Pos.BASELINE_LEFT);

        final StackPane displayPane = new StackPane();
        displayPane.getChildren().addAll(container, pivotMarker, squareOutline);
        displayPane.setTranslateY(80);
        displayPane.setMouseTransparent(true);

        final StackPane layout = new StackPane();

        // Back button for getting back to menu
        backButton = new Button("Back to main menu");
        backButton.setOnAction(this);

        layout.getChildren().addAll(backButton,
                VBoxBuilder.create().spacing(10).alignment(Pos.TOP_CENTER).children(xPivotControl, yPivotControl, zPivotControl, axisControl).build(),
                displayPane
        );
        layout.setStyle("-fx-background-color: linear-gradient(to bottom, cornsilk, midnightblue); -fx-padding:10; -fx-font-size: 16");
        scene = new Scene(layout, 480, 550);
        // From here on Stuff regarding the buttons and GUI
        // First menu where the user chooses if they wanna use abc or lpt shapes.
        // Create two combobxes to choose which algorithm will be used.
        combbox = new ComboBox();
        combbox.getItems().add("Greedy");
        combbox.getItems().add("BackTracking");
        combbox.getItems().add("Dynamic programming");
        // Set the first choice as default.
        combbox.getSelectionModel().selectFirst();
        combbox.setOnAction(this);

        combbox1 = new ComboBox();
        combbox1.getItems().add("Greedy");
        combbox1.getItems().add("BackTracking");
        combbox1.getItems().add("Dynamic programming");
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

        lengthField = new TextField("16.5");
        GridPane.setConstraints(lengthField, 1, 4);

        Label wid = new Label("Container width: ");
        GridPane.setConstraints(wid, 0, 5);

        widthField = new TextField("2.5");
        GridPane.setConstraints(widthField, 1, 5);

        Label hei = new Label("Container height: ");
        GridPane.setConstraints(hei, 0, 6);

        heightField = new TextField("4.0");
        GridPane.setConstraints(heightField, 1, 6);

        GridPane.setConstraints(abcok, 0, 4);

        grid2.getChildren().addAll(VA, VAfield, VB, VBfield, VC, VCfield, len, lengthField, wid, widthField, hei, heightField, abcok, combbox);

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

        lengthField = new TextField("16.5");
        GridPane.setConstraints(lengthField, 1, 4);

        Label widt = new Label("Container width: ");
        GridPane.setConstraints(widt, 0, 5);

        widthField = new TextField("2.5");
        GridPane.setConstraints(widthField, 1, 5);

        Label heig = new Label("Container height: ");
        GridPane.setConstraints(heig, 0, 6);

        heightField = new TextField("4.0");
        GridPane.setConstraints(heightField, 1, 6);

        VAfield.setOnAction(this);



        button = new Button();
        button.setText("Ok");
        GridPane.setConstraints(button, 0, 4);

        button.setOnAction(this); // So that handle will look in this class

        grid.getChildren().addAll(L, LField, P, PField, T, TField,  leng, lengthField, widt, widthField, heig, heightField, button, combbox1);

        // StackPane layout1 = new StackPane();
        // layout1.getChildren().add(button);
        // scene1 = new Scene(layout1, 480, 550);
        scene1 = new Scene(grid, 300, 370);
        primaryStage.setScene(scene2);






        // Stuff regarding buttons and GUI ends here.
        window.show();
    }

    /*
        Generate a new slider control initialized to the given value.
        @param value the initial value of the slider.
        @param helpText the tool tip text to use for the slider.
        @return the new slider.
       */
    private Slider createSlider(final double value, final String helpText) {
        final Slider slider = new Slider(-50, 151, value);
        slider.setMajorTickUnit(50);
        slider.setMinorTickCount(0);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setStyle("-fx-text-fill: white");
        slider.setTooltip(new Tooltip(helpText));
        return slider;
    }
    /*
     * Create a toggle group of buttons where one toggle will always remain switched on.
     */
    class PersistentButtonToggleGroup extends ToggleGroup {
        PersistentButtonToggleGroup() {
            super();
            getToggles().addListener(new ListChangeListener<Toggle>() {
                @Override public void onChanged(Change<? extends Toggle> c) {
                    while (c.next()) {
                        for (final Toggle addedToggle : c.getAddedSubList()) {
                            ((ToggleButton) addedToggle).addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
                                @Override public void handle(MouseEvent mouseEvent) {
                                    if (addedToggle.equals(getSelectedToggle())) mouseEvent.consume();
                                }
                            });
                        }
                    }
                }
            });
        }
    }

    // Scene scene = new Scene(root, 600, 600);
    // Camera stuff.
    // scene.setFill(Color.BLACK);
	/*	PerspectiveCamera camera = new PerspectiveCamera(false); // If true then 3D layout is defined to the origin, if false then 3D layout origin is located at the upper-left corner of the screen.
		// The near and farClip values specify the distance range between which the object will be displayed.
		// camera.setNearClip(0.1);
		// camera.setFarClip(1000.0);
		camera.setTranslateX(-400);
		camera.setTranslateY(-250);
		camera.setTranslateZ(-1000);
		// camera.setRotate(50.0);
		scene.setCamera(camera);

		primaryStage.setTitle("3D JAVAFX");
		primaryStage.setScene(scene);
		primaryStage.show();
	*/

	 /* Main.
	public static void main(String[] args) throws Exception  {
		launch(args);
	}
	*/

    // Handle the button clicks.
    public void handle(ActionEvent event) {
        if (event.getSource() == button) {
            L1 = Integer.parseInt(LField.getText());

            P1 = Integer.parseInt(PField.getText());

            T1 = Integer.parseInt(TField.getText());

            length = Double.parseDouble(lengthField.getText());
            width = Double.parseDouble(widthField.getText());
            height = Double.parseDouble(heightField.getText());

            combChoice = (String) combbox1.getValue();

            try {
                BufferedWriter fw1 = new BufferedWriter(new FileWriter("values.txt"));
                // fw1 = new FileWriter(new File("values.txt"));
                fw1.write(String.format(Integer.toString(L1)));
                fw1.newLine();
                fw1.write(String.format(Integer.toString(P1)));
                fw1.newLine();
                fw1.write(String.format(Integer.toString(T1)));
                fw1.newLine();
                fw1.write(combChoice);
                fw1.newLine();
                fw1.write("LPT");
                fw1.newLine();
                fw1.write(String.format(Double.toString(length)));
                fw1.newLine();
                fw1.write(String.format(Double.toString(width)));
                fw1.newLine();
                fw1.write(String.format(Double.toString(height)));
                fw1.close();

            } catch (IOException ex1) {
                ex1.printStackTrace();
            }
            try {  stop();}
            catch (Exception ex1) {}

            window.setScene(scene);
        }
        if (event.getSource() == backButton) {
            window.setScene(scene2);
        }
        if (event.getSource() == abc) {
            window.setScene(scene3);
        }
        if (event.getSource() == lpt) {
            window.setScene(scene1);
        }
        if (event.getSource() == abcok) {
            combChoice = (String) combbox.getValue();

            VA1 = Integer.parseInt(VAfield.getText());
            VB1 = Integer.parseInt(VBfield.getText());
            VC1 = Integer.parseInt(VCfield.getText());
            length = Double.parseDouble(lengthField.getText());
            width = Double.parseDouble(widthField.getText());
            height = Double.parseDouble(heightField.getText());
            try {
                BufferedWriter fw = new BufferedWriter(new FileWriter("values.txt"));
                fw.write(String.format(Integer.toString(VA1)));
                fw.newLine();
                fw.write(String.format(Integer.toString(VB1)));
                fw.newLine();
                fw.write(String.format(Integer.toString(VC1)));
                fw.newLine();
                fw.write(combChoice);
                fw.newLine();
                fw.write("ABC");
                fw.newLine();
                fw.write(String.format(Double.toString(length)));
                fw.newLine();
                fw.write(String.format(Double.toString(width)));
                fw.newLine();
                fw.write(String.format(Double.toString(height)));
                fw.close();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
            try {
                stop();

            }
            catch (Exception ex) {}

            //window.setScene(scene);
            window.close();
        }
    }

}
