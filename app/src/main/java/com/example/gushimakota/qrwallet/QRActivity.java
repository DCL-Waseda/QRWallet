package com.example.gushimakota.qrwallet;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.BarcodeView;
import com.journeyapps.barcodescanner.CompoundBarcodeView;

import java.util.List;

public class QRActivity extends AppCompatActivity {

    private CompoundBarcodeView mBarcodeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        mBarcodeView = (CompoundBarcodeView)findViewById(R.id.barcode_scanner);
        scanning();
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
