package com.app.wheelsonadminapp.data.network;

import com.app.wheelsonadminapp.model.auth.OTPResponse;
import com.app.wheelsonadminapp.model.auth.VerifyOTPResponse;
import com.app.wheelsonadminapp.model.auth.login.LoginResponse;
import com.app.wheelsonadminapp.model.auth.register.RegisterResponse;
import com.app.wheelsonadminapp.model.auth.vehicle.VehicleListResponse;
import com.app.wheelsonadminapp.model.auth.vehicle.VehicleTypeResponse;
import com.app.wheelsonadminapp.model.driver.DriverResponse;
import com.app.wheelsonadminapp.model.expense.ExpenseItemListResponse;
import com.app.wheelsonadminapp.model.expense.expense_list.ExpenseListResponse;
import com.app.wheelsonadminapp.model.service.ServiceItemResponse;
import com.app.wheelsonadminapp.model.service.vehicle_server_service.VehicleServiceListResponse;
import com.app.wheelsonadminapp.model.service.vehicle_service.ServiceListResponse;
import com.app.wheelsonadminapp.model.trip.TripResponse;
import com.app.wheelsonadminapp.model.trip.trip_details.TripDetailsResponse;
import com.app.wheelsonadminapp.model.trip.trip_track.TrackResponse;
import com.app.wheelsonadminapp.model.trip.triplist.TripListResponse;
import com.app.wheelsonadminapp.model.trip.triplist.TripLiveListResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface ApiService {

    @POST("travels_otp_create.php")
    Call<OTPResponse> getOTP(@Body JsonObject mobObject);

    @POST("travels_otp_verify.php")
    Call<VerifyOTPResponse>verifyOTP(@Body JsonObject inputObject);

    @Multipart
    @POST("travels_registration.php")
    Call<RegisterResponse> registerUser(@Part("travelname") RequestBody travelBody,
                                        @Part("ownername") RequestBody nameBody,
                                        @Part("mobileno") RequestBody mobileBody,
                                        @Part("phone") RequestBody phoneBody,
                                        @Part("vehicles") RequestBody vehiclesBody,
                                        @Part("emailid") RequestBody emailBody,
                                        @Part("address") RequestBody addressBody,
                                        @Part("password") RequestBody password,
                                        @Part MultipartBody.Part image);

    @POST("travels_login.php")
    Call<LoginResponse>loginUser(@Body JsonObject inputObject);

    @POST("driver_list.php")
    Call<DriverResponse>getDrivers(@Body JsonObject inputObject);

    @POST("vehicle_type_create.php")
    Call<VehicleTypeResponse>addVehicleType(@Body JsonObject inputObject);

    @POST("vehicle_type_list.php")
    Call<VehicleTypeResponse>getVehicles(@Body JsonObject inputObject);

    @POST("vehicle_list.php")
    Call<VehicleListResponse>getVehicleList(@Body JsonObject inputObject);

    @POST("vehicle_list_type.php")
    Call<VehicleListResponse>getVehicleListByType(@Body JsonObject inputObject);

    @Multipart
    @POST("driver_registration.php")
    Call<DriverResponse> registerDriver(@Part("travelsid") RequestBody travelBody,
                                        @Part("drivername") RequestBody nameBody,
                                        @Part("mobileno") RequestBody mobileBody,
                                        @Part("address") RequestBody vehiclesBody,
                                        @Part("city") RequestBody emailBody,
                                        @Part("district") RequestBody addressBody,
                                        @Part("state") RequestBody state,
                                        @Part("licenseno") RequestBody licenseNo,
                                        @Part("licensevalidity") RequestBody licenseValidity,
                                        @Part("licensecategory") RequestBody licenseCategory,
                                        @Part("aadharno") RequestBody aadharNo,
                                        @Part("experience") RequestBody experience,
                                        @Part("policestation") RequestBody policeStation,
                                        @Part("emailid") RequestBody emailId,
                                        @Part("password ") RequestBody password,
                                        @Part MultipartBody.Part profileImg,
                                        @Part MultipartBody.Part licenceFront,
                                        @Part MultipartBody.Part licenceBack,
                                        @Part MultipartBody.Part aadhaarFront,
                                        @Part MultipartBody.Part aadhaarBack);


    @POST("driver_registration.php")
    Call<DriverResponse> registerDriverNew(@Body RequestBody file);


    @POST("driver_update.php")
    Call<DriverResponse> updateDriverNew(@Body RequestBody file);


    @Multipart
    @POST("driver_update.php")
    Call<DriverResponse> updateDriver(@Part("travelsid") RequestBody travelBody,
                                        @Part("drivername") RequestBody nameBody,
                                        @Part("mobileno") RequestBody mobileBody,
                                        @Part("address") RequestBody vehiclesBody,
                                        @Part("city") RequestBody emailBody,
                                        @Part("district") RequestBody addressBody,
                                        @Part("state") RequestBody state,
                                        @Part("licenseno") RequestBody licenseNo,
                                        @Part("licensevalidity") RequestBody licenseValidity,
                                        @Part("licensecategory") RequestBody licenseCategory,
                                        @Part("aadharno") RequestBody aadharNo,
                                        @Part("experience") RequestBody experience,
                                        @Part("policestation") RequestBody policeStation,
                                        @Part("emailid") RequestBody emailId,
                                        @Part("password ") RequestBody password);


    @Multipart
    @POST("vehicle_create.php")
    Call<VehicleListResponse> addVehicle(@Part("travelsid") RequestBody travelBody,
                                      @Part("vehicletype") RequestBody typeBody,
                                      @Part("brand") RequestBody brandBody,
                                      @Part("model") RequestBody modelBody,
                                      @Part("regno") RequestBody regBody,
                                      @Part("insurancedate") RequestBody insuranceBody,
                                      @Part("pollutiondate") RequestBody pollutionBody,
                                      @Part("permitdate") RequestBody permitBody,
                                      @Part("seat") RequestBody seatBody);


    @POST("vehicle_create.php")
    Call<VehicleListResponse> createVehicle(@Body RequestBody file);

    @POST("vehicle_update.php")
    Call<VehicleListResponse> updateVehicle(@Body RequestBody file);


    @Multipart
    @POST("vehicle_update.php")
    Call<VehicleListResponse> updateVehicle(@Part("travelsid") RequestBody travelBody,
                                         @Part("vehicletype") RequestBody typeBody,
                                         @Part("brand") RequestBody brandBody,
                                         @Part("model") RequestBody modelBody,
                                         @Part("regno") RequestBody regBody,
                                         @Part("insurancedate") RequestBody insuranceBody,
                                         @Part("pollutiondate") RequestBody pollutionBody,
                                         @Part("permitdate") RequestBody permitBody,
                                         @Part("seat") RequestBody seatBody);


    @POST("trip_create.php")
    Call<TripResponse>addTrip(@Body JsonObject inputObject);

    @POST("trip_date_list.php")
    Call<TripResponse>getTripsByDate(@Body JsonObject inputObject);

    @POST("trip_list.php")
    Call<TripListResponse>getTrips(@Body JsonObject inputObject);

    @POST("trip_live_list.php")
    Call<TripLiveListResponse>getLiveTrips(@Body JsonObject inputObject);

    @POST("trip_details.php")
    Call<TripDetailsResponse>getTripDetails(@Body JsonObject inputObject);

    @POST("track_details.php")
    Call<TrackResponse>getTrackData(@Body JsonObject inputObject);

    @POST("service_type_list.php")
    Call<ServiceItemResponse>getServiceItems();

    @POST("service_create.php")
    Call<JsonObject> saveService(@Body JsonArray inputObject);

    @POST("service_create.php")
    Call<JsonObject> addService(@Body JsonObject inputObject);

    @POST("service_update.php")
    Call<JsonObject> updateService(@Body JsonObject inputObject);

    @POST("service_travel_list.php")
    Call<ServiceListResponse> getServiceList(@Body JsonObject inputObject);

    @POST("trip_vehicle_list.php")
    Call<TripResponse>getTripsByVehicleId(@Body JsonObject inputObject);

    @POST("vehicle_service_details.php")
    Call<VehicleServiceListResponse>getServiceListByVehicleId(@Body JsonObject inputObject);

    @POST("service_delete.php")
    Call<JsonObject>deleteServiceItem(@Body JsonObject inoutObject);

    @POST("trip_expense_type_list.php")
    Call<ExpenseItemListResponse>getExpenseItems();

    @POST("trip_expense_tripid_list.php")
    Call<ExpenseListResponse> getExpenseByTripId(@Body JsonObject inputObject);

    @POST("trip_expense_create.php")
    Call<JsonObject> addTripExpense(@Body RequestBody file);

    @POST("trip_expense_delete.php")
    Call<JsonObject> deleteExpense(@Body JsonObject inputObject);

    @POST("trip_expense_update.php")
    Call<JsonObject> updateExpense(@Body RequestBody file);

    @POST("vehicle_delete.php")
    Call<JsonObject> deleteVehicle(@Body JsonObject inputObject);
}
