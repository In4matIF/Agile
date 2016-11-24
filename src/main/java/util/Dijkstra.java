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
	
	public Set<Intersection> settledNodes;
	public Set<Intersection> unSettledNodes;
	public Map<Intersection, Intersection> predecessors;
	public Map<Intersection, Integer> distance;
	public Plan plan;

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

    private int getDistance(Intersection node, Intersection target) {
            for (Section edge : plan.getSections()) {
                    if (edge.getOrigin().equals(node)
                                    && edge.getDestination().equals(target)) {
                            return edge.getLength();
                    }
            }
            throw new RuntimeException("Should not happen");
    }

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

    private Intersection getMinimum(Set<Intersection> Intersectiones) {
            Intersection minimum = null;
            for (Intersection Intersection : Intersectiones) {
                    if (minimum == null) {
                            minimum = Intersection;
                    } else {
                            if (getShortestDistance(Intersection) < getShortestDistance(minimum)) {
                                    minimum = Intersection;
                            }
                    }
            }
            return minimum;
    }

    private boolean isSettled(Intersection Intersection) {
            return settledNodes.contains(Intersection);
    }

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