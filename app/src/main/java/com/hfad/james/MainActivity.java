package com.hfad.james;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.scan_btn)
    Button scanButton;
    @BindView(R.id.include)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        if (!isTablet) {
            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, TabletMenuActivity.class);
            startActivity(intent);
        }
    }

    public void onClickScan(View view) {
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        intentIntegrator.setPrompt("scan");
        intentIntegrator.setCameraId(0);
        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.setBarcodeImageEnabled(false);
        intentIntegrator.initiateScan();
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//        if (intentResult != null) {
//            if (intentResult.getContents() == null) {
//                Toast toast = Toast.makeText(this, "you cancelled the scan", Toast.LENGTH_LONG);
//                toast.show();
//            } else {
//                boolean isTablet = getResources().getBoolean(R.bool.isTablet);
//                if (!isTablet) {
//                    Intent intent = new Intent(this, MenuActivity.class);
//                    startActivity(intent);
//                } else {
//                    Intent intent = new Intent(this, TabletMenuActivity.class);
//                    startActivity(intent);
//                }
////                Toast toast = Toast.makeText(this, intentResult.getContents(), Toast.LENGTH_LONG);
////                toast.show();
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
}
