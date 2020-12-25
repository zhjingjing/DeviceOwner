package com.zh.test.test;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.util.List;

/**
 * @Author: Administrator
 * @Time 2020/12/3 00039:53
 * @Email: zhaohang@zhizhangyi.com
 * @Describe:
 */
public class PhoneManager {

    public static String getPhoneNumber(Activity context, int index) {
        String phone = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            SubscriptionManager subscriptionManager = SubscriptionManager.from(context);
            if (subscriptionManager != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        context.requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE},1001);
                    }else{
                        List<SubscriptionInfo> activeSubscriptionInfoList = subscriptionManager.getActiveSubscriptionInfoList();
                        if (activeSubscriptionInfoList != null && activeSubscriptionInfoList.size() > 0) {
                            for (SubscriptionInfo subscriptionInfo : activeSubscriptionInfoList) {
                                if (index != subscriptionInfo.getSimSlotIndex()) {
                                    continue;
                                }
                                if (TextUtils.isEmpty(subscriptionInfo.getNumber())) {
                                    return subscriptionInfo.getNumber();
                                }
                            }
                        }
                    }
                }
            }
        }
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            return telephonyManager.getLine1Number();
        }
        return phone;

    }
}
