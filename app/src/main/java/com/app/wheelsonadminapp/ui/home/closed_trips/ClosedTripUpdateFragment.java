package com.app.wheelsonadminapp.ui.home.closed_trips;

import static android.app.Activity.RESULT_CANCELED;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.app.wheelsonadminapp.BaseActivity;
import com.app.wheelsonadminapp.R;
import com.app.wheelsonadminapp.data.db.AppRepository;
import com.app.wheelsonadminapp.data.network.ApiService;
import com.app.wheelsonadminapp.data.network.RetrofitClientInstance;
import com.app.wheelsonadminapp.databinding.FragmentAddDriverBinding;
import com.app.wheelsonadminapp.databinding.FragmentClosedTripUpdateBinding;
import com.app.wheelsonadminapp.model.driver.DriverItem;
import com.app.wheelsonadminapp.model.driver.DriverResponse;
import com.app.wheelsonadminapp.model.expense.closedTrip.ClosedTripExpenseResponse;
import com.app.wheelsonadminapp.model.service.VehicleServiceItem;
import com.app.wheelsonadminapp.model.states.DistrictsItem;
import com.app.wheelsonadminapp.model.states.StatesItem;
import com.app.wheelsonadminapp.model.states.StatesResponse;
import com.app.wheelsonadminapp.model.trip.TripCloseResponse;
import com.app.wheelsonadminapp.model.trip.closed_trips.ClosedTripItems;
import com.app.wheelsonadminapp.model.trip.triplist.TripLiveListResponse;
import com.app.wheelsonadminapp.ui.home.HomeActivity;
import com.app.wheelsonadminapp.ui.home.driver.DriverActivity;
import com.app.wheelsonadminapp.util.AppConstants;
import com.app.wheelsonadminapp.util.MessageProgressDialog;
import com.app.wheelsonadminapp.util.NetworkUtility;
import com.app.wheelsonadminapp.util.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.skydoves.powerspinner.IconSpinnerAdapter;
import com.skydoves.powerspinner.IconSpinnerItem;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by Andria on 2/5/2022.
 */
public class ClosedTripUpdateFragment extends Fragment implements View.OnClickListener {

    FragmentClosedTripUpdateBinding closedTripUpdateBinding;
    private int GALLERY = 1, CAMERA = 2;
    String startSpeedo,closeSpeedo,expenseImg;
    File photoFile = null;
    int mode = 0;



    final Calendar myCalendar = Calendar.getInstance();
    BaseActivity homeActivity;

