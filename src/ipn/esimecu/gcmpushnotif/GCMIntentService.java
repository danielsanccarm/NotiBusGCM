package ipn.esimecu.gcmpushnotif;

import static ipn.esimecu.gcmpushnotif.CommonUtilities.SENDER_ID;
import static ipn.esimecu.gcmpushnotif.CommonUtilities.displayMessage;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import ipn.esimecu.gcmpushnotif.R;
import com.google.android.gcm.GCMBaseIntentService;


public class GCMIntentService extends GCMBaseIntentService{
	private static final String TAG = "GCMIntentService";
	 
    public GCMIntentService() {
        super(SENDER_ID);
    }
	
    /**
     * Metodo llamado en el dispositivo registrado
     **/
    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.i(TAG, "Dispositivo Registrado: regId = " + registrationId);
        displayMessage(context, "Tu dispositivo se registro en GCM");
        Log.d("NAME", Inicio.name);
        ServerUtilities.register(context, Inicio.name, Inicio.email, registrationId);
    }
 
    /**
     * Metodo llamado en el dispositivo no registrado
     * */
    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(TAG, "Dispositivo no registrado");
        displayMessage(context, getString(R.string.gcm_unregistered));
        ServerUtilities.unregister(context, registrationId);
    }
 
    /**
     * Metodo llamado en la recepción de un mensaje
     * */
    @Override
    protected void onMessage(Context context, Intent intent) {
        Log.i(TAG, "Mensaje Recibido");
        String message = intent.getExtras().getString("price");
         
        displayMessage(context, message);
        // notifies user
        generateNotification(context, message);
    }
 
    /**
     * Method called on receiving a deleted message
     * */
    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification");
        String message = getString(R.string.gcm_deleted, total);
        displayMessage(context, message);
        // notifies user
        generateNotification(context, message);
    }
 
    /**
     * Method called on Error
     * */
    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
        displayMessage(context, getString(R.string.gcm_error, errorId));
    }
 
    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
        displayMessage(context, getString(R.string.gcm_recoverable_error,
                errorId));
        return super.onRecoverableError(context, errorId);
    }
 
    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    private static void generateNotification(Context context, String message) {
        int icon = R.drawable.ic_launcher;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(icon, message, when);
         
        String title = context.getString(R.string.app_name);
         
        Intent notificationIntent = new Intent(context, Inicio.class);
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent =
                PendingIntent.getActivity(context, 0, notificationIntent, 0);
        notification.setLatestEventInfo(context, title, message, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
         
        // Sonará cuando encuentre una notificación
        notification.defaults |= Notification.DEFAULT_SOUND;
         
        // Hacemos que vibre con una notificación
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(0, notification);     
 
    }
}
