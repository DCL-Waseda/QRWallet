package com.example.gushimakota.qrwallet;

/**
 * Created by gushimakota on 16/06/10.
 */
public interface BtConnectionStatus {
    void onBtNotAvailable();
    void onBtConnecting();
    void onBtConnected();
    void onBtDeviceNotFound();
    void onBtConnectionFailed();
}
