package mx.gob.jovenes.guanajuato.connection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by code on 27/06/16.
 */
public class ConnectionUtilities {

    public static boolean hasConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = cm.getActiveNetworkInfo();

        if (network != null && network.isConnected()) {
            return true;
        }
        return false;
    }
}
