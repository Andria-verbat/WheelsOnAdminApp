package com.app.wheelsonadminapp.ui.home.service;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.wheelsonadminapp.R;
import com.app.wheelsonadminapp.model.service.VehicleServiceItem;
import com.app.wheelsonadminapp.model.service.vehicle_service.ServicedetailsItem;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class ServiceListAdapter extends RecyclerView.Adapter<ServiceListAdapter.ServiceListViewHolder> {

    List<ServicedetailsItem> serviceDetailItems;
    ServiceClickListener serviceClickListener;

    public ServiceListAdapter(List<ServicedetailsItem> serviceDetailItems,ServiceClickListener serviceClickListener) {
        this.serviceDetailItems = serviceDetailItems;
        this.serviceClickListener = serviceClickListener;
    }

    @NonNull
    @Override
    public ServiceListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_list_recycler_row, parent, false);
        return new ServiceListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceListViewHolder holder, int position) {
        ServicedetailsItem servicedetailsItem = serviceDetailItems.get(position);
        holder.vehicleName.setText(servicedetailsItem.getModel());
        holder.brand.setText("Brand : "+servicedetailsItem.getBrand());
        holder.vehicleNo.setText("Reg No : "+servicedetailsItem.getRegno());
//        holder.textInsurance.setText("Insurance Till : "+servicedetailsItem.getInsurancedate());
//        holder.textPollution.setText("Pollution Till : "+servicedetailsItem.getPollutiondate());
//        holder.textSeats.setText("Seats : "+servicedetailsItem.getSeat());
    }

    @Override
    public int getItemCount() {
        return serviceDetailItems.size();
    }

    class ServiceListViewHolder extends RecyclerView.ViewHolder {

        TextView vehicleName,brand,vehicleNo,textInsurance,textPollution,textSeats,textView;
        MaterialCardView parentCardView;
        ImageView imgProfile;

        public ServiceListViewHolder(@NonNull View itemView) {
            super(itemView);
            vehicleName = itemView.findViewById(R.id.vehicleName);
            brand = itemView.findViewById(R.id.brand);
            vehicleNo = itemView.findViewById(R.id.vehicleNo);
            textInsurance = itemView.findViewById(R.id.textInsurance);
            textPollution = itemView.findViewById(R.id.textPollution);
            textSeats = itemView.findViewById(R.id.textSeats);
            textView = itemView.findViewById(R.id.textView);
            parentCardView = itemView.findViewById(R.id.parentCardView);
            imgProfile = itemView.findViewById(R.id.imgProfile);
        }
    }

    public interface ServiceClickListener{
        void onServiceClicked(ServicedetailsItem servicedetailsItem);
    }
}
