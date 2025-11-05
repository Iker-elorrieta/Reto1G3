package Controlador;

import Modelo_Pojos.Firebase;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import java.util.concurrent.TimeUnit;

/**
 * Monitor que verifica constantemente la conexión a Firebase
 * y actualiza el estado de conectividad en tiempo real
 */
public class ConexionMonitor {
    
    private static Thread monitorThread;
    private static volatile boolean running = false;
    private static final int CHECK_INTERVAL_SECONDS = 10; // Verificar cada 10 segundos
    
    /**
     * Inicia el monitoreo de conexión en segundo plano
     */
    public static void iniciarMonitoreo() {
        if (running) {
            System.out.println("El monitor de conexión ya está ejecutándose");
            return;
        }
        
        running = true;
        monitorThread = new Thread(() -> {
            System.out.println("Monitor de conexión iniciado");
            
            while (running) {
                try {
                    verificarConexion();
                    Thread.sleep(CHECK_INTERVAL_SECONDS * 1000);
                } catch (InterruptedException e) {
                    System.out.println("Monitor de conexión interrumpido");
                    break;
                } catch (Exception e) {
                    // Continuar monitoreando aunque haya errores
                }
            }
            
            System.out.println("Monitor de conexión detenido");
        });
        
        monitorThread.setDaemon(true); // Thread daemon para que no impida cerrar la aplicación
        monitorThread.start();
    }
    
    /**
     * Detiene el monitoreo de conexión
     */
    public static void detenerMonitoreo() {
        running = false;
        if (monitorThread != null) {
            monitorThread.interrupt();
        }
    }
    
    /**
     * Verifica si hay conexión a Firebase haciendo una consulta simple
     */
    private static void verificarConexion() {
        boolean estadoAnterior = Firebase.isConectado();
        
        try {
            // Intentar hacer una consulta simple con timeout corto
            Firestore db = FirestoreClient.getFirestore();
            db.collection("Usuarios").limit(1).get().get(3, TimeUnit.SECONDS);
            
            // Si llegamos aquí, la conexión es exitosa
            if (!estadoAnterior) {
                Firebase.marcarConectado();
                System.out.println("✓ Conexión a Firebase restaurada");
            }
            
        } catch (Exception e) {
            // Error de conexión
            if (estadoAnterior) {
                Firebase.marcarDesconectado();
                System.out.println("✗ Conexión a Firebase perdida - Usando modo offline");
            }
        }
    }
    
    /**
     * Verifica la conexión una sola vez (sin iniciar el monitoreo)
     */
    public static void verificarConexionAhora() {
        verificarConexion();
    }
}
