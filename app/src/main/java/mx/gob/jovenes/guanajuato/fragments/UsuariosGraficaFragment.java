package mx.gob.jovenes.guanajuato.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import mx.gob.jovenes.guanajuato.R;

/**
 * Created by codigus on 14/08/2017.
 */

public class UsuariosGraficaFragment extends Fragment {
    private GraphView graphView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_usuarios_grafica, container, false);

        graphView = (GraphView) view.findViewById(R.id.grafica);

        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {
           new DataPoint(0, 1), new DataPoint(1, 5), new DataPoint(2, 3), new DataPoint(3, 2), new DataPoint(4, 6)
        });

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphView);
        staticLabelsFormatter.setHorizontalLabels(new String[]{"old", "middle", "new"});
        staticLabelsFormatter.setVerticalLabels(new String[]{"low", "middle", "high"});

        graphView.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        graphView.addSeries(series);


        return view;
    }
}
