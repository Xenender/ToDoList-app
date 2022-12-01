package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Console;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    //USE SHARRED PREFERENCE POUR SAUVEGARDER TAILLE ET LA RECUPERER DANS LE FRAGMENT (par default 22) ...


    /*
    sauvegarde des taches :
        -int n : nombre de tache
        -string pour chaque tache
        le nom de la sauvegarde devra être bien organisé : 14/06/22/i   i=compteur    sharedpreferences.getall()


        Map<String, ?> allEntries = prefA.getAll();
for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
    Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
}




ETAPE :


recup id checkbox pour supprimer item à la bonne id et ajouter shared avec id différente à chaque fois (+1ms)
    */

    TextView tache_tv1,tache_tv_swipe;
    Button button_Qajout;
    CalendarView calendar;
    EditText ajout_editText;

    ScreenSlidePagerAdapter adapter;

    ViewPager pager_ajout;
    ViewPager pager_main;
    ImageView btnCalendar,tache_switch_left,tache_switch_right,tache_arrow_down,open_tache,open_tools,tache_mic;
    RelativeLayout layout_quit_calendar;
    Guideline tache_gd_7, tache_gd_6;
    Button btn_save;
    ConstraintLayout layout_ajout,layout_tools,tache_layoutEditText;
    ViewGroup transitionsContainer;

    Boolean keyboard_open;

    int pagerActualItem;
    boolean noEffectSlide;

    Calendar calendarSave;

    SharedPreferences sharedPreferences;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //MAIN variables









        //AJOUT variables

        button_Qajout = findViewById(R.id.button_Qajout);
        layout_ajout = findViewById(R.id.layout_ajout);
        layout_tools = findViewById(R.id.layout_tools);
        tache_layoutEditText = findViewById(R.id.tache_layoutEditText);

        tache_tv_swipe = findViewById(R.id.tache_tv_swipe);

        calendar = findViewById(R.id.calendar);
        ajout_editText = findViewById(R.id.ajout_editText);
        btnCalendar = findViewById(R.id.btnCalendar);
        tache_switch_left=findViewById(R.id.tache_switch_left);
        tache_switch_right=findViewById(R.id.tache_switch_right);
        tache_arrow_down = findViewById(R.id.tache_arrow_down);
        tache_mic = findViewById(R.id.tache_mic);

        open_tache = findViewById(R.id.open_tache);
        open_tools = findViewById(R.id.open_tools);
        layout_quit_calendar = findViewById(R.id.layout_quit_calendar);


        tache_gd_7 = findViewById(R.id.tache_gd_7);
        tache_gd_6 = findViewById(R.id.tache_gd_6);

        tache_tv1 = findViewById(R.id.tache_tv1);

        btn_save = findViewById(R.id.btn_save);

        transitionsContainer = findViewById(R.id.root);

        keyboard_open = false;

        pager_ajout =findViewById(R.id.pager);
        adapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(),null);
        pager_ajout.setAdapter(adapter);

        pagerActualItem=0;

        noEffectSlide = false;


        //SharedPreferences sharedPreferences = getSharedPreferences("sharedVariables",MODE_PRIVATE);
        //editor = sharedPreferences.edit();

        calendarSave = null;


        calendar.setVisibility(CalendarView.INVISIBLE); //Pas afficher direct mais on le fait charger

        final View activityRootView = findViewById(R.id.root);









        //LAYOUT MAIN

        ArrayList<String> tabLstString;

        pager_main = findViewById(R.id.pager_main);


        sharedPreferences = getSharedPreferences("fileTask",MODE_PRIVATE);  //recuperation taches
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //editor.clear();
        //editor.apply();


        calendar.setMinDate(calendar.getDate()-1000);

        pager_main.setClipToPadding(false);
        pager_main.setPadding(50,0,50,0);
        pager_main.setPageMargin(20);


        GetPrefAndFormat();



        //ScreenSlidePagerAdapter_main adapter_main = new ScreenSlidePagerAdapter_main(getSupportFragmentManager());
        //pager_main.setAdapter(adapter_main);



        open_tache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Transition slide = new Slide(Gravity.BOTTOM);
                slide.addTarget(layout_ajout);
                slide.setDuration(200);
                TransitionManager.beginDelayedTransition(transitionsContainer, slide);

                button_Qajout.setVisibility(Button.VISIBLE);
                layout_ajout.setVisibility(ConstraintLayout.VISIBLE);

            }
        });


        open_tools.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Transition slide = new Slide(Gravity.BOTTOM);
                slide.addTarget(layout_tools);
                slide.setDuration(200);
                TransitionManager.beginDelayedTransition(transitionsContainer, slide);

                button_Qajout.setVisibility(Button.VISIBLE);
                layout_tools.setVisibility(ConstraintLayout.VISIBLE);
            }
        });























        //LAYOUT TACHE

        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {


                if(keyboard_open){
                    tache_tv1.setVisibility(TextView.VISIBLE); //ON REMET LA VISIBILITE EN QUITTANT LE CLAVIER
                    btnCalendar.setVisibility(ImageView.VISIBLE);
                    tache_gd_6.setGuidelinePercent(.40f);
                    keyboard_open = false;
                }


                Rect r = new Rect();
                //r will be populated with the coordinates of your view that area still visible.
                activityRootView.getWindowVisibleDisplayFrame(r);

                int heightDiff = activityRootView.getRootView().getHeight() - r.height();
                if (heightDiff > 0.25*activityRootView.getRootView().getHeight()) { // if more than 25% of the screen, its probably a keyboard... //KEYBOAAAAAAARDD



                    tache_tv1.setVisibility(TextView.GONE); // invisible
                    tache_gd_6.setGuidelinePercent(.32f);
                    btnCalendar.setVisibility(ImageView.GONE);
                    keyboard_open=true;

                }
            }
        });


        pager_ajout.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if(!noEffectSlide) {

                    if(i>pagerActualItem){ //swipe right
                        calendar.setDate(calendar.getDate() + 86400000); //+ 1 jour
                        pagerActualItem++;
                    }
                    else if(i<pagerActualItem){ //swipe left
                        calendar.setDate(calendar.getDate() - 86400000);
                        pagerActualItem--;
                    }

                }
                noEffectSlide = false;
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!keyboard_open) { //SEULEMENT SI LE CLAVIER N'EST PAS OUVERT
                    if (calendar.getVisibility() == CalendarView.INVISIBLE) {
                        calendar.setVisibility(CalendarView.VISIBLE);
                        ajout_editText.setVisibility(EditText.GONE);
                        btn_save.setVisibility(Button.GONE);
                        btnCalendar.setVisibility(ImageView.GONE);
                        tache_arrow_down.setVisibility(ImageView.GONE);
                        tache_tv_swipe.setVisibility(TextView.GONE);
                        tache_layoutEditText.setVisibility(ConstraintLayout.GONE);
                        tache_mic.setVisibility(ImageView.GONE);
                    }
                }

            }
        });


        pager_ajout.setOnTouchListener(new View.OnTouchListener() {
            private int CLICK_ACTION_THRESHOLD = 200;
            private float startX;
            private float startY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                        case MotionEvent.ACTION_UP:
                            float endX = event.getX();
                            float endY = event.getY();
                            if (isAClick(startX, endX, startY, endY)) {// WE HAVE A CLICK!!


                                if (calendar.getVisibility() == CalendarView.VISIBLE) { // ON FERME LE CALENDRIER
                                    quit_calendar();
                                }

                            }
                            break;
                }
                v.getParent().requestDisallowInterceptTouchEvent(true); //specific to my project
                return false; //specific to my project
            }
            private boolean isAClick(float startX, float endX, float startY, float endY) {
                float differenceX = Math.abs(startX - endX);
                float differenceY = Math.abs(startY - endY);
                return !(differenceX > CLICK_ACTION_THRESHOLD || differenceY > CLICK_ACTION_THRESHOLD);
            }
        });

        layout_quit_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quit_calendar();
            }
        });

        tache_switch_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pager_ajout.arrowScroll(View.FOCUS_LEFT);
            }
        });
        tache_switch_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pager_ajout.arrowScroll(View.FOCUS_RIGHT);
            }
        });



        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int annee, int mois, int jour) {

                Calendar calendarr = Calendar.getInstance();
                calendarr.set(annee, mois, jour);

                calendarSave = Calendar.getInstance();
                calendarSave.set(annee,mois,jour);

                int dayOfWeek = calendarr.get(Calendar.DAY_OF_WEEK);


                ScreenSlidePagerAdapter adapter;
                adapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(),calendarr);
                pager_ajout.setAdapter(adapter);


                noEffectSlide = true; //pour pas passer 2 fois
                pager_ajout.setCurrentItem(14);
                noEffectSlide = true; //pour pas passer 2 fois

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pager_ajout.arrowScroll(View.FOCUS_RIGHT);

                        pagerActualItem =15;   //le 15eme item   pour detecter swipe gauche ou droit dans le pagechange listener

                        Calendar calendar2 = Calendar.getInstance();
                        calendar2.set(Calendar.DAY_OF_MONTH, jour);
                        calendar2.set(Calendar.MONTH, mois);
                        calendar2.set(Calendar.YEAR, annee);
                        calendar.setDate(calendar2.getTimeInMillis());


                    }
                }, 1);

                //On ferme le calendrier :

                quit_calendar();




                //calendar.setVisibility(CalendarView.INVISIBLE);
                //ajout_editText.setVisibility(EditText.VISIBLE);
            }
        });

        //quit ajout



        layout_ajout.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return onTouchEvent(motionEvent);
            }
        });
        layout_tools.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return onTouchEvent(motionEvent);
            }
        });



        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!ajout_editText.getText().toString().equals("")) {
                    String tache = (ajout_editText.getText()).toString();
                    long date = calendar.getDate();

                    ajout_editText.setText("");

                    if(keyboard_open) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(MainActivity.INPUT_METHOD_SERVICE); //close keyboard
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                    }


                    add_task(tache, date);

                }
            }
        });

        button_Qajout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(layout_ajout.getVisibility()==ConstraintLayout.VISIBLE){
                    closeAjout();
                }
                else if(layout_tools.getVisibility() == ConstraintLayout.VISIBLE){
                    closeTools();
                }
            }
        });

        tache_mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speak();
            }
        });



    }

    void quit_calendar(){
        calendar.setVisibility(CalendarView.INVISIBLE);
        ajout_editText.setVisibility(EditText.VISIBLE);
        btn_save.setVisibility(Button.VISIBLE);
        btnCalendar.setVisibility(ImageView.VISIBLE);
        tache_arrow_down.setVisibility(ImageView.VISIBLE);
        tache_tv_swipe.setVisibility(TextView.VISIBLE);
        tache_layoutEditText.setVisibility(ConstraintLayout.VISIBLE);
        tache_mic.setVisibility(ImageView.VISIBLE);
    }

    float firstX_point, firstY_point;
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();

        switch (action) {

            case MotionEvent.ACTION_DOWN:
                firstX_point = event.getRawX();
                firstY_point = event.getRawY();
                break;

            case MotionEvent.ACTION_UP:

                float finalX = event.getRawX();
                float finalY = event.getRawY();
                int distanceX = (int) (finalX - firstX_point);
                int distanceY = (int) (finalY - firstY_point);

                if (Math.abs(distanceX) > Math.abs(distanceY)) {
                    if ((firstX_point < finalX)) {
                        //Log.d("Test", "Left to Right swipe performed");


                    } else {

                        //Log.d("Test", "Right to Left swipe performed");

                    }
                }else{
                    if ((firstY_point < finalY)) {
                        //Log.d("Test", "Up to Down swipe performed");

                        if(layout_ajout.getVisibility()==ConstraintLayout.VISIBLE) closeAjout();
                        else if(layout_tools.getVisibility()==ConstraintLayout.VISIBLE) closeTools();
                    } else {

                        //Log.d("Test", "Down to Up swipe performed");

                    }
                }


                break;
        }

        return true;
    }

    void closeAjout(){

        Transition slide = new Slide(Gravity.BOTTOM);
        slide.addTarget(layout_ajout);
        slide.setDuration(200);
        TransitionManager.beginDelayedTransition(transitionsContainer, slide);

        layout_ajout.setVisibility(ConstraintLayout.GONE);




        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                button_Qajout.setVisibility(Button.GONE);
                if(keyboard_open){
                    InputMethodManager imm = (InputMethodManager) getSystemService(MainActivity.INPUT_METHOD_SERVICE); //close keyboard
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }
            }
        }, 160);



    }

    void closeTools(){

        Transition slide = new Slide(Gravity.BOTTOM);
        slide.addTarget(layout_tools);
        slide.setDuration(200);
        TransitionManager.beginDelayedTransition(transitionsContainer, slide);

        layout_tools.setVisibility(ConstraintLayout.GONE);




        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                button_Qajout.setVisibility(Button.GONE);
                if(keyboard_open){
                    InputMethodManager imm = (InputMethodManager) getSystemService(MainActivity.INPUT_METHOD_SERVICE); //close keyboard
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }
            }
        }, 160);



    }


    void add_task(String task, long date){


        sharedPreferences = getSharedPreferences("fileTask",MODE_PRIVATE);  //recuperation taches
        SharedPreferences.Editor editor =sharedPreferences.edit();

        if(sharedPreferences.getString(String.valueOf(date),"null") != "null"){
            add_task(task,(1+date));
        }
        else {
            editor.putString(String.valueOf(date),task);
            editor.apply();
        }


        GetPrefAndFormat();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this,"tâche ajoutée",Toast.LENGTH_SHORT).show();

            }
        }, 100);




    }

    void speak(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Que voulez-vous enregistrer ?");

        try{
            startActivityForResult(intent,1000);
        }
        catch (Exception e){
            Toast.makeText(this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        switch (requestCode){
            case 1000:{
                if(resultCode == RESULT_OK && null != data){
                    ArrayList<String>result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    ajout_editText.setText(result.get(0));
                }
                break;
            }
        }
    }


    void GetPrefAndFormat(){
        sharedPreferences = getSharedPreferences("fileTask",MODE_PRIVATE);  //recuperation taches

        Map<String, ?> preferences = sharedPreferences.getAll();

        //CAS OU IL N'Y A AUCN RAPPEL
        if(preferences.isEmpty()){
            //String[] tasks = {"23/05/2022;rien;Aujourd'hui","24/05/2022;rien;Demain","25/05/2022;rien;Après Demain"};
            SimpleDateFormat dateFormat;
            String date;
            Calendar today = Calendar.getInstance();
            dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            date = dateFormat.format(today.getTime());

            String[][] tasks = {{(today.getTime().getTime())+";#nothing"},{(today.getTime().getTime()+24*60*60*1000)+";#nothing"},{(today.getTime().getTime()+(24*60*60*1000)*2)+";#nothing"},{(today.getTime().getTime()+(24*60*60*1000)*3)+";#nothing"}};
            ScreenSlidePagerAdapter_main adapter_main = new ScreenSlidePagerAdapter_main(getSupportFragmentManager(),tasks,4,tabTaille(tasks,4));
            pager_main.setAdapter(adapter_main);
        }


        //CAS RAPPELS !
        else{
            //parcour de la hashmap

            int n=0; //taille tab
            String[][] tasks = new String[preferences.size()][preferences.size()];
            long timekey;
            String taskvalue;
            int day,month,year,day2,month2,year2;




            for(Map.Entry mapentry : preferences.entrySet()) { //mapentry.getKey / .getValue   //POUR CHAQUE TASK


                timekey = Long.parseLong((String) mapentry.getKey());
                taskvalue = (String) mapentry.getValue();


                Date date = new Date(); //date 1                   //ON PREND SA DATE
                date.setTime(timekey);
                Calendar cal1 = Calendar.getInstance();
                cal1.setTime(date);
                day = cal1.get(Calendar.DAY_OF_MONTH); //date du jour
                month = cal1.get(Calendar.MONTH);
                year = cal1.get(Calendar.YEAR);



                if(n==0){//Si tab vide on ajoute la tache
                    String concat = timekey +";"+taskvalue;
                    tasks[n][0] = concat;
                    n++;
                    Log.i("tag","VALUE 1" );
                }
                else{


                    int long_tab = n;
                    boolean fragment_present = false;

                    for(int x=0; x<long_tab;x++){  //POUR CHAQUE TASKS DEJA ENREGISTREE


                        String[] a = tasks[x][0].split(";");


                        Date date2 = new Date(); //date 2
                        date2.setTime(Long.parseLong(a[0]));
                        Calendar cal2 = Calendar.getInstance();
                        cal2.setTime(date2);

                        day2 = cal2.get(Calendar.DAY_OF_MONTH); //date demandée
                        month2 = cal2.get(Calendar.MONTH);
                        year2 = cal2.get(Calendar.YEAR);


                        if((day==day2&&month==month2&&year==year2)){ //Si meme date mais pas meme valeur
                            String concat = timekey +";"+taskvalue;
                            tasks[x][tailleTab(tasks[x])] = concat;
                            Log.i("tag","VALUE 2" );
                            fragment_present=true;
                            break;

                        }


                    }
                    if(!fragment_present){
                        String concat = timekey +";"+taskvalue;
                        tasks[n][0] = concat;
                        Log.i("tag","VALUE 3" );
                        n++;

                    }




                }






            }



           // Log.i("TAG","VALUE  tab1 "+ Arrays.deepToString(tasks));
           // Log.i("TAG","VALUE  tab1.length    "+String.valueOf(tasks.length));
           // Log.i("TAG","VALUE  n    "+String.valueOf(n));
           // Log.i("TAG","VALUE  pref.length    "+String.valueOf(preferences.size()));

            Log.i("tag","VALUE   "+ Arrays.deepToString(tasks));


            tasks = trierTab(tasks,n);


            Log.i("tag","VALUE TRIE  "+ Arrays.deepToString(tasks));
            ScreenSlidePagerAdapter_main adapter_main = new ScreenSlidePagerAdapter_main(getSupportFragmentManager(),tasks,n,tabTaille(tasks,n));
            pager_main.setAdapter(adapter_main);



        }




    }
    int tailleTab(String[] tab){

        int counter = 0;
        for (int i = 0; i < tab.length; i ++) {
            if (tab[i] != null) counter++;
        }
        return counter;
    }
    boolean presenceTab(String[]tab,String chaine){
        String[] format;
        for(int i =0;i<tailleTab(tab);i++){
            format = tab[i].split(";");

            if(format[1]==chaine){
                return true;
            }
        }
        return false;

    }

    int[] tabTaille(String[][] tab,int n){

        int[] tailles = new int[tab.length];

        for(int i =0; i<n; i++){
            tailles[i] = tailleTab(tab[i]);


        }
        return tailles;

    }

    String[][] trierTab(String[][] arr,int taille){

        /*
        long date1,date2;
        for (int i = 0; i < taille; i++) {
            date1 = Long.parseLong(arr[i][0].split(";")[0]);
            for (int j = 0; j < taille; j++) {
                date2 = Long.parseLong(arr[j][0].split(";")[0]);
                if (date1 < date2) {
                    String[] temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }
        }
        return arr;

         */

        long date1,date2;
        for (int i = 0; i < taille - 1; i++)
        {
            int index = i;

            for (int j = i + 1; j < taille; j++)
            {
                date1 = Long.parseLong(arr[index][0].split(";")[0]);
                date2 = Long.parseLong(arr[j][0].split(";")[0]);
                if (date2 < date1){
                    index = j;
                }
            }

            String[] min = arr[index];
            arr[index] = arr[i];
            arr[i] = min;
        }
        return arr;

    }

}

/*
for(Map.Entry mapentry2 : preferences.entrySet()) {
                    long timekey2 = Long.parseLong((String) mapentry.getKey());

                    Date date = new Date(); //date 1
                    date.setTime(timekey);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);

                    Date date2 = new Date(); //date 2
                    date.setTime(timekey2);
                    Calendar cal2 = Calendar.getInstance();
                    cal2.setTime(date2);

                    //on va comparer les dates pour savoir si elles doivent être dans le même tableau

                    int day,month,year,day2,month2,year2;

                    day = cal.get(Calendar.DAY_OF_MONTH); //date du jour
                    month = cal.get(Calendar.MONTH);
                    year = cal.get(Calendar.YEAR);

                    day2 = cal2.get(Calendar.DAY_OF_MONTH); //date demandée
                    month2 = cal2.get(Calendar.MONTH);
                    year2 = cal2.get(Calendar.YEAR);

                    boolean contains = Arrays.stream(tasks[i]).anyMatch("s"::equals);
                    if(!contains && (day==day2 && month == month2 && year == year2)){ //Si c'est la meme date et qu'il n'est pas déjà dedans
                        String concat2 = String.valueOf(timekey2)+mapentry2.getValue();
                        tasks[i][j] =concat2;
                        j++;
                    }



                }
 */