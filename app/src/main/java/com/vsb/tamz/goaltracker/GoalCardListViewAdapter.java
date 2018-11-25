package com.vsb.tamz.goaltracker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class GoalCardListViewAdapter extends ArrayAdapter<GoalCard> {

    private Context context;
    private int layoutResourceId;
    private List<GoalCard> data;

    public GoalCardListViewAdapter(@NonNull Context context, int layoutResourceId, @NonNull List<GoalCard> data) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        CardModel cardModel;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layoutResourceId, parent, false);

            cardModel = new CardModel();
            cardModel.name = row.findViewById(R.id.goalName);
            cardModel.repeat = row.findViewById(R.id.goalRepeat);
            cardModel.duration = row.findViewById(R.id.goalDuration);
            cardModel.score = row.findViewById(R.id.goalScore);

            row.setTag(cardModel);
        } else {
            cardModel = (CardModel) row.getTag();
        }

        GoalCard entry = data.get(position);
        cardModel.name.setText(entry.getName());
        cardModel.repeat.setText(entry.getRepeat());
        cardModel.duration.setText(entry.getDuration());
        cardModel.score.setText(entry.getScore());

        return row;
    }

    static class CardModel {
//        ImageView goalImage;
        TextView name;
        TextView repeat;
        TextView duration;
        TextView score;
    }
}
