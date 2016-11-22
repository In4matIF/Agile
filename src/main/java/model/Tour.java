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
import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * Created by Olivice on 18/11/2016.
 */
public class Tour implements Observable{

    private Map<Integer, Section> sections;
    private Map<Integer, CrossingPoint> crossingPoints;
    private Integer duration;

    public Tour() {
    }

    public Tour(Map<Integer, Section> sections, Map<Integer, CrossingPoint> crossingPoints, Integer duration) {
        this.sections = sections;
        this.crossingPoints = crossingPoints;
        this.duration = duration;
    }
    
    public Tour(File xmlFile, Plan plan){
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            doc.getDocumentElement().normalize();
            
            Element tElement = (Element) (doc.getElementsByTagName("entrepot").item(0));
            
            Warehouse warehouse = new Warehouse(
                    plan.getIntersections().get(Integer.parseInt(tElement.getAttribute("adresse"))),
            		formatter.parse(tElement.getAttribute("heureDepart")).getTime()
            		);
            
            this.getCrossingPoints().put(warehouse.getIntersection().getId(),warehouse);
            
            NodeList tList = doc.getElementsByTagName("livraison");

            for (int temp = 0; temp < tList.getLength(); temp++) {
                
            	Node tNode = tList.item(temp);

                if (tNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element t2Element = (Element) tNode;

                    DeliveryPoint deliveryPoint = new DeliveryPoint(
                                plan.getIntersections().get(Integer.parseInt(t2Element.getAttribute("adresse"))),
                                Integer.parseInt(t2Element.getAttribute("duree"))
                            );
                    
                    this.getCrossingPoints().put(deliveryPoint.getIntersection().getId(),deliveryPoint);
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<Integer, Section> getSections() {
        return sections;
    }

    public void setSections(Map<Integer, Section> sections) {
        this.sections = sections;
    }

    public Map<Integer, CrossingPoint> getCrossingPoints() {
        return crossingPoints;
    }

    public void setCrossingPoints(Map<Integer, CrossingPoint> crossingPoints) {
        this.crossingPoints = crossingPoints;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "model.Tour{" +
                "sections=" + sections +
                ", crossingPoints=" + crossingPoints +
                ", duration=" + duration +
                '}';
    }

    public void addListener(InvalidationListener listener) {

    }

    public void removeListener(InvalidationListener listener) {

    }
}
