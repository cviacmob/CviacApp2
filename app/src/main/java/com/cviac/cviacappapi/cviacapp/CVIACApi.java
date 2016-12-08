package com.cviac.cviacappapi.cviacapp;

import com.cviac.datamodel.cviacapp.Employee;
import com.cviac.datamodel.cviacapp.EmployeeInfo;
import com.cviac.datamodel.cviacapp.EmployeeResponse;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

/**
 * Created by Shanmugam on 30-11-2016.
 */

public interface CVIACApi {

    @POST("/CVIACAPI/cviacdbop.php/otpreg")
    Call<RegisterResponse> registerMobile(@Body RegInfo regInfo);

    @POST("/CVIACAPI/cviacdbop.php/verifyotp")
    Call<VerifyResponse> verifyPin(@Body RegInfo regInfo);

    @GET("/CVIACAPI/cviacdbop.php")
    Call<List<EmployeeInfo>> getEmployees();






    //  Call<List<Employee>> getemployees();
}

