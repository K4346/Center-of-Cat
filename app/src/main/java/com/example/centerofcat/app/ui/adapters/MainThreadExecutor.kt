package com.example.centerofcat.app.ui.adapters

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor


class MainThreadExecutor : Executor {
    private val mHandler: Handler = Handler(Looper.getMainLooper())
    override fun execute(p0: Runnable) {
        mHandler.post(p0);
    }
}