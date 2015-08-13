package com.codepath.apps.cloetweet.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.codepath.apps.cloetweet.R;

public class PostDialog extends DialogFragment {

    private EditText etPostBody;
    private Button btnPost;

    public PostDialog() {
        // Required empty public constructor
    }

    public interface PostDialogListener {
        void onFinishPostDialog(String body);
    }

    public static PostDialog newInstance(String title) {
        PostDialog frag = new PostDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_dialog, container);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        etPostBody = (EditText) view.findViewById(R.id.etPostBody);
        btnPost = (Button) view.findViewById(R.id.btnPost);
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostDialogListener listener = (PostDialogListener) getActivity();
                listener.onFinishPostDialog(etPostBody.getText().toString());
                dismiss();
            }
        });

        return view;
    }

}