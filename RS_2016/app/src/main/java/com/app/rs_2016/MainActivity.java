package com.app.rs_2016;

//Import of the libraries
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
import android.widget.Toast;


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

    private int         iOctet1IPValue ;
    private int         iOctet2IPValue ;
    private int         iOctet3IPValue ;
    private int         iOctet4IPValue ;
    private String      strIPRobot ;
    private String      strIPCM ;
    public boolean      bCheckResult ;

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

        //Set links for the lists
        this.robotList              = (ListView)this.findViewById(R.id.listViewRobot) ;
        this.featuresList           = (ListView)this.findViewById(R.id.listViewFeatures) ;

        //Set the click listeners for the buttons
        buttonConnexionCM.setOnClickListener(
            new OnClickListener() {
                public void onClick(View v) {

                    iOctet1IPValue  = Integer.parseInt(editTextIPOctect1CM.getText().toString());
                    iOctet2IPValue  = Integer.parseInt(editTextIPOctect2CM.getText().toString());
                    iOctet3IPValue  = Integer.parseInt(editTextIPOctect3CM.getText().toString());
                    iOctet4IPValue  = Integer.parseInt(editTextIPOctect4CM.getText().toString());

                    bCheckResult    = CheckUserChoice.checkIP(iOctet1IPValue, iOctet2IPValue, iOctet3IPValue, iOctet4IPValue);

                    strIPCM         = "IP CM : " + iOctet1IPValue + "." + iOctet2IPValue + "." + iOctet3IPValue + "." + iOctet4IPValue + " Check Res = " + bCheckResult ;
                    debugTextIPCM.setText(strIPCM);

                }
            });

        buttonSend.setOnClickListener(
                new OnClickListener() {
                    public void onClick(View v) {
                        strIPCM         = "IP CM : " +editTextIPOctect1Robot.getText().toString() + "." + editTextIPOctect2Robot.getText().toString() + "." + editTextIPOctect3Robot.getText().toString()
                                + "." + editTextIPOctect4Robot.getText().toString();
                        debugTextIPCM.setText(strIPRobot);

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
