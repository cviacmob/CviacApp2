package com.cviac.activity.cviacapp;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cviac.com.cviac.app.adapaters.CircleTransform;
import com.cviac.com.cviac.app.restapis.CVIACApi;
import com.cviac.com.cviac.app.restapis.ProfileUpdateResponse;
import com.cviac.com.cviac.app.datamodels.Employee;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;


public class MyProfileActivity extends AppCompatActivity {

    private static final int MY_PERMISSION_CAMERA = 10;
    private static final int MY_PERMISSION_EXTERNAL_STORAGE = 11;

    TextView tvempid, tvempname, tvemail, tvdoj, tvmobile, tvgender, tvdob, tvmanager, tvdepartment, tvdesignation;
    final Context context = this;
    ImageView imageViewRound;

    private int REQUEST_CAMERA = 2, SELECT_FILE = 1;

    private ImageView ivImage, btnSelect;
    private String userChoosenTask;

    ProgressDialog progressDialog;
    private String empcode, empcoded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myprofile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //data from collegue
        Intent i = getIntent();
        empcode = i.getStringExtra("empcode");
        Employee emp = Employee.getemployee(empcode);
        //title set
        setTitle(emp.getEmp_name());
        btnSelect = (ImageView) findViewById(R.id.imageButtonselect);
        final String MyPREFERENCES = "MyPrefs";
        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        String mobile = prefs.getString("mobile", "");
        Employee emplogged = Employee.getemployeeByMobile(mobile);
        empcoded = emplogged.getEmp_code();
        if (!empcode.equals(empcoded)) {

            btnSelect.setVisibility(View.INVISIBLE);
        }


        ivImage = (ImageView) findViewById(R.id.imageViewprofile);


        Picasso.with(context).load(R.drawable.camera).resize(80, 80).transform(new CircleTransform())
                .into(btnSelect);
        btnSelect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectImage();
            }
        });


        tvempid = (TextView) findViewById(R.id.textViewempcoder);
        tvempid.setText(emp.getEmp_code());
        tvempname = (TextView) findViewById(R.id.textViewempnamer);
        tvempname.setText(emp.getEmp_name());
        tvemail = (TextView) findViewById(R.id.textViewemailr);
        tvemail.setText(emp.getEmail());
        tvmobile = (TextView) findViewById(R.id.textViewmobiler);
        tvmobile.setText(emp.getMobile());
        tvdob = (TextView) findViewById(R.id.textViewdobr);
        String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(new Date(emp.getDob().toString()));

        tvdob.setText(timeStamp);
        tvgender = (TextView) findViewById(R.id.textViewgenterr);
        tvgender.setText(emp.getGender());
        tvmanager = (TextView) findViewById(R.id.mageridr);
        tvmanager.setText(emp.getManager());
        tvdepartment = (TextView) findViewById(R.id.textViewdeptr);
        tvdepartment.setText(emp.getDepartment());
        tvdesignation = (TextView) findViewById(R.id.textViewdesig);
        tvdesignation.setText(emp.getDesignation());
        tvdoj = (TextView) findViewById(R.id.tvdojr);
        String timeStam = new SimpleDateFormat("dd-MM-yyyy").format(new Date(emp.getDoj().toString()));
        tvdoj.setText(timeStam);

        String imgUrl = emp.getImage_url();
        if (imgUrl != null && imgUrl.length() > 0) {
            Picasso.with(context).load(imgUrl).resize(220, 220).transform(new CircleTransform())
                    .centerCrop().memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).into(ivImage);
        } else {
            if(emp.getGender().equalsIgnoreCase("female"))
            {
                Picasso.with(context).load(R.drawable.female).resize(220, 220).transform(new CircleTransform())
                        .centerCrop().memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).into(ivImage);
            }else
            {
                Picasso.with(context).load(R.drawable.ic_boy).resize(220, 220).transform(new CircleTransform())
                        .centerCrop().memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).into(ivImage);
            }

        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MyProfileActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    dialog.dismiss();
                    cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    dialog.dismiss();
                    galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            // Start the Intent
            startActivityForResult(galleryIntent, SELECT_FILE);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_EXTERNAL_STORAGE);
        }
    }

    private void cameraIntent() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_CAMERA);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSION_CAMERA);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                }
            }
            break;
            case MY_PERMISSION_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
                if (grantResults.length > 1 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, SELECT_FILE);
            }
        }
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
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
            uploadProfileImage(destination.getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String path = MediaStore.Images.Media.insertImage(this.getContentResolver(),thumbnail,"" ,null);
        Picasso.with(this).load(path).resize(350, 350).transform(new CircleTransform())
                .centerCrop().memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).into(ivImage);
        //ivImage.setImageBitmap(thumbnail);

    }

    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String targetPath = cursor.getString(columnIndex);
                cursor.close();

                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                String path = MediaStore.Images.Media.insertImage(this.getContentResolver(),bm,"" ,null);
                Picasso.with(this).load(path).resize(350, 350).transform(new CircleTransform())
                        .centerCrop().memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).into(ivImage);

                //ivImage.setImageBitmap(bm);
                uploadProfileImage(targetPath);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadProfileImage(String targetPath) {
        progressDialog = new ProgressDialog(MyProfileActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("image uploading.....");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://apps.cviac.com")
                //.baseUrl("http://192.168.1.12")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CVIACApi api = retrofit.create(CVIACApi.class);
        File file = new File(targetPath);
        RequestBody fbody = RequestBody.create(MediaType.parse("image/*"), file);
        Call<ProfileUpdateResponse> call = api.profileUpdate(empcode, fbody);
        call.enqueue(new Callback<ProfileUpdateResponse>() {
            @Override
            public void onResponse(Response<ProfileUpdateResponse> response, Retrofit retrofit) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                ProfileUpdateResponse rsp = response.body();
                if (rsp.getImageUrl() != null) {
                    Employee.updateProfileImageUrl(empcode, rsp.getImageUrl());
                    Toast.makeText(MyProfileActivity.this, "Profile photo updated ", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                Toast.makeText(MyProfileActivity.this, "Profile photo updated failure", Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }

    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }


}
