package com.example.mobdevfinalprod;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.ai.client.generativeai.BuildConfig;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.Executor;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AIHelperPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AIHelperPage extends Fragment {
    private String API_KEY = "AIzaSyAl5AKJTlEnB1iqdLg9GonEVAKSWXIGZg4";
    TextView ai_response;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AIHelperPage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WorkOutAIPage.
     */
    // TODO: Rename and change types and number of parameters
    public static AIHelperPage newInstance(String param1, String param2) {
        AIHelperPage fragment = new AIHelperPage();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_work_out_a_i_page, container, false);
        view.findViewById(R.id.nd_container).setVisibility(View.INVISIBLE);
        ai_response = view.findViewById(R.id.ai_response);

        GenerativeModel gm = new GenerativeModel("gemini-pro",API_KEY);
        GenerativeModelFutures model = GenerativeModelFutures.from(gm);

        view.findViewById(R.id.submit_ai_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.findViewById(R.id.nd_container).setVisibility(View.VISIBLE);

                String question = ((EditText)view.findViewById(R.id.ai_message_request)).getText().toString();
                if(!question.isEmpty()) {
                    Content content = new Content.Builder().addText("in short sentences only. DO NOT ANSWER IF IT IS NOT HEALTH AND FITNESS RELATED" + question).build();

                    Executor executor = AsyncTask.SERIAL_EXECUTOR;

                    ListenableFuture<GenerateContentResponse> response = model.generateContent(content);
                    Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
                        @Override
                        public void onSuccess(GenerateContentResponse result) {
                            getActivity().runOnUiThread(()->{
                                ai_response.setText(result.getText());
                            });
                        }
                        @Override
                        public void onFailure(Throwable t) {
                            getActivity().runOnUiThread(()->{
                                ai_response.setText(t.toString());
                            });
                        }
                    },executor);
                }
            }
        });

        return view;
    }
}