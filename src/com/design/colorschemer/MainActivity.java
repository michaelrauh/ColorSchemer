/*
 * Part of this code was borrowed from the Collage Project at the MMC
 * PHP/Android group. I wrote the code however, so it's ok
 */

package com.design.colorschemer;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity implements OnClickListener {

    private static final int PICK_FROM_CAMERA = 1;

    int startUp = 0;

    Bitmap userPhoto;
    Button getImage;
    ImageView userPicture, userComplement;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // get the engines started
        init();

        // Start the camera right when the activity starts
        startCamera();

    }

    public void init() {

        // Button that will start the camera
        getImage = (Button) findViewById(R.id.bGetPicture);
        getImage.setOnClickListener(this);

        // Put a place for our picture
        userPicture = (ImageView) findViewById(R.id.ivPicture);
        userComplement = (ImageView) findViewById(R.id.ivComplement);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    public void onClick(View view) {
        // TODO Auto-generated method stub

        // If we have more buttons they'll go here
        switch (view.getId()) {
        case R.id.bGetPicture:
            startCamera();
            break;
        }
    }

    // Starts the camera everytime you run this function
    public void startCamera() {
        // TODO Auto-generated method stub

        Intent intent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, PICK_FROM_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // I'll leave the case here incase we add more features
            switch (requestCode) {
            case PICK_FROM_CAMERA:

                userPhoto = (Bitmap) data.getExtras().get("data");

                // making an arraylist of each of the colors
                ArrayList<triColor> colors = new ArrayList<triColor>();

                int[] pixels = new int[userPhoto.getWidth()
                        * userPhoto.getHeight()];

                // load from bitmap to pixel array
                userPhoto.getPixels(pixels, 0, userPhoto.getWidth(), 0, 0,
                        userPhoto.getWidth(), userPhoto.getHeight());

                // for each pixel, parse into custom RGB class, add to the
                // arraylist
                for (int i = 0; i < pixels.length; i++) {
                    triColor temp = new triColor();

                    temp.R = Color.red(pixels[i]);
                    temp.G = Color.green(pixels[i]);
                    temp.B = Color.blue(pixels[i]);

                    colors.add(temp);
                }

                // Here is the median value, in the form of a triColor class
                // object
                // triColor average = colors.get(colors.size() / 2);

                int totalR = 0;
                int totalG = 0;
                int totalB = 0;

                for (int i = 0; i < colors.size(); i++) {

                    totalR += colors.get(i).R;
                    totalG += colors.get(i).G;
                    totalB += colors.get(i).B;

                }

                int averageColor = Color.rgb(totalR / colors.size(), totalG
                        / colors.size(), totalB / colors.size());
                int[] averageColorList = new int[10000];
                for (int i = 0; i < 10000; i++) {
                    averageColorList[i] = averageColor;
                }

                // Create the new bitmap of the average color
                Bitmap averageColorPicture = Bitmap.createBitmap(
                        averageColorList, 100, 100, userPhoto.getConfig());

                // Set it to the picture displayed
                userPicture.setImageBitmap(averageColorPicture);

                /*
                 * white is 255,255,255 so the complementary color is going to
                 * be 255-R,255-G,255-B
                 */

                triColor complement = new triColor();

                complement.R = 255 - totalR / colors.size();
                complement.G = 255 - totalG / colors.size();
                complement.B = totalB / colors.size();

                int complementColor = Color.rgb(complement.R, complement.G,
                        complement.B);

                int[] complementColorList = new int[10000];
                for (int i = 0; i < 10000; i++) {
                    complementColorList[i] = complementColor;
                }

                Bitmap complementColorPicture = Bitmap.createBitmap(
                        complementColorList, 100, 100, userPhoto.getConfig());

                userComplement.setImageBitmap(complementColorPicture);

                break;
            }
        }
    }
}
