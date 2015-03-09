package com.app.rs_2016;

//Import libraries for layout elements
import android.content.Context;
import android.net.wifi.WifiInfo;
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

//Import libraries for JSON
import org.json.*;


public class MainActivity extends ActionBarActivity {
    //Declaration of the different elements
    private Button      buttonConnexionCM ;
    private Button      buttonDeconnexionCM ;
    private Button      buttonReccord ;
    private Button      buttonWalk ;
    private Button      buttonMove ;
    private Button      buttonSend ;

    private EditText    editTextIPOctect1CM ;
    private EditText    editTextIPOctect2CM ;
    private EditText    editTextIPOctect3CM ;
    private EditText    editTextIPOctect4CM ;
    private EditText    editTextIPOctect1Robot ;
    private EditText    editTextIPOctect2Robot ;
    private EditText    editTextIPOctect3Robot ;
    private EditText    editTextIPOctect4Robot ;
    private EditText    editTextXVal ;
    private EditText    editTextYVal ;
    private EditText    editTextAngleVal ;

    private ListView    robotList ;
    private ListView    featuresList ;

    private TextView    debugTextIPCM ;
    private TextView    debugTextIPRobot;
    private TextView    debugTextIPTablet;

    private int         iByteCMIP1_Value ;
    private int         iByteCMIP2_Value ;
    private int         iByteCMIP3_Value ;
    private int         iByteCMIP4_Value ;
    private String      strIPCM ;

    private int         iByteRobotIP1_Value ;
    private int         iByteRobotIP2_Value ;
    private int         iByteRobotIP3_Value ;
    private int         iByteRobotIP4_Value ;
    private String      strIPRobot ;

    public boolean      bCheckResult ;

    private String tabletAddress ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set links for the buttons
        this.buttonConnexionCM      = (Button)this.findViewById(R.id.buttonConnexionCM) ;
        this.buttonDeconnexionCM    = (Button)this.findViewById(R.id.buttonDeconnexionCM) ;
        this.buttonMove             = (Button)this.findViewById(R.id.buttonMove) ;
        this.buttonReccord          = (Button)this.findViewById(R.id.buttonReccord) ;
        this.buttonSend             = (Button)this.findViewById(R.id.buttonSend);
        this.buttonWalk             = (Button)this.findViewById(R.id.buttonWalk) ;

        //Set links for the texts
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

        this.debugTextIPCM          = (TextView)this.findViewById(R.id.debugIPCM);
        this.debugTextIPRobot       = (TextView)this.findViewById(R.id.debugIPRobot);
        this.debugTextIPTablet      = (TextView)this.findViewById(R.id.debugIPTablet);

        //Set links for the lists
        this.robotList              = (ListView)this.findViewById(R.id.listViewRobot) ;
        this.featuresList           = (ListView)this.findViewById(R.id.listViewFeatures) ;

        //Recover the IP address of the tablet
        WifiManager wifiMgr         = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo           = wifiMgr.getConnectionInfo();
        int ip                      = wifiInfo.getIpAddress();

        //Convert to IP format
        tabletAddress               = String.format("%d.%d.%d.%d", (ip & 0xff), (ip >> 8 & 0xff), (ip >> 16 & 0xff), (ip >> 24 & 0xff));

        debugTextIPTablet.setText("IP Tablet: : " + tabletAddress);

        //Set the click listeners for the buttons
        buttonConnexionCM.setOnClickListener(
            new OnClickListener() {
                public void onClick(View v) {

                    //Recover each byte value of the IP
                    iByteCMIP1_Value = Integer.parseInt(editTextIPOctect1CM.getText().toString());
                    iByteCMIP2_Value = Integer.parseInt(editTextIPOctect2CM.getText().toString());
                    iByteCMIP3_Value = Integer.parseInt(editTextIPOctect3CM.getText().toString());
                    iByteCMIP4_Value = Integer.parseInt(editTextIPOctect4CM.getText().toString());

                    bCheckResult = CheckUserChoice.checkIP(iByteCMIP1_Value, iByteCMIP2_Value, iByteCMIP3_Value, iByteCMIP4_Value);

                    //Display for debug
                    strIPCM = "IP CM : " + iByteCMIP1_Value + "." + iByteCMIP2_Value + "." + iByteCMIP3_Value + "." + iByteCMIP4_Value + " Check Res = " + String.valueOf(bCheckResult);
                    debugTextIPCM.setText(strIPCM);

                    //Treatment to do if the IP is valid
                    if (bCheckResult == true) {
                        JSONObject jsonIdentif      = new JSONObject();
                        try {
                            jsonIdentif.put("From", tabletAddress);
                            jsonIdentif.put("To", strIPCM);
                            jsonIdentif.put("Type", "Identification");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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

                        bCheckResult    = CheckUserChoice.checkIP(iByteRobotIP1_Value, iByteRobotIP2_Value, iByteRobotIP3_Value, iByteRobotIP4_Value);


                        //Display for debug
                        strIPRobot         = "IP Robot : " + iByteRobotIP1_Value + "." + iByteRobotIP2_Value + "." + iByteRobotIP3_Value + "." + iByteRobotIP4_Value + " Check Res = " + String.valueOf(bCheckResult) ;
                        debugTextIPRobot.setText(strIPRobot);
                    }
                });
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
