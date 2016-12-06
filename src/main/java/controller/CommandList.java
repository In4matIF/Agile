package controller;

import java.util.LinkedList;
import java.util.List;

/**
 * Liste des commandes effectu�es dans la session actuelle
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
     * Ajoute une commande � la liste
     * @param command la commande � jouer
     * @return true si la commande a �t� correctement rajout�e
     */
    public boolean addCommand(Command command){
        boolean success = command.doCommand();
        if(success) commands.add(command);
        return success;
    }
    
    public boolean undoCommand(){
    	if (commands.size() == 0)
    		return false;
    	else
    		return commands.pop().undoCommand();
    		
    }

}
