package com.appRS.tab2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import android.util.Log;

public class Client implements Runnable
{
	ExecutorService es;    // Defini le groupe de tache
	Socket sockcli = null; //Socket pour communiquer avec le serveurcket sockcli = null;
	
	private char sId = '2' ;
	private String sAdresseIP;
	private DataInputStream in;
	private DataOutputStream out;
	private boolean bRun;
	
	/**
	 * constructeur
	 * @param es	groupe de taches
	 */
	public Client (ExecutorService es, String sAdresseIP)
	{
		this.es = es;
		this.sAdresseIP = sAdresseIP;
	}

	public int connexion(String url)
	{
		try
		{
			sockcli = new Socket ();
			sockcli.bind(null);
			sockcli.connect(new InetSocketAddress(url,6020), 10000);  //Connexion au serveur via le socket 6020
		}
		catch (IOException ex){
			return -1; 
		}

		if(sockcli.isConnected()){
			try {
				this.in = new DataInputStream(sockcli.getInputStream());    //Definition du canal reseau venant du serveur
				this.out = new DataOutputStream(sockcli.getOutputStream());
				Flux.ecritureMessage(out, "0"+sId); //Envoie de l'id
			} 
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return 1;
		}
		else{
			return 0;
		}
	}
	
	/**Methode traitant les ordres venant du serveur*/
	public void traitementReception(String sMessage)
	{
		Log.d("Test","Le message est : " + sMessage);
	}
	
	/** Methode envoyant un message vers le serveur*/
	public void writeMessage(String sLeMessage)
	{
		try 
		{
			Flux.ecritureMessage(this.out, sLeMessage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run ()
	{
		this.connexion(sAdresseIP);  //Conexion au serveur
		bRun  = true;
		while(sockcli.isConnected() && bRun)
		{
			try
			{
				traitementReception(Flux.lectureMessage(in));  //Traitement des info venant du serveur
			}
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Log.d("Test","Fin de l ecoute");
		try 
		{
			sockcli.close();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*public static void main (String args[]) throws Exception
	{
		int iTestCo;
		ExecutorService es = Executors.newFixedThreadPool(3);
		Client client = new Client(es);
		
		iTestCo = client.connexion("127.0.0.1");
		
		if(iTestCo == 1){
			System.out.println("Connecte");
			es.execute(client);
		}
	}*/
}
