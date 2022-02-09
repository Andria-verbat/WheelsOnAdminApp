package com.app.wheelsonadminapp.ui.home.closed_trips;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.wheelsonadminapp.BaseActivity;
import com.app.wheelsonadminapp.R;
import com.app.wheelsonadminapp.data.db.AppRepository;
import com.app.wheelsonadminapp.data.network.ApiService;
import com.app.wheelsonadminapp.data.network.RetrofitClientInstance;
import com.app.wheelsonadminapp.databinding.FragmentCloseTripExpenseViewBinding;
import com.app.wheelsonadminapp.databinding.FragmentClosedTripBinding;
import com.app.wheelsonadminapp.model.expense.closedTrip.ClosedTripExpenseItem;
import com.app.wheelsonadminapp.model.expense.closedTrip.ClosedTripExpenseListResponse;
import com.app.wheelsonadminapp.model.expense.closedTrip.ClosedTripExpenseResponse;
import com.app.wheelsonadminapp.model.expense.closedTrip.ClosedTripExpenseResponseItem;
import com.app.wheelsonadminapp.model.trip.closed_trips.ClosedTripItems;
import com.app.wheelsonadminapp.model.trip.closed_trips.ClosedTripResponse;
import com.app.wheelsonadminapp.ui.home.HomeActivity;
import com.app.wheelsonadminapp.ui.home.driver.AddDriverFragment;
import com.app.wheelsonadminapp.util.AppConstants;
import com.app.wheelsonadminapp.util.MessageProgressDialog;
import com.app.wheelsonadminapp.util.NetworkUtility;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by Andria on 2/9/2022.
 */
public class CloseTripExpenseViewFragment extends Fragment implements ClosedTripExpenseAdapter.ExpenseClickListener {
    FragmentCloseTripExpenseViewBinding fragmentCloseTripExpenseViewBinding;
    BaseActivity homeActivity;
    String tripid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeActivity = (HomeActivity)getActivity();
        if(getArguments()!=null){
            tripid = getArguments().getString("tripid");
        }
        System.out.println("tripidddddd"+tripid);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentCloseTripExpenseViewBinding = FragmentCloseTripExpenseViewBinding.inflate(inflater,container,false);


        return fragmentCloseTripExpenseViewBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(NetworkUtility.isOnline(getActivity())){
            viewExpense();
        }else {
            homeActivity.showErrorToast(getString(R.string.no_internet));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentCloseTripExpenseViewBinding = null;
    }
    private void viewExpense() {
        if(NetworkUtility.isOnline(getActivity())){
            AppRepository appRepository = new AppRepository(getActivity());
            MessageProgressDialog.getInstance().show(getActivity());
            JsonObject inputObject = new JsonObject();
            inputObject.addProperty("tripid",tripid);
            Call<ClosedTripExpenseListResponse> tripResponseCall = RetrofitClientInstance.getApiService().getExpenseClosedTrips(inputObject);
            tripResponseCall.enqueue(new Callback<ClosedTripExpenseListResponse>() {
                @Override
                public void onResponse(Call<ClosedTripExpenseListResponse> call, Response<ClosedTripExpenseListResponse> response) {
                    MessageProgressDialog.getInstance().dismiss();
                    if(response.code() == 200 && response.body()!=null){
                        if(response.body().getStatus() == 1){
                            loadDataToRecycler(response.body());
                        }else {
                            fragmentCloseTripExpenseViewBinding.textNoVehicles.setVisibility(View.VISIBLE);
                            fragmentCloseTripExpenseViewBinding.closedTripExpensesRecycler.setVisibility(View.GONE);
                        }
                    }else {
                        homeActivity.showErrorToast(getString(R.string.something_wrong));
                    }
                }

                @Override
                public void onFailure(Call<ClosedTripExpenseListResponse> call, Throwable t) {
                    homeActivity.showErrorToast(getString(R.string.something_wrong));
                    MessageProgressDialog.getInstance().dismiss();
                }
            });
        }else{
            homeActivity.showErrorToast(getString(R.string.no_internet));
        }
    }





    private void loadDataToRecycler(ClosedTripExpenseListResponse closedTripResponse){
        if(closedTripResponse.getExpense().size()!=0){
            fragmentCloseTripExpenseViewBinding.textNoVehicles.setVisibility(View.GONE);
            fragmentCloseTripExpenseViewBinding.closedTripExpensesRecycler.setVisibility(View.VISIBLE);
            fragmentCloseTripExpenseViewBinding.closedTripExpensesRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
            ClosedTripExpenseAdapter closedTripAdapter = new ClosedTripExpenseAdapter(closedTripResponse.getExpense(),getActivity(),closedTripResponse.getPath(),this);
            fragmentCloseTripExpenseViewBinding.closedTripExpensesRecycler.setAdapter(closedTripAdapter);

        }else {
            fragmentCloseTripExpenseViewBinding.textNoVehicles.setVisibility(View.VISIBLE);
            fragmentCloseTripExpenseViewBinding.closedTripExpensesRecycler.setVisibility(View.GONE);
        }
    }


    @Override
    public void onExpenseClicked(ClosedTripExpenseItem closedTripExpenseItem, String imgPath) {
        Bundle bundle = new Bundle();
        bundle.putString(AppConstants.IMAGE_PATH,imgPath);
        bundle.putParcelable(AppConstants.EXPENSE,closedTripExpenseItem);
        homeActivity.replaceFragment(new ClosedTripUpdateExpenseFragment(),true,bundle);
    }
}
