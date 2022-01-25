package com.app.wheelsonadminapp.ui.home.expense;

import static android.app.Activity.RESULT_CANCELED;

import android.Manifest;
import android.annotation.SuppressLint;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.wheelsonadminapp.R;
import com.app.wheelsonadminapp.data.network.RetrofitClientInstance;
import com.app.wheelsonadminapp.databinding.FragmentAddExpenseBinding;
import com.app.wheelsonadminapp.model.auth.vehicle.VehicleItem;
import com.app.wheelsonadminapp.model.expense.ExpenseItem;
import com.app.wheelsonadminapp.model.expense.ExpenseItemListResponse;
import com.app.wheelsonadminapp.model.expense.ExpenseRecyclerAdapter;
import com.app.wheelsonadminapp.model.expense.VehicleExpenseItem;
import com.app.wheelsonadminapp.model.expense.expense_list.ExpenseListResponse;
import com.app.wheelsonadminapp.model.service.ServiceItemResponse;
import com.app.wheelsonadminapp.model.service.VehicleServiceItem;
import com.app.wheelsonadminapp.ui.home.HomeActivity;
import com.app.wheelsonadminapp.ui.home.service.CustomListViewDialog;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddExpenseFragment extends Fragment implements View.OnClickListener,ExpenseItemListAdapter.ExpenseItemClickListener,ExpenseRecyclerAdapter.ExpenseItemClickListener {
    HomeActivity homeActivity;
    FragmentAddExpenseBinding binding;
    VehicleItem vehicleItem;
    String imgPath;
    List<ExpenseItem> expenseItemsFromServer;
    CustomListViewDialog customDialog;
    List<VehicleExpenseItem>vehicleExpenseItems;
    ExpenseRecyclerAdapter expenseRecyclerAdapter;
    List<com.app.wheelsonadminapp.model.expense.expense_list.ExpenseItem> vehicleExpenseListFromServer;
    String tripId;
    boolean updateList = false;
    private int GALLERY = 1, CAMERA = 2;
    ImageView imgExpense;
    File photoFile = null;
    String serverImageBaseUrl = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeActivity = (HomeActivity)getActivity();
        if(getArguments()!=null && getArguments().getParcelable(AppConstants.VEHICLE)!=null){
            vehicleItem = getArguments().getParcelable(AppConstants.VEHICLE);
            imgPath =  getArguments().getString(AppConstants.IMAGE_PATH);
        }
        if(getArguments()!=null && getArguments().getString("TRIP_ID")!=null){
            tripId = getArguments().getString("TRIP_ID");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddExpenseBinding.inflate(inflater,container,false);
        binding.btClose.setOnClickListener(this);
        binding.btAdd.setOnClickListener(this);
        vehicleExpenseItems = new ArrayList<>();
        binding.serviceRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        expenseRecyclerAdapter = new ExpenseRecyclerAdapter(vehicleExpenseItems,getActivity(),this);
        binding.serviceRecycler.setAdapter(expenseRecyclerAdapter);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btClose:
                homeActivity.onBackPressed();
                break;
            case R.id.btAdd:
                showListDialog(expenseItemsFromServer);
                break;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getExpenseTypeItems();
    }

    private void getExpenseTypeItems(){
        if(NetworkUtility.isOnline(getActivity())){
            MessageProgressDialog.getInstance().show(getActivity());
            Call<ExpenseItemListResponse> serviceItemResponseCall = RetrofitClientInstance.getApiService().getExpenseItems();
            serviceItemResponseCall.enqueue(new Callback<ExpenseItemListResponse>() {
                @Override
                public void onResponse(Call<ExpenseItemListResponse> call, Response<ExpenseItemListResponse> response) {
                    MessageProgressDialog.getInstance().dismiss();
                    if(response.code() == 200 && response.body()!=null){
                        if(response.body().getExpense()!=null && response.body().getExpense().size()!=0){
                            expenseItemsFromServer = response.body().getExpense();
                            if(tripId!=null){
                                getExpenseByTripId(tripId);
                            }
                        }else {
                            homeActivity.showErrorToast(getString(R.string.something_wrong));
                        }
                    }else {
                        homeActivity.showErrorToast(getString(R.string.something_wrong));
                    }
                }

                @Override
                public void onFailure(Call<ExpenseItemListResponse> call, Throwable t) {
                    MessageProgressDialog.getInstance().dismiss();
                }
            });
        }else {
            homeActivity.showErrorToast(getString(R.string.no_internet));
        }
    }

    public void showListDialog(List<ExpenseItem>expenseItemsFromServer){
        ExpenseItemListAdapter expenseItemListAdapter = new ExpenseItemListAdapter(expenseItemsFromServer,this);
        customDialog = new CustomListViewDialog(getActivity(), expenseItemListAdapter);
        customDialog.show();
        customDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void clickOnItem(ExpenseItem data) {
        if(checkAlreadyAdded(data.getExpensetypeid())){
            homeActivity.showErrorToast("Expense type already added!");
        }else {
            showExpenseAddDialog(data.getExpensetypename(),data.getExpensetypeid());
        }
        if (customDialog != null){
            customDialog.dismiss();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void showExpenseAddDialog(String expenseName, String expenseTypeId){
        Dialog dialog = new Dialog(getActivity(),R.style.PauseDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.expense_dialog);
        Button btAdd = dialog.findViewById(R.id.btAdd);
        Button btCancel = dialog.findViewById(R.id.btCancel);
        EditText etType = dialog.findViewById(R.id.etType);
        EditText etRemarks = dialog.findViewById(R.id.etRemarks);
        TextView textServiceName = dialog.findViewById(R.id.textServiceName);
        imgExpense = dialog.findViewById(R.id.imgExpense);
        textServiceName.setText(expenseName);

        imgExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestMultiplePermissions();
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btAdd.setOnClickListener(v -> {
            if(etType.getText().length()!=0){

                VehicleExpenseItem vehicleExpenseItem =new VehicleExpenseItem();
                vehicleExpenseItem.setExpenseValue(etType.getText().toString());
                vehicleExpenseItem.setExpenseDate("");
                vehicleExpenseItem.setExpenseTypeId(expenseTypeId);
                vehicleExpenseItem.setExpenseTripId(tripId);
                vehicleExpenseItem.setExpenseName(expenseName);
//                    vehicleExpenseItem.setVehicleId(vehicleItem.getId());
                vehicleExpenseItem.setExpenseImage(imgPath);
                vehicleExpenseItem.setExpenseRemarks(etRemarks.getText().toString());
//                    vehicleExpenseItem.setTravelsId(vehicleItem.getTravelsid());
//                    callAddServiceApi(vehicleServiceItem);
                addTripExpenseToServer(vehicleExpenseItem);


               /* if(checkAlreadyAdded(expenseTypeId)){
                    homeActivity.showErrorToast("Expense item already added !");
                }else {
                }*/

            }else {
                homeActivity.showErrorToast("Please enter the Kilometer");
            }
            dialog.dismiss();
        });
        Objects.requireNonNull(dialog.getWindow()).getDecorView().setBackgroundColor(Color.TRANSPARENT);
        dialog.show();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void showExpenseUpdateDialog(VehicleExpenseItem vExpenseItem){
        Dialog dialog = new Dialog(getActivity(),R.style.PauseDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.expense_dialog);
        Button btUpdate = dialog.findViewById(R.id.btAdd);
        Button btCancel = dialog.findViewById(R.id.btCancel);
        EditText etType = dialog.findViewById(R.id.etType);
        EditText etRemarks = dialog.findViewById(R.id.etRemarks);
        TextView textServiceName = dialog.findViewById(R.id.textServiceName);
        imgExpense = dialog.findViewById(R.id.imgExpense);
        textServiceName.setText(vExpenseItem.getExpenseName());
        etType.setText(vExpenseItem.getExpenseValue());
        etRemarks.setText(vExpenseItem.getExpenseRemarks());
        btUpdate.setText("Update");

        if(vExpenseItem.getExpenseImage().startsWith("trip")){
            Picasso.get().load(AppConstants.SERVER_URL+serverImageBaseUrl+vExpenseItem.getExpenseImage()).error(R.drawable.ic_baseline_add_a_photo_24).into(imgExpense);
        }else {
            Picasso.get().load(new File(vExpenseItem.getExpenseImage())).error(R.drawable.ic_baseline_add_a_photo_24).into(imgExpense);
        }
        imgExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestMultiplePermissions();
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btUpdate.setOnClickListener(v -> {
            if(etType.getText().length()!=0){
                vExpenseItem.setExpenseValue(etType.getText().toString());
                vExpenseItem.setExpenseRemarks(etRemarks.getText().toString());
                updateTripExpenseToServer(vExpenseItem);

            }else {
                homeActivity.showErrorToast("Please enter the Kilometer");
            }
            dialog.dismiss();
        });
        Objects.requireNonNull(dialog.getWindow()).getDecorView().setBackgroundColor(Color.TRANSPARENT);
        dialog.show();
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
//        startActivityForResult(intent, REQUEST_CAMERA);
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



    private boolean checkAlreadyAdded(String expenseTypeId){
        boolean status = false;
        for(VehicleExpenseItem expenseItem : vehicleExpenseItems){
            if(expenseItem.getExpenseTypeId().equals(expenseTypeId)){
                status = true;
                break;
            }
        }
        return status;
    }

    private void getExpenseByTripId(String tripId){
        if(NetworkUtility.isOnline(getActivity())){
            JsonObject inputObject = new JsonObject();
            inputObject.addProperty("expensetripid",tripId);
            MessageProgressDialog.getInstance().show(getActivity());
            Call<ExpenseListResponse>expenseListResponseCall = RetrofitClientInstance.getApiService().getExpenseByTripId(inputObject);
            expenseListResponseCall.enqueue(new Callback<ExpenseListResponse>() {
                @Override
                public void onResponse(Call<ExpenseListResponse> call, Response<ExpenseListResponse> response) {
                    MessageProgressDialog.getInstance().dismiss();
                    if(response.code() == 200 && response.body()!=null){
                        if(response.body().getExpense()!=null && response.body().getExpense().size()!=0){
                            vehicleExpenseListFromServer = response.body().getExpense();
                            serverImageBaseUrl = response.body().getPath();
                            updateList = true;
                            for (int i=0;i<vehicleExpenseListFromServer.size();i++){
                                VehicleExpenseItem vehicleExpenseItem = Utils.toVehicleExpenseItem(vehicleExpenseListFromServer.get(i));
                                vehicleExpenseItems.add(vehicleExpenseItem);
                            }

                            expenseRecyclerAdapter.notifyDataSetChanged();
                        }
                    }else {
                        homeActivity.showErrorToast(getString(R.string.something_wrong));
                    }
                }

                @Override
                public void onFailure(Call<ExpenseListResponse> call, Throwable t) {
                    MessageProgressDialog.getInstance().dismiss();
                }
            });
        }else {
            homeActivity.showErrorToast(getString(R.string.no_internet));
        }
    }

    private void addTripExpenseToServer(VehicleExpenseItem vehicleExpenseItem){
        if(NetworkUtility.isOnline(getActivity())) {
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            builder.addFormDataPart("tripid",vehicleExpenseItem.getExpenseTripId());
            builder.addFormDataPart("typeid",vehicleExpenseItem.getExpenseTypeId());
            builder.addFormDataPart("amount",vehicleExpenseItem.getExpenseValue());
            builder.addFormDataPart("remark",vehicleExpenseItem.getExpenseRemarks());

            if(imgPath!=null && imgPath.length()!=0){
                File imgFile = new File(imgPath);
                builder.addFormDataPart("expenseimg", imgFile.getName(),
                        RequestBody.create(imgFile, MediaType.parse("multipart/form-data")));
            }
            RequestBody requestBody = builder.build();
            MessageProgressDialog.getInstance().show(getActivity());
            Call<JsonObject> addExpenseCall;
            addExpenseCall = RetrofitClientInstance.getApiService().addTripExpense(requestBody);

            addExpenseCall.enqueue(new Callback<JsonObject>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    MessageProgressDialog.getInstance().dismiss();
                    if(response.code() == 200 && response.body()!=null && response.body().has("status")){
                        int status = response.body().get("status").getAsInt();
                        if(status == 1){
                            vehicleExpenseItems.add(vehicleExpenseItem);
                            expenseRecyclerAdapter.notifyDataSetChanged();
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

    private void updateTripExpenseToServer(VehicleExpenseItem vehicleExpenseItem){
        if(NetworkUtility.isOnline(getActivity())) {
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            builder.addFormDataPart("expenseid",vehicleExpenseItem.getExpenseListId());
            builder.addFormDataPart("tripid",vehicleExpenseItem.getExpenseTripId());
            builder.addFormDataPart("typeid",vehicleExpenseItem.getExpenseTypeId());
            builder.addFormDataPart("amount",vehicleExpenseItem.getExpenseValue());
            builder.addFormDataPart("remark",vehicleExpenseItem.getExpenseRemarks());

            if(imgPath!=null && imgPath.length()!=0){
                File imgFile = new File(imgPath);
                builder.addFormDataPart("expenseimg", imgFile.getName(),
                        RequestBody.create(imgFile, MediaType.parse("multipart/form-data")));
                vehicleExpenseItem.setExpenseImage(imgPath);
            }
            RequestBody requestBody = builder.build();
            MessageProgressDialog.getInstance().show(getActivity());
            Call<JsonObject> addExpenseCall;
            addExpenseCall = RetrofitClientInstance.getApiService().updateExpense(requestBody);
            addExpenseCall.enqueue(new Callback<JsonObject>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    MessageProgressDialog.getInstance().dismiss();
                    if(response.code() == 200 && response.body()!=null && response.body().has("status")){
                        int status = response.body().get("status").getAsInt();
                        if(status == 1){
                            expenseRecyclerAdapter.notifyDataSetChanged();
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
                        bitmap = MediaStore.Images.Media.getBitmap(homeActivity.getContentResolver(), contentURI);
                    } else {
                        ImageDecoder.Source source = ImageDecoder.createSource(homeActivity.getContentResolver(), contentURI);
                        bitmap = ImageDecoder.decodeBitmap(source);
                    }

                    imgPath = saveImage(bitmap);
                    Picasso.get().load(new File(imgPath)).into(imgExpense);


                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            if(photoFile!=null && photoFile.exists()){
                imgPath = photoFile.getAbsolutePath();
                Picasso.get().load(new File(imgPath)).into(imgExpense);
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


    private void showDeleteDialog(VehicleExpenseItem vExpenseItem,int position){
        Dialog dialog = new Dialog(getActivity(),R.style.PauseDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.remove_dialog);
        Button btCancel = dialog.findViewById(R.id.btCancel);
        Button btDelete = dialog.findViewById(R.id.btDelete);
        TextView textServiceName = dialog.findViewById(R.id.textServiceName);
        textServiceName.setText(vExpenseItem.getExpenseName());
        btDelete.setOnClickListener(v -> {
            callDeleteExpense(vExpenseItem,position);
            dialog.dismiss();
        });
        btCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        Objects.requireNonNull(dialog.getWindow()).getDecorView().setBackgroundColor(Color.TRANSPARENT);
        dialog.show();
    }

    @Override
    public void onDeleteClicked(int position, VehicleExpenseItem vehicleExpenseItem) {
        showDeleteDialog(vehicleExpenseItem,position);
    }

    @Override
    public void onItemSelected(int position, VehicleExpenseItem vehicleExpenseItem) {
        showExpenseUpdateDialog(vehicleExpenseItem);
    }



    private void callDeleteExpense(VehicleExpenseItem expenseItem,int position){
        if(NetworkUtility.isOnline(getActivity())) {
            JsonObject inputObject = new JsonObject();
            inputObject.addProperty("expenseid",expenseItem.getExpenseListId());
            inputObject.addProperty("expensetypeid",expenseItem.getExpenseTypeId());
            MessageProgressDialog.getInstance().show(getActivity());
            Call<JsonObject>deleteApiCall = RetrofitClientInstance.getApiService().deleteExpense(inputObject);
            deleteApiCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    MessageProgressDialog.getInstance().dismiss();
                    if(response.code() == 200 && response.body()!=null && response.body().has("status")){
                        int status = response.body().get("status").getAsInt();
                        if(status == 1){
                            vehicleExpenseItems.remove(position);
                            expenseRecyclerAdapter.notifyItemRemoved(position);
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
}