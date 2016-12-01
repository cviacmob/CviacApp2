package com.cviac.cviacappapi.cviacapp;

import com.cviac.datamodel.cviacapp.Employee;
import com.cviac.datamodel.cviacapp.EmployeeResponse;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;

/**
 * Created by Shanmugam on 30-11-2016.
 */

public interface CVIACApi {
    @GET("/CVIACAPI/cviacdbop.php")
    Call<List<Employee>> getEmployees();

  //  Call<List<Employee>> getemployees();
}

