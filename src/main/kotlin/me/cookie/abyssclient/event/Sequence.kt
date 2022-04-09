package me.cookie.abyssclient.event

import com.google.common.collect.Lists
import me.cookie.abyssclient.utils.client.logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

typealias SuspendableHandler<T> = suspend Sequence<T>.(T) -> Unit

object SequenceManager : Listenable {

    // Running sequences
    internal val sequences = Lists.newCopyOnWriteArrayList<Sequence<*>>()

    /**
     * Tick sequences
     */
    val entityTickHandler = handler<PlayerMovementTickEvent> {
        for (sequence in sequences) {
            sequence.tick()
        }
    }

}

open class Sequence<T : Event>(val handler: SuspendableHandler<T>, val event: T) {

    private var coroutine = GlobalScope.launch(Dispatchers.Unconfined) {
        SequenceManager.sequences += this@Sequence
        coroutineRun()
        SequenceManager.sequences -= this@Sequence
    }

    private var continuation: Continuation<Unit>? = null
    private var elapsedTicks = 0
    private var totalTicks: () -> Int = { 0 }

    internal open suspend fun coroutineRun() {
        runCatching {
            handler(event)
        }.onFailure {
            logger.error("Exception occurred during subroutine", it)
        }
    }

    fun tick() {
        if (this.elapsedTicks < this.totalTicks()) {
            this.elapsedTicks++
        } else {
            this.continuation?.resume(Unit)
        }
    }

    suspend fun wait(ticks: Int) {
        this.wait { ticks }
    }

    /**
     * Waits for the amount of ticks that is retrieved via [ticksToWait]
     */
    suspend fun wait(ticksToWait: () -> Int) {
        elapsedTicks = 0
        totalTicks = ticksToWait

        suspendCoroutine<Unit> { continuation = it }
    }

    internal suspend fun sync() = wait(0)

    suspend fun waitUntil(case: () -> Boolean) {
        while (!case()) {
            sync()
        }
    }

}

class DummyEvent : Event()

class RepeatingSequence(handler: SuspendableHandler<DummyEvent>) : Sequence<DummyEvent>(handler, DummyEvent()) {

    private var repeat = true

    override suspend fun coroutineRun() {
        sync()

        while (repeat) {
            runCatching {
                handler(event)
            }.onFailure {
                logger.error("Exception occurred during subroutine", it)
            }

            sync()
        }
    }

    fun cancel() {
        repeat = false
    }

}
