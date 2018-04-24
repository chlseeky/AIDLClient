package com.study.lyds.aidlclient;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.study.lyds.aidlserver.IMyAidlInterface;

import java.util.List;

/**
 * blog: https://blog.csdn.net/liuyonglei1314/article/details/54317902
 */
public class MainActivity extends AppCompatActivity {
    private IMyAidlInterface mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent();
        intent.setAction("com.lyds.aidl");

        Intent intent1 = new Intent(createExplicitFromImplicitIntent(this, intent));
        bindService(intent1, mServiceC, Context.BIND_AUTO_CREATE);
    }

    ServiceConnection mServiceC = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = IMyAidlInterface.Stub.asInterface(service);

            try {
                int res = mService.add(5, 10);
                Log.e("aidlAdd",""+res);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    /**
     * 兼容Android5.0中service的intent一定要显性声明
     *
     * @param context
     * @param implicitIntent
     * @return
     */
    public static Intent createExplicitFromImplicitIntent(Context context, Intent implicitIntent) {
        // Retrieve all services that can match the given intent
        PackageManager pm = context.getPackageManager();
        //通过queryIntentActivities()方法，查询Android系统的所有具备ACTION_MAIN和CATEGORY_LAUNCHER
        //的Intent的应用程序，点击后，能启动该应用，说白了就是做一个类似Home程序的简易Launcher 。
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);

        // Make sure only one match was found
        if (resolveInfo == null || resolveInfo.size() != 1) {
            return null;
        }

        // Get component info and create ComponentName
        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        ComponentName component = new ComponentName(packageName, className);

        // Create a new intent. Use the old one for extras and such reuse
        Intent explicitIntent = new Intent(implicitIntent);
        // Set the component to be explicit
        explicitIntent.setComponent(component);
        return explicitIntent;
    }
}
