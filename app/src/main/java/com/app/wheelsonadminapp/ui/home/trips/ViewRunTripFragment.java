package com.app.wheelsonadminapp.ui.home.trips;

import static android.app.Activity.RESULT_CANCELED;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.wheelsonadminapp.BaseActivity;
import com.app.wheelsonadminapp.R;
import com.app.wheelsonadminapp.data.db.AppRepository;
import com.app.wheelsonadminapp.data.network.ApiService;
import com.app.wheelsonadminapp.data.network.RetrofitClientInstance;
import com.app.wheelsonadminapp.databinding.FragmentViewRunTripFragementBinding;
import com.app.wheelsonadminapp.model.driver.DriverResponse;
import com.app.wheelsonadminapp.model.trip.TripCloseResponse;
import com.app.wheelsonadminapp.model.trip.TripResponse;
import com.app.wheelsonadminapp.model.trip.trip_details.TripDetailsResponse;
import com.app.wheelsonadminapp.ui.home.HomeActivity;
import com.app.wheelsonadminapp.ui.home.HomeTrackingFragment;
import com.app.wheelsonadminapp.ui.home.vehicle.VehicleActivity;
import com.app.wheelsonadminapp.util.AppConstants;
import com.app.wheelsonadminapp.util.MessageProgressDialog;
import com.app.wheelsonadminapp.util.NetworkUtility;
import com.app.wheelsonadminapp.util.Utils;
import com.google.gson.JsonObject;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

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

public class ViewRunTripFragment extends Fragment implements View.OnClickListener {

    FragmentViewRunTripFragementBinding binding;
    BaseActivity homeActivity;
    String tripId;
    TripDetailsResponse tripDetailsResponse;
    AppRepository repository;

