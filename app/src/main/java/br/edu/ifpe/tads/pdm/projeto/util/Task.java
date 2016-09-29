package br.edu.ifpe.tads.pdm.projeto.util;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by Edmilson Santana on 29/09/2016.
 */

public class Task<T> extends AsyncTask<Void, Void, Task<T>.TaskResult<T>> {

    private static final String TAG = "Task";
    TaskListener<T> listener;

    public class TaskResult<T> {
        private T response;
        private Exception exception;
    }

    public Task(TaskListener<T> listener) {
        this.listener = listener;
    }

    private Task() {}

    @Override
    protected TaskResult<T> doInBackground(Void... params) {
        TaskResult<T> taskResult = new TaskResult<>();
        try {
            taskResult.response = listener.execute();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            taskResult.exception = e;
        }
        return taskResult;
    }

    @Override
    protected void onPostExecute(TaskResult<T> taskResult) {
        super.onPostExecute(taskResult);
        listener.updateView(taskResult.response);
    }


}
