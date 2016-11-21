import javafx.application.Application;
import javafx.stage.Stage;
import view.Window;

/**
 * Created by Olivice on 18/11/2016.
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

    @Override
    public void start(Stage primaryStage) throws Exception {
        Window window = new Window(primaryStage);
        window.renderButton();
    }
}
