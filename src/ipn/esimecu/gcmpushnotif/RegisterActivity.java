package ipn.esimecu.gcmpushnotif;

import static ipn.esimecu.gcmpushnotif.CommonUtilities.SENDER_ID;
import static ipn.esimecu.gcmpushnotif.CommonUtilities.SERVER_URL;

import java.io.File;

import com.google.android.gcm.GCMRegistrar;

import ipn.esimecu.gcmpushnotif.R;
import ipn.esimecu.gui.ListaDescarga;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity{
	// alert dialog manager
    AlertDialogManager alert = new AlertDialogManager();
    static public String regId;
    // Internet detector
    ConnectionDetector cd;
     
    // UI elements
    EditText txtName;
    EditText txtEmail;
     
    // Register button
    Button btnRegister;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        cd = new ConnectionDetector(getApplicationContext());
        String[] archivos = fileList();
      //Nos aseguramos de que tenga las dependencias correspondientes
        GCMRegistrar.checkDevice(this);
 
        // Make sure the manifest was properly set - comment out this line
        // while developing the app, then uncomment it when it's ready.
        GCMRegistrar.checkManifest(this);
         
        // Get GCM registration id
        final String regId = GCMRegistrar.getRegistrationId(this);
        this.regId= regId;
     // Device is already registered on GCM
     if (!regId.equals("")){
    	 
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
      }else
        if (GCMRegistrar.isRegisteredOnServer(this)) { //El correcto es sin el !
            // Skips registration.             
            Toast.makeText(getApplicationContext(), "Ya esta registrado en GCM", Toast.LENGTH_LONG).show();
            
            Intent i = new Intent(getApplicationContext(), Principal.class);
            startActivity(i);
            finish();
            
        }else{
        	Toast.makeText(getApplicationContext(), "Sin registrarse GCM", Toast.LENGTH_LONG).show();
	        // Check if Internet present
	        if (!cd.isConnectingToInternet()) {
	            // Internet Connection is not present
	            alert.showAlertDialog(RegisterActivity.this,
	                    "Internet Connection Error",
	                    "Please connect to working Internet connection", false);
	            // stop executing code by return
	            return;
	        }
	 
	        // Check if GCM configuration is set
	        if (SERVER_URL == null || SENDER_ID == null || SERVER_URL.length() == 0
	                || SENDER_ID.length() == 0) {
	            // GCM sernder id / server url is missing
	            alert.showAlertDialog(RegisterActivity.this, "Configuration Error!",
	                    "Please set your Server URL and GCM Sender ID", false);
	            // stop executing code by return
	             return;
	        }
	         
	        txtName = (EditText) findViewById(R.id.txtName);
	        txtEmail = (EditText) findViewById(R.id.txtEmail);
	        btnRegister = (Button) findViewById(R.id.btnRegister);
	         
	        /*
	         * Click event on Register button
	         * */
	        btnRegister.setOnClickListener(new View.OnClickListener() {
	             
	            @Override
	            public void onClick(View arg0) {
	                // Read EditText dat
	                String name = txtName.getText().toString();
	                String email = txtEmail.getText().toString();
	                 
	                // Check if user filled the form
	                if(name.trim().length() > 0 && email.trim().length() > 0){
	                    // Launch Main Activity
	                    Intent i = new Intent(getApplicationContext(), Inicio.class);
	                     
	                    // Registering user on our server                  
	                    // Sending registraiton details to MainActivity
	                    i.putExtra("name", name);
	                    i.putExtra("email", email);
	                    startActivity(i);
	                    finish();
	                }else{
	                    // user doen't filled that data
	                    // ask him to fill the form
	                    alert.showAlertDialog(RegisterActivity.this, "Registration Error!", "Please enter your details", false);
	                }
	            }
	        });
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
