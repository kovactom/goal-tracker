package com.vsb.tamz.goaltracker.persistence.repository;

import android.app.Application;
import android.os.AsyncTask;

import com.vsb.tamz.goaltracker.persistence.AppDatabase;
import com.vsb.tamz.goaltracker.persistence.GoalDao;
import com.vsb.tamz.goaltracker.persistence.model.Goal;

public class GoalRepository {
    private GoalDao goalDao;

    public GoalRepository(Application application) {
        goalDao = AppDatabase.getDatabase(application).goalDao();
    }

    public void insert(Goal goal) {
        new InsertAsyncTask(goalDao).execute(goal);
    }

    public void update(Goal goal) {
        new UpdateAsyncTask(goalDao).execute(goal);
    }

    public void delete(Goal goal) {
        new DeleteAsyncTask(goalDao).execute(goal);
    }

    private static class InsertAsyncTask extends AsyncTask<Goal, Void, Void> {

        private GoalDao goalDao;

        public InsertAsyncTask(GoalDao goalDao) {
            this.goalDao = goalDao;
        }

        @Override
        protected Void doInBackground(Goal... goals) {
            goalDao.insert(goals[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<Goal, Void, Void> {
        private GoalDao goalDao;

        public UpdateAsyncTask(GoalDao goalDao) {
            this.goalDao = goalDao;
        }

        @Override
        protected Void doInBackground(Goal... goals) {
            goalDao.update(goals[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<Goal, Void, Void> {
        private GoalDao goalDao;

        public DeleteAsyncTask(GoalDao goalDao) {
            this.goalDao = goalDao;
        }

        @Override
        protected Void doInBackground(Goal... goals) {
            goalDao.delete(goals[0]);
            return null;
        }
    }
}
