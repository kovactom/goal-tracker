package com.vsb.tamz.goaltracker.persistence.repository;

import android.app.Application;
import android.os.AsyncTask;

import com.vsb.tamz.goaltracker.persistence.AppDatabase;
import com.vsb.tamz.goaltracker.persistence.GoalNotificationDao;
import com.vsb.tamz.goaltracker.persistence.model.GoalNotification;

public class GoalNotificationRepository {
    private GoalNotificationDao goalNotificationDao;

    public GoalNotificationRepository(Application application) {
        this.goalNotificationDao = AppDatabase.getDatabase(application).goalNotificationDao();
    }

    public void insert(GoalNotification goalNotification) {
        new InsertAsyncTask(goalNotificationDao).execute(goalNotification);
    }

    public void update(GoalNotification goalNotification) {
        new UpdateAsyncTask(goalNotificationDao).execute(goalNotification);
    }

    public void delete(GoalNotification goalNotification) {
        new DeleteAsyncTask(goalNotificationDao).execute(goalNotification);
    }

    private static class InsertAsyncTask extends AsyncTask<GoalNotification, Void, Void> {

        private GoalNotificationDao goalNotificationDao;

        public InsertAsyncTask(GoalNotificationDao goalNotificationDao) {
            this.goalNotificationDao = goalNotificationDao;
        }

        @Override
        protected Void doInBackground(GoalNotification... goalProgresses) {
            goalNotificationDao.insert(goalProgresses[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<GoalNotification, Void, Void> {
        private GoalNotificationDao goalNotificationDao;

        public UpdateAsyncTask(GoalNotificationDao goalNotificationDao) {
            this.goalNotificationDao = goalNotificationDao;
        }

        @Override
        protected Void doInBackground(GoalNotification... goals) {
            goalNotificationDao.update(goals[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<GoalNotification, Void, Void> {
        private GoalNotificationDao goalNotificationDao;

        public DeleteAsyncTask(GoalNotificationDao goalNotificationDao) {
            this.goalNotificationDao = goalNotificationDao;
        }

        @Override
        protected Void doInBackground(GoalNotification... goals) {
            goalNotificationDao.delete(goals[0]);
            return null;
        }
    }
}
