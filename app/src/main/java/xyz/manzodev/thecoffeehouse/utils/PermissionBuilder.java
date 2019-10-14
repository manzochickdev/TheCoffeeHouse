package xyz.manzodev.thecoffeehouse.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

public class PermissionBuilder {
    private Activity activity;
    private Fragment fragment;
    private String permission;
    private String rationaleTitle , rationaleBody;
    private OnRequestSuccessListener onRequestSuccessListener;

    public PermissionBuilder withActivity(AppCompatActivity activity){
        this.activity = activity;
        return this;
    }

    public PermissionBuilder withFragment(Fragment fragment){
        this.fragment = fragment;
        return this;
    }

    public PermissionBuilder withPermission(String permission){
        this.permission = permission;
        return this;
    }


    public PermissionBuilder withRationale(String rationaleTitle,String rationaleBody){
        this.rationaleTitle = rationaleTitle;
        this.rationaleBody = rationaleBody;
        return this;
    }

    public PermissionBuilder withSuccessListener(OnRequestSuccessListener onRequestSuccessListener){
        this.onRequestSuccessListener = onRequestSuccessListener;
        return this;
    }



    public void build(){
        if (this.activity!=null){
            if (ContextCompat.checkSelfPermission(activity,
                    permission)
                    != PackageManager.PERMISSION_GRANTED) {

                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                        permission)) {
                    showRationaleDialog();
                }
                else {
                    ActivityCompat.requestPermissions(activity,new String[]{permission},111);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
                onRequestSuccessListener.onRequestSuccess();
            }
        }
        else{
            if (ContextCompat.checkSelfPermission(fragment.getContext(),permission)
                    != PackageManager.PERMISSION_GRANTED) {

                // Permission is not granted
                // Should we show an explanation?
                if (fragment.shouldShowRequestPermissionRationale(permission)){
                    showRationaleDialog();
                }
                else {
                    fragment.requestPermissions(new String[]{permission},111);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
                onRequestSuccessListener.onRequestSuccess();
            }
        }
    }


    private void showRationaleDialog() {
        new AlertDialog.Builder(activity)
                .setTitle(rationaleTitle)
                .setMessage(rationaleBody)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(activity,
                                new String[]{permission},111);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public void onRequestResult(int requestCode,int[] grantResults){
        if (this.activity!=null){
            if (requestCode==111){
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onRequestSuccessListener.onRequestSuccess();
                }
                else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                            permission)) {
                        showRationaleDialog();

                    } else {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                        intent.setData(uri);
                        activity.startActivityForResult(intent, 23);
                    }

                }
            }
        }

        else{
            if (requestCode==111){
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onRequestSuccessListener.onRequestSuccess();
                } else {
                    if (fragment.shouldShowRequestPermissionRationale(permission)) {
                        showRationaleDialog();

                    } else {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", fragment.getActivity().getPackageName(), null);
                        intent.setData(uri);
                        fragment.startActivityForResult(intent, 23);
                    }

                }
            }
        }
    }

    public interface OnRequestSuccessListener{
        void onRequestSuccess();
    }
}
