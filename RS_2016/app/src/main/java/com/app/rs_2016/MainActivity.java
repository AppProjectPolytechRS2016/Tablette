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
    private Button              buttonConnexionCM ;
    private Button              buttonDeconnexionCM ;
    private Button              buttonReccord ;
    private Button              buttonWalk ;
    private Button              buttonMove ;
    private Button              buttonSend ;

    private EditText            editTextIPOctect1CM ;
    private EditText            editTextIPOctect2CM ;
    private EditText            editTextIPOctect3CM ;
    private EditText            editTextIPOctect4CM ;
    private EditText            editTextIPOctect1Robot ;
    private EditText            editTextIPOctect2Robot ;
    private EditText            editTextIPOctect3Robot ;
    private EditText            editTextIPOctect4Robot ;
    private EditText            editTextXVal ;
    private EditText            editTextYVal ;
    private EditText            editTextAngleVal ;

    private ListView            robotList ;
    private ListView            featuresList ;

    private TextView            debugTextIPCM ;
    private TextView            debugTextIPRobot;
    private TextView            debugTextIPTablet;

    private TextView            infoDebug;

    private int                 iOctet1IPValue ;
    private int                 iOctet2IPValue ;
    private int                 iOctet3IPValue ;
    private int                 iOctet4IPValue ;

    private int                 iByteRobotIP1_Value ;
    private int                 iByteRobotIP2_Value ;
    private int                 iByteRobotIP3_Value ;
    private int                 iByteRobotIP4_Value ;

    private String              strIPRobot      = null;
    private String              strIPCM         = null;
    private String              tabletAddress;
    private int                 iPortCMNum      = 6020;
    public boolean              bCheckResult ;

    private ApplicationTablet   appTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //To add to enable the connexion to the comManager
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        //Set links for the buttons
        this.buttonConnexionCM      = (Button)this.findViewById(R.id.buttonConnexionCM) ;
        this.buttonDeconnexionCM    = (Button)this.findViewById(R.id.buttonDeconnexionCM) ;
        this.buttonMove             = (Button)this.findViewById(R.id.buttonMove) ;
        this.buttonReccord          = (Button)this.findViewById(R.id.buttonReccord) ;
     //   this.buttonSend             = (Button)this.findViewById(R.id.buttonSend);
        this.buttonSend             = (Button)this.findViewById(R.id.buttonSendTestPhone);
        this.buttonWalk             = (Button)this.findViewById(R.id.buttonWalk) ;

        //Set links for the edit texts
        this.editTextAngleVal       = (EditText)this.findViewById(R.id.editTextThetaVal) ;
        this.editTextXVal           = (EditText)this.findViewById(R.id.editTextXVal) ;
        this.editTextYVal           = (EditText)this.findViewById(R.id.editTextYVal) ;

        this.editTextIPOctect1CM    = (EditText)this.findViewById(R.id.ipCMOctet1) ;
        this.editTextIPOctect2CM    = (EditText)this.findViewById(R.id.ipCMOctet2) ;
        this.editTextIPOctect3CM    = (EditText)this.findViewById(R.id.ipCMOctet3) ;
        this.editTextIPOctect4CM    = (EditText)this.findViewById(R.id.ipCMOctet4) ;

        this.editTextIPOctect1Robot = (EditText)this.findViewById(R.id.editTextIPRobot1) ;
        this.editTextIPOctect2Robot = (EditText)this.findViewById(R.id.editTextIPRobot2) ;
        this.editTextIPOctect3Robot = (EditText)this.findViewById(R.id.editTextIPRobot3) ;
        this.editTextIPOctect4Robot = (EditText)this.findViewById(R.id.editTextIPRobot4) ;

        //Set links for the text views
        this.debugTextIPCM          = (TextView)this.findViewById(R.id.debugIPCM);
        this.debugTextIPRobot       = (TextView)this.findViewById(R.id.debugIPRobot);
        this.debugTextIPTablet       = (TextView)this.findViewById(R.id.debugIPTablet);
        this.infoDebug              = (TextView)this.findViewById(R.id.infoDebug);

        //Set links for the lists
        this.robotList              = (ListView)this.findViewById(R.id.listViewRobot) ;
        this.featuresList           = (ListView)this.findViewById(R.id.listViewFeatures) ;

        //Hide de Deconnexion button
        buttonDeconnexionCM.setVisibility(View.INVISIBLE);

        //Recover the IP address of the tablet
        WifiManager wifiMgr         = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo           = wifiMgr.getConnectionInfo();
        int ip                      = wifiInfo.getIpAddress();

        //Convert to IP format
        tabletAddress               = String.format("%d.%d.%d.%d", (ip & 0xff), (ip >> 8 & 0xff), (ip >> 16 & 0xff), (ip >> 24 & 0xff));

       debugTextIPTablet.setText("IP Tablet : " + tabletAddress);

        //Set the click listeners for the Connexion button
        buttonConnexionCM.setOnClickListener(
                new OnClickListener() {
                    public void onClick(View v) {
                        //Recover each byte value of the IP
                        iOctet1IPValue = Integer.parseInt(editTextIPOctect1CM.getText().toString());
                        iOctet2IPValue = Integer.parseInt(editTextIPOctect2CM.getText().toString());
                        iOctet3IPValue = Integer.parseInt(editTextIPOctect3CM.getText().toString());
                        iOctet4IPValue = Integer.parseInt(editTextIPOctect4CM.getText().toString());

                        //Check if the IP address is valid
                        bCheckResult = CheckUserChoice.checkIP(iOctet1IPValue, iOctet2IPValue, iOctet3IPValue, iOctet4IPValue);

                        //Display for debug
                       // strIPCM = iOctet1IPValue + "." + iOctet2IPValue + "." + iOctet3IPValue + "." + iOctet4IPValue ;
                        strIPCM                     = String.format("%d.%d.%d.%d", iOctet1IPValue, iOctet2IPValue, iOctet3IPValue, iOctet4IPValue);
                        debugTextIPCM.setText("IP CM : " + strIPCM + " Check Res = " + bCheckResult);

                        if(bCheckResult == true) {
                            ExecutorService execServ    = Executors.newFixedThreadPool(3);
                            appTab                      = new ApplicationTablet("tablet", iPortCMNum, execServ);
                            int iTest                   = appTab.connexionCM(strIPCM, iPortCMNum);


                            if(iTest == 1) {
                                Toast.makeText(MainActivity.this, "La tablette est maintenant connectée au gestionnaire de communication.", Toast.LENGTH_LONG).show();

                                //Hide the connexion button and display the deconnexion button
                                buttonConnexionCM.setVisibility(View.INVISIBLE);
                                buttonDeconnexionCM.setVisibility(View.INVISIBLE);
                            }

                            else{
                                Toast.makeText(MainActivity.this, "La tablette n'est pas connectée au gestionnaire de communication.", Toast.LENGTH_LONG).show();
                            }
                        }

                        else{
                            Toast.makeText(MainActivity.this, "L'adresse IP renseignée est invalide.", Toast.LENGTH_LONG).show();
                        }



                    }
                });

        buttonSend.setOnClickListener(
                new OnClickListener() {
                    public void onClick(View v) {
                        //Recover each byte value of the IP
                        iByteRobotIP1_Value  = Integer.parseInt(editTextIPOctect1Robot.getText().toString());
                        iByteRobotIP2_Value  = Integer.parseInt(editTextIPOctect2Robot.getText().toString());
                        iByteRobotIP3_Value  = Integer.parseInt(editTextIPOctect3Robot.getText().toString());
                        iByteRobotIP4_Value  = Integer.parseInt(editTextIPOctect4Robot.getText().toString());

                        //Check if th IP address if valid
                        bCheckResult    = CheckUserChoice.checkIP(iByteRobotIP1_Value, iByteRobotIP2_Value, iByteRobotIP3_Value, iByteRobotIP4_Value);


                        //Display for debug
                        strIPRobot         = "IP Robot : " + iByteRobotIP1_Value + "." + iByteRobotIP2_Value + "." + iByteRobotIP3_Value + "." + iByteRobotIP4_Value + " Check Res = " + String.valueOf(bCheckResult) ;
                        debugTextIPRobot.setText(strIPRobot);

                    }
                });

        buttonDeconnexionCM.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {

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
