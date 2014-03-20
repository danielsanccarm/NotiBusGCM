package ipn.esimecu.almacenamiento;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class almacena {
	
	
	public void AlmacenarDescarga(String lista, String nombre_archivo,Activity activity){
		boolean sdDisponible = false;
		boolean sdAccesoEscritura = false;
		//Almacenamos el estado de la tarjeta SD
		String estado = Environment.getExternalStorageState();
		
		if(estado.equals(Environment.MEDIA_MOUNTED))
		{
			sdDisponible = true;
			sdAccesoEscritura=true;
			try
			{
			    File ruta_sd = Environment.getExternalStorageDirectory();
			 
			    File f = new File(ruta_sd.getAbsolutePath(), nombre_archivo);
			 
			    OutputStreamWriter fout = new OutputStreamWriter(new FileOutputStream(f));
			 
			    fout.write(lista);
			    fout.close();
			    Log.i("Almacenamiento", "Guardado correctamente en tarjeta SD");
			}
			catch (Exception ex)
			{
			    Log.e("Ficheros", "Error al escribir fichero a tarjeta SD");
			}
		}
		else if (estado.equals(Environment.MEDIA_MOUNTED_READ_ONLY))
		{
		    sdDisponible = true;
		    sdAccesoEscritura = false;
		}
		else
		{
		    sdDisponible = false;
		    sdAccesoEscritura = false;
		    try{
				  OutputStreamWriter fout =new OutputStreamWriter(activity.openFileOutput(nombre_archivo,Context.MODE_PRIVATE));
				  //for(int i=0;i<lista.length(); i++){
					  fout.write(lista);
				  //}
				  fout.close();
				  Log.i("Fichero", "Guardado correctamente en memoria interna");
			  }catch(Exception ex){
				  Log.e("Fichero", "Error al escribir el archivo");
			  }
		}
		
		  
	  }
}
