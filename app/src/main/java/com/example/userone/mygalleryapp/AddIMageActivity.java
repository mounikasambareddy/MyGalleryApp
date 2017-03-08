package com.example.userone.mygalleryapp;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.provider.CalendarContract.CalendarCache.URI;

/**
 * Created by Userone on 2/23/2017.
 */
public class AddIMageActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String picturePath;
    Bitmap selectedBitmap;
    EditText imageCommentsEditText, typeOfImageEditText;
    RecyclerView recycleView;
    DatabaseHandler db;
    TextView dataBaseCount;
    ImageView displayImageView;
    private final int GALLERY_ACTIVITY_CODE = 2;



    final int CAMERA_CAPTURE = 1;
    final int CROP_PIC = 3;
    LinearLayoutManager linearLayoutManger;
    ArrayList<String> imageList = new ArrayList<String>();
    ArrayList<Bitmap> imageList1 = new ArrayList<Bitmap>();
    List<Views> contactList = new ArrayList<Views>();
    ViewRelatedItemsAdapter adapter;
    Button sendButton, saveButton;
    ImageView backArrowImageView;
    Views contact1;
    private Uri picUri;
    public static List<Views> contactData = null;
    Uri URI;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addimage_layout);
        db = new DatabaseHandler(this);
        imageCommentsEditText = (EditText) findViewById(R.id.edittext_comments);
        dataBaseCount = (TextView) findViewById(R.id.db_count);
        typeOfImageEditText = (EditText) findViewById(R.id.image_type);
        recycleView = (RecyclerView) findViewById(R.id.img_recycleView);
        displayImageView = (ImageView) findViewById(R.id.img);
        sendButton = (Button) findViewById(R.id.send);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        backArrowImageView = (ImageView) findViewById(R.id.backarrow);
        saveButton = (Button) findViewById(R.id.save);
        displayImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final CharSequence[] items = {"Take Photo", "Choose from Gallery",
                        "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(AddIMageActivity.this);
                builder.setTitle("Add Photo!");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        boolean result = Utility.checkPermission(AddIMageActivity.this);
                        if (items[item].equals("Take Photo")) {
                            try {

                                //image from camera
                                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                String imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/picture.jpg";
                                File imageFile = new File(imageFilePath);
                                picUri = Uri.fromFile(imageFile); // convert path to Uri
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
                                startActivityForResult(takePictureIntent, CAMERA_CAPTURE);

                            } catch (ActivityNotFoundException anfe) {
                                //display an error message
                                String errorMessage = "Whoops - your device doesn't support capturing images!";
                                Toast.makeText(AddIMageActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        }
                        else if (items[item].equals("Choose from Gallery"))
                        {
                            // image from gallery
                            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            // Start the Intent
                            startActivityForResult(galleryIntent, GALLERY_ACTIVITY_CODE);
                        } else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();

            }
        });

        linearLayoutManger = new LinearLayoutManager(AddIMageActivity.this);
        recycleView.setLayoutManager(linearLayoutManger);
        adapter = new ViewRelatedItemsAdapter(this);
        spinner.setOnItemSelectedListener(this);
        if (GalleryView.spinnerData.size() > 0) {
            ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, GalleryView.spinnerData);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            spinner.setAdapter(aa);

        }


        backArrowImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();


            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


             /*   URI = Uri.parse(ImagePath[0]);*/
                if (selectedBitmap != null && !typeOfImageEditText.getText().toString().equals("")) {
                    imageList1.add(selectedBitmap);
                    imageList.add(imageCommentsEditText.getText().toString() + "-~-" + typeOfImageEditText.getText().toString());
                    adapter = new ViewRelatedItemsAdapter(AddIMageActivity.this);
                    recycleView.setAdapter(adapter);
                } else {
                    Toast.makeText(getApplicationContext(), "Empty Data", Toast.LENGTH_SHORT).show();
                }

            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.open();

                String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                Log.d("display date", "--->" + currentDateTimeString);
                if (imageList.size() != 0) {
                    for (int i = 0; i < imageList.size(); i++) {
                        String insertData[] = imageList.get(i).split("-~-");

                        contact1 = new Views(selectedBitmap, insertData[0], insertData[1].substring(0, 1).toUpperCase() + insertData[1].substring(1).toLowerCase(), currentDateTimeString);
                        db.insertEmpDetails(contact1);
                    }

                    db.close();
                    Intent intent = new Intent(getApplicationContext(), GalleryView.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Empty Data", Toast.LENGTH_SHORT).show();
                }

            }
        });
        db.open();
        contactData = db.retriveEmpDetails();
        db.close();

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("uriGallery1","--->"+requestCode);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_CAPTURE) {
                // get the Uri for the captured image


                performCrop();
            }
            if (requestCode == GALLERY_ACTIVITY_CODE) {


                picUri = data.getData();
                Log.d("uriGallery","--->"+picUri.toString());
                performCrop();

            }

            if (requestCode == CROP_PIC)
            {

                    Log.d("add image", "--->");
                    Bundle extras = data.getExtras();
                    selectedBitmap = (Bitmap)extras.get("data");
                    displayImageView.setImageBitmap(selectedBitmap);



            }

        }





    }

    private void performCrop() {
        // take care of exceptions
        try {
            // call the standard crop action intent (the user device may not
            // support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 2);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, CROP_PIC);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            Toast toast = Toast
                    .makeText(this, "This device doesn't support the crop action!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        String item = adapterView.getItemAtPosition(position).toString();
        int j = 0;
        if (!item.equals("All")) {
            typeOfImageEditText.setText(item);
            for (int i = 0; i < contactData.size(); i++) {

                if (item.equals(contactData.get(i).getImageType())) {

                    j++;


                }
            }
            dataBaseCount.setText(j + "");
        } else {
            dataBaseCount.setText(contactData.size() + "");
            typeOfImageEditText.setText("");
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    private class ViewRelatedItemsAdapter extends RecyclerView.Adapter<ViewRelatedItemsAdapter.MyViewHolder> {
        Context context;
        View v;

        public ViewRelatedItemsAdapter(Context context) {
            this.context = context;
        }


        @Override
        public ViewRelatedItemsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.list_row, parent, false);
            return new ViewRelatedItemsAdapter.MyViewHolder(v);
        }


        @Override
        public void onBindViewHolder(ViewRelatedItemsAdapter.MyViewHolder holder, final int position) {
            holder.setIsRecyclable(false);
            Log.d("image", "--->" + imageList1.get(position));
            holder.setIsRecyclable(false);
            String imageData[] = imageList.get(position).split("-~-");
            holder.commentsTextView.setText(imageData[0]);
            holder.img.setImageBitmap(imageList1.get(position));
            holder.typetextTextView.setText(imageData[1]);
            holder.deleteRecycleviewImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imageList1.remove(position);
                    imageList.remove(position);
                    notifyDataSetChanged();
                }
            });
        }


        @Override
        public int getItemCount() {

            return imageList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView commentsTextView, typetextTextView;
            public ImageView img, deleteRecycleviewImageView;

            public MyViewHolder(View itemView) {
                super(itemView);
                commentsTextView = (TextView) v.findViewById(R.id.comments);
                img = (ImageView) v.findViewById(R.id.img);
                typetextTextView = (TextView) v.findViewById(R.id.typetext);
                deleteRecycleviewImageView = (ImageView) v.findViewById(R.id.delete_recycleview);
            }
        }
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }
}
