package com.app.wheelsonadminapp.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.wheelsonadminapp.R;
import com.app.wheelsonadminapp.model.TripModel;
import com.app.wheelsonadminapp.model.trip.TripItem;
import com.app.wheelsonadminapp.model.trip.triplist.TripListItem;
import com.app.wheelsonadminapp.model.trip.triplist.TripLiveListItem;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class HomeTripsAdapter extends RecyclerView.Adapter<HomeTripsAdapter.TripsViewHolder> {

    List<TripLiveListItem>tripModels;
    Context context;
    HomeTripClickListener homeTripClickListener;

    public HomeTripsAdapter(List<TripLiveListItem> tripModels, Context context,HomeTripClickListener homeTripClickListener) {
        this.tripModels = tripModels;
        this.context = context;
        this.homeTripClickListener = homeTripClickListener;
    }

    @NonNull
    @Override
    public TripsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trips_recycler_row, parent, false);
        return new TripsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripsViewHolder holder, int position) {
        TripLiveListItem tripItem = tripModels.get(position);
        holder.textTripFromTo.setText(tripItem.getFromlocation() +" to "+tripItem.getTolocation());
        holder.textStartDate.setText("Start Date : "+tripItem.getStartdate());
        holder.textEndDate.setText("End Date : "+tripItem.getEnddate());
        holder.textDriverName.setText("Driver : "+tripItem.getDrivername());
        holder.textVehicleName.setText("Vehicle : "+tripItem.getVehiclebrand()+" "+tripItem.getVehiclemodel());
        holder.textAmount.setText(tripItem.getAmount());
        holder.parentCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeTripClickListener.OnTripClicked(tripItem);
            }
        });
        holder.textCheckStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeTripClickListener.OnTripClicked(tripItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tripModels.size();
    }

    class TripsViewHolder extends RecyclerView.ViewHolder{

        TextView textTripFromTo,textStartDate,textEndDate,textDriverName,textVehicleName,textAmount,textCheckStatus;
        MaterialCardView parentCardView;

        public TripsViewHolder(@NonNull View itemView) {
            super(itemView);
            textTripFromTo = itemView.findViewById(R.id.textTripFromTo);
            textStartDate = itemView.findViewById(R.id.textStartDate);
            textEndDate = itemView.findViewById(R.id.textEndDate);
            textDriverName = itemView.findViewById(R.id.textDriverName);
            textVehicleName = itemView.findViewById(R.id.textVehicleName);
            textAmount = itemView.findViewById(R.id.textAmount);
            textCheckStatus = itemView.findViewById(R.id.textCheckStatus);
            parentCardView = itemView.findViewById(R.id.parentCardView);
        }
    }

    interface HomeTripClickListener{
        void OnTripClicked(TripLiveListItem tripItem);
    }
}
