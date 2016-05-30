package com.example.gushimakota.qrwallet;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;

import java.util.List;

public class QRActivity extends AppCompatActivity {

    private SquareQR mBarcodeView;
    private FragmentManager manager;
    private SharedPreferences pref;
    private int money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        mBarcodeView = (SquareQR) findViewById(R.id.barcode_scanner);
        manager = getSupportFragmentManager();
        setReminingFragment();
        scanning();
    }

    private void setReminingFragment(){
        pref = getSharedPreferences("ReminingMoney", Context.MODE_PRIVATE);
        money = pref.getInt("money",-100);
        ReminingFragment fragment = ReminingFragment.newInstance(String.valueOf(money),"a");
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.linear_money, fragment);
        transaction.commit();
    }

    public void debugBtn(View v){
        money -= 100;
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("money", money);
        editor.apply();
        Intent intent = new Intent(QRActivity.this,com.example.gushimakota.qrwallet.FinishActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onResume() {
        super.onResume();
        mBarcodeView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mBarcodeView.pause();
    }

    private void scanning(){
        mBarcodeView.decodeSingle(new BarcodeCallback() {

            //QR読んだ時の処理
            @Override
            public void barcodeResult(BarcodeResult result) {
                TextView textView = (TextView)findViewById(R.id.qr_text);
                textView.setText(result.getText());

                AlertDialog.Builder alertDlg = new AlertDialog.Builder(QRActivity.this);
                alertDlg.setTitle("この内容で送信します");
                alertDlg.setMessage("よろしいですか？");
                alertDlg.setPositiveButton(
                        "購入する",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                money -= 100;
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putInt("money", money);
                                editor.apply();
                                Intent intent = new Intent(QRActivity.this,com.example.gushimakota.qrwallet.FinishActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                alertDlg.setNegativeButton(
                        "読みなおす",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Cancel ボタンクリック処理
                                scanning();
                                return;
                            }
                        });

                // 表示
                alertDlg.create().show();
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {}
        });
    }
}
