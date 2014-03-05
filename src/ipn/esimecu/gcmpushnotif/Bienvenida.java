/*
 * Clase dedicada para darle funcionamiento al layout bienvenida
 * Para posteriormente dirigirse a la clase de RegisterActivity
 */
package ipn.esimecu.gcmpushnotif;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Bienvenida extends Activity {

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        long duracion=2500;		//Tiempo que se mostrara la pantalla de bienvenida
        
        setContentView(R.layout.bienvenida);	//Mostramos el contenido del layout bienvenida
        
        
        TimerTask timerTask = new TimerTask(){
        	
        	public void run(){
        	 Intent mainIntent = new Intent().setClass(Bienvenida.this, RegisterActivity.class);
        	 startActivity(mainIntent);
        	 finish();
        	}
        };
        
        Timer timer = new Timer();
        timer.schedule(timerTask, duracion);
        
    }
}
