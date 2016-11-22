package controller;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Olivice on 18/11/2016.
 */
public class CommandList {

    List<Command> commands;

    public CommandList() {
        commands = new LinkedList<>();
    }

    public CommandList(List<Command> commands) {
        this.commands = commands;
    }

    public boolean addCommand(Command command){
        boolean success = command.doCommand();
        if(success) commands.add(command);
        return success;
    }

}
