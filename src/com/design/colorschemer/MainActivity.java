/*
 * Part of this code was borrowed from the Collage Project at the MMC
 * PHP/Android group. I wrote the code however, so it's ok
 */

package com.design.colorschemer;

import java.io.FileNotFoundException;

import javax.crypto.spec.IvParameterSpec;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity implements OnClickListener {

    // boolean isViewHidden = false;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_FILE = 2;
    int startUp = 0;

    Bitmap userPhoto;
    Button getImage;
    ImageView userPicture;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get the engines started
        init();
    }

    public void init() {
        // Button that will start the camera
        getImage = (Button) findViewById(R.id.bGetPicture);
        getImage.setOnClickListener(this);

        // Put a place for our picture
        userPicture = (ImageView) findViewById(R.id.ivPicture);
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
            selectPhotoOption();
            break;
        }
    }

    // This gives the user the option of selecting to use a camera or to grab it
    // from there phone
    public void selectPhotoOption() {
        // TODO Auto-generated method stub
        final String[] items = new String[] { "Take from camera",
                "Select from gallery" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.select_dialog_item, items);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                // Get picture from camera
                if (item == 0) {
                    Intent intent = new Intent(
                            android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, PICK_FROM_CAMERA);
                }

                else { // pick from file
                    Intent intent = new Intent();

                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);

                    startActivityForResult(Intent.createChooser(intent,
                            "Complete action using"), PICK_FROM_FILE);
                }
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            switch (requestCode) {
            case PICK_FROM_CAMERA:
                userPhoto = (Bitmap) data.getExtras().get("data");
                userPicture.setImageBitmap(userPhoto);
                break;

            case PICK_FROM_FILE:

                // This gets the path data from the data given from the Uri and
                // then grabs the image and sets it to the Bitmap variable
                Uri targetUri = data.getData();

                try {
                    userPhoto = BitmapFactory.decodeStream(getContentResolver()
                            .openInputStream(targetUri));
                    userPicture.setImageBitmap(userPhoto);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            }
        }

    }
}
