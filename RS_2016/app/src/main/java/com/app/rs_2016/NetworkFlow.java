package com.app.rs_2016;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;

/**
 * Created by Jérôme.
 */


public class NetworkFlow
{
    /**This method enables to read on the input network flow and convert what have been read into a String*/
    public static String readMessage(DataInputStream in) throws IOException,EOFException
    {
        //Declarations
        int iAva    = in.available();
        byte[] bTab = new byte[iAva];

        in.readFully(bTab);

        return new String(bTab);
    }

    /**This method enables to write a message on the output network flow */
    public static void writeMessage(DataOutputStream out, String s) throws IOException
    {
        byte message[]  = s.getBytes();
        //out.writeInt(message.length);
        out.write(message);
    }
}

