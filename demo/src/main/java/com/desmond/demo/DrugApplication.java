package com.desmond.demo;

import android.app.AlarmManager;
import android.app.Application;
import android.content.pm.PackageManager;
import android.util.Log;

import com.desmond.demo.box.model.Drug;
import com.desmond.demo.common.AppConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.reflect.Field;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmSchema;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
//import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
//import retrofit2.RxJavaCallAdapterFactory;

/**
 * Created by wangzunhui on 2015/9/18.
 */
public class DrugApplication extends Application {
    private static Retrofit retrofit;
    private Integer versionCode = 0;
    public static String token = "0";
    private Integer realmVersion = 2;
    public static double longitude;
    public static double latitude;

    public static Retrofit getRetrofit() {
        return retrofit;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this)
                .schemaVersion(realmVersion)
                .migration(migration)
                .build();

//        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(realmConfig);

        initRetrofit();

        AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
    }

    RealmMigration migration = new RealmMigration() {
        @Override
        public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
            Log.e("Drug", oldVersion + " - " + newVersion);
            RealmSchema schema = realm.getSchema();
//            for (String name : schema.get("Drug").getFieldNames()) {
//                Log.e("Drug", name);
//            }

            if (oldVersion == 1) {
                schema.get("Drug").addField("icon", String.class);
            }
        }
    };

    private void initRetrofit() {
        try {
            versionCode = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                    }
            },
            new SecureRandom());
        } catch (Exception e) {
            e.printStackTrace();
        }
        HostnameVerifier hv1 = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request newRequest = chain.request().newBuilder()
                                .addHeader("platform", "android")
                                .addHeader("appVersion", versionCode.toString())
                                .addHeader("latitude", new Double(DrugApplication.latitude).toString())
                                .addHeader("longitude", new Double(DrugApplication.longitude).toString())
                                .addHeader("authorization", token == null ? "0" : token)
                                .build();
                        return chain.proceed(newRequest);
                    }
                })
                .build();

        try {
            String workerClassName = "okhttp3.OkHttpClient";
            client = new OkHttpClient.Builder().build();
            Class workerClass = Class.forName(workerClassName);
            Field hostnameVerifier = workerClass.getDeclaredField("hostnameVerifier");
            hostnameVerifier.setAccessible(true);
            hostnameVerifier.set(client, hv1);
            Field sslSocketFactory = workerClass.getDeclaredField("sslSocketFactory");
            sslSocketFactory.setAccessible(true);
            sslSocketFactory.set(client, sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm")
                .create();

        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(AppConfig.WEB_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }
}
