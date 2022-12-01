package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MyAdapter extends ArrayAdapter<String>{

    //Classe adapter qui permet de remplir la listview avec tous les paramètres nécessaires

    String [] taches,indices;

    Context mContext;


    private SparseBooleanArray mCheckStates; //sauvegarder le cochage des chekbox

    public MyAdapter(@NonNull Context context, String [] listTaches,String[] idTaches) {
        super(context, R.layout.customlv);
        this.taches = listTaches;
        this.mContext = context;
        this.indices = idTaches;
        mCheckStates = new SparseBooleanArray(listTaches.length);

    }


    public boolean isChecked(int position) {
        return mCheckStates.get(position, false);
    }

    public void setChecked(int position, boolean isChecked) {
        mCheckStates.put(position, isChecked);
    }

    public void toggle(int position) {
        setChecked(position, !isChecked(position));

    }


    @Override
    public int getCount() {
        return taches.length;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder mViewHolder = new ViewHolder();



        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.customlv, parent, false);
            mViewHolder.mTache = (TextView) convertView.findViewById(R.id.tache);
            mViewHolder.mCheckBox = (CheckBox) convertView.findViewById(R.id.checkbox);

            mViewHolder.mIndice = new String[taches.length]; // erreur ici ???????????????????? (au cas où)

            convertView.setTag(mViewHolder);
        }else {mViewHolder = (ViewHolder) convertView.getTag();

        }

        mViewHolder.mTache.setText(taches[position]);

        mViewHolder.mCheckBox.setTag(position);

        mViewHolder.mIndice[position] = indices[position]; // cette variable contient les indices de chaque item dans les shared preferences


        ViewHolder finalMViewHolder = mViewHolder;
        mViewHolder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                checkTache(b,finalMViewHolder,finalMViewHolder.mIndice[position]);

            }
        });

        mViewHolder.mTache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                finalMViewHolder.mCheckBox.setChecked(!finalMViewHolder.mCheckBox.isChecked());




                checkTache(finalMViewHolder.mCheckBox.isChecked(),finalMViewHolder,finalMViewHolder.mIndice[position]);

            }
        });

        mViewHolder.mCheckBox.setChecked(mCheckStates.get(position, false));

        SharedPreferences pref = getContext().getSharedPreferences("saveCheckBox",0);// SAVE POUR QUE ça GARDE LE CHECK EN CHANGEANT DE FRAGMENT

        mViewHolder.mCheckBox.setChecked(pref.getBoolean(mViewHolder.mIndice[position],false));





        return convertView;
    }



    static class ViewHolder{
        TextView mTache;
        CheckBox mCheckBox;
        String[] mIndice;
    }

    void checkTache(Boolean b,ViewHolder finalMViewHolder,String element){
        String tag = finalMViewHolder.mIndice[(int)finalMViewHolder.mCheckBox.getTag()];

        if(b){ //Si on coche une case
            Log.i("tag","VALUE  "+ finalMViewHolder.mCheckBox.getTag().toString());
            Log.i("tag","VALUE  "+ tag);

            mCheckStates.put((Integer) finalMViewHolder.mCheckBox.getTag(), b);

            SharedPreferences sharedPreferences = getContext().getSharedPreferences("fileTask",0);  //ON SUPPRIME LA TACHE
            SharedPreferences.Editor editor =sharedPreferences.edit();
            editor.remove(tag);
            editor.apply();

        }
        else{
            mCheckStates.put((Integer) finalMViewHolder.mCheckBox.getTag(), b);
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("fileTask",0);  //ON REMET LA TACHE
            SharedPreferences.Editor editor =sharedPreferences.edit();
            editor.putString(tag,finalMViewHolder.mTache.getText().toString());
            editor.apply();

        }
        SharedPreferences pref = getContext().getSharedPreferences("saveCheckBox",0);  //ON REMET LA TACHE
        SharedPreferences.Editor ed =pref.edit();
        ed.putBoolean(element,b);
        ed.apply();
    }
}
