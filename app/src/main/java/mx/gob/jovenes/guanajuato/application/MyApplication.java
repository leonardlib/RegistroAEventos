package mx.gob.jovenes.guanajuato.application;

import android.support.multidex.MultiDexApplication;

import com.firebase.client.Firebase;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.realm.Realm;
import mx.gob.jovenes.guanajuato.sesion.Sesion;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Uriel on 6/03/17.
 */

/**
 * Clase de la aplicación que se ejecutará en el dispositivo (punto de partida).
 */
public class MyApplication extends MultiDexApplication {
    private Retrofit retrofit;

    private static Realm realm;
    public static String LAST_UPDATE_CONVOCATORIAS = "last_update_convocatorias";
    public static String LAST_UPDATE_REGIONES = "last_update_regiones";
    public static String LAST_UPDATE_EVENTOS = "last_update_eventos";
    public static String LAST_UPDATE_EMPRESAS = "last_update_empresas";
    public static final String LAST_UPDATE_PUBLICIDAD = "last_update_publicidad";


    //dirección publica
    //public static final String BASE_URL = "http://200.23.39.11/GuanajovenWeb/public/api/";

    //uriel publica
    //public static final String BASE_URL = "http://192.168.0.62/GuanajovenWeb/public/api/";

    //public static final String BASE_URL = "http://10.0.7.119/GuanajovenWeb/public/api/";

    public static final String BASE_URL = "http://192.168.0.53/RegistroAEventosWeb/public/api/";


    /**
     * Punto de partida que ejecuta la app al iniciar.
     */
    @Override
    public void onCreate() {
        super.onCreate();

        //Método para iniciar la instancia de la sesión.
        Sesion.sessionStart(this);

        //Instancia de gson utilizada por Retrofit para usarse en otra sección de la app.
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setDateFormat("d/M/yyyy").create();

        //Instancia de retrofit, utilizada en la app.
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();

        //Iniciar Firebase
        Firebase.setAndroidContext(this);
        //Soporte para caracteristicas offline
        Firebase.getDefaultConfig().setPersistenceEnabled(true);

        Realm.init(this);
        realm = Realm.getDefaultInstance();

    }

    public static Realm getRealmInstance() { return realm; }

    public Retrofit getRetrofitInstance(){
        return retrofit;
    }

}
