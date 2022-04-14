package me.cookie.abyssclient.server

data class Cosmetics(
    val cape: Cape = Cape()
)

data class Cape(
    val name: String = "none",
    val enabled: Boolean = (name != "none"),
)