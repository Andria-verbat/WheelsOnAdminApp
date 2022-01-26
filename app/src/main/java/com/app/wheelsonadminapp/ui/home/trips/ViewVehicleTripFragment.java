package com.app.wheelsonadminapp.ui.home.trips;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.wheelsonadminapp.R;
import com.app.wheelsonadminapp.data.network.RetrofitClientInstance;
import com.app.wheelsonadminapp.databinding.FragmentViewVehicleTripBinding;
import com.app.wheelsonadminapp.model.trip.TripItem;
import com.app.wheelsonadminapp.model.trip.TripResponse;
import com.app.wheelsonadminapp.model.trip.triplist.TripListItem;
import com.app.wheelsonadminapp.model.trip.triplist.TripListResponse;
import com.app.wheelsonadminapp.ui.home.HomeActivity;
import com.app.wheelsonadminapp.ui.home.expense.AddExpenseFragment;

import com.app.wheelsonadminapp.util.AppConstants;
import com.app.wheelsonadminapp.util.MessageProgressDialog;
import com.app.wheelsonadminapp.util.NetworkUtility;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ViewVehicleTripFragment extends Fragment {

    FragmentViewVehicleTripBinding binding;
    String vehicleId;
    HomeActivity homeActivity;
    String vehicleName;
    boolean fromExpense = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeActivity = (HomeActivity) getActivity();
        if(getArguments()!=null && getArguments().getString("VEHICLE_ID")!=null){
            vehicleId = getArguments().getString("VEHICLE_ID");
            vehicleName = getArguments().getString("VEHICLE_NAME");
            Log.i("ViewVehicleTripFragment", "onCreate: "+vehicleId);
        }

        if(getArguments()!=null && getArguments().getBoolean("FROM_EXPENSE")){
            fromExpense = getArguments().getBoolean("FROM_EXPENSE");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentViewVehicleTripBinding.inflate(inflater,container,false);
        binding.textDrivers.setText(vehicleName);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getTripsByVehicleId(vehicleId);
    }

    private void getTripsByVehicleId(String vehicleId){
        if(NetworkUtility.isOnline(getActivity())){
            JsonObject inputObject = new JsonObject();
            inputObject.addProperty("vehicleid",vehicleId);
            MessageProgressDialog.getInstance().show(getActivity());
            Call<TripResponse> getTripsCall = RetrofitClientInstance.getApiService().getTripsByVehicleId(inputObject);
            getTripsCall.enqueue(new Callback<TripResponse>() {
                @Override
                public void onResponse(Call<TripResponse> call, Response<TripResponse> response) {
                    MessageProgressDialog.getInstance().dismiss();
                    if(response.code() == 200 && response.body()!=null){
                        loadTripsToRecycler(response.body().getTrip());
                    }else {
                        binding.textNoTrips.setVisibility(View.VISIBLE);
                        binding.tripsRecycler.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<TripResponse> call, Throwable t) {
                    MessageProgressDialog.getInstance().dismiss();
                    homeActivity.showErrorToast(getString(R.string.something_wrong));
                }
            });
        }else {
            homeActivity.showErrorToast(getString(R.string.no_internet));
        }
    }


    private void loadTripsToRecycler(List<TripItem> tripItems){
        if(tripItems!=null && tripItems.size() == 0){
            binding.textNoTrips.setVisibility(View.VISIBLE);
            binding.tripsRecycler.setVisibility(View.GONE);
        }else {
            binding.textNoTrips.setVisibility(View.GONE);
            binding.tripsRecycler.setVisibility(View.VISIBLE);

            TripsAdapter tripsAdapter = new TripsAdapter(tripItems, getActivity(), new TripsAdapter.TripClickListener() {
                @Override
                public void OnTripClicked(TripItem tripItem) {
                    if(fromExpense){
                        Bundle bundle = new Bundle();
                        bundle.putString("TRIP_ID",tripItem.getId());
                        homeActivity.replaceFragment(new AddExpenseFragment(),false,bundle);
                    }else {
                        Bundle bundle = new Bundle();
                        bundle.putString(AppConstants.TRIP_ID,tripItem.getId());
                        homeActivity.replaceFragment(new ViewRunTripFragment(),true,bundle);
                    }
                }
            });
            binding.tripsRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
            binding.tripsRecycler.setAdapter(tripsAdapter);
        }
    }
}