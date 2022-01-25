package com.app.wheelsonadminapp.ui.auth;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.wheelsonadminapp.R;
import com.app.wheelsonadminapp.data.network.ApiService;
import com.app.wheelsonadminapp.data.network.RetrofitClientInstance;
import com.app.wheelsonadminapp.databinding.FragmentRegisterBinding;
import com.app.wheelsonadminapp.model.auth.VerifyOTPResponse;
import com.app.wheelsonadminapp.model.auth.register.RegisterResponse;
import com.app.wheelsonadminapp.util.AppConstants;
import com.app.wheelsonadminapp.util.MessageProgressDialog;
import com.app.wheelsonadminapp.util.NetworkUtility;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static android.app.Activity.RESULT_CANCELED;

public class RegisterFragment extends Fragment implements View.OnClickListener {

    public String TAG = RegisterFragment.class.getSimpleName();
    FragmentRegisterBinding registerBinding;
    private int GALLERY = 1, CAMERA = 2;
    SignUpActivity signUpActivity;
    VerifyOTPResponse verifyOTPResponse;
    boolean picSelected = false;
    File imageFile;
    String mobile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signUpActivity = (SignUpActivity)getActivity();
        if(getArguments().getParcelable(AppConstants.REGISTER_OBJ)!=null){
            verifyOTPResponse = getArguments().getParcelable(AppConstants.REGISTER_OBJ);
            Timber.i(verifyOTPResponse.toString());
        }
        if(getArguments().getString(AppConstants.MOBILE)!=null){
            mobile = getArguments().getString(AppConstants.MOBILE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        registerBinding = FragmentRegisterBinding.inflate(inflater,container,false);
        registerBinding.imgProfile.setOnClickListener(this);
        registerBinding.imgAdd.setOnClickListener(this);
        registerBinding.btUpdate.setOnClickListener(this);
        if(mobile!=null  && mobile.length()>1){
            registerBinding.etMobileNumber.setText(mobile);
            registerBinding.etMobileNumber.setEnabled(false);
        }
        return registerBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        registerBinding = null;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imgProfile || v.getId() == R.id.imgAdd){
            requestMultiplePermissions();
        }else if(v.getId() == R.id.btUpdate){
            if(Objects.requireNonNull(registerBinding.etLandline.getText()).length() == 0){
                signUpActivity.showErrorToast("Please enter your landline number");
            }else if(Objects.requireNonNull(registerBinding.etTravelName.getText()).length() == 0){
                signUpActivity.showErrorToast("Please enter your travel's name");
            }else if(Objects.requireNonNull(registerBinding.etOwnerName.getText()).length() == 0){
                signUpActivity.showErrorToast("Please enter your owner's name");
            }else if(Objects.requireNonNull(registerBinding.etAddress.getText()).length() == 0){
                signUpActivity.showErrorToast("Please enter your address");
            }else if(Objects.requireNonNull(registerBinding.etVehicleNos.getText()).length() == 0){
                signUpActivity.showErrorToast("Please enter the no of vehicles");
            }else if(registerBinding.etEmail.getText().length() == 0){
                signUpActivity.showErrorToast("Please enter the username (email)");
            }else if(registerBinding.etPassword.getText().length() == 0){
                signUpActivity.showErrorToast("Please enter the password");
            }else if(!registerBinding.etPassword.getText().toString().equals(registerBinding.etConfirmPassword.getText().toString())){
                signUpActivity.showErrorToast("Both passwords must be same!");
            }else {
                if(NetworkUtility.isOnline(getActivity())){
                    registerUser();
                }else {
                    signUpActivity.showErrorToast(getString(R.string.no_internet));
                }

            }
        }
    }

