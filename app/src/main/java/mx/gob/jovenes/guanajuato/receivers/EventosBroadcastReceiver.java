package mx.gob.jovenes.guanajuato.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Map;

import mx.gob.jovenes.guanajuato.connection.ConnectionUtilities;

/**
 * Created by leonardolirabecerra on 08/08/17.
 */

public class EventosBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (ConnectionUtilities.hasConnection(context)) {
            System.out.println("Si hay conexión, registra los eventos guardados");
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            Map<String, ?> jsonRegistro = prefs.getAll();
            System.out.println(jsonRegistro);
        }
    }
}
