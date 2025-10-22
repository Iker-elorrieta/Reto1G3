package Modelo_Pojos;

import java.io.FileInputStream;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

public class Firebase {

	public static void inicializarFirebase() throws Exception {
	    if (FirebaseApp.getApps().isEmpty()) {
	        FileInputStream serviceAccount = new FileInputStream("serviceAccount.json");
	        FirebaseOptions options = new FirebaseOptions.Builder()
	                .setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();
	        FirebaseApp.initializeApp(options);
	    }
	}
}