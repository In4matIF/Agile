package view;

import controller.Controller;
import controller.LoadPlanCommand;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Plan;
import model.VisitorCrossingPoint;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Olivice on 18/11/2016.
 */
public class GraphicalUI implements Observer, VisitorCrossingPoint {

    @Override
    public void update(Observable o, Object arg) {

    }

    private Stage primaryStage;
    private Plan plan;
    private Controller controller;

    public GraphicalUI() {
    }

    public GraphicalUI(Stage primaryStage, Controller controller, Plan plan) {
        this.primaryStage = primaryStage;
        this.controller = controller;
        this.plan = plan;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void render() throws Exception {
        // définit la largeur et la hauteur de la fenêtre
        // en pixels, le (0, 0) se situe en haut à gauche de la fenêtre
        primaryStage.setWidth(1200);
        primaryStage.setHeight(800);
        // met un titre dans la fenêtre
        primaryStage.setTitle("PLD Agile");

        // la racine du sceneGraph est le root
        Group root = new Group();
        Scene scene = new Scene(root);
        scene.setFill(Color.WHITESMOKE);

        // création d'une intersection
        plan.getIntersections().forEach(
                (integer, intersection) -> {
                    Circle circle = new Circle(
                            intersection.getX(),
                            intersection.getY(),
                            6,
                            Color.DARKBLUE
                    );
                    Tooltip t = new Tooltip("Intersection : "+intersection.getId());
                    circle.setOnMouseEntered(event -> {
                        Node node =(Node)event.getSource();
                        t.show( node,
                                primaryStage.getX()+event.getSceneX(),
                                primaryStage.getY()+event.getSceneY()
                        );
                    });
                    circle.setOnMouseExited(event -> t.hide());
                    root.getChildren().add(circle);
                }
        );

        plan.getSections().forEach(
                section -> {
                    Line line = new Line(
                            section.getOrigin().getX(),
                            section.getOrigin().getY(),
                            section.getDestination().getX(),
                            section.getDestination().getY()
                    );
                    line.setStrokeWidth(3);
                    line.setStroke(Color.DARKBLUE);
                    root.getChildren().add(line);
                }
        );

        // ajout de la scène sur l'estrade
        primaryStage.setScene(scene);
        // ouvrir le rideau
        primaryStage.show();
    }

    public void renderButton(){
        Group root = new Group();
        final TextField textField = new TextField();
        textField.setEditable(false);
        Button buttonLoadXML = new Button("Import XML");

        buttonLoadXML.setOnAction(new EventHandler<ActionEvent>(){
            public void handle(ActionEvent arg0) {
                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML Files (*.xml)", "*.xml");
                fileChooser.getExtensionFilters().add(extFilter);
                File file = fileChooser.showOpenDialog(primaryStage);
                textField.setText(file.getName());
                //plan = new Plan(file);
                controller.loadPlan(plan, file);

                try {
                    render();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        VBox vBox = VBoxBuilder.create()
                .children(textField, buttonLoadXML)
                .build();
        root.getChildren().add(vBox);
        primaryStage.setScene(new Scene(root, 500, 400));
        primaryStage.show();

    }

    public void setController(Controller controller){
        this.controller = controller;
    }
}
