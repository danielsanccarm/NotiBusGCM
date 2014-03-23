package ipn.esimecu.gui;

import ipn.esimecu.gcmpushnotif.Principal;
import static ipn.esimecu.gcmpushnotif.RegisterActivity.regId;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;


public class Post extends AsyncTask<JSONArray,Void,JSONArray>{
	private Activity activity;
	JSONObject jsonObj = new JSONObject();
	JSONArray arreglo;
	private InputStream is = null;
	private String respuesta = "";
	private String URL;
	private ArrayList parametros;
	private int num_activityActivada;
	
	//Inicializamos todos los valores, que se proporcionan al crear el objeto POST
	public Post(ArrayList<Integer> lista,String URL, Activity activity, int activo){
		this.URL= URL;
		this.parametros=lista;
		this.activity=activity;
		this.num_activityActivada=activo;//Permitirá identificar en que activity se encuentra
		
	}


	private void conectaPost(ArrayList parametros, String URL) {
	     List<NameValuePair> nameValuePairs;
	     try {
	    
            HttpClient httpclient = new DefaultHttpClient();
	    	HttpPost httppost = new HttpPost(URL);		//Hacemos la petición
	    	 
	    	JSONObject jsonObject = new JSONObject();		//Construirmos el JSONObject
	    	String json="";
	    	if(parametros != null){
	    		jsonObject.put("parada", "["+GenerarString(parametros)+"]");
	    		if(num_activityActivada==2)
	    			jsonObject.put("id_gcm", regId);
	    		
	    		//Pasamos el json Object a string
	    		json=jsonObject.toString();
	    		Log.i("json to string", json);
	    		StringEntity se = new StringEntity(json);
	    		
	    		httppost.setEntity(se);
	    		// Encabezados para que el servidor detecte el post 
	            httppost.setHeader("Accept", "application/json");
	            httppost.setHeader("Content-type", "application/json");
	    	}
	    	 
	    	HttpResponse response = httpclient.execute(httppost); //Ejecutamos y obtenemos la respuesta 
            
            //entity = response.getEntity();
            //is = entity.getContent();
            is= response.getEntity().getContent();
	    	 
	    	 
	    	 
	    	 
	    	 
	    	 
	    	/* 
            nameValuePairs = new ArrayList<NameValuePair>();
            //Log.i(null, parametros.toString());
            if (parametros != null) {
            		//Agregamos un parametro que se llamará parada, para ser enviado, y se compondra de una cadena separada por comas 
                 	  nameValuePairs.add(new BasicNameValuePair("parada", GenerarString(parametros))); 
                 	  
                 	  if(num_activityActivada==2){			//En caso de ser la activity 2 (Principal) se agrega una variable extra
                 		  nameValuePairs.add(new BasicNameValuePair("id_gcm",regId));
                 	  
                 	  }
                	  httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            }
	             HttpResponse response = httpclient.execute(httppost); //Ejecutamos y obtenemos la respuesta
	             
	             //entity = response.getEntity();
	             //is = entity.getContent();
	             is= response.getEntity().getContent();
	             
	       */      
	             
	       } catch (Exception e) {
	            Log.e("log_tag", "Error in http connection " + e.toString());
            } finally {

            }
	}
	
	private void getRespuestaPost() {
		try {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is)); //, "iso-8859-1"), 8
		StringBuilder sb = new StringBuilder();
		Log.v("Respuesta Post", "Comienza aqui");
		String line = null;
		while ((line = reader.readLine()) != null) {
		        sb.append(line + "\n");
		}
		is.close();
		respuesta = sb.toString();
		Log.i(null, "Cadena " + respuesta);
		} catch (Exception e) {
	          Log.e("log_tag", "Error converting result " + e.toString());
	    }
	}

	
	@SuppressWarnings("finally")
	private JSONArray getJsonArray() {
        JSONArray jArray = null;
        try {
           jArray = new JSONArray(respuesta);
           
        } catch (Exception e) {

        } finally {
            return jArray;
        }
	}

	public JSONArray getServerData() { //ArrayList parametros, String URL
		Log.i("AntesdeObtenerDatos", parametros.toString()+""+URL);
		conectaPost(parametros, URL);
	    
        if (is != null) {
        	Log.i("InputStream", "NO vacio");
            getRespuestaPost();
        }
        if (respuesta != null && respuesta.trim() != "") {
            return getJsonArray();
         } else {
        	 Log.i("InputStream", "VACIO");
            return null;
         }
    }
	
	private String GenerarJSON(ArrayList lista){
		JSONArray jsonArray = new JSONArray();
		  try {
			  
			jsonObj.put("parada", lista);
			for(int i=0; i<lista.size(); i++)
				jsonArray.put(lista.get(i));
			//jsonArray.put(lista);
			jsonObj.put("parada", jsonArray); //jsonArray
			Log.i(null,jsonArray.toString());
			Log.i(null, jsonObj.toString());
			
			//Manar el json como cadena
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObj.toString();
		
	  }
	String GenerarString(ArrayList lista){
		String arreglo="";
		for(int i=0; i<lista.size(); i++){
			if(i>0)
				arreglo+=",";
			arreglo+=lista.get(i);
		}
		return arreglo;
	}


	@Override
	protected JSONArray doInBackground(JSONArray... params) {
		// TODO Auto-generated method stub
		
		this.arreglo = params[0];
		return getServerData();
	}
	
	protected void onPostExecute(JSONArray arreglo){
		
		Intent i = null;
		//Seleccionamos las funciones a ejecutarse de la actividad que llamo la función
		switch(num_activityActivada){
			case 1:
				this.arreglo=arreglo;
				for(int a=0; a<this.arreglo.length(); a++){
		    		JSONObject objetomanipular;
					try {
						objetomanipular = this.arreglo.getJSONObject(a);
						String temp=objetomanipular.getString("ID_Estacion");
			    		Log.i("manipulando json", temp);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    		
		    		
		    	}				
				
				Log.i("Comparando actividad", "Correcto");
		    	AlmacenarDescarga(this.arreglo.toString(),"listaestaciones.txt");
				 i= new Intent(activity, Principal.class); //Principal
				 activity.startActivity(i);
				 activity.finish();
		        break;
			case 2:
				if(respuesta.indexOf("false")!=-1)
					Toast.makeText(activity, "Se ha enviado satisfactoriamente su petición...", Toast.LENGTH_SHORT).show();
				else{
					Toast.makeText(activity, "No se pudo entregar su petición, intentelo de nuevo", Toast.LENGTH_SHORT).show();
					
				}
				break;
			default:
				Log.i("Comparando actividad", "No se pudo");
	}
		
        //finish();
		
		
	}
	public void AlmacenarDescarga(String lista, String nombre_archivo){
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
			    fout.flush();
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