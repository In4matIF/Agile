import controller.Controller;
import javafx.application.Application;
import javafx.stage.Stage;
import model.Plan;
import view.Window;

/**
 * Classe main du programme
 * Lance l'UI
 */
public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        super.init();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    /**
     * Ouvre la fenetre principale
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Window window = new Window(primaryStage);
        window.render();
    }
}
