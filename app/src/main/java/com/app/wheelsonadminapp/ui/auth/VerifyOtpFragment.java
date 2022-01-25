package com.app.wheelsonadminapp.ui.auth;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.wheelsonadminapp.R;
import com.app.wheelsonadminapp.data.network.ApiService;
import com.app.wheelsonadminapp.data.network.RetrofitClientInstance;
import com.app.wheelsonadminapp.databinding.FragmentVerifyOtpBinding;
import com.app.wheelsonadminapp.model.auth.VerifyOTPResponse;
import com.app.wheelsonadminapp.util.AppConstants;
import com.app.wheelsonadminapp.util.MessageProgressDialog;
import com.app.wheelsonadminapp.util.NetworkUtility;
import com.google.gson.JsonObject;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class VerifyOtpFragment extends Fragment implements View.OnClickListener {

    private CountDownTimer countDownTimer;
    FragmentVerifyOtpBinding fragmentVerifyOtpBinding;
    SignUpActivity signUpActivity;
    String OTP = "";
    String mobileNo = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signUpActivity = (SignUpActivity)getActivity();
        if(getArguments()!=null && getArguments().getString(AppConstants.OTP)!=null){
            OTP =  getArguments().getString(AppConstants.OTP);
            mobileNo =  getArguments().getString(AppConstants.MOBILE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentVerifyOtpBinding = FragmentVerifyOtpBinding.inflate(inflater, container, false);
        startTimer();
        fragmentVerifyOtpBinding.btVerify.setOnClickListener(this);
        if(OTP!=null && OTP.length()!=0){
            fragmentVerifyOtpBinding.otpView.setOTP(OTP);
            fragmentVerifyOtpBinding.btVerify.requestFocus();
        }
        return fragmentVerifyOtpBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        if(countDownTimer!=null){
            countDownTimer.cancel();
            countDownTimer = null;
        }
        super.onDestroyView();
        fragmentVerifyOtpBinding = null;
    }

    private void startTimer(){
        countDownTimer = new CountDownTimer(120000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                fragmentVerifyOtpBinding.txtTimer.setText(""+String.format("%d min %d sec",
                        TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            @Override
            public void onFinish() {
                fragmentVerifyOtpBinding.txtTimer.setText("00:00");
                fragmentVerifyOtpBinding.txtTimer.setVisibility(View.GONE);
                if(countDownTimer!=null){
                    countDownTimer.cancel();
                }
            }
        }.start();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btVerify){
            if(fragmentVerifyOtpBinding.otpView.getOTP().length()!=0){
                verifyOTP();
            }
        }
    }

    private void verifyOTP(){
        if(NetworkUtility.isOnline(getActivity())){
            MessageProgressDialog.getInstance().show(getActivity());
            JsonObject otpObject = new JsonObject();
            otpObject.addProperty("mobileno",mobileNo);
            otpObject.addProperty("otp",fragmentVerifyOtpBinding.otpView.getOTP());
            ApiService apiService = RetrofitClientInstance.getRetrofitInstance().create(ApiService.class);
            Call<VerifyOTPResponse>verifyOTPResponseCall = apiService.verifyOTP(otpObject);
            verifyOTPResponseCall.enqueue(new Callback<VerifyOTPResponse>() {
                @Override
                public void onResponse(Call<VerifyOTPResponse> call, Response<VerifyOTPResponse> response) {
                    MessageProgressDialog.getInstance().dismiss();
                    if(response.code() == 200 && response.body()!=null){
                        if(response.body().getStatus() ==1){
                            Bundle bundle = new Bundle();
                            bundle.putString(AppConstants.MOBILE,mobileNo);
                            bundle.putParcelable(AppConstants.REGISTER_OBJ,response.body());
                            signUpActivity.replaceFragment(new RegisterFragment(),false,bundle);
                        }else if(response.body().getStatus() ==2){
                            Bundle bundle = new Bundle();
                            bundle.putString(AppConstants.MOBILE,mobileNo);
                            signUpActivity.replaceFragment(new RegisterFragment(),false,bundle);
                        }
                        else {
                            signUpActivity.showErrorToast(getString(R.string.something_wrong));
                        }
                    }else {
                        signUpActivity.showErrorToast(getString(R.string.something_wrong));
                    }
                }

                @Override
                public void onFailure(Call<VerifyOTPResponse> call, Throwable t) {
                    MessageProgressDialog.getInstance().dismiss();
                }
            });
        }else {
            signUpActivity.showErrorToast(getString(R.string.no_internet));
        }
    }
}