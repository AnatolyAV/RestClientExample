package ru.andreev_av.restclientexample.helpers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import ru.andreev_av.restclientexample.services.ProcessorService;

public abstract class AbstractServiceHelper {
    private final Context mcontext;
    private final int mProviderId;
    private final String mResultAction;

    public AbstractServiceHelper(Context context, int providerId, String resultAction) {
        mcontext = context;
        mProviderId = providerId;
        mResultAction = resultAction;
    }

    protected void RunMethod(int methodId) {
        RunMethod(methodId, null);
    }

    protected void RunMethod(int methodId, Bundle bundle) {
        Intent service = new Intent(mcontext, ProcessorService.class);

        service.putExtra(ProcessorService.Extras.PROVIDER_EXTRA, mProviderId);
        service.putExtra(ProcessorService.Extras.METHOD_EXTRA, methodId);
        service.putExtra(ProcessorService.Extras.RESULT_ACTION_EXTRA, mResultAction);

        if (bundle != null) {
            service.putExtras(bundle);
        }

        mcontext.startService(service);
    }
}
