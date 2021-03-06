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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Objet representant les informations de la livraison
 */
public class Tour implements Observable{

	private List<Section> sections;
    private Map<Integer, CrossingPoint> crossingPoints;
    private Integer duration; //en secondes
    private Integer idWarehouse; //id de l'entrepot
    private List<Intersection> intersections; //
    private List<CrossingPoint> ordainedCrossingPoints; //Liste ordonnee de crossing points pour
    													//l'ajout et la suppression de points

    public Tour() {
    }

    public Tour(List<Section> sections, Map<Integer, CrossingPoint> crossingPoints, Integer duration) {
        this.sections = sections;
        this.crossingPoints = crossingPoints;
        this.duration = duration;
    }
    
    /**
     * Constructeur de l'objet a partir du fichier xml de livraison ainsi que du plan
     * @param xmlFile le fichier de livraison
     * @param plan l'objet plan cree en amont
     */
    public Tour(File xmlFile, Plan plan) throws Exception{
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlFile);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        doc.getDocumentElement().normalize();

        NodeList nTest = doc.getElementsByTagName("demandeDeLivraisons");
        if(nTest.getLength()==0){
            throw new Exception("Fichier incompatible.");
        }

        Element tElement = (Element) (doc.getElementsByTagName("entrepot").item(0));

        long departure = 0;

        String[] strTime = (tElement.getAttribute("heureDepart")).split(":");
        departure = Integer.parseInt(strTime[0]) * 3600
                + Integer.parseInt(strTime[1]) * 60
                + Integer.parseInt(strTime[2]);

        //Creation de l'entrepot
        Warehouse warehouse = new Warehouse(
                plan.getIntersections().get(Integer.parseInt(tElement.getAttribute("adresse"))),
                departure
                );
        idWarehouse = warehouse.getIntersection().getId();

        crossingPoints = new HashMap<>();
        sections = new LinkedList<Section>();
        crossingPoints.put(warehouse.getIntersection().getId(),warehouse);

        NodeList tList = doc.getElementsByTagName("livraison");

        //Creation des points de livraison
        for (int temp = 0; temp < tList.getLength(); temp++) {

            Node tNode = tList.item(temp);

            if (tNode.getNodeType() == Node.ELEMENT_NODE) {

                Element t2Element = (Element) tNode;

                long beginTime = 0;
                long endTime = Long.MAX_VALUE;

                if(t2Element.hasAttribute("debutPlage") && t2Element.hasAttribute("finPlage")) {
                    strTime = (t2Element.getAttribute("debutPlage")).split(":");
                    beginTime = Integer.parseInt(strTime[0]) * 3600
                            + Integer.parseInt(strTime[1]) * 60
                            + Integer.parseInt(strTime[2]);

                    strTime = (t2Element.getAttribute("finPlage")).split(":");
                    endTime = Integer.parseInt(strTime[0]) * 3600
                            + Integer.parseInt(strTime[1]) * 60
                            + Integer.parseInt(strTime[2]);
                }

                DeliveryPoint deliveryPoint = new DeliveryPoint(
                            plan.getIntersections().get(Integer.parseInt(t2Element.getAttribute("adresse"))),
                            beginTime,
                            endTime,
                            Integer.parseInt(t2Element.getAttribute("duree"))
                        );

                crossingPoints.put(deliveryPoint.getIntersection().getId(),deliveryPoint);
            }
        }
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
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

    public Integer getIdWarehouse() {
		return idWarehouse;
	}

	public void setIdWarehouse(Integer idWarehouse) {
		this.idWarehouse = idWarehouse;
	}

	public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public List<Intersection> getIntersections() {
        return intersections;
    }

    public void setIntersections(List<Intersection> intersections) {
        this.intersections = intersections;
    }
    
    public List<CrossingPoint> getOrdainedCrossingPoints() {
		return ordainedCrossingPoints;
	}

	public void setOrdainedCrossingPoints(List<CrossingPoint> ordainedCrossingPoints) {
		this.ordainedCrossingPoints = ordainedCrossingPoints;
	}

    public void deleteDelivery(int deliveryId){
        crossingPoints.remove(deliveryId);
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
