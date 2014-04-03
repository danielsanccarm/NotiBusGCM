package ipn.esimecu.gcmpushnotif;
import static ipn.esimecu.gcmpushnotif.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static ipn.esimecu.gcmpushnotif.CommonUtilities.EXTRA_MESSAGE;
import static ipn.esimecu.gcmpushnotif.CommonUtilities.SENDER_ID;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;

import ipn.esimecu.gcmpushnotif.R;
import ipn.esimecu.gui.ListaDescarga;
import ipn.esimecu.almacenamiento.almacena;

import com.actionbarsherlock.view.MenuItem;
import com.google.android.gcm.GCMRegistrar;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class Inicio extends Activity {
	
	// label para mostrar gcm mensajes
    TextView lblMensaje;
    
    // Asyntask
    AsyncTask<Void, Void, Void> mRegisterTask;
    
    public static String name;
    public static String email;
    //Se almacenará el registrationID de gcm
    
	
	//Alerta
	AlertDialogManager alert = new AlertDialogManager();
	
	//Detector de Internet
	ConnectionDetector cd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mensajes);
		
		cd = new ConnectionDetector(getApplicationContext());
		
		//Verificamos si hay Internet
		if(!cd.isConnectingToInternet()){
			//No hay conexión
			alert.showAlertDialog(Inicio.this,
					"Error en la Conexión a Internet",
					"Por favor conecte a Internet", false);
			return;
		}
		
		// Getting name, email from intent
        Intent i = getIntent();
         
        name = i.getStringExtra("name");
        email = i.getStringExtra("email");     
         
        //Nos aseguramos de que tenga las dependencias correspondientes
        GCMRegistrar.checkDevice(this);
 
        // Make sure the manifest was properly set - comment out this line
        // while developing the app, then uncomment it when it's ready.
        GCMRegistrar.checkManifest(this);
 
        lblMensaje = (TextView) findViewById(R.id.TextViewMensajes);
        //lblMensaje.setText("");
        
        
        registerReceiver(mHandleMessageReceiver, new IntentFilter(
                DISPLAY_MESSAGE_ACTION));
         
        // Obtenemos el id de registro de GCM
        final String regId = GCMRegistrar.getRegistrationId(this);
        
        // Check if regid already presents
        if (regId.equals("")) {
        	
            // Aún no esta registrado, registrar
            GCMRegistrar.register(this, SENDER_ID);
        } else {
            // Dispositivo Registrado en GCM
            if (GCMRegistrar.isRegisteredOnServer(this)) {
                // Skips registration.          
            	Log.v("GCM", "Registrado");
            	String estado = Environment.getExternalStorageState();
                if (estado.equals(Environment.MEDIA_MOUNTED))
                {
                	Lectura();
                	Log.i("Lectura", "Correcto");
                }
                //Toast.makeText(getApplicationContext(), "Ya esta registrado en GCM", Toast.LENGTH_LONG).show();
            } else {
                // Try to register again, but not in the UI thread.
                // It's also necessary to cancel the thread onDestroy(),
                // hence the use of AsyncTask instead of a raw thread.
                final Context context = this;
                mRegisterTask = new AsyncTask<Void, Void, Void>() {
 
                    @Override
                    protected Void doInBackground(Void... params) {
                        // Register on our server
                        // On server creates a new user
                    	almacena guardar = new almacena();
                    	guardar.AlmacenarDescarga(regId, "registration_id.txt", Inicio.this);
                        ServerUtilities.register(context, name, email, regId);
                        Toast.makeText(getApplicationContext(), ServerUtilities.notificacion, Toast.LENGTH_SHORT).show();
                        //if(newMessage.equals("Zoga Server: Dispositivo agregado satisfactoriamente")){
                       	 String[] archivos = fileList();
                            File f = new File(Environment.getExternalStorageDirectory(),"listaestaciones.txt");
                           	 if(VerificarExistencia(archivos,"listaestaciones.txt") || f.exists()){
                           		 Intent i = new Intent(getApplicationContext(),Principal.class);
                           		 startActivity(i);
                           		 finish();
                           	 }else{
                       	    	 Intent i = new Intent(getApplicationContext(), ListaDescarga.class); //Principal
                       	         startActivity(i);
                       	         finish();
                           	 }
                        //}
                        //Toast.makeText(getApplicationContext(), ServerUtilities.notificacion, Toast.LENGTH_SHORT).show();
                        return null;
                    }
 
                    @Override
                    protected void onPostExecute(Void result) {
                        mRegisterTask = null;
                        
                        
                    }
 
                };
                mRegisterTask.execute(null, null, null);
            }
        }
	}
	
	/**
     * Receiving push messages
     * */
    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
            // Waking up mobile if it is sleeping
            WakeLocker.acquire(getApplicationContext());
             
            /**
             * Take appropriate action on this message
             * depending upon your app requirement
             * For now i am just displaying it on the screen
             * */
             
            // Showing received message
            lblMensaje.append(newMessage + "\n");      
            //Comprobamos el estado de la memoria externa (tarjeta SD)
            
            /*
            String estado = Environment.getExternalStorageState();
            if (estado.equals(Environment.MEDIA_MOUNTED))
            {
            	Lectura();
            	Log.i("Lectura", "Correcto");
            }
            */
            //Toast.makeText(getApplicationContext(), "Nuevo Mensaje: " + newMessage, Toast.LENGTH_LONG).show();
            //if(newMessage!= null)
            	//Toast.makeText(getApplicationContext(), "si lo detecto", Toast.LENGTH_SHORT).show();
            // Releasing wake lock
            WakeLocker.release();
            if(newMessage.equals("Zoga Server: Dispositivo agregado satisfactoriamente!")){
	            String[] archivos = fileList();
	            File f = new File(Environment.getExternalStorageDirectory(),"listaestaciones.txt");
	           	 if(VerificarExistencia(archivos,"listaestaciones.txt") || f.exists()){
	           		 Intent i = new Intent(getApplicationContext(),Principal.class);
	           		 startActivity(i);
	           		 finish();
	           	 }else{
	       	    	 Intent i = new Intent(getApplicationContext(), ListaDescarga.class); //Principal
	       	         startActivity(i);
	       	         finish();
	           	 }
            }
        }
    };
     
    @Override
    protected void onDestroy() {
        if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }
        try {
            unregisterReceiver(mHandleMessageReceiver);
            GCMRegistrar.onDestroy(this);
        } catch (Exception e) {
            Log.e("UnRegister Receiver Error", "> " + e.getMessage());
        }
        super.onDestroy();
    }
    
    private void Lectura(){
	    	try
			{
				File ruta_sd = Environment.getExternalStorageDirectory();
				File f = new File(ruta_sd.getAbsolutePath(), "MensajesNotiBus.txt");
				if(f.exists()){
					//lblMensaje.setText("");
					FileReader fred =
						new FileReader(f);		//El true es para activar el append, para 
																//ingresarle al final del archivo
					BufferedReader br = new BufferedReader(fred);
					String linea;
	                
	                while ((linea =br.readLine()) != null) {
	                	
	                    lblMensaje.append(linea+"\n");
	                    
	                }
	                br.close();
	                fred.close();
				}else{
					lblMensaje.setText("");
					lblMensaje.setText(R.string.sinMensajes);
				}
					
			}
			catch (Exception ex)
			{
				Log.e("Ficheros", "Error al leer el fichero a tarjeta SD");
			}
    }
    
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.inicio, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(android.view.MenuItem item) {
		// TODO Auto-generated method stub
		//Toast.makeText(getApplicationContext(), "Item select"+item.getItemId(), Toast.LENGTH_SHORT).show();
		//if(item.getItemId()==1){
			vaciarMensajes();
	        Toast.makeText(getApplicationContext(), "Se limpiaron los mensajes", Toast.LENGTH_SHORT).show();
	        lblMensaje.setText("");
		//}
		return super.onOptionsItemSelected(item);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		Toast.makeText(getApplicationContext(), "Item select"+item.getItemId(), Toast.LENGTH_SHORT).show();
		if(item.getItemId()==1){
			vaciarMensajes();
	        Toast.makeText(getApplicationContext(), "Se limpiaron los mensajes", Toast.LENGTH_SHORT).show();
	        lblMensaje.setText("");
		}
        return true; /** true -> consumimos el item, no se propaga*/
	}
	
	private void vaciarMensajes(){
		String estado = Environment.getExternalStorageState();
        if (estado.equals(Environment.MEDIA_MOUNTED))
        {
        	try
			{
				File ruta_sd = Environment.getExternalStorageDirectory();
				File f = new File(ruta_sd.getAbsolutePath(), "MensajesNotiBus.txt");
				f.delete();
			}
			catch (Exception ex)
			{
				Log.e("Ficheros", "Error al eliminar el archivo");
			}
        }
	}
	private boolean VerificarExistencia(String[] archivos, String nombre){
    	for(int i=0; i<archivos.length; i++){
    		if(nombre.equals(archivos[i]))
    			return true;
    	}
    	return false;
    }
	 
}
