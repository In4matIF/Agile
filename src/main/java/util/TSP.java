package util;

import model.Graph;

/**
 * Interface pour le TSP
 */
public interface TSP {

	/**
	 * Methode permettant de savoir si le temps limite est atteint
	 * @return Un booleen
	 */
	public Boolean getTempsLimiteAtteint();


	/**
	 * Methode permettant d'acceder a la meilleure solution trouvee jusqu'alors, s'il en existe une
	 * @param i Un entier representant le rang du point de livraison a acceder
	 * @return Un entier representant l'id d'intersection du point de livraison
	 */
	public Integer getMeilleureSolution(int i);

	/**
	 * Methode permettant d'acceder au cout de la meilleure solution trouvee jusqu'alors, s'il en existe une
	 * @return Un entier long representant le temps de parcours du trajet de la meilleure solution
	 */
	public long getCoutMeilleureSolution();

	/**
	 * Methode devant etre redefinie par les sous-classes de TemplateTSP
	 * @param tpsLimite Un entier long contenant le temps limite d'execution de la recherche
	 * @param graph Un objet Graph contenant toutes les informations sur le graphe sur lequel effectuer la recherche
	 */
	public void chercheSolution(long tpsLimite, Graph graph);
}
