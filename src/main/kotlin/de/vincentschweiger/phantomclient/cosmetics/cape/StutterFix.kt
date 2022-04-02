package de.vincentschweiger.phantomclient.cosmetics.cape


object StutterFix {
    private var timeLastFrameMs: Long = 0
    var averageFrameTimeMs: Long = 0
        private set

    fun frameStart() {
        val i = System.currentTimeMillis()
        val j = i - timeLastFrameMs
        timeLastFrameMs = i
        averageFrameTimeMs = (averageFrameTimeMs + j) / 2L
        averageFrameTimeMs = limit(averageFrameTimeMs, 1L, 1000L)
    }

    val averageFrameTimeSec: Float
        get() = averageFrameTimeMs.toFloat() / 1000.0f

    fun limit(f: Float, f1: Float, f2: Float): Float {
        return if (f < f1) {
            f1
        } else Math.min(f, f2)
    }

    fun limit(l: Long, l1: Long, l2: Long): Long {
        return if (l < l1) {
            l1
        } else Math.min(l, l2)
    }
}