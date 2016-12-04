package controller;

import model.DeliveryPoint;
import view.Window;

/**
 * Etat o� la livraison est charg�e et calcul�e
 */
public class TourState extends DefaultState{

    @Override
    public void renitializePlan(Window window, CommandList commandList) {
        Controller.setCurrentState(Controller.initState);
    }

    @Override
    public void renitializeTour(Window window, CommandList commandList) {
        Controller.setCurrentState(Controller.planState);
    }

    @Override
    public void generateTourSheet(Window window, CommandList commandList) {
    	commandList.addCommand(new GenerateTourSheetCommand());
        Controller.setCurrentState(Controller.tourState);
    }
    
    public void deleteDeliveryPoint(Window window, CommandList commandList, DeliveryPoint toDelete)
    {
    	commandList.addCommand(new DeleteDeliveryPointCommand(toDelete));
    	Controller.setCurrentState(Controller.tourState);
    }
}
