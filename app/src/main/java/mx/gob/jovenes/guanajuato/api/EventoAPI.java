package mx.gob.jovenes.guanajuato.api;

import java.util.ArrayList;

import mx.gob.jovenes.guanajuato.model.Evento;
import mx.gob.jovenes.guanajuato.model.Usuario;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by codigus on 11/5/2017.
 */

public interface EventoAPI {

    @GET("eventos")
    Call<Response<ArrayList<Evento>>> obtenerEventos(
            @Query("timestamp") String timestamp
    );

    @POST("eventos/registrar")
    Call<Response<String>> registrar(
            @Query("token") String token,
            @Query("id_evento") Integer idEvento
    );
}
