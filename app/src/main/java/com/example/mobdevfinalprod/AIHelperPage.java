package com.example.mobdevfinalprod;

import static android.view.animation.Animation.ABSOLUTE;
import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.text.LineBreaker;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.constraintlayout.motion.widget.MotionScene;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ai.client.generativeai.BuildConfig;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import org.w3c.dom.Text;

import java.util.concurrent.Executor;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AIHelperPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AIHelperPage extends Fragment {
    private static String API_KEY = "AIzaSyAl5AKJTlEnB1iqdLg9GonEVAKSWXIGZg4";
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

        ImageButton send_button = view.findViewById(R.id.submit_ai_message);
        LinearLayout conversation = view.findViewById(R.id.chat_container);
        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm =getSystemService(v.getContext(), InputMethodManager.class);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                String question = ((EditText) view.findViewById(R.id.ai_message_request)).getText().toString();
                ((EditText)view.findViewById(R.id.ai_message_request)).setText("");
                if(question.length() == 0) {
                    Toast.makeText(v.getContext(), "Please enter a question", Toast.LENGTH_LONG).show();
                }
                else {
                    ReportPage.scrollDown(view.findViewById(R.id.scrollview_ai_helper));
                    addUserQuestion(question,conversation,view);
                    addAIResponse(question,conversation,view);
                }
            }
        });
        return view;
    }
    private void addUserQuestion(String question, LinearLayout conversation,View v) {
        TextView useQuestion = new TextView(v.getContext());
        useQuestion.setPadding(20, 20, 20, 20);
        useQuestion.setTextColor(Color.BLACK);
        useQuestion.setTextSize(12);
        useQuestion.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        Typeface customTypeface = ResourcesCompat.getFont(v.getContext(), R.font.roboto);
        useQuestion.setTypeface(customTypeface);
        useQuestion.setText(question);
        useQuestion.setBackgroundResource(R.drawable.user_question_backround);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.topMargin = 20;
        layoutParams.bottomMargin = 20;
        layoutParams.gravity = Gravity.END;
        useQuestion.setLayoutParams(layoutParams);

        conversation.addView(useQuestion);
    }
    private void addAIResponse(String question, LinearLayout conversation, View v) {
        Typeface customTypeface = ResourcesCompat.getFont(v.getContext(), R.font.roboto);
        ProgressBar initial = new ProgressBar(getContext());
        ColorStateList colorStateList = ColorStateList.valueOf(Color.RED); // Example color
        initial.setIndeterminateTintList(colorStateList);
        ImageView aiCharacter = new ImageView(v.getContext());
        aiCharacter.setBackgroundResource(R.drawable.aihelper);
        float scale = v.getContext().getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (40 * scale + 0.5f);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dpAsPixels, dpAsPixels);
        aiCharacter.setLayoutParams(layoutParams);
        conversation.addView(aiCharacter);
        conversation.addView(initial);
        getAIResponse(question, new ResponseCallback() {
            @Override
            public void onResponse(String response) {

                TextView aiResponse = new TextView(v.getContext());
                aiResponse.setText(response.replace("*",""));
                aiResponse.setPadding(20, 20, 20, 20);
                aiResponse.setTextColor(Color.BLACK);
                aiResponse.setTextSize(12);
                aiResponse.setTypeface(customTypeface);

                Animation slideAnimation = new TranslateAnimation(-1000f,0f,0,0);
                slideAnimation.setDuration(800);
                Animation fadeAnimation = new AlphaAnimation(0,1);
                fadeAnimation.setDuration(800);

                // Add the new TextView to the conversation
                getActivity().runOnUiThread(()->{
                    conversation.removeView(initial);
                    aiResponse.startAnimation(slideAnimation);
                    aiResponse.startAnimation(fadeAnimation);
                    conversation.addView(aiResponse);
                    ReportPage.scrollDown(v.findViewById(R.id.scrollview_ai_helper));
                });
            }
        });
    }
    protected static void getAIResponse(String prompt, ResponseCallback callback) {
        GenerativeModel gm = new GenerativeModel("gemini-pro",API_KEY);
        GenerativeModelFutures gmf = GenerativeModelFutures.from(gm);

        Content content =  new Content.Builder().addText(prompt).build();
        Executor executor = AsyncTask.SERIAL_EXECUTOR;
        ListenableFuture<GenerateContentResponse> response = gmf.generateContent(content);
        final String[] answer = {null};
        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                callback.onResponse(result.getText());
            }

            @Override
            public void onFailure(Throwable t) {
                callback.onResponse("Error" + t.getMessage());
            }
        },executor);
    }
    interface ResponseCallback {
        void onResponse(String response);
    }
}