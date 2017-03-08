package com.example.userone.mygalleryapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GalleryView extends Activity implements AdapterView.OnItemSelectedListener {
    DatabaseHandler db;
    CardView cardView;
    TextView commentsTextView;
   public static  String selectedDate,selectedComments,selectedOccation,selectedId;
    public static  ArrayList<String> spinnerData = new ArrayList<String>();
public static Bitmap selectedBitmapmage;
    public static List<Views> contactData = null;
    List<Views> contactList= new ArrayList<Views>();
    Integer[] pics = {
            R.drawable.antartica1,
            R.drawable.antartica2,
            R.drawable.antartica3,
            R.drawable.antartica4,
            R.drawable.antartica5,
            R.drawable.antartica6,
            R.drawable.antartica7,
            R.drawable.antartica8,
            R.drawable.antartica9,
            R.drawable.antartica10
    };
    ImageView imageView;
    Button addImageButton;
    Gallery ga;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseHandler(this);
     ga = (Gallery) findViewById(R.id.Gallery01);
        cardView = (CardView) findViewById(R.id.card_view2);
        commentsTextView = (TextView) findViewById(R.id.commets_textview);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        db.open();
        contactData = db.retriveEmpDetails();
        db.close();
        spinnerData.clear();
        for (int i = 0; i < contactData.size(); i++) {

            spinnerData.add(contactData.get(i).getImageType());
            Views contactdata1 = new Views();
            contactdata1.setImageType(contactData.get(i).getImageType());
            contactdata1.setComments(contactData.get(i).getComments());

            contactdata1.setImageBytes(contactData.get(i).getImageBytes());
            contactList.add(contactdata1);
            Log.d("spinner data","--->"+contactData.get(i).getImageType()+"..."+contactData.get(i).getComments());

        }
        //avoid dulplicates in arraylist

// add elements to al, including duplicates
        Set<String> hs = new HashSet<>();
        hs.addAll(spinnerData);
        spinnerData.clear();
        spinnerData.add("All");
        spinnerData.addAll(hs);

        if(spinnerData.size()>0)
        {
            ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,spinnerData);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            spinner.setAdapter(aa);

        }
        ga.setAdapter(new ImageAdapter(this));




        imageView = (ImageView) findViewById(R.id.ImageView01);
        addImageButton = (Button) findViewById(R.id.add_image);
        ga.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Toast.makeText(getBaseContext(),
                        "You have selected picture " + (arg2 + 1) + " of Antartica",
                        Toast.LENGTH_SHORT).show();
                selectedBitmapmage=contactData.get(arg2).getImageBytes();
                selectedDate=contactData.get(arg2).getDateString();
                selectedComments=contactData.get(arg2).getComments();
                selectedOccation=contactData.get(arg2).getImageType();
                selectedId=contactData.get(arg2).get_id()+"";
                imageView.setImageBitmap(contactData.get(arg2).getImageBytes());
                commentsTextView.setText(contactData.get(arg2).getComments());
              /*  imageView.setImageResource(pics[arg2]);*/


            }


        });
        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(GalleryView.this, AddIMageActivity.class);
                startActivity(intent);
                finish();
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(contactData.size()>0)
                {
                    Intent intent= new Intent(GalleryView.this,DisplayImageDetailsActivity.class);

                    startActivity(intent);
                    finish();
                }

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        String item = adapterView.getItemAtPosition(position).toString();
        contactList.clear();
        Log.d("item","--->"+item);
        for (int i = 0; i < contactData.size(); i++)
        {
            Views contactdata1 = new Views();
            if(item.equals(contactData.get(i).getImageType()))
            {


                contactdata1.setImageType(contactData.get(i).getImageType());
                contactdata1.setComments(contactData.get(i).getComments());

                contactdata1.setImageBytes(contactData.get(i).getImageBytes());

                contactList.add(contactdata1);
                Log.d("item1","--->"+contactData.get(i).getImageType()+"..."+contactData.get(i).getComments());
             }
            else if(item.equals("All"))
            {

                contactdata1.setImageType(contactData.get(i).getImageType());
                contactdata1.setComments(contactData.get(i).getComments());

                contactdata1.setImageBytes(contactData.get(i).getImageBytes());
                contactList.add(contactdata1);
            }
            ga.setAdapter(new ImageAdapter(this));


        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    public class ImageAdapter extends BaseAdapter {

        private Context ctx;
        int imageBackground;

        public ImageAdapter(Context c) {
            Log.d("adapter", "--->");
            ctx = c;
            TypedArray ta = obtainStyledAttributes(R.styleable.Gallery1);
            imageBackground = ta.getResourceId(R.styleable.Gallery1_android_galleryItemBackground, 1);
            ta.recycle();
        }

        @Override
        public int getCount() {

            return contactList.size();
           /* return pics.length;*/
        }

        @Override
        public Object getItem(int arg0) {

            return arg0;
        }

        @Override
        public long getItemId(int arg0) {

            return arg0;
        }

        @Override
        public View getView(int position, View arg1, ViewGroup arg2) {

            if (position == 0) {
                selectedDate=contactData.get(position).getDateString();
                selectedComments=contactData.get(position).getComments();
                selectedOccation=contactData.get(position).getImageType();
                selectedId=contactData.get(position).get_id()+"";
                selectedBitmapmage=contactData.get(position).getImageBytes();
                imageView.setImageBitmap(contactList.get(position).getImageBytes());
                commentsTextView.setText(contactList.get(position).getComments());
            }
            ImageView iv = new ImageView(ctx);
            iv.setImageBitmap(contactList.get(position).getImageBytes());
         /*   iv.setImageResource(pics[position]);*/

         /*   iv.setImageResource(pics[position]);*/
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            iv.setLayoutParams(new Gallery.LayoutParams(350, 270));
            iv.setBackgroundResource(imageBackground);


            return iv;
        }

    }
}