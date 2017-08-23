package mx.gob.jovenes.guanajuato.activities;



import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.adapters.VPEstadisticasAdapter;
import mx.gob.jovenes.guanajuato.api.EventoAPI;
import mx.gob.jovenes.guanajuato.api.Response;
import mx.gob.jovenes.guanajuato.application.MyApplication;
import mx.gob.jovenes.guanajuato.sesion.Sesion;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

/**
 * Created by codigus on 11/08/2017.
 */

public class EstadisticasActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private TabLayout tabs;
    private ViewPager viewPager;
    private VPEstadisticasAdapter adapter;
    private int idEvento;
    private Retrofit retrofit;
    private EventoAPI eventoAPI;

    private static final String ENVIANDO_CORREO = "Enviando correo...";
    private static final String POR_FAVOR_ESPERE = "Por favor espere...";
    private static final String CORREO_ENVIADO = "Correo enviado";
    private static final String ERROR_AL_ENVIAR = "Error al enviar";
    private static final String ERROR_NO_HAY_REGISTROS = "Evento sin registros";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        retrofit = ((MyApplication) getApplication()).getRetrofitInstance();
        eventoAPI = retrofit.create(EventoAPI.class);

        setContentView(R.layout.activity_estadisticas);

        idEvento = getIntent().getIntExtra("idEvento", 0);

        tabs = (TabLayout) findViewById(R.id.tabs_estadísticas);
        viewPager = (ViewPager) findViewById(R.id.vp_estadisticas);
        adapter = new VPEstadisticasAdapter(getSupportFragmentManager());

        viewPager.setAdapter(adapter);
        tabs.setupWithViewPager(viewPager);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tabs.getTabAt(0).setText("Registrados");
        tabs.getTabAt(1).setText("Interesados");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_generar_excel:
                ProgressDialog progressDialog = ProgressDialog.show(this, ENVIANDO_CORREO, POR_FAVOR_ESPERE, true, false);

                Call<Response<Boolean>> call = eventoAPI.generarExcel(idEvento, Sesion.getUsuario().getEmail());

                call.enqueue(new Callback<Response<Boolean>>() {
                    @Override
                    public void onResponse(Call<Response<Boolean>> call, retrofit2.Response<Response<Boolean>> response) {
                        if (response.body().errors.length == 0) {
                            progressDialog.dismiss();
                            Snackbar.make(findViewById(R.id.container_estadisticas), CORREO_ENVIADO, Snackbar.LENGTH_LONG).show();
                        } else if (response.body().errors[0].equals(ERROR_NO_HAY_REGISTROS)){
                            progressDialog.dismiss();
                            Snackbar.make(findViewById(R.id.container_estadisticas), ERROR_NO_HAY_REGISTROS, Snackbar.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Response<Boolean>> call, Throwable t) {
                        progressDialog.dismiss();
                        Snackbar.make(findViewById(R.id.container_estadisticas), ERROR_AL_ENVIAR, Snackbar.LENGTH_LONG).show();
                    }
                });
                break;
            case  android.R.id.home:
                this.onBackPressed();
                break;
        }
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
