package com.example.myapplication;

import static java.sql.Types.NULL;

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


public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

    int annee,mois,jour,js;
    Calendar calendarr;



    public ScreenSlidePagerAdapter(FragmentManager fm, Calendar calendarr) {
        super(fm);
        this.calendarr = calendarr;


    }
    @Override
    public Fragment getItem(int position) {



        fragment_ajout frag = new fragment_ajout();
        Bundle a = new Bundle();

        if(calendarr == null) {

            Calendar calendarr2 = Calendar.getInstance();
            Date actu = calendarr2.getTime();

            Date[] tabDate;
            tabDate = datePlusN(actu,30); // On a un tableau avec toutes les dates des 30 prochains jours, on va filtrer le tableau pour le rendre plus joli

            String[] tabJours = new String[tabDate.length];

            for(int i=0;i<tabDate.length;i++){

                switch (i){
                    case 0:
                        tabJours[0] = "Aujourd'hui";
                        break;
                    case 1:
                        tabJours[1] = "Demain";
                        break;

                    default:
                        String dayWeek,day,month,year,chaine;
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(tabDate[i]);
                        int jour = cal.get(Calendar.DAY_OF_WEEK);
                        switch (jour){
                            case 1:
                                dayWeek="Dimanche";
                                break;
                            case 2:
                                dayWeek="Lundi";
                                break;
                            case 3:
                                dayWeek="Mardi";
                                break;
                            case 4:
                                dayWeek="Mercredi";
                                break;
                            case 5:
                                dayWeek="Jeudi";
                                break;
                            case 6:
                                dayWeek="Vendredi";
                                break;
                            case 7:
                                dayWeek="Samedi";
                                break;

                            default:dayWeek="erreur";break;
                        }
                        day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));

                        int mois = cal.get(Calendar.MONTH);
                        switch (mois){
                            case 0:
                                month="janvier";
                                break;
                            case 1:
                                month="février";
                                break;
                            case 2:
                                month="mars";
                                break;
                            case 3:
                                month="avril";
                                break;
                            case 4:
                                month="mai";
                                break;
                            case 5:
                                month="juin";
                                break;
                            case 6:
                                month="juillet";
                                break;
                            case 7:
                                month="août";
                                break;
                            case 8:
                                month="septembre";
                                break;
                            case 9:
                                month="octobre";
                                break;
                            case 10:
                                month="novembre";
                                break;
                            case 11:
                                month="décembre";
                                break;
                            default:month="erreur";break;
                        }
                        year = String.valueOf(cal.get(Calendar.YEAR));

                        chaine = dayWeek + " " + day + " " + month + "\n" + year;
                        tabJours[i] = chaine;


                }

                }


            a.putString("key", tabJours[position]);
            frag.setArguments(a);
        }
        else{
            //Cas où on change la date par le calendrier

            Date actu = calendarr.getTime();
            Date[] tabDateM,tabDateP,concatenate;
            tabDateM = dateMoinsN(actu,15); // On a un tableau avec toutes les dates des 15 précédents jours, on va filtrer le tableau pour le rendre plus joli
            tabDateM = reverse(tabDateM, tabDateM.length);
            tabDateP = datePlusN(actu,15);// On a un tableau avec toutes les dates des 15 prochains jours, on va filtrer le tableau pour le rendre plus joli

            concatenate = Arrays.copyOf(tabDateM, tabDateM.length + tabDateP.length);
            System.arraycopy(tabDateP, 0, concatenate, tabDateM.length, tabDateP.length);

            // Concatenate = les 15 précédents et prochains


            String[] tabJours = new String[concatenate.length];

            for(int i=0;i<concatenate.length;i++){
                String dayWeek,day,month,year,chaine;
                Calendar cal = Calendar.getInstance();
                cal.setTime(concatenate[i]);
                int jour = cal.get(Calendar.DAY_OF_WEEK);
                switch (jour){
                    case 1:
                        dayWeek="Dimanche";
                        break;
                    case 2:
                        dayWeek="Lundi";
                        break;
                    case 3:
                        dayWeek="Mardi";
                        break;
                    case 4:
                        dayWeek="Mercredi";
                        break;
                    case 5:
                        dayWeek="Jeudi";
                        break;
                    case 6:
                        dayWeek="Vendredi";
                        break;
                    case 7:
                        dayWeek="Samedi";
                        break;

                    default:dayWeek="erreur";break;
                }
                day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));

                int mois = cal.get(Calendar.MONTH);
                switch (mois){
                    case 0:
                        month="janvier";
                        break;
                    case 1:
                        month="février";
                        break;
                    case 2:
                        month="mars";
                        break;
                    case 3:
                        month="avril";
                        break;
                    case 4:
                        month="mai";
                        break;
                    case 5:
                        month="juin";
                        break;
                    case 6:
                        month="juillet";
                        break;
                    case 7:
                        month="août";
                        break;
                    case 8:
                        month="septembre";
                        break;
                    case 9:
                        month="octobre";
                        break;
                    case 10:
                        month="novembre";
                        break;
                    case 11:
                        month="décembre";
                        break;
                    default:month="erreur";break;
                }
                year = String.valueOf(cal.get(Calendar.YEAR));

                chaine = dayWeek + " " + day + " " + month + "\n" + year;
                tabJours[i] = chaine;

            }
            a.putString("key", tabJours[position]);


            frag.setArguments(a);


        }



        position = position +1; //DANS TOUS LES CAS
        return frag;

    }
    @Override
    public int getCount() {
        return 30;
    }


    Date[] dateMoinsN(Date date,int n){
        long jour = 24 * 60 * 60 * 1000;
        Date[] tab = new Date[n];
        for(int i=0;i<n;i++){
            Date nouvelle = new Date();
            long diff = date.getTime() - (i+1)*jour;
            nouvelle.setTime(diff);
            tab[i] = nouvelle;

        }
        return tab;
    }

    Date[] datePlusN(Date date,int n){
        long jour = 24 * 60 * 60 * 1000;
        Date[] tab = new Date[n];
        for(int i=0;i<n;i++){
            Date nouvelle = new Date();
            long diff = date.getTime() + (i)*jour;
            nouvelle.setTime(diff);
            tab[i] = nouvelle;

        }


        return tab;
    }

    static Date[] reverse(Date a[], int n) {
        Date[] b = new Date[n];
        int j = n;
        for (int i = 0; i < n; i++) {
            b[j - 1] = a[i];
            j = j - 1;
        }
        return  b;
    }



}