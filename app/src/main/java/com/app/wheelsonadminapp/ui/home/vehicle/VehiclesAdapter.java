package com.app.wheelsonadminapp.ui.home.vehicle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.wheelsonadminapp.R;
import com.app.wheelsonadminapp.model.auth.vehicle.VehicleItem;
import com.app.wheelsonadminapp.util.AppConstants;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class VehiclesAdapter extends RecyclerView.Adapter<VehiclesAdapter.VehicleViewModel> {

    List<VehicleItem>vehicleModels;
    Context mContext;
    VehicleClickListener vehicleClickListener;
    String imgPath;
    boolean fromManageTrips;

    public VehiclesAdapter(List<VehicleItem> vehicleModels, Context mContext, String imgPath, VehicleClickListener vehicleClickListener, boolean fromManageTrips) {
        this.vehicleModels = vehicleModels;
        this.mContext = mContext;
        this.vehicleClickListener = vehicleClickListener;
        this.imgPath = imgPath;
        this.fromManageTrips=fromManageTrips;
    }

    @NonNull
    @Override
    public VehicleViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vehicles_recycler_row, parent, false);
        return new VehicleViewModel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleViewModel holder, int position) {
        VehicleItem vehicleItem = vehicleModels.get(position);
        holder.vehicleName.setText(vehicleItem.getModel());
        holder.brand.setText("Brand : "+vehicleItem.getBrand());
        holder.vehicleNo.setText("Reg No : "+vehicleItem.getRegno());
        holder.textInsurance.setText("Insurance Till : "+vehicleItem.getInsurancedate());
        holder.textPollution.setText("Pollution Till : "+vehicleItem.getPollutiondate());
        holder.textSeats.setText("Seats : "+vehicleItem.getSeat());
        holder.textSpeedReading.setText("Speedometer Reading : "+vehicleItem.getStartKm());
        if(!fromManageTrips){
            if(vehicleItem.getStatus().equals("1")){
                holder.textViewInactive.setVisibility(View.VISIBLE);
            }else {
                holder.textViewInactive.setVisibility(View.GONE);
            }
        }else {
            holder.textViewInactive.setVisibility(View.GONE);
        }

        Picasso.get().load(AppConstants.SERVER_URL+imgPath+vehicleItem.getTaximg())
                .centerCrop().placeholder(R.drawable.app_logo).resize(125,125).into(holder.imgProfile);
        /*holder.parentCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vehicleClickListener.onVehicleClicked(vehicleItem,imgPath);
            }
        });*/

        holder.textViewInactive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vehicleClickListener.onInActiveClicked(vehicleItem);
            }
        });
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vehicleClickListener.onVehicleClicked(vehicleItem,imgPath);
            }
        });
    }

    @Override
    public int getItemCount() {
        return vehicleModels.size();
    }

    class VehicleViewModel  extends RecyclerView.ViewHolder{

        TextView vehicleName,brand,vehicleNo,textInsurance,textPollution,textSeats,textView,textSpeedReading,textViewInactive;
        MaterialCardView parentCardView;
        ImageView imgProfile;

        public VehicleViewModel(@NonNull View itemView) {
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
            textSpeedReading=itemView.findViewById(R.id.textSpeedReading);
            textViewInactive=itemView.findViewById(R.id.textViewInactive);
        }
    }

    public interface VehicleClickListener{
        void onVehicleClicked(VehicleItem vehicleItem,String imgPath);
        void onInActiveClicked(VehicleItem vehicleItem);
    }
}
