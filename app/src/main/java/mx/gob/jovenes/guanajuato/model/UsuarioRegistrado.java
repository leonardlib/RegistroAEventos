package mx.gob.jovenes.guanajuato.model;

/**
 * Created by codigus on 14/08/2017.
 */

public class UsuarioRegistrado {
    private String email;
    private String idGuanajoven;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String rutaImagen;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdGuanajoven() {
        return idGuanajoven;
    }

    public void setIdGuanajoven(String idGuanajoven) {
        this.idGuanajoven = idGuanajoven;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }
}
