package com.example.gushimakota.qrwallet;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity implements SelectTheActionFragment.OnSelectTheActionFragmentInteractionListener, MyListFragment.OnMyListFragmentInteractionListener {

    private MyListFragment listFragment;
    private ReminingFragment reminingFragment;
    private SelectTheActionFragment selectTheActionFragment;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private SharedPreferences prefMoney;
    private SharedPreferences prefList;
    private int reminingMoney;

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
        prefMoney = getSharedPreferences("ReminingMoney", Context.MODE_PRIVATE);
        prefList = getSharedPreferences("HistoryList", Context.MODE_PRIVATE);

        boolean initMoney = prefMoney.getBoolean("initMoney", false);
        SharedPreferences.Editor editorMoney = prefMoney.edit();
        if (initMoney==false){
            editorMoney.putBoolean("initMoney", true);
            editorMoney.putInt("ReminingMoney",10000);
            editorMoney.apply();
        }
        reminingMoney = prefMoney.getInt("ReminingMoney",-100);
    }

    private void setReminingFragment(){
        reminingFragment= ReminingFragment.newInstance(String.valueOf(reminingMoney),"a");
        transaction.replace(R.id.linear_main,reminingFragment);
    }

    private void setSelectFragment(){
        selectTheActionFragment = SelectTheActionFragment.newInstance();
        transaction.add(R.id.linear_main,selectTheActionFragment);
    }

        @Override
    public void onChangeTheFragmentToList(){
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        replaceListFragment();
        transaction.commit();
    }

    @Override
    public void onChangeTheFragmentToSelect(){
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        replaceSelectFragment();
        transaction.commit();
    }

    private void replaceSelectFragment(){
        setReminingFragment();
        setSelectFragment();
    }

    private void replaceListFragment(){
        setReminingFragment();

        prefList = getSharedPreferences("HistoryList", Context.MODE_PRIVATE);
        String json = prefList.getString("List", "");

        listFragment = MyListFragment.newInstance(json,"a");
        transaction.add(R.id.linear_main,listFragment);
    }
}