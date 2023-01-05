package ddwucom.mobile.ma02_20200937;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Word> wordList;
    private LayoutInflater layoutInflater;

    public MyAdapter(Context context, int layout, ArrayList<Word> wordList) {
        this.context = context;
        this.layout = layout;
        this.wordList = wordList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return wordList.size();
    }

    @Override
    public Object getItem(int i) {
        return wordList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return wordList.get(i).get_id();
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup viewGroup) {
        final int position = pos;
        ViewHolder holder;

        if(convertView == null) {
            convertView = layoutInflater.inflate(layout, viewGroup,false);

            holder = new ViewHolder();
            holder.tvWordId = (TextView) convertView.findViewById(R.id.tvWordId);
            holder.wordName = (TextView) convertView.findViewById(R.id.tvWord);
            holder.wordMeaning = (TextView) convertView.findViewById(R.id.tvWordMean);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.wordName.setText(wordList.get(position).getName());
        holder.wordMeaning.setText(wordList.get(position).getMeaning());
        holder.tvWordId.setText(String.valueOf(position+1));

        //status로 word 색상 변경
        switch (wordList.get(position).getStatus()) {
            case "new":
                holder.wordName.setTextColor(Color.parseColor("#dbdbdb"));
                break;
            case "yes":
                holder.wordName.setTextColor(Color.parseColor("#7aff7a"));
                break;
            case "no":
                holder.wordName.setTextColor(Color.parseColor("#ff7a7a"));
                break;
        }
        return convertView;
    }

    static class ViewHolder {
        TextView tvWordId;
        TextView wordName;
        TextView wordMeaning;
    }
}
