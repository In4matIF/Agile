package controller;

/**
 * Interface pour le design pattern Command
 */
public interface Command {

    public boolean doCommand() throws Exception;
    public boolean undoCommand();
    public boolean isDoable();
}
