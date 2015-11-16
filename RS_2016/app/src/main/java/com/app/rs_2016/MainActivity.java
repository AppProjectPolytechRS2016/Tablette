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
import android.widget.ListAdapter;
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
    private TextView            textViewChosenOrder ;

    private TableLayout         rowMoveParam;

    //Intern variables
    private int                 iByte1IP_Value ;
    private int                 iByte2IP_Value ;
    private int                 iByte3IP_Value ;
    private int                 iByte4IP_Value ;

    private String              strIPRobot      = null;
    private String              strIPCM         = null;
    private String              tabletAddress;
    private String              strChosenOrder;

    private int                 iPortCMNum      = 6020;
    private int                 iXValue;
    private int                 iYValue;
    private int                 iThetaValue;

    public boolean              bCheckResult ;

    private ApplicationTablet   appTab;

    private JSONObject          jsonOrder;

    //Define the arrays for the listViews
    private ArrayList<String>   stFeaturesList = new ArrayList<String>();
    private ArrayList<String>   stRobotList = new ArrayList<String>();

    private ArrayAdapter<String> ListAdapterRobot;
    private ArrayAdapter<String> ListAdapterFeature;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /*-------- Recover the IP of the device --------*/
        //To add to enable the connection to the comManager
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        //Recover the IP address of the tablet
        WifiManager wifiMgr         = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo           = wifiMgr.getConnectionInfo();
        int ip                      = wifiInfo.getIpAddress();

        //Convert to IP format
        tabletAddress               = String.format("%d.%d.%d.%d", (ip & 0xff), (ip >> 8 & 0xff), (ip >> 16 & 0xff), (ip >> 24 & 0xff));

        /*-------- Set links for the components --------*/
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
        this.textViewChosenOrder   = (TextView) this.findViewById(R.id.textViewChosenOrder);

        //Set links for the lists
        this.robotList              = (ListView)this.findViewById(R.id.listViewRobot) ;
        this.featuresList           = (ListView)this.findViewById(R.id.listViewFeatures) ;

        rowMoveParam                = (TableLayout)this.findViewById(R.id.moveParam);



        /*-------- Set visibility for the different components --------*/
        //Hide the Move parameters
        rowMoveParam.setVisibility(View.INVISIBLE);

        //Hide the LogOut button
        buttonLogOutCM.setVisibility(View.INVISIBLE);

        //Hide the buttons for the robot
        buttonLogInRobot.setVisibility(View.INVISIBLE);
        buttonSend.setVisibility(View.INVISIBLE);
        buttonLogOutRobot.setVisibility(View.INVISIBLE);
        buttonUpdateRobotList.setVisibility(View.INVISIBLE);

        /**
         * Method that enables the tablet to be connected to the comManager
         */
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

                            //Shapping the IP address
                            strIPCM         = String.format("%d.%d.%d.%d", iByte1IP_Value, iByte2IP_Value, iByte3IP_Value, iByte4IP_Value);

                            //If the IP address is valid
                            if (bCheckResult == true) {
                                ExecutorService execServ    = Executors.newFixedThreadPool(3);
                                appTab                      = new ApplicationTablet("tablet", iPortCMNum, execServ);

                                //Send the JSON to start a communication with the comManager
                                jsonCM                      = appTab.logInCM(strIPCM, iPortCMNum, tabletAddress);

                                Log.i("JSON - LogInCM", jsonCM.toString());

                                //If the connection succeed
                                if (jsonCM.get("MsgType").equals("Order") == true) {
                                    Toast.makeText(MainActivity.this, R.string.toast_ip_Msg1, Toast.LENGTH_LONG).show();

                                    //Change ListView backgrounds
                                    robotList.setBackgroundColor(0xffBA7F78);
                                    featuresList.setBackgroundColor(0xffBA7F78);

                                    //Enable the robot list
                                    robotList.setEnabled(true);

                                    //Hide the LogIn button and display the LogOut button
                                    buttonLogInCM.setVisibility(View.INVISIBLE);
                                    buttonLogOutCM.setVisibility(View.VISIBLE);
                                    buttonLogInRobot.setVisibility(View.VISIBLE);
                                    buttonUpdateRobotList.setVisibility(View.VISIBLE);

                                    if(jsonCM.get("RobotList").equals("null") == false) {

                                        //Set the Robot list
                                        JSONArray jArray            = (JSONArray) jsonCM.get("RobotList");
                                        ArrayList<String> sRobots   = new ArrayList<String>();

                                        int len = jArray.size();

                                        for (int i = 0; i < len; i++) {
                                            String stRobotIp = jArray.get(i).toString();
                                            stRobotList.add(stRobotIp);
                                        }

                                        //Display the list
                                        ListAdapterRobot            = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, stRobotList);
                                        robotList.setAdapter(ListAdapterRobot);
                                    }

                                    //Disable the IP EditTexts
                                    editTextIPByte1CM.setEnabled(false);
                                    editTextIPByte2CM.setEnabled(false);
                                    editTextIPByte3CM.setEnabled(false);
                                    editTextIPByte4CM.setEnabled(false);

                                    bCheckResult                    = false;

                                }
                                else {
                                    Toast.makeText(MainActivity.this, R.string.toast_ip_Msg2, Toast.LENGTH_LONG).show();
                                }
                            }

                            else {
                                Toast.makeText(MainActivity.this, R.string.toast_ip_Msg3, Toast.LENGTH_LONG).show();
                            }
                        }
                        //If the IP address is not valid
                        catch(Exception e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, R.string.toast_ip_Msg3, Toast.LENGTH_LONG).show();
                        }

                    }
                });

        /**
         * Method that enables the user to log out the tablet from the comManager
         */
        //Set the ClickListener for the
        buttonLogOutCM.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Send the IPAddress in order to log out the tablet from the comManager
                        appTab.logOutCM(strIPCM, tabletAddress);

                        //Hide the LogOut button and show the LogIn button
                        buttonLogOutCM.setVisibility(View.INVISIBLE);
                        buttonLogInCM.setVisibility(View.VISIBLE);
                        buttonLogInRobot.setVisibility(View.VISIBLE);
                        buttonUpdateRobotList.setVisibility(View.INVISIBLE);

                        //Display for user
                        Toast.makeText(MainActivity.this, R.string.toast_ip_Msg4, Toast.LENGTH_LONG).show();

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
                        textViewChosenOrder.setText("");
                        textViewIPRobot.setText("");

                    }
                }
        );

        /**
         * Method that ask the selected robot is a connection is possible and if so, start a connection
         */
        //Set the ClicklListener for the LogIn Robot button, in order to send orders to the robot
        buttonLogInRobot.setOnClickListener(
                new OnClickListener() {
                    public void onClick(View v) {
                        try {
                            int iTest = 0;
                            JSONObject jsonReceived                 = new JSONObject();

                            //Re-initialize the bCheckResult variable
                            bCheckResult                            = false;
                            stFeaturesList.clear();

                            //If the robot IP has been filled
                            if(strIPRobot.length() > 0) {

                                JSONObject jsonOrder                = new JSONObject();

                                //Create the JSON object for the log in order
                                jsonOrder.put("From", tabletAddress);
                                jsonOrder.put("To", strIPRobot);
                                jsonOrder.put("MsgType", "Order");
                                jsonOrder.put("OrderName", "ConnectTo");

                                //Send the order to the robot
                                jsonReceived                        = appTab.sendOrder(jsonOrder);

                                Log.i("JSON - LogInRobot", jsonReceived.toString());
                                Log.i("JSON - test", String.valueOf(jsonReceived.get("OrderAccepted").toString().equals("true")));

                                //If the tablet is connected to the robot
                                if ((jsonReceived.get("MsgType").equals("Ack") == true) && (jsonReceived.get("OrderAccepted").toString().equals("true") == true)) {
                                    Toast.makeText(MainActivity.this, R.string.toast_robot_Msg1, Toast.LENGTH_LONG).show();

                                    //Set the visibily of the log in and log out buttons
                                    buttonLogInRobot.setVisibility(View.INVISIBLE);
                                    buttonLogOutRobot.setVisibility(View.VISIBLE);

                                    //Set the features list
                                    JSONArray jArray                = (JSONArray) jsonReceived.get("FeatureList");
                                    ArrayList<String> sFeatures     = new ArrayList<String>();

                                    int len = jArray.size();

                                    for (int i = 0; i < len; i++) {
                                        String stFeatureName = jArray.get(i).toString();

                                        //This order is only for the Kinect devices, so it is not displayed in the tablet's features list
                                        if(stFeatureName.equals("Mime") == false) {
                                            stFeaturesList.add(stFeatureName);
                                        }
                                    }

                                    //Display the list
                                    ListAdapterFeature             = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, stFeaturesList);
                                    featuresList.setAdapter(ListAdapterFeature);

                                    //Disable the robot list
                                    robotList.setEnabled(false);

                                    buttonSend.setVisibility(View.VISIBLE);
                                }
                                else {
                                    Toast.makeText(MainActivity.this, R.string.toast_robot_Msg2, Toast.LENGTH_LONG).show();
                                }
                            }
                            else{
                                Toast.makeText(MainActivity.this, R.string.toast_robot_Msg3, Toast.LENGTH_LONG).show();
                            }
                        }

                        //If the IP address is not valid
                        catch(Exception e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, R.string.toast_robot_Msg4, Toast.LENGTH_LONG).show();
                        }
                    }
                });

        /**
         * Method that enable the user to stop the connection between the tablet and a robot
         */
        //Set the ClickListener for the LogOut button for the robot
        buttonLogOutRobot.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Clear the features list
                        stFeaturesList.clear();
                        featuresList.setAdapter(ListAdapterFeature);

                        //Hide the move parameters
                        rowMoveParam.setVisibility(View.INVISIBLE);
                        buttonSend.setVisibility(View.INVISIBLE);

                        //Create the JSON object
                        JSONObject jsonOrder        = new JSONObject();

                         jsonOrder.put("From", tabletAddress);
                         jsonOrder.put("To", strIPRobot);
                         jsonOrder.put("MsgType", "Order");
                         jsonOrder.put("OrderName", "Disconnect");


                        JSONObject jsonReceived     = new JSONObject();

                        //Send the log out order to the robot
                        jsonReceived                = appTab.sendOrder(jsonOrder);

                        //Display for debug
                        Log.i("JSON - LogOutRobot", jsonReceived.toString());

                        //Enable the robot list
                        robotList.setEnabled(true);

                        //If the tablet has been logged out from the robot
                        if(jsonReceived.get("Disconnected").toString().equals("true") == true) {
                            Toast.makeText(MainActivity.this, R.string.toast_robot_Msg5, Toast.LENGTH_LONG).show();

                            //Set visibility for the buttons
                            buttonLogInRobot.setVisibility(View.VISIBLE);
                            buttonLogOutRobot.setVisibility(View.INVISIBLE);
                        }

                    }
                }
        );

        /**
         * Method that recover the selected order and send it to the selected robot
         */
        //Set the ClickListener for the Send button
        buttonSend.setOnClickListener(
                new OnClickListener() {
                    @Override
                     public void onClick(View v) {
                        //If an order has been selected
                        if(strChosenOrder.length() > 0) {

                            JSONObject jsonOrder    = new JSONObject();

                            //Create the JSON object for the order
                            jsonOrder.put("From", tabletAddress);
                            jsonOrder.put("To", strIPRobot);
                            jsonOrder.put("MsgType", "Order");
                            jsonOrder.put("OrderName", strChosenOrder);

                            //If the selected order is "Move"
                            if (strChosenOrder.equals("Move")) {

                                try {
                                    //Recover all move parameters
                                    String strXVal = editTextXVal.getText().toString();
                                    String strYVal = editTextYVal.getText().toString();
                                    String strThetaVal = editTextAngleVal.getText().toString();

                                    iXValue             = CheckUserChoice.checkIntParam(strXVal);
                                    iYValue             = CheckUserChoice.checkIntParam(strYVal);
                                    iThetaValue         = CheckUserChoice.checkIntParam(strThetaVal);

                                }

                                catch (Exception e){
                                    e.printStackTrace();
                                }


                                //Add specific fields to the JSON object
                                jsonOrder.put("XValue", iXValue);
                                jsonOrder.put("YValue", iYValue);
                                jsonOrder.put("ThetaValue", iThetaValue);
                            }

                            JSONObject jsonReceived = new JSONObject();

                            //Send the order to the robot
                            jsonReceived = appTab.sendOrder(jsonOrder);

                            Log.i("JSON - SendOrder", jsonReceived.toString());

                            //If the order has been accepted by the robot
                            if (jsonReceived.get("OrderAccepted").toString().equals("true") == true) {
                                //Set visibility for the different components
                                buttonLogInRobot.setVisibility(View.VISIBLE);
                                buttonLogOutRobot.setVisibility(View.INVISIBLE);
                                rowMoveParam.setVisibility(View.INVISIBLE);
                                buttonSend.setVisibility(View.INVISIBLE);

                                //Erase the edtiText and the feature list
                                editTextAngleVal.setText("0");
                                editTextXVal.setText("0");
                                editTextYVal.setText("0");
                                textViewChosenOrder.setText("");

                                //Clear the features list
                                stFeaturesList.clear();
                                featuresList.setAdapter(ListAdapterFeature);

                                //Enable the robot list
                                robotList.setEnabled(true);

                                Toast.makeText(MainActivity.this, R.string.toast_order_Msg1, Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(MainActivity.this, R.string.toast_order_Msg2, Toast.LENGTH_LONG).show();
                                textViewChosenOrder.setText("");
                            }
                        }
                        else{
                            Toast.makeText(MainActivity.this, R.string.toast_order_Msg3, Toast.LENGTH_LONG).show();
                        }
                     }
                }
        );
;

        /**
         * Method that recover the selected robot
         */
        //Set the listener for the robot list
        robotList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //Recover the selected item
                        strIPRobot    = (String) robotList.getItemAtPosition(position);

                        //Display the selected order in the corresponding editText
                        textViewIPRobot.setText(strIPRobot);
                    }
                }
        );

        /**
         * Method that recover the selected feature
         */
        //Set the listener for the features list
        featuresList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //Recover the selected item
                        strChosenOrder    = (String) featuresList.getItemAtPosition(position);

                        //Display the selected item in the corresponding editText
                        textViewChosenOrder.setText(strChosenOrder);

                        //If the selected order is "Move", display the parameters' editText
                        if(strChosenOrder.equals("Move")){
                            rowMoveParam.setVisibility(View.VISIBLE);
                        }

                        else{
                            rowMoveParam.setVisibility(View.INVISIBLE);
                        }

                    }
                }
        );

        /**
         * Method that enables the user to update the robot list
         */
        //Set the ClickListener for the Update List button
        buttonUpdateRobotList.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Clear the robot list
                        stRobotList.clear();
                        robotList.setAdapter(ListAdapterRobot);

                        //Create the JSON object
                        jsonOrder                       = new JSONObject();

                        jsonOrder.put("From", tabletAddress);
                        jsonOrder.put("To", strIPCM);
                        jsonOrder.put("MsgType", "UpdateList");

                        //Send the JSON object to the comManager
                        jsonOrder                       = appTab.sendOrder(jsonOrder);

                        Log.i("JSON - UpdateList", jsonOrder.toString());

                        //If the receive list is not empty
                        if(jsonOrder.get("RobotList").equals("null") == false) {
                            //Set the Robot list
                            JSONArray jArray            = (JSONArray) jsonOrder.get("RobotList");
                            ArrayList<String> sRobots   = new ArrayList<String>();

                            int len = jArray.size();

                            for (int i = 0; i < len; i++) {
                                String stRobotIp = jArray.get(i).toString();
                                stRobotList.add(stRobotIp);
                            }

                            //Display the list
                            ListAdapterRobot            = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, stRobotList);
                            robotList.setAdapter(ListAdapterRobot);
                        }
                        else{
                            Toast.makeText(MainActivity.this, R.string.toast_robot_Msg6, Toast.LENGTH_LONG).show();
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
