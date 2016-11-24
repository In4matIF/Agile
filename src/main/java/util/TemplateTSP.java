package util;

import java.util.ArrayList;
import java.util.Iterator;

import model.Graph;

public abstract class TemplateTSP implements TSP {
	
	private Integer[] meilleureSolution;
	private int coutMeilleureSolution = 0;
	private Boolean tempsLimiteAtteint;
    protected int dureeMinimale = 0;
    protected int coutMinimal = 0;
	
	public Boolean getTempsLimiteAtteint(){
		return tempsLimiteAtteint;
	}

	public void chercheSolution(int tpsLimite, Graph graph) {
		tempsLimiteAtteint = false;
		coutMeilleureSolution = Integer.MAX_VALUE;
        dureeMinimale = Integer.MAX_VALUE;
        coutMinimal = Integer.MAX_VALUE;
		meilleureSolution = new Integer[graph.getCrossingPoints().size()];
		ArrayList<Integer> nonVus = new ArrayList<Integer>();
		graph.getCrossingPoints().forEach(
                (integer, crossingPoint) -> {
                    if(crossingPoint != null)
                    {
	                	if (integer != graph.getIdWarehouse())
	                        nonVus.add(integer);
	                    if(crossingPoint.getDuration() < dureeMinimale)
	                        dureeMinimale = crossingPoint.getDuration();
                    }
                }
		);
        graph.getPaths().forEach(
                (path) -> {
                    if(path.getLength() < coutMinimal)
                        dureeMinimale = path.getLength();
                }
        );
		ArrayList<Integer> vus = new ArrayList<Integer>(graph.getCrossingPoints().size());
		vus.add(graph.getIdWarehouse());
		branchAndBound(graph.getIdWarehouse(), nonVus, vus, 0, graph, System.currentTimeMillis(), tpsLimite);
	}
	
	public Integer getMeilleureSolution(int i){
		if ((meilleureSolution == null) || (i<0) || (i>=meilleureSolution.length))
			return null;
		return meilleureSolution[i];
	}
	
	public int getCoutMeilleureSolution(){
		return coutMeilleureSolution;
	}
	
	/**
	 * Methode devant etre redefinie par les sous-classes de TemplateTSP
	 * @param sommetCourant
	 * @param nonVus : tableau des sommets restant a visiter
	 * @return une borne inferieure du cout des permutations commencant par sommetCourant,
	 * contenant chaque sommet de nonVus exactement une fois et terminant par le sommet 0
	 */
	protected abstract int bound(Integer sommetCourant, ArrayList<Integer> nonVus);
	
	/**
	 * Methode devant etre redefinie par les sous-classes de TemplateTSP
	 * @param sommetCrt
	 * @param nonVus : tableau des sommets restant a visiter
	 * @return un iterateur permettant d'iterer sur tous les sommets de nonVus
	 */
	protected abstract Iterator<Integer> iterator(Integer sommetCrt, ArrayList<Integer> nonVus);
	
	/**
	 * Methode definissant le patron (template) d'une resolution par separation et evaluation (branch and bound) du TSP
	 * @param sommetCrt le dernier sommet visite
	 * @param nonVus la liste des sommets qui n'ont pas encore ete visites
	 * @param vus la liste des sommets visites (y compris sommetCrt)
	 * @param coutVus la somme des couts des arcs du chemin passant par tous les sommets de vus + la somme des duree des sommets de vus
	 * @param graph : structure du graphe complet des livraisons
	 * @param tpsDebut : moment ou la resolution a commence
	 * @param tpsLimite : limite de temps pour la resolution
	 */	
	 void branchAndBound(int sommetCrt, ArrayList<Integer> nonVus, ArrayList<Integer> vus, int coutVus, Graph graph, long tpsDebut, int tpsLimite){
         if (System.currentTimeMillis() - tpsDebut > tpsLimite){
			 tempsLimiteAtteint = true;
			 return;
		 }
	    if (nonVus.size() == 0){ // tous les sommets ont ete visites
            coutVus += graph.getCrossingPoints().get(sommetCrt).getPaths().get(graph.getIdWarehouse()).getLength(); // on ajoute le dernier cout retour vers l'ntrepot
	    	if (coutVus < coutMeilleureSolution){ // on a trouve une solution meilleure que meilleureSolution
	    		vus.toArray(meilleureSolution);
	    		coutMeilleureSolution = coutVus;
	    	}
	    } else if (coutVus + bound(sommetCrt, nonVus) < coutMeilleureSolution){
	        Iterator<Integer> it = iterator(sommetCrt, nonVus);
	        while (it.hasNext()){
	        	Integer prochainSommet = it.next();
	        	vus.add(prochainSommet);
	        	nonVus.remove(prochainSommet);
	        	branchAndBound(
                        prochainSommet, nonVus, vus,
                        coutVus + graph.getCrossingPoints().get(sommetCrt).getPaths().get(prochainSommet).getLength() //cout pour aller au prochain sommet
                                + graph.getCrossingPoints().get(prochainSommet).getDuration(), //duree du prochain sommet
                        graph, tpsDebut, tpsLimite);
	        	vus.remove(prochainSommet);
	        	nonVus.add(prochainSommet);
	        }	    
	    }
	}
}

