package Modelo_Pojos;

import java.io.Serializable;
import java.util.Date;

public class HistoricoDetalle implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nombreWorkout;
    private int nivel;
    private long tiempoTotal; // en milisegundos
    private long tiempoPrevisto; // en milisegundos
    private Date fecha;
    private int porcentaje;

    public HistoricoDetalle() {
        this.nombreWorkout = "";
        this.nivel = 0;
        this.tiempoTotal = 0L;
        this.tiempoPrevisto = 0L;
        this.fecha = null;
        this.porcentaje = 0;
    }

    public HistoricoDetalle(String nombreWorkout, int nivel, long tiempoTotal, long tiempoPrevisto, Date fecha, int porcentaje) {
        this.nombreWorkout = nombreWorkout;
        this.nivel = nivel;
        this.tiempoTotal = tiempoTotal;
        this.tiempoPrevisto = tiempoPrevisto;
        this.fecha = fecha;
        this.porcentaje = porcentaje;
    }

    public String getNombreWorkout() {
        return nombreWorkout;
    }

    public void setNombreWorkout(String nombreWorkout) {
        this.nombreWorkout = nombreWorkout;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public long getTiempoTotal() {
        return tiempoTotal;
    }

    public void setTiempoTotal(long tiempoTotal) {
        this.tiempoTotal = tiempoTotal;
    }

    public long getTiempoPrevisto() {
        return tiempoPrevisto;
    }

    public void setTiempoPrevisto(long tiempoPrevisto) {
        this.tiempoPrevisto = tiempoPrevisto;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(int porcentaje) {
        this.porcentaje = porcentaje;
    }

    @Override
    public String toString() {
        return "HistoricoDetalle [nombreWorkout=" + nombreWorkout + ", nivel=" + nivel + ", tiempoTotal=" + tiempoTotal
                + ", tiempoPrevisto=" + tiempoPrevisto + ", fecha=" + fecha + ", porcentaje=" + porcentaje + "]";
    }
}
