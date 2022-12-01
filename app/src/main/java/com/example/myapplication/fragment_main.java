package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;
import java.util.ListIterator;


public class fragment_main extends Fragment {

    FrameLayout frag_main_layout;

    TextView text_frag_main;

    ListView lv_frag_main;

    Context mContext;

    public fragment_main() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {




        View view = inflater.inflate(R.layout.main_fragment, container, false);
        frag_main_layout = (FrameLayout) view.findViewById(R.id.frag_main_layout);
        lv_frag_main = (ListView) view.findViewById(R.id.lv_frag_main);
        text_frag_main = (TextView) view.findViewById(R.id.text_frag_main);



        if(this.getArguments()!=null) {
            String title="erreur";
            Bundle a=getArguments();
            String[] tasks = a.getStringArray("tasks");
            int tabtaille = a.getInt("tailletab");

            String[] newTab=new String[tabtaille],tabIndice=new String[tabtaille],tabSplit;
            long dateD=0;

            for(int i=0; i<tabtaille;i++){
                tabSplit=tasks[i].split(";"); //recup task
                newTab[i] = tabSplit[1];
                tabIndice[i] = tabSplit[0];


                dateD=Long.parseLong(tabSplit[0]); //recup date


            }
            Date date = new Date();
            date.setTime(dateD);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            Calendar adj = Calendar.getInstance(); //date de maintenant


            int day,month,year,day2,month2,year2,dayweek2;
            String jour,mois,annee,dayWeek,chaine;

            day = adj.get(Calendar.DAY_OF_MONTH); //date du jour
            month = adj.get(Calendar.MONTH);
            year = adj.get(Calendar.YEAR);

            day2 = cal.get(Calendar.DAY_OF_MONTH); //date demandée
            month2 = cal.get(Calendar.MONTH);
            year2 = cal.get(Calendar.YEAR);
            dayweek2=cal.get(Calendar.DAY_OF_WEEK);

            if(day == day2 && month == month2 && year == year2){//c'est aujourd'hui
                title = "Aujourd'hui";
            }

            else{
                Date d = new Date();
                d.setTime(adj.getTime().getTime()+86400000); //+1jour
                adj.setTime(d);
                day = adj.get(Calendar.DAY_OF_MONTH); //date du jour
                month = adj.get(Calendar.MONTH);
                year = adj.get(Calendar.YEAR);
                if(day == day2 && month == month2 && year == year2){//c'est Demain
                    title = "Demain";
                }
                else{//cas où c'est pas les 2 premiers jours

                    switch (dayweek2){
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
                    jour = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));

                    switch (month2){
                        case 0:
                            mois="janvier";
                            break;
                        case 1:
                            mois="février";
                            break;
                        case 2:
                            mois="mars";
                            break;
                        case 3:
                            mois="avril";
                            break;
                        case 4:
                            mois="mai";
                            break;
                        case 5:
                            mois="juin";
                            break;
                        case 6:
                            mois="juillet";
                            break;
                        case 7:
                            mois="août";
                            break;
                        case 8:
                            mois="septembre";
                            break;
                        case 9:
                            mois="octobre";
                            break;
                        case 10:
                            mois="novembre";
                            break;
                        case 11:
                            mois="décembre";
                            break;
                        default:mois="erreur";break;
                    }
                    annee = String.valueOf(cal.get(Calendar.YEAR));

                    chaine = dayWeek + " " + jour + " " + mois + "\n" + annee; //ON A LA CHAINE DE LA DATE FORMATEE

                    title = chaine;



                }
            }




            if(newTab.length ==1 && newTab[0].equals("#nothing")){   //Cas ou il n'y a pas de rappels
                newTab = new String[0];
            }





            text_frag_main.setText(title);
            lv_frag_main.setAdapter(new MyAdapter(mContext,newTab,tabIndice));




        }








        if(this.getArguments()!=null) {


            Bundle a = getArguments();
            int color = a.getInt("color");
            frag_main_layout.setBackgroundResource(color);
        }



        text_frag_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("tag","VALUE 1RR1");
            }
        });






        // Inflate the layout for this fragment
        return view;
    }
}