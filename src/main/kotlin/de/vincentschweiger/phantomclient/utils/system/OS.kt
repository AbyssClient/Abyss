package de.vincentschweiger.phantomclient.utils.system

import java.util.*

enum class OS {
    WINDOWS,
    LINUX,
    MAC,
    UNKNOWN
}

object OSUtil {
    fun getOS(): OS {
        val prop = System.getProperty("os.name").lowercase(Locale.getDefault());
        return if (prop.indexOf("win") >= 0) OS.WINDOWS
        else if (prop.indexOf("mac") >= 0) OS.MAC
        else if (prop.indexOf("linux") >= 0) OS.LINUX
        else OS.UNKNOWN
    }
}

var IS_WINDOWS = OSUtil.getOS() == OS.WINDOWS
var IS_LINUX = OSUtil.getOS() == OS.LINUX
var IS_MAC = OSUtil.getOS() == OS.MAC