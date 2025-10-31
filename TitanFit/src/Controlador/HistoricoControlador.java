package Controlador;

import java.util.List;

import Modelo_Pojos.HistoricoDetalle;

public class HistoricoControlador {

    public static List<HistoricoDetalle> obtenerHistoricoUsuario(String userEmail) {
        return Modelo_Gestor.HistoricoGestor.obtenerHistoricoUsuario(userEmail);
    }

}
