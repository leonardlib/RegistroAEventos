package mx.gob.jovenes.guanajuato.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.api.EventoAPI;
import mx.gob.jovenes.guanajuato.api.Response;
import mx.gob.jovenes.guanajuato.application.MyApplication;
import mx.gob.jovenes.guanajuato.connection.ConnectionUtilities;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;


public class ContinousCaptureActivity extends AppCompatActivity {
    private static final String TAG = ContinousCaptureActivity.class.getSimpleName();
    private DecoratedBarcodeView barcodeView;
    private BeepManager beepManager;
    private String lastText;
    private EventoAPI eventoAPI;
    private Retrofit retrofit;
    private String token;
    private Activity activity;
    private SharedPreferences prefs;

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if(result.getText() == null || result.getText().equals(lastText)) {
                // Prevent duplicate scans
                return;
            }

            lastText = result.getText();
            //barcodeView.setStatusText(result.getText());
            beepManager.playBeepSoundAndVibrate();

            token = lastText;
            Toast toast = Toast.makeText(activity, "Realizando registro de usuario...", Toast.LENGTH_LONG);
            Bundle b = getIntent().getExtras();
            Integer idEvento = b.getInt("idEvento");
            toast.show();

            System.out.println(token);
            System.out.println(idEvento);

            //Checar la conexión a internet
            if (ConnectionUtilities.hasConnection(activity.getBaseContext())) {
                Call<Response<String>> call = eventoAPI.registrar(token, idEvento);
                call.enqueue(new Callback<Response<String>>() {
                    @Override
                    public void onResponse(Call<Response<String>> call, retrofit2.Response<Response<String>> response) {
                        toast.cancel();
                        if(response.body() != null && response.body().success) {
                            Toast.makeText(activity, "Usuario registrado: " + response.body().data, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(activity, "No se pudo registrar este usuario, intenta de nuevo", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Response<String>> call, Throwable t) {
                        toast.cancel();
                        Toast.makeText(activity, "No se pudo registrar este usuario, intenta de nuevo", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                //Guardar el registro al evento en SharedPreferences
                toast.cancel();
                Toast.makeText(activity, "El registro se relizará cuando esté conectado a internet", Toast.LENGTH_LONG).show();
                JSONObject objetoPadre = new JSONObject();
                JSONObject objetoJson = new JSONObject();

                try {
                    objetoJson.put("token", token);
                    objetoJson.put("idEvento", idEvento);
                    objetoPadre.put("" + token, objetoJson.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String listaRegistros = prefs.getString("registro.eventos", "null");

                if (listaRegistros.equalsIgnoreCase("null")) {
                    prefs.edit().putString("registro.eventos", objetoPadre.toString()).apply();
                } else {
                    try {
                        JSONObject objetoGuardado = new JSONObject(listaRegistros);
                        objetoGuardado.put("" + token, objetoJson.toString());
                        prefs.edit().putString("registro.eventos", objetoGuardado.toString()).apply();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            //Added preview of scanned barcode
            //ImageView imageView = (ImageView) findViewById(R.id.barcodePreview);
            //imageView.setImageBitmap(result.getBitmapWithResultPoints(Color.YELLOW));
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.continous_scan);

        barcodeView = (DecoratedBarcodeView) findViewById(R.id.barcode_scanner);
        barcodeView.decodeContinuous(callback);
        activity = this;
        prefs = PreferenceManager.getDefaultSharedPreferences(this.getApplication());
        retrofit = ((MyApplication) this.getApplication()).getRetrofitInstance();
        eventoAPI = retrofit.create(EventoAPI.class);

        beepManager = new BeepManager(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        barcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        barcodeView.pause();
    }

    public void pause(View view) {
        barcodeView.pause();
    }

    public void resume(View view) {
        barcodeView.resume();
    }

    public void triggerScan(View view) {
        barcodeView.decodeSingle(callback);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }
}
