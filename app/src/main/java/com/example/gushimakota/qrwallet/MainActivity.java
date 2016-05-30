package com.example.gushimakota.qrwallet;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements SelectTheActionFragment.OnFragmentInteractionListener{

    private ReminingFragment reminingFragment;
    private SelectTheActionFragment selectTheActionFragment;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private SharedPreferences pref;
    private int money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        setShrdPref();
        setReminingFragment();
        setSelectFragment();
        transaction.commit();
    }

    private void setShrdPref(){
        pref = getSharedPreferences("ReminingMoney", Context.MODE_PRIVATE);
        boolean init = pref.getBoolean("init", false);
        SharedPreferences.Editor editor = pref.edit();
        if (init==false){
            editor.putBoolean("init", true);
            editor.putInt("money",1000);
            editor.apply();
        }
        money = pref.getInt("money",-100);
    }

    private void setReminingFragment(){
        reminingFragment= ReminingFragment.newInstance(String.valueOf(money),"a");
        transaction.add(R.id.linear_main,reminingFragment);
    }

    private void setSelectFragment(){
        selectTheActionFragment = SelectTheActionFragment.newInstance("a","a");
        transaction.add(R.id.linear_main,selectTheActionFragment);
    }

        @Override
    public void onBuyFragmentInteraction(){
//            フラグメントで実装しようとしてた時代
//        manager = getSupportFragmentManager();
//        transaction = manager.beginTransaction();
//        replaceQrFragment();
//        transaction.commit();
    }



}