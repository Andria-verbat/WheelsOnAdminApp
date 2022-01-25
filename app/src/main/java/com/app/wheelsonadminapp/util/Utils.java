package com.app.wheelsonadminapp.util;

import android.content.Context;
import android.os.Environment;

import com.app.wheelsonadminapp.model.expense.VehicleExpenseItem;
import com.app.wheelsonadminapp.model.expense.expense_list.ExpenseItem;
import com.app.wheelsonadminapp.model.service.VehicleServiceItem;
import com.app.wheelsonadminapp.model.service.vehicle_server_service.ServiceDetailsItem;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static String getJsonFromAssets(Context context, String fileName) {
        String jsonString;
        try {
            InputStream is = context.getAssets() .open(fileName);

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            jsonString = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return jsonString;
    }

    public static String inputStreamToString(InputStream inputStream) {
        try {
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes, 0, bytes.length);
            String json = new String(bytes);
            return json;
        } catch (IOException e) {
            return null;
        }
    }

    // https://developer.android.com/training/camera/photobasics#java

    public static File createImageFile(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
//        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public static VehicleServiceItem toVehicleServiceItem(ServiceDetailsItem serviceDetailsItem,String travelsId){
        VehicleServiceItem vehicleServiceItem = new VehicleServiceItem();
        vehicleServiceItem.setServiceKM(serviceDetailsItem.getServicekm());
        vehicleServiceItem.setVehicleId(serviceDetailsItem.getServicevehicleid());
        vehicleServiceItem.setServiceDate(serviceDetailsItem.getServicedate());
        vehicleServiceItem.setServiceName(serviceDetailsItem.getServicetype());
        vehicleServiceItem.setServiceTypeId(serviceDetailsItem.getServicetypeid());
        vehicleServiceItem.setTravelsId(travelsId);
        vehicleServiceItem.setServiceListId(serviceDetailsItem.getServiceid());
        if(serviceDetailsItem.getServicekm().equals("0")){
            vehicleServiceItem.setServiceValue(serviceDetailsItem.getServicedate());
        }else {
            vehicleServiceItem.setServiceValue(serviceDetailsItem.getServicekm());
        }
        return vehicleServiceItem;
    }

    public static VehicleExpenseItem toVehicleExpenseItem(ExpenseItem serviceDetailsItem){
        VehicleExpenseItem vehicleExpenseItem = new VehicleExpenseItem();
        vehicleExpenseItem.setExpenseTripId(serviceDetailsItem.getExpensetripid());
        vehicleExpenseItem.setExpenseRemarks(serviceDetailsItem.getExpenseremark());
        vehicleExpenseItem.setExpenseImage(serviceDetailsItem.getExpenseimage());
        vehicleExpenseItem.setExpenseDate("");
        vehicleExpenseItem.setExpenseTypeId(serviceDetailsItem.getExpensetypeid());
        vehicleExpenseItem.setExpenseName(serviceDetailsItem.getExpensetype());
        vehicleExpenseItem.setExpenseValue(serviceDetailsItem.getExpenseAmount());
        vehicleExpenseItem.setExpenseListId(serviceDetailsItem.getExpenseid());
        return vehicleExpenseItem;
    }

}
