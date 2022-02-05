package com.app.wheelsonadminapp.ui.home.service;

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
import com.app.wheelsonadminapp.databinding.FragmentSelectVehicleBinding;
import com.app.wheelsonadminapp.model.auth.vehicle.VehicleItem;
import com.app.wheelsonadminapp.model.auth.vehicle.VehicleListResponse;
import com.app.wheelsonadminapp.model.auth.vehicle.VehicleTypeResponse;
import com.app.wheelsonadminapp.model.auth.vehicle.VehicletypeItem;
import com.app.wheelsonadminapp.ui.home.HomeActivity;
import com.app.wheelsonadminapp.ui.home.trips.ViewVehicleTripFragment;
import com.app.wheelsonadminapp.ui.home.vehicle.VehiclesAdapter;
import com.app.wheelsonadminapp.util.AppConstants;
import com.app.wheelsonadminapp.util.MessageProgressDialog;
import com.app.wheelsonadminapp.util.NetworkUtility;
import com.google.gson.JsonObject;
import com.skydoves.powerspinner.IconSpinnerAdapter;
import com.skydoves.powerspinner.IconSpinnerItem;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;


public class SelectVehicleFragment extends Fragment implements View.OnClickListener {

    FragmentSelectVehicleBinding binding;
    HomeActivity homeActivity;
    List<IconSpinnerItem> iconSpinnerItems;
    List<VehicletypeItem>spinnerItems;
    AppRepository appRepository;
    boolean fromService = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeActivity = (HomeActivity)getActivity();
        if(getArguments()!=null && getArguments().getBoolean("FROM_SERVICE")){
            fromService = getArguments().getBoolean("FROM_SERVICE");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSelectVehicleBinding.inflate(inflater,container,false);
        binding.fabAddService.setOnClickListener(this);
        appRepository = new AppRepository(getActivity());
        if(!fromService){
            binding.textManage.setText("Select vehicle to add expense");
        }
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fabAddService:
                break;
        }

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

    private void getVehicleTypeList(){
        MessageProgressDialog.getInstance().show(getActivity());
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("travelsid",appRepository.getUser().getUserId());
        ApiService apiService = RetrofitClientInstance.getRetrofitInstance().create(ApiService.class);
        Call<VehicleTypeResponse> responseCall = apiService.getVehicles(jsonObject);
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
        IconSpinnerAdapter iconSpinnerAdapter = new IconSpinnerAdapter(binding.spinnerState);
        iconSpinnerAdapter.setItems(iconSpinnerItems);
        binding.spinnerState.setSpinnerAdapter(iconSpinnerAdapter);
        binding.spinnerState.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<Object>() {
            @Override
            public void onItemSelected(int i, @org.jetbrains.annotations.Nullable Object o, int i1, Object t1) {
                Timber.i(spinnerItems.get(i1).getId()+" "+spinnerItems.get(i1).getVehicletype());
                getVehicleByTypeList(String.valueOf(spinnerItems.get(i1).getId()));
            }
        });
    }

    private void getVehicleList(){
        if(NetworkUtility.isOnline(binding.getRoot().getContext())){
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
        if(NetworkUtility.isOnline(binding.getRoot().getContext())){
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
            binding.textNoVehicles.setVisibility(View.VISIBLE);
            binding.vehiclesRecycler.setVisibility(View.GONE);
        }else {
            binding.textNoVehicles.setVisibility(View.GONE);
            binding.vehiclesRecycler.setVisibility(View.VISIBLE);
            binding.vehiclesRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
            VehiclesAdapter vehiclesAdapter = new VehiclesAdapter(vehicleListResponse.getVehicle(), getActivity(), vehicleListResponse.getPath().toString(), new VehiclesAdapter.VehicleClickListener() {
                @Override
                public void onVehicleClicked(VehicleItem vehicleItem, String imgPath) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(AppConstants.VEHICLE,vehicleItem);
                    bundle.putString(AppConstants.IMAGE_PATH,imgPath);
                    if(fromService){
                        homeActivity.replaceFragment(new AddServiceFragment(),false,bundle);
                    }else {
                        bundle.putString("VEHICLE_ID",vehicleItem.getId());
                        bundle.putString("VEHICLE_NAME",vehicleItem.getBrand()+" "+vehicleItem.getModel());
                        bundle.putBoolean("FROM_EXPENSE",true);
                        homeActivity.replaceFragment(new ViewVehicleTripFragment(),true,bundle);
//                        homeActivity.replaceFragment(new AddExpenseFragment(),false,bundle);
                    }
                }

                @Override
                public void onInActiveClicked(VehicleItem vehicleItem) {

                }
            }, true);
            binding.vehiclesRecycler.setAdapter(vehiclesAdapter);
        }
    }
}