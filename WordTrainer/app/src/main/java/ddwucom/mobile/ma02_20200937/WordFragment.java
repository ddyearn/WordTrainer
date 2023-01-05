package ddwucom.mobile.ma02_20200937;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WordFragment extends Fragment implements View.OnClickListener {

    final int REQ_CODE = 100;
    final int REQ_CODE2 = 200;
    final int REQ_CODE3 = 300;

    MainActivity mainActivity;

    ArrayList<Word> wordList;
    MyAdapter myAdapter;
    ListView listView;
    TextView tvCount;
    Button btnNew;
    EditText etSearch;

    WordDBManager wordDBManager;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WordFragment newInstance(String param1, String param2) {
        WordFragment fragment = new WordFragment();
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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_word, container, false);

        tvCount = (TextView) v.findViewById(R.id.tvCount);
        btnNew = (Button) v.findViewById(R.id.btnNew);
        btnNew.setOnClickListener(this);
        etSearch = (EditText) v.findViewById(R.id.etSearch);

        listView = (ListView) v.findViewById(R.id.listView);
        wordList = new ArrayList<>();

        myAdapter = new MyAdapter(getContext(), R.layout.custom_adapter_view, wordList);
        listView.setAdapter(myAdapter);

        listView.setOnItemClickListener(itemClickListener);
        listView.setOnItemLongClickListener(itemLongClickListener);

        wordDBManager = new WordDBManager(getContext());

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = etSearch.getText().toString();
                wordList.clear();
                wordList.addAll(wordDBManager.searchWordByText(text));
                myAdapter.notifyDataSetChanged();
            }
        });

        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNew:
                Intent addIntent = new Intent(getActivity(), AddActivity.class);
                startActivityForResult(addIntent, REQ_CODE);
                break;
        }
    }

    AdapterView.OnItemClickListener itemClickListener =
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                    Word word = wordList.get(pos);
                    Intent modIntent = new Intent(getActivity(), ModifyActivity.class);
                    modIntent.putExtra("word",word);
                    startActivityForResult(modIntent, REQ_CODE2);
                }
            };

    AdapterView.OnItemLongClickListener itemLongClickListener =
            new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                    final int pos = position;
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("단어 삭제")
                            .setMessage(wordList.get(pos).getName() + " 단어를 삭제하시겠습니까?")
                            .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(getContext(), "확인 버튼 클릭", Toast.LENGTH_SHORT).show();
                                    boolean result = wordDBManager.removeWord(wordList.get(pos).getName());
                                    Log.d("result", String.valueOf(result));
                                    if(result) {
                                        Toast.makeText(getContext(), "삭제 완료", Toast.LENGTH_SHORT).show();
                                        wordList.clear();
                                        wordList.addAll(wordDBManager.getAllWord());
                                        myAdapter.notifyDataSetChanged();
                                    } else {
                                        Toast.makeText(getContext(), "삭제 실패", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setNegativeButton("취소", null)
                            .setCancelable(false)
                            .show();
                    return true;
                }
            };


    public void onResume() {
        super.onResume();
        wordList.clear();
        wordList.addAll(wordDBManager.getAllWord());
        tvCount.setText(wordList.size() + "words");
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    String name = data.getStringExtra("name");
                    Toast.makeText(getActivity(), name + " 추가 완료", Toast.LENGTH_SHORT).show();
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    Toast.makeText(getActivity(), "단어 추가 취소", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQ_CODE2:
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(getActivity(), "단어 수정 완료", Toast.LENGTH_SHORT).show();
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    Toast.makeText(getActivity(), "단어 수정 취소", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}