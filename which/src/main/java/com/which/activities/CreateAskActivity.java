package com.which.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.which.R;
import com.which.data.dao.UserDao;
import com.which.data.entitties.User;
import com.which.utils.AskAPI;
import com.which.utils.ServerConnection;
import com.which.utils.helper.ImageHelper;
import com.which.utils.resources.Answer;
import com.which.utils.resources.AskRequest;
import com.which.utils.resources.AskResponse;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by tomeramir on 02/09/2016.
 */
public class CreateAskActivity extends AppCompatActivity {
    private static final String LOG_TAG = CreateAskActivity.class.getSimpleName();

    private static final int CAMERA_CALLBACK = 1;
    private static final int GALLERY_CALLBACK = 2;

    private int currCallback;

    private Bitmap bitmap;

    private EditText mQuestion;

    private ImageView currImage;

    private ImageView mPictureLeft;
    private ImageView mPictureRight;

    private EditText currText;

    private EditText mTextLeft;
    private EditText mTextRight;

    private ImageButton mGalleryLeft;
    private ImageButton mGalleryRight;

    private ImageButton mCameraLeft;
    private ImageButton mCameraRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.acitivity_create_ask);

        mQuestion = (EditText) findViewById(R.id.question_edit_text);

        Button submitAsk = (Button) findViewById(R.id.submit_ask);
        submitAsk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doCreateAsk();
            }
        });

        mPictureLeft = (ImageView) findViewById(R.id.left_image_answer);
        mPictureRight = (ImageView) findViewById(R.id.right_image_answer);

        mTextLeft = (EditText) findViewById(R.id.left_text_answer);
        mTextRight = (EditText) findViewById(R.id.right_text_answer);

        mGalleryLeft = (ImageButton) findViewById(R.id.left_gallery_button);
        mGalleryRight = (ImageButton) findViewById(R.id.right_gallery_button);

        mGalleryLeft.setOnClickListener(new AddPickGalleryCallback(mPictureLeft, mTextLeft, GALLERY_CALLBACK));
        mGalleryRight.setOnClickListener(new AddPickGalleryCallback(mPictureRight, mTextRight, GALLERY_CALLBACK));

        mCameraLeft = (ImageButton) findViewById(R.id.left_camera_button);
        mCameraRight = (ImageButton) findViewById(R.id.right_camera_button);

        mCameraLeft.setOnClickListener(new AddPickGalleryCallback(mPictureLeft, mTextLeft, CAMERA_CALLBACK));
        mCameraRight.setOnClickListener(new AddPickGalleryCallback(mPictureRight, mTextRight, CAMERA_CALLBACK));
    }


    private void doCreateAsk() {
        AskRequest ask = new AskRequest();

        ask.setType("default");
        ask.setText(mQuestion.getText().toString());

        Answer left = new Answer();

        if (mPictureLeft.getVisibility() == View.VISIBLE) {
            left.setType("img");
            left.setValue(ImageHelper.bitmapToBase64(((BitmapDrawable) mPictureLeft.getDrawable()).getBitmap()));
        } else {
            left.setType("txt");
            left.setValue(mTextLeft.getText().toString());
        }

        ask.setLeft(left);

        Answer right = new Answer();

        if (mPictureRight.getVisibility() == View.VISIBLE) {
            right.setType("img");
            right.setValue(ImageHelper.bitmapToBase64(((BitmapDrawable) mPictureRight.getDrawable()).getBitmap()));
        } else {
            right.setType("txt");
            right.setValue(mTextRight.getText().toString());
        }

        ask.setRight(right);

        (new CreateAskAsyncTask(this, ask)).execute();
    }

    private class AddPickGalleryCallback implements View.OnClickListener {

        private final ImageView sentImage;
        private final EditText sentText;
        private final int sentCallback;

        private AddPickGalleryCallback(ImageView sentImage, EditText sentText, int sentCallback) {
            this.sentImage = sentImage;
            this.sentText = sentText;
            this.sentCallback = sentCallback;
        }

        @Override
        public void onClick(View view) {
            currImage = sentImage;
            currText = sentText;
            currCallback = sentCallback;

            if (sentCallback == GALLERY_CALLBACK) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, GALLERY_CALLBACK);
            } else {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, 1);
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        bitmap = null;

        if (requestCode == CAMERA_CALLBACK && resultCode == RESULT_OK) {

            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            currImage.setImageBitmap(bitmap);

        } else if (requestCode == GALLERY_CALLBACK && resultCode == RESULT_OK) {

            Uri selectedImage = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                currImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (bitmap != null) {
            currText.setVisibility(View.GONE);
            currImage.setVisibility(View.VISIBLE);
        }
    }

    private static class CreateAskAsyncTask extends AsyncTask<Void, Void, Void> {

        private CreateAskActivity context;
        private AskRequest ask;

        public CreateAskAsyncTask(CreateAskActivity context, AskRequest ask) {
            this.context = context;
            this.ask = ask;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            User user = UserDao.getCurrentUser(context);

            ask.setAccess_token(user.getAccess_token());

            AskAPI api = ServerConnection.createAskAPI();

            Call<AskResponse> call = api.doPostAsk(ask);

            try {
                Response<AskResponse> response = call.execute();

                if (response.isSuccessful()) {
                    Log.i(LOG_TAG, "Success " + response.body().getAsk_id());
                } else {
                    Log.i(LOG_TAG, "Error");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            context.finish();
        }
    }
}
