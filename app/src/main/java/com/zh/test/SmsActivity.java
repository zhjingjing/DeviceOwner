package com.zh.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import net.sqlcipher.database.SQLiteDatabase;

public class SmsActivity extends AppCompatActivity {

    public static void launch(Context context){
        context.startActivity(new Intent(context,SmsActivity.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);


    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_send:
                sendSms();
                break;
        }
    }

    private void sendSms() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                Log.e("xxx","请求");
               SmsActivity.this.requestPermissions(new String[]{Manifest.permission.SEND_SMS},1002);
            }else{
                Log.e("xxx","send");
                SmsManager aDefault = SmsManager.getDefault();
                aDefault.sendTextMessage("10086",null,"6",null,null);
                Toast.makeText(this,"发送成功",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==1002&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
            Log.e("xxx","授权成功");
        }
    }
}
