package com.example.gushimakota.qrwallet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class SelectTheActionFragment extends Fragment {


    private Button mBuyingButton;
    private Button mListButton;

    private OnSelectTheActionFragmentInteractionListener mListener;

    public SelectTheActionFragment() {
        // Required empty public constructor
    }

    public static SelectTheActionFragment newInstance(String param1, String param2) {
        SelectTheActionFragment fragment = new SelectTheActionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_select_the_action, container, false);
        mBuyingButton = (Button)v.findViewById(R.id.buy_button);
        mBuyingButton.setText("購入画面へ");
        mListButton = (Button)v.findViewById(R.id.list_button);
        mListButton.setText("履歴");
        mBuyingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToQRActivity();
            }
        });
        mListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onListButtonPressed();
            }
        });
        return v;
    }

    public void onListButtonPressed() {
        if (mListener != null) {
            mListener.onChangeTheFragmentToList();
        }
    }

    private void goToQRActivity(){
        Intent intent = new Intent(getContext(),com.example.gushimakota.qrwallet.QRActivity.class);
        startActivity(intent);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSelectTheActionFragmentInteractionListener) {
            mListener = (OnSelectTheActionFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMyListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnSelectTheActionFragmentInteractionListener {
        void onChangeTheFragmentToList();
    }
}
