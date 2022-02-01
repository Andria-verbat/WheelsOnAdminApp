package com.app.wheelsonadminapp.ui.home.vehicle;

import android.Manifest;
import android.app.DatePickerDialog;
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
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.app.wheelsonadminapp.R;
import com.app.wheelsonadminapp.data.db.AppRepository;
import com.app.wheelsonadminapp.data.network.RetrofitClientInstance;
import com.app.wheelsonadminapp.databinding.FragmentAddVehicleBinding;
import com.app.wheelsonadminapp.model.auth.vehicle.VehicleItem;
import com.app.wheelsonadminapp.model.auth.vehicle.VehicleListResponse;
import com.app.wheelsonadminapp.model.auth.vehicle.VehicletypeItem;
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

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static android.app.Activity.RESULT_CANCELED;


public class AddVehicleFragment extends Fragment implements View.OnClickListener {

    FragmentAddVehicleBinding addVehicleBinding;
    VehicleActivity homeActivity;
    VehicleItem vehicleItem;
    List<VehicletypeItem>vehicleTypeItems;
    String imgPath;
    boolean editMode = false;
    List<IconSpinnerItem> iconSpinnerItems;
    final Calendar myCalendar = Calendar.getInstance();
    private int GALLERY = 1, CAMERA = 2;
    int dobMode = 0; // 1--> Insurance. 2--> Permit. 3 --> Pollution
    // Create the File where the photo should go
    File photoFile = null;
    String selectedVehicleType = "";

    int mode = 0;
    // 1 --> Profile Pic
    // 2 --> Insurance Front
    // 3 --> RC Front
    // 4 --> Pollution Front
    // 5 --> Permit Front

    String profilePath,insuranceFront,RCFront,pollutionFront,permitFront;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeActivity = (VehicleActivity)getActivity();
        if(getArguments()!=null && getArguments().getParcelable(AppConstants.VEHICLE)!=null){
            vehicleItem = getArguments().getParcelable(AppConstants.VEHICLE);
            imgPath =  getArguments().getString(AppConstants.IMAGE_PATH);
            if(vehicleItem.getStatus().equals("1")){
                editMode = false;
            }else {
                editMode = true;
            }

        }

