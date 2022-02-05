package com.app.wheelsonadminapp.ui.home.closed_trips;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.wheelsonadminapp.BaseActivity;
import com.app.wheelsonadminapp.R;
import com.app.wheelsonadminapp.data.db.AppRepository;
import com.app.wheelsonadminapp.data.network.ApiService;
import com.app.wheelsonadminapp.data.network.RetrofitClientInstance;
import com.app.wheelsonadminapp.databinding.FragmentClosedTripBinding;
import com.app.wheelsonadminapp.databinding.FragmentDriversBinding;
import com.app.wheelsonadminapp.model.driver.DriverResponse;
import com.app.wheelsonadminapp.model.trip.closed_trips.ClosedTripItems;
import com.app.wheelsonadminapp.model.trip.closed_trips.ClosedTripResponse;
import com.app.wheelsonadminapp.ui.home.HomeActivity;
import com.app.wheelsonadminapp.ui.home.driver.AddDriverFragment;
import com.app.wheelsonadminapp.ui.home.driver.DriverActivity;
import com.app.wheelsonadminapp.ui.home.driver.DriverAdapter;
import com.app.wheelsonadminapp.util.AppConstants;
import com.app.wheelsonadminapp.util.MessageProgressDialog;
import com.app.wheelsonadminapp.util.NetworkUtility;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by Andria on 2/4/2022.
 */
public class ClosedTripFragment extends Fragment implements ClosedTripAdapter.ClosedTripClick{
    FragmentClosedTripBinding fragmentClosedTripBinding;
    BaseActivity homeActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeActivity = (HomeActivity)getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentClosedTripBinding = FragmentClosedTripBinding.inflate(inflater,container,false);


        return fragmentClosedTripBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(NetworkUtility.isOnline(getActivity())){
            getClosedTrips();
        }else {
            homeActivity.showErrorToast(getString(R.string.no_internet));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentClosedTripBinding = null;
    }



    private void getClosedTrips(){
        MessageProgressDialog.getInstance().show(getActivity());
        AppRepository appRepository = new AppRepository(getActivity());
        JsonObject inputObject = new JsonObject();
        inputObject.addProperty("travelsid",appRepository.getUser().getUserId());
        ApiService apiService = RetrofitClientInstance.getRetrofitInstance().create(ApiService.class);
        Call<ClosedTripResponse> closedTripResponseCall = apiService.getClosedTrips(inputObject);
        closedTripResponseCall.enqueue(new Callback<ClosedTripResponse>() {
            @Override
            public void onResponse(Call<ClosedTripResponse> call, Response<ClosedTripResponse> response) {
                Timber.i(response.body().toString());
                MessageProgressDialog.getInstance().dismiss();
                if(response.code() == 200 && response.body()!=null){
                    if(response.body().getStatus() == 1){
                        loadDataToRecycler(response.body());
                    }else {
                        fragmentClosedTripBinding.textNoVehicles.setVisibility(View.VISIBLE);
                        fragmentClosedTripBinding.closedTripRecycler.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ClosedTripResponse> call, Throwable t) {
                Timber.i(t.getMessage());
                MessageProgressDialog.getInstance().dismiss();
            }
        });
    }

    private void loadDataToRecycler(ClosedTripResponse closedTripResponse){
        if(closedTripResponse.getTrip().size()!=0){
            fragmentClosedTripBinding.textNoVehicles.setVisibility(View.GONE);
            fragmentClosedTripBinding.closedTripRecycler.setVisibility(View.VISIBLE);
            fragmentClosedTripBinding.closedTripRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
            ClosedTripAdapter closedTripAdapter = new ClosedTripAdapter(closedTripResponse.getTrip(),getActivity(),closedTripResponse.getPath(),this);
            fragmentClosedTripBinding.closedTripRecycler.setAdapter(closedTripAdapter);

        }else {
            fragmentClosedTripBinding.textNoVehicles.setVisibility(View.VISIBLE);
            fragmentClosedTripBinding.closedTripRecycler.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClosedTripClicked(ClosedTripItems closedTripItem, String imgPath) {
        Bundle bundle = new Bundle();
        bundle.putString(AppConstants.IMAGE_PATH,imgPath);
        bundle.putParcelable(AppConstants.CLOSED_TRIP,closedTripItem);
        homeActivity.replaceFragment(new ClosedTripUpdateFragment(),true,bundle);
    }
}
