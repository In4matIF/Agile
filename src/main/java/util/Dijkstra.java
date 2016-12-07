package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.*;

/**
 * Objet permettant les calculs de plus court chemin avec Dijkstra
 */
public class Dijkstra {
	
	/**
	 * Noeuds visités dans l'algorithme
	 */
	public Set<Intersection> settledNodes;
	
	/**
	 * Noeuds non visités
	 */
	public Set<Intersection> unSettledNodes;
	
	/**
	 * Map associant les intersections avec leur prédécesseurs dans le graphe
	 */
	public Map<Intersection, Intersection> predecessors;
	
	/**
	 * Map associant les Intersections avec leur distance les séparant de l'origine
	 */
	public Map<Intersection, Integer> distance;
	
	/**
	 * Plan sur lequel baser le calcul de Dijkstra
	 */
	public Plan plan;

	/**
	 * Constructeur de l'objet à partir d'un Plan
	 * @param p le plan sur lequel appliquer Dijkstra
	 */
	public Dijkstra(Plan p)
	{
		plan = p;
	}
        
    /**
     * Applique Dijkstra à partir de l'intersection spécifiée
     * @param source l'Intersection de départ
     */
	public void execute(Intersection source) {
        settledNodes = new HashSet<Intersection>();
        unSettledNodes = new HashSet<Intersection>();
        distance = new HashMap<Intersection, Integer>();
        predecessors = new HashMap<Intersection, Intersection>();
        
        distance.put(source, 0);
        unSettledNodes.add(source);
        
        while (unSettledNodes.size() > 0) {
        		Intersection node = getMinimum(unSettledNodes);
                settledNodes.add(node);
                unSettledNodes.remove(node);
                findMinimalDistances(node);
        }
    }
    
	/**
	 * Regarde tous les voisins de l'intersection en paramètre
	 * et la définie comme prédecesseurs si ceux-ci sont à une distance
	 * plus courte en passant par l'intersection
	 * @param node une intersection
	 */
    private void findMinimalDistances(Intersection node) {
        List<Intersection> adjacentNodes = getNeighbors(node);
        for (Intersection target : adjacentNodes) {
                if (getShortestDistance(target) > getShortestDistance(node)
                                + getDistance(node, target)) {
                        distance.put(target, getShortestDistance(node)
                                        + getDistance(node, target));
                        predecessors.put(target, node);
                        unSettledNodes.add(target);
                }
        }

    }

    /**
     * Renvoie la distance entre les intersections node et target
     * @param node l'intersection de départ
     * @param target l'intersection d'arrivée
     * @return la distance entre les deux intersections
     */
    private int getDistance(Intersection node, Intersection target) {
            for (Section edge : plan.getSections()) {
                    if (edge.getOrigin().equals(node)
                                    && edge.getDestination().equals(target)) {
                            //return edge.getLength();
                    		return edge.getDurationSeconds();
                    }
            }
            throw new RuntimeException("Should not happen");
    }

    /**
     * Renvoie les voisins de l'intersection en paramètre
     * @param node une intersection
     * @return les voisins de l'intersection
     */
    private List<Intersection> getNeighbors(Intersection node) {
            List<Intersection> neighbors = new ArrayList<Intersection>();
            for (Section edge : plan.getSections()) {
                    if (edge.getOrigin().equals(node)
                                    && !isSettled(edge.getDestination())) {
                            neighbors.add(edge.getDestination());
                    }
            }
            return neighbors;
    }

    /**
     * Renvoie l'intersection la plus proche de l'origine  parmis les intersections passées en paramètre
     * @param intersections la collection d'intersection
     * @return l'intersection la plus proche parmis la collection
     */
    private Intersection getMinimum(Set<Intersection> intersections) {
            Intersection minimum = null;
            for (Intersection inter : intersections) {
                    if (minimum == null) {
                            minimum = inter;
                    } else {
                            if (getShortestDistance(inter) < getShortestDistance(minimum)) {
                                    minimum = inter;
                            }
                    }
            }
            return minimum;
    }

    /**
     * Renvoie vrai si l'intersection en paramètre est contenue dans la liste des noeuds visités
     * @param Intersection l'intersection à vérifier
     * @return true si l'intersection est contenue dans setttledNodes, false sinon
     */
    private boolean isSettled(Intersection Intersection) {
            return settledNodes.contains(Intersection);
    }

    /**
     * Renvoie la distance la plus courte depuis l'origine vers l'intersection passé en paramètre
     * @param destination l'intersection de destination
     * @return la distance à l'intersection depuis l'origine (ou Integer.MAX_VALUE si pas de chemin existant)
     */
    private int getShortestDistance(Intersection destination) {
            Integer d = distance.get(destination);
            if (d == null) {
                    return Integer.MAX_VALUE;
            } else {
                    return d;
            }
    }

    /**
     * Méthode renvoyant la liste d'intersections correspondants au chemin le plus court
     * entre l'Intersection de départ et l'intersection d'arrrivée spécifiée
     * @param target l'intersection d'arrivée
     * @return la liste des Intersections du chemin le plus court entre le départ et l'arrivée
     */
    public LinkedList<Intersection> getPath(Intersection target) {
            LinkedList<Intersection> path = new LinkedList<Intersection>();
            Intersection step = target;
            // check if a path exists
            if (predecessors.get(step) == null) {
                    return null;
            }
            path.add(step);
            while (predecessors.get(step) != null) {
                    step = predecessors.get(step);
                    path.add(step);
            }
            // Put it into the correct order
            Collections.reverse(path);
            return path;
    }

}