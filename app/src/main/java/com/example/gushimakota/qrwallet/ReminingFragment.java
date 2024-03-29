package com.example.gushimakota.qrwallet;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReminingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReminingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReminingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_MONEY = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mMoney;
    private String mParam2;

//    private OnMyListFragmentInteractionListener mListener;
    public ReminingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReminingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReminingFragment newInstance(String param1, String param2) {
        ReminingFragment fragment = new ReminingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MONEY, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMoney = getArguments().getString(ARG_MONEY);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_remining, container, false);
        TextView moneyText = (TextView)v.findViewById(R.id.money_text);
        moneyText.setText("現在の残高: "+mMoney+"P");
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
//    public void onReturnButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction();
//        }
//    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnMyListFragmentInteractionListener) {
//            mListener = (OnMyListFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnMyListFragmentInteractionListener");
//        }
//    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
//    public interface OnMyListFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction();
//    }
}
