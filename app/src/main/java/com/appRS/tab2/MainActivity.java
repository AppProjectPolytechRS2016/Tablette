package com.appRS.tab2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
//import android.os.Build;
//import android.app.ActionBar;

public class MainActivity extends Activity{
	private Button butConnexion;
	private Button butSit;
	private Button butStand;
	private Button butAnimation;
	private Button butViens;
	private Button butArret;
	private Button butSuiVisage;
	
	private TextView tView1;
	private EditText textIP;
	private Client client;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		//Mise en place des liens pour les boutons
		this.butConnexion = (Button)this.findViewById(R.id.butConnexion);
		this.butSit = (Button)this.findViewById(R.id.butSit);
		this.butStand = (Button)this.findViewById(R.id.butStand);
		this.butAnimation = (Button)this.findViewById(R.id.butAnimation);
		this.butViens = (Button)this.findViewById(R.id.butViens);
		this.butArret = (Button)this.findViewById(R.id.butArret);
		this.butSuiVisage = (Button)this.findViewById(R.id.butSuiviVisage);
		
		//Mise en place des liens pour les text
		this.textIP = (EditText)this.findViewById(R.id.editText1);
		this.tView1 = (TextView)this.findViewById(R.id.textView1);
		
		//Mise en place des Click listener
		//textIP.getText().toString()
		butConnexion.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ExecutorService es = Executors.newFixedThreadPool(3);
				client = new Client(es,textIP.getText().toString());
				//client.connexion("192.168.1.13");
				es.execute(client);
				if(tView1.getVisibility() == View.VISIBLE){
					tView1.setVisibility(View.INVISIBLE);
					//client.writeMessage("Hello_world");
				}
				else{
					tView1.setVisibility(View.VISIBLE);
					//client.writeMessage("Hello_world_retour");
				}
				
			}
		});
		butSit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				client.writeMessage("114");
			}
		});
		
		this.butStand.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				client.writeMessage("113");
				
			}
		});
		this.butAnimation.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				client.writeMessage("112");
				
			}
		}); 
		this.butViens.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				client.writeMessage("115");
				
			}
		}); 
		this.butArret.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				client.writeMessage("116");
				
			}
		}); 
		this.butSuiVisage.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				client.writeMessage("111");
				
			}
		}); 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}
}
