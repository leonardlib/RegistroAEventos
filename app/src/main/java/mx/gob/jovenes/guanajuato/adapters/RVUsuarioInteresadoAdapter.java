package mx.gob.jovenes.guanajuato.adapters;

import android.content.Context;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Comparator;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.model.UsuarioRegistrado;

/**
 * Created by codigus on 11/08/2017.
 */

public class RVUsuarioInteresadoAdapter extends RecyclerView.Adapter<RVUsuarioInteresadoAdapter.UsuarioInteresadoViewHolder>{
    private Context context;
    private Comparator<UsuarioRegistrado> COMPARADOR_ALFABETICO = (o1, o2) -> o1.getNombre().compareTo(o2.getNombre());
    private SortedList<UsuarioRegistrado> usuariosInteresadosSortedList = new SortedList<>(UsuarioRegistrado.class, new SortedList.Callback<UsuarioRegistrado>() {
        @Override
        public int compare(UsuarioRegistrado o1, UsuarioRegistrado o2) {
            return COMPARADOR_ALFABETICO.compare(o1, o2);
        }

        @Override
        public void onChanged(int position, int count) {
            notifyItemRangeChanged(position, count);
        }

        @Override
        public boolean areContentsTheSame(UsuarioRegistrado oldItem, UsuarioRegistrado newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areItemsTheSame(UsuarioRegistrado item1, UsuarioRegistrado item2) {
            return item1.getNombre() == item2.getNombre();
        }

        @Override
        public void onInserted(int position, int count) {
            notifyItemRangeInserted(position, count);
        }

        @Override
        public void onRemoved(int position, int count) {
            notifyItemRangeRemoved(position, count);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            notifyItemRangeRemoved(fromPosition, toPosition);
        }
    });

    public RVUsuarioInteresadoAdapter(Context context) {
        this.context = context;
    }

    @Override
    public UsuarioInteresadoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_rv_usuario_registrado, parent, false);
        return new UsuarioInteresadoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(UsuarioInteresadoViewHolder holder, int position) {
        String rutaImagen = usuariosInteresadosSortedList.get(position).getRutaImagen();
        String nombre = usuariosInteresadosSortedList.get(position).getNombre() + " " + usuariosInteresadosSortedList.get(position).getApellidoPaterno() + " " + usuariosInteresadosSortedList.get(position).getApellidoMaterno();
        String correo = usuariosInteresadosSortedList.get(position).getEmail();
        String idGuanajoven = usuariosInteresadosSortedList.get(position).getIdGuanajoven();

        Picasso.with(context).load(rutaImagen).into(holder.imageViewImagenUsuario);
        holder.textViewNombreUsuario.setText(context.getResources().getText(R.string.indicativos_nombre) + nombre);
        holder.textViewCorreoUsuario.setText(context.getResources().getText(R.string.indicativos_correo) + correo);
        holder.textViewPuntuacionUsuario.setText(context.getResources().getText(R.string.indicativos_id_guanajoven) + idGuanajoven);
    }

    public void add(UsuarioRegistrado usuarioRegistrado) {
        usuariosInteresadosSortedList.add(usuarioRegistrado);
    }

    public void remove(UsuarioRegistrado usuarioRegistrado) {
        usuariosInteresadosSortedList.remove(usuarioRegistrado);
    }

    public void add(List<UsuarioRegistrado> usuariosRegistrados) {
        usuariosInteresadosSortedList.addAll(usuariosRegistrados);
    }

    public void remove(List<UsuarioRegistrado> usuariosRegistrados) {
        usuariosInteresadosSortedList.beginBatchedUpdates();
        for (UsuarioRegistrado usuarioRegistrado : usuariosRegistrados) {
            usuariosInteresadosSortedList.remove(usuarioRegistrado);
        }
        usuariosInteresadosSortedList.endBatchedUpdates();
    }

    public void replaceAll(List<UsuarioRegistrado> usuariosRegistrados) {
        usuariosInteresadosSortedList.beginBatchedUpdates();
        for (int i = usuariosInteresadosSortedList.size() - 1; i >= 0; i--) {
            final UsuarioRegistrado usuarioRegistrado = usuariosInteresadosSortedList.get(i);
            if (!usuariosRegistrados.contains(usuarioRegistrado)) {
                usuariosInteresadosSortedList.remove(usuarioRegistrado);
            }
        }
        usuariosInteresadosSortedList.addAll(usuariosRegistrados);
        usuariosInteresadosSortedList.endBatchedUpdates();
    }

    @Override
    public int getItemCount() {
        if (usuariosInteresadosSortedList != null) {
            return usuariosInteresadosSortedList.size();
        } else {
            return 0;
        }
    }

    public class UsuarioInteresadoViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView imageViewImagenUsuario;
        private TextView textViewNombreUsuario;
        private TextView textViewCorreoUsuario;
        private TextView textViewPuntuacionUsuario;

        public UsuarioInteresadoViewHolder(View item) {
            super(item);

            imageViewImagenUsuario = (CircleImageView) item.findViewById(R.id.imagen_usuario_registrado);
            textViewNombreUsuario = (TextView) item.findViewById(R.id.textview_nombre_usuario_registrado);
            textViewCorreoUsuario = (TextView) item.findViewById(R.id.textview_correo_usuario_registrado);
            textViewPuntuacionUsuario = (TextView) item.findViewById(R.id.textview_puntuacion_usuario_registrado);

        }

    }
}
