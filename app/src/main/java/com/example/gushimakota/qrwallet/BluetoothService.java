package com.example.gushimakota.qrwallet;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

/**
 * Created by gushimakota on 16/06/10.
 */
public class BluetoothService {
    private static BluetoothAdapter mBtAdapter = null;
    private static BluetoothDevice mDevice = null;
    private BluetoothSocket mSocket = null;
    private Handler mHandler = new Handler();

    private BtConnectionStatus mStatus = null;
    private BluetoothConnection mConnection = null;

    public BluetoothService(BtConnectionStatus status){
        mStatus = status;
    }

    public void postConnecting(){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mStatus.onBtConnecting();
            }
        });
    }

    public void postConnectionFailed(){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mStatus.onBtConnectionFailed();
            }
        });
    }

    public void postConnected(){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mStatus.onBtConnected();
            }
        });
    }

    void setup(){
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBtAdapter.isEnabled() == false){
            mStatus.onBtNotAvailable();
            return;
        }

        Set<BluetoothDevice> devices = mBtAdapter.getBondedDevices();
        for(BluetoothDevice dev: devices){
            if (dev.getName().equals(R.string.bluetooth_device_name)){
                mDevice = dev;
                break;
            }
        }
        if(mDevice == null){
            mStatus.onBtDeviceNotFound();
            return;
        }

        mConnection = new BluetoothConnection();
        mConnection.start();
    }

    void stop(){
        mConnection.stop();
    }

    private class BluetoothConnection extends Thread {
        private final UUID mmSppUuid = UUID.fromString(String.valueOf(R.string.uuid));

        public BluetoothConnection() {
            BluetoothSocket tmp = null;
            try {
                tmp = mDevice.createRfcommSocketToServiceRecord(mmSppUuid);
            } catch (IOException e) {
                Log.e("Bt", "BluetoothConnection()", e);
            }
            mSocket = tmp;
        }

        @Override
        public void run() {
            postConnecting();
            ;
            mBtAdapter.cancelDiscovery();

            try {
                mSocket.connect();
            } catch (IOException e) {
                this.close();
                Log.e("Bt", "socket.connect error", e);
                postConnectionFailed();
                return;
            }

            postConnected();
        }

        public void close() {
            try {
                mSocket.close();
            } catch (IOException e) {
                Log.e("Bt", "socket.close()", e);
            }
        }
    }
    void write(byte[] bytes){
        BluetoothWrite w = new BluetoothWrite(bytes);
        w.start();
    }

    private class BluetoothWrite extends Thread{
        private OutputStream mmOutputStream = null;
        private byte[] mmBuffer = null;

        public BluetoothWrite(byte[] bytes){
            mmBuffer = bytes;
        }

        @Override
        public void run(){
            if (mmBuffer == null){
                return;
            }
            try{
                mmOutputStream = mSocket.getOutputStream();
                mmOutputStream.write(mmBuffer);
            }catch (IOException e){
                Log.e("Bt","outputstream.write ",e);
                postConnectionFailed();
            }
        }
    }
}
