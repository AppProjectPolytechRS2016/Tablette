package com.app.rs_2016;

//Import libraries
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;


import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


/**
 * Created by Marion on 14/03/2015.
 */
public class ApplicationTablet {

    //Declarations
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
     * @Function : logInCM
     * @param IPCMAddress : IP address of the comManager
     * @param iPortNum : port number used for the communication with the comManager
     * @param IPTablet : IP address of the tabler
     * @return : an interger ; if 1 the connexion has been established
     * @Description : Connect the tablet to the comManager
     */
    public JSONObject logInCM(String IPCMAddress, int iPortNum, String IPTablet)
    {
        //Declarations
        JSONObject jsonRes      = new JSONObject();

        try
        {
            //Create a new socket
            socketClient    = new Socket(IPCMAddress, iPortNum);
        }
        catch (IOException ex){
                ex.printStackTrace();
        }

        if(socketClient.isConnected()){
            try {

                //Define the network stream from the comManager
                this.in         = new DataInputStream(socketClient.getInputStream());
                this.out        = new DataOutputStream(socketClient.getOutputStream());

                //Create the JSONObject for the identification of the tablet
               JSONObject jsonIdent     = new JSONObject();

               jsonIdent.put("From", IPTablet);
               jsonIdent.put("To", IPCMAddress);
               jsonIdent.put("MsgType", "Ident");
               jsonIdent.put("EquipmentType", "Tablet");

                //Send the JSONObject in a String format
                this.writeMessageOnFlow(jsonIdent.toString() + "\r\n");

                //Processing for the received message
                jsonRes         = this.treatReceivedMsg();
                jsonRes.put("Stated", "Connected");

            }
            catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        else{
             jsonRes.put("State", "NotConnected");
        }

        return jsonRes;
    }

    /**
     * @Function : logoutCM
     * @param IPCM : IP address of the comManager
     * @param IPTablet : IP address of the tablet
     * @Descripton : Disconnect the tablet from the comManager
     */
    public void logOutCM(String IPCM, String IPTablet)
    {
        try{
            JSONObject jsonOrder    = new JSONObject();

            //Create the JSON object
            jsonOrder.put("From", IPTablet);
            jsonOrder.put("To", IPCM);
            jsonOrder.put("MsgType", "Logout");

            //Write the message on the flow
            this.writeMessageOnFlow(jsonOrder.toString() + "\r\n");

            this.socketClient.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * @Function : sendOrder
     * @param order : the JSONObject corresponding to the order to send
     * @Description : this method enables to send message to another equipment
     */
    public JSONObject sendOrder(JSONObject order)
    {
        //Declarations
        JSONObject jsonReceived     = new JSONObject();

        try {
            //Display for debug
            Log.d("Debug - JSON received", jsonReceived.toString());

            //Write message on flow
            this.writeMessageOnFlow(order.toString() + "\r\n");

            //Processing for the received message
            jsonReceived    = this.treatReceivedMsg();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return jsonReceived;
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
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * @Function : treatReceivedMsg
     * @Description : wait for a string on flow, then convert it to a JSONObject
     * @return
     */
    public JSONObject treatReceivedMsg(){
        //Declarations
        String strReceived      = null;
        JSONObject jsonReceived = new JSONObject();
        JSONParser parser       = new JSONParser();

        //Wait until a String appears on the flow
        do {
            try {
                //Recover the string on the flow
                strReceived     = NetworkFlow.readMessage(in);

                //Display for debug
                Log.d("Debug - String received", strReceived.toString());
            }
            catch (IOException e) {
                e.printStackTrace();
            }

        }while (strReceived.length() == 0);


        Object obj = null;
        try {
            //Parse the String into an object
            obj = parser.parse(strReceived);
        }

        catch (ParseException e) {
            e.printStackTrace();
        }

        //Convert the object into a JSON object
        jsonReceived = (JSONObject) obj;
        
        return jsonReceived;
    }
}
