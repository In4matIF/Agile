package util;

import java.util.ArrayList;
import java.util.Iterator;

import javafx.util.Pair;
import model.CrossingPoint;
import model.DeliveryPoint;
import model.Graph;
import model.Warehouse;

/**
 * Classe template du TSP impl�mentant l'algorithme
 */
public abstract class TemplateTSP implements TSP {
	
	private Integer[] meilleureSolution;
	private long coutMeilleureSolution = 0;
	private Boolean tempsLimiteAtteint;
    protected int dureeMinimale = 0;
    protected int coutMinimal = 0;
    protected boolean foundSolution = false;
    private boolean isPossible = true;

	private ArrayList<Pair> obviousIssues = new ArrayList<Pair>();

	public ArrayList<Pair> getObviousIssues() {
		return obviousIssues;
	}
	
	public Boolean getTempsLimiteAtteint(){
		return tempsLimiteAtteint;
	}

	public boolean getFoundSolution() { return foundSolution; }

	public void chercheSolution(long tpsLimite, Graph graph) {
		tempsLimiteAtteint = false;
		coutMeilleureSolution = Long.MAX_VALUE;
		dureeMinimale = Integer.MAX_VALUE;
		coutMinimal = Integer.MAX_VALUE;
		meilleureSolution = new Integer[graph.getCrossingPoints().size()];

		ArrayList<Integer> vus = new ArrayList<Integer>(graph.getCrossingPoints().size());
		ArrayList<Integer> nonVus = new ArrayList<Integer>();

		if (!isPossible(graph)){
			System.out.println("Impossible !");
			return;
		}

		graph.getCrossingPoints().forEach(
                (integer, crossingPoint) -> {
                    if(crossingPoint != null) {
						if (integer != graph.getIdWarehouse())
							nonVus.add(integer);
						if (crossingPoint.getDuration() < dureeMinimale)
							dureeMinimale = crossingPoint.getDuration();
					}
                }
		);
        graph.getPaths().forEach(
                (path) -> {
                    //if(path.getLength() < coutMinimal)
                	if(path.getDuration() < coutMinimal)
                        //coutMinimal = path.getLength();
                    	coutMinimal = path.getDuration();
                }
        );
		vus.add(graph.getIdWarehouse());
		Warehouse warehouse = (Warehouse)(graph.getCrossingPoints().get(graph.getIdWarehouse()));
		branchAndBound(graph.getIdWarehouse(), nonVus, vus, warehouse.getDepartureTime(), graph, System.currentTimeMillis(), tpsLimite);

		if(foundSolution){
			long crtTime = warehouse.getDepartureTime();
			for(int i = 1; i < meilleureSolution.length; i ++){
				DeliveryPoint crtPoint = (DeliveryPoint) (graph.getCrossingPoints().get(meilleureSolution[i]));
				crtTime += graph.getCrossingPoints().get(meilleureSolution[i-1]).getPaths().get(meilleureSolution[i]).getDuration();
				if(crtTime < crtPoint.getBeginTime()){
					crtPoint.setWaitTime(crtPoint.getBeginTime() - crtTime);
				}else{
					crtPoint.setWaitTime(0);
				}
				crtPoint.setArrival(crtTime);
				crtTime += crtPoint.getWaitTime() + crtPoint.getDuration();
				crtPoint.setDeparture(crtTime);
			}
		}
	}
	
	public Integer getMeilleureSolution(int i){
		if ((meilleureSolution == null) || (i<0) || (i>=meilleureSolution.length))
			return null;
		return meilleureSolution[i];
	}
	
	public long getCoutMeilleureSolution(){
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
	 void branchAndBound(int sommetCrt, ArrayList<Integer> nonVus, ArrayList<Integer> vus, long coutVus, Graph graph, long tpsDebut, long tpsLimite){
		 if (System.currentTimeMillis() - tpsDebut > tpsLimite){
			 tempsLimiteAtteint = true;
			 return;
		 }
		 if(coutVus < graph.getCrossingPoints().get(sommetCrt).getBeginTime()) {
			 coutVus = graph.getCrossingPoints().get(sommetCrt).getBeginTime();
		 }
	    if (nonVus.size() == 0){ // tous les sommets ont ete visites
            //coutVus += graph.getCrossingPoints().get(sommetCrt).getPaths().get(graph.getIdWarehouse()).getLength(); // on ajoute le dernier cout retour vers l'ntrepot
            coutVus += graph.getCrossingPoints().get(sommetCrt).getDuration() + graph.getCrossingPoints().get(sommetCrt).getPaths().get(graph.getIdWarehouse()).getDuration(); // on ajoute le dernier cout retour vers l'ntrepot
	    	if (coutVus < coutMeilleureSolution){ // on a trouve une solution meilleure que meilleureSolution
	    		vus.toArray(meilleureSolution);
	    		coutMeilleureSolution = coutVus;
	    		foundSolution = true;
	    	}
	    } else if (coutVus + bound(sommetCrt, nonVus) < coutMeilleureSolution && coutVus < graph.getCrossingPoints().get(sommetCrt).getEndTime()){
	        Iterator<Integer> it = iterator(sommetCrt, nonVus);
	        while (it.hasNext()){
	        	Integer prochainSommet = it.next();
	        	vus.add(prochainSommet);
	        	nonVus.remove(prochainSommet);
	        	branchAndBound(
                        prochainSommet, nonVus, vus,
                        //coutVus + graph.getCrossingPoints().get(sommetCrt).getPaths().get(prochainSommet).getLength() //cout pour aller au prochain sommet
                        coutVus + graph.getCrossingPoints().get(sommetCrt).getDuration()
										+ graph.getCrossingPoints().get(sommetCrt).getPaths().get(prochainSommet).getDuration(), //cout pour aller au prochain sommet
                        graph, tpsDebut, tpsLimite);
	        	vus.remove(prochainSommet);
	        	nonVus.add(prochainSommet);
	        }	    
	    }
	}

	boolean isPossible(Graph graph){

		ArrayList<Integer> alreadyVerified = new ArrayList<Integer>(graph.getCrossingPoints().size());

		graph.getCrossingPoints().forEach(
				(integer, crossingPoint) -> {
					alreadyVerified.add(integer);
					graph.getCrossingPoints().forEach(
							(otherInteger, otherCrossingPoint) -> {
								if(!alreadyVerified.contains(otherInteger)){
									long interTime = crossingPoint.getDuration()
											+ crossingPoint.getPaths().get(otherInteger).getDuration();
									long invertTime = otherCrossingPoint.getDuration()
											+ otherCrossingPoint.getPaths().get(integer).getDuration();
									if(interTime + crossingPoint.getBeginTime() > otherCrossingPoint.getEndTime()
											&& invertTime + otherCrossingPoint.getBeginTime() > crossingPoint.getEndTime() )
										isPossible = false;
										obviousIssues.add(new Pair(crossingPoint,otherCrossingPoint));
								}
							}
					);
				}
		);
	 	return isPossible;
	}
}

