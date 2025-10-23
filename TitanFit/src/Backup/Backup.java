package Backup;

import java.io.IOException;

public class Backup {

	
	public void crearProceso() {
		try {
			ProcessBuilder pb = new ProcessBuilder(
				    "java",
				    "-cp",
				    "target/classes",       
				    "Backup.BackupProceso" 
				);
				pb.inheritIO(); 
				pb.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	

}