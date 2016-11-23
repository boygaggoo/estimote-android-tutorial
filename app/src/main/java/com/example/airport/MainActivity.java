package com.example.airport;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final Map<String, List<String>> PLACES_BY_BEACONS;

    // TODO: replace "<major>:<minor>" strings to match your own beacons.
    static {
        Map<String, List<String>> placesByBeacons = new HashMap<>();
        placesByBeacons.put("24184:38128", new ArrayList<String>() {{
            add("Boupell SA de CV");
            // read as: "Heavenly Sandwiches" is closest
            // to the beacon with major 22504 and minor 48827
            add("quando");
            // "Green & Green Salads" is the next closest
            add("HiHo");
            // "Mini Panini" is the furthest away
        }});
        placesByBeacons.put("37878:25684", new ArrayList<String>() {{
            add("Museo de teotihuacan");
            add("Museo del prado");
            add("Galeria Uffizy");
        }});
        placesByBeacons.put("588:58313", new ArrayList<String>() {{
            add("El Escorial");
            add("Arco de la calzada");
            add("Expora science center");
        }});
        PLACES_BY_BEACONS = Collections.unmodifiableMap(placesByBeacons);
    }

    private List<String> placesNearBeacon(Beacon beacon) {
        String beaconKey = String.format("%d:%d", beacon.getMajor(), beacon.getMinor());
        if (PLACES_BY_BEACONS.containsKey(beaconKey)) {
            return PLACES_BY_BEACONS.get(beaconKey);
        }
        return Collections.emptyList();
    }

    private BeaconManager beaconManager;
    private Region region;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv=(TextView)findViewById(R.id.cajatexto);
 /*
        if(MyApplication.beaconManager!=null)
            beaconManager=MyApplication.beaconManager;
        else
             beaconManager = new BeaconManager(this);
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                if (!list.isEmpty()) {
                    Beacon nearestBeacon = list.get(0);
                    List<String> places = placesNearBeacon(nearestBeacon);
                    // TODO: update the UI here
                    int min=nearestBeacon.getMinor();
                    int may=nearestBeacon.getMajor();
                    UUID uuid=nearestBeacon.getProximityUUID();


                    tv.setText("Sitios cercanos a "+may+":"+min+": " + places);


                    Log.d("quando", "Sitios cercanos a "+may+":"+min+": " + places);
                }
            }
        });
        region = new Region("ranged region", UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), null, null);
 */
    }

    @Override
    protected void onResume() {
        super.onResume();

       /* SystemRequirementsChecker.checkWithDefaultDialogs(this);

       beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
            }
        });
        */
    }

    @Override
    protected void onPause() {
     //   beaconManager.stopRanging(region);

        super.onPause();
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

    public void irasensores(View v){
        startListBeaconsActivity(SensorsActivity.class.getName());

    }


    private void startListBeaconsActivity(String extra) {
        Intent intent = new Intent(MainActivity.this, ListBeaconsActivity.class);
        intent.putExtra(ListBeaconsActivity.EXTRAS_TARGET_ACTIVITY, extra);
        startActivity(intent);
    }

}
