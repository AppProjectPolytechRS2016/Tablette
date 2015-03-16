package com.app.rs_2016;

//Import libraries
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Marion on 14/03/2015.
 */
public class ApplicationTablet {

    ExecutorService             execServ;
    Socket                      socketClient    = null;

    private String              strName;
    private String              strIPTablet;
    private int                 iPortNum;

    private DataInputStream     in;
    private DataOutputStream    out;
    private boolean             bRun;
    private char                sId             = '1' ;

    public ApplicationTablet(String strName, int iPortNum, ExecutorService execServ) {
        this.strName    = strName;
        this.iPortNum   = iPortNum;
        this.execServ   = execServ;
    }

    public int connexionCM(String IPCMAddress, int iPortNum)
    {
        try
        {
            socketClient    = new Socket(IPCMAddress, iPortNum);
          //  socketClient.bind(null);
            //socketClient.connect(new InetSocketAddress(IPCMAddress,iPortNum), 10000);
        }
        catch (IOException ex){
            return -1;
        }

        if(socketClient.isConnected()){
            try {

                //Define the network stream from the comManager
                this.in     = new DataInputStream(socketClient.getInputStream());
                this.out    = new DataOutputStream(socketClient.getOutputStream());
                NetworkFlow.writeMessage(out, "0" + sId);

                //
           /*    JSONObject jsonIdent        = new org.json.JSONObject();

                try {
                    jsonIdent.put("From", tabletAddress);
                    jsonIdent.put("From", IPCMAddress);
                    jsonIdent.put("Type", "Identification");
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }*/

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

    public void deconnexionCM()
    {
    }

    public void sendOrder(JSONObject order)
    {

    }

    public void ask4video(JSONObject order)
    {

    }
}
