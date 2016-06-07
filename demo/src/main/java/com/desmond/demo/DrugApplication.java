package com.desmond.demo;

import android.app.Application;
import android.content.pm.PackageManager;
import android.util.Log;

import com.desmond.demo.common.AppConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

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
    private Integer realmVersion = 1;

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
    }

    RealmMigration migration = new RealmMigration() {
        @Override
        public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
            Log.e("Drug", oldVersion + " - " + newVersion);
            RealmSchema schema = realm.getSchema();
//            for (String name : schema.get("Drug").getFieldNames()) {
//                Log.e("Drug", name);
//            }

//            if (oldVersion == 1) {
//
//            }
        }
    };

    private void initRetrofit() {
        try {
            versionCode = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request newRequest = chain.request().newBuilder()
                                .addHeader("platform", "android")
                                .addHeader("appVersion", versionCode.toString())
                                .addHeader("authorization", token == null ? "0" : token)
                                .build();
                        return chain.proceed(newRequest);
                    }
                })
                .build();

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
