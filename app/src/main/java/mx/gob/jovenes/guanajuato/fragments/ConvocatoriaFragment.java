package mx.gob.jovenes.guanajuato.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.adapters.RVConvocatoriaAdapter;
import mx.gob.jovenes.guanajuato.api.ConvocatoriaAPI;
import mx.gob.jovenes.guanajuato.api.Response;
import mx.gob.jovenes.guanajuato.application.MyApplication;
import mx.gob.jovenes.guanajuato.model.Convocatoria;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

/**
 * Created by Juan José Estrada Valtierra on 12/04/17.
 */
public class ConvocatoriaFragment extends CustomFragment{
    private ConvocatoriaAPI convocatoriaAPI;
    private RecyclerView rvConvocatoria;
    private TextView tvEmptyConvocatoria;
    private RVConvocatoriaAdapter adapter;
    private Retrofit retrofit;
    private ArrayList<Convocatoria> convocatorias;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Instancias de la API
        retrofit = ((MyApplication) getActivity().getApplication()).getRetrofitInstance();
        convocatoriaAPI = retrofit.create(ConvocatoriaAPI.class);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_convocatorias, container, false);
        rvConvocatoria = (RecyclerView) v.findViewById(R.id.rv_convocatoria);
        tvEmptyConvocatoria = (TextView) v.findViewById(R.id.tv_empty_convocatoria);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());

        rvConvocatoria.setLayoutManager(llm);

        Call<Response<ArrayList<Convocatoria>>> call = convocatoriaAPI.get();

        //Llamada a servidor caso de acertar o fallar
        call.enqueue(new Callback<Response<ArrayList<Convocatoria>>>() {
            @Override
            public void onResponse(Call<Response<ArrayList<Convocatoria>>> call, retrofit2.Response<Response<ArrayList<Convocatoria>>> response) {
                if(response.body().success) {

                    convocatorias = response.body().data;
                    adapter = new RVConvocatoriaAdapter(getActivity(), convocatorias);
                    rvConvocatoria.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<Response<ArrayList<Convocatoria>>> call, Throwable t) {
                Log.d("Titulo","...");
            }
        });

        return v;
    }

    @Override
    public void onStop() {
        super.onStop();
        final Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        getActivity().findViewById(R.id.toolbar).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.collapsing_toolbar).setVisibility(View.GONE);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().findViewById(R.id.toolbar).setVisibility(View.GONE);
        getActivity().findViewById(R.id.collapsing_toolbar).setVisibility(View.VISIBLE);
        final Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar2);
        //final Drawable d = Drawable.createFromPath("@drawable/reports");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Convocatorias");
        ((CollapsingToolbarLayout)getActivity().findViewById(R.id.collapsing_toolbar)).setTitle("Convocatorias");
        //((CollapsingToolbarLayout)getActivity().findViewById(R.id.collapsing_toolbar)).setStatusBarScrim(d);

    }
}
