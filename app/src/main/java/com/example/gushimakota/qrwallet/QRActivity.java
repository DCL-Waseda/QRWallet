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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;

import java.util.ArrayList;
import java.util.List;

public class QRActivity extends AppCompatActivity {

    private SquareQR mBarcodeView;
    private FragmentManager manager;
    private SharedPreferences prefMoney;
    private SharedPreferences prefList;
    private int money;
    private ItemsMap itemsMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        itemsMap = new ItemsMap();
        mBarcodeView = (SquareQR) findViewById(R.id.barcode_scanner);
        manager = getSupportFragmentManager();
        setReminingFragment();
        scanning();
    }

    private void setReminingFragment(){
        prefMoney = getSharedPreferences("ReminingMoney", Context.MODE_PRIVATE);
        money = prefMoney.getInt("money",-100);
        ReminingFragment fragment = ReminingFragment.newInstance(String.valueOf(money),"a");
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.linear_money, fragment);
        transaction.commit();
    }

    public void debugBtn(View v){
        String debug = "DebugTest";
        if(itemsMap.checkThePrice(debug) < 0 || itemsMap.checkThePrice(debug) - money > 0){
            return;
        }
        money -= itemsMap.checkThePrice(debug);
        SharedPreferences.Editor editor = prefMoney.edit();
        editor.putInt("money", money);
        editor.apply();

        addSharedList(debug);
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
            public void barcodeResult(final BarcodeResult result) {
                TextView textView = (TextView)findViewById(R.id.qr_text);
                textView.setText(result.getText());
                if(itemsMap.checkThePrice(result.getText()) < 0){
                    Toast.makeText(getBaseContext(),"このQRコードは読めません",Toast.LENGTH_SHORT).show();
                    scanning();
                    return;
                }else if(itemsMap.checkThePrice(result.getText()) - money > 0){
                    Toast.makeText(getBaseContext(),"残高が足りません",Toast.LENGTH_SHORT).show();
                    scanning();
                    return;
                }
                AlertDialog.Builder alertDlg = new AlertDialog.Builder(QRActivity.this);
                alertDlg.setTitle("この商品を購入します");
                alertDlg.setMessage("よろしいですか？");
                alertDlg.setPositiveButton(
                        "購入する",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                addSharedList(result.getText());
                                money -= itemsMap.checkThePrice(result.getText());
                                SharedPreferences.Editor editorMoney = prefMoney.edit();
                                editorMoney.putInt("money", money);
                                editorMoney.apply();
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

    private void addSharedList(String result){
        // 設定値を読み込む
        prefList = getSharedPreferences("HistoryList", Context.MODE_PRIVATE);
        String json;
        List<String> strings = new ArrayList<String>();
        Boolean init = prefList.getBoolean("InitList",false);
        Gson gson = new Gson();
        SharedPreferences.Editor editorList = prefList.edit();
        // JSON形式を配列に保存する
        if(init){
            json = prefList.getString("List", "");
            strings = gson.fromJson(json, ArrayList.class);
        }else{
            editorList.putBoolean("InitList",true);
        }
        strings.add(result);
        // JSONに再変換
        String stringJson = gson.toJson(strings);
        // 保存
        editorList.putString("List", stringJson);
        editorList.commit();
    }
}
