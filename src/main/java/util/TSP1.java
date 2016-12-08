package util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

public class TSP1 extends TemplateTSP {

	@Override
	protected Iterator<Integer> iterator(Integer sommetCrt, ArrayList<Integer> nonVus) {
		return new IteratorSeq(nonVus, sommetCrt);
	}

	@Override
	protected int bound(Integer sommetCourant, ArrayList<Integer> nonVus) {
		listCout.sort(Comparator.<Integer>naturalOrder());
		listDuree.sort(Comparator.<Integer>naturalOrder());
		int bound = 0;
		for(int i = 0; i <= nonVus.size(); i ++){
			bound += listCout.get(i) + listDuree.get(i);
		}
		return bound;
	}
}
