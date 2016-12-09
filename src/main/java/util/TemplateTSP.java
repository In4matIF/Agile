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
 * Classe template du TSP implementant l'algorithme
 */
public abstract class TemplateTSP implements TSP {

	//Variables relatives à la recherche de solution
	private Integer[] meilleureSolution;
	private long coutMeilleureSolution = 0;
	private Boolean tempsLimiteAtteint;
	protected boolean foundSolution = false;
	private boolean isPossible = true;
	protected ArrayList<Integer> listDuree = new ArrayList<Integer>();;
	protected ArrayList<Integer> listCout = new ArrayList<Integer>();;

	//Variables relatives à la recherche d'alternatives
	private Integer[] meilleureAlternative;
	private long coutMeilleureAlternative = 0;
    private long coutAlternative = 0;
    private int maxVus = 0;
	private ArrayList<Pair> obviousIssues = new ArrayList<Pair>();

	public Boolean getTempsLimiteAtteint(){
		return tempsLimiteAtteint;
	}

	public Integer getMeilleureSolution(int i){
		if ((meilleureSolution == null) || (i<0) || (i>=meilleureSolution.length))
			return null;
		return meilleureSolution[i];
	}

	public long getCoutMeilleureSolution(){
		return coutMeilleureSolution;
	}

	public void chercheSolution(long tpsLimite, Graph graph) {
		ArrayList<Integer> vus = new ArrayList<Integer>();
		ArrayList<Integer> nonVus = new ArrayList<Integer>();
		tempsLimiteAtteint = false;
		coutMeilleureSolution = Long.MAX_VALUE;
		coutMeilleureAlternative = Long.MAX_VALUE;
		meilleureSolution = new Integer[graph.getCrossingPoints().size()];

		//Pre-traitement du graphe par le methode isPossible
		if (!isPossible(graph)){
			System.out.println("Impossible !");
			return;
		}

		//Initialisation des listes de duree et de sommets non vus point de livraison
		graph.getCrossingPoints().forEach(
				(integer, crossingPoint) -> {
					listDuree.add(crossingPoint.getDuration());
					if(crossingPoint != null) {
						if (integer != graph.getIdWarehouse())
							nonVus.add(integer);
					}
				}
		);

		//Initialisation de la liste des couts pour chaque chemin reliant les points de livraison
		graph.getPaths().forEach(
				(path) -> {
					listCout.add(path.getDuration());
				}
		);

		//Demarrage de la recherche
		vus.add(graph.getIdWarehouse());//Le premier point vus est l'entrepot
		Warehouse warehouse = (Warehouse)(graph.getCrossingPoints().get(graph.getIdWarehouse()));
		branchAndBound(graph.getIdWarehouse(), nonVus, vus, warehouse.getDepartureTime(), graph, System.currentTimeMillis(), tpsLimite);

		//Post-traitement permettant de recuperer les valeurs de temps d'arrivee, d'attente et de depart en parcourant
		//le trajet de la meilleure solution (alternative ou non)
		Integer [] finalPath;
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

	/**
	 * Methode permettant d'acceder aux paires de points de livraisons ne pouvant etre desservis et relies en respectant
	 * leurs plages horaires
	 * @return Une liste d'objets de type Pair
	 */
    public ArrayList<Pair> getObviousIssues() {
		return obviousIssues;
	}

	/**
	 * Methode permettant de savoir si une solution a ete trouvee à lors de la recherche
	 * @return Un booleen
	 */
	public boolean getFoundSolution() { return foundSolution; }

	/**
	 * Methode permettant d'acceder a la meilleure alternative trouvee a defaut d'une meilleure solution
	 * @param i Un entier representant le rang du point de livraison a acceder
	 * @return Un entier representant l'id d'intersection du point de livraison
	 */
	public Integer getMeilleureAlternative(int i){
		if ((meilleureAlternative == null) || (i<0) || (i>=meilleureAlternative.length))
			return null;
		return meilleureAlternative[i];
	}

	/**
	 * Methode permettant d'acceder au cout de la meilleure alternative trouvee a defaut d'une meilleure solution
	 * @return Un entier long representant le temps de parcours du trajet de la meilleure alternative
	 */
	public long getCoutMeilleureAlternative(){
		return coutMeilleureAlternative;
	}

	/**
	 * Methode abstraite devant etre redefinie par les sous-classes de TemplateTSP et servant a effectuer des coupes
	 * dans l'arbre de recherche
	 * @param sommetCourant Un entier representant le sommet courant
	 * @param nonVus Un tableau contenant l'id de tous les points de livraisons restant à visiter
	 * @return Un entier contenant une borne inferieure du cout des chemins possibles à parcourir à partir du sommet
	 * courant pour effectuer la livraison
	 */
	protected abstract int bound(Integer sommetCourant, ArrayList<Integer> nonVus);

	/**
	 * Methode abstraite devant etre redefinie par les sous-classes de TemplateTSP
	 * @param sommetCrt Un entier representant le sommet courant
	 * @param nonVus Un tableau contenant l'id de tous les points de livraisons restant à visiter
	 * @return Un iterateur d'entier permettant d'iterer sur tous les sommets des points de livraisons non vus
	 */
	protected abstract Iterator<Integer> iterator(Integer sommetCrt, ArrayList<Integer> nonVus);
	
	/**
	 * Methode definissant le patron (template) d'une resolution par separation et evaluation (branch and bound) du TSP
	 * @param sommetCrt Un entier representant le dernier sommet visite
	 * @param nonVus Une liste d'entiers contenant les sommets qui n'ont pas encore ete visites
	 * @param vus Une liste d'entiers contenant les sommets visites (y compris sommetCrt)
	 * @param coutVus Un entier long designant le temps de parcours nécessaire pour parvenir jusqu'au sommet courant
	 * @param graph Un objet Graph contenant toutes les informations sur le graphe sur lequel effectuer la recherche
	 * @param tpsDebut Un entier long designant le moment ou la resolution a commence
	 * @param tpsLimite Un entier long designant la limite de temps pour la resolution
	 */
	void branchAndBound(int sommetCrt, ArrayList<Integer> nonVus, ArrayList<Integer> vus, long coutVus, Graph graph, long tpsDebut, long tpsLimite){
		 //Creation et utilisation d'un omparateur permettant de trier la liste des sommet nonVus en fonction de leur
		 // distance avec le sommet courant
		Comparator<Integer> comparatorNonVus = new Comparator<Integer>() {
			 @Override
			 public int compare(Integer first, Integer second) {
				 long distanceFirst = graph.getCrossingPoints().get(sommetCrt).getPaths().get(first).getDuration();
				 long distanceSecond = graph.getCrossingPoints().get(sommetCrt).getPaths().get(second).getDuration();
				 return -Long.compare(distanceFirst, distanceSecond);
			 }
		};
		nonVus.sort(comparatorNonVus);

		 //Vérification du temps d'execution du programme
		if (System.currentTimeMillis() - tpsDebut > tpsLimite){
			 tempsLimiteAtteint = true;
			 return;
		}

		 //Ajout de l'attente en cas d'arrivée trop en avance
		if(coutVus < graph.getCrossingPoints().get(sommetCrt).getBeginTime()) {
			 coutVus = graph.getCrossingPoints().get(sommetCrt).getBeginTime();
		}

		//Mise à jour de la solution alternative
		if(sommetCrt != graph.getIdWarehouse())
			coutAlternative = coutVus
					+ graph.getCrossingPoints().get(sommetCrt).getDuration() //Duree de livraison du sommet courant
					+ graph.getCrossingPoints().get(sommetCrt).getPaths().get(graph.getIdWarehouse()).getDuration(); //Cout de retour vers l'entrepot
		if (nonVus.size() == 0 && coutVus < graph.getCrossingPoints().get(sommetCrt).getEndTime()){ // Tous les sommmets ont été visités et la dernière contrainte horaire est respectee
			coutVus += graph.getCrossingPoints().get(sommetCrt).getDuration() //Duree de livraison du sommet courant
					+ graph.getCrossingPoints().get(sommetCrt).getPaths().get(graph.getIdWarehouse()).getDuration(); //Cout de retour vers l'entrepot
			if (coutVus < coutMeilleureSolution){ //La solution trouvee est meilleure que la precedente
				vus.toArray(meilleureSolution);
				coutMeilleureSolution = coutVus;
				foundSolution = true;
			}
		} else if (coutVus + bound(sommetCrt, nonVus) < coutMeilleureSolution //Le temps de parcours respecte la limite imposee par bound
				&& coutVus < graph.getCrossingPoints().get(sommetCrt).getEndTime()){ //La dernière contrainte horaire est respectee
			if(vus.size() > maxVus || (coutAlternative < coutMeilleureAlternative && vus.size() == maxVus)) {//Le sommet courant représente une meilleure alternative que la precedente
				meilleureAlternative = new Integer[vus.size()];
				vus.toArray(meilleureAlternative);
				coutMeilleureAlternative = coutAlternative;
				maxVus = vus.size();
			}
			Iterator<Integer> it = iterator(sommetCrt, nonVus);
			while (it.hasNext()){//Pour chaque sommet non vus restant
				//Mise à jour des valeurs pour le passage au prochain sommet
				Integer prochainSommet = it.next();
				vus.add(prochainSommet);
				nonVus.remove(prochainSommet);
				listCout.remove(listCout.indexOf(graph.getCrossingPoints().get(sommetCrt).getPaths().get(prochainSommet).getDuration()));
				listDuree.remove(listDuree.indexOf(graph.getCrossingPoints().get(prochainSommet).getDuration()));

				//Appel recursif de la methode
				branchAndBound(
						prochainSommet, nonVus, vus,
						coutVus + graph.getCrossingPoints().get(sommetCrt).getDuration()
										+ graph.getCrossingPoints().get(sommetCrt).getPaths().get(prochainSommet).getDuration(), //cout pour aller au prochain sommet
						graph, tpsDebut, tpsLimite);

				//Retablissement des valeurs pour les autres prochains sommets non vus
				vus.remove(prochainSommet);
				nonVus.add(prochainSommet);
				listCout.add(graph.getCrossingPoints().get(sommetCrt).getPaths().get(prochainSommet).getDuration());
				listDuree.add(graph.getCrossingPoints().get(prochainSommet).getDuration());
			}
		}
	}

	/**
	 * Methode permettant d'effectuer un pre-traitement sur le graphe afin de reperer par avance l'impossibilité de
	 * relier et livrer deux points de livraisons en respectant leurs contraintes horaires
	 * @param graph Un objet Graph contenant toutes les informations sur le graphe sur lequel effectuer la recherche
	 * @return Un booleen verifiant si le parcours avec respect des contraintes horaires est eventuellement possible
	 */
	boolean isPossible(Graph graph){

		ArrayList<Integer> alreadyVerified = new ArrayList<Integer>(graph.getCrossingPoints().size());

		graph.getCrossingPoints().forEach(
				(integer, crossingPoint) -> {//Pour chaque point de livraison
					alreadyVerified.add(integer);
					graph.getCrossingPoints().forEach(
							(otherInteger, otherCrossingPoint) -> {
								if(!alreadyVerified.contains(otherInteger)){//Pour chaque autre point de livraison
									long interTime = crossingPoint.getDuration()
											+ crossingPoint.getPaths().get(otherInteger).getDuration();
									long invertTime = otherCrossingPoint.getDuration()
											+ otherCrossingPoint.getPaths().get(integer).getDuration();
									if(interTime + crossingPoint.getBeginTime() > otherCrossingPoint.getEndTime()//Les points ne sont pas reliables et livrables
											&& invertTime + otherCrossingPoint.getBeginTime() > crossingPoint.getEndTime())//dans aucun des deux sens
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

