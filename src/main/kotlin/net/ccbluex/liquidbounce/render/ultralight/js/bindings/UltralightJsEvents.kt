/*
 * This file is part of LiquidBounce (https://github.com/CCBlueX/LiquidBounce)
 *
 * Copyright (c) 2016 - 2022 CCBlueX
 *
 * LiquidBounce is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LiquidBounce is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LiquidBounce. If not, see <https://www.gnu.org/licenses/>.
 */

package net.ccbluex.liquidbounce.render.ultralight.js.bindings

import com.labymedia.ultralight.databind.context.ContextProvider
import com.labymedia.ultralight.javascript.JavascriptObject
import com.labymedia.ultralight.javascript.JavascriptPropertyAttributes
import com.mojang.blaze3d.systems.RenderSystem
import de.vincentschweiger.phantomclient.events.Event
import de.vincentschweiger.phantomclient.events.EventCancelable
import de.vincentschweiger.phantomclient.events.EventManager
import net.ccbluex.liquidbounce.render.ultralight.View
import net.ccbluex.liquidbounce.utils.client.logger

/**
 * Referenced by JS as `events`
 */
class UltralightJsEvents(private val viewContextProvider: ContextProvider, val view: View) {

    companion object {
        private val EVENT_MAP = mutableMapOf<String, Class<out Event>>()

        init {
            EVENT_MAP["close"] = ViewCloseEvent::class.java
        }
    }

    fun on(name: String, handler: JavascriptObject) {
        if (!handler.isFunction) {
            throw IllegalArgumentException("$handler is not a function.")
        }

        // Do we know an event that has this name? What is the event class behind this name?
        val eventClass = EVENT_MAP[name] ?: throw IllegalArgumentException("Unknown event: $name")

        // Get the list of the current event type
        val hookList = _registeredEvents.computeIfAbsent(eventClass) { ArrayList() }

        // The event function is stored in this property
        val propertyName = "engine__${name}__${hookList.size}"

        // Make a property with the name engine__ plus the name of the event. This is made to
        // make the function accessible for the event handler
        viewContextProvider.syncWithJavascript {
            it.context.globalObject.setProperty(propertyName, handler, JavascriptPropertyAttributes.NONE)
        }

        // Create an event hook that will call the JS function
        val eventHook = EventHook<Event>(
                this,
                { event ->
                    RenderSystem.recordRenderCall {
                        runCatching {
                            viewContextProvider.syncWithJavascript {
                                it.context.globalObject.getProperty(propertyName).toObject().callAsFunction(
                                        it.context.globalObject,
                                        view.context.databind.conversionUtils.toJavascript(it.context, event)
                                )
                            }
                        }.onFailure {
                            logger.error("Ultralight JS Engine", it)
                        }
                    }
                },
                false,
        )

        // Add the event hook to the list
        hookList.add(eventHook)

        // Register the event hook
        EventManager.registerEventHook(eventClass, eventHook)
    }

    /**
     * Directly call event to ultralight view
     */
    fun _directlyCallEvent(event: Event) {
        val target = _registeredEvents[event.javaClass] ?: return

        for (eventHook in target) {
            runCatching {
                eventHook.handler(event)
            }.onFailure {
                logger.error("Exception while executing handler.")
            }
        }
    }

    fun _fireViewClose(): Boolean {
        val viewCloseEvent = ViewCloseEvent()
        _directlyCallEvent(viewCloseEvent)
        return !viewCloseEvent.isCancelled
    }

    inner class ViewCloseEvent : EventCancelable()

}
