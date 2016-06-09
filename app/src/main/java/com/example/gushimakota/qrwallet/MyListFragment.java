package com.example.gushimakota.qrwallet;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnMyListFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_LIST_JSONSTRING = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String jsonString;
    private String mParam2;

    private OnMyListFragmentInteractionListener mListener;

    private Button returnSelect;
    private ListView historyList;
    private SharedPreferences prefList;
    private List<String> strings;
    private ItemsMap itemsMap;
    List<ItemDataForMyList> list;

    public MyListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyListFragment newInstance(String param1, String param2) {
        MyListFragment fragment = new MyListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LIST_JSONSTRING, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            jsonString = getArguments().getString(ARG_LIST_JSONSTRING);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_my_list, container, false);
        returnSelect = (Button)v.findViewById(R.id.return_select_fragment);
        historyList = (ListView)v.findViewById(R.id.history_list);
        itemsMap = new ItemsMap();
        openJson();
        setListView(v);
        returnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onReturnButtonPressed();
            }
        });
        return v;
    }

    private void openJson(){
        // JSON形式を配列に保存する
        Gson gson = new Gson();
        strings = gson.fromJson(jsonString, ArrayList.class);
    }

    private void setListView(View v){
        list = new ArrayList<ItemDataForMyList>();
        int i=0;
        for(String item : strings){
            ItemDataForMyList itemDataForMyList = new ItemDataForMyList();
            itemDataForMyList.setItemName(item);
            itemDataForMyList.setItemPrice(itemsMap.checkThePrice(item));
            list.add(i, itemDataForMyList);
            i++;
        }
        MyListAdapter adapter = new MyListAdapter(getContext(),0,list);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_expandable_list_item_1, strings);
        historyList.setEmptyView(v.findViewById(R.id.emptyView));
        historyList.setAdapter(adapter);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onReturnButtonPressed() {
        if (mListener != null) {
            mListener.onChangeTheFragmentToSelect();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMyListFragmentInteractionListener) {
            mListener = (OnMyListFragmentInteractionListener) context;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnMyListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onChangeTheFragmentToSelect();
    }
}
