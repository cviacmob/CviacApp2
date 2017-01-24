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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cviac.com.cviac.app.adapaters.AnnoncementAdapter;
import com.cviac.com.cviac.app.adapaters.EventsAdapter;
import com.cviac.com.cviac.app.datamodels.Annoncements;
import com.cviac.com.cviac.app.datamodels.EventInfo;

import java.util.List;

public class AnnoncementActivity extends Activity {
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
                alertforivite();
            }
        });
    }

    private List<Annoncements> getAnno() {
        return Annoncements.getAnnoncements();
    }


    private void alertforivite() {
        final Dialog dialog = new Dialog(getApplicationContext());
        dialog.setContentView(R.layout.custom);
        Annoncements ans = new Annoncements();
        TextView text = (TextView) dialog.findViewById(R.id.text);
        String mgs = ans.getAnnoncemsg();
        text.setText(mgs);
        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }
}
