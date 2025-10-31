package Backup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;

import Controlador.FirebaseControlador;
import Modelo_Pojos.Ejercicio;
import Modelo_Pojos.Historico;
import Modelo_Pojos.Serie;
import Modelo_Pojos.Usuario;
import Modelo_Pojos.Workout;

public class BackupProceso {

	static List<Usuario> listaUsuarios = new ArrayList<>();
	static List<Workout> listaWorkouts = new ArrayList<>();
	static List<Historico> listaHistorico = new ArrayList<>();

	public static void recogerUsuarios() {
		Firestore db = FirestoreClient.getFirestore();
		try {
			ApiFuture<QuerySnapshot> future = db.collection("Usuarios").get();
			List<QueryDocumentSnapshot> documents = future.get().getDocuments();

			for (QueryDocumentSnapshot document : documents) {
				Usuario usuario = new Usuario();
				usuario.setNombre(document.getString("nombre"));
				usuario.setEmail(document.getId());
				usuario.setApellidos(document.getString("apellidos"));
				usuario.setPassword(document.getString("contraseña"));
				if (document.contains("nivel") && document.getLong("nivel") != null) {
					usuario.setNivel(document.getLong("nivel").intValue());
				} else {
					usuario.setNivel(0);
				}
				Object fechaObj = document.get("fechaNacimiento");
				if (fechaObj != null) {
					if (fechaObj instanceof Timestamp) {
						usuario.setFechaNacimiento(((Timestamp) fechaObj).toDate());
					} else if (fechaObj instanceof Date) {
						usuario.setFechaNacimiento((Date) fechaObj);
					} else if (fechaObj instanceof String) {
						try {
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
							usuario.setFechaNacimiento(sdf.parse((String) fechaObj));
						} catch (Exception ex) {
							usuario.setFechaNacimiento(null);
						}
					} else {
						usuario.setFechaNacimiento(null);
					}
				}

				// --- Recoger historicos para este usuario ---
				try {
					ApiFuture<QuerySnapshot> historicosFuture = document.getReference().collection("Historico").get();
					List<QueryDocumentSnapshot> historicosDocs = historicosFuture.get().getDocuments();
					ArrayList<Historico> historicosList = new ArrayList<>();

					for (QueryDocumentSnapshot hDoc : historicosDocs) {
						Historico h = new Historico();
						h.setPorcentaje(hDoc.getLong("porcentaje").intValue());
						h.setTiempo(hDoc.getLong("tiempo").intValue());
						h.setFecha(hDoc.getDate("fecha"));

						Object refObj = hDoc.get("id_workout");
						int id_workout = 0;
						if (refObj instanceof DocumentReference) {
							DocumentReference ref = (DocumentReference) refObj;
							try {
								id_workout = Integer.parseInt(ref.getId());
							} catch (NumberFormatException e) {
								id_workout = 0;
							}
						} else if (refObj != null) {
							try {
								id_workout = Integer.parseInt(refObj.toString());
							} catch (NumberFormatException e) {
								id_workout = 0;
							}
						}
						h.setId_workout(id_workout);

						historicosList.add(h);
					}

					usuario.setHistorico(historicosList);
				} catch (Exception e) {
					e.printStackTrace();
				}

				listaUsuarios.add(usuario);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void recogerWorkouts() {
		Firestore db = FirestoreClient.getFirestore();
		try {
			ApiFuture<QuerySnapshot> future = db.collection("Workouts").get();
			List<QueryDocumentSnapshot> documents = future.get().getDocuments();

			for (QueryDocumentSnapshot document : documents) {
				Workout workout = new Workout();

				workout.setNombre(document.getString("nombre"));
				if (document.contains("numeroEjercicios") && document.getLong("numeroEjercicios") != null) {
					workout.setNumeroEjercicios(document.getLong("numeroEjercicios").intValue());
				}
				if (document.contains("nivel") && document.getLong("nivel") != null) {
					workout.setNivel(document.getLong("nivel").intValue());
				}
				workout.setVideo(document.getString("video"));

				ApiFuture<QuerySnapshot> ejerciciosFuture = document.getReference().collection("Ejercicios").get();
				List<QueryDocumentSnapshot> ejerciciosDocs = ejerciciosFuture.get().getDocuments();

				for (QueryDocumentSnapshot ejDoc : ejerciciosDocs) {
					Ejercicio ejercicio = new Ejercicio();
					ejercicio.setNombre(ejDoc.getId());
					ejercicio.setDescripcion(ejDoc.getString("descripcion"));
					ejercicio.setFoto(ejDoc.getString("foto"));

					// Get series for this ejercicio (use ejDoc reference)
					ApiFuture<QuerySnapshot> seriesFuture = ejDoc.getReference().collection("Series").get();
					List<QueryDocumentSnapshot> seriesDocs = seriesFuture.get().getDocuments();

					for (QueryDocumentSnapshot serieDoc : seriesDocs) {
						Serie serie = new Serie();

						try {
							serie.setSerieId(Integer.parseInt(serieDoc.getId()));
						} catch (Exception ex) {
							serie.setSerieId(0);
						}

						Long descansoLong = serieDoc.getLong("descanso");
						Long tiempoLong = serieDoc.getLong("tiempo");
						int descanso = (descansoLong == null) ? 0 : descansoLong.intValue() / 1000;
						int tiempo = (tiempoLong == null) ? 0 : tiempoLong.intValue() / 1000;

						serie.setDescanso(descanso);
						serie.setTiempo(tiempo);

						ejercicio.getSeries().add(serie);

					}

					workout.getEjercicios().add(ejercicio);
				}

				listaWorkouts.add(workout);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void historicoXML() {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.newDocument();

			Element rootElement = doc.createElement("Historicos");
			doc.appendChild(rootElement);

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			for (Usuario usuario : listaUsuarios) {
				if (usuario.getHistorico() != null && !usuario.getHistorico().isEmpty()) {
					for (Historico h : usuario.getHistorico()) {
						Element historicoElem = doc.createElement("Historico");

						Element userId = doc.createElement("UsuarioEmail");
						userId.appendChild(doc.createTextNode(String.valueOf(usuario.getEmail())));
						historicoElem.appendChild(userId);

						Element workoutIdElem = doc.createElement("WorkoutId");
						workoutIdElem.appendChild(doc.createTextNode(String.valueOf(h.getId_workout())));
						historicoElem.appendChild(workoutIdElem);

						Element porcentajeElem = doc.createElement("Porcentaje");
						porcentajeElem.appendChild(doc.createTextNode(String.valueOf(h.getPorcentaje())));
						historicoElem.appendChild(porcentajeElem);

						Element tiempoElem = doc.createElement("Tiempo");
						tiempoElem.appendChild(doc.createTextNode(String.valueOf(h.getTiempo())));
						historicoElem.appendChild(tiempoElem);

						Element fechaElem = doc.createElement("Fecha");
						if (h.getFecha() != null) {
							fechaElem.appendChild(doc.createTextNode(sdf.format(h.getFecha())));
						} else {
							fechaElem.appendChild(doc.createTextNode(""));
						}
						historicoElem.appendChild(fechaElem);

						rootElement.appendChild(historicoElem);
					}
				}
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("historicos.xml"));
			transformer.transform(source, result);

			System.out.println("XML generado correctamente.");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			FirebaseControlador.inicializarFirebase();
			Thread hilousuario = new Thread(() -> recogerUsuarios());
			Thread hiloworkout = new Thread(() -> recogerWorkouts());

			hilousuario.start();
			hiloworkout.start();

			hilousuario.join();
			hiloworkout.join();

			Thread hilohistorico = new Thread(() -> historicoXML());
			hilohistorico.start();
			hilohistorico.join();

			File file = new File(System.getProperty("user.dir"), "backup.dat");

			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			oos.writeObject(listaUsuarios);
			oos.writeObject(listaWorkouts);

			oos.close();
			fos.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
