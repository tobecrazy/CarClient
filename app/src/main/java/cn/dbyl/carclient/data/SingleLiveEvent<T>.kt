package cn.dbyl.carclient.data

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Code from
 * @see https://github.com/android/architecture-samples/blob/dev-todo-mvvm-live/todoapp/app/src/main/java/com/example/android/architecture/blueprints/todoapp/SingleLiveEvent.java
 * Create by Young on 01/05/2020
 * Single Live Event
 **/
class SingleLiveEvent<T> : MutableLiveData<T>() {
    companion object {
        val TAG: String? = SingleLiveEvent::class.java.canonicalName
    }

    private var mPending: AtomicBoolean = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        if (hasActiveObservers()) {
            Log.d(TAG, "Multiple observers registered but only one will be notified of changes.")
        }
        //observe the internal mutableLiveData
        super.observe(owner, Observer<T> { t ->
            if (mPending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        })
    }

    @MainThread
    override fun setValue(value: T?) {
        mPending.set(true)
        super.setValue(value)
    }


    /***
     * Used for case when T is void , to make calls cleaner
     */
    @MainThread
    fun call() {
        setValue(null)
    }

}