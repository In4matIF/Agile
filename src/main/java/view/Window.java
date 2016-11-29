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
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Intersection;
import model.Plan;
import model.Tour;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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

	private final float WIDTH_RATIO = 0.95f;// (float)CANVAS_WIDTH/(float)SCENE_WIDTH;
	private final float HEIGHT_RATIO = (float) CANVAS_HEIGHT / (float) SCENE_HEIGHT;

	private final Color PLAN_INTERSECTION_COLOR = Color.AQUA;
	private final Color PLAN_SECTION_COLOR = Color.AQUA;
	private final Color TOUR_DELIVERY_COLOR = Color.MEDIUMPURPLE;
	private final Color TOUR_WHAREHOUSE_COLOR = Color.DARKGREEN;
	private final Color TOUR_PATH_COLOR = Color.RED;
	private final Color TOUR_VISITED_SECTION_COLOR = Color.YELLOW;

	private Stage primaryStage;
	private Controller controller;

	public static Plan plan;
	public static Tour tour;

	static int noSectionToDraw = 0;
	private Color colorToDraw = PLAN_INTERSECTION_COLOR;

	public Window() {
	}

	/**
	 * Crée une nouvelle fenêtre
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
	 * Récupère le container PrimaryStage
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
	 * Récupère le contrôleur de l'application (MVC)
	 * 
	 * @return Le contrôleur de l'application (MVC)
	 */
	public Controller getController() {
		return controller;
	}

	/**
	 * Change la valeur du contrôleur de l'application (MVC)
	 * 
	 * @param controller
	 *            Le nouveau contrôleur de l'application (MVC)
	 */
	public void setController(Controller controller) {
		this.controller = controller;
	}

	/**
	 * Configure la gestion des panels dans l'interface graphique, ainsi que les
	 * actions liées aux boutons de l'interface
	 */
	public void render() {

		GridPane grid = new GridPane();
		grid.setHgap(20.0);
		grid.setVgap(20.0);
		grid.setPadding(new Insets(10.0));
		grid.setAlignment(Pos.CENTER);

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

		// PLAN GROUP
		Group planCanvas = new Group();
		planCanvas.setAutoSizeChildren(true);
		planCanvas.minHeight(CANVAS_HEIGHT);
		planCanvas.maxHeight(CANVAS_HEIGHT);
		planCanvas.minWidth(CANVAS_WIDTH);
		planCanvas.maxWidth(CANVAS_WIDTH);
		// PLAN AREA RECTANGLE
		Rectangle rectangle = new Rectangle(CANVAS_WIDTH,CANVAS_HEIGHT);
		rectangle.setFill(Color.TRANSPARENT);
		rectangle.setStroke(Color.BLACK);
		planCanvas.getChildren().add(rectangle);
		grid.add(planCanvas, 0, 0, 3, 1); // col1, row2, takes up 2 cols, takes
											// up 10 rows

		// TODO in future iteration: make these do something
		final TextArea filler1 = new TextArea("[livraison]");
		filler1.setPrefHeight(TEXT_AREA_DELIVERY_HEIGHT);
		filler1.setPrefWidth(TEXT_AREA_DELIVERY_WIDTH);
		grid.add(filler1, 3, 0, 1, 1);
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
					renderPlan(planCanvas);
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
					renderLivraison(filler1, filler2, planCanvas);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});

		suprimerPlanBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent supPlanEvent) {
				plan = new Plan();
				//gc.setFill(Color.TAN);
				//gc.fillRect(0, 0, planCanvas.getWidth(), planCanvas.getHeight());
			}
		});

		suprimerLivraisonBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent supLivrEvent) {
				filler1.setText("TODO: implement this");
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
					drawTour(planCanvas);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});

	}

	/**
	 * Gère l'affichage du plan en canvas dans l'interface graphique
	 * 
	 * @param planCanvas
	 *            L'objet canvas contenant les formes géométrique à dessiner
	 */
	public void renderPlan(Group planCanvas) throws Exception {

		plan.getIntersections().forEach((integer, intersection) -> {
			float x = intersection.getX() * WIDTH_RATIO;
			float y = intersection.getY() * HEIGHT_RATIO;

			Circle circle = new Circle(x, y, 6, PLAN_INTERSECTION_COLOR);

			Tooltip t = new Tooltip("Intersection : "+intersection.getId());
			circle.setOnMouseEntered(
				event -> {
					Node node =(Node)event.getSource(); t.show( node,
			 		primaryStage.getX()+event.getSceneX(),
			 		primaryStage.getY()+event.getSceneY() );
				}
			);
			circle.setOnMouseExited(event -> t.hide());
			planCanvas.getChildren().add(circle);
		});

		plan.getSections().forEach(section -> {
			float xOrigin = section.getOrigin().getX() * WIDTH_RATIO;
			float yOrigin = section.getOrigin().getY() * HEIGHT_RATIO;
			float xDestination = section.getDestination().getX() * WIDTH_RATIO;
			float yDestination = section.getDestination().getY() * HEIGHT_RATIO;
			Line line = new Line(xOrigin, yOrigin, xDestination, yDestination);
			line.setStrokeWidth(3);
			line.setStroke(PLAN_SECTION_COLOR);
			planCanvas.getChildren().add(line);
		});

	}

	/**
	 * Gère l'affichage du plan en canvas dans l'interface graphique
	 * 
	 * @param filler1
	 *            Contient les informations sur les livraisons
	 * @param filler2
	 *            Contient les informations de la feuille de route
	 * @param planCanvas
	 *            L'objet canvas contenant les formes géométrique à dessiner
	 */
	public void renderLivraison(TextArea filler1, TextArea filler2, Group planCanvas)
			throws Exception {
		filler1.setText("");
		filler2.setText("");

		tour.getIntersections().forEach(intersection -> {
			String deliverys = filler1.getText() + "Livraison : " + intersection.getId() + "\r\n";
			filler1.setText(deliverys);
		});

		tour.getCrossingPoints().forEach((integer, crossingPoint) -> {

			float x = crossingPoint.getIntersection().getX() * WIDTH_RATIO;
			float y = crossingPoint.getIntersection().getY() * HEIGHT_RATIO;
			Circle circle = new Circle(x, y, 6);

			if (tour.getIdWarehouse() == integer) {
				circle.setFill(TOUR_WHAREHOUSE_COLOR);
			} else {
				circle.setFill(TOUR_DELIVERY_COLOR);
			}
			planCanvas.getChildren().add(circle);

		});

		tour.getSections().forEach(
			(id, section) -> {
				float xOrigin = section.getOrigin().getX() * WIDTH_RATIO;
				float yOrigin = section.getOrigin().getY() * HEIGHT_RATIO;
				float xDestination = section.getDestination().getX() * WIDTH_RATIO;
				float yDestination = section.getDestination().getY() * HEIGHT_RATIO;
				Line line = new Line(xOrigin, yOrigin, xDestination, yDestination);
				line.setStrokeWidth(3);
				line.setStroke(TOUR_PATH_COLOR);
				planCanvas.getChildren().add(line);
				String deliverys = filler2.getText() + "Rue : " + section.getStreet() + " / Destination : "
						+ section.getDestination().getId() + "\r\n";
				filler2.setText(deliverys);
			}
		);

		filler1.setText(filler1.getText() + "\r\n Total duration : " + (int) tour.getDuration()/1000 + "km \r\n");

	}

	/**
	 * Dessine la livraison section par section
	 *
	 */
	public void drawTour(Group planCanvas) {
		noSectionToDraw = 0;
		if (colorToDraw == PLAN_INTERSECTION_COLOR)
			colorToDraw = TOUR_VISITED_SECTION_COLOR;
		else
			colorToDraw = PLAN_INTERSECTION_COLOR;



		Timeline drawSections = new Timeline(new KeyFrame(Duration.seconds(0.25), new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				float xOrigin = tour.getSections().get(noSectionToDraw).getOrigin().getX() * WIDTH_RATIO;
				float yOrigin = tour.getSections().get(noSectionToDraw).getOrigin().getY() * HEIGHT_RATIO;
				float xDestination = tour.getSections().get(noSectionToDraw).getDestination().getX() * WIDTH_RATIO;
				float yDestination = tour.getSections().get(noSectionToDraw).getDestination().getY() * HEIGHT_RATIO;
				Line line = new Line(xOrigin, yOrigin, xDestination, yDestination);
				line.setStrokeWidth(3);
				line.setStroke(colorToDraw);
				planCanvas.getChildren().add(line);
				noSectionToDraw++;
			}
		}));
		drawSections.setCycleCount(tour.getSections().size());
		drawSections.play();
	}
}
