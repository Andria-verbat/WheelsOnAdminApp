package com.app.wheelsonadminapp.ui.home.trips;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.app.wheelsonadminapp.BaseActivity;
import com.app.wheelsonadminapp.R;
import com.app.wheelsonadminapp.data.db.AppRepository;
import com.app.wheelsonadminapp.data.network.ApiService;
import com.app.wheelsonadminapp.data.network.RetrofitClientInstance;
import com.app.wheelsonadminapp.databinding.FragmentAddTripBinding;
import com.app.wheelsonadminapp.model.auth.vehicle.VehicleItem;
import com.app.wheelsonadminapp.model.auth.vehicle.VehicleListResponse;
import com.app.wheelsonadminapp.model.driver.DriverItem;
import com.app.wheelsonadminapp.model.driver.DriverResponse;
import com.app.wheelsonadminapp.model.trip.TripResponse;
import com.app.wheelsonadminapp.ui.home.HomeActivity;
import com.app.wheelsonadminapp.ui.home.vehicle.VehicleActivity;
import com.app.wheelsonadminapp.util.MessageProgressDialog;
import com.app.wheelsonadminapp.util.NetworkUtility;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.app.wheelsonadminapp.util.AppConstants.apiKey;

public class AddTripFragment extends Fragment implements View.OnClickListener {

    FragmentAddTripBinding binding;
    private static int AUTOCOMPLETE_REQUEST_CODE = 1;
    boolean fromClicked = false;
    boolean toClicked = false;
    double fromLat, toLat, fromLong,toLong;
    int selectedDriverId = 0;
    int selectedVehicleId = 0;
    int dateMode = 0; // 1 --> startDate, 2 --> endDate
    final Calendar myCalendar = Calendar.getInstance();


