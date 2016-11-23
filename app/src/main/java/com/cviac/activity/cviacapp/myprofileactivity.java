package com.cviac.activity.cviacapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Bitmap;


import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.cviac.datamodel.cviacapp.Employee;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class myprofileactivity extends AppCompatActivity {

    TextView tvempid, tvempname,tvemail,tvmobile,tvgender,tvdob,tvmanager,tvdepartment,tvdesignation;
    final Context context = this;
ImageView imageViewRound;

    private int REQUEST_CAMERA = 2, SELECT_FILE = 1;
    private ImageButton btnSelect;
    private ImageView ivImage;
    private String userChoosenTask;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    // private EditText result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myprofile);
       // imageViewRound =(ImageView)findViewById(R.id.imageViewprofile) ;
        btnSelect = (ImageButton) findViewById(R.id.imageButtonselect);
        btnSelect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        ivImage = (ImageView) findViewById(R.id.imageViewprofile);
        employee();
      Employee emp=new Employee();
        tvempid = (TextView) findViewById(R.id.textViewempcoder);
        tvempid.setText(emp.getEmpID());
        tvempname=(TextView)findViewById(R.id.textViewempcoder) ;
        tvempname.setText(emp.getName());
        tvemail=(TextView)findViewById(R.id.textViewemailr) ;
        tvemail.setText(emp.getEmailID());
        tvmobile=(TextView)findViewById(R.id.textViewmobiler) ;
        tvmobile.setText(emp.getMobile());
        tvdob=(TextView)findViewById(R.id.textViewdobr) ;
        tvdob.setText((CharSequence) emp.getDob());
        tvgender=(TextView)findViewById(R.id.textViewgenterr) ;
        tvgender.setText(emp.getGender());
        tvmanager=(TextView)findViewById(R.id.mageridr) ;
        tvmanager.setText(emp.getManagername());
        tvdepartment=(TextView)findViewById(R.id.textViewdeptr) ;
        tvdepartment.setText(emp.getDepartment());
        tvdesignation=(TextView)findViewById(R.id.textViewdesig) ;
        tvdesignation.setText(emp.getDesignation());


    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(myprofileactivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {


                if (items[item].equals("Take Photo")) {
                    userChoosenTask ="Take Photo";

                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask ="Choose from Library";

                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        getOutputMediaFile();
        /*File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        ivImage.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ivImage.setImageBitmap(bm);
    }
    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }
    private void employee() {
        Employee employee = new Employee();
        employee.setName("bala");
        employee.setEmpID("cc01");
        employee.setEmailID("bala.gp@gmai.com");
        employee.setGender("male");
        employee.setManagername("Ramesh");
        employee.setDegination("software engineer");
        employee.setdepartment("mobility");
        employee.setMobile("123456789");
    }
}




