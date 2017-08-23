package mx.gob.jovenes.guanajuato.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.adapters.RVUsuarioRegistradoAdapter;
import mx.gob.jovenes.guanajuato.api.EventoAPI;
import mx.gob.jovenes.guanajuato.api.Response;
import mx.gob.jovenes.guanajuato.application.MyApplication;
import mx.gob.jovenes.guanajuato.model.UsuarioRegistrado;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

/**
 * Created by codigus on 11/08/2017.
 */

public class UsuariosRegistradosFragment extends Fragment implements SearchView.OnQueryTextListener {
    private RecyclerView recyclerViewUsuariosRegistrados;
    private TextView textViewEmptyUsuarios;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RVUsuarioRegistradoAdapter adapter;
    private Retrofit retrofit;
    private EventoAPI eventoAPI;
    private int idEvento;
    private List<UsuarioRegistrado> usuarioRegistrados;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        idEvento = getActivity().getIntent().getIntExtra("idEvento", 0);

        retrofit = ((MyApplication) getActivity().getApplication()).getRetrofitInstance();
        eventoAPI = retrofit.create(EventoAPI.class);

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_usuarios_registrados, container, false);

        recyclerViewUsuariosRegistrados = (RecyclerView) view.findViewById(R.id.rv_usuarios_registrados);
        textViewEmptyUsuarios = (TextView) view.findViewById(R.id.tv_empty_usuarios);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipelayout_usuarios_registrados);
        usuarioRegistrados = new ArrayList<>();
        adapter = new RVUsuarioRegistradoAdapter(getContext());

        swipeRefreshLayout.setOnRefreshListener(() -> generarLlamada());

        generarLlamada();

        return view;
    }

    private void generarLlamada() {
        swipeRefreshLayout.setRefreshing(false);
        Call<Response<List<UsuarioRegistrado>>> call = eventoAPI.obtenerUsuariosRegistrados(idEvento);

        call.enqueue(new Callback<Response<List<UsuarioRegistrado>>>() {
            @Override
            public void onResponse(Call<Response<List<UsuarioRegistrado>>> call, retrofit2.Response<Response<List<UsuarioRegistrado>>> response) {
                if (response.body().data.size() != 0) {
                    usuarioRegistrados = response.body().data;

                    adapter.add(usuarioRegistrados);

                    recyclerViewUsuariosRegistrados.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerViewUsuariosRegistrados.setAdapter(adapter);
                } else {
                    textViewEmptyUsuarios.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Response<List<UsuarioRegistrado>>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_estadisticas, menu);

        final MenuItem searchItem = menu.findItem(R.id.item_buscar_usuarios);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (!newText.equals("")) {
            final List<UsuarioRegistrado> filtro = filtro(usuarioRegistrados, newText);
            adapter.replaceAll(filtro);
            recyclerViewUsuariosRegistrados.scrollToPosition(0);
        } else {
            generarLlamada();
        }
        return true;
    }

    private List<UsuarioRegistrado> filtro(List<UsuarioRegistrado> usuarioRegistrados, String query) {
        final String lowerCase = query.toLowerCase();
        final List<UsuarioRegistrado> filtro = new ArrayList<>();

        for (UsuarioRegistrado usuarioRegistrado : usuarioRegistrados) {
            final String texto = usuarioRegistrado.getNombre().toLowerCase();
            if (texto.contains(lowerCase)) {
                filtro.add(usuarioRegistrado);
            }
        }

        return filtro;
    }
}
