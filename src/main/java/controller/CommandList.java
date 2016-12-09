package controller;

import java.util.LinkedList;
import java.util.List;

/**
 * Liste des commandes effectuees dans la session actuelle
 */
public class CommandList {

    LinkedList<Command> commands;

    public CommandList() {
        commands = new LinkedList<>();
    }

    public CommandList(LinkedList<Command> commands) {
        this.commands = commands;
    }

    /**
     * Ajoute une commande a la liste
     * @param command la commande a jouer
     * @return true si la commande a ete correctement rajoutee
     */
    public boolean addCommand(Command command) throws Exception{
        boolean success = command.doCommand();
        if(success) commands.push(command);
        return success;
    }
    
    public boolean undoCommand(){
    	if (commands.size() == 0)
    		return false;
    	else if (commands.getFirst().isDoable())
    		return commands.pop().undoCommand();
    	else
    		return false;
    		
    }

}
