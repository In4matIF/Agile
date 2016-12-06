package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import model.Intersection;
import model.Section;

/**
 * Tests unitaires de JUnit sur la classe Section
 */
public class SectionTest {

	private Section s;
	private int LENGTH = 50;
	private int SPEED = 20;
	@Before
	public void setUp() throws Exception {
		s = new Section(new Intersection(1,25,25),new Intersection(2,0,0),LENGTH,SPEED,"x0");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetOrigin() {
		Integer i = 1;
		assertEquals(s.getOrigin().getId(),i);
		i=25;
		assertEquals(s.getOrigin().getX(),i);
		assertEquals(s.getOrigin().getY(),i);
	}
	
	@Test
	public void testSetOrigin() {
		s.setOrigin(new Intersection(3,15,15));
		Integer i = 3;
		assertEquals(s.getOrigin().getId(),i);
		i=15;
		assertEquals(s.getOrigin().getX(),i);
		assertEquals(s.getOrigin().getY(),i);
	}
	
	@Test
	public void testGetDestination() {
		Integer i = 2;
		assertEquals(s.getDestination().getId(),i);
		i=0;
		assertEquals(s.getDestination().getX(),i);
		assertEquals(s.getDestination().getY(),i);
	}
	
	@Test
	public void testSetDestination() {
		s.setDestination(new Intersection(4,10,10));
		Integer i = 4;
		assertEquals(s.getDestination().getId(),i);
		i=10;
		assertEquals(s.getDestination().getX(),i);
		assertEquals(s.getDestination().getY(),i);
	}
	
	@Test
	public void testGetLength() {
		Integer i = 50;
		assertEquals(s.getLength(),i);
	}
	
	@Test
	public void testSetLength() {
		Integer i = 100;
		s.setLength(100);
		assertEquals(s.getLength(),i);
	}

	@Test
	public void testGetSpeed() {
		Integer i = 20;
		assertEquals(s.getSpeed(),i);
	}
	
	@Test
	public void testSetSpeed() {
		Integer i = 30;
		s.setSpeed(30);
		assertEquals(s.getSpeed(),i);
	}
	
	@Test
	public void testGetStreet() {
		assertEquals(s.getStreet(),new String("x0"));
	}
	
	@Test
	public void testSetStreet() {
		s.setStreet(new String("y0"));
		assertEquals(s.getStreet(),new String("y0"));
	}

	@Test
	public void testGetDurationSeconds() {
		int durationS = s.getDurationSeconds();
		int durationExpected = (int)(((float)LENGTH)/((float)SPEED)*3.6f);
		assertEquals(durationS,durationExpected);
	}
}
