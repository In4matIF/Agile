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
import javafx.scene.control.Label;
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
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.control.ScrollPane;
import model.DeliveryPoint;
import model.Intersection;
import model.Plan;
import model.Tour;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.prism.BasicStroke;

/**
 * Classe d'interface graphique
 */
public class Window {

	private final int SCENE_WIDTH = 1400;
	private final int SCENE_HEIGHT = 800;
	private final int CANVAS_WIDTH = 750;
	private final int CANVAS_HEIGHT = 650;
	private final int TEXT_AREA_DELIVERY_WIDTH = 380;
	private final int TEXT_AREA_DELIVERY_HEIGHT = 650;
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

	private Map<Integer,Boolean> OpenState = new HashMap<>();
	
	public static Plan plan;
	public static Tour tour;

	static int noSectionToDraw = 0;
	private Color colorToDraw = TOUR_VISITED_SECTION_COLOR;

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

		Button supprimerLivraisonBtn = new Button("X");
		supprimerLivraisonBtn.setMaxWidth(20);
		grid.add(supprimerLivraisonBtn, 2, 2);

		//INFOS LORS DU PARCOURS
		GridPane infosPos = new GridPane();
		infosPos.setHgap(30.0);
		infosPos.setVgap(2.0);
		infosPos.setPadding(new Insets(2.0));
		infosPos.setAlignment(Pos.CENTER_LEFT);
		grid.add(infosPos, 3, 1);
		
		Label stepDisplay1 = new Label("Etape");
		infosPos.add(stepDisplay1, 0, 0);
		
		Label stepDisplay2 = new Label("0/0");
		infosPos.add(stepDisplay2, 1, 0);
		
		Label nextStreet1 = new Label("Prochaine rue ");
		infosPos.add(nextStreet1, 0, 1);
		
		Label nextStreet2 = new Label();
		infosPos.add(nextStreet2, 1, 1);
		
		GridPane infosTime = new GridPane();
		infosTime.setHgap(30.0);
		infosTime.setVgap(2.0);
		infosTime.setPadding(new Insets(2.0));
		infosTime.setAlignment(Pos.CENTER_LEFT);
		grid.add(infosTime, 3, 2);
		
		Label timePast1 = new Label("Temps Passé");
		infosTime.add(timePast1, 0, 0);
		
		Label timePast2 = new Label("0mn");
		infosTime.add(timePast2, 1, 0);
		
		Label timeLeft1 = new Label("Temps Restant");
		infosTime.add(timeLeft1, 0, 1);
		
		Label timeLeft2 = new Label("0mn");
		infosTime.add(timeLeft2, 1, 1);

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
											// up 1 row

		//Delivery - titles
		Text title = new Text(new String("adresse   arrive -- depart   duree"));

