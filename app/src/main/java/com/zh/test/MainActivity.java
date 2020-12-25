package com.zh.test;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zh.test.config.AppConfig;
import com.zh.test.utils.ErrorCode;
import com.zh.test.utils.MyDeviceAdminReceiver;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvDeviceOwner, tvClear, tvRestart, tvLock, tvSetPwd,
            tvAdminList, tvScreenInfo, tvChangeAppHide, tvForbid,
            tvWifiMac, tvUnInstall;
    ComponentName componentName;

    private DevicePolicyManager devicePolicyManager;
    private boolean isDeviceOwner;
    private static final String TAG = "MainActivity.class";

    private boolean isHide = true;
    private boolean isForbidCamera;//是否禁用相机
    private boolean isForbidBar;
    private boolean isSuspended;//是否挂起

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("tag", SystemClock.elapsedRealtime() + "");

        initView();
        initData();
        initListener();
        doActive();

//        printTest();
//        copyAndToast("content","xxxx");
//
//        long time=30_000;
//        try {
//            testLog();
//        }catch (Exception e){
//            Log.e("xxx",e.toString());
//        }finally {
//            Log.e("xxx","结束了");
//        }
//        SmsActivity.launch(this);
//        final ContentResolver cr = getContentResolver();
//        Settings.System.putString(cr, Settings.System.AIRPLANE_MODE_ON, "0");
//        Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
//        sendBroadcast(intent);

    }

    private void testLog(){
        throw new RuntimeException();
    }

    private void copyAndToast(String content, String message) {
        ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setPrimaryClip(ClipData.newPlainText(content, content));
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }


    public void printTest() {
        System.out.println(ErrorCode.RESULT_UNKNOWN_ERROR);
        System.out.println(ErrorCode.RESULT_NETWORK_ERROR);
        System.out.println(ErrorCode.RESULT_JSON_FORMAT_ERROR);
        System.out.println(ErrorCode.RESULT_SERVER_ERROR);
        System.out.println(ErrorCode.RESULT_CANCEL_BY_USER);
    }

    public void initView() {
        tvRestart = findViewById(R.id.tv_restart_app);
        tvClear = findViewById(R.id.tv_clear_owner);
        tvDeviceOwner = findViewById(R.id.tv_device_owner);
        tvLock = findViewById(R.id.tv_lock);
        tvSetPwd = findViewById(R.id.tv_active_admin);
        tvAdminList = findViewById(R.id.tv_active_admin_list);
        tvScreenInfo = findViewById(R.id.tv_lock_screen_info);
        tvChangeAppHide = findViewById(R.id.tv_change_app_status);
        tvForbid = findViewById(R.id.tv_forbid_app);
        tvWifiMac = findViewById(R.id.tv_wifi_mac);
        tvUnInstall = findViewById(R.id.tv_uninstall);
    }

    public void initData() {
        devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        componentName = new ComponentName(this, MyDeviceAdminReceiver.class);
    }

    public String getWifiAddress() {
        //获取wifi服务
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        //判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        return (ipAddress & 0xFF) + "." +
                ((ipAddress >> 8) & 0xFF) + "." +
                ((ipAddress >> 16) & 0xFF) + "." +
                (ipAddress >> 24 & 0xFF);
    }


    public String getWIFISSID(Activity activity) {
        String ssid = "unknown id";

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O || Build.VERSION.SDK_INT == Build.VERSION_CODES.P) {
            WifiManager mWifiManager = (WifiManager) activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

            assert mWifiManager != null;
            WifiInfo info = mWifiManager.getConnectionInfo();

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                return info.getSSID();
            } else {
                return info.getSSID().replace("\"", "");
            }
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O_MR1) {
            ConnectivityManager connManager = (ConnectivityManager) activity.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            assert connManager != null;
            NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
            if (networkInfo.isConnected()) {
                if (networkInfo.getExtraInfo() != null) {
                    return networkInfo.getExtraInfo().replace("\"", "");
                }
            }
        }
        return ssid;
    }

    public void initListener() {
        tvDeviceOwner.setOnClickListener(this);
        tvRestart.setOnClickListener(this);
        tvLock.setOnClickListener(this);
        tvSetPwd.setOnClickListener(this);
        tvClear.setOnClickListener(this);
        tvScreenInfo.setOnClickListener(this);
        tvChangeAppHide.setOnClickListener(this);
        tvForbid.setOnClickListener(this);
        tvWifiMac.setOnClickListener(this);
    }

    public void doActive() {
        if (!devicePolicyManager.isAdminActive(componentName)) {
            //激活
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
            startActivity(intent);
        } else {

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            isDeviceOwner = devicePolicyManager.isDeviceOwnerApp(componentName.getPackageName());
            Log.e(TAG, "isDeviceOwner:" + isDeviceOwner);
        }
        Log.e(TAG, "isAdminActive:" + devicePolicyManager.isAdminActive(componentName));
    }

    public boolean getAdminActiveStatus() {
        return devicePolicyManager.isAdminActive(componentName);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_device_owner:
                doActive();
                break;
            case R.id.tv_clear_owner:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    只能被deviceOwner调用
                    if (isDeviceOwner) {
                        devicePolicyManager.clearDeviceOwnerApp(componentName.getPackageName());
                    }
                }
                break;
            case R.id.tv_restart_app:
                //获取deviceOwner后可用
                if (getAdminActiveStatus()) {
                    devicePolicyManager.reboot(componentName);
                } else {
                    doActive();
                }
                break;
            case R.id.tv_lock:
                //激活后就可以了
                if (getAdminActiveStatus()) {
                    devicePolicyManager.lockNow();
                } else {
                    doActive();
                }
                break;
            case R.id.tv_active_admin:
                if (isDeviceOwner) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        List<ComponentName> activeAdmins = devicePolicyManager.getActiveAdmins();
                        if (activeAdmins != null) {
                            StringBuilder stringBuilder = new StringBuilder();
                            for (int i = 0; i < activeAdmins.size(); i++) {
                                stringBuilder.append("PackageName:" + activeAdmins.get(i).getPackageName() + "\n"
                                        + "ClassName:" + activeAdmins.get(i).getClassName() + "\n");
                            }
                            tvAdminList.setText(stringBuilder.toString());
                        }
                    }

                } else {
                    Log.e(TAG, "isAdminActive:" + false);
                }

                break;
            case R.id.tv_lock_screen_info:
                if (isDeviceOwner) {
                    devicePolicyManager.setDeviceOwnerLockScreenInfo(componentName, "此设备我管理了，你们别管了，这会在锁屏页面展示，大家别当真。。");
                    Toast.makeText(this, "设置成功了", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.tv_change_app_status:
                if (isDeviceOwner) {
                    devicePolicyManager.setApplicationHidden(componentName, AppConfig.TAOBAO, isHide);
                    Toast.makeText(this, isHide ? "隐藏淘宝" : "显示淘宝", Toast.LENGTH_LONG).show();
                    isHide = !isHide;
                }
                break;
            case R.id.tv_forbid_app:
                if (isDeviceOwner) {
                    isForbidCamera = !isForbidCamera;
                    devicePolicyManager.setCameraDisabled(componentName, isForbidCamera);
                    Toast.makeText(this, isForbidCamera ? "禁用相机" : "启用相机", Toast.LENGTH_LONG).show();
                } else {
                    doActive();
                }
                break;
            case R.id.tv_wifi_mac:
                if (isDeviceOwner) {
                    String wifiMac = devicePolicyManager.getWifiMacAddress(componentName) == null ? "" : devicePolicyManager.getWifiMacAddress(componentName);
                    tvWifiMac.setText(wifiMac);
                } else {
                    doActive();
                }
                break;
            case R.id.tv_change_bar:
                setStatusBarDisabled(componentName, isForbidBar);
                Toast.makeText(this, isForbidBar ? "禁用系统导航栏" : "开启系统导航栏", Toast.LENGTH_LONG).show();
                isForbidBar = !isForbidBar;
                break;
            case R.id.tv_suspended:
                setPackagesSuspended(componentName, new String[]{AppConfig.TAOBAO}, isSuspended);
                isSuspended = !isSuspended;
                Toast.makeText(this, isSuspended ? "开启淘宝" : "禁用淘宝", Toast.LENGTH_LONG).show();
                break;
            case R.id.tv_lock_time:
                if (isDeviceOwner) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        //设置最大休眠时间，
                        devicePolicyManager.setMaximumTimeToLock(componentName, 3000000);
                        Log.e(TAG, String.valueOf(devicePolicyManager.getMaximumTimeToLock(componentName)));
                    }
                }
                break;
            case R.id.tv_uninstall:
                //不是跟设备管理器一样长按没有卸载，而是点击卸载时弹出无法卸载。
                devicePolicyManager.setUninstallBlocked(componentName, AppConfig.TAOBAO, forbidUnInstall);
                forbidUnInstall = !forbidUnInstall;
                tvUnInstall.setText(forbidUnInstall ? "禁止卸载" : "卸载");
                break;
            default:
                break;
        }
    }

    private boolean forbidUnInstall = false;

    /**
     * @param admin
     * @param disabled
     * @return 禁用启用顶部导航栏
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean setStatusBarDisabled(ComponentName admin, boolean disabled) {
        boolean res = false;
        if (isDeviceOwner) {
            res = devicePolicyManager.setStatusBarDisabled(admin, disabled);
        }

        Log.e(TAG, "setStatusBarDisabled" + res);
        return res;
    }


    /**
     * @param admin
     * @param packageNames
     * @param suspended
     * @return 设置应用挂起
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private String[] setPackagesSuspended(ComponentName admin, String[] packageNames, boolean suspended) {
        String[] res = null;

        if (isDeviceOwner) {
            res = devicePolicyManager.setPackagesSuspended(admin, packageNames, suspended);
        }
        return res;
    }

}
