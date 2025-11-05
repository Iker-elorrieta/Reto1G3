package Modelo_Gestor;

import Modelo_Pojos.HistoricoDetalle;
import Modelo_Pojos.Firebase;
import Modelo_Pojos.Workout;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;

import Controlador.FirebaseControlador;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class HistoricoGestor {

    public static List<HistoricoDetalle> obtenerHistoricoUsuario(String userEmail) {
        try {
            if (Firebase.isConectado()) {
                try {
                    return obtenerHistoricoDesdeFirebase(userEmail);
                } catch (ExecutionException ee) {
                    // Marcar Firebase como desconectado
                    Firebase.marcarDesconectado();
                    System.out.println("Error de red detectado. Cargando histórico desde historicos.xml...");
                    return obtenerHistoricoDesdeXML(userEmail);
                }
            } else {
                return obtenerHistoricoDesdeXML(userEmail);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Marcar Firebase como desconectado y usar XML
            Firebase.marcarDesconectado();
            System.out.println("Error al obtener histórico. Intentando con historicos.xml...");
            return obtenerHistoricoDesdeXML(userEmail);
        }
    }
    
    private static List<HistoricoDetalle> obtenerHistoricoDesdeFirebase(String userEmail) throws Exception {
        List<HistoricoDetalle> lista = new ArrayList<>();
        
        // Ensure Firebase initialized
        try { FirebaseControlador.inicializarFirebase(); } catch (Throwable t) {}
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference userRef = db.collection("Usuarios").document(userEmail);

        ApiFuture<QuerySnapshot> future = userRef.collection("Historico").get();
        QuerySnapshot historicoSnap = future.get();

        for (QueryDocumentSnapshot hDoc : historicoSnap.getDocuments()) {
            // The field 'id_workout' was stored as a DocumentReference. Retrieve it using get(..., DocumentReference.class).
            DocumentReference workoutRef = hDoc.get("id_workout", DocumentReference.class);
            int porcentaje = 0;
            if (hDoc.contains("porcentaje")) {
                try { porcentaje = hDoc.getLong("porcentaje").intValue(); } catch (Exception e) { porcentaje = 0; }
            }

            Long tiempoLong = null;
            try { tiempoLong = hDoc.getLong("tiempo"); } catch (Exception ignore) {}
            long tiempoTotal = (tiempoLong == null) ? 0L : tiempoLong.longValue();

            Date fecha = null;
            try { fecha = hDoc.getDate("fecha"); } catch (Exception ignore) {}

            String nombreWorkout = "";
            int nivel = 0;
            long tiempoPrevisto = 0L;

            if (workoutRef != null) {
                DocumentSnapshot wSnap = workoutRef.get().get();
                if (wSnap.exists()) {
                    nombreWorkout = wSnap.getString("nombre");
                    try { nivel = Integer.parseInt(wSnap.getId()); } catch (Exception ignore) { nivel = 0; }

                    // Calcular tiempo previsto sumando los campos 'tiempo' de las Series (se asume están en milisegundos)
                    CollectionReference ejerciciosCol = wSnap.getReference().collection("Ejercicios");
                    ApiFuture<QuerySnapshot> ejerciciosFuture = ejerciciosCol.get();
                    QuerySnapshot ejerciciosSnap = ejerciciosFuture.get();
                    for (QueryDocumentSnapshot ejDoc : ejerciciosSnap.getDocuments()) {
                        ApiFuture<QuerySnapshot> seriesFuture = ejDoc.getReference().collection("Series").get();
                        QuerySnapshot seriesSnap = seriesFuture.get();
                        for (QueryDocumentSnapshot sDoc : seriesSnap.getDocuments()) {
                            try {
                                Long t = sDoc.getLong("tiempo");
                                if (t != null) tiempoPrevisto += t.longValue();
                            } catch (Exception ignore) {}
                        }
                    }
                }
            }

            HistoricoDetalle hd = new HistoricoDetalle(nombreWorkout, nivel, tiempoTotal, tiempoPrevisto, fecha, porcentaje);
            lista.add(hd);
        }
        
        System.out.println("Histórico cargado desde Firebase: " + lista.size() + " registros");
        return lista;
    }
    
    private static List<HistoricoDetalle> obtenerHistoricoDesdeXML(String userEmail) {
        List<HistoricoDetalle> lista = new ArrayList<>();
        
        try {
            File xmlFile = new File("historicos.xml");
            
            if (!xmlFile.exists()) {
                System.out.println("Archivo historicos.xml no encontrado");
                return lista;
            }
            
            // Parsear el XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            
            // Crear XPath
            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xpath = xPathFactory.newXPath();
            
            // Expresión XPath para buscar todos los Historico del usuario específico
            String expression = String.format("//Historico[UsuarioEmail='%s']", userEmail);
            NodeList nodeList = (NodeList) xpath.evaluate(expression, doc, XPathConstants.NODESET);
            
            System.out.println("Históricos encontrados para " + userEmail + ": " + nodeList.getLength());
            
            // Obtener workouts del backup para conseguir nombres y tiempos previstos
            List<Workout> workouts = WorkoutGestor.obtenerWorkouts();
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    
                    // Extraer datos del XML usando XPath
                    int workoutId = 0;
                    try {
                        String workoutIdStr = element.getElementsByTagName("WorkoutId").item(0).getTextContent();
                        workoutId = Integer.parseInt(workoutIdStr);
                    } catch (Exception e) {
                        workoutId = 0;
                    }
                    
                    int porcentaje = 0;
                    try {
                        String porcentajeStr = element.getElementsByTagName("Porcentaje").item(0).getTextContent();
                        porcentaje = Integer.parseInt(porcentajeStr);
                    } catch (Exception e) {
                        porcentaje = 0;
                    }
                    
                    long tiempoTotal = 0L;
                    try {
                        String tiempoStr = element.getElementsByTagName("Tiempo").item(0).getTextContent();
                        tiempoTotal = Long.parseLong(tiempoStr);
                    } catch (Exception e) {
                        tiempoTotal = 0L;
                    }
                    
                    Date fecha = null;
                    try {
                        String fechaStr = element.getElementsByTagName("Fecha").item(0).getTextContent();
                        fecha = sdf.parse(fechaStr);
                    } catch (Exception e) {
                        fecha = null;
                    }
                    
                    // Buscar el workout correspondiente en el backup para obtener nombre y tiempo previsto
                    String nombreWorkout = "Workout " + workoutId;
                    int nivel = workoutId;
                    long tiempoPrevisto = 0L;
                    
                    for (Workout w : workouts) {
                        if (w.getNivel() == workoutId || (w.getId() != null && w.getId().equals(String.valueOf(workoutId)))) {
                            nombreWorkout = w.getNombre();
                            nivel = w.getNivel();
                            
                            // Calcular tiempo previsto del workout
                            if (w.getEjercicios() != null) {
                                for (int j = 0; j < w.getEjercicios().size(); j++) {
                                    if (w.getEjercicios().get(j).getSeries() != null) {
                                        for (int k = 0; k < w.getEjercicios().get(j).getSeries().size(); k++) {
                                            tiempoPrevisto += w.getEjercicios().get(j).getSeries().get(k).getTiempo() * 1000; // convertir a milisegundos
                                        }
                                    }
                                }
                            }
                            break;
                        }
                    }
                    
                    HistoricoDetalle hd = new HistoricoDetalle(nombreWorkout, nivel, tiempoTotal, tiempoPrevisto, fecha, porcentaje);
                    lista.add(hd);
                    
                    System.out.println("  - " + nombreWorkout + " (Nivel " + nivel + ") - " + porcentaje + "% - " + fecha);
                }
            }
            
            System.out.println("Histórico cargado desde historicos.xml: " + lista.size() + " registros");
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al leer histórico desde historicos.xml");
        }
        
        return lista;
    }
}