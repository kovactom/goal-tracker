package com.vsb.tamz.goaltracker.persistence.repository;

import android.os.AsyncTask;

import com.vsb.tamz.goaltracker.persistence.GoalProgressDao;
import com.vsb.tamz.goaltracker.persistence.model.GoalProgress;

public class GoalProgressRepository {
    private GoalProgressDao goalProgressDao;

    public GoalProgressRepository(GoalProgressDao goalProgressDao) {
        this.goalProgressDao = goalProgressDao;
    }

    public void insert(GoalProgress goalProgress) {
        new InsertAsyncTask(goalProgressDao).execute(goalProgress);
    }

    public void update(GoalProgress goalProgress) {
        new UpdateAsyncTask(goalProgressDao).execute(goalProgress);
    }

    public void delete(GoalProgress goalProgress) {
        new DeleteAsyncTask(goalProgressDao).execute(goalProgress);
    }

    private static class InsertAsyncTask extends AsyncTask<GoalProgress, Void, Void> {

        private GoalProgressDao goalProgressDao;

        public InsertAsyncTask(GoalProgressDao goalProgressDao) {
            this.goalProgressDao = goalProgressDao;
        }

        @Override
        protected Void doInBackground(GoalProgress... goalProgresses) {
            goalProgressDao.insert(goalProgresses[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<GoalProgress, Void, Void> {
        private GoalProgressDao goalProgressDao;

        public UpdateAsyncTask(GoalProgressDao goalProgressDao) {
            this.goalProgressDao = goalProgressDao;
        }

        @Override
        protected Void doInBackground(GoalProgress... goals) {
            goalProgressDao.update(goals[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<GoalProgress, Void, Void> {
        private GoalProgressDao goalProgressDao;

        public DeleteAsyncTask(GoalProgressDao goalProgressDao) {
            this.goalProgressDao = goalProgressDao;
        }

        @Override
        protected Void doInBackground(GoalProgress... goals) {
            goalProgressDao.delete(goals[0]);
            return null;
        }
    }
}
