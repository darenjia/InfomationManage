package com.bokun.bkjcb.infomationmanage;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.bokun.bkjcb.infomationmanage.Acrivity.CheckPermissionsActivity;

public class MainActivity extends CheckPermissionsActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + 12306));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
