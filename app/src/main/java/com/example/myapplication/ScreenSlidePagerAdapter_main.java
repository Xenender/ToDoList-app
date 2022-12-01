package com.example.myapplication;

import static java.sql.Types.NULL;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.CalendarView;

import com.example.myapplication.R;

import java.time.Month;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;


public class ScreenSlidePagerAdapter_main extends FragmentStatePagerAdapter {

    //variables globales à definir dans le constructeur
    String[][] tasks;
    int taille;
    int[] tabTaille;

    int[] tabColorbase={R.drawable.layout_main_color_1,R.drawable.layout_main_color_2,R.drawable.layout_main_color_3,R.drawable.layout_main_color_4};
    int[] tabColor;
    public ScreenSlidePagerAdapter_main(FragmentManager fm,String[][] tasks,int taille,int[] tabTaille) {
        super(fm);
        this.tasks = tasks;
        this.taille = taille;
        this.tabTaille = tabTaille;

        this.tabColor = new int[taille];


    }
    @Override
    public Fragment getItem(int position) {

        int j =0;
        for(int i = 0; i<(taille);i++){
            tabColor[i]=tabColorbase[j];
            j++;
            if(j>3) j =0;
        }

        Bundle a = new Bundle();
        fragment_main frag = new fragment_main();
        a.putInt("color",tabColor[position]);
        a.putStringArray("tasks",tasks[position]);
        a.putInt("tailletab",tabTaille[position]);
        frag.setArguments(a);


        position = position +1; //DANS TOUS LES CAS
        return frag;

    }
    @Override
    public int getCount() {
        return taille;
    }


}