package com.app.wheelsonadminapp.ui.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.wheelsonadminapp.R;
import com.app.wheelsonadminapp.data.db.AppRepository;
import com.app.wheelsonadminapp.data.db.UserEntity;
import com.app.wheelsonadminapp.data.network.ApiService;
import com.app.wheelsonadminapp.data.network.RetrofitClientInstance;
import com.app.wheelsonadminapp.databinding.FragmentEmailLoginBinding;
import com.app.wheelsonadminapp.model.auth.login.LoginResponse;
import com.app.wheelsonadminapp.ui.home.HomeActivity;
import com.app.wheelsonadminapp.util.MessageProgressDialog;
import com.app.wheelsonadminapp.util.NetworkUtility;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class EmailLoginFragment extends Fragment {

    public String TAG = EmailLoginFragment.class.getSimpleName();
    FragmentEmailLoginBinding emailLoginBinding;
    SignUpActivity signUpActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signUpActivity = (SignUpActivity)getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        emailLoginBinding = FragmentEmailLoginBinding.inflate(inflater,container,false);
        // debug purpose
        emailLoginBinding.etEmail.setText("kk@test.com");
        emailLoginBinding.etPassword.setText("9876543210");
        emailLoginBinding.btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(emailLoginBinding.etEmail.getText().length() == 0){
                    signUpActivity.showErrorToast("Please enter the email");
                }else if(emailLoginBinding.etPassword.getText().length() == 0){
                    signUpActivity.showErrorToast("Please enter the password");
                }else {
                    loginUser();
                }
            }
        });
        return emailLoginBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        emailLoginBinding = null;
    }

    private void loginUser(){
        if(NetworkUtility.isOnline(getActivity())){
            MessageProgressDialog.getInstance().show(getActivity());
            JsonObject loginObject = new JsonObject();
            loginObject.addProperty("username",emailLoginBinding.etEmail.getText().toString());
            loginObject.addProperty("password",emailLoginBinding.etPassword.getText().toString());
            ApiService apiService = RetrofitClientInstance.getRetrofitInstance().create(ApiService.class);
            Call<LoginResponse>loginResponseCall = apiService.loginUser(loginObject);
            loginResponseCall.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    MessageProgressDialog.getInstance().dismiss();
                    if(response.code() == 200 && response.body()!=null){
                        if(response.body().getStatus() == 1){
                            saveDataToDb(response.body());
                        }else {
                            signUpActivity.showErrorToast(getString(R.string.login_error));
                        }
                    }else {
                        signUpActivity.showErrorToast(getString(R.string.something_wrong));
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    MessageProgressDialog.getInstance().dismiss();
                    Timber.i(t.getMessage());
                }
            });
        }else {
            signUpActivity.showErrorToast(getString(R.string.no_internet));
        }

    }

    private void saveDataToDb(LoginResponse loginResponse){
        AppRepository appRepository = new AppRepository(getActivity());
        UserEntity userFromDb = appRepository.getUser();
        if (userFromDb != null && userFromDb.getUid() == 1) {
            //User exists
            userFromDb.setAddress(loginResponse.getTravel().get(0).getAddress());
            userFromDb.setEmail(loginResponse.getTravel().get(0).getEmailid());
            userFromDb.setMobile(loginResponse.getTravel().get(0).getMobileno());
            userFromDb.setOwnerName(loginResponse.getTravel().get(0).getOwnername());
            userFromDb.setPhone(loginResponse.getTravel().get(0).getPhone());
            userFromDb.setToken(loginResponse.getTravel().get(0).getToken());
            userFromDb.setTravelsName(loginResponse.getTravel().get(0).getTravelname());
            userFromDb.setVehicleCount(loginResponse.getTravel().get(0).getVehicles());
            userFromDb.setUserId(loginResponse.getTravel().get(0).getId());
            userFromDb.setImage(loginResponse.getTravel().get(0).getImage());
            appRepository.updateUser(userFromDb);
            Timber.i("User updated");
        }else {
            UserEntity userEntity = new UserEntity();
            userEntity.setAddress(loginResponse.getTravel().get(0).getAddress());
            userEntity.setEmail(loginResponse.getTravel().get(0).getEmailid());
            userEntity.setMobile(loginResponse.getTravel().get(0).getMobileno());
            userEntity.setOwnerName(loginResponse.getTravel().get(0).getOwnername());
            userEntity.setPhone(loginResponse.getTravel().get(0).getPhone());
            userEntity.setToken(loginResponse.getTravel().get(0).getToken());
            userEntity.setTravelsName(loginResponse.getTravel().get(0).getTravelname());
            userEntity.setVehicleCount(loginResponse.getTravel().get(0).getVehicles());
            userEntity.setImage(loginResponse.getTravel().get(0).getImage());
            userEntity.setUid(1);
            userEntity.setUserId(loginResponse.getTravel().get(0).getId());
            appRepository.createUser(userEntity);
            Timber.i("User created");
        }
        startActivity(new Intent(getActivity(), HomeActivity.class));
        getActivity().finish();
    }

}