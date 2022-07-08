package com.example.flowmvvm.utils.rxAndroid

import androidx.annotation.NonNull
import io.reactivex.rxjava3.core.Scheduler

interface RxBaseSchedulerProvider {

    @NonNull
    fun computation(): Scheduler

    @NonNull
    fun io(): Scheduler

    @NonNull
    fun ui(): Scheduler
}
