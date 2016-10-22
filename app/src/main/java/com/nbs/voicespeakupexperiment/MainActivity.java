package com.nbs.voicespeakupexperiment;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity
        implements TextToSpeech.OnInitListener,
        TranslateRequest.OnTranslateRequestListener {

    @Bind(R.id.txtSpeechInput)
    TextView txtSpeechInput;
    @Bind(R.id.btnSpeak)
    ImageButton btnSpeak;
    @Bind(R.id.btnTranslate)
    ImageButton btnTranslate;
    @Bind(R.id.ln_translate)
    LinearLayout lnTranslate;
    @Bind(R.id.txtTranslatedText)
    TextView txtTranslatedText;
    private ProgressDialog mProgressDialog;

    private final int REQ_CODE_SPEECH_INPUT = 100;
    private TextToSpeech textToSpeech;
    private TranslateRequest translateRequest;
    private String mTranslatedText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        getSupportActionBar().hide();

        translateRequest = new TranslateRequest();
        translateRequest.setOnTranslateRequestListener(this);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please wait...");


        textToSpeech = new TextToSpeech(this, this);

    }

    @OnClick(R.id.btnSpeak)
    public void onBtnSpeakClicked() {
        txtSpeechInput.setText("");
        txtTranslatedText.setText("");
        lnTranslate.setVisibility(View.GONE);
        promptSpeechInput();
    }

    @OnClick(R.id.btnTranslate)
    public void onBtnTranslateClicked() {
        translate();
    }

    private void translate() {
        String text = txtSpeechInput.getText().toString();
        mProgressDialog.show();
        translateRequest.callApi(text);
    }

    /**
     * Showing google speech input dialog
     */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtSpeechInput.setText("\"" + result.get(0) + "\"");
                    lnTranslate.setVisibility(View.VISIBLE);
                }
                break;
            }

        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.FRENCH);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                btnTranslate.setEnabled(true);
                speakOut();
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }

    private void speakOut() {
        if (!TextUtils.isEmpty(mTranslatedText)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                textToSpeech.speak(mTranslatedText, TextToSpeech.QUEUE_FLUSH, null, null);
            } else {
                textToSpeech.speak(mTranslatedText, TextToSpeech.QUEUE_FLUSH, null);
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onTranslateSuccess(String translatedText) {
        mProgressDialog.cancel();
        try {
            mTranslatedText = Html.fromHtml(translatedText).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        txtTranslatedText.setText(mTranslatedText);
        speakOut();
    }

    @Override
    public void onTranslateFailed(String errorMessage) {
        mProgressDialog.cancel();
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }
}
