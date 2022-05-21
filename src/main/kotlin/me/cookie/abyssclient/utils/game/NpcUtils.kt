package me.cookie.abyssclient.utils.game

import java.util.UUID

fun isProbablyNpc(uuid: UUID): Boolean = uuid.version() == 2