        if(getArguments()!=null && getArguments().getParcelableArrayList(AppConstants.VEHICLE_TYPE_LIST)!=null){
            vehicleTypeItems = getArguments().getParcelableArrayList(AppConstants.VEHICLE_TYPE_LIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        addVehicleBinding = FragmentAddVehicleBinding.inflate(inflater,container,false);
        setDOB();
        addVehicleBinding.btSubmit.setOnClickListener(this);
        addVehicleBinding.fabEdit.setOnClickListener(this);
        addVehicleBinding.parentLayout.setOnClickListener(this);
        if(editMode){
            addVehicleBinding.fabEdit.setVisibility(View.VISIBLE);
            addVehicleBinding.btSubmit.setVisibility(View.INVISIBLE);
            setEditable(false);
        }else {
            addVehicleBinding.fabEdit.setVisibility(View.GONE);
            addVehicleBinding.btSubmit.setVisibility(View.VISIBLE);
            setEditable(true);
        }
        if(vehicleItem!=null){
            loadDataFromObject(vehicleItem);
        }
        if(vehicleTypeItems!=null && vehicleTypeItems.size()!=0){
            addDataToSpinner(vehicleTypeItems);
        }
        addVehicleBinding.imgProfile.setOnClickListener(this);
        addVehicleBinding.imgAdd.setOnClickListener(this);
        addVehicleBinding.imgInsurance.setOnClickListener(this);
        addVehicleBinding.imgRC.setOnClickListener(this);
        addVehicleBinding.imgPollution.setOnClickListener(this);
        addVehicleBinding.imgPermit.setOnClickListener(this);
        return addVehicleBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        addVehicleBinding = null;
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
                (dialog, which) -> {
                    switch (which) {
                        case 0:
                            choosePhotoFromGallery();
                            break;
                        case 1:
                            takePhotoFromCamera();
                            break;
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

    private void loadDataFromObject(VehicleItem vehicleItem){
        addVehicleBinding.textDrivers.setText(vehicleItem.getBrand()+" "+vehicleItem.getModel());
        addVehicleBinding.etVehicleName.setText(vehicleItem.getBrand());
        addVehicleBinding.etModelName.setText(vehicleItem.getModel());
        addVehicleBinding.etRegNo.setText(vehicleItem.getRegno());
        addVehicleBinding.etInsuranceValidity.setText(vehicleItem.getInsurancedate());
        addVehicleBinding.etPollutionValidity.setText(vehicleItem.getPollutiondate());
        addVehicleBinding.etPermitValidity.setText(vehicleItem.getPermitdate());
        addVehicleBinding.etSeatCount.setText(vehicleItem.getSeat());
        addVehicleBinding.etVehicleNickName.setText(vehicleItem.getVehicleNickName());
        addVehicleBinding.etStartKm.setText(vehicleItem.getStartKm());
        Picasso.get().load(AppConstants.SERVER_URL+imgPath+vehicleItem.getTaximg())
                .centerCrop().placeholder(R.drawable.app_logo)
                .resize(125,125).into(addVehicleBinding.imgProfile);
        Picasso.get().load(AppConstants.SERVER_URL+imgPath+vehicleItem.getInsuranceimg())
                .centerCrop().placeholder(R.drawable.app_logo)
                .resize(125,125).into(addVehicleBinding.imgInsurance);
        Picasso.get().load(AppConstants.SERVER_URL+imgPath+vehicleItem.getRcbookimg())
                .centerCrop().placeholder(R.drawable.app_logo)
                .resize(125,125).into(addVehicleBinding.imgRC);
        Picasso.get().load(AppConstants.SERVER_URL+imgPath+vehicleItem.getPollutionimg())
                .centerCrop().placeholder(R.drawable.app_logo)
                .resize(125,125).into(addVehicleBinding.imgPollution);
        Picasso.get().load(AppConstants.SERVER_URL+imgPath+vehicleItem.getPermitimg())
                .centerCrop().placeholder(R.drawable.app_logo)
                .resize(125,125).into(addVehicleBinding.imgPermit);
        if(editMode){
            addVehicleBinding.btSubmit.setText("UPDATE INFO");
            addVehicleBinding.textManage.setText("Edit vehicle details");
        }else {
            if(vehicleItem.getStatus().equals("1")){
                addVehicleBinding.btSubmit.setText("INACTIVE");
                addVehicleBinding.textManage.setText("Delete vehicle");
            }else {
                addVehicleBinding.btSubmit.setText("SUBMIT INFO");
                addVehicleBinding.textManage.setText("Add new vehicle");
            }

        }

    }

    private void addDataToSpinner(List<VehicletypeItem>spinnerItems){
        selectedVehicleType = "";
        iconSpinnerItems = new ArrayList<>();
        for (int i=0;i<spinnerItems.size();i++){
            if(spinnerItems.get(i).getVehicletype().length()!=0){
                IconSpinnerItem iconSpinnerItem = new IconSpinnerItem(spinnerItems.get(i).getVehicletype());
                iconSpinnerItems.add(iconSpinnerItem);
            }
        }
        IconSpinnerAdapter iconSpinnerAdapter = new IconSpinnerAdapter(addVehicleBinding.spinnerType);
        iconSpinnerAdapter.setItems(iconSpinnerItems);

        addVehicleBinding.spinnerType.setSpinnerAdapter(iconSpinnerAdapter);
        addVehicleBinding.spinnerType.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<Object>() {
            @Override
            public void onItemSelected(int i, @org.jetbrains.annotations.Nullable Object o, int i1, Object t1) {
                Timber.i(spinnerItems.get(i1).getId()+" "+spinnerItems.get(i1).getVehicletype());
                selectedVehicleType = spinnerItems.get(i1).getId();
            }
        });



        if(vehicleItem!=null){
            for (int j = 0;j<spinnerItems.size();j++){
                if(spinnerItems.get(j).getId().equals(vehicleItem.getVehicletype())){
                    addVehicleBinding.spinnerType.selectItemByIndex(j);
                }
            }
        }
    }

    private void setDOB(){
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "dd-MM-yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                if(dobMode == 1){
                    addVehicleBinding.etInsuranceValidity.setText(sdf.format(myCalendar.getTime()));
                }else if(dobMode == 2){
                    addVehicleBinding.etPermitValidity.setText(sdf.format(myCalendar.getTime()));
                }else if(dobMode == 3){
                    addVehicleBinding.etPollutionValidity.setText(sdf.format(myCalendar.getTime()));
                }
            }

        };

        addVehicleBinding.etInsuranceValidity.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addVehicleBinding.spinnerType.dismiss();
                dobMode = 1;
                new DatePickerDialog(getActivity(),R.style.datepicker, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        addVehicleBinding.etPermitValidity.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addVehicleBinding.spinnerType.dismiss();
                dobMode = 2;
                new DatePickerDialog(getActivity(),R.style.datepicker, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        addVehicleBinding.etPollutionValidity.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addVehicleBinding.spinnerType.dismiss();
               dobMode = 3;
                new DatePickerDialog(getActivity(),R.style.datepicker, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }


    private void callAddVehicleApi(){
        if(NetworkUtility.isOnline(getActivity())) {
            MessageProgressDialog.getInstance().show(getActivity());
            AppRepository appRepository = new AppRepository(getActivity());

            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            builder.addFormDataPart("travelsid",appRepository.getUser().getUserId());
            builder.addFormDataPart("vehicletype",selectedVehicleType);
            builder.addFormDataPart("brand",addVehicleBinding.etVehicleName.getText().toString());
            builder.addFormDataPart("model",addVehicleBinding.etModelName.getText().toString());
            builder.addFormDataPart("regno",addVehicleBinding.etRegNo.getText().toString());
            builder.addFormDataPart("insurancedate",addVehicleBinding.etInsuranceValidity.getText().toString());
            builder.addFormDataPart("pollutiondate",addVehicleBinding.etPollutionValidity.getText().toString());
            builder.addFormDataPart("permitdate",addVehicleBinding.etPermitValidity.getText().toString());
            builder.addFormDataPart("seat",addVehicleBinding.etSeatCount.getText().toString());
            builder.addFormDataPart("km",addVehicleBinding.etStartKm.getText().toString());
            builder.addFormDataPart("vehiclename",addVehicleBinding.etVehicleNickName.getText().toString());

            if(RCFront !=null && RCFront.length()!=0){
                File rcFile = new File(RCFront);
                if(rcFile.exists()){
                    builder.addFormDataPart("rcbookimg", rcFile.getName(),
                            RequestBody.create(rcFile,MediaType.parse("multipart/form-data")));
                }
            }

            if(pollutionFront !=null && pollutionFront.length()!=0){
                File pollutionFile = new File(pollutionFront);
                if(pollutionFile.exists()){
                    builder.addFormDataPart("pollutionimg", pollutionFile.getName(),
                            RequestBody.create(pollutionFile,MediaType.parse("multipart/form-data")));
                }
            }

            if(insuranceFront !=null && insuranceFront.length()!=0){
                File insuranceFile = new File(insuranceFront);
                if(insuranceFile.exists()){
                    builder.addFormDataPart("insuranceimg", insuranceFile.getName(),
                            RequestBody.create(insuranceFile,MediaType.parse("multipart/form-data")));
                }
            }

            if(permitFront !=null && permitFront.length()!=0){
                File permitFile = new File(permitFront);
                if(permitFile.exists()){
                    builder.addFormDataPart("permitimg", permitFile.getName(),
                            RequestBody.create(permitFile,MediaType.parse("multipart/form-data")));
                }
            }

            if(profilePath !=null && profilePath.length()!=0){
                File profileFile = new File(profilePath);
                if(profileFile.exists()){
                    builder.addFormDataPart("taximg", profileFile.getName(),
                            RequestBody.create(profileFile,MediaType.parse("multipart/form-data")));
                }
            }

            if(editMode){
                builder.addFormDataPart("vehicleid",vehicleItem.getId());
            }
            RequestBody requestBody = builder.build();
            Call<VehicleListResponse> responseCall = null;

            if(editMode){
                responseCall = RetrofitClientInstance.getApiService().updateVehicle(requestBody);
            }else {
                responseCall = RetrofitClientInstance.getApiService().createVehicle(requestBody);
            }
            responseCall.enqueue(new Callback<VehicleListResponse>() {
                @Override
                public void onResponse(Call<VehicleListResponse> call, Response<VehicleListResponse> response) {
                    MessageProgressDialog.getInstance().dismiss();
                    if(response.code() == 200 && response.body()!=null){
                        if(response.body().getStatus() == 1){
                            homeActivity.showSuccessToast("Vehicle added successfully!");
                            homeActivity.onBackPressed();
                        }else {
                            homeActivity.showErrorToast(getString(R.string.something_wrong));
                        }
                    }else {
                        homeActivity.showErrorToast(getString(R.string.something_wrong));
                    }
                }

                @Override
                public void onFailure(Call<VehicleListResponse> call, Throwable t) {
                    MessageProgressDialog.getInstance().dismiss();
                }
            });


        }else {
            homeActivity.showErrorToast(getString(R.string.no_internet));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btSubmit:
                if(addVehicleBinding.btSubmit.getText().equals("INACTIVE")){
                    deleteVehicle(vehicleItem.getId());
                }else {
                    addVehicleBinding.spinnerType.dismiss();
                    submitData();
                }

                break;
            case R.id.imgProfile:
                mode = 1;
                addVehicleBinding.spinnerType.dismiss();
                requestMultiplePermissions();
                break;
            case R.id.imgInsurance:
                mode = 2;
                addVehicleBinding.spinnerType.dismiss();
                requestMultiplePermissions();
                break;
            case R.id.imgRC:
                mode = 3;
                addVehicleBinding.spinnerType.dismiss();
                requestMultiplePermissions();
                break;
            case R.id.imgPollution:
                mode = 4;
                addVehicleBinding.spinnerType.dismiss();
                requestMultiplePermissions();
                break;
            case R.id.imgPermit:
                mode = 5;
                addVehicleBinding.spinnerType.dismiss();
                requestMultiplePermissions();
                break;
            case R.id.fabEdit:
                addVehicleBinding.spinnerType.dismiss();
                setEditable(true);
                break;
            case R.id.parentLayout:
                addVehicleBinding.spinnerType.dismiss();
                break;
        }
    }

    private void deleteVehicle(String status) {
        if(NetworkUtility.isOnline(getActivity())) {
            JsonObject inputObject = new JsonObject();
            inputObject.addProperty("vehicleid",status);

            MessageProgressDialog.getInstance().show(getActivity());
            Call<JsonObject>deleteApiCall = RetrofitClientInstance.getApiService().deleteVehicle(inputObject);
            deleteApiCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    MessageProgressDialog.getInstance().dismiss();
                    if(response.code() == 200 && response.body()!=null && response.body().has("status")){
                        int status = response.body().get("status").getAsInt();
                        if(status == 1){
                            homeActivity.onBackPressed();
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    MessageProgressDialog.getInstance().dismiss();
                }
            });
        }else {
            homeActivity.showErrorToast(getString(R.string.no_internet));
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
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                    } else {
                        ImageDecoder.Source source = ImageDecoder.createSource(getActivity().getContentResolver(), contentURI);
                        bitmap = ImageDecoder.decodeBitmap(source);
                    }
                    switch (mode){
                        case 1:
                            profilePath = saveImage(bitmap);
                            addVehicleBinding.imgProfile.setImageBitmap(bitmap);
                            addVehicleBinding.imgAdd.setVisibility(View.GONE);
                            mode = 0;
                            break;
                        case 2:
                            insuranceFront = saveImage(bitmap);
                            addVehicleBinding.imgInsurance.setImageBitmap(bitmap);
                            mode = 0;
                            break;
                        case 3:
                            RCFront = saveImage(bitmap);
                            addVehicleBinding.imgRC.setImageBitmap(bitmap);
                            mode = 0;
                            break;
                        case 4:
                            pollutionFront = saveImage(bitmap);
                            addVehicleBinding.imgPollution.setImageBitmap(bitmap);
                            mode = 0;
                            break;
                        case 5:
                            permitFront = saveImage(bitmap);
                            addVehicleBinding.imgPermit.setImageBitmap(bitmap);
                            mode = 0;
                            break;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
//            Uri contentURI = data.getData();
            if(photoFile!=null && photoFile.exists()){
                switch (mode){
                    case 1:
                        profilePath = photoFile.getAbsolutePath();
                        Picasso.get().load(new File(profilePath)).into(addVehicleBinding.imgProfile);
                        addVehicleBinding.imgAdd.setVisibility(View.GONE);
                        mode = 0;
                        break;
                    case 2:
                        insuranceFront = photoFile.getAbsolutePath();
                        Picasso.get().load(new File(insuranceFront)).into(addVehicleBinding.imgInsurance);
                        mode = 0;
                        break;
                    case 3:
                        RCFront = photoFile.getAbsolutePath();
                        Picasso.get().load(new File(RCFront)).into(addVehicleBinding.imgRC);
                        mode = 0;
                        break;
                    case 4:
                        pollutionFront = photoFile.getAbsolutePath();
                        Picasso.get().load(new File(pollutionFront)).into(addVehicleBinding.imgPollution);
                        mode = 0;
                        break;
                    case 5:
                        permitFront = photoFile.getAbsolutePath();
                        Picasso.get().load(new File(permitFront)).into(addVehicleBinding.imgPermit);
                        mode = 0;
                        break;
                }
            }
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

    private void submitData(){
        if(addVehicleBinding.spinnerType.getText().toString().startsWith("Select")){
            homeActivity.showErrorToast("Please select the vehicle type");
            addVehicleBinding.spinnerType.requestFocus();
        }else if(addVehicleBinding.etVehicleName.getText().length() == 0){
            homeActivity.showErrorToast("Please enter the brand name");
            addVehicleBinding.etVehicleName.requestFocus();
        }else if(addVehicleBinding.etModelName.getText().length() == 0){
            homeActivity.showErrorToast("Please enter the model name");
            addVehicleBinding.etModelName.requestFocus();
        }else if(addVehicleBinding.etVehicleNickName.getText().length() == 0){
            homeActivity.showErrorToast("Please enter the vehicle name");
            addVehicleBinding.etVehicleNickName.requestFocus();
        }else if(addVehicleBinding.etRegNo.getText().length() == 0){
            homeActivity.showErrorToast("Please enter the registration no");
            addVehicleBinding.etRegNo.requestFocus();
        }else if(addVehicleBinding.etInsuranceValidity.getText().length() == 0){
            homeActivity.showErrorToast("Please enter the insurance validity");
            addVehicleBinding.etInsuranceValidity.requestFocus();
        }else if(addVehicleBinding.etPermitValidity.getText().length() == 0){
            homeActivity.showErrorToast("Please enter the permit validity");
            addVehicleBinding.etPermitValidity.requestFocus();
        }else if(addVehicleBinding.etPollutionValidity.getText().length() == 0){
            homeActivity.showErrorToast("Please enter the pollution validity");
            addVehicleBinding.etPollutionValidity.requestFocus();
        }else if(addVehicleBinding.etStartKm.getText().length() == 0){
            homeActivity.showErrorToast("Please enter the starting kilometer");
            addVehicleBinding.etStartKm.requestFocus();
        }else {
            callAddVehicleApi();
        }
    }

    private void setEditable(boolean status){
        addVehicleBinding.etVehicleName.setEnabled(status);
        addVehicleBinding.etVehicleNickName.setEnabled(status);
        addVehicleBinding.etModelName.setEnabled(status);
        addVehicleBinding.etRegNo.setEnabled(status);
        addVehicleBinding.etInsuranceValidity.setEnabled(status);
        addVehicleBinding.etPermitValidity.setEnabled(status);
        addVehicleBinding.etPollutionValidity.setEnabled(status);
        addVehicleBinding.etSeatCount.setEnabled(status);
        addVehicleBinding.etStartKm.setEnabled(status);
        addVehicleBinding.imgInsurance.setEnabled(status);
        addVehicleBinding.imgPermit.setEnabled(status);
        addVehicleBinding.imgRC.setEnabled(status);
        addVehicleBinding.imgPollution.setEnabled(status);
        addVehicleBinding.imgAdd.setEnabled(status);
        addVehicleBinding.imgProfile.setEnabled(status);
        addVehicleBinding.spinnerType.setEnabled(status);
        if(status){
            addVehicleBinding.btSubmit.setVisibility(View.VISIBLE);
            addVehicleBinding.fabEdit.setVisibility(View.GONE);
        }else {
            addVehicleBinding.btSubmit.setVisibility(View.INVISIBLE);
            addVehicleBinding.fabEdit.setVisibility(View.VISIBLE);
        }
    }


}