		// Delivery Panel
		GridPane deliveryLegendPane = new GridPane();
		List<GridPane> deliveryGP = new ArrayList<GridPane>() ;
		final ScrollPane deliveryPaneScroll = new ScrollPane();
		deliveryPaneScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		deliveryPaneScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		deliveryPaneScroll.setPrefWidth(TEXT_AREA_DELIVERY_WIDTH);
		deliveryPaneScroll.setFitToHeight(true);
		final FlowPane deliveryPane = new FlowPane();
		deliveryPane.setVgap(5);
		grid.add(deliveryLegendPane,3,0,1,1);
		deliveryLegendPane.add(title,0,0);
		deliveryLegendPane.add(deliveryPaneScroll,0,1);
		deliveryPaneScroll.setContent(deliveryPane);
    	deliveryLegendPane.setPrefHeight(TEXT_AREA_DELIVERY_HEIGHT);
    	deliveryLegendPane.setPrefWidth(TEXT_AREA_DELIVERY_WIDTH);
    	deliveryPane.setPrefHeight(TEXT_AREA_DELIVERY_HEIGHT);
    	deliveryPane.setPrefWidth(TEXT_AREA_DELIVERY_WIDTH);
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
				/*File f = new File("xml");
				try {
					System.out.println(f.getCanonicalPath());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}*/
				//planChooser.setInitialDirectory(new File("src\\main\\resources\\xml"));
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
				//livrChooser.setInitialDirectory(new File("src\\main\\resources\\xml"));
				File fileLivr = livrChooser.showOpenDialog(primaryStage);
				livraisonText.setText(fileLivr.getName());
				controller.loadTour(fileLivr);
				stepDisplay2.setText("0/"+tour.getSections().size());
				// disp livraisons
				try {
					renderLivraison(deliveryPane, filler2, planCanvas, deliveryGP,OpenState);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});

		suprimerPlanBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent supPlanEvent) {
				//plan = new Plan();
				controller.deleteDeliveryPoint((DeliveryPoint)tour.getCrossingPoints().get(203));
				try {
					renderLivraison(deliveryPane, filler2, planCanvas, deliveryGP,OpenState);
				} catch (Exception e) {
					e.printStackTrace();
				}
				//gc.setFill(Color.TAN);
				//gc.fillRect(0, 0, planCanvas.getWidth(), planCanvas.getHeight());
			}
		});

		supprimerLivraisonBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent supLivrEvent) {
				controller.undo();
			}
		});

		feuilleBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent feiulleEvent) {
				/*File output = new File("FeuilleDeRoute.txt");
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
				}*/
				controller.generateTourSheet();
			}
		});
		
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
            	if(event.getCode() == KeyCode.RIGHT)
                {
                	event.consume();
                	drawNextStep(planCanvas);
                	stepDisplay2.setText(noSectionToDraw+"/"+tour.getSections().size());
                	nextStreet2.setText(tour.getSections().get(noSectionToDraw).getStreet());
                }
                else if(event.getCode() == KeyCode.LEFT)
                {
                	event.consume();
                	drawPreviousStep(planCanvas);
                	stepDisplay2.setText(noSectionToDraw+"/"+tour.getSections().size());
                	nextStreet2.setText(tour.getSections().get(noSectionToDraw).getStreet());
                }
            }
        });
		
		

	}

	/**
	 * Gﾃｨre l'affichage du plan en canvas dans l'interface graphique
	 * 
	 * @param planCanvas
	 *            L'objet canvas contenant les formes géométrique à dessiner
	 */
	public void renderPlan(Group planCanvas) throws Exception {

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

	}

	/**
	 * Gﾃｨre l'affichage du plan en canvas dans l'interface graphique
	 * 
	 * @param deliveryPane
	 *            Contient les informations sur les livraisons
	 * @param filler2
	 *            Contient les informations de la feuille de route
	 * @param planCanvas
	 *            L'objet canvas contenant les formes géométrique à dessiner
	 */
	public void renderLivraison(FlowPane deliveryPane, TextArea filler2, Group planCanvas, List<GridPane> deliveryGP, Map<Integer, Boolean> openState)
			throws Exception {
		filler2.setText("");

		tour.getCrossingPoints().forEach((id,deliveryPoint) -> {
			if(id!=tour.getIdWarehouse())
			{
			VBox deliveryVB = new VBox();
			GridPane tmpGP = new GridPane();
			tmpGP.setHgap(10.0);
			tmpGP.setVgap(10.0);
			tmpGP.setPadding(new Insets(10.0));
			tmpGP.setAlignment(Pos.CENTER);
			deliveryGP.add(tmpGP);
			Rectangle temp = new Rectangle(400,100);
			temp.setFill(new LinearGradient(0,0,0,1, true, CycleMethod.NO_CYCLE,
			        new Stop[]{
			        new Stop(0,Color.web("#4977A3")),
			        new Stop(0.5, Color.web("#B0C6DA")),
			        new Stop(1,Color.web("#9CB6CF")),}));
			    temp.setStroke(Color.web("#D0E6FA"));
			    temp.setArcHeight(3.5);
			    temp.setArcWidth(3.5);
			Text text = new Text(new String("Adresse : " + deliveryPoint.getIntersection().getId() + "\r\n durée : " + deliveryPoint.getDuration()+ "\r\n arrivée : " + deliveryPoint.getBeginTime()+ "\r\n départ : " + deliveryPoint.getEndTime()));
			Text textAttente = new Text(new String ("0"));
			Circle adresse = new Circle(20,Color.TRANSPARENT);
			adresse.setStroke(Color.WHITE);
			Text arriveH = new Text(new String(toString().valueOf(((DeliveryPoint)deliveryPoint).getArrival()/3600)));
			Text arriveM = new Text(new String(toString().valueOf(((DeliveryPoint)deliveryPoint).getArrival()%3600/60)));
			Text fillerDash = new Text(new String("--"));
			Text departH = new Text(new String(toString().valueOf(((DeliveryPoint)deliveryPoint).getDeparture()/3600)));
			Text departM = new Text(new String(toString().valueOf(((DeliveryPoint)deliveryPoint).getDeparture()%3600/60)));
			tmpGP.add(adresse, 0,0);
			tmpGP.add(arriveH, 1, 0);
			tmpGP.add(arriveM, 2, 0);
			tmpGP.add(fillerDash, 3, 0);
			tmpGP.add(departH, 4, 0);
			tmpGP.add(departM, 5, 0);
			StackPane stack = new StackPane();
			deliveryVB.getChildren().add(stack);
			stack.getChildren().add(temp);
			stack.getChildren().add(tmpGP);
			StackPane circleStack = new StackPane();
			Circle circleAttente = new Circle(20,Color.TRANSPARENT);
			if(0<10)
			{
				circleAttente.setStroke(Color.RED);
			}
			else if(0<30)
			{
				circleAttente.setStroke(Color.ORANGE);
			}
			else
			{
				circleAttente.setStroke(Color.GREEN);
			}
			tmpGP.add(circleStack, 6, 0);
			circleStack.getChildren().add(circleAttente); 
			circleStack.getChildren().add(textAttente);
			deliveryPane.getChildren().add(deliveryVB);
			
			Rectangle clickable = new Rectangle(400,100);
			clickable.setFill(Color.TRANSPARENT);
			clickable.setStroke(Color.BLACK);
			
			openState.put(id,false);
			
			clickable.setOnMouseClicked(new EventHandler<MouseEvent>()
	        {
	            @Override
	            public void handle(MouseEvent t) {
	            	if(openState.get(id) == false)
	            	{
		            StackPane stackDetails = new StackPane();
		            GridPane gridDetails = new GridPane();
		            Rectangle othercolor = new Rectangle(400,300);
	            	othercolor.setFill(new LinearGradient(0,0,0,1, true, CycleMethod.NO_CYCLE,
	    			        new Stop[]{
	    			        new Stop(0,Color.web("#4977A3")),
	    			        new Stop(0.5, Color.web("#B0C6DA")),
	    			        new Stop(1,Color.web("#9CB6CF")),}));
	    			    othercolor.setStroke(Color.web("#D0E6FA"));
	    			    othercolor.setArcHeight(3.5);
	    			    othercolor.setArcWidth(3.5);
	            	gridDetails.setHgap(10.0);
	            	gridDetails.setVgap(10.0);
	            	gridDetails.setPadding(new Insets(10.0));
	            	gridDetails.setAlignment(Pos.CENTER);
	            	Button supprimer = new Button("Supprimer");
	            	Button modifier = new Button("Modifier");
	            	Label adresseLabel =  new Label("Adresse : ");
	            	TextField adresse = new TextField(id.toString());
	            	Label arriveeLabel =  new Label("Arrivée : ");
	            	TextField arrivee = new TextField(String.valueOf(((DeliveryPoint)deliveryPoint).getArrival()));
	            	Label debutLivraisonLabel =  new Label("Début livraison : ");
	            	TextField debutLivraison = new TextField(String.valueOf((((DeliveryPoint)deliveryPoint).getArrival())+((DeliveryPoint)deliveryPoint).getWaitTime()));
	            	Label departLabel =  new Label("Départ : ");
	            	TextField depart = new TextField(String.valueOf(((DeliveryPoint)deliveryPoint).getDeparture()));
	            	Label attenteLabel =  new Label("Attente : ");
	            	TextField attente = new TextField(String.valueOf(((DeliveryPoint)deliveryPoint).getWaitTime()));
	            	adresse.setDisable(true);
	            	arrivee.setDisable(true);
	            	depart.setDisable(true);
	            	debutLivraison.setDisable(true);
	            	attente.setDisable(true);
	            	gridDetails.add(adresseLabel,0,0,3,1);
	            	gridDetails.add(adresse,4,0,3,1);
	            	gridDetails.add(arriveeLabel,0,1,3,1);
	            	gridDetails.add(arrivee,4,1,3,1);
	            	gridDetails.add(debutLivraisonLabel,0,2,3,1);
	            	gridDetails.add(debutLivraison,4,2,3,1);
	            	gridDetails.add(departLabel,0,3,3,1);
	            	gridDetails.add(depart,4,3,3,1);
	            	gridDetails.add(attenteLabel,0,4,3,1);
	            	gridDetails.add(attente,4,4,3,1);
	            	gridDetails.add(supprimer,0,5,3,1);
	            	gridDetails.add(modifier, 4,5,3,1);
	            	stackDetails.getChildren().add(othercolor);
	            	stackDetails.getChildren().add(gridDetails);
	            	deliveryVB.getChildren().add(stackDetails);
	            	stackDetails.setUserData(id);
	            	openState.put(id, true);
	            	}
	            	else
	            	{
	            		deliveryVB.getChildren().clear();
	            		deliveryVB.getChildren().add(stack);
	            		openState.put(id, false);
	            	}
	            }
	        });
			stack.getChildren().add(clickable);
			}
		});

		tour.getSections().forEach(
				(section) -> {
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

		//filler1.setText(filler1.getText() + "\r\n Total duration : " + (int) tour.getDuration()/1000 + "km \r\n");

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
	
	public void drawNextStep(Group planCanvas) {	
		float xOrigin = tour.getSections().get(noSectionToDraw).getOrigin().getX() * WIDTH_RATIO;
		float yOrigin = tour.getSections().get(noSectionToDraw).getOrigin().getY() * HEIGHT_RATIO;
		float xDestination = tour.getSections().get(noSectionToDraw).getDestination().getX() * WIDTH_RATIO;
		float yDestination = tour.getSections().get(noSectionToDraw).getDestination().getY() * HEIGHT_RATIO;
		Line line = new Line(xOrigin, yOrigin, xDestination, yDestination);
		line.setStrokeWidth(3);
		line.setStroke(colorToDraw);
		planCanvas.getChildren().add(line);
		noSectionToDraw++;
		if(noSectionToDraw >= tour.getSections().size())
		{
			noSectionToDraw=0;
			if(colorToDraw == TOUR_PATH_COLOR)
				colorToDraw = TOUR_VISITED_SECTION_COLOR;
			else
				colorToDraw = TOUR_PATH_COLOR;
		}
	}
	
	public void drawPreviousStep(Group planCanvas) {	
		if(noSectionToDraw == 0)
		{
			noSectionToDraw=tour.getSections().size();
			if(colorToDraw == TOUR_PATH_COLOR)
				colorToDraw = TOUR_VISITED_SECTION_COLOR;
			else
				colorToDraw = TOUR_PATH_COLOR;
		}
		float xOrigin = tour.getSections().get(noSectionToDraw-1).getOrigin().getX() * WIDTH_RATIO;
		float yOrigin = tour.getSections().get(noSectionToDraw-1).getOrigin().getY() * HEIGHT_RATIO;
		float xDestination = tour.getSections().get(noSectionToDraw-1).getDestination().getX() * WIDTH_RATIO;
		float yDestination = tour.getSections().get(noSectionToDraw-1).getDestination().getY() * HEIGHT_RATIO;
		Line line = new Line(xOrigin, yOrigin, xDestination, yDestination);
		line.setStrokeWidth(3);
		if(colorToDraw == TOUR_PATH_COLOR)
			line.setStroke(TOUR_VISITED_SECTION_COLOR);
		else
			line.setStroke(TOUR_PATH_COLOR);
		planCanvas.getChildren().add(line);
		noSectionToDraw--;
	}
}
