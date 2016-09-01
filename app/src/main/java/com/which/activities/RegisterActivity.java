package com.which.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.which.R;
import com.which.utils.IdentityAPI;
import com.which.utils.ServerConnection;
import com.which.utils.resources.LoginResponse;
import com.which.utils.resources.RegisterData;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by tomeramir on 31/08/2016.
 */
public class RegisterActivity extends AppCompatActivity {

    private static final String LOG_TAG = RegisterActivity.class.getSimpleName();
    private ImageView mPictureView;
    private Bitmap bitmap = null;
    private String encodedImage = null;
    private EditText mUsername;
    private EditText mPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mPictureView = (ImageView) findViewById(R.id.profile_picture_view);
        mUsername = (EditText) findViewById(R.id.register_username_input);
        mPassword = (EditText) findViewById(R.id.register_password_input);

        ImageButton mCameraButton = (ImageButton) findViewById(R.id.take_picture_button);
        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, 1);
                }
            }
        });

        Button mGalleryButton = (Button) findViewById(R.id.gallery_picture_button);
        mGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);
            }
        });

        Button mRegisterButton = (Button) findViewById(R.id.register_action_button);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();
                validateAndStart(username, password);
            }
        });
    }

    private void validateAndStart(String username, String password) {
        if (encodedImage == null) {
            // TODO: Validate for, and put default profile pic
//            return;
        }

        RegisterAsyncTask uploadBitmapAsycTask =
                new RegisterAsyncTask(this, username, password, encodedImage);

        uploadBitmapAsycTask.execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        bitmap = null;

        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            mPictureView.setImageBitmap(bitmap);
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                mPictureView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (bitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
            byte[] byteArrayImage = baos.toByteArray();

            encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);


        }
    }

    private void base64ToBitmap(String bitmap) {
        byte[] imageAsBytes = Base64.decode(bitmap.getBytes(), Base64.DEFAULT);
        mPictureView.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
    }

    private class RegisterAsyncTask extends AsyncTask<Void, Void, String> {
        private final Context mContext;
        private final String profile_picture;

        private RegisterData register;

        public RegisterAsyncTask(Context context, String username, String password, String profile_picture) {
            this.mContext = context;
            this.profile_picture = profile_picture;

            register = new RegisterData();
            register.setUsername(username);
            register.setPassword(password);
            register.setProfile_picture(profile_picture);
        }

        @Override
        protected String doInBackground(Void... voids) {
            IdentityAPI identityAPI = ServerConnection.createIdentityAPI();

            Call<LoginResponse> call = identityAPI.doRegister(register);

            try {
                Response<LoginResponse> response = call.execute();

                if (response.isSuccessful()) {
                    // TODO: persist in db
                    Log.i(LOG_TAG, "Registration success full, token is: " + response.body().getAccess_token());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String res) {
//            ((RegisterActivity) mContext).base64ToBitmap(res);
            Intent intent = new Intent(mContext, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
