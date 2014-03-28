package ipn.esimecu.gcmpushnotif;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuInflater;
import com.google.android.gcm.GCMRegistrar;

import static android.support.v4.view.ViewPager.OnPageChangeListener;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.actionbarsherlock.app.ActionBar.NAVIGATION_MODE_TABS;
import static com.actionbarsherlock.app.ActionBar.TabListener;
import ipn.esimecu.gui.ListaDescarga;
import ipn.esimecu.gui.Post;

public class Principal extends SherlockFragmentActivity implements OnPageChangeListener, TabListener {
	   private static int conteoTabs=0;
	   private static int num_activity;
	   JSONObject objeto;
	    ViewPager mPager;
	    static TextView NombreRuta;
	    static ListView lv=null;
	    private String[] estaciones;
	    static int seleccion;		//Variable utilizada para indicar en que Tab se encuentra el usuario 
	    private static ArrayList<ArrayList <String>> estac_nombre = new ArrayList<ArrayList<String>>();	//Sería como una matriz bidimensional de ArrayList donde se guardaran las estaciones de una ruta en un renglon 
	    private static ArrayList<ArrayList <String>> estac_codigo = new ArrayList<ArrayList<String>>();
	    static ArrayAdapter<String> adaptador ;
	    public String regId;
	    JSONArray jsonArray; //Creamos el objeto JSONArray

	    
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_principal);
	        //Limpiamos las variables para evitar repetir valores
	        conteoTabs=0;
	        lv=null;
	        estac_nombre.clear();
	        estac_codigo.clear();
	        num_activity=2;
	        
	        ArrayList<String> arregloVacio= new ArrayList();
	        NombreRuta = (TextView) findViewById(R.id.tvNombreRuta);
	        mPager = (ViewPager)findViewById(R.id.pager);
	        
	        ArrayList<String> temp = new ArrayList();
	     // Obtenemos el id de registro de GCM
	        regId= GCMRegistrar.getRegistrationId(this);
	        ActionBar ab = getSupportActionBar();
	        //ab.setNavigationMode(NAVIGATION_MODE_TABS);
	        //Insertaremos las pestañas conforme a lo solicitado por el usuario, obtendremos
	        //los valores de archivo listaestaciones
	        LeerCargarJson();
	       
	       
	        String verificar_existencia=""; //Variable que se utilizará verificar que no se haya insertado ese nombre de la ruta
	        int contruta=-1; //Nos servirá para separar por ruta dentro de la matriz
	        for(int i=0; i<jsonArray.length(); i++){
	        	try {
	        		
					objeto = jsonArray.getJSONObject(i);
					temp.add(objeto.getString("Nombre"));
					
					if(!verificar_existencia.equals(objeto.getString("Ruta")))
					{
						
						verificar_existencia=objeto.getString("Ruta");
						Log.i("Ruta", objeto.getString("Ruta"));
						ab.addTab(ab.newTab().setText(verificar_existencia).setTabListener(this));
						if(i==0)
							contruta=0;
						else
							contruta++;
						Log.i("conteo", ""+contruta);
						estac_nombre.add(new ArrayList<String>());	//Insertamos una nueva fila
						estac_codigo.add(new ArrayList<String>());
						conteoTabs++;
					
					}
						estac_nombre.get(contruta).add(temp.get(i));		//Insertamos las estaciones en la fila correspondiente a su ruta
						estac_codigo.get(contruta).add(objeto.getString("ID_Estacion"));
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	        
	        mPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
	        mPager.setOnPageChangeListener(this);
	        ab.setNavigationMode(NAVIGATION_MODE_TABS);
	        adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,arregloVacio);		//Inicializamos el adaptador
	        
	    }

	    public boolean onCreateOptionsMenu(Menu menu){
	    	MenuInflater inflater = getSupportMenuInflater();
	        inflater.inflate(R.menu.principal, menu);
	        return true;
	    }
	    
	     public boolean onOptionsItemSelected(MenuItem item) {
	    	 	
	              switch (item.getItemId()) {
	              case R.id.Mensajes:
	            	  Intent i = new Intent(getApplicationContext(), Inicio.class);
	                  startActivity(i);
	                     break;
	              case R.id.Estaciones:
	            	  i = new Intent(getApplicationContext(), ListaDescarga.class);
	            	  startActivity(i);
	            	  break;
	              }
	              
	              return true; /** true -> consumimos el item, no se propaga*/
	     }
	    
	    @Override
	    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
	    }

	    @Override
	    public void onPageSelected(int position) {
	    	//Limpiamos el adaptador para insertar los nuevos valores
	    	adaptador.clear();
	    	ArrayList<String> est;
	    	//ArrayAdapter <String> adaptador = null;
	        getSupportActionBar().setSelectedNavigationItem(position);
	        NombreRuta.setText(""+position);		//Mostramos el nombre de la ruta
	        Log.i(null, ""+position);
	        
	        est=estac_nombre.get(position);		//Asignamos toda la fila 0 al ArrayList est
	        
	        seleccion=position;
	            
            for(int i=0; i<est.size(); i++){
            	Log.i("Prueba onCreateView", est.get(i));
            	adaptador.add(est.get(i));		//Agregamos un nuevo elemento al adaptador del ListView
            }
            
	    }

	    @Override
	    public void onPageScrollStateChanged(int state) {
	    }

	    @Override
	    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
	    	
	        mPager.setCurrentItem(tab.getPosition());
	    }

	    @Override
	    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
	    }

	    @Override
	    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
	    }
	    
	    protected void onListItemClick(ListView l, View v, int position, long id) {
	        //Toast.makeText(getApplicationContext(), adaptador.getItem(position), Toast.LENGTH_SHORT).show();
	    	Log.i("onListItemClick", adaptador.getItem(position));
	    }
	    
	    void LeerCargarJson(){
	    	
	    	String estado = Environment.getExternalStorageState();
	    	if(estado.equals(Environment.MEDIA_MOUNTED))
			{
	    		try
	    		{
	    		    File ruta_sd = Environment.getExternalStorageDirectory();
	    		 
	    		    File f = new File(ruta_sd.getAbsolutePath(), "listaestaciones.txt");
	    		 
	    		    BufferedReader fin =
	    		        new BufferedReader(
	    		            new InputStreamReader(
	    		                new FileInputStream(f)));
	    		 
	    		    String texto = fin.readLine();
	    		    fin.close();
	    		    jsonArray= new JSONArray(texto); //Asignamos el contenido al jsonArray
		    	    Log.i("Lectura", "Cargado exitosamente desde SD");
	    		}
	    		catch (Exception ex)
	    		{
	    		    Log.e("Ficheros", "Error al leer fichero desde tarjeta SD");
	    		}
			}else{			
		    	try
		    	{
		    		//Leemos el contenido del archivo, en donde tendremos almacenada la cadena json
		    	    BufferedReader fin = new BufferedReader( new InputStreamReader(
		    	                openFileInput("listaestaciones.txt")));
		    	 
		    	    String texto = fin.readLine();
		    	    fin.close(); //Cerramos el buffer de lectura
		    	    jsonArray= new JSONArray(texto); //Asignamos el contenido al jsonArray
		    	    Log.i("Lectura", "Cargado exitosamente desde memoria interna");
		    	}
		    	catch (Exception ex)
		    	{
		    	    Log.e("Ficheros", "Error al leer fichero desde memoria interna");
		    	}
			}
	    }
	    

	    public static class MyAdapter extends FragmentStatePagerAdapter {
	        public MyAdapter(FragmentManager fm) {
	            super(fm);
	        }

	        @Override
	        public int getCount() {
	        	
	            return conteoTabs;
	        	
	        }

	        @Override
	        public Fragment getItem(int position) {
	        	
	        	//Log.i("Fragment", Integer.toString(position));
	        	Log.i("getItem",""+position);
	        	if(conteoTabs<=position)
	        		Log.i("Fragment", conteoTabs+"<="+position);
	        	return BoringFragment.newInstance(position + 1);
	        	
	        }
	    }

	    public static class BoringFragment extends SherlockFragment {
	        int mNum;
	        ArrayList<String> est_nomb = null, est_cod = null;

	        /**
	         * Create a new instance of CountingFragment, providing "num"
	         * as an argument.
	         */
	        static BoringFragment newInstance(int num) {
	          BoringFragment f = new BoringFragment();

	            // Supply num input as an argument.
	            Bundle args = new Bundle();
	            args.putInt("num", num);
	            f.setArguments(args);
	            
	            return f;
	        }

	        /**
	         * When creating, retrieve this instance's number from its arguments.
	         */
	        @Override
	        public void onCreate(Bundle savedInstanceState) {
	            super.onCreate(savedInstanceState);
	            mNum = getArguments() != null ? getArguments().getInt("num") : 1;
	            
	        }

	        /**
	         * The Fragment's UI is just a simple text view showing its
	         * instance number.
	         */
	        @Override
	        public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                                 Bundle savedInstanceState) {
	        
	        	
	        	 lv = new ListView(getActivity());	
	            //TextView tv = new TextView(getActivity());
	            lv.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT));
	            
	            //Cargamos unicamente la Primera Tab
	            if(mNum-1==0){
		            est_nomb=estac_nombre.get(mNum-1);		//Asignamos toda la fila 0 al ArrayList est
		            est_cod=estac_codigo.get(mNum-1);
		            //NombreRuta.setText(mNum-1); //Mostramos el nombre de la ruta								+++++++
		            seleccion=0;
		            for(int i=0; i<est_nomb.size(); i++){
		            	Log.i("Prueba onCreateView", est_nomb.get(i));
		            	adaptador.add(est_nomb.get(i));		//Agregamos un nuevo elemento al adaptador del ListView
		            }
	            }
	            
	    
	            lv.setAdapter(adaptador); //Cargamos la lista
	            //Capturamos el click
	            lv.setOnItemClickListener(new OnItemClickListener(){

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						Toast.makeText(getActivity(), adaptador.getItem(arg2), Toast.LENGTH_LONG).show();
						Log.i("codigo",estac_codigo.get(seleccion).get(arg2));
						final ArrayList <Integer> listaSolicitud = new ArrayList<Integer>(); 			//Almacenaremos las paradas seleccionadas para enviarlas en el json
						
						listaSolicitud.add(Integer.parseInt(estac_codigo.get(seleccion).get(arg2)));
						
						try{
						JSONArray datos = null;
				    	Post post;
				    	
				    	if(listaSolicitud.size()!=0){		//Verificamos que se haya insertado
				    		
				    		post = new Post(listaSolicitud,"http://daniel.zoga.com.mx/gcm_server_php/solicitud_notificacion.php",getActivity(),num_activity);
				    		post.execute(datos);
				    	}else{
				    		Toast.makeText(getActivity(), "Seleccione una estación", Toast.LENGTH_SHORT).show();
				    	}
						}catch(Exception ex){
							Log.e(null, ex.toString());
						}
						
					}
	            	
	            });
	            	
	            return lv;
	        }

	    }

}