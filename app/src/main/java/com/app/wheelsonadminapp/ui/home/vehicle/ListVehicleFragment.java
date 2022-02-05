package com.app.wheelsonadminapp.ui.home.vehicle;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.wheelsonadminapp.BaseActivity;
import com.app.wheelsonadminapp.R;
import com.app.wheelsonadminapp.data.db.AppRepository;
import com.app.wheelsonadminapp.data.network.ApiService;
import com.app.wheelsonadminapp.data.network.RetrofitClientInstance;
import com.app.wheelsonadminapp.databinding.FragmentListVehicleBinding;
import com.app.wheelsonadminapp.model.VehicleModel;
import com.app.wheelsonadminapp.model.auth.vehicle.VehicleItem;
import com.app.wheelsonadminapp.model.auth.vehicle.VehicleListResponse;
import com.app.wheelsonadminapp.model.auth.vehicle.VehicleTypeResponse;
import com.app.wheelsonadminapp.model.auth.vehicle.VehicletypeItem;
import com.app.wheelsonadminapp.ui.auth.SignUpActivity;
import com.app.wheelsonadminapp.ui.home.HomeActivity;
import com.app.wheelsonadminapp.ui.home.trips.AddTripFragment;
import com.app.wheelsonadminapp.ui.home.trips.TripsFragment;
import com.app.wheelsonadminapp.ui.home.trips.ViewVehicleTripFragment;
import com.app.wheelsonadminapp.util.AppConstants;
import com.app.wheelsonadminapp.util.MessageProgressDialog;
import com.app.wheelsonadminapp.util.NetworkUtility;
import com.google.gson.JsonObject;
import com.skydoves.powerspinner.IconSpinnerAdapter;
import com.skydoves.powerspinner.IconSpinnerItem;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class ListVehicleFragment extends Fragment implements View.OnClickListener, VehiclesAdapter.VehicleClickListener {

    FragmentListVehicleBinding vehicleBinding;
    BaseActivity homeActivity;
    List<IconSpinnerItem> iconSpinnerItems;
    List<VehicletypeItem>spinnerItems;
    AppRepository appRepository;
    boolean fromManageTrips = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getActivity() instanceof VehicleActivity){
            homeActivity = (VehicleActivity)getActivity();
        }else {
            homeActivity = (HomeActivity)getActivity();
        }

        if(getArguments()!=null && getArguments().getBoolean("FROM_MANAGE_TRIP")){
            fromManageTrips = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vehicleBinding = FragmentListVehicleBinding.inflate(inflater,container,false);
        vehicleBinding.fabAddType.setOnClickListener(this);
        vehicleBinding.fabAddVehicle.setOnClickListener(this);
        vehicleBinding.fabAdd.setOnClickListener(this);
        vehicleBinding.btViewByDate.setOnClickListener(this);

        appRepository = new AppRepository(getActivity());
        if(fromManageTrips){
            vehicleBinding.rightLabels.setVisibility(View.GONE);
            vehicleBinding.fabAdd.setVisibility(View.VISIBLE);
            vehicleBinding.btViewByDate.setVisibility(View.VISIBLE);
            vehicleBinding.textDrivers.setText("Trips");
            vehicleBinding.textManage.setText("Tap on vehicle to see trips");
        }
        return vehicleBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        vehicleBinding = null;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(NetworkUtility.isOnline(getActivity())){
            getVehicleTypeList();
        }else {
            homeActivity.showErrorToast(getString(R.string.no_internet));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fabAddType:
                showAddTypeDialog();
                vehicleBinding.rightLabels.collapse();
                break;
            case R.id.fabAddVehicle:
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(AppConstants.VEHICLE_TYPE_LIST, (ArrayList<? extends Parcelable>) spinnerItems);
                homeActivity.replaceFragment(new AddVehicleFragment(),true,bundle);
                vehicleBinding.rightLabels.collapse();
                break;
            case R.id.fabAdd:
                Bundle bundle1 = new Bundle();
                bundle1.putString(AppConstants.TRIP_ID,"");
                homeActivity.replaceFragment(new AddTripFragment(),true,bundle1);
                break;
            case R.id.btViewByDate:
                homeActivity.replaceFragment(new TripsFragment(),false,null);
                break;
        }
    }


    private void showAddTypeDialog(){
        Dialog dialog = new Dialog(getActivity(),R.style.PauseDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.vehicle_type_dialog);
        Button btAdd = dialog.findViewById(R.id.btAdd);
        EditText etType = dialog.findViewById(R.id.etType);
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etType.getText().length()!=0){
                    if(NetworkUtility.isOnline(getActivity())){
                        MessageProgressDialog.getInstance().show(getActivity());
                        AppRepository appRepository = new AppRepository(getActivity());
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("travelsid",appRepository.getUser().getUserId());
                        jsonObject.addProperty("vehicletype",etType.getText().toString());
                        ApiService apiService = RetrofitClientInstance.getRetrofitInstance().create(ApiService.class);
                        Call<VehicleTypeResponse> typeResponseCall = apiService.addVehicleType(jsonObject);
                        typeResponseCall.enqueue(new Callback<VehicleTypeResponse>() {
                            @Override
                            public void onResponse(Call<VehicleTypeResponse> call, Response<VehicleTypeResponse> response) {
                                MessageProgressDialog.getInstance().dismiss();
                                if(response.code() == 200 && response.body()!=null){
                                    if(response.body().getStatus() == 1){
                                        homeActivity.showSuccessToast("Type added successfully!");
                                    }else {
                                        homeActivity.showErrorToast(getString(R.string.something_wrong));
                                    }
                                }
                                dialog.dismiss();
                            }

                            @Override
                            public void onFailure(Call<VehicleTypeResponse> call, Throwable t) {
                                MessageProgressDialog.getInstance().dismiss();
                                homeActivity.showErrorToast(getString(R.string.something_wrong));
                                dialog.dismiss();
                            }
                        });
                    }else {
                        homeActivity.showErrorToast(getString(R.string.no_internet));
                        dialog.dismiss();
                    }

                }else {
                    homeActivity.showErrorToast("Please enter the type");
                }
            }
        });
        Objects.requireNonNull(dialog.getWindow()).getDecorView().setBackgroundColor(Color.TRANSPARENT);
        dialog.show();
    }

    private void getVehicleTypeList(){
        MessageProgressDialog.getInstance().show(getActivity());
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("travelsid",appRepository.getUser().getUserId());
        ApiService apiService = RetrofitClientInstance.getRetrofitInstance().create(ApiService.class);
        Call<VehicleTypeResponse>responseCall = apiService.getVehicles(jsonObject);
        responseCall.enqueue(new Callback<VehicleTypeResponse>() {
            @Override
            public void onResponse(Call<VehicleTypeResponse> call, Response<VehicleTypeResponse> response) {
                Timber.i(response.body().getVehicletype().toString());
                MessageProgressDialog.getInstance().dismiss();
                if(response.code() == 200 && response.body()!=null){
                    if(response.body().getStatus() == 1){
                        spinnerItems = response.body().getVehicletype();
                        addDataToSpinner(spinnerItems);
                        getVehicleList();
                    }
                }
            }

            @Override
            public void onFailure(Call<VehicleTypeResponse> call, Throwable t) {
                Timber.i(t.getMessage());
                MessageProgressDialog.getInstance().dismiss();
            }
        });
    }

    private void addDataToSpinner(List<VehicletypeItem>spinnerItems){
        iconSpinnerItems = new ArrayList<>();
        for (int i=0;i<spinnerItems.size();i++){
            if(spinnerItems.get(i).getVehicletype().length()!=0){
                IconSpinnerItem iconSpinnerItem = new IconSpinnerItem(spinnerItems.get(i).getVehicletype());
                iconSpinnerItems.add(iconSpinnerItem);
            }
        }
        IconSpinnerAdapter iconSpinnerAdapter = new IconSpinnerAdapter(vehicleBinding.spinnerState);
        iconSpinnerAdapter.setItems(iconSpinnerItems);
        vehicleBinding.spinnerState.setSpinnerAdapter(iconSpinnerAdapter);
        vehicleBinding.spinnerState.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<Object>() {
            @Override
            public void onItemSelected(int i, @org.jetbrains.annotations.Nullable Object o, int i1, Object t1) {
                Timber.i(spinnerItems.get(i1).getId()+" "+spinnerItems.get(i1).getVehicletype());
                getVehicleByTypeList(String.valueOf(spinnerItems.get(i1).getId()));
            }
        });
    }


    private void getVehicleList(){
        if(NetworkUtility.isOnline(vehicleBinding.getRoot().getContext())){
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("travelsid",appRepository.getUser().getUserId());
            Call<VehicleListResponse>listResponseCall = RetrofitClientInstance.getApiService().getVehicleList(jsonObject);
            listResponseCall.enqueue(new Callback<VehicleListResponse>() {
                @Override
                public void onResponse(Call<VehicleListResponse> call, Response<VehicleListResponse> response) {
                    if(response.code() == 200 && response.body()!=null){
                        if(response.body().getStatus() == 1){
                            loadDataToList(response.body());
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

    private void getVehicleByTypeList(String type){
        if(NetworkUtility.isOnline(vehicleBinding.getRoot().getContext())){
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("travelsid",appRepository.getUser().getUserId());
            jsonObject.addProperty("vehicletype",type);
            Call<VehicleListResponse>listResponseCall = RetrofitClientInstance.getApiService().getVehicleListByType(jsonObject);
            listResponseCall.enqueue(new Callback<VehicleListResponse>() {
                @Override
                public void onResponse(Call<VehicleListResponse> call, Response<VehicleListResponse> response) {
                    if(response.code() == 200 && response.body()!=null){
                        if(response.body().getStatus() == 1){
                            loadDataToList(response.body());
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

    private void loadDataToList(VehicleListResponse vehicleListResponse){
        if(vehicleListResponse.getVehicle().size() == 0){
            vehicleBinding.textNoVehicles.setVisibility(View.VISIBLE);
            vehicleBinding.vehiclesRecycler.setVisibility(View.GONE);
        }else {
            vehicleBinding.textNoVehicles.setVisibility(View.GONE);
            vehicleBinding.vehiclesRecycler.setVisibility(View.VISIBLE);
            vehicleBinding.vehiclesRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
            VehiclesAdapter vehiclesAdapter = new VehiclesAdapter(vehicleListResponse.getVehicle() ,getActivity(),vehicleListResponse.getPath().toString(),this,fromManageTrips);
            vehicleBinding.vehiclesRecycler.setAdapter(vehiclesAdapter);
        }
    }

    @Override
    public void onVehicleClicked(VehicleItem vehicleItem, String imgPath) {
        Bundle bundle = new Bundle();
        if(fromManageTrips){
            bundle.putString("VEHICLE_ID",vehicleItem.getId());
            bundle.putString("VEHICLE_NAME",vehicleItem.getBrand()+" "+vehicleItem.getModel());
            bundle.putString("VEHICLE STATUS",vehicleItem.getStatus());
            homeActivity.replaceFragment(new ViewVehicleTripFragment(),true,bundle);
        }else {
            bundle.putParcelable(AppConstants.VEHICLE,vehicleItem);
            bundle.putString(AppConstants.IMAGE_PATH,imgPath);
            bundle.putParcelableArrayList(AppConstants.VEHICLE_TYPE_LIST, (ArrayList<? extends Parcelable>) spinnerItems);
            homeActivity.replaceFragment(new AddVehicleFragment(),true,bundle);
        }
    }

    @Override
    public void onInActiveClicked(VehicleItem vehicleItem) {
        deleteVehicle(vehicleItem.getId());
    }

    private void deleteVehicle(String status) {
        if(NetworkUtility.isOnline(getActivity())) {
            JsonObject inputObject = new JsonObject();
            inputObject.addProperty("vehicleid",status);

            MessageProgressDialog.getInstance().show(getActivity());
            Call<JsonObject>deleteApiCall = RetrofitClientInstance.getApiService().deleteVehicle(inputObject);
            deleteApiCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    MessageProgressDialog.getInstance().dismiss();
                    if(response.code() == 200 && response.body()!=null && response.body().has("status")){
                        int status = response.body().get("status").getAsInt();
                        if(status == 1){
                            //homeActivity.onBackPressed();
                            getVehicleList();
                        }else {
                            homeActivity.showSuccessToast("Trips assigned to this vehicle, cannot deactivate.");
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    MessageProgressDialog.getInstance().dismiss();
                }
            });
        }else {
            homeActivity.showErrorToast(getString(R.string.no_internet));
        }
    }
}