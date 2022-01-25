package com.app.wheelsonadminapp.ui.home.service;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.app.wheelsonadminapp.R;
import com.app.wheelsonadminapp.data.db.AppRepository;
import com.app.wheelsonadminapp.data.network.ApiService;
import com.app.wheelsonadminapp.data.network.RetrofitClientInstance;
import com.app.wheelsonadminapp.databinding.FragmentAddServiceBinding;
import com.app.wheelsonadminapp.model.auth.vehicle.VehicleItem;
import com.app.wheelsonadminapp.model.auth.vehicle.VehicleTypeResponse;
import com.app.wheelsonadminapp.model.service.ServiceItem;
import com.app.wheelsonadminapp.model.service.ServiceItemResponse;
import com.app.wheelsonadminapp.model.service.VehicleServiceItem;
import com.app.wheelsonadminapp.model.service.vehicle_server_service.VehicleServiceListResponse;
import com.app.wheelsonadminapp.ui.home.HomeActivity;
import com.app.wheelsonadminapp.util.AppConstants;
import com.app.wheelsonadminapp.util.MessageProgressDialog;
import com.app.wheelsonadminapp.util.NetworkUtility;
import com.app.wheelsonadminapp.util.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddServiceFragment extends Fragment implements View.OnClickListener,ListDataAdapter.RecyclerViewItemClickListener,ServiceRecyclerAdapter.ServiceItemClickListener {

    FragmentAddServiceBinding addServiceBinding;
    HomeActivity homeActivity;
    CustomListViewDialog customDialog;
    VehicleItem vehicleItem;
    String imgPath;
    ServiceRecyclerAdapter serviceRecyclerAdapter;
    List<VehicleServiceItem>serviceItems;
    AppRepository appRepository;
    List<ServiceItem> serviceItemListFromServer;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeActivity = (HomeActivity)getActivity();
        if(getArguments()!=null && getArguments().getParcelable(AppConstants.VEHICLE)!=null){
            vehicleItem = getArguments().getParcelable(AppConstants.VEHICLE);
            imgPath =  getArguments().getString(AppConstants.IMAGE_PATH);
        }
        serviceItemListFromServer = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        addServiceBinding = FragmentAddServiceBinding.inflate(inflater,container,false);
        addServiceBinding.fabServiceItem.setOnClickListener(this);
        addServiceBinding.btAdd.setOnClickListener(this);
        addServiceBinding.btClose.setOnClickListener(this);
        appRepository = new AppRepository(getActivity());
        if(vehicleItem!=null){
            addServiceBinding.textVehicleName.setText(vehicleItem.getBrand()+" "+vehicleItem.getModel());
            addServiceBinding.textManage.setText(vehicleItem.getRegno());
        }
        addServiceBinding.serviceRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        serviceItems = new ArrayList<>();
        serviceRecyclerAdapter = new ServiceRecyclerAdapter(serviceItems,getActivity(),this);
        addServiceBinding.serviceRecycler.setAdapter(serviceRecyclerAdapter);
        return addServiceBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        addServiceBinding = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fabServiceItem:
                if(serviceItems.size()==0){
                    homeActivity.showSuccessToast("Please add atleast one service item to save!");
                }else {
                    callSaveServiceApi();
                }
                break;
            case R.id.btAdd:
                showListDialog(serviceItemListFromServer);
                break;
            case R.id.btClose:
                homeActivity.onBackPressed();
                break;
        }
    }

    private void getServiceItems(){
        if(NetworkUtility.isOnline(getActivity())){
            MessageProgressDialog.getInstance().show(getActivity());
            Call<ServiceItemResponse> serviceItemResponseCall = RetrofitClientInstance.getApiService().getServiceItems();
            serviceItemResponseCall.enqueue(new Callback<ServiceItemResponse>() {
                @Override
                public void onResponse(Call<ServiceItemResponse> call, Response<ServiceItemResponse> response) {
                    MessageProgressDialog.getInstance().dismiss();
                    getVehicleServiceItemsFromServer();
                    if(response.code() == 200 && response.body()!=null){
                        if(response.body().getService()!=null && response.body().getService().size()!=0){
                            serviceItemListFromServer = response.body().getService();
                        }else {
                            homeActivity.showErrorToast(getString(R.string.something_wrong));
                        }
                    }else {
                        homeActivity.showErrorToast(getString(R.string.something_wrong));
                    }
                }

                @Override
                public void onFailure(Call<ServiceItemResponse> call, Throwable t) {
                    MessageProgressDialog.getInstance().dismiss();
                }
            });
        }else {
            homeActivity.showErrorToast(getString(R.string.no_internet));
        }
    }

    private void getVehicleServiceItemsFromServer(){
        if(NetworkUtility.isOnline(getActivity())){
            MessageProgressDialog.getInstance().show(getActivity());
            JsonObject inputObject = new JsonObject();
            inputObject.addProperty("vehicleid",vehicleItem.getId());
            Call<VehicleServiceListResponse> serviceItemResponseCall = RetrofitClientInstance.getApiService().getServiceListByVehicleId(inputObject);
            serviceItemResponseCall.enqueue(new Callback<VehicleServiceListResponse>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(Call<VehicleServiceListResponse> call, Response<VehicleServiceListResponse> response) {
                    MessageProgressDialog.getInstance().dismiss();
                    if(response.code() == 200 && response.body()!=null){
                        if(response.body().getServiceDetails()!=null && response.body().getServiceDetails().size()!=0){
                                for (int i=0;i<response.body().getServiceDetails().size();i++){
                                    VehicleServiceItem vehicleServiceItem =
                                            Utils.toVehicleServiceItem(response.body().getServiceDetails().get(i),
                                                    appRepository.getUser().userId);
                                    serviceItems.add(vehicleServiceItem);
                                }
                                serviceRecyclerAdapter.notifyDataSetChanged();
                        }else if(response.body().getStatus() ==0){

                        }else {
                            homeActivity.showErrorToast(getString(R.string.something_wrong));
                        }
                    }else {
                        homeActivity.showErrorToast(getString(R.string.something_wrong));
                    }
                }

                @Override
                public void onFailure(Call<VehicleServiceListResponse> call, Throwable t) {
                    MessageProgressDialog.getInstance().dismiss();
                }
            });
        }else {
            homeActivity.showErrorToast(getString(R.string.no_internet));
        }
    }

    public void showListDialog(List<ServiceItem>serviceItems) {
        ListDataAdapter dataAdapter = new ListDataAdapter(serviceItems, this);
        customDialog = new CustomListViewDialog(getActivity(), dataAdapter);
        customDialog.show();
        customDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void clickOnItem(ServiceItem data) {
        if(data.getServicename().trim().endsWith("(Date)")){
            showDateAddDialog(data.getServicename(),data.getServiceid());
        }else {
            showKMAddDialog(data.getServicename(),data.getServiceid());
        }
        if (customDialog != null){
            customDialog.dismiss();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void showKMAddDialog(String serviceName,String serviceId){
        Dialog dialog = new Dialog(getActivity(),R.style.PauseDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.km_dialog);
        Button btAdd = dialog.findViewById(R.id.btAdd);
        EditText etType = dialog.findViewById(R.id.etType);
        TextView textServiceName = dialog.findViewById(R.id.textServiceName);
        textServiceName.setText(serviceName);
        btAdd.setOnClickListener(v -> {
            if(etType.getText().length()!=0){
                if(checkAlreadyAdded(serviceId)){
                    homeActivity.showErrorToast("Service item already added !");
                }else {
                    VehicleServiceItem vehicleServiceItem = new VehicleServiceItem();
                    vehicleServiceItem.setServiceValue(etType.getText().toString());
                    vehicleServiceItem.setServiceKM(etType.getText().toString());
                    vehicleServiceItem.setServiceDate("");
                    vehicleServiceItem.setServiceTypeId(serviceId);
                    vehicleServiceItem.setServiceName(serviceName);
                    vehicleServiceItem.setVehicleId(vehicleItem.getId());
                    vehicleServiceItem.setTravelsId(vehicleItem.getTravelsid());
                    callAddServiceApi(vehicleServiceItem);
                    serviceItems.add(vehicleServiceItem);
                    serviceRecyclerAdapter.notifyDataSetChanged();
                }

            }else {
                homeActivity.showErrorToast("Please enter the Kilometer");
            }
            dialog.dismiss();
        });
        Objects.requireNonNull(dialog.getWindow()).getDecorView().setBackgroundColor(Color.TRANSPARENT);
        dialog.show();
    }

    private void showKMEditDialog(VehicleServiceItem vServiceItem){
        Dialog dialog = new Dialog(getActivity(),R.style.PauseDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.km_edit_dialog);
        Button btUpdate = dialog.findViewById(R.id.btAdd);
        Button btCancel = dialog.findViewById(R.id.btCancel);
        EditText etType = dialog.findViewById(R.id.etType);
        TextView textServiceName = dialog.findViewById(R.id.textServiceName);
        textServiceName.setText(vServiceItem.getServiceName());
        etType.setText(vServiceItem.getServiceKM());
        btUpdate.setOnClickListener(v -> {
            if(etType.getText().length()!=0){
                vServiceItem.setServiceValue(etType.getText().toString());
                vServiceItem.setServiceKM(etType.getText().toString());
                vServiceItem.setServiceDate("");
                callUpdateServiceApi(vServiceItem);
                serviceRecyclerAdapter.notifyDataSetChanged();

            }else {
                homeActivity.showErrorToast("Please enter the Kilometer");
            }
            dialog.dismiss();
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        Objects.requireNonNull(dialog.getWindow()).getDecorView().setBackgroundColor(Color.TRANSPARENT);
        dialog.show();
    }

    private boolean checkAlreadyAdded(String serviceId){
        boolean status = false;
        for(VehicleServiceItem vehicleServiceItem : serviceItems){
            if(vehicleServiceItem.getServiceTypeId().equals(serviceId)){
                status = true;
                break;
            }
        }
        return status;
    }



    @SuppressLint("NotifyDataSetChanged")
    private void showDateAddDialog(String serviceName,String serviceId){
        final Calendar myCalendar = Calendar.getInstance();
        Dialog dialog = new Dialog(getActivity(),R.style.PauseDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.date_add_dialog);
        Button btAdd = dialog.findViewById(R.id.btAdd);
        EditText etType = dialog.findViewById(R.id.etType);
        TextView textServiceName = dialog.findViewById(R.id.textServiceName);
        textServiceName.setText(serviceName);

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                etType.setText(sdf.format(myCalendar.getTime()));
            }

        };


        etType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }

        });
        btAdd.setOnClickListener(v -> {
            if(etType.getText().length()!=0){
                VehicleServiceItem vehicleServiceItem = new VehicleServiceItem();
                vehicleServiceItem.setServiceDate(etType.getText().toString());
                vehicleServiceItem.setServiceKM("");
                vehicleServiceItem.setServiceValue(etType.getText().toString());
                vehicleServiceItem.setServiceTypeId(serviceId);
                vehicleServiceItem.setServiceName(serviceName);
                vehicleServiceItem.setVehicleId(vehicleItem.getId());
                vehicleServiceItem.setTravelsId(vehicleItem.getTravelsid());
                serviceItems.add(vehicleServiceItem);
                callAddServiceApi(vehicleServiceItem);
                serviceRecyclerAdapter.notifyDataSetChanged();
            }else {
                homeActivity.showErrorToast("Please enter the Date");
            }
            dialog.dismiss();
        });
        Objects.requireNonNull(dialog.getWindow()).getDecorView().setBackgroundColor(Color.TRANSPARENT);
        dialog.show();
    }

    private void showDateEditDialog(VehicleServiceItem vServiceItem){
        final Calendar myCalendar = Calendar.getInstance();
        Dialog dialog = new Dialog(getActivity(),R.style.PauseDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.date_edit_dialog);
        Button btUpdate = dialog.findViewById(R.id.btAdd);
        Button btCancel = dialog.findViewById(R.id.btCancel);
        EditText etType = dialog.findViewById(R.id.etType);
        TextView textServiceName = dialog.findViewById(R.id.textServiceName);
        textServiceName.setText(vServiceItem.getServiceName());
        etType.setText(vServiceItem.getServiceDate());

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                etType.setText(sdf.format(myCalendar.getTime()));
            }

        };


        etType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }

        });
        btUpdate.setOnClickListener(v -> {
            if(etType.getText().length()!=0){
                vServiceItem.setServiceDate(etType.getText().toString());
                vServiceItem.setServiceKM("0");
                vServiceItem.setServiceValue(etType.getText().toString());
                callUpdateServiceApi(vServiceItem);
                serviceRecyclerAdapter.notifyDataSetChanged();
            }else {
                homeActivity.showErrorToast("Please enter the Date");
            }
            dialog.dismiss();
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        Objects.requireNonNull(dialog.getWindow()).getDecorView().setBackgroundColor(Color.TRANSPARENT);
        dialog.show();
    }

    @Override
    public void onItemClicked(int position, VehicleServiceItem vehicleServiceItem) {
        showDeleteDialog(vehicleServiceItem,position);
    }

    @Override
    public void onItemSelected(int position, VehicleServiceItem vehicleServiceItem) {
        if(vehicleServiceItem.getServiceKM().equals("0")){
            showDateEditDialog(vehicleServiceItem);
        }else{
            showKMEditDialog(vehicleServiceItem);
        }
    }

    private void showDeleteDialog(VehicleServiceItem vServiceItem,int position){
        Dialog dialog = new Dialog(getActivity(),R.style.PauseDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.remove_dialog);
        Button btCancel = dialog.findViewById(R.id.btCancel);
        Button btDelete = dialog.findViewById(R.id.btDelete);
        TextView textServiceName = dialog.findViewById(R.id.textServiceName);
        textServiceName.setText(vServiceItem.getServiceName());
        btDelete.setOnClickListener(v -> {
            callDeleteServiceApi(vServiceItem,position);
            dialog.dismiss();
        });
        btCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        Objects.requireNonNull(dialog.getWindow()).getDecorView().setBackgroundColor(Color.TRANSPARENT);
        dialog.show();
    }

    private void callSaveServiceApi(){
        if(NetworkUtility.isOnline(getActivity())) {
            MessageProgressDialog.getInstance().show(getActivity());
            Call<JsonObject>saveServiceCall = RetrofitClientInstance.getApiService().saveService(getJsonInput());
            saveServiceCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if(response.code() == 200 && response.body()!=null && response.body().has("status")){
                        int status = response.body().get("status").getAsInt();
                        if(status == 1){
                            homeActivity.showSuccessToast("Service added successfully!");
                            homeActivity.onBackPressed();
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                }
            });
        }else {
            homeActivity.showErrorToast(getString(R.string.no_internet));
        }
    }

    private void callAddServiceApi(VehicleServiceItem vServiceItem){
        if(NetworkUtility.isOnline(getActivity())) {
            JsonObject serviceObject = new JsonObject();
            serviceObject.addProperty("travelsid",vServiceItem.getTravelsId());
            serviceObject.addProperty("vehicleid",vServiceItem.getVehicleId());
            serviceObject.addProperty("servicetypeid",vServiceItem.getServiceTypeId());
            serviceObject.addProperty("servicekm",vServiceItem.getServiceKM());
            serviceObject.addProperty("servicedetails",vServiceItem.getServiceName());
            serviceObject.addProperty("servicedate",vServiceItem.getServiceDate());
            MessageProgressDialog.getInstance().show(getActivity());
            Call<JsonObject>saveServiceCall = RetrofitClientInstance.getApiService().addService(serviceObject);
            saveServiceCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    MessageProgressDialog.getInstance().dismiss();
                    if(response.code() == 200 && response.body()!=null && response.body().has("status")){
                        int status = response.body().get("status").getAsInt();
                        if(status == 1){
//                            homeActivity.showSuccessToast("Service added successfully!");
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


    private void callUpdateServiceApi(VehicleServiceItem vehicleServiceItem){
        if(NetworkUtility.isOnline(getActivity())) {
            JsonObject inputObject = new JsonObject();
            inputObject.addProperty("serviceid",vehicleServiceItem.getServiceListId());
            inputObject.addProperty("travelsid",vehicleServiceItem.getTravelsId());
            inputObject.addProperty("vehicleid",vehicleServiceItem.getVehicleId());
            inputObject.addProperty("servicetypeid",vehicleServiceItem.getServiceTypeId());
            if(vehicleServiceItem.getServiceKM()==null || vehicleServiceItem.getServiceKM().equals("0")){
                inputObject.addProperty("servicedate",vehicleServiceItem.getServiceDate());
            }else {
                inputObject.addProperty("servicekm",vehicleServiceItem.getServiceKM());
            }
            inputObject.addProperty("servicedetails",vehicleServiceItem.getServiceName());
            MessageProgressDialog.getInstance().show(getActivity());
            Call<JsonObject>saveServiceCall = RetrofitClientInstance.getApiService().updateService(inputObject);
            saveServiceCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    MessageProgressDialog.getInstance().dismiss();
                    if(response.code() == 200 && response.body()!=null && response.body().has("status")){
                        int status = response.body().get("status").getAsInt();
                        if(status == 1){
                            homeActivity.showSuccessToast("Service updated successfully!");
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

    private void callDeleteServiceApi(VehicleServiceItem vehicleServiceItem,int position){
        if(NetworkUtility.isOnline(getActivity())) {
            JsonObject inputObject = new JsonObject();
            inputObject.addProperty("serviceid",vehicleServiceItem.getServiceListId());
            inputObject.addProperty("servicetypeid",vehicleServiceItem.getServiceTypeId());
            MessageProgressDialog.getInstance().show(getActivity());
            Call<JsonObject>deleteServiceCall = RetrofitClientInstance.getApiService().deleteServiceItem(inputObject);
            deleteServiceCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    MessageProgressDialog.getInstance().dismiss();
                    if(response.code() == 200 && response.body()!=null && response.body().has("status")){
                        int status = response.body().get("status").getAsInt();
                        if(status == 1){
                            serviceItems.remove(position);
                            serviceRecyclerAdapter.notifyItemRemoved(position);
//                            homeActivity.showSuccessToast("Service deleted successfully!");
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    MessageProgressDialog.getInstance().dismiss();
                    homeActivity.showErrorToast(getString(R.string.something_wrong));
                }
            });
        }else {
            homeActivity.showErrorToast(getString(R.string.no_internet));
        }
    }


    private JsonArray getJsonInput(){
        JsonArray serviceArray = new JsonArray();
        for (VehicleServiceItem serviceItem : serviceItems){
            JsonObject serviceObject = new JsonObject();
            serviceObject.addProperty("travelsid",serviceItem.getTravelsId());
            serviceObject.addProperty("vehicleid",serviceItem.getVehicleId());
            serviceObject.addProperty("servicetypeid",serviceItem.getServiceTypeId());
            serviceObject.addProperty("servicekm",serviceItem.getServiceKM());
            serviceObject.addProperty("servicedetails",serviceItem.getServiceName());
            serviceObject.addProperty("servicedate",serviceItem.getServiceDate());
            serviceArray.add(serviceObject);
        }
        return serviceArray;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getServiceItems();
    }
}