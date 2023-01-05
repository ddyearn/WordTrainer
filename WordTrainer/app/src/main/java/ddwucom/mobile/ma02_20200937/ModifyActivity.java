package ddwucom.mobile.ma02_20200937;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ModifyActivity extends AppCompatActivity {
    
    Word word;
    EditText modName;
    EditText modMeaning;
    
    WordDBManager wordDBManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);
        
        modName = findViewById(R.id.modName);
        modMeaning = findViewById(R.id.modMeaning);
        
        word = (Word) getIntent().getSerializableExtra("word");
        
        modName.setText(word.getName());
        modMeaning.setText(word.getMeaning());
        
        wordDBManager = new WordDBManager(this);
    }
    
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnModify:
                if(modName.getText().toString().equals("")) {
                    Toast.makeText(this, "단어를 입력하세요.", Toast.LENGTH_SHORT).show();
                    break;
                } else if (modMeaning.getText().toString().equals("")) {
                    Toast.makeText(this, "단어의 뜻을 입력하세요.", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    word.setName(modName.getText().toString());
                    word.setMeaning(modMeaning.getText().toString());

                    if(wordDBManager.modifyWord(word)) {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("word", word);
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    } else {
                        setResult(RESULT_CANCELED);
                    }
                }
                break;
            case R.id.btnCancelModify:
                setResult(RESULT_CANCELED);
                finish();
                break;
        }
    }
}
