package util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

import javafx.util.Pair;
import javafx.util.converter.IntegerStringConverter;
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
    protected ArrayList<Integer> listDuree = new ArrayList<Integer>();;
    protected ArrayList<Integer> listCout = new ArrayList<Integer>();;
    private long coutAlternative = 0;
    private int maxVus = 0;
    private long coutMeilleureAlternative = 0;
    private Integer[] meilleureAlternative;

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
		coutMeilleureAlternative = Long.MAX_VALUE;
		dureeMinimale = Integer.MAX_VALUE;
		coutMinimal = Integer.MAX_VALUE;
		meilleureSolution = new Integer[graph.getCrossingPoints().size()];

		ArrayList<Integer> vus = new ArrayList<Integer>();
		ArrayList<Integer> nonVus = new ArrayList<Integer>();

		if (!isPossible(graph)){
			System.out.println("Impossible !");
			return;
		}

		graph.getCrossingPoints().forEach(
                (integer, crossingPoint) -> {
                    listDuree.add(crossingPoint.getDuration());
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
                    listCout.add(path.getDuration());
                	if(path.getDuration() < coutMinimal)
                    	coutMinimal = path.getDuration();
                }
        );

		vus.add(graph.getIdWarehouse());
		Warehouse warehouse = (Warehouse)(graph.getCrossingPoints().get(graph.getIdWarehouse()));
		branchAndBound(graph.getIdWarehouse(), nonVus, vus, warehouse.getDepartureTime(), graph, System.currentTimeMillis(), tpsLimite);

		Integer [] finalPath = new Integer[graph.getCrossingPoints().size()];
		if(foundSolution) {
            finalPath = meilleureSolution;
        }else {
            finalPath = meilleureAlternative;
        }
        long crtTime = warehouse.getDepartureTime();
        for(int i = 1; i < finalPath.length; i ++) {
            DeliveryPoint crtPoint = (DeliveryPoint) (graph.getCrossingPoints().get(finalPath[i]));
            crtTime += graph.getCrossingPoints().get(finalPath[i - 1]).getPaths().get(finalPath[i]).getDuration();
            if (crtTime < crtPoint.getBeginTime()) {
                crtPoint.setWaitTime(crtPoint.getBeginTime() - crtTime);
            } else {
                crtPoint.setWaitTime(0);
            }
            crtPoint.setArrival(crtTime);
            crtTime += crtPoint.getWaitTime() + crtPoint.getDuration();
            crtPoint.setDeparture(crtTime);
        }

	}
	
	public Integer getMeilleureSolution(int i){
		if ((meilleureSolution == null) || (i<0) || (i>=meilleureSolution.length))
			return null;
		return meilleureSolution[i];
	}

	public Integer getMeilleureAlternative(int i){
		if ((meilleureAlternative == null) || (i<0) || (i>=meilleureAlternative.length))
			return null;
		return meilleureAlternative[i];
	}
	
	public long getCoutMeilleureSolution(){
		return coutMeilleureSolution;
	}

	public long getCoutMeilleureAlternative(){
		return coutMeilleureAlternative;
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
		 Comparator<Integer> comparatorNonVus = new Comparator<Integer>() {
			 @Override
			 public int compare(Integer first, Integer second) {
				 long distanceFirst = graph.getCrossingPoints().get(sommetCrt).getPaths().get(first).getDuration();
				 long distanceSecond = graph.getCrossingPoints().get(sommetCrt).getPaths().get(second).getDuration();
				 return -Long.compare(distanceFirst, distanceSecond);
			 }
		 };
		 nonVus.sort(comparatorNonVus);

		 if (System.currentTimeMillis() - tpsDebut > tpsLimite){
			 tempsLimiteAtteint = true;
			 return;
		 }
		 if(coutVus < graph.getCrossingPoints().get(sommetCrt).getBeginTime()) {
			 coutVus = graph.getCrossingPoints().get(sommetCrt).getBeginTime();
		 }
		 if(sommetCrt != graph.getIdWarehouse())
		    coutAlternative = coutVus + graph.getCrossingPoints().get(sommetCrt).getDuration() + graph.getCrossingPoints().get(sommetCrt).getPaths().get(graph.getIdWarehouse()).getDuration();
	    if (nonVus.size() == 0 && coutVus < graph.getCrossingPoints().get(sommetCrt).getEndTime()){ // tous les sommets ont ete visites
            //coutVus += graph.getCrossingPoints().get(sommetCrt).getPaths().get(graph.getIdWarehouse()).getLength(); // on ajoute le dernier cout retour vers l'ntrepot
            coutVus += graph.getCrossingPoints().get(sommetCrt).getDuration() + graph.getCrossingPoints().get(sommetCrt).getPaths().get(graph.getIdWarehouse()).getDuration(); // on ajoute le dernier cout retour vers l'ntrepot
	    	if (coutVus < coutMeilleureSolution){ // on a trouve une solution meilleure que meilleureSolution
	    		vus.toArray(meilleureSolution);
	    		coutMeilleureSolution = coutVus;
	    		foundSolution = true;
	    	}
	    } else if (coutVus + bound(sommetCrt, nonVus) < coutMeilleureSolution && coutVus < graph.getCrossingPoints().get(sommetCrt).getEndTime()){
            if(vus.size() > maxVus || (coutAlternative < coutMeilleureAlternative && vus.size() == maxVus)) {
                meilleureAlternative = new Integer[vus.size()];
                vus.toArray(meilleureAlternative);
                coutMeilleureAlternative = coutAlternative;
                maxVus = vus.size();
            }
	        Iterator<Integer> it = iterator(sommetCrt, nonVus);
	        while (it.hasNext()){
	        	Integer prochainSommet = it.next();
	        	vus.add(prochainSommet);
	        	nonVus.remove(prochainSommet);
                listCout.remove(listCout.indexOf(graph.getCrossingPoints().get(sommetCrt).getPaths().get(prochainSommet).getDuration()));
                listDuree.remove(listDuree.indexOf(graph.getCrossingPoints().get(prochainSommet).getDuration()));

	        	branchAndBound(
                        prochainSommet, nonVus, vus,
                        //coutVus + graph.getCrossingPoints().get(sommetCrt).getPaths().get(prochainSommet).getLength() //cout pour aller au prochain sommet
                        coutVus + graph.getCrossingPoints().get(sommetCrt).getDuration()
										+ graph.getCrossingPoints().get(sommetCrt).getPaths().get(prochainSommet).getDuration(), //cout pour aller au prochain sommet
                        graph, tpsDebut, tpsLimite);

	        	vus.remove(prochainSommet);
	        	nonVus.add(prochainSommet);
                listCout.add(graph.getCrossingPoints().get(sommetCrt).getPaths().get(prochainSommet).getDuration());
                listDuree.add(graph.getCrossingPoints().get(prochainSommet).getDuration());
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

