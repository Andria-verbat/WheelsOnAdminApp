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
import com.app.wheelsonadminapp.data.network.RetrofitClientInstance;
import com.app.wheelsonadminapp.databinding.FragmentServiceListBinding;
import com.app.wheelsonadminapp.model.service.vehicle_service.ServiceListResponse;
import com.app.wheelsonadminapp.model.service.vehicle_service.ServicedetailsItem;
import com.app.wheelsonadminapp.ui.home.HomeActivity;
import com.app.wheelsonadminapp.util.MessageProgressDialog;
import com.app.wheelsonadminapp.util.NetworkUtility;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceListFragment extends Fragment implements View.OnClickListener, ServiceListAdapter.ServiceClickListener {

    FragmentServiceListBinding serviceListBinding;
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
        serviceListBinding = FragmentServiceListBinding.inflate(inflater,container,false);
        serviceListBinding.fabService.setOnClickListener(this);
        appRepository = new AppRepository(getActivity());
        return serviceListBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        serviceListBinding = null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getServiceList();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fabService:
                homeActivity.replaceFragment(new SelectVehicleFragment(),true,null);
                break;
        }
    }

    private void getServiceList(){
        if(NetworkUtility.isOnline(getActivity())){
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("travelsid",appRepository.getUser().getUserId());
            MessageProgressDialog.getInstance().show(getActivity());
            Call<ServiceListResponse> serviceListResponseCall = RetrofitClientInstance.getApiService().getServiceList(jsonObject);
            serviceListResponseCall.enqueue(new Callback<ServiceListResponse>() {
                @Override
                public void onResponse(Call<ServiceListResponse> call, Response<ServiceListResponse> response) {
                    MessageProgressDialog.getInstance().dismiss();
                    if(response.code() == 200 && response.body()!=null){
                        if(response.body().getStatus() ==1){
                            addToRecycler(response.body().getServicedetails());
                        }else {
                            homeActivity.showErrorToast(getString(R.string.something_wrong));
                        }
                    }else {
                        homeActivity.showErrorToast(getString(R.string.something_wrong));
                    }
                }

                @Override
                public void onFailure(Call<ServiceListResponse> call, Throwable t) {
                    homeActivity.showErrorToast(getString(R.string.something_wrong));
                    MessageProgressDialog.getInstance().dismiss();
                }
            });
        }else {
            homeActivity.showErrorToast(getString(R.string.no_internet));
        }
    }

    private void addToRecycler(List<ServicedetailsItem> serviceDetails){
        serviceListBinding.vehiclesRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        ServiceListAdapter serviceListAdapter = new ServiceListAdapter(serviceDetails,this);
        serviceListBinding.vehiclesRecycler.setAdapter(serviceListAdapter);
    }

    @Override
    public void onServiceClicked(ServicedetailsItem servicedetailsItem) {
        getServiceDetailsById(servicedetailsItem.getVehicleid());
    }

    private void getServiceDetailsById(String serviceId){
        if(NetworkUtility.isOnline(getActivity())){

        }else {
            homeActivity.showErrorToast(getString(R.string.no_internet));
        }
    }
}