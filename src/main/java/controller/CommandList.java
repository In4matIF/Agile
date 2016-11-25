package controller;

import java.util.LinkedList;
import java.util.List;

/**
 * Liste des commandes effectuées dans la session actuelle
 */
public class CommandList {

    List<Command> commands;

    public CommandList() {
        commands = new LinkedList<>();
    }

    public CommandList(List<Command> commands) {
        this.commands = commands;
    }

    /**
     * Ajoute une commande à la liste
     * @param command la commande à jouer
     * @return true si la commande a été correctement rajoutée
     */
    public boolean addCommand(Command command){
        boolean success = command.doCommand();
        if(success) commands.add(command);
        return success;
    }

}
