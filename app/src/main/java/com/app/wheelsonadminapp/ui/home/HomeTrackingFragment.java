package com.app.wheelsonadminapp.ui.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.app.wheelsonadminapp.R;
import com.app.wheelsonadminapp.data.network.RetrofitClientInstance;
import com.app.wheelsonadminapp.databinding.FragmentHomeTrackingBinding;
import com.app.wheelsonadminapp.model.trip.trip_details.TripDetailItem;
import com.app.wheelsonadminapp.model.trip.trip_track.TrackResponse;
import com.app.wheelsonadminapp.util.AppConstants;
import com.app.wheelsonadminapp.util.NetworkUtility;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeTrackingFragment extends Fragment implements OnMapReadyCallback {

    FragmentHomeTrackingBinding binding;
    HomeActivity homeActivity;
    TripDetailItem tripDetailItem;
    private GoogleMap mMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeActivity = (HomeActivity)getActivity();
        if(getArguments()!=null && getArguments().getParcelable(AppConstants.TRIP_DETAILS)!=null){
            tripDetailItem = getArguments().getParcelable(AppConstants.TRIP_DETAILS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeTrackingBinding.inflate(inflater,container,false);
        FragmentManager fm = getChildFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment)fm.findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {
        mMap = googleMap;
        if(tripDetailItem!=null){
            getTrackData(tripDetailItem.getTripid());

            GoogleDirection.withServerKey(getString(R.string.google_map_api_key))
                    .from(new LatLng(Double.parseDouble(tripDetailItem.getFromlat()), Double.parseDouble(tripDetailItem.getFromlon())))
                    .to(new LatLng(Double.parseDouble(tripDetailItem.getTolat()), Double.parseDouble(tripDetailItem.getTolon())))
                    .execute(new DirectionCallback() {
                        @Override
                        public void onDirectionSuccess(@Nullable Direction direction) {
                            mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(tripDetailItem.getFromlat()), Double.parseDouble(tripDetailItem.getFromlon()))));
                            mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(tripDetailItem.getTolat()), Double.parseDouble(tripDetailItem.getTolon()))));
                            List<Route> routeList = direction.getRouteList();
                            if(routeList!=null && routeList.size()!=0){
                                mMap.addPolyline(DirectionConverter.createPolyline(getActivity(),
                                        routeList.get(0).getLegList().get(0).getDirectionPoint(),
                                        5,
                                        Color.RED));

                                setCameraWithCoordinationBounds(routeList.get(0),mMap);
                            }

                        }

                        @Override
                        public void onDirectionFailure(@NonNull Throwable t) {

                        }
                    });
        }

    }

    public void setCameraWithCoordinationBounds(Route route,GoogleMap map){
        LatLng southWest =  route.getBound().getSouthwestCoordination().getCoordination();
        LatLng northWest =  route.getBound().getNortheastCoordination().getCoordination();
        LatLngBounds bounds = new LatLngBounds(southWest,northWest);
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds,100));
    }

    private void getTrackData(String tripId){
        if(NetworkUtility.isOnline(homeActivity)){
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("tripid",tripId);
            Call<TrackResponse>responseCall = RetrofitClientInstance.getApiService().getTrackData(jsonObject);
            responseCall.enqueue(new Callback<TrackResponse>() {
                @Override
                public void onResponse(Call<TrackResponse> call, Response<TrackResponse> response) {
                    if(response.code() == 200 && response.body()!=null){
                        if(response.body().getStatus() == 1){
                            addCustomMarker(response.body());
                        }else {
                            homeActivity.showErrorToast(getString(R.string.something_wrong));
                        }
                    }else {
                        homeActivity.showErrorToast(getString(R.string.something_wrong));
                    }
                }

                @Override
                public void onFailure(Call<TrackResponse> call, Throwable t) {
                    homeActivity.showErrorToast(getString(R.string.something_wrong));
                }
            });
        }else {
            homeActivity.showErrorToast(getString(R.string.no_internet));
        }
    }

    private void addCustomMarker(TrackResponse trackResponse){
        if(trackResponse.getTrip()!=null && trackResponse.getTrip().size()!=0){
            LatLng latLng = new LatLng(Double.parseDouble(trackResponse.getTrip().get(0).getLatitude()),
                    Double.parseDouble(trackResponse.getTrip().get(0).getLongitude()));
            mMap.addMarker(new MarkerOptions().position(latLng).title("Running")
                    // below line is use to add custom marker on our map.
                    .icon(BitmapFromVector(homeActivity, R.drawable.ic_baseline_car)));
        }
    }

    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        // below line is use to generate a drawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

        // below line is use to set bounds to our vector drawable.
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

        // below line is use to create a bitmap for our
        // drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        // below line is use to add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas);

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

}