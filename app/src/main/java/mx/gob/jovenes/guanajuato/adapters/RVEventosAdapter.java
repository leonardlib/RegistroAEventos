package mx.gob.jovenes.guanajuato.adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tyczj.extendedcalendarview.Event;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.fragments.DetalleEventoFragment;
import mx.gob.jovenes.guanajuato.fragments.DetalleRegionFragment;
import mx.gob.jovenes.guanajuato.model.Evento;

/**
 * Created by uriel on 21/05/16.
 */
public class RVEventosAdapter extends RecyclerView.Adapter<RVEventosAdapter.EventosViewHolder> {
    private List<Evento> eventos;
    private Context context;

    public RVEventosAdapter(Context context, List<Evento> eventos) {
        this.context = context;
        this.eventos = eventos;
    }

    @Override
    public EventosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_evento, parent, false);
        EventosViewHolder pvh = new EventosViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(EventosViewHolder holder, int position) {
        holder.tituloTv.setText(eventos.get(position).getTitulo());
        holder.descripcionTv.setText(eventos.get(position).getDescripcion());
        holder.fechaTv.setText(getFechaCast(eventos.get(position).getFechaInicio()) + " - " + getFechaCast(eventos.get(position).getFechaFin()));
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dateInStringend = getFechaCast(eventos.get(position).getFechaFin());
        try {
            Date fechafin = formatter.parse(dateInStringend);
            Date date = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date newFormat = formatter.parse(dateFormat.format(date));
            if (newFormat.before(fechafin)) {
                holder.estado.setText("Evento abierto");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    private String getFechaCast(String fecha) {
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat miFormato = new SimpleDateFormat("dd/MM/yyyy");

        try {
            String reformato = miFormato.format(formato.parse(fecha));
            return reformato;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return eventos.size();
    }

    public class EventosViewHolder extends RecyclerView.ViewHolder {
        TextView tituloTv;
        TextView descripcionTv;
        TextView fechaTv;
        TextView estado;

        EventosViewHolder(View itemView) {
            super(itemView);
            tituloTv = (TextView) itemView.findViewById(R.id.tv_titulo);
            descripcionTv = (TextView) itemView.findViewById(R.id.tv_descripcion);
            fechaTv = (TextView) itemView.findViewById(R.id.tv_fecha);
            estado = (TextView) itemView.findViewById(R.id.estado);
            itemView.setOnClickListener((View) -> {
                    DetalleEventoFragment f = DetalleEventoFragment.newInstance(eventos.get(getAdapterPosition()).getIdEvento());
                    FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.segunda_fragment_container, f).addToBackStack(null).commit();
            });
        }

    }

}