    private int GALLERY = 1, CAMERA = 2;
    String startSpeedo,closeSpeedo;
    // Create the File where the photo should go
    File photoFile = null;
    int mode = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getActivity() instanceof VehicleActivity){
            homeActivity = (VehicleActivity)getActivity();
        }else {
            homeActivity = (HomeActivity)getActivity();
        }
        if(getArguments()!=null && getArguments().getString(AppConstants.TRIP_ID)!=null){
            tripId = getArguments().getString(AppConstants.TRIP_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentViewRunTripFragementBinding.inflate(inflater,container,false);
        binding.btTrack.setOnClickListener(this);
        binding.btCloseTrip.setOnClickListener(this);
        binding.fabEditTrip.setOnClickListener(this);
        binding.frameEndSpeedo.setOnClickListener(this);
        binding.frameStartSpeedo.setOnClickListener(this);
        repository = new AppRepository(homeActivity);
        binding.textUserName.setText("Hi, "+repository.getUser().getTravelsName());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(tripId!=null){
            getTripDetails();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btTrack:
                if(tripDetailsResponse!=null){
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(AppConstants.TRIP_DETAILS,tripDetailsResponse.getTrip().get(0));
                    homeActivity.replaceFragment(new HomeTrackingFragment(),true,bundle);
                }
                break;
            case R.id.btCloseTrip:
                closeTrip();
                //
                break;
            case R.id.fabEditTrip:
                Bundle bundle = new Bundle();
                bundle.putString(AppConstants.TRIP_ID,tripId);
                homeActivity.replaceFragment(new AddTripFragment(),true,bundle);
                break;
            case R.id.frameStartSpeedo:
                mode=1;
                requestMultiplePermissions();
                break;
            case R.id.frameEndSpeedo:
                mode=2;
                requestMultiplePermissions();
                break;
        }
    }

    private void closeTrip() {
        if(!binding.etStartingKm.getText().toString().equals("")&&
                !binding.etCloseKm.getText().toString().equals("")){
            if(NetworkUtility.isOnline(homeActivity)){
                AppRepository appRepository = new AppRepository(getActivity());
                MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                builder.addFormDataPart("tripid",tripId);
                builder.addFormDataPart("startingkm",binding.etStartingKm.getText().toString());
                builder.addFormDataPart("endingkm",binding.etCloseKm.getText().toString());
                builder.addFormDataPart("remark",binding.etComments.getText().toString());
                MessageProgressDialog.getInstance().show(getActivity());

                if(startSpeedo != null && startSpeedo.length()!=0){
                    File startSpeedoFile = new File(startSpeedo);
                    if(startSpeedoFile.exists()){
                        builder.addFormDataPart("kmstartimg", startSpeedoFile.getName(),
                                RequestBody.create(startSpeedoFile, MediaType.parse("multipart/form-data")));
                    }

                }

                if(closeSpeedo != null && closeSpeedo.length()!=0){
                    File closeSpeedoFile = new File(closeSpeedo);
                    if(closeSpeedoFile.exists()){
                        builder.addFormDataPart("kmendimg", closeSpeedoFile.getName(),
                                RequestBody.create(closeSpeedoFile,MediaType.parse("multipart/form-data")));
                    }

                }

                RequestBody requestBody = builder.build();
                ApiService apiService = RetrofitClientInstance.getRetrofitInstance().create(ApiService.class);
                Call<TripCloseResponse> tripResponseCall = null;
                tripResponseCall = apiService.closeTrip(requestBody);

                tripResponseCall.enqueue(new Callback<TripCloseResponse>() {
                    @Override
                    public void onResponse(Call<TripCloseResponse> call, Response<TripCloseResponse> response) {
                        MessageProgressDialog.getInstance().dismiss();
                        if(response.code() == 200 && response.body()!=null){
                            if(response.body().getStatus() == 1){
                                homeActivity.onBackPressed();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<TripCloseResponse> call, Throwable t) {
                        MessageProgressDialog.getInstance().dismiss();
                    }
                });

            }else {
                homeActivity.showErrorToast(getString(R.string.no_internet));
            }
        }else {
            homeActivity.showErrorToast("Please enter both closing and starting speedometer reading");
        }


    }

    private void getTripDetails(){
        if(NetworkUtility.isOnline(homeActivity)){
            JsonObject inputObject = new JsonObject();
            inputObject.addProperty("tripid",tripId);
            MessageProgressDialog.getInstance().show(homeActivity);
            Call<TripDetailsResponse>responseCall = RetrofitClientInstance.getApiService().getTripDetails(inputObject);
            responseCall.enqueue(new Callback<TripDetailsResponse>() {
                @Override
                public void onResponse(Call<TripDetailsResponse> call, Response<TripDetailsResponse> response) {
                    MessageProgressDialog.getInstance().dismiss();
                    if(response.code() == 200 && response.body()!=null){
                        if(response.body().getStatus() == 1){
                            tripDetailsResponse = response.body();
                            loadData(tripDetailsResponse);
                        }else {
                            homeActivity.showErrorToast(getString(R.string.something_wrong));
                            requireActivity().onBackPressed();
                        }
                    }else {
                        homeActivity.showErrorToast(getString(R.string.something_wrong));
                        requireActivity().onBackPressed();
                    }
                }

                @Override
                public void onFailure(Call<TripDetailsResponse> call, Throwable t) {
                    homeActivity.showErrorToast(getString(R.string.something_wrong));
                    MessageProgressDialog.getInstance().dismiss();
                }
            });
        }else {
            homeActivity.showErrorToast(getString(R.string.no_internet));
        }
    }

    private void loadData(TripDetailsResponse tripDetailsResponse){
        binding.textStartLocation.setText("Starting from : "+tripDetailsResponse.getTrip().get(0).getFromlocation());
        binding.textDropLocation.setText("Dropping to : "+tripDetailsResponse.getTrip().get(0).getTolocation());
        binding.textTripFromTo.setText(tripDetailsResponse.getTrip().get(0).getTolocation()
                +" to "+tripDetailsResponse.getTrip().get(0).getTolocation());
        binding.textContactName.setText(tripDetailsResponse.getTrip().get(0).getPerson());
        binding.textContactNumber.setText(tripDetailsResponse.getTrip().get(0).getMobile1()
                +","+tripDetailsResponse.getTrip().get(0).getMobile2());
        binding.etTotalAmount.setText("Rs."+tripDetailsResponse.getTrip().get(0).getAmount());
        binding.etTripRate.setText("Rs."+tripDetailsResponse.getTrip().get(0).getKmrate());
        binding.etStartingKm.setText(tripDetailsResponse.getTrip().get(0).getStartingkm());
//        binding.etFuelAmount.setText("Rs."+tripDetailsResponse.getExpense().get(0).getFuel());
//        binding.etTollAmount.setText("Rs."+tripDetailsResponse.getExpense().get(0).getToll());
//        binding.etParkingAmount.setText("Rs."+tripDetailsResponse.getExpense().get(0).getPark());
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
        //        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(intent, CAMERA);
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {

            try {
                photoFile = Utils.createImageFile(getActivity());
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.app.wheelsonadminapp.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA);
            }
        }
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
                        bitmap = MediaStore.Images.Media.getBitmap(homeActivity.getContentResolver(), contentURI);
                    } else {
                        ImageDecoder.Source source = ImageDecoder.createSource(homeActivity.getContentResolver(), contentURI);
                        bitmap = ImageDecoder.decodeBitmap(source);
                    }
                    switch (mode){
                        case 1:
                            startSpeedo = saveImage(bitmap);
                            binding.imgStartSpeedo.setImageBitmap(bitmap);

                            mode = 0;
                            break;
                        case 2:
                            closeSpeedo = saveImage(bitmap);
                            binding.imgEndSpeedo.setImageBitmap(bitmap);
                            mode = 0;
                            break;

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            if(photoFile!=null && photoFile.exists()){
                switch (mode){
                    case 1:
                        startSpeedo = photoFile.getAbsolutePath();
                        Picasso.get().load(new File(startSpeedo)).into(binding.imgStartSpeedo);

                        mode = 0;
                        break;
                    case 2:
                        closeSpeedo = photoFile.getAbsolutePath();
                        Picasso.get().load(new File(closeSpeedo)).into(binding.imgEndSpeedo);
                        mode = 0;
                        break;

                }
            }
        }
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File imgDirectory = new File(String.valueOf(homeActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)));
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
}