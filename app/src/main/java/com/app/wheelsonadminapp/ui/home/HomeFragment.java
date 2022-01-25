package com.app.wheelsonadminapp.ui.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.wheelsonadminapp.R;
import com.app.wheelsonadminapp.data.db.AppRepository;
import com.app.wheelsonadminapp.data.network.RetrofitClientInstance;
import com.app.wheelsonadminapp.databinding.FragmentHomeBinding;
import com.app.wheelsonadminapp.model.TripModel;
import com.app.wheelsonadminapp.model.trip.TripItem;
import com.app.wheelsonadminapp.model.trip.TripResponse;
import com.app.wheelsonadminapp.model.trip.triplist.TripListItem;
import com.app.wheelsonadminapp.model.trip.triplist.TripListResponse;
import com.app.wheelsonadminapp.ui.home.driver.DriverActivity;
import com.app.wheelsonadminapp.ui.home.driver.DriversFragment;
import com.app.wheelsonadminapp.ui.home.service.AddServiceFragment;
import com.app.wheelsonadminapp.ui.home.service.SelectVehicleFragment;
import com.app.wheelsonadminapp.ui.home.service.ServiceListFragment;
import com.app.wheelsonadminapp.ui.home.trip_tracking.TripListFragment;
import com.app.wheelsonadminapp.ui.home.trips.TripsAdapter;
import com.app.wheelsonadminapp.ui.home.trips.TripsFragment;
import com.app.wheelsonadminapp.ui.home.trips.ViewRunTripFragment;
import com.app.wheelsonadminapp.ui.home.vehicle.ListVehicleFragment;
import com.app.wheelsonadminapp.ui.home.vehicle.VehicleActivity;
import com.app.wheelsonadminapp.util.AppConstants;
import com.app.wheelsonadminapp.util.MessageProgressDialog;
import com.app.wheelsonadminapp.util.NetworkUtility;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements View.OnClickListener, HomeTripsAdapter.HomeTripClickListener {

    FragmentHomeBinding homeBinding;
    HomeActivity homeActivity;
    AppRepository appRepository;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeActivity = (HomeActivity)getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       homeBinding = FragmentHomeBinding.inflate(inflater,container,false);
//       setUpTripsRecycler();
       homeBinding.linearManageDrivers.setOnClickListener(this);
       homeBinding.linearVehicles.setOnClickListener(this);
       homeBinding.linearTrips.setOnClickListener(this);
       homeBinding.linearTrack.setOnClickListener(this);
       homeBinding.linearService.setOnClickListener(this);
       homeBinding.linearExpense.setOnClickListener(this);
       appRepository = new AppRepository(getActivity());
       if(appRepository.getUser()!=null){
           homeBinding.textUserName.setText("Hi, "+appRepository.getUser().getTravelsName());
       }
       return homeBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        homeBinding = null;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getTrips();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.linearManageDrivers:
                startActivity(new Intent(getActivity(), DriverActivity.class));
                break;
            case R.id.linearVehicles:
                startActivity(new Intent(getActivity(), VehicleActivity.class));
                break;
            case R.id.linearTrips:
                Bundle bundle = new Bundle();
                bundle.putBoolean("FROM_MANAGE_TRIP",true);
                homeActivity.replaceFragment(new ListVehicleFragment(),true,bundle);
                break;
            case R.id.linearTrack:
                homeActivity.replaceFragment(new TripListFragment(),true,null);
                break;
            case R.id.linearService:
                Bundle serviceBundle =  new Bundle();
                serviceBundle.putBoolean("FROM_SERVICE",true);
                homeActivity.replaceFragment(new SelectVehicleFragment(),true,serviceBundle);
                break;
            case R.id.linearExpense:
                homeActivity.replaceFragment(new SelectVehicleFragment(),true,null);
                break;
        }
    }

    private String getFormattedDate(Calendar date){
        return (String) DateFormat.format("yyyy-MM-dd", date);
    }

    private void getTrips(){
        if(NetworkUtility.isOnline(getActivity())){
            AppRepository appRepository = new AppRepository(getActivity());
            MessageProgressDialog.getInstance().show(getActivity());
            JsonObject inputObject = new JsonObject();
            inputObject.addProperty("travelsid",appRepository.getUser().getUserId());
            Call<TripListResponse> tripResponseCall = RetrofitClientInstance.getApiService().getTrips(inputObject);
            tripResponseCall.enqueue(new Callback<TripListResponse>() {
                @Override
                public void onResponse(Call<TripListResponse> call, Response<TripListResponse> response) {
                    MessageProgressDialog.getInstance().dismiss();
                    if(response.code() == 200 && response.body()!=null){
                        if(response.body().getStatus() == 1){
                            loadRecyclerData(response.body().getTrip());
                            homeBinding.textNoTrips.setVisibility(View.GONE);
                            homeBinding.tripsRecycler.setVisibility(View.VISIBLE);
                        }else {
                            homeBinding.textNoTrips.setVisibility(View.VISIBLE);
                            homeBinding.tripsRecycler.setVisibility(View.GONE);
                        }
                    }else {
                        homeActivity.showErrorToast(getString(R.string.something_wrong));
                    }
                }

                @Override
                public void onFailure(Call<TripListResponse> call, Throwable t) {
                    homeActivity.showErrorToast(getString(R.string.something_wrong));
                    MessageProgressDialog.getInstance().dismiss();
                }
            });
        }else{
            homeActivity.showErrorToast(getString(R.string.no_internet));
        }
    }

    private void loadRecyclerData(List<TripListItem>tripItems){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        homeBinding.tripsRecycler.setLayoutManager(linearLayoutManager);
        HomeTripsAdapter homeTripsAdapter = new HomeTripsAdapter(tripItems,homeActivity,this);
        homeBinding.tripsRecycler.setAdapter(homeTripsAdapter);
    }

    @Override
    public void OnTripClicked(TripListItem tripItem) {
        Bundle bundle = new Bundle();
        bundle.putString(AppConstants.TRIP_ID,tripItem.getId());
        homeActivity.replaceFragment(new ViewRunTripFragment(),true,bundle);
    }
}