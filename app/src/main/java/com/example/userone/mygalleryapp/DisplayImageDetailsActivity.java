package com.example.userone.mygalleryapp;

import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Userone on 2/28/2017.
 */

public class DisplayImageDetailsActivity extends AppCompatActivity{
ImageView selectedImage;
    EditText commentsEditText,occationEditText;
    ImageView backArrowImageView;
    RelativeLayout ShareRelativeLayout,deleteRecordImageView;
    DatabaseHandler db;
    TextView wallpaperTextView;

    ImageView update1ImageView,update2ImageView;
    TextView commentsTextView,occationTextView,dateTextView,deletedAllTextView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_image_details_layout);
        selectedImage=(ImageView)findViewById(R.id.single_img);
        db=new DatabaseHandler(this);
        commentsEditText=(EditText)findViewById(R.id.comments_edittext);
        occationEditText=(EditText)findViewById(R.id.occation_type_edittext);
        commentsTextView=(TextView)findViewById(R.id.comments_textview);
        occationTextView=(TextView)findViewById(R.id.occation_type);
        deletedAllTextView=(TextView)findViewById(R.id.deletedall);
        ShareRelativeLayout=(RelativeLayout)findViewById(R.id.share);
        deleteRecordImageView=(RelativeLayout)findViewById(R.id.delete_record_rl);
        dateTextView=(TextView)findViewById(R.id.date);
        wallpaperTextView=(TextView)findViewById(R.id.wallpaper);
        update1ImageView=(ImageView)findViewById(R.id.update1) ;
        update2ImageView=(ImageView)findViewById(R.id.update2) ;
        selectedImage.setImageBitmap(GalleryView.selectedBitmapmage);
        backArrowImageView=(ImageView)findViewById(R.id.backarrow);
        commentsTextView.setText(GalleryView.selectedComments);
        occationTextView.setText(GalleryView.selectedOccation);
        dateTextView.setText(GalleryView.selectedDate);
        db.open();

        update1ImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (commentsTextView.getVisibility() == View.VISIBLE)
                {
                    update1ImageView.setImageResource(R.drawable.update_black);

                    commentsTextView.setVisibility(View.GONE);
                    commentsEditText.setVisibility(View.VISIBLE);
                    commentsEditText.setText(commentsTextView.getText().toString());
                }
                else{
                    update1ImageView.setImageResource(R.drawable.update_icons);
                    commentsTextView.setVisibility(View.VISIBLE);
                    commentsEditText.setVisibility(View.GONE);

                    if(!commentsEditText.getText().toString().equals(commentsTextView.getText().toString()))
                    {
                        commentsTextView.setText(commentsEditText.getText().toString());
                        boolean test=db.updateCommentsRecord(Integer.parseInt(GalleryView.selectedId),commentsEditText.getText().toString());
                        if(test)
                        {
                            Toast.makeText(getApplicationContext(),"Comment Updated successfully",Toast.LENGTH_SHORT).show();
                        }
                    }



                }
            }
        });
        update2ImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (occationTextView.getVisibility() == View.VISIBLE)
                {
                    update2ImageView.setImageResource(R.drawable.update_black);

                    occationTextView.setVisibility(View.GONE);
                    occationEditText.setVisibility(View.VISIBLE);
                    occationEditText.setText(occationTextView.getText().toString());
                }
                else{
                    update2ImageView.setImageResource(R.drawable.update_icons);
                    occationTextView.setVisibility(View.VISIBLE);
                    occationEditText.setVisibility(View.GONE);

                    if(!occationEditText.getText().toString().equals(occationTextView.getText().toString()))
                    {
                        occationTextView.setText(occationEditText.getText().toString());
                        boolean test=db.updateOccationRecord(Integer.parseInt(GalleryView.selectedId),occationEditText.getText().toString());
                        if(test)
                        {
                            Toast.makeText(getApplicationContext(),"Occation Updated successfully",Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            }
        });
        backArrowImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();


            }
        });
        wallpaperTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                WallpaperManager myWallpaperManager
                        = WallpaperManager.getInstance(getApplicationContext());
                try {
                    myWallpaperManager.setBitmap(GalleryView.selectedBitmapmage);
                    Toast.makeText(getApplicationContext(),"Wallpaper setted Successfully",Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            }
        });
        deleteRecordImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(DisplayImageDetailsActivity.this).create();

                alertDialog.setMessage("Are r want to delete this record perminantly");
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        long deleteCount=db.deleteRecord(Integer.parseInt(GalleryView.selectedId));
                        if(deleteCount==1)
                        {
                            Toast.makeText(getApplicationContext(),"Deleted  successfully",Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }
                    }
                });
                alertDialog.show();



            }
        });
        deletedAllTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(DisplayImageDetailsActivity.this).create();

                alertDialog.setMessage("Are r want to delete "+GalleryView.selectedOccation+"record perminantly");
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.open();
                        Log.d("data","--->"+GalleryView.selectedOccation);
                       long deletecount= db.deleteNameRecord(GalleryView.selectedOccation);
                         Toast.makeText(getApplicationContext(),deletecount+"Records deleted  successfully",Toast.LENGTH_SHORT).show();
                        onBackPressed();

                    }
                });
                alertDialog.show();

            }
        });
        ShareRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri bmpUri = getLocalBitmapUri(selectedImage);
                boolean networkStatus = isNetworkAvailable();
                if (networkStatus)
                {
                    if (bmpUri != null) {
                        // Construct a ShareIntent with link to image
                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                   /* shareIntent.putExtra(Intent.EXTRA_TEXT, selectedItemData[5] + "\n\n" + shareLink);*/
                        shareIntent.setType("image/*");

                        // Launch sharing dialog for image
                        startActivity(Intent.createChooser(shareIntent, "Share Image"));
                    } else {
                        Log.d("share exp", "---->sharing failed, handle error");
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"no Internet Connection",Toast.LENGTH_LONG).show();
                }



            }
        });
        selectedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(DisplayImageDetailsActivity.this).create();

                alertDialog.setMessage("Would u like save this image into Gallery");
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {


                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        MediaStore.Images.Media.insertImage(
                                getContentResolver(),
                                GalleryView.selectedBitmapmage,
                                "demo_image",
                                "demo_image"
                        );
                    }
                });
                alertDialog.show();
            }
        });
    }
    // Returns the URI path to the Bitmap displayed in specified ImageView
    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;


        if (drawable instanceof BitmapDrawable) {
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            // Use methods on Context to access package-specific directories on external storage.
            // This way, you don't need to request external read/write permission.
            // See https://youtu.be/5xVh-7ywKpE?t=25m25s
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 0, out);

            out.close();
            // **Warning:** This will fail for API >= 24, use a FileProvider as shown below instead.
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    //alert Dialog
    public void ShowAlert(String alertMessage) {
        AlertDialog alertDialog = new AlertDialog.Builder(DisplayImageDetailsActivity.this).create();

        alertDialog.setMessage(alertMessage);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.show();
    }
    @Override
    public void onBackPressed() {

       Intent intent= new Intent(getApplicationContext(),GalleryView.class);
        startActivity(intent);
        finish();
    }
}
