package Backup;

import java.io.IOException;

public class Backup {

	
	public void crearProceso() {
		try {
			ProcessBuilder pb = new ProcessBuilder(
				    "java",
				    "-jar",
				    "Recursos/TitanfitBackup.jar" 
				);
				pb.inheritIO(); 
				pb.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	

}