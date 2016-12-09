package util;

import java.util.Collection;
import java.util.Iterator;

/**
 * Classe de l'iterateur des sommets derivee de l'iterateur d'entier
 */
public class IteratorSeq implements Iterator<Integer> {

	private Integer[] candidats;
	private int nbCandidats;

	/**
	 * Constructeur de l'iterateur pour iterer sur l'ensemble des sommets de nonVus à partir du sommet courant
	 * @param nonVus Une liste d'entiers contenant l'id des sommets non vus
	 * @param sommetCrt Un entier contenant l'id du sommet courant
	 */
	public IteratorSeq(Collection<Integer> nonVus, int sommetCrt){
		this.candidats = new Integer[nonVus.size()];
		nbCandidats = 0;
		for (Integer s : nonVus){
			candidats[nbCandidats++] = s;
		}
	}
	
	@Override
	public boolean hasNext() {
		return nbCandidats > 0;
	}

	@Override
	public Integer next() {
		return candidats[--nbCandidats];
	}

	@Override
	public void remove() {}

}
