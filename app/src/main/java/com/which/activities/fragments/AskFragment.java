package com.which.activities.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.which.utils.helper.GetAsksHelper;
import com.which.utils.resources.AskEntity;
import com.which.utils.resources.Token;

public class AskFragment extends Fragment {
    private View mAskView;
    private TextView askText;
    private ImageButton leftImage;
    private Button leftButton;
    private ImageButton rightImage;
    private Button rightButton;

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
        leftImage = (ImageButton) mAskView.findViewById(R.id.left_image_button);
        leftButton = (Button) mAskView.findViewById(R.id.left_button);
        rightImage = (ImageButton) mAskView.findViewById(R.id.right_image_button);
        rightButton = (Button) mAskView.findViewById(R.id.right_button);

        (new GetAskAsyncTask(getActivity(), this)).execute();

        return mAskView;
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
            fragment.askText.setText(askEntity.getText());

            if (askEntity.getLeft().getType().equals("txt")) {
                fragment.leftButton.setText(askEntity.getLeft().getValue());
                fragment.leftButton.setVisibility(View.VISIBLE);
            }

            if (askEntity.getRight().getType().equals("txt")) {
                fragment.rightButton.setText(askEntity.getRight().getValue());
                fragment.rightButton.setVisibility(View.VISIBLE);
            }
//            leftImage.setImageBitmap(ImageHelper.base64ToBitmap(askEntity.getLeft().getValue()));
//            rightImage.setImageBitmap(ImageHelper.base64ToBitmap(askEntity.getLeft().getValue()));

        }
    }
}
