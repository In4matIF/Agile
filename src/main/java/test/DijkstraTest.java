package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import model.Intersection;
import model.Plan;
import model.Section;
import util.Dijkstra;

public class DijkstraTest {
	
	private Dijkstra d;
	private Plan p;
	private Intersection origin;
	private Intersection destination;
	private List<Intersection> path;
	
	@Before
	public void setUp() throws Exception {
		Map<Integer, Intersection> intersections = new HashMap<Integer, Intersection>();
		ArrayList<Section> sections = new ArrayList<Section>();
		
		Intersection i0 = new Intersection(0,0,0);
		Intersection i1 = new Intersection(1,1,0);
		Intersection i2 = new Intersection(2,2,0);
		Intersection i3 = new Intersection(3,1,1);
		Intersection i4 = new Intersection(4,1,2);
		Intersection i5 = new Intersection(5,2,2);
		Intersection i6 = new Intersection(6,3,2);
		
		
		Section s0 = new Section(i0,i1,1,1,"s0");
		Section s1 = new Section(i1,i2,1,1,"s1");
		Section s2 = new Section(i2,i6,1,1,"s2");
		Section s3 = new Section(i0,i3,1,1,"s3");
		Section s4 = new Section(i3,i6,1,1,"s4");
		Section s5 = new Section(i0,i4,1,1,"s5");
		Section s6 = new Section(i4,i5,1,1,"s6");
		Section s7 = new Section(i3,i2,1,1,"s7");
		
		ArrayList<Section> al0 = new ArrayList<Section>();
		al0.add(s0);
		al0.add(s3);
		al0.add(s5);
		i0.setSections(al0);
		ArrayList<Section> al1 = new ArrayList<Section>();
		al1.add(s1);
		i1.setSections(al1);
		ArrayList<Section> al2 = new ArrayList<Section>();
		al2.add(s2);
		i2.setSections(al2);
		ArrayList<Section> al3 = new ArrayList<Section>();
		al3.add(s4);
		al3.add(s7);
		i3.setSections(al3);
		ArrayList<Section> al4 = new ArrayList<Section>();
		al4.add(s6);
		i4.setSections(al4);

		intersections.put(0, i0);
		intersections.put(1, i1);
		intersections.put(2, i2);
		intersections.put(3, i3);
		intersections.put(4, i4);
		intersections.put(5, i5);
		intersections.put(6, i6);
		
		sections.add(s0);
		sections.add(s1);
		sections.add(s2);
		sections.add(s3);
		sections.add(s4);
		sections.add(s5);
		sections.add(s6);
		sections.add(s7);
		
		Plan p = new Plan(intersections, sections);
		
		origin = i0;
		destination = i6;
		
		d = new Dijkstra(p);
		d.execute(origin);
		path = d.getPath(destination);
	}
	
	@Test
	public void testPath()
	{
		Integer p0 = 0;
		Integer p1 = 3;
		Integer p2 = 6;
		
		assertEquals(path.get(0).getId(),p0);
		assertEquals(path.get(1).getId(),p1);
		assertEquals(path.get(2).getId(),p2);
	}
}
