package view;

import controller.Controller;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.control.ScrollPane;
import model.Intersection;
import model.Plan;
import model.Tour;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.sun.prism.BasicStroke;

/**
 * Classe d'interface graphique
 */
public class Window {

	private final int SCENE_WIDTH = 1400;
	private final int SCENE_HEIGHT = 800;
	private final int CANVAS_WIDTH = 750;
	private final int CANVAS_HEIGHT = 650;
	private final int TEXT_AREA_DELIVERY_WIDTH = 280;
	private final int TEXT_AREA_DELIVERY_HEIGHT = 350;
	private final int TEXT_AREA_SHEET_WIDTH = 280;
	private final int TEXT_AREA_SHEET_HEIGHT = 350;

	private Stage primaryStage;
	private Controller controller;

	public static Plan plan;
	public static Tour tour;

	static int noSectionToDraw = 0;
	static Color colorToDraw = Color.CYAN;

	public Window() {
	}

	/**
	 * Crﾃｩe une nouvelle fenﾃｪtre
	 * 
	 * @param primaryStage
	 *            Le container principale de l'interface
	 */
	public Window(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.plan = new Plan();
		this.tour = new Tour();
		this.controller = new Controller(this);
	}

	/**
	 * Rﾃｩcupﾃｨre le container PrimaryStage
	 * 
	 * @return Le container principale de l'interface
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	/**
	 * Change la valeur du container PrimaryStage
	 * 
	 * @param primaryStage
	 *            Le nouveau container principale de l'interface
	 */
	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	/**
	 * Rﾃｩcupﾃｨre le contrﾃｴleur de l'application (MVC)
	 * 
	 * @return Le contrﾃｴleur de l'application (MVC)
	 */
	public Controller getController() {
		return controller;
	}

	/**
	 * Change la valeur du contrﾃｴleur de l'application (MVC)
	 * 
	 * @param controller
	 *            Le nouveau contrﾃｴleur de l'application (MVC)
	 */
	public void setController(Controller controller) {
		this.controller = controller;
	}

