package com.cviac.activity.cviacapp;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;


import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;


import com.cviac.adapter.cviacapp.CircleTransform;
import com.cviac.datamodel.cviacapp.Employee;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class MyProfileActivity extends AppCompatActivity  implements ActivityCompat.OnRequestPermissionsResultCallback {

    TextView tvempid, tvempname,tvemail,tvmobile,tvgender,tvdob,tvmanager,tvdepartment,tvdesignation;
    final Context context = this;
    ImageView imageViewRound;

    /**
     * Id to identify a camera permission request.
     */
    private static final int REQUEST_CAMERA = 0;

    /**
     * Id to identify a contacts permission request.
     */
    private static final int REQUEST_CONTACTS = 1;




    // Whether the Log Fragment is currently shown.
    private boolean mLogShown;

    /**
     * Root of the layout of this Activity.
     */
    private View mLayout;
    private int  SELECT_FILE = 1;
    public static final String TAG = "MyProfileActivity";
    private ImageView ivImage,btnSelect;
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ivImage = (ImageView) findViewById(R.id.imageViewprofile);
        Picasso.with(context).load(R.drawable.bala).resize(220, 220).transform(new CircleTransform())
                .into(ivImage);
        btnSelect = (ImageView) findViewById(R.id.imageButtonselect);
        Picasso.with(context).load(R.drawable.camera).resize(80, 80).transform(new CircleTransform())
                .into(btnSelect);
        btnSelect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        //data from collegue
        Intent i = getIntent();
        String empcode = i.getStringExtra("empcode");

        Employee emp = Employee.getemployees(empcode);
        tvempid = (TextView) findViewById(R.id.textViewempcoder);
        tvempid.setText(emp.getEmp_code());
        tvempname=(TextView)findViewById(R.id.textViewempnamer) ;
        tvempname.setText(emp.getEmp_name());
        tvemail=(TextView)findViewById(R.id.textViewemailr) ;
        tvemail.setText(emp.getEmail());
        tvmobile=(TextView)findViewById(R.id.textViewmobiler) ;
        tvmobile.setText(emp.getMobile());

        Date dNow = new Date(String.valueOf(emp.getDob()));
        SimpleDateFormat ft =
                new SimpleDateFormat ("dd-MM-yyyy");


        tvdob=(TextView)findViewById(R.id.textViewdobr) ;
        tvdob.setText(ft.format(dNow));
        tvgender=(TextView)findViewById(R.id.textViewgenterr) ;
        tvgender.setText(emp.getGender());
        tvmanager=(TextView)findViewById(R.id.mageridr) ;
        tvmanager.setText(emp.getManager());
        tvdepartment=(TextView)findViewById(R.id.textViewdeptr) ;
        tvdepartment.setText(emp.getDepartment());
        tvdesignation=(TextView)findViewById(R.id.textViewdesig) ;
        tvdesignation.setText(emp.getDesignation());
        //tvdesignation=(TextView)findViewById(R.id.textViewdesig) ;
        //tvdesignation.setText(emp.getS());



    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MyProfileActivity.this);
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
        View shor = null;
        showCamera(shor);
        /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);*/
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    public void showCamera(View view) {
        Log.i(TAG, "Show camera button pressed. Checking permission.");
        // BEGIN_INCLUDE(camera_permission)
        // Check if the Camera permission is already available.
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Camera permission has not been granted.

            requestCameraPermission();

        } else {

            // Camera permissions is already available, show the camera preview.
            Log.i(TAG,
                    "CAMERA permission has already been granted. Displaying camera preview.");

        }
        // END_INCLUDE(camera_permission)

    }


    private void requestCameraPermission() {
        Log.i(TAG, "CAMERA permission has NOT been granted. Requesting permission.");


        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.CAMERA)) {

            Log.i(TAG,
                    "Displaying camera permission rationale to provide additional context.");
            Snackbar.make(mLayout, R.string.permission_camera_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(MyProfileActivity.this,
                                    new String[]{android.Manifest.permission.CAMERA},
                                    REQUEST_CAMERA);
                        }
                    })
                    .show();
        } else {

            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA},
                    REQUEST_CAMERA);
        }

    }



}
