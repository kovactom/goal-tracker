package com.vsb.tamz.goaltracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vsb.tamz.goaltracker.persistence.AppDatabase;
import com.vsb.tamz.goaltracker.persistence.model.GoalProgress;

import java.util.Date;
import java.util.List;

public class GoalCardListViewAdapter extends ArrayAdapter<GoalCard> implements View.OnClickListener {

    private AppDatabase db;
    private Context context;
    private int layoutResourceId;
    private List<GoalCard> data;
    private CardModel cardModel;

    public GoalCardListViewAdapter(@NonNull Context context, int layoutResourceId, @NonNull List<GoalCard> data) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;

        db = AppDatabase.getDatabase(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layoutResourceId, parent, false);

            cardModel = new CardModel();
            cardModel.goalImage = row.findViewById(R.id.goalImage);
            cardModel.name = row.findViewById(R.id.goalName);
            cardModel.repeat = row.findViewById(R.id.goalRepeat);
            cardModel.duration = row.findViewById(R.id.goalDuration);
            cardModel.score = row.findViewById(R.id.goalScore);
            cardModel.doneButton = row.findViewById(R.id.doneButton);

            row.setTag(cardModel);
        } else {
            cardModel = (CardModel) row.getTag();
        }

        GoalCard entry = data.get(position);
        cardModel.goalImage.setImageURI(Uri.parse(entry.getPicture()));
        cardModel.name.setText(entry.getName());
        cardModel.repeat.setText(entry.getRepeat());
        cardModel.duration.setText(entry.getDuration());
        cardModel.score.setText(entry.getScore());
        cardModel.doneButton.setVisibility(entry.isActive() ? Button.VISIBLE : Button.INVISIBLE);
        cardModel.doneButton.setOnClickListener(this);
        cardModel.doneButton.setTag(R.id.goal_id, entry.getId());
        row.setOnClickListener(this);
        row.setTag(R.id.goal_id, entry.getId());

        return row;
    }

    @Override
    public void onClick(View view) {
        if (view.equals(cardModel.doneButton)) {
            Toast.makeText(this.context, "Done!", Toast.LENGTH_LONG).show();
            GoalProgress goalProgress = new GoalProgress();
            goalProgress.setGoalId((long) view.getTag(R.id.goal_id));
            goalProgress.setDate(new Date());
            db.goalProgressDao().insert(goalProgress);
            ((Activity) context).recreate();
        } else {
            Intent intent = new Intent(context, GoalDetailActivity.class);
            intent.putExtra("goalId", (long) view.getTag(R.id.goal_id));
            ((Activity) context).startActivityForResult(intent, OverviewActivity.GOAL_DETAIL_REQUEST_CODE);
        }
    }

    static class CardModel {
        ImageView goalImage;
        TextView name;
        TextView repeat;
        TextView duration;
        TextView score;
        Button doneButton;
    }
}
