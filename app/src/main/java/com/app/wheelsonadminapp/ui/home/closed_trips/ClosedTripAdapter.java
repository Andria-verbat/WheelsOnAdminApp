package com.app.wheelsonadminapp.ui.home.closed_trips;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.wheelsonadminapp.R;
import com.app.wheelsonadminapp.model.driver.DriverItem;
import com.app.wheelsonadminapp.model.trip.closed_trips.ClosedTripItems;
import com.app.wheelsonadminapp.ui.home.driver.DriverAdapter;
import com.app.wheelsonadminapp.util.AppConstants;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Andria on 2/4/2022.
 */
public class ClosedTripAdapter extends RecyclerView.Adapter<ClosedTripAdapter.ClosedTripViewModel>{

    List<ClosedTripItems> closedTripItems;
    String imgPath;
    Context context;
    ClosedTripClick closedTripClick;


    public ClosedTripAdapter(List<ClosedTripItems> closedTripItems, Context context, String imgPath,ClosedTripClick closedTripClick) {
        this.closedTripItems = closedTripItems;
        this.context = context;
        this.imgPath = imgPath;
        this.closedTripClick=closedTripClick;

    }

    @NonNull
    @Override
    public ClosedTripViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.closed_trip_recycler_row, parent, false);
        return new ClosedTripViewModel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClosedTripViewModel holder, int position) {
        ClosedTripItems closedTripItem = closedTripItems.get(position);
        holder.fromto.setText(closedTripItem.getFromlocation()+" To "+closedTripItem.getTolocation());
        holder.startDate.setText("Start Date : "+closedTripItem.getStartdate());
        holder.endDate.setText("End Date : "+closedTripItem.getEnddate());
        holder.startKM.setText("Start KM : "+closedTripItem.getStartingkm());
        holder.endKM.setText("End KM : "+closedTripItem.getEndkm());
        if(closedTripItem.getDrivername()!=null){
            holder.driverName.setText("Driver Name : "+closedTripItem.getDrivername());
        }else {
            holder.driverName.setText("Driver Name : "+"Not Assigned");
        }

       // String imgUrl = AppConstants.SERVER_URL+imgPath+driverModel.getDriverimg();
        /*Picasso.get().load(imgUrl)
                .centerCrop().placeholder(R.drawable.app_logo).resize(125,125).into(holder.imgProfile);*/

        holder.parentCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              closedTripClick.onClosedTripClicked(closedTripItem,imgPath);
            }
        });
    }

    @Override
    public int getItemCount() {
        return closedTripItems.size();
    }

    class ClosedTripViewModel  extends RecyclerView.ViewHolder{

        TextView fromto,startDate,endDate,startKM,endKM,driverName;
        MaterialCardView parentCardView;
        ImageView imgProfile;

        public ClosedTripViewModel(@NonNull View itemView) {
            super(itemView);
            fromto = itemView.findViewById(R.id.fromto);
            startDate = itemView.findViewById(R.id.startDate);
            endDate = itemView.findViewById(R.id.endDate);
            startKM = itemView.findViewById(R.id.startKM);
            endKM = itemView.findViewById(R.id.endKM);
            driverName = itemView.findViewById(R.id.driverName);

            parentCardView = itemView.findViewById(R.id.parentCardView);

        }
    }


    interface ClosedTripClick{
        void onClosedTripClicked(ClosedTripItems closedTripItem,String imgPath);
    }
}
