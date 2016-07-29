package com.example.gushimakota.qrwallet;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class QRActivity extends AppCompatActivity{

    private SquareQR mBarcodeView;
    private SharedPreferences prefMoney;
    private SharedPreferences prefList;
    private int reminingMoney;
    private int productMoney;
    private ItemsMap itemsMap;



    /** Bluetooth Adapter */
    private BluetoothAdapter mAdapter;
    /** Bluetoothデバイス */
    private BluetoothDevice mDevice;
    /** Bluetooth UUID */
    private final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    /** デバイス名 */
    private final String DEVICE_NAME = "RNBT-B187";
    /** Soket */
    private BluetoothSocket mSocket;

    /** BluetoothのOutputStream. */
    OutputStream mmOutputStream = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        itemsMap = new ItemsMap();
        mBarcodeView = (SquareQR) findViewById(R.id.barcode_scanner);
        setBluetoothAdapter();
        setReminingFragment();
        scanning();
    }



    private void setBluetoothAdapter(){
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        Set< BluetoothDevice > devices = mAdapter.getBondedDevices();
        for ( BluetoothDevice device : devices){
            if(device.getName().equals(DEVICE_NAME)){
                mDevice = device;
                Log.e(device.getName(),device.getAddress());
                try {
                    mSocket = mDevice.createInsecureRfcommSocketToServiceRecord(MY_UUID);
                    mSocket.connect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }
        }
    }

    public void debug2(View v){
        try {
            mmOutputStream = mSocket.getOutputStream();
            char message = 'o';
            try {
                mmOutputStream.write(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            Log.e("Bluetooth socket err", "connect failed!!");
            e.printStackTrace();
        }
    }

    private void sendMsg(){
        try {
            mmOutputStream = mSocket.getOutputStream();
            char message = 'a';
            try {
                mmOutputStream.write(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            Log.e("Bluetooth socket err", "connect failed!!");
            e.printStackTrace();
        }
    }

    private void setReminingFragment(){
        prefMoney = getSharedPreferences("ReminingMoney", Context.MODE_PRIVATE);
        reminingMoney = prefMoney.getInt("ReminingMoney",-100);
        ReminingFragment fragment = ReminingFragment.newInstance(String.valueOf(reminingMoney),"a");
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.linear_money, fragment);
        transaction.commit();
    }

    public void debugBtn(View v){
        String debug = "DebugTest";
        if(itemsMap.checkThePrice(debug) < 0 || itemsMap.checkThePrice(debug) - reminingMoney > 0){
            return;
        }
        reminingMoney -= itemsMap.checkThePrice(debug);
        SharedPreferences.Editor editor = prefMoney.edit();
        editor.putInt("ReminingMoney", reminingMoney);
        editor.apply();
        sendMsg();
        addSharedList(debug);
        Intent intent = new Intent(QRActivity.this,com.example.gushimakota.qrwallet.FinishActivity.class);
        startActivity(intent);
        finish();
        socketClose();
    }

    public void socketClose() {
        Thread t = new Thread(){
            @Override
            public void run(){
                try {
                    Thread.sleep(500);
                    try {
                        mSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
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
        try{
            mSocket.close();
        }
        catch(Exception e){}
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
//        mBtService.stop();
    }

    private void scanning(){
        mBarcodeView.decodeSingle(new BarcodeCallback() {

            //QR読んだ時の処理
            @Override
            public void barcodeResult(final BarcodeResult result) {
                readQR(result);
                if(productMoney < 0){
                    Toast.makeText(getBaseContext(),"このQRコードは読めません",Toast.LENGTH_SHORT).show();
                    scanning();
                    return;
                }else if(itemsMap.checkThePrice(result.getText()) - reminingMoney > 0){
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
                                reminingMoney -= productMoney;
                                SharedPreferences.Editor editorMoney = prefMoney.edit();
                                editorMoney.putInt("ReminingMoney", reminingMoney);
                                editorMoney.apply();
                                sendMsg();
                                Intent intent = new Intent(QRActivity.this,com.example.gushimakota.qrwallet.FinishActivity.class);
                                startActivity(intent);
                                finish();
                                socketClose();
                            }
                        });
                alertDlg.setNegativeButton(
                        "読みなおす",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Cancel ボタンクリック処理
                                scanning();
                            }
                        });

                // 表示
                alertDlg.create().show();
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {}
        });
    }

    private void readQR(BarcodeResult result){
        String qrText = result.getText();
        productMoney = itemsMap.checkThePrice(qrText);
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
        strings.add(0,result);
        // JSONに再変換
        String stringJson = gson.toJson(strings);
        // 保存
        editorList.putString("List", stringJson);
        editorList.apply();
    }
}
