package com.cviac.cviacappapi.cviacapp;

import com.cviac.datamodel.cviacapp.EmployeeInfo;
import com.squareup.okhttp.RequestBody;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Query;

/**
 * Created by Shanmugam on 30-11-2016.
 */

public interface CVIACApi {

    @POST("/CVIACAPI/cviacdbop.php/otpreg")
    Call<RegisterResponse> registerMobile(@Body RegInfo regInfo);


    @POST("/CVIACAPI/cviacdbop.php/additionalregistration")
    Call<AdditinalRegisterResponse> registeradditionalverification(@Body AdditionalRegInfo regInfo);

    @POST("/CVIACAPI/cviacdbop.php/verifyotp")
    Call<VerifyResponse> verifyPin(@Body RegInfo regInfo);

    @GET("/CVIACAPI/cviacdbop.php")
    Call<List<EmployeeInfo>> getEmployees();

    @Multipart
    @POST("/CVIACAPI/upload.php")
    Call<ProfileUpdateResponse> profileUpdate(@Query("emp_code") String empcode,  @Part("fileToUpload\"; filename=\"pp.png\" ") RequestBody file);
    //  Call<List<Employee>> getemployees();


}

