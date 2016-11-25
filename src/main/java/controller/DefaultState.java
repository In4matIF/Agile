package controller;

import model.Plan;
import view.Window;

import java.io.File;

/**
 * Objet State générique pour le design patter State
 */
public class DefaultState implements State{

    @Override
    public void loadPlan(Window window, CommandList commandList, File file) {

    }

    @Override
    public void loadTour(Window window, CommandList commandList, File file) {

    }

    @Override
    public void renitializePlan(Window window, CommandList commandList) {

    }

    @Override
    public void renitializeTour(Window window, CommandList commandList) {

    }

    @Override
    public void generateTourSheet(Window window, CommandList commandList) {

    }
}
