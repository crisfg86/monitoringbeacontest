package com.example.monitoringbeacon;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.example.monitoringbeacon.altbeacontest.R;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

import java.util.Random;

/**
 * Created by cris on 20/1/16.
 */
public class MyApplication extends Application implements BootstrapNotifier {

    protected static final String TAG = "MonitoringActivity";
    private static final String SENSORO_REGION = "sensoro";

    private RegionBootstrap sensoroRegionBootstrap;
    private BackgroundPowerSaver backgroundPowerSaver;

    @Override
    public void didEnterRegion(Region region) {
        Log.i(TAG, "Enter region " + region.getUniqueId());

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder b = new NotificationCompat.Builder(this);

        b.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setTicker("AltBeaconTest")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Enjoy your working day!")
                .setContentText("Great day at the office!")
                .setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent)
                .setContentInfo("Info");

        Random r = new Random();
        int identifier = r.nextInt(1000 - 1) + 1;
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(identifier, b.build());

    }

    @Override
    public void didExitRegion(Region region) {
        Log.i(TAG, "Leaving region " + region.getUniqueId());

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder b = new NotificationCompat.Builder(this);

        b.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setTicker("AltBeaconTest")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("The office says goodbye!")
                .setContentText("you deserve a rest!")
                .setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent)
                .setContentInfo("Info");

        Random r = new Random();
        int identifier = r.nextInt(1000 - 1) + 1;
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, b.build());
    }

    @Override
    public void didDetermineStateForRegion(int state, Region region) {
        Log.i(TAG, "I have just switched from seeing/not seeing beacons: "+state);
    }

    public void onCreate(){
        super.onCreate();

        backgroundPowerSaver = new BackgroundPowerSaver(this);

        BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser()
                .setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.setBackgroundBetweenScanPeriod(25000l);
        beaconManager.setBackgroundScanPeriod(5000l);

        Identifier id1Sensoro = Identifier.parse("23A01AF0-232A-4518-9C0E-323FB773F5EF");
        Identifier id2Sensoro = Identifier.parse("4FAC");
        Identifier id3Sensoro = Identifier.parse("A879");
        Region regionSensoro = new Region(SENSORO_REGION, id1Sensoro, id2Sensoro, id3Sensoro);
        sensoroRegionBootstrap = new RegionBootstrap(this, regionSensoro);
    }
}
