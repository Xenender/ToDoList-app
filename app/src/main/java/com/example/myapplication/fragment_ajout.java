package com.example.myapplication;

import static android.content.Context.MODE_PRIVATE;
import static android.support.constraint.Constraints.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class fragment_ajout extends Fragment {


    TextView t2;
    // TODO: Rename and change types of parameters

    public fragment_ajout() {
        // Required empty public constructor

        //Bundle a=new Bundle();
        //setArguments(a);
        //a.getString("str");
        //Argument pour la date : i i1 i2 // page favori
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.ajout_fragment, container, false);


        t2 = (TextView)view.findViewById(R.id.tache_tv2);


        //SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("sharedVariables", Context.MODE_PRIVATE);

        //float size = sharedPreferences.getFloat("textSize",22);

        //t2.setTextSize(size);


        if(this.getArguments()!=null){


            Bundle a = getArguments();
            String mes = a.getString("key");
            t2.setText(mes);



        }




                // Inflate the layout for this fragment
        return view;
    }


}