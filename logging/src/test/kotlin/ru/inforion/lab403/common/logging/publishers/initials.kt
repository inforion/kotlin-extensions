package ru.inforion.lab403.common.logging.publishers

import ru.inforion.lab403.common.logging.FINE
import ru.inforion.lab403.common.logging.INFO
import ru.inforion.lab403.common.logging.SEVERE
import ru.inforion.lab403.common.logging.storage.LoggerStorage

fun setupPublishers(lambda: (name: String) -> AbstractPublisher) {
    LoggerStorage.removePublisher(LoggerStorage.ALL)

    lambda("Publisher-1").also {
        LoggerStorage.addPublisher(LoggerStorage.ALL, it)
    }
    lambda("Publisher-2").also {
        LoggerStorage.addPublisher(".a.b.c", it)
    }
    lambda("Publisher-3").also {
        LoggerStorage.addPublisher(".a.b.c.d.h.i", it)
    }
    lambda("Publisher-4").also {
        LoggerStorage.addPublisher(".a", it)
    }

    LoggerStorage.setAdditivity(".a.b.c.d", false)

    LoggerStorage.setLevel(".a.b.c", INFO)
    LoggerStorage.setLevel(".a.b.c.d", FINE)
    LoggerStorage.setLevel(".a", SEVERE)
}

fun setupPublishersTestNull() = setupPublishers { TestNullPublisher(it) }