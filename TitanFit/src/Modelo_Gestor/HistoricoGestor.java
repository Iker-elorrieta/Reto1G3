package Modelo_Gestor;

import Modelo_Pojos.HistoricoDetalle;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;

import Controlador.FirebaseControlador;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HistoricoGestor {

    public static List<HistoricoDetalle> obtenerHistoricoUsuario(String userEmail) {
        List<HistoricoDetalle> lista = new ArrayList<>();
        try {
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

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}