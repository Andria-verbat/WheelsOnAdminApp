package com.app.wheelsonadminapp.ui.home.service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.wheelsonadminapp.R;
import com.app.wheelsonadminapp.model.service.VehicleServiceItem;

import java.util.List;

public class ServiceRecyclerAdapter extends RecyclerView.Adapter<ServiceRecyclerAdapter.ServiceViewModel> {

    List<VehicleServiceItem>serviceItems;
    Context context;
    ServiceItemClickListener serviceItemClickListener;

    public ServiceRecyclerAdapter(List<VehicleServiceItem> serviceItems, Context context, ServiceItemClickListener serviceItemClickListener) {
        this.serviceItems = serviceItems;
        this.context = context;
        this.serviceItemClickListener = serviceItemClickListener;
    }

    @NonNull
    @Override
    public ServiceViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_recycler_row, parent, false);
        return new ServiceViewModel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewModel holder, @SuppressLint("RecyclerView") int position) {
        VehicleServiceItem vehicleServiceItem = serviceItems.get(position);
        holder.textServiceName.setText(vehicleServiceItem.getServiceName());
        holder.textServiceValue.setText(vehicleServiceItem.getServiceValue());
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceItemClickListener.onItemClicked(position,vehicleServiceItem);
            }
        });
        holder.parentCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                serviceItemClickListener.onItemSelected(position,vehicleServiceItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return serviceItems.size();
    }

    class ServiceViewModel  extends RecyclerView.ViewHolder{

        TextView textServiceName,textServiceValue;
        ImageView imgDelete;
        CardView parentCardView;

        public ServiceViewModel(@NonNull View itemView) {
            super(itemView);
            textServiceName = itemView.findViewById(R.id.textName);
            textServiceValue = itemView.findViewById(R.id.textValue);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            parentCardView = itemView.findViewById(R.id.parentCardView);
        }
    }

    public interface ServiceItemClickListener{
        void onItemClicked(int position,VehicleServiceItem vehicleServiceItem);
        void onItemSelected(int position,VehicleServiceItem vehicleServiceItem);
    }
}
