package ipn.esimecu.gui;

import ipn.esimecu.gcmpushnotif.Principal;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

public class Post extends AsyncTask<JSONArray,Void,JSONArray>{
	private Activity activity;
	JSONObject jsonObj = new JSONObject();
	JSONArray arreglo;
	private InputStream is = null;
	private String respuesta = "";
	private String URL;
	private ArrayList parametros;
	
	public Post(ArrayList<Integer> lista,String URL, Activity activity){
		this.URL= URL;
		this.parametros=lista;
		this.activity=activity;
		
	}


	private void conectaPost(ArrayList parametros, String URL) {
	     List<NameValuePair> nameValuePairs;
	     
	     
	     try {
	    
            HttpClient httpclient = new DefaultHttpClient();
	    	 HttpPost httppost = new HttpPost(URL);		//Hacemos la petición
            nameValuePairs = new ArrayList<NameValuePair>();
            //Log.i(null, parametros.toString());
            if (parametros != null) {
                 	  nameValuePairs.add(new BasicNameValuePair("parada", GenerarString(parametros))); //parametros.toString()                                                                       }
                	  httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            }
	             HttpResponse response = httpclient.execute(httppost); //Ejecutamos y obtenemos la respuesta
	             //entity = response.getEntity();
	             //is = entity.getContent();
	             is= response.getEntity().getContent();
	       } catch (Exception e) {
	            Log.e("log_tag", "Error in http connection " + e.toString());
            } finally {

            }
	}
	
	private void getRespuestaPost() {
		try {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is)); //, "iso-8859-1"), 8
		StringBuilder sb = new StringBuilder();
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
	    conectaPost(parametros, URL);
        if (is != null) {
            getRespuestaPost();
        }
        if (respuesta != null && respuesta.trim() != "") {
            return getJsonArray();
         } else {
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
		this.arreglo=arreglo;
		for(int i=0; i<this.arreglo.length(); i++){
    		JSONObject objetomanipular;
			try {
				objetomanipular = this.arreglo.getJSONObject(i);
				String temp=objetomanipular.getString("ID_Estacion");
	    		Log.i("manipulando json", temp);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    		
    	}
    	AlmacenarDescarga(this.arreglo.toString());
		Intent i = new Intent(activity, Principal.class); //Principal
        activity.startActivity(i);
        
        //finish();
		
		
	}
	public void AlmacenarDescarga(String lista){
		  try{
			  OutputStreamWriter fout =new OutputStreamWriter(activity.openFileOutput("listaestaciones.txt",Context.MODE_PRIVATE));
			  //for(int i=0;i<lista.length(); i++){
				  fout.write(lista);
			  //}
			  fout.close();
			  Log.i("Fichero", "Guardado correctamente");
		  }catch(Exception ex){
			  Log.e("Fichero", "Error al escribir el archivo");
		  }
	  }
}