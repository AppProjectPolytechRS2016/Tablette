package com.app.rs_2016;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;

/**
 * Created by Jérôme.
 */


public class NetworkFlow
{
    /**Methode permettant de lire le flux reseau d'entree et de convertir ce qui a ete lue en String*/
    public static String readMessage(DataInputStream in) throws IOException,EOFException
    {
        int iAva    = in.available();
        byte[] bTab = new byte[iAva];
        in.readFully(bTab);
        return new String(bTab);
    }

    /**Methode permettant d'ecrire un message sur le flux reseau de sortie */
    public static void writeMessage(DataOutputStream out, String s) throws IOException
    {
        byte message[]  = s.getBytes();
        //out.writeInt(message.length);
        out.write(message);
    }
}