    ArrayList<DriverItem> algorithmItems;
    SpinnerDriverAdapter adapter;
    SpinnerVehicleAdapter vehicleAdapter;
    BaseActivity homeActivity;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getActivity() instanceof VehicleActivity){
            homeActivity = (VehicleActivity)getActivity();
        }else {
            homeActivity = (HomeActivity)getActivity();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddTripBinding.inflate(inflater,container,false);
        // Initialize the SDK
        Places.initialize(getActivity().getApplicationContext(), apiKey);

        // Create a new PlacesClient instance
        PlacesClient placesClient = Places.createClient(getActivity());

        binding.etFromLocation.setOnClickListener(this);
        binding.etToLocation.setOnClickListener(this);
        binding.btSubmit.setOnClickListener(this);
        setDOB();
        setTimePicker();
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void openPlaceSearch(){
        // Set the fields to specify which types of place data to
        // return after the user has made a selection.
        List<Place.Field> fields = Arrays.asList(Place.Field.LAT_LNG,Place.Field.ID, Place.Field.NAME);

        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .build(getActivity());
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDriversFromServer();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.etFromLocation:
                fromClicked = true;
                toClicked = false;
                binding.etFromLocation.setText("");
                fromLat = 0;
                fromLong = 0;
                openPlaceSearch();
                break;
            case R.id.etToLocation:
                fromClicked = false;
                toClicked = true;
                binding.etToLocation.setText("");
                toLat = 0;
                toLong = 0;
                openPlaceSearch();
                break;
            case R.id.btSubmit:
                assignTrip();
                break;
        }
    }

    private void assignTrip(){
        if(binding.etFromLocation.getText().length() == 0){
            homeActivity.showErrorToast("Please enter the From Location");
            binding.etFromLocation.requestFocus();
        }else if(binding.etToLocation.getText().length() == 0){
            homeActivity.showErrorToast("Please enter the To Location");
            binding.etToLocation.requestFocus();
        }else if(binding.spinnerDriver.getSelectedItemPosition() == 0){
            homeActivity.showErrorToast("Please select a driver");
            binding.spinnerDriver.requestFocus();
        }else if(binding.spinnerVehicle.getSelectedItemPosition() == 0){
            homeActivity.showErrorToast("Please select a vehicle");
            binding.spinnerVehicle.requestFocus();
        }else if(binding.eTripStartDate.getText().length() == 0){
            homeActivity.showErrorToast("Please enter the start date");
            binding.eTripStartDate.requestFocus();
        }else if(binding.etTripEndDate.getText().length() == 0){
            homeActivity.showErrorToast("Please enter the end date");
            binding.etTripEndDate.requestFocus();
        }else if(binding.etPickUpTime.getText().length() == 0){
            homeActivity.showErrorToast("Please enter the pickup time");
            binding.etPickUpTime.requestFocus();
        }else if(binding.etPickUpAddress.getText().length() == 0){
            homeActivity.showErrorToast("Please enter the pickup address");
            binding.etPickUpAddress.requestFocus();
        }else if(binding.etDropAddress.getText().length() == 0){
            homeActivity.showErrorToast("Please enter the drop address");
            binding.etDropAddress.requestFocus();
        }else if(binding.etPrimaryContactPerson.getText().length() == 0){
            homeActivity.showErrorToast("Please enter the contact name");
            binding.etPrimaryContactPerson.requestFocus();
        }else if(binding.etPrimaryContactNumber.getText().length() == 0 || binding.etPrimaryContactNumber.getText().length() < 9){
            homeActivity.showErrorToast("Please enter the contact number");
            binding.etPrimaryContactNumber.requestFocus();
        }/*else if(binding.etTripAmount.getText().length() == 0){
            homeActivity.showErrorToast("Please enter the trip amount");
            binding.etTripAmount.requestFocus();
        }else if(binding.etRateKm.getText().length() == 0){
            homeActivity.showErrorToast("Please enter the trip rate per km");
            binding.etRateKm.requestFocus();
        }*/else {
            callAddTripAPI();
        }
    }

    private void callAddTripAPI(){
        if(NetworkUtility.isOnline(getActivity())){
            AppRepository appRepository = new AppRepository(getActivity());
            MessageProgressDialog.getInstance().show(getActivity());
            JsonObject tripObject = new JsonObject();
            tripObject.addProperty("travelsid",appRepository.getUser().getUserId());
            tripObject.addProperty("fromlocation",binding.etFromLocation.getText().toString());
            tripObject.addProperty("fromlat",String.valueOf(fromLat));
            tripObject.addProperty("fromlon",String.valueOf(fromLong));
            tripObject.addProperty("tolocation",binding.etToLocation.getText().toString());
            tripObject.addProperty("tolat",String.valueOf(toLat));
            tripObject.addProperty("tolon",String.valueOf(toLong));
            tripObject.addProperty("driverid",String.valueOf(selectedDriverId));
            tripObject.addProperty("vehicleid",String.valueOf(selectedVehicleId));
            tripObject.addProperty("startdate",binding.eTripStartDate.getText().toString());
            tripObject.addProperty("enddate",binding.etTripEndDate.getText().toString());
            tripObject.addProperty("pickuptime",binding.etPickUpTime.getText().toString());
            tripObject.addProperty("pickaddress",binding.etPickUpAddress.getText().toString());
            tripObject.addProperty("dropaddress",binding.etDropAddress.getText().toString());
            tripObject.addProperty("person",binding.etPrimaryContactPerson.getText().toString());
            tripObject.addProperty("mobile1",binding.etPrimaryContactNumber.getText().toString());
            tripObject.addProperty("mobile2",binding.etSecondaryContactNumber.getText().toString());
            if(binding.etTripAmount.getText().length() == 0){
                tripObject.addProperty("amount","");
            }else {
                tripObject.addProperty("amount",binding.etTripAmount.getText().toString());
            }
            if(binding.etRateKm.getText().length() == 0){
                tripObject.addProperty("kmrate","");
            }else {
                tripObject.addProperty("kmrate",binding.etRateKm.getText().toString());
            }
            tripObject.addProperty("comments",binding.etComment.getText().toString());
            Call<TripResponse>tripResponseCall = RetrofitClientInstance.getApiService().addTrip(tripObject);
            tripResponseCall.enqueue(new Callback<TripResponse>() {
                @Override
                public void onResponse(Call<TripResponse> call, Response<TripResponse> response) {
                    MessageProgressDialog.getInstance().dismiss();
                    if(response.code() == 200 && response.body()!=null){
                        if(response.body().getStatus() == 1){
                            homeActivity.showSuccessToast("Trip assigned successfully!");
                            homeActivity.onBackPressed();
                        }else {
                            homeActivity.showErrorToast(getString(R.string.something_wrong));
                        }
                    }else {
                        homeActivity.showErrorToast(getString(R.string.something_wrong));
                    }
                }

                @Override
                public void onFailure(Call<TripResponse> call, Throwable t) {
                    homeActivity.showErrorToast(getString(R.string.something_wrong));
                    MessageProgressDialog.getInstance().dismiss();
                }
            });

        }else {
            homeActivity.showErrorToast(getString(R.string.no_internet));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Timber.i("Place: " + place.getName() + ", " + place.getLatLng().latitude);
                if(fromClicked){
                    binding.etFromLocation.setText(place.getName());
                    fromLat = place.getLatLng().latitude;
                    fromLong = place.getLatLng().longitude;
                }else if(toClicked){
                    binding.etToLocation.setText(place.getName());
                    toLat = place.getLatLng().latitude;
                    toLong = place.getLatLng().longitude;
                }

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Timber.i(status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void getDriversFromServer(){
        MessageProgressDialog.getInstance().show(getActivity());
        AppRepository appRepository = new AppRepository(getActivity());
        JsonObject inputObject = new JsonObject();
        inputObject.addProperty("travelsid",appRepository.getUser().getUserId());
        ApiService apiService = RetrofitClientInstance.getRetrofitInstance().create(ApiService.class);
        Call<DriverResponse> driverResponseCall = apiService.getDrivers(inputObject);
        driverResponseCall.enqueue(new Callback<DriverResponse>() {
            @Override
            public void onResponse(Call<DriverResponse> call, Response<DriverResponse> response) {
                Timber.i(response.body().toString());
                MessageProgressDialog.getInstance().dismiss();
                if(response.code() == 200 && response.body()!=null){
                    if(response.body().getStatus() == 1){
                        DriverItem driverItem = new DriverItem();
                        driverItem.setName("Select Driver");
                        ArrayList<DriverItem> arrayList= (ArrayList<DriverItem>) response.body().getDriver();
                        arrayList.add(0,driverItem);
                        adapter = new SpinnerDriverAdapter(getActivity(), arrayList);
                        binding.spinnerDriver.setAdapter(adapter);
                        binding.spinnerDriver.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                // It returns the clicked item.
                                DriverItem clickedItem = (DriverItem) parent.getItemAtPosition(position);
                                String name = clickedItem.getName();
                                if(name.equals("Select Driver")){
//                                    homeActivity.showSuccessToast("Please select a Driver");
                                    selectedDriverId = 0;
                                }else {
                                    selectedDriverId = Integer.parseInt(clickedItem.getId());
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                        getVehicleList();
                    }else {

                    }
                }
            }

            @Override
            public void onFailure(Call<DriverResponse> call, Throwable t) {
                Timber.i(t.getMessage());
                MessageProgressDialog.getInstance().dismiss();
            }
        });
    }

    private void getVehicleList(){
        if(NetworkUtility.isOnline(binding.getRoot().getContext())){
            AppRepository appRepository = new AppRepository(getActivity());
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("travelsid",appRepository.getUser().getUserId());
            Call<VehicleListResponse>listResponseCall = RetrofitClientInstance.getApiService().getVehicleList(jsonObject);
            listResponseCall.enqueue(new Callback<VehicleListResponse>() {
                @Override
                public void onResponse(Call<VehicleListResponse> call, Response<VehicleListResponse> response) {
                    if(response.code() == 200 && response.body()!=null){
                        if(response.body().getStatus() == 1){
                            VehicleItem vehicleItem = new VehicleItem();
                            vehicleItem.setModel("Select Vehicle");
                            vehicleItem.setBrand("");
                            ArrayList<VehicleItem> arrayList= (ArrayList<VehicleItem>) response.body().getVehicle();
                            arrayList.add(0,vehicleItem);
                            vehicleAdapter = new SpinnerVehicleAdapter(getActivity(), arrayList);
                            binding.spinnerVehicle.setAdapter(vehicleAdapter);

                            binding.spinnerVehicle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    if(position == 0){
                                        selectedVehicleId = 0;
                                    }else {
                                        VehicleItem clickedItem = (VehicleItem) parent.getItemAtPosition(position);
                                        selectedVehicleId = Integer.parseInt(clickedItem.getId());
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        }
                    }else {
                        homeActivity.showErrorToast(getString(R.string.something_wrong));
                    }
                }

                @Override
                public void onFailure(Call<VehicleListResponse> call, Throwable t) {
                    homeActivity.showErrorToast(getString(R.string.something_wrong));
                }
            });
        }else {
            homeActivity.showErrorToast(getString(R.string.no_internet));
        }
    }


    private void setDOB(){
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                if(dateMode == 1){
                    binding.eTripStartDate.setText(sdf.format(myCalendar.getTime()));
                }else if(dateMode == 2){
                    binding.etTripEndDate.setText(sdf.format(myCalendar.getTime()));
                }
            }

        };

        binding.eTripStartDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dateMode = 1;
                new DatePickerDialog(getActivity(),R.style.datepicker, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        binding.etTripEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateMode = 2;
                new DatePickerDialog(getActivity(),R.style.datepicker, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void setTimePicker(){
        binding.etPickUpTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(),R.style.datepicker, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String selectedZero = "";
                        String selectedHourZero = "";
                        if(selectedMinute < 10){
                            selectedZero = "0";
                        }else {
                            selectedZero = "";
                        }
                        if(String.valueOf(selectedHour).length() ==1 ){
                            selectedHourZero = "0";
                        }
                        binding.etPickUpTime.setText( selectedHourZero+selectedHour + ":" + selectedZero+selectedMinute);
                    }
                }, hour, minute, false);
                mTimePicker.setTitle("Select PickUp Time");
                mTimePicker.show();

            }
        });
    }

}