    private void  requestMultiplePermissions(){
        Dexter.withActivity(getActivity())
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            showPictureDialog();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                    }
                })
                .onSameThread()
                .check();
    }

    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getActivity());
        pictureDialog.setTitle("Select Image");
        String[] pictureDialogItems = {
                "Gallery",
                "Camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallery();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                Bitmap bitmap = null;
                try {
                    if (Build.VERSION.SDK_INT < 29) {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                    } else {
                        ImageDecoder.Source source = ImageDecoder.createSource(getActivity().getContentResolver(), contentURI);
                        bitmap = ImageDecoder.decodeBitmap(source);
                    }
                    String path = saveImage(bitmap);
                    imageFile = new File(path);
                    registerBinding.imgProfile.setImageBitmap(bitmap);
                    registerBinding.imgAdd.setVisibility(View.GONE);
                    picSelected = true;

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            registerBinding.imgProfile.setImageBitmap(thumbnail);
            registerBinding.imgAdd.setVisibility(View.GONE);
            String path = saveImage(thumbnail);
            imageFile = new File(path);
            picSelected = true;
        }
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File imgDirectory = new File(String.valueOf(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)));
        // have the object build the directory structure, if needed.
        if (!imgDirectory.exists()) {
            imgDirectory.mkdirs();
        }

        try {
            File f = new File(imgDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(getActivity(),
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    private void registerUser(){
        MessageProgressDialog.getInstance().show(getActivity());
            RequestBody travelName = RequestBody.create(registerBinding.etTravelName.getText().toString(),MediaType.parse("multipart/form-data"));
            RequestBody ownerName = RequestBody.create(registerBinding.etOwnerName.getText().toString(),MediaType.parse("multipart/form-data"));
            RequestBody mobileNo = RequestBody.create(registerBinding.etMobileNumber.getText().toString(),MediaType.parse("multipart/form-data"));
            RequestBody phone = RequestBody.create(registerBinding.etLandline.getText().toString(),MediaType.parse("multipart/form-data"));
            RequestBody vehicles = RequestBody.create(registerBinding.etVehicleNos.getText().toString(),MediaType.parse("multipart/form-data"));
            RequestBody emailId = RequestBody.create(registerBinding.etEmail.getText().toString(),MediaType.parse("multipart/form-data"));
            RequestBody address = RequestBody.create(registerBinding.etAddress.getText().toString(),MediaType.parse("multipart/form-data"));
            RequestBody password = RequestBody.create(registerBinding.etPassword.getText().toString(),MediaType.parse("multipart/form-data"));
            MultipartBody.Part body = null;
            if(imageFile!=null){
                RequestBody requestFile = RequestBody.create(imageFile,MediaType.parse("multipart/form-data"));
                body = MultipartBody.Part.createFormData("image", imageFile.getName(), requestFile);
            }

//            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
//            builder.addFormDataPart("travelname", registerBinding.etTravelName.getText().toString());
//            builder.addFormDataPart("ownername", registerBinding.etOwnerName.getText().toString());
//            builder.addFormDataPart("mobileno", registerBinding.etMobileNumber.getText().toString());
//            builder.addFormDataPart("phone", registerBinding.etLandline.getText().toString());
//            builder.addFormDataPart("vehicles", registerBinding.etVehicleNos.getText().toString());
//            builder.addFormDataPart("emailid", registerBinding.etEmail.getText().toString());
//            builder.addFormDataPart("address", registerBinding.etAddress.getText().toString());
//            builder.addFormDataPart("password", registerBinding.etPassword.getText().toString());
//            builder.addFormDataPart("image", file.getName(), RequestBody.create(file,MediaType.parse("multipart/form-data")));
//            requestBody = builder.build();
            ApiService apiService = RetrofitClientInstance.getRetrofitInstance().create(ApiService.class);
            Call<RegisterResponse> registerResponseCall;
            registerResponseCall = apiService.registerUser(travelName,ownerName,mobileNo,phone,vehicles,emailId,
                address,password,body);
            registerResponseCall.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                    Timber.i(response.body().toString());
                    MessageProgressDialog.getInstance().dismiss();
                    if(response.code() == 200 && response.body().getStatus() == 1){
                        signUpActivity.showSuccessToast("User registered successfully !");
                        signUpActivity.clearBackStackAndOpen(new EmailLoginFragment());
                    }else {
                        signUpActivity.showErrorToast(getString(R.string.something_wrong));
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {
                    Timber.i(t.getMessage());
                    MessageProgressDialog.getInstance().dismiss();
                    signUpActivity.showErrorToast(getString(R.string.something_wrong));
                }
            });

    }
}