package com.app.wheelsonadminapp.ui.home.trips;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.wheelsonadminapp.BaseActivity;
import com.app.wheelsonadminapp.R;
import com.app.wheelsonadminapp.data.db.AppRepository;
import com.app.wheelsonadminapp.data.network.RetrofitClientInstance;
import com.app.wheelsonadminapp.databinding.FragmentViewRunTripFragementBinding;
import com.app.wheelsonadminapp.model.trip.trip_details.TripDetailsResponse;
import com.app.wheelsonadminapp.ui.home.HomeActivity;
import com.app.wheelsonadminapp.ui.home.HomeTrackingFragment;
import com.app.wheelsonadminapp.ui.home.vehicle.VehicleActivity;
import com.app.wheelsonadminapp.util.AppConstants;
import com.app.wheelsonadminapp.util.MessageProgressDialog;
import com.app.wheelsonadminapp.util.NetworkUtility;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewRunTripFragment extends Fragment implements View.OnClickListener {

    FragmentViewRunTripFragementBinding binding;
    BaseActivity homeActivity;
    String tripId;
    TripDetailsResponse tripDetailsResponse;
    AppRepository repository;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getActivity() instanceof VehicleActivity){
            homeActivity = (VehicleActivity)getActivity();
        }else {
            homeActivity = (HomeActivity)getActivity();
        }
        if(getArguments()!=null && getArguments().getString(AppConstants.TRIP_ID)!=null){
            tripId = getArguments().getString(AppConstants.TRIP_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentViewRunTripFragementBinding.inflate(inflater,container,false);
        binding.btTrack.setOnClickListener(this);
        binding.btCloseTrip.setOnClickListener(this);
        repository = new AppRepository(homeActivity);
        binding.textUserName.setText("Hi, "+repository.getUser().getTravelsName());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(tripId!=null){
            getTripDetails();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btTrack:
                if(tripDetailsResponse!=null){
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(AppConstants.TRIP_DETAILS,tripDetailsResponse.getTrip().get(0));
                    homeActivity.replaceFragment(new HomeTrackingFragment(),true,bundle);
                }
                break;
            case R.id.btCloseTrip:
                homeActivity.onBackPressed();
                break;
        }
    }

    private void getTripDetails(){
        if(NetworkUtility.isOnline(homeActivity)){
            JsonObject inputObject = new JsonObject();
            inputObject.addProperty("tripid",tripId);
            MessageProgressDialog.getInstance().show(homeActivity);
            Call<TripDetailsResponse>responseCall = RetrofitClientInstance.getApiService().getTripDetails(inputObject);
            responseCall.enqueue(new Callback<TripDetailsResponse>() {
                @Override
                public void onResponse(Call<TripDetailsResponse> call, Response<TripDetailsResponse> response) {
                    MessageProgressDialog.getInstance().dismiss();
                    if(response.code() == 200 && response.body()!=null){
                        if(response.body().getStatus() == 1){
                            tripDetailsResponse = response.body();
                            loadData(tripDetailsResponse);
                        }else {
                            homeActivity.showErrorToast(getString(R.string.something_wrong));
                            requireActivity().onBackPressed();
                        }
                    }else {
                        homeActivity.showErrorToast(getString(R.string.something_wrong));
                        requireActivity().onBackPressed();
                    }
                }

                @Override
                public void onFailure(Call<TripDetailsResponse> call, Throwable t) {
                    homeActivity.showErrorToast(getString(R.string.something_wrong));
                    MessageProgressDialog.getInstance().dismiss();
                }
            });
        }else {
            homeActivity.showErrorToast(getString(R.string.no_internet));
        }
    }

    private void loadData(TripDetailsResponse tripDetailsResponse){
        binding.textStartLocation.setText("Starting from : "+tripDetailsResponse.getTrip().get(0).getFromlocation());
        binding.textDropLocation.setText("Dropping to : "+tripDetailsResponse.getTrip().get(0).getTolocation());
        binding.textTripFromTo.setText(tripDetailsResponse.getTrip().get(0).getTolocation()
                +" to "+tripDetailsResponse.getTrip().get(0).getTolocation());
        binding.textContactName.setText(tripDetailsResponse.getTrip().get(0).getPerson());
        binding.textContactNumber.setText(tripDetailsResponse.getTrip().get(0).getMobile1()
                +","+tripDetailsResponse.getTrip().get(0).getMobile2());
        binding.etTotalAmount.setText("Rs."+tripDetailsResponse.getTrip().get(0).getAmount());
        binding.etTripRate.setText("Rs."+tripDetailsResponse.getTrip().get(0).getKmrate());
        binding.etStartingKm.setText(tripDetailsResponse.getTrip().get(0).getStartingkm());
//        binding.etFuelAmount.setText("Rs."+tripDetailsResponse.getExpense().get(0).getFuel());
//        binding.etTollAmount.setText("Rs."+tripDetailsResponse.getExpense().get(0).getToll());
//        binding.etParkingAmount.setText("Rs."+tripDetailsResponse.getExpense().get(0).getPark());
    }
}