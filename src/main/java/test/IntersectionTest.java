package test;

import model.Intersection;
import model.Section;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Classe de test de l'objet Intersection
 */
public class IntersectionTest {

    private Intersection i;
    private final Integer ID = 1;
    private final Integer X = 2;
    private final Integer Y = 3;

    /**
     * Création d'un objet Intersection
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        i = new Intersection(ID,X,Y);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetID() {
        assertEquals(i.getId(),ID);
    }

    @Test
    public void testSetID() {
        Integer newID = 10;
        i.setId(newID);
        assertEquals(i.getId(),newID);
    }

    @Test
    public void testGetX() {
        assertEquals(i.getX(),X);
    }

    @Test
    public void testSetX() {
        Integer newX = 20;
        i.setX(newX);
        assertEquals(i.getX(),newX);
    }

    @Test
    public void testGetY() {
        assertEquals(i.getY(),Y);
    }

    @Test
    public void testSetY() {
        Integer newY = 30;
        i.setY(newY);
        assertEquals(i.getY(),newY);
    }
}
