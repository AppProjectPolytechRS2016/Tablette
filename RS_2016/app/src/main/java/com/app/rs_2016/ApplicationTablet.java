package com.app.rs_2016;

//Import libraries
import org.json.* ;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import android.util.Log;

/**
 * Created by Sarion on 14/03/2015.
 */
public class ApplicationTablet {

    ExecutorService             execServ;
    Socket                      socketClient;

    private String              strName;
    private String              strIPTablet;
    private int                 iPortNum;

    private DataInputStream     in;
    private DataOutputStream    out;
    private boolean             bRun;

    public ApplicationTablet(String strName, int iPortNum, ExecutorService execServ) {
        this.strName    = strName;
        this.iPortNum   = iPortNum;
        this.execServ   = execServ;
    }

    public boolean connexionCM(String IPCMAddress, int iPortNum)
    {
        try
        {
            socketClient    = new Socket ();
            socketClient.bind(null);
            socketClient.connect(new InetSocketAddress(IPCMAddress,iPortNum), 10000);  //Connexion au gestionneaire de communication
        }
        catch (IOException ex){
            return false;
        }

        if(socketClient.isConnected()){
            try {
                this.in     = new DataInputStream(socketClient.getInputStream());    //Definition du canal reseau venant du serveur
                this.out    = new DataOutputStream(socketClient.getOutputStream());
            }
            catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return true;
        }
        else{
            return false;
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
