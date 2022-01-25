package com.app.wheelsonadminapp.ui.home.driver;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.wheelsonadminapp.R;
import com.app.wheelsonadminapp.model.DriverModel;
import com.app.wheelsonadminapp.model.driver.DriverItem;
import com.app.wheelsonadminapp.ui.home.HomeTripsAdapter;
import com.app.wheelsonadminapp.util.AppConstants;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DriverAdapter extends RecyclerView.Adapter<DriverAdapter.DriverViewModel>{

    List<DriverItem>driverModels;
    String imgPath;
    Context context;
    DriverClickListener driverClickListener;

    public DriverAdapter(List<DriverItem> driverModels, Context context,String imgPath,DriverClickListener driverClickListener) {
        this.driverModels = driverModels;
        this.context = context;
        this.imgPath = imgPath;
        this.driverClickListener =  driverClickListener;
    }

    @NonNull
    @Override
    public DriverViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drivers_recycler_row, parent, false);
        return new DriverViewModel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DriverViewModel holder, int position) {
        DriverItem driverModel = driverModels.get(position);
        holder.textName.setText(driverModel.getName());
        holder.textLicenceNo.setText("Licence No : "+driverModel.getLicenseno());
        holder.textMobile.setText("Mobile No : "+driverModel.getMobile());
        holder.licenceValidity.setText("Valid Till : "+driverModel.getLicensevalidity());
        holder.textCategory.setText("Category : "+driverModel.getLicensecategory());
        holder.textExperience.setText("Experience : "+driverModel.getExperience());
        String imgUrl = AppConstants.SERVER_URL+imgPath+driverModel.getDriverimg();
        Picasso.get().load(imgUrl)
                .centerCrop().placeholder(R.drawable.app_logo).resize(125,125).into(holder.imgProfile);
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                driverClickListener.onDriverClicked(driverModel,imgPath);
            }
        });
        holder.parentCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                driverClickListener.onDriverClicked(driverModel,imgPath);
            }
        });
    }

    @Override
    public int getItemCount() {
        return driverModels.size();
    }

    class DriverViewModel  extends RecyclerView.ViewHolder{

        TextView textName,textLicenceNo,textMobile,licenceValidity,textCategory,textExperience,textView;
        MaterialCardView parentCardView;
        ImageView imgProfile;

        public DriverViewModel(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textName);
            textLicenceNo = itemView.findViewById(R.id.textLicenceNo);
            textMobile = itemView.findViewById(R.id.textMobile);
            licenceValidity = itemView.findViewById(R.id.licenceValidity);
            textCategory = itemView.findViewById(R.id.textCategory);
            textExperience = itemView.findViewById(R.id.textExperience);
            textView = itemView.findViewById(R.id.textView);
            parentCardView = itemView.findViewById(R.id.parentCardView);
            imgProfile = itemView.findViewById(R.id.imgProfile);
        }
    }

    interface DriverClickListener{
        void onDriverClicked(DriverItem driverItem,String imgPath);
    }
}
