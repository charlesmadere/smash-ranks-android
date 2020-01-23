package com.garpr.android.test

import com.garpr.android.misc.ThreadUtils
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future
import java.util.concurrent.FutureTask
import java.util.concurrent.TimeUnit

class TestThreadUtilsImpl : ThreadUtils {

    override val background: ExecutorService = object : ExecutorService {

        override fun awaitTermination(timeout: Long, unit: TimeUnit): Boolean {
            throw NotImplementedError()
        }

        override fun execute(command: Runnable) {
            command.run()
        }

        override fun <T : Any?> invokeAll(
                tasks: MutableCollection<out Callable<T>>
        ): MutableList<Future<T>> {
            throw NotImplementedError()
        }

        override fun <T : Any?> invokeAll(
                tasks: MutableCollection<out Callable<T>>,
                timeout: Long,
                unit: TimeUnit
        ): MutableList<Future<T>> {
            throw NotImplementedError()
        }

        override fun <T : Any?> invokeAny(tasks: MutableCollection<out Callable<T>>): T {
            throw NotImplementedError()
        }

        override fun <T : Any?> invokeAny(
                tasks: MutableCollection<out Callable<T>>,
                timeout: Long,
                unit: TimeUnit
        ): T {
            throw NotImplementedError()
        }

        override fun isShutdown(): Boolean {
            throw NotImplementedError()
        }

        override fun isTerminated(): Boolean {
            throw NotImplementedError()
        }

        override fun shutdown() {
            throw NotImplementedError()
        }

        override fun shutdownNow(): MutableList<Runnable> {
            throw NotImplementedError()
        }

        override fun <T : Any?> submit(task: Callable<T>): Future<T> {
            val future = FutureTask(task)
            future.run()
            return future
        }

        override fun submit(task: Runnable): Future<*> {
            return submit(task, null)
        }

        override fun <T : Any?> submit(task: Runnable, result: T?): Future<T?> {
            val future = FutureTask(task, result)
            future.run()
            return future
        }

    }

}
