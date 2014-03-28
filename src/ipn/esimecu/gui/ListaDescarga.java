package ipn.esimecu.gui;

import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ipn.esimecu.gcmpushnotif.Principal;

import ipn.esimecu.gcmpushnotif.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListaDescarga extends Activity {
	MyCustomAdapter dataAdapter =null;
	int num_activity;			//Variable que indica que esta activity es la número 1, se utilizará para seleccionar las funciones en el POST
	
	
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_lista_descarga);
	    num_activity=1;
	    DesplegarListView();
	    CheckButtonClick();
	  }

	  private void DesplegarListView(){
		  ArrayList<ModelParada> listaParadas = new ArrayList<ModelParada>();
		  //Insertamos los valores
		  ModelParada parada = new ModelParada(1,"1-D Metro Santa Martha - Mixcoac",false);
		  listaParadas.add(parada);
		  parada = new ModelParada(2,"9-C Centro Comercial Santa Fe - Tlacuitlapa/Puerta Grande",false);
		  listaParadas.add(parada);
		  parada = new ModelParada(3,"11 Aragón - Metro Chapultepec por la Villa",false);
		  listaParadas.add(parada);
		  parada = new ModelParada(4,"11-A Aragón - Metro Chapultepec por Av. 604",false);
		  listaParadas.add(parada);
		  parada = new ModelParada(5,"12 Aragón - Panteón San Isidro",false);
		  listaParadas.add(parada);
		  parada = new ModelParada(6,"13-A Metro Chapultepec - Torres de Padierna/Pegregal de San Nicolas",false);
		  listaParadas.add(parada);
		  parada = new ModelParada(7,"17-E Metro Universidad - San Pedro Martir por Carretera Federal",false);
		  listaParadas.add(parada);
		  parada = new ModelParada(8,"17-F Metro Tasqueña - San Pedro Martir por Fovisste",false);
		  listaParadas.add(parada);
		  parada = new ModelParada(9,"18 Metro Cuatro Caminos - Col. Moctezuma 2a. Sección",false);
		  listaParadas.add(parada);
		  parada = new ModelParada(10,"19 Metro el Rosario - Parque México por Cuitlahuac",false);
		  listaParadas.add(parada);
		  parada = new ModelParada(11,"19-A Metro el Rosario - Parque México por San Luis",false);
		  listaParadas.add(parada);
		  parada = new ModelParada(12,"23 Col- Tepetatal (El Charcho) - Metro la Raza",false);
		  listaParadas.add(parada);
		  parada = new ModelParada(13,"25 Zacatenco - Metro Potrero",false);
		  listaParadas.add(parada);
		  parada = new ModelParada(14,"27-A Reclusorio Norte - Metro Hidalgo/Alameda Central",false);
		  listaParadas.add(parada);
		  parada = new ModelParada(15,"33 León de los Aldama - Metro Chabacano",false);
		  listaParadas.add(parada);
		  parada = new ModelParada(16,"37 U.C.T.M. ATZACOALCO - Carmen Serdan",false);
		  listaParadas.add(parada);
		  parada = new ModelParada(17,"39 Metro San Lazaro - Carmen Serdan",false);
		  listaParadas.add(parada);
		  parada = new ModelParada(18,"39-A Metro San Lazaro - Xochimilco/Bosque de Nativitas por Cafetales",false);
		  listaParadas.add(parada);
		  parada = new ModelParada(19,"39-B Metro San Lazaro - Xochimilco/Bosque de Nativitas por Miramontes",false);
		  listaParadas.add(parada);
		  parada = new ModelParada(20,"43 San Felipe/León de los Aldama - Central de Abasto",false);
		  listaParadas.add(parada);
		  parada = new ModelParada(21,"46-C Lienzo Charro/Santa Martha - Central de Abasto",false);
		  listaParadas.add(parada);
		  parada = new ModelParada(22,"47-A Alameda Oriente - Xochimilco/Bosque de Nativitas",false);
		  listaParadas.add(parada);
		  parada = new ModelParada(23,"52-C Metro Santa Martha - Metro Zapata",false);
		  listaParadas.add(parada);
		  parada = new ModelParada(24,"57-A Metro Cuatro Caminos - Metro Constitución de 1917",false);
		  listaParadas.add(parada);
		  parada = new ModelParada(25,"57-C Metro Cuatro Caminos - Metro Constitución de 1917",false);
		  listaParadas.add(parada);
		  parada = new ModelParada(26,"59 Metro El Rosario - Metro Chapultepec",false);
		  listaParadas.add(parada);
		  parada = new ModelParada(27,"59-A Metro El Rosario - Sullivan",false);
		  listaParadas.add(parada);
		  parada = new ModelParada(28,"76 Centro Comercial Santa Fe - La Villa/Cantera por Palmas",false);
		  listaParadas.add(parada);
		  parada = new ModelParada(29,"76-A Centro Comercial Santa Fe - La Villa/Cantera por Reforma",false);
		  listaParadas.add(parada);
		  parada = new ModelParada(30,"101 Col. Lomas de Cuautepec - Metro Indios Verdes",false);
		  listaParadas.add(parada);
		  parada = new ModelParada(31,"101-A Ampliación Malacates - La Villa Ferroplaza",false);
		  listaParadas.add(parada);
		  parada = new ModelParada(32,"101-B Colonia Forestal - La Villa Ferroplaza",false);
		  listaParadas.add(parada);
		  parada = new ModelParada(33,"101-D Col. Cocoyotes(La Brecha) - La Villa Ferroplaza",false);
		  listaParadas.add(parada);
		  parada = new ModelParada(34,"102 Col. Cocoyotes(La Brecha) - Metro Indios Verdes",false);
		  listaParadas.add(parada);
		  parada = new ModelParada(35,"103 Ampliación Malacates - Metro la Raza",false);
		  listaParadas.add(parada);
		  parada = new ModelParada(36,"104 Col. El Tepetatal (El Charco) - Metro Potrero",false);
		  listaParadas.add(parada);
		  parada = new ModelParada(37,"107 Metro el Rosario - Metro Tacuba ",false);
		  listaParadas.add(parada);
		  parada = new ModelParada(38,"107-B Metro Martín Carrera - ",false);
		  listaParadas.add(parada);
		  parada = new ModelParada(39,"108 Col. El Tepetatal (El Charco) - Metro Indios Verdes",false);
		  listaParadas.add(parada);
		  parada = new ModelParada(40,"110 Chimalpa - Metro Tacubaya",false);
		  listaParadas.add(parada);
		  parada = new ModelParada(41,"110-B San Lorenzo Acopilco - Metro Tacubaya",false);
		  listaParadas.add(parada);
		  parada = new ModelParada(42,"110-C La Pila - Metro Tacubaya",false);
		  listaParadas.add(parada);
		  parada = new ModelParada(43,"112 Ampliación Jalalpa - Metro Tacubaya",false);
		  listaParadas.add(parada);
		  parada = new ModelParada(44,"113-B Col. Navidad (Las Piedras) - Metro Tacubaya",false);
		  listaParadas.add(parada);
		  parada = new ModelParada(45,"115 Jesus del Monte (Cuajimalpa) - Metro Tacubaya",false);
		  listaParadas.add(parada);
		  
		//create an ArrayAdaptar from the String Array
		  dataAdapter = new MyCustomAdapter(this, R.layout.parada_info, listaParadas);
		  ListView listView = (ListView) findViewById(R.id.lvListaDescargas);
		  // Assign adapter to ListView
		  listView.setAdapter(dataAdapter);
		 
		 
		  listView.setOnItemClickListener(new OnItemClickListener() {
		   public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
		    // When clicked, show a toast with the TextView text
		    ModelParada parada = (ModelParada) parent.getItemAtPosition(position);
		    Toast.makeText(getApplicationContext(),
		      "Clicked on Row: " + parada.obtenerNombre(), 
		      Toast.LENGTH_LONG).show();
		   }
		   });
		  
	  }
	  
	  
	  private class MyCustomAdapter extends ArrayAdapter<ModelParada>{

		private ArrayList<ModelParada> listaParadas;

		public MyCustomAdapter(Context context, int textViewResourceId,
				ArrayList<ModelParada> listaParada) {
			super(context, textViewResourceId,listaParada);
			this.listaParadas = new ArrayList<ModelParada>();
			this.listaParadas.addAll(listaParada);
			// TODO Auto-generated constructor stub
		}
		
		private class ViewHolder{
			TextView codigo;
			CheckBox nombre;
		}
		
		public View getView(int position, View convertView, ViewGroup parent){
			ViewHolder holder=null;
			Log.v("ConvertView", String.valueOf(position));
			if(convertView == null){
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(R.layout.parada_info, null);
				
				holder = new ViewHolder();
				//holder.codigo = (TextView) convertView.findViewById(R.id.tvCode);
				holder.nombre = (CheckBox) convertView.findViewById(R.id.cbCheck);
				convertView.setTag(holder);
				holder.nombre.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						CheckBox cb = (CheckBox) v;
						ModelParada parada = (ModelParada) cb.getTag();
						//Toast.makeText(getApplicationContext(), "Clicked on CheckBox " + cb.getTag() +
						//	       " is " + cb.isChecked(), Toast.LENGTH_SHORT).show();
						parada.setSelected(cb.isChecked());
						
					}
				});
				
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			ModelParada parada = listaParadas.get(position);
			//holder.codigo.setText("("+ parada.obtenerCodigo()+")");
			holder.nombre.setText(parada.obtenerNombre());
			holder.nombre.setChecked(parada.isSelected());
			holder.nombre.setTag(parada);
			
			return convertView;
			
			
		}
		  
	  }
	  
	  private void CheckButtonClick(){
		  final ArrayList <Integer> listaDescarga = new ArrayList<Integer>(); 			//Almacenaremos las paradas seleccionadas para enviarlas en el json
		  Button myButton = (Button) findViewById(R.id.btSolicitudDescarga);
		  myButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Toast.makeText(getApplicationContext(), "Se enviará su petición", Toast.LENGTH_LONG).show();
				StringBuffer responseText = new StringBuffer();
			    
			 
			    ArrayList<ModelParada> listaParadas = dataAdapter.listaParadas;
			    for(int i=0;i<listaParadas.size();i++){
			     ModelParada parada = listaParadas.get(i);
			     if(parada.isSelected()){
			    	 responseText.append("\n" + parada.obtenerCodigo()+ "\n" + parada.obtenerNombre());
			    	 //Colocar validación de si ya se había seleccionado anteiormente
			    	 listaDescarga.add(parada.obtenerCodigo());
			     }
			    }
			    
			    Log.i(null, "lista JSON: "+listaDescarga.size());
 		    
			    
			    try{
			    	JSONArray datos = null;
			    	Post post;
			    	if(listaDescarga.size()!=0){
			    	post = new Post(listaDescarga,"http://daniel.zoga.com.mx/gcm_server_php/lista_descarga.php",ListaDescarga.this,num_activity);

			    	post.execute(datos);
			    	}else
			    		Toast.makeText(getApplicationContext(), "Seleccione una Ruta", Toast.LENGTH_SHORT).show();
			    	

			    
			    }catch(Exception ex){
			    	Log.e(null, ex.toString());
			    	/*Toast.makeText(getBaseContext(),
                            "Error al conectar con el servidor. ",
                            Toast.LENGTH_SHORT).show();*/
			    }
			}
			  
		  });
	  }
	  
	  private boolean VerificarExistenciaJSON(ArrayList<Integer> lista, int nuevoelemento){
		  for(int i=0; i<lista.size();i++){
			  if(lista.get(i).equals(nuevoelemento)){
				  return true;
			  }
		  }
		  return false;
	  }

	  


}
