package model;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Olivice on 18/11/2016.
 */
public class Plan implements Observable{

    private Map<Integer, Intersection> intersections = new HashMap<Integer, Intersection>();
    private List<Section> sections = new ArrayList<>();

    public Plan() {
    }

    public Plan(Map<Integer, Intersection> intersections, ArrayList sections) {
        this.intersections = intersections;
        this.sections = sections;
    }

    public Plan(File xmlFile){
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("noeud");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    Intersection intersection = new Intersection(
                            Integer.parseInt(eElement.getAttribute("id")),
                            Integer.parseInt(eElement.getAttribute("x")),
                            Integer.parseInt(eElement.getAttribute("y"))
                            );
                    this.getIntersections().put(intersection.getId(),intersection);
                }
            }

            NodeList sList = doc.getElementsByTagName("troncon");

            for (int temp = 0; temp < sList.getLength(); temp++) {
                Node sNode = sList.item(temp);

                if (sNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) sNode;

                    Intersection origine = this.getIntersections().get(Integer.parseInt(eElement.getAttribute("origine")));
                    Intersection destination = this.getIntersections().get(Integer.parseInt(eElement.getAttribute("destination")));

                    Section section = new Section(
                        origine,
                        destination,
                        Integer.parseInt(eElement.getAttribute("longueur")),
                        Integer.parseInt(eElement.getAttribute("vitesse")),
                        eElement.getAttribute("nomRue")
                    );

                    origine.getSections().add(section);

                    this.getSections().add(section);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<Integer, Intersection> getIntersections() {
        return intersections;
    }

    public void setIntersections(Map<Integer, Intersection> intersections) {
        this.intersections = intersections;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(ArrayList sections) {
        this.sections = sections;
    }

    @Override
    public String toString() {
        return "Plan{" +
                "intersections=" + intersections +
                ", sections=" + sections +
                '}';
    }

    public void addListener(InvalidationListener listener) {

    }

    public void removeListener(InvalidationListener listener) {

    }
}
