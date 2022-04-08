package de.vincentschweiger.phantomclient.utils.client

class ThreadLock<T> {

    private lateinit var lockedOnThread: Thread
    private var content: T? = null

    fun get(): T {
        if (Thread.currentThread() != lockedOnThread) {
            error("thread-locked content accessed by other thread")
        }

        return content!!
    }

    fun lock(t: T) {
        lockedOnThread = Thread.currentThread()
        content = t
    }

}