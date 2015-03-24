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


    /**
     * @Function : ApplicationTablet
     * @param strName : the name of the device
     * @param iPortNum : port number for the communication with the comManager
     * @param execServ : executor service to use
     * @Description : Constructor
     */
    public ApplicationTablet(String strName, int iPortNum, ExecutorService execServ) {
        this.strName    = strName;
        this.iPortNum   = iPortNum;
        this.execServ   = execServ;
    }


    /**
     * @Function : connexionCM
     * @param IPCMAddress : IP address of the comManager
     * @param iPortNum : port number used for the communication with the comManager
     * @param IPTablet : IP address of the tabler
     * @return : an interger ; if 1 the connexion has been established
     * @Description : Connect the tablet to the comManager
     */
    public int logInCM(String IPCMAddress, int iPortNum, String IPTablet)
    {
        try
        {
            socketClient    = new Socket(IPCMAddress, iPortNum);
        }
        catch (IOException ex){
            return -1;
        }

        if(socketClient.isConnected()){
            try {

                //Define the network stream from the comManager
                this.in     = new DataInputStream(socketClient.getInputStream());
                this.out    = new DataOutputStream(socketClient.getOutputStream());

                //Create the JSONObject for the identification of the tablet
               JSONObject jsonIdent     = new JSONObject();

               try {
                    jsonIdent.put("From", IPTablet);
                    jsonIdent.put("To", IPCMAddress);
                    jsonIdent.put("MsgType", "Ident");
                    jsonIdent.put("EquipmentType", "Tablet");
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

                //Send the JSONObject in a String format
                this.writeMessageOnFlow(jsonIdent.toString());

            }
            catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return -1;
            }
            return 1;
        }
        else{
            return 0;
        }
    }

    /**
     * @Function : deconnexionCM
     * @Descripton : Disconnect the tablet
     */
    public void logOutCM()
    {
        try{
            this.socketClient.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * @Function : sendOrder
     * @param order : the JSONObject corresponding to the order to send
     * @Description :
     */
    public int sendOrder(JSONObject order)
    {
        try{
            this.writeMessageOnFlow(order.toString());
        }
        catch(Exception e){
            e.printStackTrace();
            return -1;
        }

        return 1;


    }

    public void ask4video(JSONObject order)
    {

    }

    /**
     * @Function : writeMessageOnFlow
     * @param message : message to send to the comManager
     * @Description : Write the message on the network flow in order to send it
     */
    public void writeMessageOnFlow(String message)
    {
        try {
            NetworkFlow.writeMessage(out, message);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
