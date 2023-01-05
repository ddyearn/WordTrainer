package ddwucom.mobile.ma02_20200937;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AddActivity extends AppCompatActivity {

    private static final String TAG = "AddActivity";

    EditText etName;
    EditText etMeaning;

    WordDBManager wordDBManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        etName = findViewById(R.id.etName);
        etMeaning = findViewById(R.id.etMeaning);

        wordDBManager = new WordDBManager(this);

        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = etName.getText().toString();
                new Thread(){
                    @Override
                    public void run() {
                        Papago papago = new Papago();
                        String resultWord;
                        resultWord= papago.getTranslation(text,"en","ko");

                        Bundle papagoBundle = new Bundle();
                        papagoBundle.putString("resultWord",resultWord);

                        Log.d(TAG, "resultWord" + resultWord);

                        Message msg = papago_handler.obtainMessage();
                        msg.setData(papagoBundle);
                        papago_handler.sendMessage(msg);

                        Log.d(TAG, "msg" + msg);
                    }
                }.start();
            }
        });
    }

    @SuppressLint("HandlerLeak")
    Handler papago_handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String resultWord = bundle.getString("resultWord");
            etMeaning.setText(resultWord);
        }
    };

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAdd:
                if(etName.getText().toString().equals("")) {
                    Toast.makeText(this,"단어를 입력하세요.", Toast.LENGTH_SHORT).show();;
                    break;
                } else if (etMeaning.getText().toString().equals("")) {
                    Toast.makeText(this,"단어의 뜻을 입력하세요.", Toast.LENGTH_SHORT).show();;
                    break;
                } else {
                    boolean result = wordDBManager.addNewWord(
                            new Word(etName.getText().toString(), etMeaning.getText().toString(), "new"));
                    if (result == true) {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("name", etName.getText().toString());
//                        resultIntent.putExtra("meaning", etMeaning.getText().toString());
//                        resultIntent.putExtra("status", "new");
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                    } else {
                        Toast.makeText(this, "새로운 단어 추가 실패!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.btnCancel:
                setResult(RESULT_CANCELED);
                finish();
                break;
        }
    }
}
