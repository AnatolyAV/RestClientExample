package ru.andreev_av.restclientexample.providers;

import android.os.Bundle;

public interface IServiceProvider {

    boolean RunTask(int methodId, Bundle extras);
}
