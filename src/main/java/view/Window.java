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
import javafx.scene.layout.*;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
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
import javafx.stage.Modality;

import java.awt.Font;
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

	private final int SCENE_WIDTH = 1220;
	private final int SCENE_HEIGHT = 850;
	private final int CANVAS_WIDTH = 750;
	private final int CANVAS_HEIGHT = 650;
	private final int TEXT_AREA_DELIVERY_WIDTH = 425;
	private final int TEXT_AREA_DELIVERY_HEIGHT = 650;
	private final int TEXT_AREA_SHEET_WIDTH = 280;
	private final int TEXT_AREA_SHEET_HEIGHT = 350;
	private final int RECTANGLE_WIDTH = 400;
	private final int RECTANGLE_HEIGHT = 100;

	private final float WIDTH_RATIO = 0.95f;// (float)CANVAS_WIDTH/(float)SCENE_WIDTH;
	private final float HEIGHT_RATIO = (float) CANVAS_HEIGHT / (float) SCENE_HEIGHT;

	private final int SELECTED_CIRCLE_RADIUS = 14;
	private final int NON_SELECTED_CIRCLE_RADIUS = 6;

	private final Color PLAN_INTERSECTION_COLOR = Color.web("#00373A");
	private final Color PLAN_SECTION_COLOR = Color.web("#00373A");
	private final Color TOUR_DELIVERY_COLOR = Color.web("#E5514D");
	private final Color TOUR_WHAREHOUSE_COLOR = Color.web("#E5B34D");
	private final Color TOUR_PATH_COLOR = Color.web("#3E589A");
	private final Color TOUR_VISITED_SECTION_COLOR = Color.web("#E5514D");
	private final Color DELIVERY_BG = Color.web("#17767A");
	private final Color DELIVERY_DETAIL_BG = Color.web("#2F868A");
	private final Color DELIVERY_TEXT_COLOR = Color.web("#B0E1E3");
	private final Color LABEL_TEXT_COLOR = Color.web("#B0E1E3");
	private final Color WINDOW_BACKGROUND = Color.web("#17767A");
	private final Color BORDER_COLOR = Color.web("#B0E1E3");
	private final String BUTTON_COLOR = "#78C1C4";
	private final String BUTTON_FONT_COLOR = "#024F53";

	private final int DETAILS_FONT_SIZE = 20;

	private final String PLAN_FILE_TEXT = "Fichier Plan";
	private final String TOUR_FILE_TEXT = "Fichier Tour";

	private Stage primaryStage;
	private Scene scene;
	private Group root;
	private Controller controller;

	private FlowPane deliveryPane;
	private Group planCanvas;
	private List<GridPane> deliveryGP;
	private ScrollPane deliveryPaneScroll;
	private Map<Integer,Boolean> openState;

	public static Plan plan;
	private String currentPlanFile = PLAN_FILE_TEXT;
	public static Tour tour;
	private String currentTourFile = TOUR_FILE_TEXT;

	static int noSectionToDraw = 0;
	static List<Integer> timesPast = new ArrayList<Integer>();
	static int timePast=0;
	static int timeTotal=0;
	private Color colorToDraw = TOUR_VISITED_SECTION_COLOR;

	public Window() {
	}

	/**
	 * Cree une nouvelle fenetre
	 *
	 * @param primaryStage
	 *            Le container principale de l'interface
	 */
	public Window(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.plan = new Plan();
		this.tour = new Tour();
		this.deliveryPane = new FlowPane();
		this.planCanvas = new Group();
		this.deliveryGP = new ArrayList<>();
		this.deliveryPaneScroll = new ScrollPane();
		this.openState = new HashMap<>();
		this.controller = new Controller(this);
	}

	/**
	 * Recupere le controleur de l'application (MVC)
	 *
	 * @return Le controleur de l'application (MVC)
	 */
	public Controller getController() {
		return controller;
	}

	/**
	 * Change la valeur du controleur de l'application (MVC)
	 *
	 * @param controller
	 *            Le nouveau controleur de l'application (MVC)
	 */
	public void setController(Controller controller) {
		this.controller = controller;
	}

	// Initialise l'interface
	private void init(){
		this.deliveryPane = new FlowPane();
		this.planCanvas = new Group();
		this.deliveryGP = new ArrayList<>();
		this.deliveryPaneScroll = new ScrollPane();
		this.openState = new HashMap<>();
	}

	/**
	 * Configure la gestion des panels dans l'interface graphique, ainsi que les
	 * actions liees aux boutons de l'interface
	 */
	public void render() {
		init();

		// GRID PANE
		GridPane grid = new GridPane();
		grid.setHgap(20.0);
		grid.setVgap(20.0);
		grid.setPadding(new Insets(10.0));
		grid.setAlignment(Pos.CENTER);

		// AREA LOAD PLAN
		final TextField planText = new TextField(currentPlanFile);
		planText.setStyle("-fx-control-inner-background: "+BUTTON_COLOR+"; -fx-text-fill: "+BUTTON_FONT_COLOR+";");
		planText.setEditable(false);
		grid.add(planText, 0, 1);

		Button planBtn = new Button("charger plan");
		planBtn.setStyle("-fx-base: "+BUTTON_COLOR+"; -fx-text-fill: "+BUTTON_FONT_COLOR+";");
		grid.add(planBtn, 1, 1);

		Button suprimerPlanBtn = new Button("X");
		suprimerPlanBtn.setStyle("-fx-base: "+BUTTON_COLOR+"; -fx-text-fill: "+BUTTON_FONT_COLOR+";");
		suprimerPlanBtn.setMaxWidth(20);
		grid.add(suprimerPlanBtn, 2, 1);

		// AREA LOAD DELIVERY
		final TextField livraisonText = new TextField(currentTourFile);
		livraisonText.setStyle("-fx-control-inner-background: "+BUTTON_COLOR+"; -fx-text-fill: "+BUTTON_FONT_COLOR+";");
		livraisonText.setEditable(false);
		grid.add(livraisonText, 0, 2);

		Button livraisonBtn = new Button("charger livraison");
		livraisonBtn.setStyle("-fx-base: "+BUTTON_COLOR+"; -fx-text-fill: "+BUTTON_FONT_COLOR+";");
		grid.add(livraisonBtn, 1, 2);

		Button supprimerLivraisonBtn = new Button("X");
		supprimerLivraisonBtn.setStyle("-fx-base: "+BUTTON_COLOR+"; -fx-text-fill: "+BUTTON_FONT_COLOR+";");
		supprimerLivraisonBtn.setMaxWidth(20);
		grid.add(supprimerLivraisonBtn, 2, 2);

		//INFORMATION DURING THE STEP TO STEP ANIMATION
		GridPane infosPos = new GridPane();
		infosPos.setHgap(30.0);
		infosPos.setVgap(2.0);
		infosPos.setPadding(new Insets(2.0));
		infosPos.setAlignment(Pos.CENTER_LEFT);
		grid.add(infosPos, 3, 1);

		Label stepDisplay1 = new Label("Etape");
		stepDisplay1.setTextFill(LABEL_TEXT_COLOR);
		infosPos.add(stepDisplay1, 0, 0);

		Label stepDisplay2 = new Label("0/0");
		stepDisplay2.setTextFill(LABEL_TEXT_COLOR);
		infosPos.add(stepDisplay2, 1, 0);

		Label nextStreet1 = new Label("Prochaine rue ");
		nextStreet1.setTextFill(LABEL_TEXT_COLOR);
		infosPos.add(nextStreet1, 0, 1);

		Label nextStreet2 = new Label();
		nextStreet2.setTextFill(LABEL_TEXT_COLOR);
		infosPos.add(nextStreet2, 1, 1);

		GridPane infosTime = new GridPane();
		infosTime.setHgap(30.0);
		infosTime.setVgap(2.0);
		infosTime.setPadding(new Insets(2.0));
		infosTime.setAlignment(Pos.CENTER_LEFT);
		grid.add(infosTime, 3, 2);

		Label timePast1 = new Label("Temps Passé");
		timePast1.setTextFill(LABEL_TEXT_COLOR);
		infosTime.add(timePast1, 0, 0);

		Label timePast2 = new Label(formatSecondTime(0,true));
		timePast2.setTextFill(LABEL_TEXT_COLOR);
		infosTime.add(timePast2, 1, 0);

		Label timeLeft1 = new Label("Temps Restant");
		timeLeft1.setTextFill(LABEL_TEXT_COLOR);
		infosTime.add(timeLeft1, 0, 1);

		Label timeLeft2 = new Label(formatSecondTime(0,true));
		timeLeft2.setTextFill(LABEL_TEXT_COLOR);
		infosTime.add(timeLeft2, 1, 1);

		// PLAN GROUP
		planCanvas.setAutoSizeChildren(true);
		planCanvas.minHeight(CANVAS_HEIGHT);
		planCanvas.maxHeight(CANVAS_HEIGHT);
		planCanvas.minWidth(CANVAS_WIDTH);
		planCanvas.maxWidth(CANVAS_WIDTH);
		// PLAN AREA RECTANGLE
		Rectangle rectangle = new Rectangle(CANVAS_WIDTH,CANVAS_HEIGHT);
		rectangle.setFill(DELIVERY_DETAIL_BG);
		planCanvas.getChildren().add(rectangle);
		grid.add(planCanvas, 0, 0, 3, 1);

		//Delivery - titles
		Text title = new Text(new String("                             adresse   arrive      --   depart       duree"));
		title.setStroke(DELIVERY_TEXT_COLOR);

		// Delivery Panel
		GridPane deliveryLegendPane = new GridPane();
		BorderPane center = new BorderPane();
		deliveryPaneScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		deliveryPaneScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		deliveryPaneScroll.setPrefWidth(TEXT_AREA_DELIVERY_WIDTH);
		deliveryPaneScroll.setFitToHeight(true);
		deliveryPaneScroll.setFitToWidth(true);
		deliveryPaneScroll.setStyle("-fx-background-color: #2F868A;");
		deliveryPane.setStyle("-fx-background-color: #2F868A;");
		deliveryPane.setVgap(5);
		grid.add(deliveryLegendPane,3,0,2,1);
		deliveryLegendPane.add(center,0,0);
		center.setBottom(title);
		deliveryLegendPane.add(deliveryPaneScroll,0,1);
		deliveryPaneScroll.setContent(deliveryPane);
    	deliveryLegendPane.setPrefHeight(TEXT_AREA_DELIVERY_HEIGHT);
    	deliveryLegendPane.setPrefWidth(TEXT_AREA_DELIVERY_WIDTH);
    	deliveryLegendPane.setAlignment(Pos.CENTER);
    	deliveryPane.setPrefHeight(TEXT_AREA_DELIVERY_HEIGHT);
    	deliveryPane.setPrefWidth(TEXT_AREA_DELIVERY_WIDTH);

		Button feuilleBtn = new Button("generer feuille de route");
		feuilleBtn.setStyle("-fx-base: "+BUTTON_COLOR+"; -fx-text-fill: "+BUTTON_FONT_COLOR+";");
		grid.add(feuilleBtn, 4, 1);

		//ADD LIVRAISON
		Button popup = new Button();
		popup.setText("Ajouter une livraison");
		popup.setStyle("-fx-base: "+BUTTON_COLOR+"; -fx-text-fill: "+BUTTON_FONT_COLOR+";");
		popup.setOnAction(
	        new EventHandler<ActionEvent>() {
	            @Override
	            public void handle(ActionEvent event) {
	                final Stage dialog = new Stage();
	                dialog.initModality(Modality.APPLICATION_MODAL);;
	                dialog.initOwner(primaryStage);
	                GridPane ajoutGrid = new GridPane();
	                Label addAddress = new Label("Adresse : ");
	                ajoutGrid.add(addAddress, 0, 0);
	                Label addBeginTime = new Label("Début : ");
	                ajoutGrid.add(addBeginTime, 0, 1);
	                Label addEndTime = new Label("Fin : ");
	                ajoutGrid.add(addEndTime, 0, 2);
	                Label addDuration = new Label("Durée : ");
	                ajoutGrid.add(addDuration, 0, 3);
	                TextField tfAddress = new TextField();
	                tfAddress.setPrefSize(100, 30);
	                ajoutGrid.add(tfAddress, 1, 0);
	                TextField tfBeginTime = new TextField();
	                tfBeginTime.setPrefSize(100, 30);
	                ajoutGrid.add(tfBeginTime, 1, 1);
	                TextField tfEndTime = new TextField();
	                tfEndTime.setPrefSize(100, 30);
	                ajoutGrid.add(tfEndTime, 1, 2);
	                TextField tfDuration = new TextField();
	                tfDuration.setPrefSize(100, 30);
	                ajoutGrid.add(tfDuration, 1, 3);
	                Button addDelivery = new Button();
	                addDelivery.setText("Valider");
	        		ajoutGrid.add(addDelivery,1,4);
	        		ajoutGrid.setHgap(10.0);
	        		ajoutGrid.setVgap(10.0);
	        		ajoutGrid.setPadding(new Insets(10.0));
	        		ajoutGrid.setAlignment(Pos.CENTER);
	        		addDelivery.setOnAction(
	        				new EventHandler<ActionEvent>() {
	        	            @Override
	        	            public void handle(ActionEvent event) {
	        	                try {
	        	                	controller.addDeliveryPoint(new DeliveryPoint(plan.getIntersections().get(Integer.valueOf(tfAddress.getText())),(long)Long.valueOf(tfBeginTime.getText()),(long)Long.valueOf(tfBeginTime.getText()),Integer.valueOf(tfBeginTime.getText())));
	        		                render();
	        						renderPlan();
	        		                renderLivraison();
	        					} catch (Exception e) {
	        						// TODO Auto-generated catch block
	        						e.printStackTrace();
	        					}
	        	            }
	        	         });
	                Scene dialogScene = new Scene(ajoutGrid, 300, 200);
	                dialog.setScene(dialogScene);
	                dialog.show();
	            }
	         });
		grid.add(popup, 4, 2);
		
		//UNDO BUTTON
		Button undo = new Button();
		undo.setText("Annuler");
		undo.setStyle("-fx-base: "+BUTTON_COLOR+"; -fx-text-fill: "+BUTTON_FONT_COLOR+";");
		grid.add(undo,2,3);
		undo.setOnAction(
				new EventHandler<ActionEvent>() {
	            @Override
	            public void handle(ActionEvent event) {
	                try {
		                controller.undo();
		                render();
						renderPlan();
		                renderLivraison();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            }
	         });

		// test purposes only
		grid.setGridLinesVisible(false);

		// set stage
		root = new Group();
		root.getChildren().add(grid);
		scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT,WINDOW_BACKGROUND);
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
				if(filePlan!=null){
					try {
						controller.renitializePlan();
					}catch (Exception e){}
					currentPlanFile = filePlan.getName();
					currentTourFile = TOUR_FILE_TEXT;
					render();
					planText.setText(currentPlanFile);
					try {
						controller.loadPlan(filePlan);
						renderPlan();
					} catch (Exception e) {
						if(e.getMessage().equals("Fichier incompatible.")){
							currentPlanFile = PLAN_FILE_TEXT;
							render();
							errorPopUp(e.getMessage());
						}else{
							e.printStackTrace();
						}
					}
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
				if(fileLivr!=null) {
					try{
						controller.renitializeDelivery();
						currentTourFile = TOUR_FILE_TEXT;
						render();
						renderPlan();
					} catch (Exception e){

					}
					// disp livraisons
					try {
						boolean isOk = controller.loadTour(fileLivr);
						if (isOk){
							currentTourFile = fileLivr.getName();
							livraisonText.setText(currentTourFile);
							render();
							renderPlan();
							stepDisplay2.setText("0/" + tour.getSections().size());
							for(int i=0;i<tour.getSections().size();i++)
							{
								int duration=0;
								if(tour.getCrossingPoints().get(tour.getSections().get(i).getDestination().getId())!=null)
								{
									duration = tour.getSections().get(i).getDurationSeconds()
									+tour.getCrossingPoints().get(tour.getSections().get(i).getDestination().getId()).getDuration();
									if(tour.getSections().get(i).getDestination().getId() != tour.getIdWarehouse())
										duration+=((DeliveryPoint)tour.getCrossingPoints().get(tour.getSections().get(i).getDestination().getId())).getWaitTime();
								}
								else
									duration = tour.getSections().get(i).getDurationSeconds();
								timesPast.add(duration);
								timeTotal+=duration;
							}
							timeLeft2.setText(formatSecondTime(timeTotal,true));
							renderLivraison();
						}else{
							errorPopUp("Le calcul d'une tournée optimale pour ce fichier est impossible.");
						}
					} catch (Exception e) {
						if(e.getMessage()!=null && e.getMessage().equals("Fichier incompatible.")){
							currentTourFile = TOUR_FILE_TEXT;
							render();
							try{
								renderPlan();
							}catch (Exception e2){}
							errorPopUp(e.getMessage());
						}else{
							e.printStackTrace();
						}
					}
				}
			}

		});

		suprimerPlanBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent supPlanEvent) {
				try{
					controller.renitializePlan();
					currentPlanFile = PLAN_FILE_TEXT;
					currentTourFile = TOUR_FILE_TEXT;
					render();
				} catch (Exception e){

				}
				//controller.addDeliveryPoint(new DeliveryPoint(plan.getIntersections().get(80), 0, Long.MAX_VALUE, 900));
			}
		});

		supprimerLivraisonBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent supLivrEvent) {
				try{
					controller.renitializeDelivery();
					currentTourFile = TOUR_FILE_TEXT;
					render();
					renderPlan();
				} catch (Exception e){

				}
			}
		});

		feuilleBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent feiulleEvent) {
				try {
					controller.generateTourSheet();
					errorPopUp("La feuille de route a été générée avec succès !");
				}catch (Exception e){}
			}
		});

		//STEP BY STEP CONTROL
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if(event.getCode() == KeyCode.RIGHT)
				{
					event.consume();
					timePast+=timesPast.get(noSectionToDraw);
					timeLeft2.setText(formatSecondTime(timeTotal-timePast,true));
					timePast2.setText(formatSecondTime(timePast,true));
					drawNextStep();
					stepDisplay2.setText(noSectionToDraw+"/"+tour.getSections().size());
					nextStreet2.setText(tour.getSections().get(noSectionToDraw).getStreet());
				}
				else if(event.getCode() == KeyCode.LEFT)
				{
					event.consume();
					if(noSectionToDraw>0)
						timePast-=timesPast.get(noSectionToDraw-1);
					else
						timePast=timeTotal-timesPast.get(timesPast.size()-1);
					timeLeft2.setText(formatSecondTime(timeTotal-timePast,true));
					timePast2.setText(formatSecondTime(timePast,true));
					drawPreviousStep();
					stepDisplay2.setText(noSectionToDraw+"/"+tour.getSections().size());
					nextStreet2.setText(tour.getSections().get(noSectionToDraw).getStreet());
				}
			}
		});

		planCanvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent livraisonEvent) {
				planCanvas.requestFocus();
			}

		});

	}

	/**
	 * Gere l'affichage du plan en canvas dans l'interface graphique
	 *
	 */
	public void renderPlan() throws Exception {

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

			Circle circle = new Circle(x, y, NON_SELECTED_CIRCLE_RADIUS, PLAN_INTERSECTION_COLOR);

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
	 * Gere l'affichage du plan en canvas dans l'interface graphique
	 */
	public void renderLivraison()
			throws Exception {

		for(int i=0; i<tour.getOrdainedCrossingPoints().size(); i++) {
			if(tour.getOrdainedCrossingPoints().get(i) instanceof DeliveryPoint )
			{
			int id = tour.getOrdainedCrossingPoints().get(i).getIntersection().getId();
			DeliveryPoint p = (DeliveryPoint)tour.getOrdainedCrossingPoints().get(i);
			VBox deliveryVB = new VBox();
			GridPane tmpGP = new GridPane();
			tmpGP.setHgap(10.0);
			tmpGP.setVgap(10.0);
			tmpGP.setPadding(new Insets(10.0));
			tmpGP.setAlignment(Pos.CENTER);
			deliveryGP.add(tmpGP);
			Rectangle temp = new Rectangle(RECTANGLE_WIDTH,RECTANGLE_HEIGHT);
			temp.setFill(DELIVERY_BG);
			temp.setStroke(DELIVERY_BG);
			temp.setArcHeight(3.5);
			temp.setArcWidth(3.5);
			Text textAdresse = new Text(String.valueOf(id));
			textAdresse.setStroke(DELIVERY_TEXT_COLOR);
			Text textAttente = new Text(formatSecondTime(p.getWaitTime(),false));
			textAttente.setStroke(DELIVERY_TEXT_COLOR);
			StackPane circleAdresseStack = new StackPane();
			Circle circleAdresse = new Circle(20,Color.TRANSPARENT);
			circleAdresse.setStroke(Color.WHITE);
			circleAdresse.setStrokeWidth(4);
			circleAdresseStack.getChildren().add(circleAdresse);
			circleAdresseStack.getChildren().add(textAdresse);
			Text arriveH = new Text(formatSecondTime(p.getArrival(),false));
			arriveH.setStroke(DELIVERY_TEXT_COLOR);
			Text fillerDash = new Text(new String("--"));
			fillerDash.setStroke(DELIVERY_TEXT_COLOR);
			Text departH = new Text(formatSecondTime(p.getDeparture(),false));
			departH.setStroke(DELIVERY_TEXT_COLOR);
			tmpGP.add(circleAdresseStack, 0,0);
			tmpGP.add(arriveH, 1, 0);
			tmpGP.add(fillerDash, 3, 0);
			tmpGP.add(departH, 4, 0);
			StackPane stack = new StackPane();
			deliveryVB.getChildren().add(stack);
			stack.getChildren().add(temp);
			stack.getChildren().add(tmpGP);
			StackPane circleStack = new StackPane();

			Circle circleAttente = new Circle(20,Color.TRANSPARENT);
			circleAttente.setStrokeWidth(4);
			long x = p.getWaitTime();
			if(x<(long)600)
			{
				circleAttente.setStroke(Color.RED);
			}
			else if(x<(long)1800)
			{
				circleAttente.setStroke(Color.ORANGE);
			}
			else
			{
				circleAttente.setStroke(Color.GREEN);
			}
			tmpGP.add(circleStack, NON_SELECTED_CIRCLE_RADIUS, 0);
			circleStack.getChildren().add(circleAttente);
			circleStack.getChildren().add(textAttente);
			deliveryPane.getChildren().add(deliveryVB);

			Rectangle clickable = new Rectangle(RECTANGLE_WIDTH,RECTANGLE_HEIGHT);
			clickable.setFill(Color.TRANSPARENT);
			clickable.setStroke(BORDER_COLOR);

			openState.put(tour.getOrdainedCrossingPoints().get(i).getIntersection().getId(),false);

			//Details des livraisons
			clickable.setOnMouseClicked(new EventHandler<MouseEvent>()
	        {
	            @Override
	            public void handle(MouseEvent t) {
	            	if(openState.get(id) == false)
	            	{
		            StackPane stackDetails = new StackPane();
		            GridPane gridDetails = new GridPane();
		            Rectangle othercolor = new Rectangle(RECTANGLE_WIDTH,RECTANGLE_HEIGHT+200);
	            	othercolor.setFill(DELIVERY_DETAIL_BG);
	    			othercolor.setStroke(DELIVERY_DETAIL_BG);
	    			othercolor.setArcHeight(3.5);
	    			othercolor.setArcWidth(3.5);
	            	gridDetails.setHgap(10.0);
	            	gridDetails.setVgap(10.0);
	            	gridDetails.setPadding(new Insets(10.0));
	            	gridDetails.setAlignment(Pos.CENTER);
	            	Button supprimer = new Button("Supprimer");
					supprimer.setStyle("-fx-base: "+BUTTON_COLOR+"; -fx-text-fill: "+BUTTON_FONT_COLOR+";");
					supprimer.setOnAction(
							new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent event) {
									try {
										controller.deleteDeliveryPoint(p);
										render();
										renderPlan();
										renderLivraison();
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							});
	            	Button modifier = new Button("Modifier");
					modifier.setStyle("-fx-base: "+BUTTON_COLOR+"; -fx-text-fill: "+BUTTON_FONT_COLOR+";");
	            	Label adresseLabel =  new Label("Adresse : ");
	            	adresseLabel.setTextFill(DELIVERY_TEXT_COLOR);
	            	adresseLabel.setStyle("-fx-font-size:"+DETAILS_FONT_SIZE+" px");
	            	TextField adresse = new TextField(String.valueOf(id));
	            	Label arriveeLabel =  new Label("Arrivée : ");
	            	arriveeLabel.setTextFill(DELIVERY_TEXT_COLOR);
	            	arriveeLabel.setStyle("-fx-font-size:"+DETAILS_FONT_SIZE+" px");
	            	TextField arrivee = new TextField(formatSecondTime(p.getArrival(),true));
	            	Label debutLivraisonLabel =  new Label("Début livraison : ");
	            	debutLivraisonLabel.setTextFill(DELIVERY_TEXT_COLOR);
	            	debutLivraisonLabel.setStyle("-fx-font-size:"+DETAILS_FONT_SIZE+" px");
	            	TextField debutLivraison = new TextField(formatSecondTime(p.getArrival()+p.getWaitTime(),true));
	            	Label departLabel =  new Label("Départ : ");
	            	departLabel.setStyle("-fx-font-size:"+DETAILS_FONT_SIZE+" px");
	            	departLabel.setTextFill(DELIVERY_TEXT_COLOR);
	            	TextField depart = new TextField(formatSecondTime(p.getDeparture(),true));
	            	Label attenteLabel =  new Label("Attente : ");
	            	attenteLabel.setStyle("-fx-font-size:"+DETAILS_FONT_SIZE+" px");
	            	attenteLabel.setTextFill(DELIVERY_TEXT_COLOR);
	            	TextField attente = new TextField(formatSecondTime(p.getWaitTime(),true));
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
	            	Circle circle = (Circle) getPlanCircle(p.getIntersection().getId());
	            	if(circle!=null){
	            		circle.setRadius(SELECTED_CIRCLE_RADIUS);
					}
	            	openState.put(id, true);
	            	}
	            	else
	            	{
	            		deliveryVB.getChildren().clear();
	            		deliveryVB.getChildren().add(stack);
						Circle circle = (Circle) getPlanCircle(p.getIntersection().getId());
						if(circle!=null){
							circle.setRadius(NON_SELECTED_CIRCLE_RADIUS);
						}
	            		openState.put(id, false);
	            	}
	            }
	        });
			stack.getChildren().add(clickable);
			}
		};

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
				}
		);

		tour.getCrossingPoints().forEach((integer, crossingPoint) -> {

			float x = crossingPoint.getIntersection().getX() * WIDTH_RATIO;
			float y = crossingPoint.getIntersection().getY() * HEIGHT_RATIO;
			Circle circle = new Circle(x, y, NON_SELECTED_CIRCLE_RADIUS);
			circle.setId(integer.toString());

			if (tour.getIdWarehouse() == integer) {
				circle.setFill(TOUR_WHAREHOUSE_COLOR);
			} else {
				circle.setFill(TOUR_DELIVERY_COLOR);
			}
			planCanvas.getChildren().add(circle);

		});
	}

	public void drawNextStep() {
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
			timePast=0;
			if(colorToDraw == TOUR_PATH_COLOR)
				colorToDraw = TOUR_VISITED_SECTION_COLOR;
			else
				colorToDraw = TOUR_PATH_COLOR;
		}
	}

	public void drawPreviousStep() {
		if(noSectionToDraw == 0)
		{
			noSectionToDraw=tour.getSections().size();
			timePast=timeTotal;
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

	private String formatSecondTime(long time,boolean showSeconds){
		long seconds = time % 60;
		long totalMinutes = time / 60;
		long minutes = totalMinutes % 60;
		long hours = totalMinutes / 60;
		String result;
		if(showSeconds){
			result = (hours<10?"0"+hours:hours) + ":" + (minutes<10?"0"+minutes:minutes) + ":" + (seconds<10?"0"+seconds:seconds);
		}else{
			result = (hours<10?"0"+hours:hours) + ":" + (minutes<10?"0"+minutes:minutes);
		}
		return result;
	}

	public void errorPopUp(String errorMessage)
	{
		final Stage dialog = new Stage();
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.initOwner(primaryStage);
		Label errorLabel = new Label(errorMessage);
		BorderPane bp = new BorderPane();
		bp.setCenter(errorLabel);
		Button close = new Button("Close");
		close.setOnAction(
				new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						dialog.hide();
					}
				});
		bp.setBottom(close);
		Scene dialogScene = new Scene(bp, 500, 200);
		dialog.setScene(dialogScene);
		dialog.show();
	}

	private Node getPlanCircle(Integer id){
		ArrayList<Node> result = new ArrayList();
		planCanvas.getChildren().forEach(
			node -> {
				if(node instanceof Circle){
					if(node.getId()!=null && node.getId().equals(id.toString())){
						result.add(node);
					}
				}
			}
		);
		if(result.size()>0) return result.get(0);
		return null;
	}

}
