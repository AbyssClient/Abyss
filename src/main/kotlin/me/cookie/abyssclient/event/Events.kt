package me.cookie.abyssclient.event

import me.cookie.abyssclient.config.Value
import me.cookie.abyssclient.utils.client.Nameable
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.network.AbstractClientPlayerEntity
import net.minecraft.client.util.InputUtil
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.client.world.ClientWorld
import net.minecraft.entity.Entity

// Game events
@Nameable("gameTick")
class GameTickEvent : Event()

// Render events
@Nameable("gameRender")
class GameRenderEvent : Event()

@Nameable("overlayRender")
class OverlayRenderEvent(val matrices: MatrixStack, val tickDelta: Float) : Event()

@Nameable("screenRender")
class ScreenRenderEvent(val screen: Screen, val matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) : Event()

@Nameable("windowResize")
class WindowResizeEvent(val window: Long, val width: Int, val height: Int) : Event()

@Nameable("windowFocus")
class WindowFocusEvent(val window: Long, val focused: Boolean) : Event()

@Nameable("mouseButton")
class MouseButtonEvent(val window: Long, val button: Int, val action: Int, val mods: Int) : Event()

@Nameable("mouseScroll")
class MouseScrollEvent(val window: Long, val horizontal: Double, val vertical: Double) : Event()

@Nameable("mouseCursor")
class MouseCursorEvent(val window: Long, val x: Double, val y: Double) : Event()

@Nameable("keyboardKey")
class KeyboardKeyEvent(val window: Long, val keyCode: Int, val scancode: Int, val action: Int, val mods: Int) : Event()

@Nameable("keyboardChar")
class KeyboardCharEvent(val window: Long, val codepoint: Int) : Event()

// Input events

@Nameable("inputHandle")
class InputHandleEvent : Event()

@Nameable("key")
class KeyEvent(val key: InputUtil.Key, val action: Int, val mods: Int) : Event()

@Nameable("mouseRotation")
class MouseRotationEvent(var cursorDeltaX: Double, var cursorDeltaY: Double) : CancellableEvent()

// User action events
@Nameable("screen")
class ScreenEvent(val screen: Screen?) : CancellableEvent()

@Nameable("playerMovementTick")
class PlayerMovementTickEvent() : Event()

@Nameable("chatSend")
class ChatSendEvent(val message: String) : CancellableEvent()

// Client events
@Nameable("clientStart")
class ClientStartEvent : Event()

@Nameable("clientShutdown")
class ClientShutdownEvent : Event()

// Config events
@Nameable("valueChanged")
class ValueChangedEvent(val value: Value<*>) : Event()

@Nameable("toggleModule")
class ToggleModuleEvent(val module: me.cookie.abyssclient.module.Module, val newState: Boolean) : Event()

// Game events
@Nameable("playerRender")
class PlayerRenderEvent(val player: AbstractClientPlayerEntity) : Event()

@Nameable("joinWorld")
class WorldJoinEvent(val world: ClientWorld) : Event()

@Nameable("worldDisconnect")
class WorldDisconnectEvent : Event()

/**
 * Also fired when client joins world
 * => fired for every entity that gets tracked
 */
@Nameable("playerJoinWorld")
class PlayerJoinWorldEvent(val player: AbstractClientPlayerEntity) : Event()