	/**
	 * Configure la gestion des panels dans l'interface graphique, ainsi que les
	 * actions liﾃｩes aux boutons de l'interface
	 */
	public void render() {

		GridPane grid = new GridPane();
		grid.setHgap(20.0);
		grid.setVgap(20.0);
		grid.setPadding(new Insets(10.0));
		grid.setAlignment(Pos.CENTER);

		// attempt to fix col3 size issue, currently failing
		/*
		 * ColumnConstraints col1 = new ColumnConstraints();
		 * col1.setPercentWidth(25); ColumnConstraints col2 = new
		 * ColumnConstraints(); col2.setPercentWidth(25); ColumnConstraints col3
		 * = new ColumnConstraints(); col3.setPercentWidth(10);
		 * ColumnConstraints col4 = new ColumnConstraints();
		 * col4.setPercentWidth(20); ColumnConstraints col5 = new
		 * ColumnConstraints(); col5.setPercentWidth(20);
		 * grid.getColumnConstraints().addAll(col1, col2, col3, col4, col5);
		 */

		// PLAN
		final TextField planText = new TextField("fichier");
		planText.setEditable(false);
		grid.add(planText, 0, 1);

		Button planBtn = new Button("charger plan");
		grid.add(planBtn, 1, 1);

		Button suprimerPlanBtn = new Button("X");
		suprimerPlanBtn.setMaxWidth(20);
		grid.add(suprimerPlanBtn, 2, 1);

		// LIVRAISON
		final TextField livraisonText = new TextField("fichier2");
		livraisonText.setEditable(false);
		grid.add(livraisonText, 0, 2);

		Button livraisonBtn = new Button("charger livraison");
		grid.add(livraisonBtn, 1, 2);

		Button suprimerLivraisonBtn = new Button("X");
		suprimerLivraisonBtn.setMaxWidth(20);
		grid.add(suprimerLivraisonBtn, 2, 2);

		Button playTour = new Button("Play");
		grid.add(playTour, 3, 1);

		// PLAN
		final Canvas planCanvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
		final GraphicsContext gc = planCanvas.getGraphicsContext2D();
		grid.add(planCanvas, 0, 0, 3, 1); // col1, row2, takes up 2 cols, takes
											// up 10 rows

		// check gc is working / size of canvas
		gc.setFill(Color.TAN);
		gc.fillRect(0, 0, planCanvas.getWidth(), planCanvas.getHeight());

		// TODO in future iteration: make these do something
		final ScrollPane deliveryPaneScroll = new ScrollPane();
		deliveryPaneScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		deliveryPaneScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		final FlowPane deliveryPane = new FlowPane();
		deliveryPane.setVgap(5);
		grid.add(deliveryPaneScroll,3,0,1,1);
    	deliveryPane.setPrefHeight(planCanvas.getHeight());
		/*final TextArea filler1 = new TextArea("[livraison]");
		filler1.setPrefHeight(TEXT_AREA_DELIVERY_HEIGHT);
		filler1.setPrefWidth(TEXT_AREA_DELIVERY_WIDTH);
		grid.add(filler1, 3, 0, 1, 1);*/
		final TextArea filler2 = new TextArea("[feuille de route]");
		filler2.setPrefHeight(TEXT_AREA_SHEET_HEIGHT);
		filler2.setPrefWidth(TEXT_AREA_SHEET_WIDTH);
		grid.add(filler2, 4, 0, 1, 1);

		Button feuilleBtn = new Button("generer feuille de route");
		grid.add(feuilleBtn, 4, 1);

		// test purposes only
		grid.setGridLinesVisible(false);

		// set stage
		Group root = new Group();
		root.getChildren().add(grid);
		Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
		primaryStage.setTitle("PLD Agile");
		primaryStage.setScene(scene);

		primaryStage.show();

		// BUTTON ACTIONS
		planBtn.setOnAction(new EventHandler<ActionEvent>() {
			// @Override
			public void handle(ActionEvent arg0) {
				FileChooser planChooser = new FileChooser();
				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML Files (*.xml)", "*.xml");
				planChooser.getExtensionFilters().add(extFilter);
				File filePlan = planChooser.showOpenDialog(primaryStage);
				planText.setText(filePlan.getName());
				controller.loadPlan(filePlan);
				try {
					renderPlan(planCanvas, gc);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});

		livraisonBtn.setOnAction(new EventHandler<ActionEvent>() {
			// @Override
			public void handle(ActionEvent livraisonEvent) {
				FileChooser livrChooser = new FileChooser();
				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML Files (*.xml)", "*.xml");
				livrChooser.getExtensionFilters().add(extFilter);
				File fileLivr = livrChooser.showOpenDialog(primaryStage);
				livraisonText.setText(fileLivr.getName());
				controller.loadTour(fileLivr);
				// disp livraisons
				try {
					renderLivraison(deliveryPane, filler2, planCanvas, gc);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});

		suprimerPlanBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent supPlanEvent) {
				plan = new Plan();
				gc.setFill(Color.TAN);
				gc.fillRect(0, 0, planCanvas.getWidth(), planCanvas.getHeight());
			}
		});

		suprimerLivraisonBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent supLivrEvent) {
				
			}
		});

		feuilleBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent feiulleEvent) {
				File output = new File("FeuilleDeRoute.txt");
				try {
					FileWriter fw = new FileWriter(output);
					{
						try {
							fw.write(filler2.getText());
						} catch (IOException e) {
							System.out.println("Erreur lors de la lecture des intersections : " + e.getMessage());
							e.printStackTrace();
						}
					}
					fw.close();
				} catch (IOException e) {
					System.out.println("Erreur lors de la lecture : " + e.getMessage());
				}
			}
		});

		playTour.setOnAction(new EventHandler<ActionEvent>() {
			// @Override
			public void handle(ActionEvent arg0) {
				try {
					drawTour(gc);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});

	}

	/**
	 * Gﾃｨre l'affichage du plan en canvas dans l'interface graphique
	 * 
	 * @param planCanvas
	 *            L'objet canvas contenant les formes gﾃｩomﾃｩtrique ﾃ� dessiner
	 * @param gc
	 *            Objet appelﾃｩ pour dessiner des formes dans un canvas
	 */
	public void renderPlan(Canvas planCanvas, GraphicsContext gc) throws Exception {

		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, planCanvas.getWidth(), planCanvas.getHeight());

		float widthRatio = 0.95f;// (float)CANVAS_WIDTH/(float)SCENE_WIDTH;
		float heightRatio = (float) CANVAS_HEIGHT / (float) SCENE_HEIGHT;

		// creation d'une intersection
		gc.setFill(Color.DARKBLUE);
		gc.setStroke(Color.DARKBLUE);
		gc.setLineWidth(3);
		plan.getIntersections().forEach((integer, intersection) -> {
			float x = intersection.getX() * widthRatio;
			float y = intersection.getY() * heightRatio;
			int radius = 6;

			gc.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);

			/*
			 * Tooltip t = new Tooltip("Intersection : "+intersection.getId());
			 * circle.setOnMouseEntered(event -> { Node node
			 * =(Node)event.getSource(); t.show( node,
			 * primaryStage.getX()+event.getSceneX(),
			 * primaryStage.getY()+event.getSceneY() ); });
			 * circle.setOnMouseExited(event -> t.hide());
			 */
			// root.getChildren().add(circle);
		});

		plan.getSections().forEach(section -> {
			gc.strokeLine(section.getOrigin().getX() * widthRatio, section.getOrigin().getY() * heightRatio,
					section.getDestination().getX() * widthRatio, section.getDestination().getY() * heightRatio);
			// root.getChildren().add(line);
		});

	}

	/**
	 * Gﾃｨre l'affichage du plan en canvas dans l'interface graphique
	 * 
	 * @param filler1
	 *            Contient les informations sur les livraisons
	 * @param filler2
	 *            Contient les informations de la feuille de route
	 * @param planCanvas
	 *            L'objet canvas contenant les formes gﾃｩomﾃｩtrique ﾃ� dessiner
	 * @param gc
	 *            Objet appelﾃｩ pour dessiner des formes dans un canvas
	 */
	public void renderLivraison(FlowPane deliveryPane, TextArea filler2, Canvas planCanvas, GraphicsContext gc)
			throws Exception {
		filler2.setText("");

		gc.setStroke(Color.CYAN);

		float widthRatio = 0.95f;// (float)CANVAS_WIDTH/(float)SCENE_WIDTH;
		float heightRatio = (float) CANVAS_HEIGHT / (float) SCENE_HEIGHT;

		List<Rectangle> deliveryRectangles = new ArrayList<Rectangle>() ;
		tour.getIntersections().forEach(intersection -> {
			Rectangle temp = new Rectangle(400,100);
			temp.setFill(new LinearGradient(0,0,0,1, true, CycleMethod.NO_CYCLE,
			        new Stop[]{
			        new Stop(0,Color.web("#4977A3")),
			        new Stop(0.5, Color.web("#B0C6DA")),
			        new Stop(1,Color.web("#9CB6CF")),}));
			    temp.setStroke(Color.web("#D0E6FA"));
			    temp.setArcHeight(3.5);
			    temp.setArcWidth(3.5);
			deliveryRectangles.add(temp);
			Text text = new Text(new String("Livraison : " + intersection.getId()+ "\r\n"));
			StackPane stack = new StackPane();
			stack.getChildren().add(temp);
			stack.getChildren().add(text);
			deliveryPane.getChildren().add(stack);
			//String deliverys = filler1.getText() + "Livraison : " + intersection.getId() + "\r\n";
			//filler1.setText(deliverys);
		});

		tour.getCrossingPoints().forEach((integer, crossingPoint) -> {

			float x = crossingPoint.getIntersection().getX() * widthRatio;
			float y = crossingPoint.getIntersection().getY() * heightRatio;
			int radius = 8;

			if (tour.getIdWarehouse() == integer) {
				System.out.println(integer);
				gc.setFill(Color.GREEN);
			} else {
				gc.setFill(Color.WHITE);
			}

			gc.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);

		});

		tour.getSections().forEach((id, section) -> {
			gc.strokeLine(section.getOrigin().getX() * widthRatio, section.getOrigin().getY() * heightRatio,
					section.getDestination().getX() * widthRatio, section.getDestination().getY() * heightRatio);
			// root.getChildren().add(line);
			String deliverys = filler2.getText() + "Rue : " + section.getStreet() + " / Destination : "
					+ section.getDestination().getId() + "\r\n";
			filler2.setText(deliverys);
		});

		//filler1.setText(filler1.getText() + "\r\n Total duration : " + (int) tour.getDuration()/1000 + "km \r\n");

	}

	/**
	 * Dessine la livraison section par section
	 * 
	 * @param gc
	 *            Objet appelﾃｩ pour dessiner des formes dans un canvas
	 */
	public void drawTour(GraphicsContext gc) {
		noSectionToDraw = 0;
		if (colorToDraw == Color.CYAN)
			colorToDraw = Color.GREENYELLOW;
		else
			colorToDraw = Color.CYAN;
		gc.setStroke(colorToDraw);

		float widthRatio = 0.95f;// (float)CANVAS_WIDTH/(float)SCENE_WIDTH;
		float heightRatio = (float) CANVAS_HEIGHT / (float) SCENE_HEIGHT;

		Timeline drawSections = new Timeline(new KeyFrame(Duration.seconds(0.25), new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				gc.strokeLine(tour.getSections().get(noSectionToDraw).getOrigin().getX() * widthRatio,
						tour.getSections().get(noSectionToDraw).getOrigin().getY() * heightRatio,
						tour.getSections().get(noSectionToDraw).getDestination().getX() * widthRatio,
						tour.getSections().get(noSectionToDraw).getDestination().getY() * heightRatio);
				noSectionToDraw++;
			}
		}));
		drawSections.setCycleCount(tour.getSections().size());
		drawSections.play();
	}
}
