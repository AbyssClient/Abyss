package de.vincentschweiger.phantomclient.event

typealias Handler<T> = (T) -> Unit

class EventHook<T : Event>(val handlerClass: Listenable, val handler: Handler<T>, val ignoresCondition: Boolean, val priority: Int = 0)

interface Listenable {

    /**
     * Allows to disable event handling when condition is false.
     */
    fun handleEvents(): Boolean = parent()?.handleEvents() ?: true

    /**
     * Parent listenable
     */
    fun parent(): Listenable? = null

}

inline fun <reified T : Event> Listenable.handler(ignoreCondition: Boolean = false, priority: Int = 0, noinline handler: Handler<T>) {
    //println("Registering event hook")
    //println(T::class.java)
    EventManager.registerEventHook(T::class.java, EventHook(this, handler, ignoreCondition, priority))
}

/**
 * Registers an event hook for events of type [T] and launches a sequence
 */
inline fun <reified T : Event> Listenable.sequenceHandler(ignoreCondition: Boolean = false, noinline eventHandler: SuspendableHandler<T>) {
    handler<T>(ignoreCondition) { event -> Sequence(eventHandler, event) }
}

/**
 * Registers a repeatable sequence which repeats the execution of code.
 */
fun Listenable.repeatable(eventHandler: (SuspendableHandler<DummyEvent>)) {
    var sequence: RepeatingSequence? = null
    handler<ToggleModuleEvent>(ignoreCondition = true) {
        if (this == it.module || this.parent() == it.module) {
            if (this.handleEvents()) {
                if (sequence == null) {
                    sequence = RepeatingSequence(eventHandler)
                }
            } else if (sequence != null) {
                sequence?.cancel()
                sequence = null
            }
        }
    }
}