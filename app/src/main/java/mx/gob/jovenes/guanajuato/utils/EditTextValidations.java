package mx.gob.jovenes.guanajuato.utils;

import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import java.util.Date;

import fr.ganfra.materialspinner.MaterialSpinner;
import mx.gob.jovenes.guanajuato.sesion.Sesion;

/**
 * Created by code on 4/05/16.
 */
public class EditTextValidations {

    /**
     * Función para determinar si un campo es vacío o no, en caso de ser vacío, muestra el error en el campo.
     * @param et: EditText que se va a comprobar.
     * @return boolean
     */
    public static boolean esCampoVacio(EditText et){
        if(et.getText().toString().length() == 0){
            ((TextInputLayout)et.getParent().getParent()).setErrorEnabled(true);
            ((TextInputLayout)et.getParent().getParent()).setError("Este campo es obligatorio");
            return true;
        }
        else{
            return false;
        }
    }

    public static boolean esCorreoUsuario(EditText editText) {
        if (editText.getText().toString().equals(Sesion.getUsuario().getEmail())) {
            ((TextInputLayout)editText.getParent().getParent()).setErrorEnabled(true);
            ((TextInputLayout)editText.getParent().getParent()).setError("El correo del usuario ya está incluido, no hace falta agregarlo");
            return false;
        } else {
            return true;
        }
    }

    //la documentación para pendejos
    public static boolean datosCURPInvalido(EditText editText) {
        if (editText.getText().toString().length() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean spinnerDatosCurpInvalido(MaterialSpinner spinner){
        if(spinner.getSelectedItemPosition() == 0){
            return true;
        }
        else{
            return false;
        }
    }

    //pinches comentarios de relleno para verme bien cool
    public static boolean curpValido(EditText editTextCurp, EditText editTextNombre, EditText editTextApellidoPaterno,
                                     EditText editTextApellidoMaterno, EditText editTextFechaNacimiento,
                                     MaterialSpinner spinnerGenero, MaterialSpinner spinnerEstadoNacimiento) {
        if (editTextCurp.getText().length() == 18) {
            if (editTextNombre.getText().toString().length() != 0 &&
                    editTextApellidoPaterno.getText().toString().length() != 0 &&
                    editTextApellidoMaterno.getText().toString().length() != 0 &&
                    editTextFechaNacimiento.getText().toString().length() != 0 &&
                    spinnerGenero.getSelectedItemPosition() != 0 &&
                    spinnerEstadoNacimiento.getSelectedItemPosition() != 0) {
                return true;
            } else {
                return false;
            }
        } else {
            ((TextInputLayout)editTextCurp.getParent().getParent()).setErrorEnabled(true);
            ((TextInputLayout)editTextCurp.getParent().getParent()).setError("CURP debe tener 18 caracteres");
            return false;
        }
    }

    /**
     * Método que determina si un email es válido o no, indica el error en el EditText en caso de no serlo.
     * @param et:EditText que se va a comprobar.
     * @return boolean
     */
    public static boolean esEmailValido(EditText et){
        String email = et.getText().toString();
        if(ValidEmail.isValidEmail(email)){
            return true;
        }
        else{
            ((TextInputLayout)et.getParent().getParent()).setErrorEnabled(true);
            ((TextInputLayout)et.getParent().getParent()).setError("El email no es válido");
            return false;
        }
    }


    /**
     * Identifica si el campo de código postal es válido (5 caracteres numéricos).
     * @param et
     * @return
     */
    public static boolean esCodigoPostalValido(EditText et){
        String cp = et.getText().toString();
        if(cp.length() == 5){
            return true;
        }
        else{
            ((TextInputLayout)et.getParent().getParent()).setErrorEnabled(true);
            ((TextInputLayout)et.getParent().getParent()).setError("El código postal es inválido");
            return false;
        }
    }

    /**
     * Método que determina si una contraseña es válida o no, indica el error en el EditText en caso de no serlo.
     * @param et:EditText que se va a comprobar.
     * @return boolean
     */
    public static boolean esContrasenaValida(EditText et){
        if(et.getText().toString().length() > 6){
            return true;
        }
        else{
            ((TextInputLayout)et.getParent().getParent()).setErrorEnabled(true);
            ((TextInputLayout)et.getParent().getParent()).setError("La contraseña debe contener más de 6 caracteres");
            return false;
        }
    }

    /**
     * Función para comprobar si las contraseñas en dos EditTexts coinciden, indica el error en el primero.
     * @param et1: Primer EditText a comprobar.
     * @param et2: Segundo EditText a comprobar.
     * @return boolean
     */
    public static boolean contrasenasCoinciden(EditText et1, EditText et2){
        if(et1.getText().toString().equals(et2.getText().toString())){
            return true;
        }
        else{
            ((TextInputLayout)et1.getParent().getParent()).setErrorEnabled(true);
            ((TextInputLayout)et1.getParent().getParent()).setError("Las contraseñas no coinciden");
            return false;
        }
    }

    /**
     * Función para mostrar un error en el spinner cuando la selección es nula.
     * @param spinner: Objeto tipo MaterialSpinner.
     * @return boolean.
     */
    public static boolean spinnerSinSeleccion(MaterialSpinner spinner){
        if(spinner.getSelectedItemPosition() == 0){
            spinner.setError("Este campo es obligatorio");
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Función que asigna el Listener de cambio de texto a un EditText para que cuando escriba algo
     * se quiten los errores.
     * @param et: Edit Text sobre el que se quiera aplicar la función.
     */
    public static void removeErrorTyping(final EditText et){
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
               ((TextInputLayout) et.getParent().getParent()).setErrorEnabled(false);
               ((TextInputLayout) et.getParent().getParent()).setError(null);
            }
        });
    }


    /**
     * Función para mostrar un mensaje dentro del campo de texto (placeholder) cuando se haya recibido el foco.
     * @param et: EditText al que se le aplicará la función.
     * @param texto: Texto a mostrar.
     */
    public static void showHint(final EditText et, final String texto){
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    et.setHint(texto);
                }
                else{
                    et.setHint("");
                }
            }
        });
    }

    public static boolean compararFechaHora(EditText f1, EditText h1, String fec1, String fec2){
        Log.d("FECHAS", fec1 + "     " + fec2);
        Date d1 = DateUtilities.stringToDate(fec1);
        Date d2 = DateUtilities.stringToDate(fec2);

        if(d2.before(d1)){
            ((TextInputLayout)f1.getParent().getParent()).setErrorEnabled(true);
            ((TextInputLayout)f1.getParent().getParent()).setError("La fecha de fin no puede ser posterior a la de inicio.");
            ((TextInputLayout)h1.getParent().getParent()).setErrorEnabled(true);
            ((TextInputLayout)h1.getParent().getParent()).setError("");
            return false;
        }
        else{
            return true;
        }
    }


    public static void dependencySpinners(MaterialSpinner spn1, View[] views) {
        for(int i = views.length -1 ; i >= 0 ; i -= 1) {
            views[i].setVisibility(View.GONE);
        }
        spn1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                if(position == 0) {

                    for(int i = views.length -1 ; i >= 0 ; i -= 1) {
                        views[i].setVisibility(View.VISIBLE);
                    }
                }
                else {
                    for(int i = views.length -1 ; i >= 0 ; i -= 1) {

                        if (views[i] instanceof EditText) {
                            ((EditText) views[i]).getText().clear();
                        }

                        if(views[i].getClass().equals(MaterialSpinner.class)){
                            ((MaterialSpinner)views[i]).setSelection(0);
                        }

                        views[i].setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
