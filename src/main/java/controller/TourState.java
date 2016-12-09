package controller;

import model.DeliveryPoint;
import model.Plan;
import model.Tour;
import view.Window;

/**
 * Etat ou la livraison est chargee et calculee
 */
public class TourState extends DefaultState{

    @Override
    public void renitializePlan(Window window, CommandList commandList) throws Exception{
        Window.plan = new Plan();
        Window.tour = new Tour();
        commandList.addCommand(new RenitializePlanCommand());
        Controller.setCurrentState(Controller.initState);
    }

    @Override
    public void renitializeTour(Window window, CommandList commandList) throws Exception{
        Window.tour = new Tour();
        commandList.addCommand(new RenitializeTourCommand());
        Controller.setCurrentState(Controller.planState);
    }

    @Override
    public void generateTourSheet(Window window, CommandList commandList) throws Exception{
    	commandList.addCommand(new GenerateTourSheetCommand());
        Controller.setCurrentState(Controller.tourState);
    }
    
    public void deleteDeliveryPoint(Window window, CommandList commandList, DeliveryPoint toDelete) throws Exception
    {
    	commandList.addCommand(new DeleteDeliveryPointCommand(toDelete));
    	Controller.setCurrentState(Controller.tourState);
    }
    
    public void addDeliveryPoint(Window window, CommandList commandList, DeliveryPoint toAdd) throws Exception
    {
    	commandList.addCommand(new AddDeliveryPointCommand(toAdd));
    	Controller.setCurrentState(Controller.tourState);
    }
    
}
