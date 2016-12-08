package controller;

import model.DeliveryPoint;
import model.Plan;
import model.Tour;
import view.Window;

/**
 * Etat o� la livraison est charg�e et calcul�e
 */
public class TourState extends DefaultState{

    @Override
    public void renitializePlan(Window window, CommandList commandList) {
        Window.plan = new Plan();
        Window.tour = new Tour();
        commandList.addCommand(new RenitializePlanCommand());
        Controller.setCurrentState(Controller.initState);
    }

    @Override
    public void renitializeTour(Window window, CommandList commandList) {
        Window.tour = new Tour();
        commandList.addCommand(new RenitializeTourCommand());
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
    
    public void addDeliveryPoint(Window window, CommandList commandList, DeliveryPoint toAdd)
    {
    	commandList.addCommand(new AddDeliveryPointCommand(toAdd));
    	Controller.setCurrentState(Controller.tourState);
    }
    
}
