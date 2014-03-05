package ipn.esimecu.gui;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class FuncionesJSON extends AsyncTask<String, Integer, Boolean> {
	private JSONObject json;
	
	FuncionesJSON(JSONObject json){
		this.json= json;
	}
	
	protected Boolean doInBackground (String ...params){
		boolean result = true;
		HttpClient httpClient = new DefaultHttpClient();
		
		HttpPost post = new HttpPost("daniel.zoga.com.mx/gcm_server_php/lista_descarga.php");
		post.setHeader("content-type", "application/json");
		try{
			 StringEntity entity = new StringEntity(json.toString());
		     post.setEntity(entity);
		     HttpResponse resp = httpClient.execute(post);
		     String respStr = EntityUtils.toString(resp.getEntity());
		     if(!respStr.equals("true"))
	                result = false;
		}
		catch(Exception ex){
			Log.e("ServicioRest", "Error",ex);
			result =false;
		}
		return result;
	}
}
