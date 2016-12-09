package util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

/**
 * Classe du TSP heritee du template dedie
 */
public class TSP1 extends TemplateTSP {

	@Override
	protected Iterator<Integer> iterator(Integer sommetCrt, ArrayList<Integer> nonVus) {
		//L'iterateur des sommet non vus sera defini par la classe IteratorSeq
		return new IteratorSeq(nonVus, sommetCrt);
	}

	@Override
	protected int bound(Integer sommetCourant, ArrayList<Integer> nonVus) {
		//La coupe est effectuee en prenant les plus petites valeurs de cout et duree pour les points de livraisons
		// restant
		listCout.sort(Comparator.<Integer>naturalOrder());
		listDuree.sort(Comparator.<Integer>naturalOrder());
		int bound = 0;
		for(int i = 0; i <= nonVus.size(); i ++){
			bound += listCout.get(i) + listDuree.get(i);
		}
		return bound;
	}
}
