package com.example.airport;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.EstimoteSDK;
import com.estimote.sdk.Region;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MyApplication extends Application {

    public static BeaconManager beaconManager;

    private static final Map<String, List<String>> PLACES_BY_BEACONS;

    // TODO: replace "<major>:<minor>" strings to match your own beacons.
    static {
        Map<String, List<String>> placesByBeacons = new HashMap<>();
        placesByBeacons.put("24184:38128", new ArrayList<String>() {{
            add("Boupell"); //nombre
            // read as: "Heavenly Sandwiches" is closest
            // to the beacon with major 22504 and minor 48827
            add("Aplicaciones móviles"); //producto 1
            // "Green & Green Salads" is the next closest
            add("Gente trabajadora"); // producto 2
            // "Mini Panini" is the furthest away
        }});
        placesByBeacons.put("37878:25684", new ArrayList<String>() {{
            add("Museo de teotihuacan");
            add("Piramides");
            add("Shows");
        }});
        placesByBeacons.put("588:58313", new ArrayList<String>() {{
            add("El Escorial");
            add("Arte");
            add("Arquitectura");
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


    @Override
    public void onCreate() {
        super.onCreate();


        EstimoteSDK.initialize(this, "estimotedemos-543", "b88b15ace1e8a31f3b975e42c0ff7bff");

        beaconManager = new BeaconManager(getApplicationContext());
//default scan period is 5s, waittime v25s. Por default los beacons estan configurados para radiar cada 950 ms
        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> list) {
                if(!list.isEmpty()){
                    for(int i=0;i<list.size();i++){
                        Beacon currentbeacon=list.get(i);
                       List<String> valores=placesNearBeacon(currentbeacon);


                        showNotification("Entrando a Región"+valores.get(0),valores.get(1)+" , "+valores.get(2));
                    }
                }
            }
            @Override
            public void onExitedRegion(Region region) {
                // could add an "exit" notification too if you want (-:
              //  showNotification("Salida de Región",region.getMajor()+" : "+region.getMinor());
            }
        });
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startMonitoring(new Region("todas regiones",
                        UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), null, null));
            }
        });


    }

    public void showNotification(String title, String message) {
        Intent notifyIntent = new Intent(this, ShowAdActivity.class);
        notifyIntent.putExtra("texto1",message);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText("En este sitio podras encontrar: "+message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }
}
