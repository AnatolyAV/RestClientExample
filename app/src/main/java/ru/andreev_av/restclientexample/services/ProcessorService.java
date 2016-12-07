package ru.andreev_av.restclientexample.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;

import ru.andreev_av.restclientexample.activities.MainActivity;
import ru.andreev_av.restclientexample.providers.IServiceProvider;
import ru.andreev_av.restclientexample.providers.ServiceProvider;

public class ProcessorService extends Service {

    private final Context mContext = this;
    private Integer lastStartId;

    private IServiceProvider GetProvider(int providerId) {
        switch (providerId) {
            case Providers.ROWS_PROVIDER:
                return new ServiceProvider(this);
        }
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        lastStartId = startId;

        Bundle extras = intent.getExtras();

        AsyncServiceTask task = new AsyncServiceTask(extras);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[]) null);

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static class Extras {
        public static final String PROVIDER_EXTRA = "PROVIDER_EXTRA";
        public static final String METHOD_EXTRA = "METHOD_EXTRA";
        public static final String RESULT_ACTION_EXTRA = "RESULT_ACTION_EXTRA";
        public static final String RESULT_EXTRA = "RESULT_EXTRA";
    }

    public static class Providers {
        public static final int ROWS_PROVIDER = 1;
    }

    private class AsyncServiceTask extends AsyncTask<Void, Void, Boolean> {
        private final Bundle mExtras;
        Intent intent = new Intent(MainActivity.BROADCAST_ACTION);

        public AsyncServiceTask(Bundle extras) {
            mExtras = extras;
        }

        @Override
        protected void onPreExecute() {
            // сообщаем об старте задачи
            intent.putExtra(MainActivity.PARAM_STATUS, MainActivity.STATUS_START);
            mContext.sendBroadcast(intent);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Boolean result = false;
            final int providerId = mExtras.getInt(Extras.PROVIDER_EXTRA);
            final int methodId = mExtras.getInt(Extras.METHOD_EXTRA);

            if (providerId != 0 && methodId != 0) {
                final IServiceProvider provider = GetProvider(providerId);

                if (provider != null) {
                    try {
                        result = provider.RunTask(methodId, mExtras);
                    } catch (Exception e) {
                        result = false;
                        e.printStackTrace();
                    }
                }

            }

            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // сообщаем об окончании задачи
            intent.putExtra(MainActivity.PARAM_STATUS, MainActivity.STATUS_FINISH);
            intent.putExtra(Extras.RESULT_EXTRA, result.booleanValue());
            intent.putExtras(mExtras);
            mContext.sendBroadcast(intent);
            stopSelf(lastStartId);
        }

    }
}
