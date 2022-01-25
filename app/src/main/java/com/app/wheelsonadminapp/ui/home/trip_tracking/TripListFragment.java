package com.app.wheelsonadminapp.ui.home.trip_tracking;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.wheelsonadminapp.R;
import com.app.wheelsonadminapp.data.db.AppRepository;
import com.app.wheelsonadminapp.data.network.RetrofitClientInstance;
import com.app.wheelsonadminapp.databinding.FragmentTripListBinding;
import com.app.wheelsonadminapp.model.trip.trip_details.TripDetailItem;
import com.app.wheelsonadminapp.model.trip.triplist.TripListItem;
import com.app.wheelsonadminapp.model.trip.triplist.TripListResponse;
import com.app.wheelsonadminapp.ui.home.HomeActivity;
import com.app.wheelsonadminapp.ui.home.HomeTrackingFragment;
import com.app.wheelsonadminapp.ui.home.HomeTripsAdapter;
import com.app.wheelsonadminapp.ui.home.trips.ViewRunTripFragment;
import com.app.wheelsonadminapp.util.AppConstants;
import com.app.wheelsonadminapp.util.MessageProgressDialog;
import com.app.wheelsonadminapp.util.NetworkUtility;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TripListFragment extends Fragment implements TripListAdapter.HomeTripClickListener {

    FragmentTripListBinding binding;
    HomeActivity homeActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeActivity = (HomeActivity)getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTripListBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getTrips();
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
                            binding.textNoTrips.setVisibility(View.GONE);
                            binding.tripsRecycler.setVisibility(View.VISIBLE);
                        }else {
                            binding.textNoTrips.setVisibility(View.VISIBLE);
                            binding.tripsRecycler.setVisibility(View.GONE);
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

    private void loadRecyclerData(List<TripListItem> tripItems){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        binding.tripsRecycler.setLayoutManager(linearLayoutManager);
        TripListAdapter tripListAdapter = new TripListAdapter(tripItems,homeActivity,this);
        binding.tripsRecycler.setAdapter(tripListAdapter);
    }

    @Override
    public void OnTripClicked(TripListItem tripItem) {
        Bundle bundle = new Bundle();
        TripDetailItem tripDetailItem = new TripDetailItem();
        tripDetailItem.setTripid(tripItem.getId());
        tripDetailItem.setFromlat(tripItem.getFromlat());
        tripDetailItem.setFromlon(tripItem.getFromlon());
        tripDetailItem.setFromlocation(tripItem.getFromlocation());
        tripDetailItem.setTolat(tripItem.getTolat());
        tripDetailItem.setTolon(tripItem.getTolon());
        tripDetailItem.setTolocation(tripItem.getTolocation());
        bundle.putParcelable(AppConstants.TRIP_DETAILS,tripDetailItem);
        homeActivity.replaceFragment(new HomeTrackingFragment(),true,bundle);
    }
}