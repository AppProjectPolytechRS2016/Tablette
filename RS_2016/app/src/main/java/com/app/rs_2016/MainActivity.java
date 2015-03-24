package com.app.rs_2016;

//Import of the libraries
import android.content.Context;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem ;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.view.View;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiInfo;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainActivity extends ActionBarActivity {
    //Declaration of the different elements
    private Button              buttonLogInCM ;
    private Button              buttonLogOutCM ;
    private Button              buttonReccord ;
    private Button              buttonWalk ;
    private Button              buttonMove ;
    private Button              buttonSend ;
    private Button              buttonLogInRobot;
    private Button              buttonLogOutRobot;

    private EditText            editTextIPByte1CM ;
    private EditText            editTextIPByte2CM ;
    private EditText            editTextIPByte3CM ;
    private EditText            editTextIPByte4CM ;
    private EditText            editTextIPByte1Robot ;
    private EditText            editTextIPByte2Robot ;
    private EditText            editTextIPByte3Robot ;
    private EditText            editTextIPByte4Robot ;
    private EditText            editTextXVal ;
    private EditText            editTextYVal ;
    private EditText            editTextAngleVal ;

    private ListView            robotList ;
    private ListView            featuresList ;

    private TextView            debugTextIPCM ;
    private TextView            debugTextIPRobot;
    private TextView            debugTextIPTablet;
    private TextView            debugParamMove;


    //Intern variables
    private int                 iByte1IP_Value ;
    private int                 iByte2IP_Value ;
    private int                 iByte3IP_Value ;
    private int                 iByte4IP_Value ;

    private int                 iByteRobotIP1_Value ;
    private int                 iByteRobotIP2_Value ;
    private int                 iByteRobotIP3_Value ;
    private int                 iByteRobotIP4_Value ;

    private String              strIPRobot      = null;
    private String              strIPCM         = null;
    private String              tabletAddress;

    private int                 iPortCMNum      = 6020;
    private int                 iXValue;
    private int                 iYValue;
    private int                 iThetaValue;

    public boolean              bCheckResult ;

    private ApplicationTablet   appTab;

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
        this.buttonMove             = (Button)this.findViewById(R.id.buttonMove) ;
        this.buttonReccord          = (Button)this.findViewById(R.id.buttonReccord) ;
        this.buttonSend             = (Button)this.findViewById(R.id.buttonSend);
        this.buttonWalk             = (Button)this.findViewById(R.id.buttonWalk) ;
        this.buttonLogInRobot       = (Button)this.findViewById(R.id.buttonLogInRobot);
        this.buttonLogOutRobot      = (Button)this.findViewById(R.id.buttonLogOutRobot);

        //Set links for the edit texts
        this.editTextAngleVal       = (EditText)this.findViewById(R.id.editTextThetaVal) ;
        this.editTextXVal           = (EditText)this.findViewById(R.id.editTextXVal) ;
        this.editTextYVal           = (EditText)this.findViewById(R.id.editTextYVal) ;

        this.editTextIPByte1CM      = (EditText)this.findViewById(R.id.ipCMByte1) ;
        this.editTextIPByte2CM      = (EditText)this.findViewById(R.id.ipCMByte2) ;
        this.editTextIPByte3CM      = (EditText)this.findViewById(R.id.ipCMByte3) ;
        this.editTextIPByte4CM      = (EditText)this.findViewById(R.id.ipCMByte4) ;

        this.editTextIPByte1Robot   = (EditText)this.findViewById(R.id.editTextIPRobot1) ;
        this.editTextIPByte2Robot   = (EditText)this.findViewById(R.id.editTextIPRobot2) ;
        this.editTextIPByte3Robot   = (EditText)this.findViewById(R.id.editTextIPRobot3) ;
        this.editTextIPByte4Robot   = (EditText)this.findViewById(R.id.editTextIPRobot4) ;

        //Set links for the debug text views
        this.debugTextIPCM          = (TextView)this.findViewById(R.id.debugIPCM);
        this.debugTextIPRobot       = (TextView)this.findViewById(R.id.debugIPRobot);
        this.debugTextIPTablet      = (TextView)this.findViewById(R.id.debugIPTablet);
        this.debugParamMove         = (TextView)this.findViewById(R.id.debugMoveParam);


        //Set links for the lists
        this.robotList              = (ListView)this.findViewById(R.id.listViewRobot) ;
        this.featuresList           = (ListView)this.findViewById(R.id.listViewFeatures) ;

        //Hide the LogOut buttons
        buttonLogOutCM.setVisibility(View.INVISIBLE);
        buttonLogOutRobot.setVisibility(View.INVISIBLE);

        //Recover the IP address of the tablet
        WifiManager wifiMgr         = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo           = wifiMgr.getConnectionInfo();
        int ip                      = wifiInfo.getIpAddress();

        //Convert to IP format
        tabletAddress               = String.format("%d.%d.%d.%d", (ip & 0xff), (ip >> 8 & 0xff), (ip >> 16 & 0xff), (ip >> 24 & 0xff));

       debugTextIPTablet.setText("IP Tablet : " + tabletAddress);

        //Set the click listeners for the LogIn button
        buttonLogInCM.setOnClickListener(
                new OnClickListener() {
                    public void onClick(View v) {
                        try {
                            int iTest       = 0;

                            //Recover each byte value of the IP
                            iByte1IP_Value  = Integer.parseInt(editTextIPByte1CM.getText().toString());
                            iByte2IP_Value  = Integer.parseInt(editTextIPByte2CM.getText().toString());
                            iByte3IP_Value  = Integer.parseInt(editTextIPByte3CM.getText().toString());
                            iByte4IP_Value  = Integer.parseInt(editTextIPByte4CM.getText().toString());

                            //Check if the IP address is valid
                            bCheckResult    = CheckUserChoice.checkIP(iByte1IP_Value, iByte2IP_Value, iByte3IP_Value, iByte4IP_Value);

                            //Display for debug
                            strIPCM         = String.format("%d.%d.%d.%d", iByte1IP_Value, iByte2IP_Value, iByte3IP_Value, iByte4IP_Value);
                            debugTextIPCM.setText("IP CM : " + strIPCM + " Check Res = " + bCheckResult);

                            //If the IP address is valid
                            if (bCheckResult == true) {
                                ExecutorService execServ = Executors.newFixedThreadPool(3);
                                appTab      = new ApplicationTablet("tablet", iPortCMNum, execServ);
                                iTest   = appTab.logInCM(strIPCM, iPortCMNum, tabletAddress);

                                //If the connection succeed
                                if (iTest == 1) {
                                    Toast.makeText(MainActivity.this, "La tablette est maintenant connectée au gestionnaire de communication.", Toast.LENGTH_LONG).show();

                                    //Hide the LogIn button and display the LogOut button
                                    buttonLogInCM.setVisibility(View.INVISIBLE);
                                    buttonLogOutCM.setVisibility(View.VISIBLE);

                                    //Disable the IP EditTexts
                                    editTextIPByte1CM.setEnabled(false);
                                    editTextIPByte2CM.setEnabled(false);
                                    editTextIPByte3CM.setEnabled(false);
                                    editTextIPByte4CM.setEnabled(false);
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
                        appTab.logOutCM();

                        //Hide the LogOut button and show the LogIn button
                        buttonLogOutCM.setVisibility(View.INVISIBLE);
                        buttonLogInCM.setVisibility(View.VISIBLE);
                        Toast.makeText(MainActivity.this, "La tablette s'est déconnectée du serveur.", Toast.LENGTH_LONG).show();

                        //Enable the IP EditTexts
                        editTextIPByte1CM.setEnabled(true);
                        editTextIPByte2CM.setEnabled(true);
                        editTextIPByte3CM.setEnabled(true);
                        editTextIPByte4CM.setEnabled(true);

                    }
                }
        );


        //Set the ClicklListener for the LogIn Robot button, in order to send orders to the robot
        buttonLogInRobot.setOnClickListener(
                new OnClickListener() {
                    public void onClick(View v) {

                        try {
                            int iTest               = 0;

                            //Re-initialize the bCheckResult variable
                            bCheckResult            = false;

                            //Recover each byte value of the IP
                            iByteRobotIP1_Value     = Integer.parseInt(editTextIPByte1Robot.getText().toString());
                            iByteRobotIP2_Value     = Integer.parseInt(editTextIPByte2Robot.getText().toString());
                            iByteRobotIP3_Value     = Integer.parseInt(editTextIPByte3Robot.getText().toString());
                            iByteRobotIP4_Value     = Integer.parseInt(editTextIPByte4Robot.getText().toString());

                            // Check if the IP address is valid
                            bCheckResult            = CheckUserChoice.checkIP(iByte1IP_Value, iByte2IP_Value, iByte3IP_Value, iByte4IP_Value);

                            //Display for debug
                            strIPRobot              = iByteRobotIP1_Value + "." + iByteRobotIP2_Value + "." + iByteRobotIP3_Value +"." +iByteRobotIP4_Value;
                            debugTextIPRobot.setText("IP Robot : " + strIPRobot + " Check Res = " + bCheckResult);

                            //If the IP address is valid
                            if(bCheckResult == true){
                                JSONObject jsonOrder    = new JSONObject();
                                jsonOrder.put("From", strIPCM);
                                jsonOrder.put("To", strIPRobot);
                                jsonOrder.put("MsgType", "Order");
                                jsonOrder.put("OrderName", "ConnectTo");

                                iTest =     appTab.sendOrder(jsonOrder);

                                //If the tablet is connected to the robot
                                if(iTest == 1){
                                    Toast.makeText(MainActivity.this, "La tablette est maintenant connectée au robot.", Toast.LENGTH_LONG).show();

                                    //Set the visibily of the log in and log out buttons
                                    buttonLogInRobot.setVisibility(View.INVISIBLE);
                                    buttonLogOutRobot.setVisibility(View.VISIBLE);

                                    //Disable the IP editText
                                    editTextIPByte1Robot.setEnabled(false);
                                    editTextIPByte2Robot.setEnabled(false);
                                    editTextIPByte3Robot.setEnabled(false);
                                    editTextIPByte4Robot.setEnabled(false);
                                }
                            }
                            else {
                                Toast.makeText(MainActivity.this, "L'adresse IP renseignée est invalide.", Toast.LENGTH_LONG).show();
                            }
                        }
                        //If the IP address is not valid
                        catch(Exception e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "L'adresse IP du robot contient des champs vides.", Toast.LENGTH_LONG).show();
                        }

                    }
                });

        //Set the ClickListener for the LogOut button for the robot
        buttonLogOutRobot.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Show the LogIn button
                        buttonLogInRobot.setVisibility(View.VISIBLE);

                        //Disable the IP editText
                        editTextIPByte1Robot.setEnabled(true);
                        editTextIPByte2Robot.setEnabled(true);
                        editTextIPByte3Robot.setEnabled(true);
                        editTextIPByte4Robot.setEnabled(true);
                    }
                }
        );

        //Set the ClickListener for the Send button
        buttonSend.setOnClickListener(
                new OnClickListener() {
                    @Override
                     public void onClick(View v) {
                        String strXVal      = editTextXVal.getText().toString();
                        String strYVal      = editTextYVal.getText().toString();
                        String strThetaVal  = editTextAngleVal.getText().toString();

                        iXValue             = CheckUserChoice.checkIntParam(strXVal);
                        iYValue             = CheckUserChoice.checkIntParam(strYVal);
                        iThetaValue         = CheckUserChoice.checkIntParam(strThetaVal);

                        debugParamMove.setText("Xval = " + strXVal + "; YVal = " + iYValue + "; Theta = " + iThetaValue);
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
