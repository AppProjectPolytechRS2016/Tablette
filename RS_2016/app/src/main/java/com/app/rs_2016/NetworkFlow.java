package com.app.rs_2016;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Sarion on 14/03/2015.
 */
public class NetworkFlow {

    public String readMsg(DataInputStream inStream) throws IOException
    {
        int taille      = inStream.readInt();
        byte message[]  = new byte[taille];
        int nb          = inStream.read(message,0,taille);
        System.out.println(nb);
        return new String(message);
    }

    public static void sendMsg(DataOutputStream out, String s) throws IOException
    {
        byte message[]    = s.getBytes();
        out.writeInt(message.length);
        out.write(message);
    }
}
