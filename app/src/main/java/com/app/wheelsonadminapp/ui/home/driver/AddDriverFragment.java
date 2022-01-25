package com.app.wheelsonadminapp.ui.home.driver;

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
import com.app.wheelsonadminapp.data.network.ApiService;
import com.app.wheelsonadminapp.data.network.RetrofitClientInstance;
import com.app.wheelsonadminapp.databinding.FragmentAddDriverBinding;
import com.app.wheelsonadminapp.model.driver.DriverItem;
import com.app.wheelsonadminapp.model.driver.DriverResponse;
import com.app.wheelsonadminapp.model.states.DistrictsItem;
import com.app.wheelsonadminapp.model.states.StatesItem;
import com.app.wheelsonadminapp.model.states.StatesResponse;
import com.app.wheelsonadminapp.ui.home.HomeActivity;
import com.app.wheelsonadminapp.util.AppConstants;
import com.app.wheelsonadminapp.util.MessageProgressDialog;
import com.app.wheelsonadminapp.util.NetworkUtility;
import com.app.wheelsonadminapp.util.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.lang.reflect.Type;


import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static android.app.Activity.RESULT_CANCELED;

public class AddDriverFragment extends Fragment implements View.OnClickListener {

    FragmentAddDriverBinding addDriverBinding;
    private int GALLERY = 1, CAMERA = 2;
    String profilePath,aadhaarFront,aadhaarBack,licenceFront,licenceBack;
    // Create the File where the photo should go
    File photoFile = null;
    int mode = 0;
    // 1 --> Profile Pic
    // 2 --> Licence Front
    // 3 --> Licence Back
    // 4 --> Aadhaar Front
    // 5 --> Aadhaar Back

    StatesResponse statesResponse;
    List<IconSpinnerItem>spinnerStateItems,spinnerDistrictItems;
    final Calendar myCalendar = Calendar.getInstance();
    DriverActivity homeActivity;

    boolean profileEdited = false;
    boolean licenceFrontEdited = false;
    boolean licenceBackEdited = false;
    boolean aadhaarFrontEdited = false;
    boolean aadhaarBackEdited = false;

