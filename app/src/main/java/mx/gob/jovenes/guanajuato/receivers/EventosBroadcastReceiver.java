package mx.gob.jovenes.guanajuato.receivers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;

import mx.gob.jovenes.guanajuato.api.EventoAPI;
import mx.gob.jovenes.guanajuato.api.Response;
import mx.gob.jovenes.guanajuato.application.MyApplication;
import mx.gob.jovenes.guanajuato.connection.ConnectionUtilities;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

/**
 * Created by leonardolirabecerra on 08/08/17.
 */

public class EventosBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (ConnectionUtilities.hasConnection(context)) {
            System.out.println("Si hay conexión, registra los eventos guardados");

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            String jsonRegistro = prefs.getString("registro.eventos", "null");

            if (jsonRegistro.equalsIgnoreCase("null")) {

            } else {
                try {
                    JSONObject objetoJson = new JSONObject(jsonRegistro);

                    Iterator<String> iter = objetoJson.keys();
                    while (iter.hasNext()) {
                        String key = iter.next();
                        try {
                            Object objeto = objetoJson.get(key);
                            JSONObject registroJson = new JSONObject(objeto.toString());

                            String token = registroJson.getString("token");
                            Integer idEvento = registroJson.getInt("idEvento");
                            System.out.println("token: " + token);
                            System.out.println("evento: " + idEvento);

                            Retrofit retrofit = ((MyApplication) context.getApplicationContext()).getRetrofitInstance();
                            EventoAPI eventoAPI = retrofit.create(EventoAPI.class);

                            Call<Response<String>> call = eventoAPI.registrar(token, idEvento);
                            call.enqueue(new Callback<Response<String>>() {
                                @Override
                                public void onResponse(Call<Response<String>> call, retrofit2.Response<Response<String>> response) {
                                    if(response.body() != null && response.body().success) {
                                        System.out.println("Registro exitoso");
                                    } else {
                                        System.out.println("Algo ocurrió mal");
                                    }
                                }

                                @Override
                                public void onFailure(Call<Response<String>> call, Throwable t) {
                                    System.out.println("Algo ocurrió mal 2");
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    prefs.edit().remove("registro.eventos").apply();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
