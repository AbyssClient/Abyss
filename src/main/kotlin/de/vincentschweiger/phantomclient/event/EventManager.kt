package de.vincentschweiger.phantomclient.event

import de.vincentschweiger.phantomclient.Phantom
import de.vincentschweiger.phantomclient.utils.client.Nameable
import de.vincentschweiger.phantomclient.utils.client.logger
import kotlin.reflect.full.findAnnotation

/**
 * A modern and fast event handler using lambda handlers
 */
object EventManager {

    private val registry = mutableMapOf<Class<out Event>, ArrayList<EventHook<in Event>>>()

    val mappedEvents = arrayOf(
            GameTickEvent::class,
            ClientStartEvent::class,
            OverlayRenderEvent::class,
            InputHandleEvent::class,
            KeyEvent::class,
            ScreenEvent::class,
            ToggleModuleEvent::class,
            ClientShutdownEvent::class
    ).map { Pair(it.findAnnotation<Nameable>()!!.name, it) }

    init {
        SequenceManager
        Phantom
    }

    /**
     * Used by handler methods
     */
    fun <T : Event> registerEventHook(eventClass: Class<out Event>, eventHook: EventHook<T>) {
        val handlers = registry.computeIfAbsent(eventClass) { ArrayList() }

        val hook = eventHook as EventHook<in Event>

        if (!handlers.contains(hook)) {
            handlers.add(hook)

            handlers.sortByDescending { it.priority }
        }
    }

    /**
     * Unregisters a handler.
     */
    fun <T : Event> unregisterEventHook(eventClass: Class<out Event>, eventHook: EventHook<T>) {
        registry[eventClass]?.remove(eventHook as EventHook<in Event>)
    }

    /**
     * Unregister listener
     *
     * @param listenable for unregister
     */
    fun unregisterListener(listenable: Listenable) {
        for ((key, handlerList) in registry) {
            handlerList.removeIf { it.handlerClass == listenable }

            registry[key] = handlerList
        }
    }

    /**
     * Call event to listeners
     *
     * @param event to call
     */
    fun <T : Event> callEvent(event: T): T {
        val target = registry[event.javaClass] ?: return event

        for (eventHook in target) {
            if (!eventHook.ignoresCondition && !eventHook.handlerClass.handleEvents()) {
                continue
            }

            runCatching {
                eventHook.handler(event)
            }.onFailure {
                logger.error("Exception while executing handler.", it)
            }
        }

        return event
    }

}
