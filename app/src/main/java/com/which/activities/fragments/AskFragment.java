package com.which.activities.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.which.R;
import com.which.data.dao.AskDao;
import com.which.data.dao.UserDao;
import com.which.data.entitties.User;
import com.which.utils.AskAPI;
import com.which.utils.ServerConnection;
import com.which.utils.helper.GetAsksHelper;
import com.which.utils.helper.ImageHelper;
import com.which.utils.resources.AskEntity;
import com.which.utils.resources.RequestAnswer;
import com.which.utils.resources.Token;

import java.io.IOException;

import retrofit2.Response;

public class AskFragment extends Fragment {
    private static final String LOG_TAG = AskFragment.class.getSimpleName();

    private View mAskView;
    private TextView askText;
    private ImageButton leftImage;
    private Button leftButton;
    private ImageButton rightImage;
    private Button rightButton;
    private Button skipButton;

    private int askID;

    public AskFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mAskView = inflater.inflate(R.layout.ask_item_layout, container, false);

        askText = (TextView) mAskView.findViewById(R.id.ask_text_view);

        // Left Buttons

        leftImage = (ImageButton) mAskView.findViewById(R.id.left_image_button);
        leftButton = (Button) mAskView.findViewById(R.id.left_button);

        AnswerOnClickListener leftListener = new AnswerOnClickListener("left", getActivity(), this);

        leftImage.setOnClickListener(leftListener);
        leftButton.setOnClickListener(leftListener);

        // Right Buttons

        rightImage = (ImageButton) mAskView.findViewById(R.id.right_image_button);
        rightButton = (Button) mAskView.findViewById(R.id.right_button);

        AnswerOnClickListener rightListener = new AnswerOnClickListener("right", getActivity(), this);

        rightImage.setOnClickListener(rightListener);
        rightButton.setOnClickListener(rightListener);

        skipButton = (Button) mAskView.findViewById(R.id.skip_button);
        skipButton.setOnClickListener(new AnswerOnClickListener("skip", getActivity(), this));

        (new GetAskAsyncTask(getActivity(), this)).execute();

        return mAskView;
    }

    private class AnswerOnClickListener implements View.OnClickListener {
        private String direction;
        private Context context;
        private AskFragment askFragment;

        private AnswerOnClickListener(String direction, Context context, AskFragment askFragment) {
            this.direction = direction;
            this.context = context;
            this.askFragment = askFragment;
        }

        @Override
        public void onClick(View view) {
            (new SendAnswerAsyncTask(askFragment, context, direction)).execute();
        }
    }

    private class SendAnswerAsyncTask extends AsyncTask<Void, Void, Void> {

        private AskFragment askFragment;
        private Context context;
        private String direction;

        private SendAnswerAsyncTask(AskFragment askFragment, Context context, String direction) {
            this.askFragment = askFragment;
            this.context = context;
            this.direction = direction;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            User user = UserDao.getCurrentUser(context);
            AskAPI api = ServerConnection.createAskAPI();

            try {
                Response<Response<Void>> response = api.doAnswer(new RequestAnswer(user.getAccess_token(), askFragment.askID, direction)).execute();

                if (response.isSuccessful()) {
                    Log.i(LOG_TAG, "Success answer");
                } else {
                    Log.i(LOG_TAG, "Fail answer");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            (new DeleteAsyncTask(context, askFragment.askID, askFragment)).execute();
        }
    }

    private class DeleteAsyncTask extends AsyncTask<Void, Void, Void> {
        private Context context;
        private int askID;
        private AskFragment askFragment;

        private DeleteAsyncTask(Context context, int askID, AskFragment askFragment) {
            this.context = context;
            this.askID = askID;
            this.askFragment = askFragment;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            AskDao.deleteById(context, askID);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            (new GetAskAsyncTask(context, askFragment)).execute();
        }
    }

    private class GetAskAsyncTask extends AsyncTask<Void, AskEntity, AskEntity> {
        private AskFragment fragment;
        private Context context;

        public GetAskAsyncTask(Context context, AskFragment askFragment) {
            this.context = context;
            this.fragment = askFragment;
        }

        @Override
        protected AskEntity doInBackground(Void... voids) {

            AskEntity ask = AskDao.getAsk(context);

            if (ask == null) {
                User user = UserDao.getCurrentUser(context);
                GetAsksHelper.getTasks(new Token(user.getAccess_token()), context);
            }

            ask = AskDao.getAsk(context);

            return ask;
        }

        @Override
        protected void onPostExecute(final AskEntity askEntity) {
            if (askEntity == null) {
                // TODO: Show No Questions
                return;
            }

            fragment.askID = askEntity.getAsk_id();

            fragment.askText.setText(askEntity.getText());

            if (askEntity.getLeft().getType().equals("txt")) {
                fragment.leftButton.setText(askEntity.getLeft().getValue());

                fragment.leftImage.setVisibility(View.GONE);
                fragment.leftButton.setVisibility(View.VISIBLE);
            } else {
                fragment.leftImage.setImageBitmap(ImageHelper.base64ToBitmap(askEntity.getLeft().getValue()));

                fragment.leftButton.setVisibility(View.GONE);
                fragment.leftImage.setVisibility(View.VISIBLE);
            }

            if (askEntity.getRight().getType().equals("txt")) {
                fragment.rightButton.setText(askEntity.getRight().getValue());

                fragment.rightImage.setVisibility(View.GONE);
                fragment.rightButton.setVisibility(View.VISIBLE);
            } else {
                fragment.rightImage.setImageBitmap(ImageHelper.base64ToBitmap(askEntity.getRight().getValue()));

                fragment.rightButton.setVisibility(View.GONE);
                fragment.rightImage.setVisibility(View.VISIBLE);
            }
        }
    }
}
