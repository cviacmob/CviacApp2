package com.cviac.cviacappapi.cviacapp;

import com.cviac.datamodel.cviacapp.Employee;
import com.cviac.datamodel.cviacapp.EmployeeResponse;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

import static android.R.attr.path;

/**
 * Created by Shanmugam on 30-11-2016.
 */

public interface CVIACApi {
    @GET("/CVIACAPI/cviacdbop.php")
    Call<List<Employee>> getEmployees();

            @POST("/CVIACAPI/cviacdbop.php/otpreg")
            Call<Employee> getemployeeByMobile(@Path("mobile") String mobile );


  //  Call<List<Employee>> getemployees();
}

