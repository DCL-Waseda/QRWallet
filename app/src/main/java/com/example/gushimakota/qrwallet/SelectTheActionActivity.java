package com.example.gushimakota.qrwallet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SelectTheActionActivity extends AppCompatActivity {

    private Button mBuyingButton;
    private Button mListButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_the_action);
        mBuyingButton = (Button)findViewById(R.id.buy_button);
        mListButton = (Button)findViewById(R.id.list_button);
        mBuyingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectTheActionActivity.this, com.example.gushimakota.qrwallet.QRActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
