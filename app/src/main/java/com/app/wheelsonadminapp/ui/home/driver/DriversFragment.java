package com.app.wheelsonadminapp.ui.home.driver;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.wheelsonadminapp.R;
import com.app.wheelsonadminapp.data.db.AppRepository;
import com.app.wheelsonadminapp.data.network.ApiService;
import com.app.wheelsonadminapp.data.network.RetrofitClientInstance;
import com.app.wheelsonadminapp.databinding.FragmentDriversBinding;
import com.app.wheelsonadminapp.model.DriverModel;
import com.app.wheelsonadminapp.model.driver.DriverItem;
import com.app.wheelsonadminapp.model.driver.DriverResponse;
import com.app.wheelsonadminapp.ui.home.HomeActivity;
import com.app.wheelsonadminapp.util.AppConstants;
import com.app.wheelsonadminapp.util.MessageProgressDialog;
import com.app.wheelsonadminapp.util.NetworkUtility;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;


public class DriversFragment extends Fragment implements DriverAdapter.DriverClickListener {

    FragmentDriversBinding driversBinding;
    DriverActivity homeActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeActivity = (DriverActivity)getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        driversBinding = FragmentDriversBinding.inflate(inflater,container,false);
//        loadDriversToRecycler();
        driversBinding.fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeActivity.replaceFragment(new AddDriverFragment(),true,null);
            }
        });
        return driversBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(NetworkUtility.isOnline(getActivity())){
            getDriversFromServer();
        }else {
            homeActivity.showErrorToast(getString(R.string.no_internet));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        driversBinding = null;
    }

   /* private void loadDriversToRecycler(){
        List<DriverModel>driverModels = new ArrayList<>();
        driverModels.add(new DriverModel());
        driverModels.add(new DriverModel());
        driverModels.add(new DriverModel());
        driverModels.add(new DriverModel());
        driverModels.add(new DriverModel());
        driverModels.add(new DriverModel());
        driverModels.add(new DriverModel());
        driverModels.add(new DriverModel());
        driverModels.add(new DriverModel());
        driversBinding.driverRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        DriverAdapter driverAdapter = new DriverAdapter(driverModels,getActivity());
        driversBinding.driverRecycler.setAdapter(driverAdapter);
    }*/

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
                        loadDataToRecycler(response.body());
                    }else {
                        driversBinding.textNoDrivers.setVisibility(View.VISIBLE);
                        driversBinding.driverRecycler.setVisibility(View.GONE);
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

    private void loadDataToRecycler(DriverResponse driverResponse){
        if(driverResponse.getDriver().size()!=0){
            driversBinding.textNoDrivers.setVisibility(View.GONE);
            driversBinding.driverRecycler.setVisibility(View.VISIBLE);
            driversBinding.driverRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
            DriverAdapter driverAdapter = new DriverAdapter(driverResponse.getDriver(),getActivity(),driverResponse.getPath(),this);
            driversBinding.driverRecycler.setAdapter(driverAdapter);

        }else {
            driversBinding.textNoDrivers.setVisibility(View.VISIBLE);
            driversBinding.driverRecycler.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDriverClicked(DriverItem driverItem, String imgPath) {
        Bundle bundle = new Bundle();
        bundle.putString(AppConstants.IMAGE_PATH,imgPath);
        bundle.putParcelable(AppConstants.DRIVER,driverItem);
        homeActivity.replaceFragment(new AddDriverFragment(),true,bundle);
    }
}