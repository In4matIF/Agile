package controller;

/**
 * Interface pour le design pattern Command
 */
public interface Command {

    public boolean doCommand();
    public boolean undoCommand();
    public boolean isDoable();
}
