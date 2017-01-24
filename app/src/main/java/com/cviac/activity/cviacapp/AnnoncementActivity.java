package com.cviac.activity.cviacapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cviac.com.cviac.app.adapaters.AnnoncementAdapter;

import com.cviac.com.cviac.app.datamodels.Annoncements;
import com.cviac.com.cviac.app.datamodels.Employee;


import java.util.List;

public class AnnoncementActivity extends AppCompatActivity {
    ListView lv;
    List<Annoncements> anno;
    AnnoncementAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annoncement);
        lv = (ListView) findViewById(R.id.annoncelist);
        anno = getAnno();
        adapter = new AnnoncementAdapter(anno, getApplicationContext());
        lv.setAdapter(adapter);

       lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               //String br=(String)lv.getItemAtPosition(position);
               Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
                       Toast.LENGTH_SHORT).show();
           }
       });
    }

    private List<Annoncements> getAnno() {
        return Annoncements.getAnnoncements();
    }


    private void alertforivite(String postion) {

     Annoncements emp = new Annoncements();
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        TextView tv=new TextView(this);
        builder1.setView(tv);
        tv.setText(postion);

       // builder1.setMessage(ans.getAnnoncemsg());
        builder1.setCancelable(true);



        builder1.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }
}