    DriverItem driverItem;
    String imgPath;
    boolean editMode = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeActivity = (DriverActivity)getActivity();
        if(getArguments()!=null && getArguments().getParcelable(AppConstants.DRIVER)!=null){
            driverItem = getArguments().getParcelable(AppConstants.DRIVER);
            imgPath = getArguments().getString(AppConstants.IMAGE_PATH);
            editMode = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        addDriverBinding = FragmentAddDriverBinding.inflate(inflater,container,false);
        addDriverBinding.imgAdd.setOnClickListener(this);
        addDriverBinding.imgProfile.setOnClickListener(this);
        addDriverBinding.frameLicenceFront.setOnClickListener(this);
        addDriverBinding.frameLicenceBack.setOnClickListener(this);
        addDriverBinding.frameAdhaarFront.setOnClickListener(this);
        addDriverBinding.frameAdhaarBack.setOnClickListener(this);
        addDriverBinding.fabEdit.setOnClickListener(this);
        addDriverBinding.btSubmit.setOnClickListener(this);

        if(editMode){
            addDriverBinding.fabEdit.setVisibility(View.VISIBLE);
            addDriverBinding.btSubmit.setVisibility(View.INVISIBLE);
            setEditable(false);
        }else {
            addDriverBinding.fabEdit.setVisibility(View.GONE);
            addDriverBinding.btSubmit.setVisibility(View.VISIBLE);
            setEditable(true);
        }

        String myJson=Utils.inputStreamToString(getActivity().getResources().openRawResource(R.raw.indianstatesdistricts));
        statesResponse = new Gson().fromJson(myJson, StatesResponse.class);
        Timber.i(statesResponse.toString());
        loadStates();
        setDOB();
        if(driverItem!=null){
            loadData(driverItem);
        }
        return addDriverBinding.getRoot();
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
                updateLabel();
            }

        };

        addDriverBinding.etLicenceValidity.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(),R.style.datepicker, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        addDriverBinding.etLicenceValidity.setText(sdf.format(myCalendar.getTime()));
    }

    private void loadStates(){
        List<StatesItem>statesItems = statesResponse.getStates();
        spinnerStateItems = new ArrayList<>();
        spinnerDistrictItems = new ArrayList<>();
        for (int i = 0;i< statesItems.size();i++){
            IconSpinnerItem iconSpinnerItem = new IconSpinnerItem(statesItems.get(i).getName());
            spinnerStateItems.add(iconSpinnerItem);
        }
        IconSpinnerAdapter iconSpinnerAdapter = new IconSpinnerAdapter(addDriverBinding.spinnerState);
        iconSpinnerAdapter.setItems(spinnerStateItems);
        addDriverBinding.spinnerState.setSpinnerAdapter(iconSpinnerAdapter);
        addDriverBinding.spinnerState.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<Object>() {

            @Override
            public void onItemSelected(int i, @org.jetbrains.annotations.Nullable Object o, int i1, Object t1) {
                List<DistrictsItem>districtsItems = statesResponse.getStates().get(i1).getDistricts();
                for (int j = 0;j< districtsItems.size();j++){
                    IconSpinnerItem iconSpinnerItem = new IconSpinnerItem(districtsItems.get(j).getName());
                    spinnerDistrictItems.add(iconSpinnerItem);
                }
                IconSpinnerAdapter iconSpinnerAdapter2 = new IconSpinnerAdapter(addDriverBinding.spinnerDistrict);
                iconSpinnerAdapter2.setItems(spinnerDistrictItems);
                addDriverBinding.spinnerDistrict.setSpinnerAdapter(iconSpinnerAdapter2);
            }
        });
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
            case R.id.imgAdd:
            case R.id.imgProfile:
                mode = 1;
                requestMultiplePermissions();
                break;
            case R.id.frameLicenceFront:
                mode = 2;
                requestMultiplePermissions();
                break;
            case R.id.frameLicenceBack:
                mode = 3;
                requestMultiplePermissions();
                break;
            case R.id.frameAdhaarFront:
                mode = 4;
                requestMultiplePermissions();
                break;
            case R.id.frameAdhaarBack:
                mode = 5;
                requestMultiplePermissions();
                break;
            case R.id.btSubmit:
                addDriver();
                break;
            case R.id.fabEdit:
                setEditable(true);
                break;

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
                            profilePath = saveImage(bitmap);
                            addDriverBinding.imgProfile.setImageBitmap(bitmap);
                            addDriverBinding.imgAdd.setVisibility(View.GONE);
                            mode = 0;
                            break;
                        case 2:
                            licenceFront = saveImage(bitmap);
                            addDriverBinding.imgLicenceFront.setImageBitmap(bitmap);
                            mode = 0;
                            break;
                        case 3:
                            licenceBack = saveImage(bitmap);
                            addDriverBinding.imgLicenceBack.setImageBitmap(bitmap);
                            mode = 0;
                            break;
                        case 4:
                            aadhaarFront = saveImage(bitmap);
                            addDriverBinding.imgAdhaarFront.setImageBitmap(bitmap);
                            mode = 0;
                            break;
                        case 5:
                            aadhaarBack = saveImage(bitmap);
                            addDriverBinding.imgAdhaarBack.setImageBitmap(bitmap);
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
                        profilePath = photoFile.getAbsolutePath();
                        Picasso.get().load(new File(profilePath)).into(addDriverBinding.imgProfile);
                        addDriverBinding.imgAdd.setVisibility(View.GONE);
                        mode = 0;
                        break;
                    case 2:
                        licenceFront = photoFile.getAbsolutePath();
                        Picasso.get().load(new File(licenceFront)).into(addDriverBinding.imgLicenceFront);
                        mode = 0;
                        break;
                    case 3:
                        licenceBack = photoFile.getAbsolutePath();
                        Picasso.get().load(new File(licenceBack)).into(addDriverBinding.imgLicenceBack);
                        mode = 0;
                        break;
                    case 4:
                        aadhaarFront = photoFile.getAbsolutePath();
                        Picasso.get().load(new File(aadhaarFront)).into(addDriverBinding.imgAdhaarFront);
                        mode = 0;
                        break;
                    case 5:
                        aadhaarBack = photoFile.getAbsolutePath();
                        Picasso.get().load(new File(aadhaarBack)).into(addDriverBinding.imgAdhaarBack);
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

    private void addDriver(){
        if(addDriverBinding.etName.getText().length() == 0){
            homeActivity.showErrorToast("Please enter the full name");
            addDriverBinding.etName.requestFocus();
        }else if(addDriverBinding.etMobile.getText().length() == 0){
            homeActivity.showErrorToast("Please enter the mobile number");
            addDriverBinding.etMobile.requestFocus();
        }else if(addDriverBinding.etAddress.getText().length() == 0){
            homeActivity.showErrorToast("Please enter the address");
            addDriverBinding.etAddress.requestFocus();
        }else if(addDriverBinding.spinnerState.getText().toString().startsWith("Select")){
            homeActivity.showErrorToast("Please select the State");
        }else if(addDriverBinding.spinnerDistrict.getText().toString().startsWith("Start")){
            homeActivity.showErrorToast("Please select the District");
        }else if(addDriverBinding.etCity.getText().length() == 0){
            homeActivity.showErrorToast("Please enter the City");
            addDriverBinding.etCity.requestFocus();
        }else if(addDriverBinding.etLicence.getText().length() == 0){
            homeActivity.showErrorToast("Please enter the licence no");
            addDriverBinding.etLicence.requestFocus();
        }else if(addDriverBinding.etLicenceValidity.getText().length() == 0){
            homeActivity.showErrorToast("Please enter the licence validity");
            addDriverBinding.etLicenceValidity.requestFocus();
        }else if(addDriverBinding.spinnerCategory.getText().toString().startsWith("Select")){
            homeActivity.showErrorToast("Please select the Category");
        }else if(addDriverBinding.etAadhaar.getText().length() == 0){
            homeActivity.showErrorToast("Please enter the Aadhaar no");
            addDriverBinding.etAadhaar.requestFocus();
        }else if(addDriverBinding.etExperience.getText().length() == 0){
            homeActivity.showErrorToast("Please enter the experience");
            addDriverBinding.etExperience.requestFocus();
        }else if(addDriverBinding.etPoliceStation.getText().length() == 0){
            homeActivity.showErrorToast("Please enter the Police Station name");
            addDriverBinding.etPoliceStation.requestFocus();
        }else if(!editMode){
            if(licenceFront == null || licenceFront.length() == 0){
                homeActivity.showErrorToast("Please attach the license front picture");
            }else if(licenceBack == null || licenceBack.length() == 0){
                homeActivity.showErrorToast("Please attach the license back picture");
            }else if(aadhaarFront == null ||aadhaarFront.length() == 0){
                homeActivity.showErrorToast("Please attach the Adhaar front picture");
            }else if(aadhaarBack == null || aadhaarBack.length() == 0){
                homeActivity.showErrorToast("Please attach the Aadhaar back picture");
            }else {
                callAddDriverApi();
            }
        }else {
            callAddDriverApi();
        }
    }

    private void callAddDriverApi(){
        if(NetworkUtility.isOnline(getActivity())){
            AppRepository appRepository = new AppRepository(getActivity());
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            builder.addFormDataPart("travelsid",appRepository.getUser().getUserId());
            builder.addFormDataPart("drivername",addDriverBinding.etName.getText().toString());
            builder.addFormDataPart("mobileno",addDriverBinding.etMobile.getText().toString());
            builder.addFormDataPart("address",addDriverBinding.etAddress.getText().toString());
            builder.addFormDataPart("city",addDriverBinding.etCity.getText().toString());
            builder.addFormDataPart("district",addDriverBinding.spinnerDistrict.getText().toString());
            builder.addFormDataPart("state",addDriverBinding.spinnerState.getText().toString());
            builder.addFormDataPart("licenseno",addDriverBinding.etLicence.getText().toString());
            builder.addFormDataPart("licensevalidity",addDriverBinding.etLicenceValidity.getText().toString());
            builder.addFormDataPart("licensecategory",addDriverBinding.spinnerCategory.getText().toString());
            builder.addFormDataPart("aadharno",addDriverBinding.etAadhaar.getText().toString());
            builder.addFormDataPart("experience",addDriverBinding.etExperience.getText().toString());
            builder.addFormDataPart("policestation",addDriverBinding.etPoliceStation.getText().toString());
            builder.addFormDataPart("emailid",appRepository.getUser().getEmail());
            builder.addFormDataPart("password",addDriverBinding.etMobile.toString());

            MessageProgressDialog.getInstance().show(getActivity());

/*            RequestBody travelsId = RequestBody.create(appRepository.getUser().getUserId(), MediaType.parse("multipart/form-data"));
            RequestBody driverName = RequestBody.create(addDriverBinding.etName.getText().toString(), MediaType.parse("multipart/form-data"));
            RequestBody mobileNo = RequestBody.create(addDriverBinding.etMobile.getText().toString(), MediaType.parse("multipart/form-data"));
            RequestBody address = RequestBody.create(addDriverBinding.etAddress.getText().toString(), MediaType.parse("multipart/form-data"));
            RequestBody city = RequestBody.create(addDriverBinding.etCity.getText().toString(), MediaType.parse("multipart/form-data"));
            RequestBody district = RequestBody.create(addDriverBinding.spinnerDistrict.getText().toString(), MediaType.parse("multipart/form-data"));
            RequestBody state = RequestBody.create(addDriverBinding.spinnerState.getText().toString(), MediaType.parse("multipart/form-data"));
            RequestBody licenseNo = RequestBody.create(addDriverBinding.etLicence.getText().toString(), MediaType.parse("multipart/form-data"));
            RequestBody licenseValidity = RequestBody.create(addDriverBinding.etLicenceValidity.getText().toString(), MediaType.parse("multipart/form-data"));
            RequestBody licenseCategory = RequestBody.create(addDriverBinding.spinnerCategory.getText().toString(), MediaType.parse("multipart/form-data"));
            RequestBody aadharNo = RequestBody.create(addDriverBinding.etAadhaar.getText().toString(), MediaType.parse("multipart/form-data"));
            RequestBody experience = RequestBody.create(addDriverBinding.etExperience.getText().toString(), MediaType.parse("multipart/form-data"));
            RequestBody policeStation = RequestBody.create(addDriverBinding.etPoliceStation.getText().toString(), MediaType.parse("multipart/form-data"));
            RequestBody emailId = RequestBody.create(appRepository.getUser().getEmail(), MediaType.parse("multipart/form-data"));
            RequestBody password = RequestBody.create(addDriverBinding.etMobile.toString(), MediaType.parse("multipart/form-data"));
            MultipartBody.Part profileImgBody = null;
            MultipartBody.Part licenceFrontBody = null;
            MultipartBody.Part licenceBackBody = null;
            MultipartBody.Part aadhaarFrontBody = null;
            MultipartBody.Part aadhaarBackBody = null;*/

            if(profilePath != null && profilePath.length()!=0){
                File profileFile = new File(profilePath);
                if(profileFile.exists()){
                    builder.addFormDataPart("driverimg", profileFile.getName(),
                            RequestBody.create(profileFile,MediaType.parse("multipart/form-data")));
                }

            }

            if(licenceFront != null && licenceFront.length()!=0){
                File licenceFrontFile = new File(licenceFront);
                if(licenceFrontFile.exists()){
                    builder.addFormDataPart("licensefrontimg", licenceFrontFile.getName(),
                            RequestBody.create(licenceFrontFile,MediaType.parse("multipart/form-data")));
                }

            }

            if(licenceBack != null && licenceBack.length()!=0){
                File licenceBackFile = new File(licenceBack);
                if(licenceBackFile.exists()){
                    builder.addFormDataPart("licensebackimg", licenceBackFile.getName(),
                            RequestBody.create(licenceBackFile,MediaType.parse("multipart/form-data")));
                }

            }

            if(aadhaarFront != null && aadhaarFront.length()!=0){
                File aadhaarFrontFile = new File(aadhaarFront);
                if(aadhaarFrontFile.exists()){
                    builder.addFormDataPart("aadharfrontimg", aadhaarFrontFile.getName(),
                            RequestBody.create(aadhaarFrontFile,MediaType.parse("multipart/form-data")));
                }

            }

            if(aadhaarBack != null && aadhaarBack.length()!=0){
                File aadhaarBackFile = new File(aadhaarBack);
                if(aadhaarBackFile.exists()){
                    builder.addFormDataPart("aadharbackimg", aadhaarBackFile.getName(),
                            RequestBody.create(aadhaarBackFile,MediaType.parse("multipart/form-data")));
                }
            }

            if(editMode){
                builder.addFormDataPart("driverid",driverItem.getId());
            }
            RequestBody requestBody = builder.build();
            ApiService apiService = RetrofitClientInstance.getRetrofitInstance().create(ApiService.class);
            Call<DriverResponse> driverResponseCall = null;
            if(editMode){
                driverResponseCall = apiService.updateDriverNew(requestBody);
            }else {
                driverResponseCall = apiService.registerDriverNew(requestBody);
            }
            driverResponseCall.enqueue(new Callback<DriverResponse>() {
                @Override
                public void onResponse(Call<DriverResponse> call, Response<DriverResponse> response) {
                    MessageProgressDialog.getInstance().dismiss();
                    if(response.code() == 200 && response.body()!=null){
                        if(response.body().getStatus() == 1){
                            getActivity().onBackPressed();
                        }
                    }
                }

                @Override
                public void onFailure(Call<DriverResponse> call, Throwable t) {
                    MessageProgressDialog.getInstance().dismiss();
                }
            });

        }else {
            homeActivity.showErrorToast(getString(R.string.no_internet));
        }

    }

    private void loadData(DriverItem driverItem){
        StatesItem statesItem = null;
        addDriverBinding.textDrivers.setText(driverItem.getName());
        addDriverBinding.textManage.setText("Edit driver details");
        addDriverBinding.etName.setText(driverItem.getName());
        addDriverBinding.etMobile.setText(driverItem.getMobile());
        addDriverBinding.etAddress.setText(driverItem.getAddress());
        addDriverBinding.etCity.setText(driverItem.getCity());
        addDriverBinding.etLicence.setText(driverItem.getLicenseno());
        addDriverBinding.etLicenceValidity.setText(driverItem.getLicensevalidity());
        addDriverBinding.etAadhaar.setText(driverItem.getAadharno());
        addDriverBinding.etExperience.setText(driverItem.getExperience());
        addDriverBinding.etPoliceStation.setText(driverItem.getPolicestation());
        addDriverBinding.imgProfile.setVisibility(View.VISIBLE);
        if(editMode){
            addDriverBinding.btSubmit.setText("UPDATE INFO");
        }else {
            addDriverBinding.btSubmit.setText("SUBMIT INFO");
        }
        String imgUrl = AppConstants.SERVER_URL+imgPath+driverItem.getDriverimg();
        Timber.i(imgUrl);
        Picasso.get().load(imgUrl)
                .centerCrop().placeholder(R.drawable.app_logo).resize(125,125).into(addDriverBinding.imgProfile);
        Picasso.get().load(AppConstants.SERVER_URL+imgPath+driverItem.getLicensefrontimg())
                .centerCrop().placeholder(R.drawable.app_logo).resize(125,125).into(addDriverBinding.imgLicenceFront);
        Picasso.get().load(AppConstants.SERVER_URL+imgPath+driverItem.getLicensebackimg())
                .centerCrop().placeholder(R.drawable.app_logo).resize(125,125).into(addDriverBinding.imgLicenceBack);
        Picasso.get().load(AppConstants.SERVER_URL+imgPath+driverItem.getAadharfrontimg())
                .centerCrop().placeholder(R.drawable.app_logo).resize(125,125).into(addDriverBinding.imgAdhaarFront);
        Picasso.get().load(AppConstants.SERVER_URL+imgPath+driverItem.getAadharbackimg())
                .centerCrop().placeholder(R.drawable.app_logo).resize(125,125).into(addDriverBinding.imgAdhaarBack);

        for (int i=0;i<statesResponse.getStates().size();i++){
            if(statesResponse.getStates().get(i).getName().equals(driverItem.getState().trim())){
                statesItem = statesResponse.getStates().get(i);
                addDriverBinding.spinnerState.selectItemByIndex(i);
                for(int j=0;j<statesItem.getDistricts().size();j++){
                    if(statesItem.getDistricts().get(j).getName().equals(driverItem.getDistrict().trim())){
                        addDriverBinding.spinnerDistrict.selectItemByIndex(j);
                        break;
                    }
                }
            }
        }
    }

    private void setEditable(boolean status){
        addDriverBinding.etAadhaar.setEnabled(status);
        addDriverBinding.etAddress.setEnabled(status);
        addDriverBinding.etCity.setEnabled(status);
        addDriverBinding.etExperience.setEnabled(status);
        addDriverBinding.etLicence.setEnabled(status);
        addDriverBinding.etLicenceValidity.setEnabled(status);
        addDriverBinding.etMobile.setEnabled(status);
        addDriverBinding.etName.setEnabled(status);
        addDriverBinding.etPoliceStation.setEnabled(status);
        addDriverBinding.spinnerState.setEnabled(status);
        addDriverBinding.spinnerDistrict.setEnabled(status);
        addDriverBinding.spinnerCategory.setEnabled(status);
        addDriverBinding.frameAdhaarBack.setEnabled(status);
        addDriverBinding.frameAdhaarFront.setEnabled(status);
        addDriverBinding.frameLicenceBack.setEnabled(status);
        addDriverBinding.frameLicenceFront.setEnabled(status);
        addDriverBinding.frameImage.setEnabled(status);
        if(status){
            addDriverBinding.btSubmit.setVisibility(View.VISIBLE);
            addDriverBinding.fabEdit.setVisibility(View.GONE);
        }else {
            addDriverBinding.btSubmit.setVisibility(View.INVISIBLE);
            addDriverBinding.fabEdit.setVisibility(View.VISIBLE);
        }
    }
}