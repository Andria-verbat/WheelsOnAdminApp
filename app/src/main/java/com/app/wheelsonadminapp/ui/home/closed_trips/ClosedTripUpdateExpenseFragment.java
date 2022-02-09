package com.app.wheelsonadminapp.ui.home.closed_trips;

import static android.app.Activity.RESULT_CANCELED;

import android.Manifest;
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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.app.wheelsonadminapp.databinding.FragmentClosedTripUpdateBinding;
import com.app.wheelsonadminapp.databinding.FragmentClosedTripUpdateExpenseBinding;
import com.app.wheelsonadminapp.model.expense.closedTrip.ClosedTripExpenseItem;
import com.app.wheelsonadminapp.model.expense.closedTrip.ClosedTripExpenseResponse;
import com.app.wheelsonadminapp.model.trip.TripCloseResponse;
import com.app.wheelsonadminapp.model.trip.closed_trips.ClosedTripItems;
import com.app.wheelsonadminapp.ui.home.HomeActivity;
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

/**
 * Created by Andria on 2/9/2022.
 */
public class ClosedTripUpdateExpenseFragment extends Fragment implements View.OnClickListener {

    FragmentClosedTripUpdateExpenseBinding fragmentClosedTripUpdateExpenseBinding;
    private int GALLERY = 1, CAMERA = 2;
    String expenseImg;
    File photoFile = null;
    int mode = 0;


    final Calendar myCalendar = Calendar.getInstance();
    BaseActivity homeActivity;

    ClosedTripExpenseItem closedTripExpenseItem;
    String imgPath;
    ImageView imgExpense;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeActivity = (HomeActivity) getActivity();
        if (getArguments() != null && getArguments().getParcelable(AppConstants.EXPENSE) != null) {
            closedTripExpenseItem = getArguments().getParcelable(AppConstants.EXPENSE);
            imgPath = getArguments().getString(AppConstants.IMAGE_PATH);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentClosedTripUpdateExpenseBinding = FragmentClosedTripUpdateExpenseBinding.inflate(inflater, container, false);
        fragmentClosedTripUpdateExpenseBinding.frameStartSpeedo.setOnClickListener(this);
        fragmentClosedTripUpdateExpenseBinding.btUpdateExpense.setOnClickListener(this);
        fragmentClosedTripUpdateExpenseBinding.btDeleteExpense.setOnClickListener(this);


        if(closedTripExpenseItem!=null){
            loadData(closedTripExpenseItem);
        }
        return fragmentClosedTripUpdateExpenseBinding.getRoot();
    }


    private void requestMultiplePermissions() {
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

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getActivity());
        pictureDialog.setTitle("Select Image");
        String[] pictureDialogItems = {
                "Gallery",
                "Camera"};
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
        switch (v.getId()) {

            case R.id.frameStartSpeedo:
                mode = 1;
                requestMultiplePermissions();
                break;
            case R.id.btUpdateExpense:
                updateExpense();
                break;
            case R.id.btDeleteExpense:
                deleteExpense();
                break;


        }
    }

    private void deleteExpense() {
        if(NetworkUtility.isOnline(getActivity())) {
            JsonObject inputObject = new JsonObject();
            inputObject.addProperty("expenseid",closedTripExpenseItem.getExpenseid());
            //inputObject.addProperty("servicetypeid",vehicleServiceItem.getServiceTypeId());
            MessageProgressDialog.getInstance().show(getActivity());
            Call<JsonObject>deleteServiceCall = RetrofitClientInstance.getApiService().deleteExpenseClosedTrip(inputObject);
            deleteServiceCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    MessageProgressDialog.getInstance().dismiss();
                    if(response.code() == 200 && response.body()!=null && response.body().has("status")){
                        int status = response.body().get("status").getAsInt();
                        if(status == 1){
                            homeActivity.onBackPressed();
                            homeActivity.showSuccessToast("Service deleted successfully!");
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    MessageProgressDialog.getInstance().dismiss();
                    homeActivity.showErrorToast(getString(R.string.something_wrong));
                }
            });
        }else {
            homeActivity.showErrorToast(getString(R.string.no_internet));
        }
    }

    private void updateExpense() {
        if(!fragmentClosedTripUpdateExpenseBinding.etExpenseRate.getText().toString().equals("")&&!fragmentClosedTripUpdateExpenseBinding.etExpenseName.getText().toString().equals("")){
            if(NetworkUtility.isOnline(homeActivity)){
                AppRepository appRepository = new AppRepository(getActivity());
                MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                builder.addFormDataPart("expenseid",closedTripExpenseItem.getExpenseid());
                builder.addFormDataPart("travelsid",closedTripExpenseItem.getExpensetravelsid());
                builder.addFormDataPart("tripid",closedTripExpenseItem.getExpensetripid());
                builder.addFormDataPart("name",fragmentClosedTripUpdateExpenseBinding.etExpenseName.getText().toString());
                builder.addFormDataPart("amount",fragmentClosedTripUpdateExpenseBinding.etExpenseRate.getText().toString());

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

                                homeActivity.showSuccessToast("Successfully updated expense");
                                homeActivity.onBackPressed();
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

            }

        }else {
            homeActivity.showErrorToast("Please enter the both expense name and amount");
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
                    switch (mode) {
                        case 1:
                            expenseImg = saveImage(bitmap);
                            fragmentClosedTripUpdateExpenseBinding.imgExpense.setImageBitmap(bitmap);
                            mode = 0;
                            break;


                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            if (photoFile != null && photoFile.exists()) {
                switch (mode) {
                    case 1:
                        expenseImg = photoFile.getAbsolutePath();
                        Picasso.get().load(new File(expenseImg)).into(fragmentClosedTripUpdateExpenseBinding.imgExpense);

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






    private void loadData(ClosedTripExpenseItem closedTripExpenseItem){

        fragmentClosedTripUpdateExpenseBinding.etExpenseName.setText(closedTripExpenseItem.getExpensetype());
        fragmentClosedTripUpdateExpenseBinding.etExpenseRate.setText(closedTripExpenseItem.getExpense_amount());

        String imgUrl = AppConstants.SERVER_URL+imgPath+closedTripExpenseItem.getExpenseimage();
        // Timber.i(imgUrl);
        Picasso.get().load(imgUrl)
                .centerCrop().placeholder(R.drawable.app_logo).resize(125,125).into(fragmentClosedTripUpdateExpenseBinding.imgExpense);





    }


}