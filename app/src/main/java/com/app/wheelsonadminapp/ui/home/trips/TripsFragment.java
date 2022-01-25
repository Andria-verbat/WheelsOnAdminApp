package com.app.wheelsonadminapp.ui.home.trips;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.app.wheelsonadminapp.R;
import com.app.wheelsonadminapp.data.db.AppRepository;
import com.app.wheelsonadminapp.data.network.ApiService;
import com.app.wheelsonadminapp.data.network.RetrofitClientInstance;
import com.app.wheelsonadminapp.databinding.FragmentTripsBinding;
import com.app.wheelsonadminapp.model.TripModel;
import com.app.wheelsonadminapp.model.driver.DriverResponse;
import com.app.wheelsonadminapp.model.trip.TripItem;
import com.app.wheelsonadminapp.model.trip.TripResponse;
import com.app.wheelsonadminapp.ui.home.HomeActivity;
import com.app.wheelsonadminapp.ui.home.HomeTripsAdapter;
import com.app.wheelsonadminapp.ui.home.vehicle.ListVehicleFragment;
import com.app.wheelsonadminapp.util.MessageProgressDialog;
import com.app.wheelsonadminapp.util.NetworkUtility;
import com.app.wheelsonadminapp.util.horizontal_calendar.HorizontalCalendar;
import com.app.wheelsonadminapp.util.horizontal_calendar.utils.HorizontalCalendarListener;
import com.events.calendar.utils.EventsCalendarUtil;
import com.events.calendar.views.EventsCalendar;
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
import timber.log.Timber;

public class TripsFragment extends Fragment implements View.OnClickListener, TripsAdapter.TripClickListener {

    FragmentTripsBinding tripsBinding;
    HorizontalCalendar horizontalCalendar;
    HomeActivity homeActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeActivity = (HomeActivity)getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        tripsBinding = FragmentTripsBinding.inflate(inflater,container,false);

        /* start before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        /* end after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);

        horizontalCalendar = new HorizontalCalendar.Builder(tripsBinding.getRoot(), R.id.calendarView)
                .range(startDate, endDate)
                .configure()
                .formatTopText("MMM")
                .formatMiddleText("dd")
                .formatBottomText("EEE")
                .textSize(12f, 20f, 12f)
                .showTopText(true)
                .showBottomText(true)
                .end()
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
//                Toast.makeText(getContext(), DateFormat.format("EEE, MMM d, yyyy", date) + " is selected!", Toast.LENGTH_SHORT).show();
                String selectedDate = DateFormat.format("EEE, MMM d, yyyy", date)+"";
                getTripsByDate(getFormattedDate(date));
            }

        });

//        setTripsRecycler();

        tripsBinding.imgCalendar.setOnClickListener(this);
        tripsBinding.fabAdd.setOnClickListener(this);
        tripsBinding.btViewByVehicle.setOnClickListener(this);

        return tripsBinding.getRoot();
    }

    private String getFormattedDate(Calendar date){
        return (String) DateFormat.format("yyyy-MM-dd", date);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        tripsBinding = null;
    }

    private void showCalendarDialog() {
        Dialog dialog = new Dialog(getActivity(),R.style.PauseDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_calendar);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        EventsCalendar eventsCalendar = dialog.findViewById(R.id.eventsCalendar);
        eventsCalendar.setBackgroundColor(getActivity().getColor(R.color.button_dark));
        eventsCalendar.setSelectionColor(getActivity().getColor(R.color.blue_dark));
        eventsCalendar.setSelectionMode(EventsCalendarUtil.SINGLE_SELECTION);
        eventsCalendar.setWeekStartDay(Calendar.SUNDAY, false);
        /* start before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        /* end after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);
        eventsCalendar.setMonthRange(startDate,endDate);
        eventsCalendar.setCallback(new EventsCalendar.Callback() {
            @Override
            public void onDaySelected(Calendar calendar) {
                Log.i("TAG", "onDaySelected: "+calendar.toString());
                horizontalCalendar.selectDate(calendar,true);
                String myFormat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                Timber.i(sdf.format(calendar.getTime()));
                getTripsByDate(sdf.format(calendar.getTime()));
                dialog.dismiss();
            }

            @Override
            public void onDayLongPressed(Calendar calendar) {

            }

            @Override
            public void onMonthChanged(Calendar calendar) {

            }
        }).build();
        Objects.requireNonNull(dialog.getWindow()).getDecorView().setBackgroundColor(Color.TRANSPARENT);
        dialog.show();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.imgCalendar:
                showCalendarDialog();
                break;
            case R.id.fabAdd:
                homeActivity.replaceFragment(new AddTripFragment(),true,null);
                break;
            case R.id.btViewByVehicle:
                Bundle bundle = new Bundle();
                bundle.putBoolean("FROM_MANAGE_TRIP",true);
                homeActivity.replaceFragment(new ListVehicleFragment(),true,bundle);
                break;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Calendar myCalendar = Calendar.getInstance();
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        getTripsByDate(sdf.format(myCalendar.getTime()));
    }

    private void getTripsByDate(String date){
        if(NetworkUtility.isOnline(getActivity())){
            AppRepository appRepository = new AppRepository(getActivity());
            MessageProgressDialog.getInstance().show(getActivity());
            JsonObject inputObject = new JsonObject();
            inputObject.addProperty("travelsid",appRepository.getUser().getUserId());
            inputObject.addProperty("tripdate",date);
            Call<TripResponse> tripResponseCall = RetrofitClientInstance.getApiService().getTripsByDate(inputObject);
            tripResponseCall.enqueue(new Callback<TripResponse>() {
                @Override
                public void onResponse(Call<TripResponse> call, Response<TripResponse> response) {
                    MessageProgressDialog.getInstance().dismiss();
                    if(response.code() == 200 && response.body()!=null){
                        if(response.body().getStatus() == 1){
                            loadTripsToRecycler(response.body().getTrip());
                        }else {
                            tripsBinding.textNoTrips.setVisibility(View.VISIBLE);
                            tripsBinding.tripsRecycler.setVisibility(View.GONE);
                        }
                    }else {
                        homeActivity.showErrorToast(getString(R.string.something_wrong));
                    }
                }

                @Override
                public void onFailure(Call<TripResponse> call, Throwable t) {
                    homeActivity.showErrorToast(getString(R.string.something_wrong));
                    MessageProgressDialog.getInstance().dismiss();
                }
            });
        }else{
            homeActivity.showErrorToast(getString(R.string.no_internet));
        }
    }

    private void loadTripsToRecycler(List<TripItem>tripItems){
        if(tripItems.size() == 0){
            tripsBinding.textNoTrips.setVisibility(View.VISIBLE);
            tripsBinding.tripsRecycler.setVisibility(View.GONE);
        }else {
            tripsBinding.textNoTrips.setVisibility(View.GONE);
            tripsBinding.tripsRecycler.setVisibility(View.VISIBLE);
            TripsAdapter tripsAdapter = new TripsAdapter(tripItems,getActivity(),this);
            tripsBinding.tripsRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
            tripsBinding.tripsRecycler.setAdapter(tripsAdapter);
        }
    }

    @Override
    public void OnTripClicked(TripItem tripItem) {

    }
}