    ClosedTripItems closedTripItems;
    String imgPath;
    ImageView imgExpense;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeActivity = (HomeActivity)getActivity();
        if(getArguments()!=null && getArguments().getParcelable(AppConstants.CLOSED_TRIP)!=null){
            closedTripItems = getArguments().getParcelable(AppConstants.CLOSED_TRIP);
            imgPath = getArguments().getString(AppConstants.IMAGE_PATH);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        closedTripUpdateBinding = FragmentClosedTripUpdateBinding.inflate(inflater,container,false);
        closedTripUpdateBinding.frameEndSpeedo.setOnClickListener(this);
        closedTripUpdateBinding.frameStartSpeedo.setOnClickListener(this);
        closedTripUpdateBinding.btUpdateTrip.setOnClickListener(this);
        closedTripUpdateBinding.fabaddExpense.setOnClickListener(this);
        closedTripUpdateBinding.fabViewExpense.setOnClickListener(this);

        if(closedTripItems!=null){
            loadData(closedTripItems);
        }
        return closedTripUpdateBinding.getRoot();
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btUpdateTrip:
                closeTrip();
                break;
            case R.id.frameStartSpeedo:
                mode = 1;
                requestMultiplePermissions();
                break;
            case R.id.frameEndSpeedo:
                mode = 2;
                requestMultiplePermissions();
                break;
            case R.id.fabaddExpense:
                showExpenseAddDialog();
                break;
            case R.id.fabViewExpense:
                Bundle serviceBundle =  new Bundle();
                serviceBundle.putString("tripid",closedTripItems.getId());
                homeActivity.replaceFragment(new CloseTripExpenseViewFragment(),true,serviceBundle);
                break;


        }
    }


    private void showExpenseAddDialog(){
        Dialog dialog = new Dialog(getActivity(),R.style.PauseDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.add_expense_close_trip);
        Button btAdd = dialog.findViewById(R.id.btAdd);
        EditText etExpenseAmt = dialog.findViewById(R.id.etExpenseAmt);
        EditText etExpenseName=dialog.findViewById(R.id.etExpenseName);
        FrameLayout frameExpense=dialog.findViewById(R.id.frameExpense);
        imgExpense=dialog.findViewById(R.id.imgExpense);
        frameExpense.setOnClickListener(v -> {
            mode=3;
            requestMultiplePermissions();
        });

        btAdd.setOnClickListener(v -> {
            if(!etExpenseAmt.getText().toString().equals("")&&!etExpenseName.getText().toString().equals("")){
                if(NetworkUtility.isOnline(homeActivity)){
                    AppRepository appRepository = new AppRepository(getActivity());
                    MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                    builder.addFormDataPart("tripid",closedTripItems.getId());
                    builder.addFormDataPart("remark",etExpenseName.getText().toString());
                    builder.addFormDataPart("amount",etExpenseAmt.getText().toString());

                    MessageProgressDialog.getInstance().show(getActivity());

                    if(expenseImg != null && expenseImg.length()!=0){
                        File expenseImgFile = new File(expenseImg);
                        if(expenseImgFile.exists()){
                            builder.addFormDataPart("expenseimg", expenseImgFile.getName(),
                                    RequestBody.create(expenseImgFile, MediaType.parse("multipart/form-data")));
                        }

                    }


                    RequestBody requestBody = builder.build();
                    ApiService apiService = RetrofitClientInstance.getRetrofitInstance().create(ApiService.class);
                    Call<ClosedTripExpenseResponse> tripResponseCall = null;
                    tripResponseCall = apiService.addExpenseClosedTrip(requestBody);

                    tripResponseCall.enqueue(new Callback<ClosedTripExpenseResponse>() {
                        @Override
                        public void onResponse(Call<ClosedTripExpenseResponse> call, Response<ClosedTripExpenseResponse> response) {
                            MessageProgressDialog.getInstance().dismiss();
                            if(response.code() == 200 && response.body()!=null){
                                if(response.body().getStatus() == 1){
                                    dialog.dismiss();
                                    homeActivity.showSuccessToast("Successfully added expense");
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ClosedTripExpenseResponse> call, Throwable t) {
                            MessageProgressDialog.getInstance().dismiss();
                        }
                    });

                }else {
                    homeActivity.showErrorToast(getString(R.string.no_internet));
                    dialog.dismiss();
                }

            }else {
                homeActivity.showErrorToast("Please enter the both expense name and amount");
            }

        });
        Objects.requireNonNull(dialog.getWindow()).getDecorView().setBackgroundColor(Color.TRANSPARENT);
        dialog.show();
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
                            closedTripUpdateBinding.imgStartSpeedo.setImageBitmap(bitmap);

                            mode = 0;
                            break;
                        case 2:
                            closeSpeedo = saveImage(bitmap);
                            closedTripUpdateBinding.imgEndSpeedo.setImageBitmap(bitmap);
                            mode = 0;
                            break;
                        case 3:
                            expenseImg = saveImage(bitmap);
                            imgExpense.setImageBitmap(bitmap);
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
                        Picasso.get().load(new File(startSpeedo)).into(closedTripUpdateBinding.imgStartSpeedo);

                        mode = 0;
                        break;
                    case 2:
                        closeSpeedo = photoFile.getAbsolutePath();
                        Picasso.get().load(new File(closeSpeedo)).into(closedTripUpdateBinding.imgEndSpeedo);
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

    private void closeTrip() {
        if(!closedTripUpdateBinding.etStartingKm.getText().toString().equals("")&&
                !closedTripUpdateBinding.etCloseKm.getText().toString().equals("")){
            if(NetworkUtility.isOnline(homeActivity)){
                AppRepository appRepository = new AppRepository(getActivity());
                MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                builder.addFormDataPart("tripid",closedTripItems.getId());
                builder.addFormDataPart("startingkm",closedTripUpdateBinding.etStartingKm.getText().toString());
                builder.addFormDataPart("endingkm",closedTripUpdateBinding.etCloseKm.getText().toString());
                builder.addFormDataPart("remark",closedTripUpdateBinding.etComments.getText().toString());
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




    private void loadData(ClosedTripItems closedTripItems){
        closedTripUpdateBinding.textStartLocation.setText("Starting from : "+closedTripItems.getFromlocation());
        closedTripUpdateBinding.textDropLocation.setText("Dropping to : "+closedTripItems.getTolocation());
        closedTripUpdateBinding.textTripFromTo.setText(closedTripItems.getTolocation()
                +" to "+closedTripItems.getTolocation());
        closedTripUpdateBinding.textContactName.setText(closedTripItems.getPerson());
        closedTripUpdateBinding.textContactNumber.setText(closedTripItems.getMobile1()
                +","+closedTripItems.getMobile2());
        closedTripUpdateBinding.etTotalAmount.setText("Rs."+closedTripItems.getAmount());
        closedTripUpdateBinding.etTripRate.setText("Rs."+closedTripItems.getKmrate());
        closedTripUpdateBinding.etStartingKm.setText(closedTripItems.getStartingkm());
        closedTripUpdateBinding.etCloseKm.setText(closedTripItems.getEndkm());
        closedTripUpdateBinding.etComments.setText(closedTripItems.getComments());

        String imgUrl = AppConstants.SERVER_URL+imgPath+closedTripItems.getEndimage();
       // Timber.i(imgUrl);
       /* Picasso.get().load(imgUrl)
                .centerCrop().placeholder(R.drawable.app_logo).resize(125,125).into(addDriverBinding.imgProfile);*/
        Picasso.get().load(AppConstants.SERVER_URL+imgPath+closedTripItems.getStartimage())
                .centerCrop().placeholder(R.drawable.app_logo).resize(125,125).into(closedTripUpdateBinding.imgStartSpeedo);
        Picasso.get().load(AppConstants.SERVER_URL+imgPath+closedTripItems.getEndimage())
                .centerCrop().placeholder(R.drawable.app_logo).resize(125,125).into(closedTripUpdateBinding.imgEndSpeedo);




    }


}
