package mx.gob.jovenes.guanajuato.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.thin.downloadmanager.DefaultRetryPolicy;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListenerV1;
import com.thin.downloadmanager.ThinDownloadManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import mx.gob.jovenes.guanajuato.connection.ClienteHttp;
import mx.gob.jovenes.guanajuato.connection.ConnectionUtilities;

/**
 * Created by Uriel on 20/03/2016.
 */
public class RetrieveVideosBroadcastReceiver extends BroadcastReceiver {
    public static final String REGISTERED_ALARM = "retrieve_video_broadcast_receiver_checked";
    public static final String FECHA_ACTUALIZACION = "retrieve_video_broadcast_receiver_fecha";
    private static SharedPreferences prefs;
    private static Context context;
    /**
     * Función registerAlarm(Context paramContext)
     *
     * @param paramContext: Contexto sobre el cual se ejecutará la alarma
     */
    public static void registerAlarm(Context paramContext) {

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        // PendingIntent that will perform a broadcast
        PendingIntent localPendingIntent = PendingIntent
                .getBroadcast(
                        paramContext.getApplicationContext(),
                        0,
                        new Intent(paramContext, RetrieveVideosBroadcastReceiver.class),
                        PendingIntent.FLAG_UPDATE_CURRENT);
        // Retrieve an AlarmManager to set a repeating daily alarm
        AlarmManager am = ((AlarmManager) paramContext.getSystemService(Context.ALARM_SERVICE));
        //am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), localPendingIntent);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_HOUR, localPendingIntent);

        context =paramContext;
        if (ConnectionUtilities.hasConnection(context)) {
            prefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
            new RevisarFechaVideoAsyncTask().execute();
        }
    }

    /**
     * onReceive(context, intent)
     * Se ejecuta cada que es enviado el broadcast, en este caso
     *
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(final Context context, Intent intent) {
        this.context = context;

        if (ConnectionUtilities.hasConnection(context)) {
            prefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
            new RevisarFechaVideoAsyncTask().execute();
        }
    }


    private static class RevisarFechaVideoAsyncTask extends AsyncTask<Void, Void, String>{

        @Override
        protected String doInBackground(Void... args) {
            String url1 = "http://" + ClienteHttp.SERVER_IP + "//app_php/video/video.php";
            HashMap<String, String> params = new HashMap<>();
            params.put("fecha_actualizacion", prefs.getString(FECHA_ACTUALIZACION, "0000-00-00 00:00"));
            ClienteHttp clienteHttp = new ClienteHttp();

            return clienteHttp.hacerRequestHttp(url1, params);

        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("DESCARGA",  result);
            String path = context.getExternalCacheDir().toString()+"/video.mp4";
            File file = new File(path);
            if (result.equals("true") || !file.exists() ) {

                String url = "http://" + ClienteHttp.SERVER_IP + "//res/video/video.mp4";
                Uri downloadUri = Uri.parse(url);
                Uri destinationUri = Uri.parse(context.getExternalCacheDir().toString() + "/video.mp4");
                DownloadRequest downloadRequest = new DownloadRequest(downloadUri)
                        .setRetryPolicy(new DefaultRetryPolicy())
                        .setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.HIGH)
                        .setStatusListener(new DownloadStatusListenerV1() {
                            @Override
                            public void onDownloadComplete(DownloadRequest downloadRequest) {
                                Log.d("Descarga", "Completada");
                                java.util.Date fecha = new java.util.Date();
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String strFecha = formatter.format(fecha).toString();
                                Log.d("DESCARGA1", strFecha);
                                prefs.edit().putString(FECHA_ACTUALIZACION, strFecha).commit();

                            }

                            @Override
                            public void onDownloadFailed(DownloadRequest downloadRequest, int errorCode, String errorMessage) {
                                Log.d("Descarga", errorCode + " : " + errorMessage);

                            }

                            @Override
                            public void onProgress(DownloadRequest downloadRequest, long totalBytes, long downloadedBytes, int progress) {


                            }
                        });
                ThinDownloadManager downloadManager = new ThinDownloadManager();
                downloadManager.add(downloadRequest);
            }

        }
    }
}