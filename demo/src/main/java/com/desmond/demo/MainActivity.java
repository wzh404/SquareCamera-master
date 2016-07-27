package com.desmond.demo;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.desmond.demo.base.view.MainView;
import com.desmond.demo.common.util.NetworkUtil;
import com.desmond.demo.plan.receiver.AlarmBroadcastReceiver;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity  {
    protected LocationManager locationManager;
    private String provider;
    private PendingIntent sender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainView view = new MainView(this);
        setContentView(view.getView());

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> locationList = locationManager.getProviders(true);
        if (locationList.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else if (locationList.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else {
            Toast.makeText(getApplicationContext(), "没有可用的定位服务", Toast.LENGTH_LONG).show();
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            Log.e("Drug", "_______________NO_PERMISSION_GRANTED");

//            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},9);
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null){
            Log.e("Drug", location.getLongitude() + " ____ " + location.getLatitude());
        }

        locationManager.requestLocationUpdates(provider, 5000, 1, locationListener);
        Log.e("Drug", provider + "__________" + NetworkUtil.getNetworkType(this));

        startAlarm();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //将监听器移除
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.removeUpdates(locationListener);

        AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
        manager.cancel(sender);
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.e("Drug", location.getLongitude() + " - " + location.getLatitude());
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e("Drug", "_____________change");
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e("Drug", "_____________enabled");
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e("Drug", "_____________disabled");
        }
    };

    private void startAlarm(){
        Intent intent = new Intent(this, AlarmBroadcastReceiver.class);
        intent.putExtra("ID", "201101");
        sender = PendingIntent.getBroadcast(this, 0, intent, 0);

        AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
        Calendar c= Calendar.getInstance();
        c.setTime(new Date());

//        c.set(Calendar.YEAR, 2016);
//        c.set(Calendar.MONTH, Calendar.JULY);//也可以填数字，0-11,一月为0
//        c.set(Calendar.DAY_OF_MONTH, 27);
        c.set(Calendar.HOUR_OF_DAY, 17);
        c.set(Calendar.MINUTE, 30);
        c.set(Calendar.SECOND, 00);

        manager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), sender);
    }
}
