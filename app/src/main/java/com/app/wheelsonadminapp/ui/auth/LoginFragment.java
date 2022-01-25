package com.app.wheelsonadminapp.ui.auth;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.app.wheelsonadminapp.R;
import com.app.wheelsonadminapp.data.network.ApiService;
import com.app.wheelsonadminapp.data.network.RetrofitClientInstance;
import com.app.wheelsonadminapp.databinding.FragmentLoginBinding;
import com.app.wheelsonadminapp.model.auth.OTPResponse;
import com.app.wheelsonadminapp.util.AppConstants;
import com.app.wheelsonadminapp.util.MessageProgressDialog;
import com.app.wheelsonadminapp.util.NetworkUtility;
import com.google.gson.JsonObject;


import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class LoginFragment extends Fragment implements View.OnClickListener {

    public String TAG = LoginFragment.class.getSimpleName();
    FragmentLoginBinding fragmentLoginBinding;
    SignUpActivity signUpActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signUpActivity = (SignUpActivity)getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentLoginBinding = FragmentLoginBinding.inflate(inflater, container, false);
        fragmentLoginBinding.btRegister.setOnClickListener(this);
        fragmentLoginBinding.btLogin.setOnClickListener(this);
        return fragmentLoginBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentLoginBinding = null;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btRegister){
            if(fragmentLoginBinding.etMobileNumber.getText().length()!=0){
                if(NetworkUtility.isOnline(getActivity())){
                    getOTP("+91"+fragmentLoginBinding.etMobileNumber.getText().toString());
                }else {
                    signUpActivity.showErrorToast(getString(R.string.no_internet));
                }
            }else {
                signUpActivity.showErrorToast("Please enter the mobile number");
            }
        }else if(v.getId() == R.id.btLogin){
            signUpActivity.replaceFragment(new EmailLoginFragment(),true,null);
        }
    }

    private void getOTP(String userMobile){
        MessageProgressDialog.getInstance().show(getActivity());
        ApiService apiService = RetrofitClientInstance.getRetrofitInstance().create(ApiService.class);
        JsonObject otpObject = new JsonObject();
        otpObject.addProperty("mobileno", userMobile);
        Call<OTPResponse> otpResponseCall = apiService.getOTP(otpObject);
        otpResponseCall.enqueue(new Callback<OTPResponse>() {
            @Override
            public void onResponse(Call<OTPResponse> call, Response<OTPResponse> response) {
                MessageProgressDialog.getInstance().dismiss();
                if(response.code() == 200 && response.body()!=null){
                    if(response.body().getStatus() == 1 && response.body().getData()!=null){
                        Bundle bundle = new Bundle();
                        bundle.putString(AppConstants.OTP,response.body().getData());
                        bundle.putString(AppConstants.MOBILE,
                                fragmentLoginBinding.textCode.getText().toString()+
                                        fragmentLoginBinding.etMobileNumber.getText().toString());
                        signUpActivity.replaceFragment(new VerifyOtpFragment(),true,bundle);
                    }else {
                        signUpActivity.showErrorToast(getString(R.string.something_wrong));
                    }
                }
            }

            @Override
            public void onFailure(Call<OTPResponse> call, Throwable t) {
                MessageProgressDialog.getInstance().dismiss();
            }
        });
    }
}