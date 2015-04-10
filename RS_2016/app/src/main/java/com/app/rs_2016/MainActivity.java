package com.app.rs_2016;

//Import of the libraries
import android.content.Context;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem ;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TextView;
import android.view.View;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiInfo;
import android.widget.Toast;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainActivity extends ActionBarActivity {
    //Declaration of the different elements
    private Button              buttonLogInCM ;
    private Button              buttonLogOutCM ;
    private Button              buttonSend ;
    private Button              buttonLogInRobot;
    private Button              buttonLogOutRobot;
    private Button              buttonUpdateRobotList;

    private EditText            editTextIPByte1CM ;
    private EditText            editTextIPByte2CM ;
    private EditText            editTextIPByte3CM ;
    private EditText            editTextIPByte4CM ;
    private EditText            editTextXVal ;
    private EditText            editTextYVal ;
    private EditText            editTextAngleVal ;

    private ListView            robotList ;
    private ListView            featuresList ;

    private TextView            textViewIPRobot ;
    private TextView            textViewChoosenOrder ;

    private TableLayout         rowMoveParam;

    //Intern variables
    private int                 iByte1IP_Value ;
    private int                 iByte2IP_Value ;
    private int                 iByte3IP_Value ;
    private int                 iByte4IP_Value ;

    private String              strIPRobot      = null;
    private String              strIPCM         = null;
    private String              tabletAddress;
    private String              strChoosenOrder;

    private int                 iPortCMNum      = 6020;
    private int                 iXValue;
    private int                 iYValue;
    private int                 iThetaValue;

    public boolean              bCheckResult ;

    private ApplicationTablet   appTab;

    private JSONObject          jsonOrder;

    private ArrayList<String>   stFeaturesList = new ArrayList<String>();
    private ArrayList<String>   stRobotList = new ArrayList<String>();

    private ArrayAdapter<String> ListAdapterRobot;
    private ArrayAdapter<String> ListAdapterFeature;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //To add to enable the connection to the comManager
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        //Set links for the buttons
        this.buttonLogInCM          = (Button)this.findViewById(R.id.buttonLogInCM) ;
        this.buttonLogOutCM         = (Button)this.findViewById(R.id.buttonLogOutCM) ;
        this.buttonSend             = (Button)this.findViewById(R.id.buttonSend);
        this.buttonLogInRobot       = (Button)this.findViewById(R.id.buttonLogInRobot);
        this.buttonLogOutRobot      = (Button)this.findViewById(R.id.buttonLogOutRobot);
        this.buttonUpdateRobotList  = (Button)this.findViewById(R.id.buttonUpdateRobotList);

        //Set links for the edit texts
        this.editTextAngleVal       = (EditText)this.findViewById(R.id.editTextThetaVal) ;
        this.editTextXVal           = (EditText)this.findViewById(R.id.editTextXVal) ;
        this.editTextYVal           = (EditText)this.findViewById(R.id.editTextYVal) ;

        this.editTextIPByte1CM      = (EditText)this.findViewById(R.id.ipCMByte1) ;
        this.editTextIPByte2CM      = (EditText)this.findViewById(R.id.ipCMByte2) ;
        this.editTextIPByte3CM      = (EditText)this.findViewById(R.id.ipCMByte3) ;
        this.editTextIPByte4CM      = (EditText)this.findViewById(R.id.ipCMByte4) ;

        this.textViewIPRobot        = (TextView)this.findViewById(R.id.textViewIPRobot) ;
        this.textViewChoosenOrder   = (TextView) this.findViewById(R.id.textViewChoosenOrder);

        //Set links for the lists
        this.robotList              = (ListView)this.findViewById(R.id.listViewRobot) ;
        this.featuresList           = (ListView)this.findViewById(R.id.listViewFeatures) ;

        rowMoveParam                = (TableLayout)this.findViewById(R.id.moveParam);

        rowMoveParam.setVisibility(View.INVISIBLE);

        //Hide the LogOut button
        buttonLogOutCM.setVisibility(View.INVISIBLE);

        //Recover the IP address of the tablet
        WifiManager wifiMgr         = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo           = wifiMgr.getConnectionInfo();
        int ip                      = wifiInfo.getIpAddress();

        //Hide the buttons for the robot
        buttonLogInRobot.setVisibility(View.INVISIBLE);
        buttonSend.setVisibility(View.INVISIBLE);
        buttonLogOutRobot.setVisibility(View.INVISIBLE);

        //Convert to IP format
        tabletAddress               = String.format("%d.%d.%d.%d", (ip & 0xff), (ip >> 8 & 0xff), (ip >> 16 & 0xff), (ip >> 24 & 0xff));

        //Set the click listeners for the LogIn button
        buttonLogInCM.setOnClickListener(
                new OnClickListener() {
                    public void onClick(View v) {
                        try {
                            JSONObject jsonCM   = new JSONObject();

                            //Recover each byte value of the IP
                            iByte1IP_Value  = Integer.parseInt(editTextIPByte1CM.getText().toString());
                            iByte2IP_Value  = Integer.parseInt(editTextIPByte2CM.getText().toString());
                            iByte3IP_Value  = Integer.parseInt(editTextIPByte3CM.getText().toString());
                            iByte4IP_Value  = Integer.parseInt(editTextIPByte4CM.getText().toString());

                            //Check if the IP address is valid
                            bCheckResult    = CheckUserChoice.checkIP(iByte1IP_Value, iByte2IP_Value, iByte3IP_Value, iByte4IP_Value);

                            //Display for debug
                            strIPCM         = String.format("%d.%d.%d.%d", iByte1IP_Value, iByte2IP_Value, iByte3IP_Value, iByte4IP_Value);

                            //If the IP address is valid
                            if (bCheckResult == true) {
                                ExecutorService execServ    = Executors.newFixedThreadPool(3);
                                appTab                      = new ApplicationTablet("tablet", iPortCMNum, execServ);
                                jsonCM                      = appTab.logInCM(strIPCM, iPortCMNum, tabletAddress);

                                //If the connection succeed
                                if (jsonCM.get("MsgType").equals("Order") == true) {
                                    Toast.makeText(MainActivity.this, "La tablette est maintenant connectée au gestionnaire de communication.", Toast.LENGTH_LONG).show();

                                    //Hide the LogIn button and display the LogOut button
                                    buttonLogInCM.setVisibility(View.INVISIBLE);
                                    buttonLogOutCM.setVisibility(View.VISIBLE);
                                    buttonLogInRobot.setVisibility(View.VISIBLE);
                                    buttonSend.setVisibility(View.VISIBLE);

                                    if(jsonCM.get("RobotList").equals("null") == false) {

                                        //Set the Robot list
                                        JSONArray jArray = (JSONArray) jsonCM.get("RobotList");
                                        ArrayList<String> sRobots = new ArrayList<String>();

                                        int len = jArray.size();

                                        for (int i = 0; i < len; i++) {
                                            String stRobotIp = jArray.get(i).toString();
                                            stRobotList.add(stRobotIp);
                                        }

                                        //Display the list
                                        ListAdapterRobot = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, stRobotList);
                                        robotList.setAdapter(ListAdapterRobot);
                                    }

                                    //Disable the IP EditTexts
                                    editTextIPByte1CM.setEnabled(false);
                                    editTextIPByte2CM.setEnabled(false);
                                    editTextIPByte3CM.setEnabled(false);
                                    editTextIPByte4CM.setEnabled(false);

                                    bCheckResult    = false;

                                }
                                else {
                                    Toast.makeText(MainActivity.this, "La tablette n'a pas pu se connecter au gestionnaire de communication.", Toast.LENGTH_LONG).show();
                                }
                            }

                            else {
                                Toast.makeText(MainActivity.this, "L'adresse IP renseignée est invalide.", Toast.LENGTH_LONG).show();
                            }
                        }
                        //If the IP address is not valid
                        catch(Exception e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "L'adresse IP du gestionnaire de communication contient des champs vides.", Toast.LENGTH_LONG).show();
                        }

                    }
                });

        //Set the ClickListener for the
        buttonLogOutCM.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        appTab.logOutCM(strIPCM, tabletAddress);

                        //Hide the LogOut button and show the LogIn button
                        buttonLogOutCM.setVisibility(View.INVISIBLE);
                        buttonLogInCM.setVisibility(View.VISIBLE);
                        buttonLogInRobot.setVisibility(View.VISIBLE);
                        Toast.makeText(MainActivity.this, "La tablette s'est déconnectée du serveur.", Toast.LENGTH_LONG).show();

                        //Clear robot and feature list
                        stFeaturesList.clear();
                        featuresList.setAdapter(ListAdapterFeature);

                        stRobotList.clear();
                        robotList.setAdapter(ListAdapterRobot);

                        //Enable the IPCM EditTexts
                        editTextIPByte1CM.setEnabled(true);
                        editTextIPByte2CM.setEnabled(true);
                        editTextIPByte3CM.setEnabled(true);
                        editTextIPByte4CM.setEnabled(true);

                        //Hide the robot's parameters
                        rowMoveParam.setVisibility(View.INVISIBLE);
                        buttonSend.setVisibility(View.INVISIBLE);

                    }
                }
        );

        //Set the ClicklListener for the LogIn Robot button, in order to send orders to the robot
        buttonLogInRobot.setOnClickListener(
                new OnClickListener() {
                    public void onClick(View v) {
                        try {
                            int iTest = 0;
                            JSONObject jsonReceived     = new JSONObject();

                            //Re-initialize the bCheckResult variable
                            bCheckResult = false;
                            stFeaturesList.clear();
                            stRobotList.clear();

                                JSONObject jsonOrder = new JSONObject();

                                jsonOrder.put("From", tabletAddress);
                                jsonOrder.put("To", strIPRobot);
                                jsonOrder.put("MsgType", "Order");
                                jsonOrder.put("OrderName", "ConnectTo");

                                jsonReceived = appTab.sendOrder(jsonOrder);

                                //If the tablet is connected to the robot
                                if(jsonReceived.get("MsgType").equals("Ack") == true){
                                        Toast.makeText(MainActivity.this, "La tablette est maintenant connectée au robot.", Toast.LENGTH_LONG).show();

                                        //Set the visibily of the log in and log out buttons
                                        buttonLogInRobot.setVisibility(View.INVISIBLE);
                                        buttonLogOutRobot.setVisibility(View.VISIBLE);

                                        //Set the Robot list
                                        JSONArray jArray = (JSONArray)jsonReceived.get("FeatureList");
                                        ArrayList<String> sFeatures = new ArrayList<String>();

                                        int len         = jArray.size();

                                        for (int i=0;i<len;i++){
                                            String stFeatureName    = jArray.get(i).toString();
                                            stFeaturesList.add(stFeatureName);
                                        }

                                        //Display the list
                                        ListAdapterFeature = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, stFeaturesList);
                                        featuresList.setAdapter(ListAdapterFeature);

                                        buttonSend.setVisibility(View.VISIBLE);
                                    }

                                else{
                                        Toast.makeText(MainActivity.this, "La tablette n'a pas pu se connecter au robot.", Toast.LENGTH_LONG).show();
                                    }
                        }

                        //If the IP address is not valid
                        catch(Exception e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Connexion impossible. Merci de vérifier l'adresse IP du robot.", Toast.LENGTH_LONG).show();
                        }
                    }
                });

        //Set the ClickListener for the LogOut button for the robot
        buttonLogOutRobot.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Show the LogIn button

                        //Clear the features list
                        stFeaturesList.clear();
                        featuresList.setAdapter(ListAdapterFeature);

                        //Hide the move parameters
                        rowMoveParam.setVisibility(View.INVISIBLE);
                        buttonSend.setVisibility(View.INVISIBLE);

                        //Send a message to the robot
                        JSONObject jsonOrder    = new JSONObject();

                         jsonOrder.put("From", tabletAddress);
                         jsonOrder.put("To", strIPRobot);
                         jsonOrder.put("MsgType", "Order");
                         jsonOrder.put("OrderName", "Disconnect");

                        JSONObject jsonReceived     = new JSONObject();
                        jsonReceived                = appTab.sendOrder(jsonOrder);
                        Log.d("Debug - JSON received", jsonReceived.toString());

                    }
                }
        );

        //Set the ClickListener for the Send button
        buttonSend.setOnClickListener(
                new OnClickListener() {
                    @Override
                     public void onClick(View v) {

                        JSONObject jsonOrder = new JSONObject();

                        jsonOrder.put("From", tabletAddress);
                        jsonOrder.put("To", strIPRobot);
                        jsonOrder.put("MsgType", "Order");
                        jsonOrder.put("OrderName", strChoosenOrder);

                        if(strChoosenOrder.equals("Move")){

                            Toast.makeText(MainActivity.this, "Move selected", Toast.LENGTH_LONG).show();
                            //Recover all move parameters
                            String strXVal      = editTextXVal.getText().toString();
                            String strYVal      = editTextYVal.getText().toString();
                            String strThetaVal  = editTextAngleVal.getText().toString();

                            iXValue             = CheckUserChoice.checkIntParam(strXVal);
                            iYValue             = CheckUserChoice.checkIntParam(strYVal);
                            iThetaValue         = CheckUserChoice.checkIntParam(strThetaVal);

                            jsonOrder.put("From", tabletAddress);
                            jsonOrder.put("To", strIPRobot);
                            jsonOrder.put("MsgType", "Order");
                            jsonOrder.put("OrderName", strChoosenOrder);
                            jsonOrder.put("XValue", iXValue);
                            jsonOrder.put("YValue", iYValue);
                            jsonOrder.put("ThetaValue", iThetaValue);
                        }

                        JSONObject jsonReceived        = new JSONObject();
                        jsonReceived = appTab.sendOrder(jsonOrder);

                        if(jsonReceived.get("OrderAccepted") == true) {
                            buttonLogInRobot.setVisibility(View.VISIBLE);
                            buttonLogOutRobot.setVisibility(View.INVISIBLE);
                            Toast.makeText(MainActivity.this, "Ordre accepté par le robot.", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(MainActivity.this, "L'ordre n'a pas pu être accepté par le robot.", Toast.LENGTH_LONG).show();
                        }
                     }
                }
        );
;

        robotList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        strIPRobot    = (String) robotList.getItemAtPosition(position);
                        textViewIPRobot.setText(strIPRobot);
                    }
                }
        );

        featuresList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        strChoosenOrder    = (String) featuresList.getItemAtPosition(position);

                        textViewChoosenOrder.setText(strChoosenOrder);

                        //If the selected order is "Move"
                        if(strChoosenOrder.equals("Move")){
                            rowMoveParam.setVisibility(View.VISIBLE);
                        }

                        else{
                            rowMoveParam.setVisibility(View.INVISIBLE);
                        }

                    }
                }
        );

        buttonUpdateRobotList.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        stRobotList.clear();
                        //robotList.setAdapter(ListAdapterRobot);


                        jsonOrder   = new JSONObject();

                        jsonOrder.put("From", tabletAddress);
                        jsonOrder.put("To", strIPCM);
                        jsonOrder.put("MsgType", "UpdateList");

                        jsonOrder           = appTab.sendOrder(jsonOrder);

                        if(jsonOrder.get("RobotList").equals("null") == false) {
                            //Set the Robot list
                            JSONArray jArray = (JSONArray) jsonOrder.get("RobotList");
                            ArrayList<String> sRobots = new ArrayList<String>();

                            int len = jArray.size();

                            for (int i = 0; i < len; i++) {
                                String stRobotIp = jArray.get(i).toString();
                                stRobotList.add(stRobotIp);
                            }

                            //Display the list
                            ListAdapterRobot = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, stRobotList);
                            robotList.setAdapter(ListAdapterRobot);
                        }
                        else{
                            Toast.makeText(MainActivity.this, "Il n'y a pas de robots connectés.", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
