package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Intersection;
import model.Plan;

import java.io.File;

/**
 * Created by Olivice on 18/11/2016.
 */
public class Window {

    private Stage primaryStage;
    private Plan plan = new Plan();

    public Window() {
    }

    public Window(Stage primaryStage) {
        this.primaryStage = primaryStage;
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

            /*// création du sol
            Rectangle ground = new Rectangle(0, 400, 800, 200);
            ground.setFill(Color.GREEN);

            // création d'un élément plus complexe, le panneau
            Group sign = new Group();
            sign.setTranslateX(150);
            sign.setTranslateY(200);
            // Attention les coordonnées sont celles du panneau, pas de la scène
            Text text = new Text(10, 30, "Hello world!");
            text.setFont(new Font(80));
            text.setFill(Color.WHITE);
            // le repère utilisé est celui du panneau
            Rectangle panel = new Rectangle( 0, -50, 500, 110);
            panel.setFill(Color.DARKBLUE);
            // composer l'élément plus complexe
            sign.getChildren().add(panel);
            sign.getChildren().add(text);*/

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
                plan = new Plan(file);
                System.out.println(plan.toString());
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
}
