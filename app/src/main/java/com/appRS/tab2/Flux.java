package com.appRS.tab2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Flux
{
	/**Methode permettant de lire le flux reseau d'entree et de convertir ce qui a ete lue en String*/
	public static String lectureMessage(DataInputStream in) throws IOException
	{
		int taille = in.readInt();		
		byte message[]=new byte[taille];
		int nb = in.read(message,0,taille);
		System.out.println(nb);
		return new String(message);
	}
	
	/**Methode permettant d'ecrire un message sur le flux reseau de sortie */
	public static void ecritureMessage(DataOutputStream out, String s) throws IOException
	{
		byte message[] = s.getBytes();		
		out.writeInt(message.length);
		out.write(message);
	}
}
