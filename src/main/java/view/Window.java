package view;

import controller.Controller;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.io.File;

import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import model.Intersection;
import model.Plan;
import model.Tour;



    private Stage primaryStage;
    private Controller controller;
public class Window extends Application {

    public static Plan plan;
    public static Tour tour;

    public Window() {
    }
	private Stage primaryStage;
	private Plan plan = new Plan();

    public Window(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.plan = new Plan();
        this.tour = new Tour();
        this.controller = new Controller(this);
    }
/*	public Window() {
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
*/
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(final Stage primaryStage) {

		GridPane grid = new GridPane();
		grid.setHgap(20.0);
		grid.setVgap(20.0);
		grid.setPadding(new Insets(10.0));
		grid.setAlignment(Pos.CENTER);
		
		//attempt to fix col3 size issue, currently failing
/*		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(25);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPercentWidth(25);
		ColumnConstraints col3 = new ColumnConstraints();
		col3.setPercentWidth(10);
		ColumnConstraints col4 = new ColumnConstraints();
		col4.setPercentWidth(20);
		ColumnConstraints col5 = new ColumnConstraints();
		col5.setPercentWidth(20);
		grid.getColumnConstraints().addAll(col1, col2, col3, col4, col5);
*/
		
		
		//PLAN
		final TextField planText = new TextField("fichier");
		planText.setEditable(false);
		grid.add(planText, 0, 1);
		
		Button planBtn = new Button("charger plan");
		grid.add(planBtn, 1, 1);
		
		Button suprimerPlanBtn = new Button("X");
		suprimerPlanBtn.setMaxWidth(20);
		grid.add(suprimerPlanBtn, 2, 1);

		//LIVRAISON
		final TextField livraisonText = new TextField("fichier2");
		livraisonText.setEditable(false);
		grid.add(livraisonText, 0, 2);
		
		Button livraisonBtn = new Button("charger livraison");
		grid.add(livraisonBtn, 1, 2);

    public Controller getController() {
        return controller;
    }
		Button suprimerLivraisonBtn = new Button("X");
		suprimerLivraisonBtn.setMaxWidth(20);
		grid.add(suprimerLivraisonBtn, 2,  2);

    public void setController(Controller controller) {
        this.controller = controller;
    }
		
		//PLAN
		final Canvas planCanvas = new Canvas(350,350);
		final GraphicsContext gc = planCanvas.getGraphicsContext2D();
		grid.add(planCanvas, 0, 0, 3, 1); //col1, row2, takes up 2 cols, takes up 10 rows

    public void renderPlan() throws Exception {
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
		//check gc is working / size of canvas
		gc.setFill(Color.TAN);
		gc.fillRect(0,0,planCanvas.getWidth(), planCanvas.getHeight());
		
		//TODO in future iteration: make these do something
		final TextField filler1 = new TextField("[livraison]");
		filler1.setPrefHeight(350);
		grid.add(filler1, 3, 0, 1, 1);
		final TextField filler2 = new TextField("[feuille de route]");
		filler2.setPrefHeight(350);
		grid.add(filler2, 4, 0, 1, 1);

        // ajout de la scène sur l'estrade
        primaryStage.setScene(scene);
        // ouvrir le rideau
        primaryStage.show();
    }
		Button feuilleBtn = new Button("générer feiulle de route");
		grid.add(feuilleBtn, 4, 1);

    public void render(){
        Group root = new Group();
        final TextField textField = new TextField();
        textField.setEditable(false);
        Button buttonLoadXML = new Button("Import XML");
		
		//test purposes only
		grid.setGridLinesVisible(true);
		
		//set stage
		Group root = new Group();
		root.getChildren().add(grid);
		Scene scene = new Scene(root, 1000, 450);
		primaryStage.setTitle("GridPane Draft");
		primaryStage.setScene(scene);
		
		primaryStage.show();
		
		
		
		
		
		//BUTTON ACTIONS
		planBtn.setOnAction(new EventHandler<ActionEvent>() {
			//@Override
			public void handle(ActionEvent arg0) {
				FileChooser planChooser = new FileChooser();
				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML Files (*.xml)", "*.xml");
				planChooser.getExtensionFilters().add(extFilter);
				File filePlan = planChooser.showOpenDialog(primaryStage);
				planText.setText(filePlan.getName());
				plan = new Plan(filePlan);
				System.out.println(plan.toString());
				try {
					renderPlan(planCanvas, gc);		
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		});
		
		livraisonBtn.setOnAction(new EventHandler<ActionEvent>() {
			//@Override
			public void handle(ActionEvent livraisonEvent) {
				FileChooser livrChooser = new FileChooser();
				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML Files (*.xml)", "*.xml");
				livrChooser.getExtensionFilters().add(extFilter);
				File fileLivr = livrChooser.showOpenDialog(primaryStage);
				livraisonText.setText(fileLivr.getName());
				//disp livraisons

        buttonLoadXML.setOnAction(new EventHandler<ActionEvent>(){
            public void handle(ActionEvent arg0) {
                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML Files (*.xml)", "*.xml");
                fileChooser.getExtensionFilters().add(extFilter);
                File file = fileChooser.showOpenDialog(primaryStage);
                textField.setText(file.getName());

                controller.loadPlan(file);
                try {
                    renderPlan();
                } catch (Exception e) {
                    e.printStackTrace();
                }
				try {
					renderLivraison();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		});
		
		suprimerPlanBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent supPlanEvent) {
				gc.setFill(Color.TAN);
				gc.fillRect(0,0, planCanvas.getWidth(), planCanvas.getHeight());
			}
		});
		
		suprimerLivraisonBtn.setOnAction(new EventHandler<ActionEvent> () {
			public void handle(ActionEvent supLivrEvent) {
				filler1.setText("TODO: implement this");
			}
		});
		
		feuilleBtn.setOnAction(new EventHandler<ActionEvent> () {
			public void handle(ActionEvent feiulleEvent) {
				filler2.setText("TODO: implement this");
			}
		});
		

	}
	
	
	public void renderPlan(Canvas planCanvas, GraphicsContext gc) throws Exception {
		System.out.println("srsly a problem exists still");

		gc.setFill(Color.LIGHTSKYBLUE);
		gc.fillRect(0,0, planCanvas.getWidth(), planCanvas.getHeight());

        // création d'une intersection
 /*       plan.getIntersections().forEach(
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
        });
        VBox vBox = VBoxBuilder.create()
                .children(textField, buttonLoadXML)
                .build();
        root.getChildren().add(vBox);
        primaryStage.setScene(new Scene(root, 500, 400));
        primaryStage.show();

    }

        );
*/
/*        gc.setFill(Color.DARKBLUE);
        for(Integer i = plan.getIntersections().size(); i > 0; i--) {
        	Intersection tmp = plan.get(i);
        	gc.fillOval(tmp.getX(), tmp.getY(), 6, 6);
        	Tooltip t = new Tooltip("Intersection: " + tmp.getId());
        	//circle events on and off
        	
        	
        }
*/
/*        plan.getSections().forEach(
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

*/		
	}
	
	public void renderLivraison() throws Exception {
		System.out.println("a thing");
	}
}



