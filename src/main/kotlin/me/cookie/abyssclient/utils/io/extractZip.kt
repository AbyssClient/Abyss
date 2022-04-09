package me.cookie.abyssclient.utils.io

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.util.zip.ZipInputStream

fun extractZip(zipStream: InputStream, folder: File) {
    if (!folder.exists()) {
        folder.mkdir()
    }

    ZipInputStream(zipStream).use { zipInputStream ->
        var zipEntry = zipInputStream.nextEntry

        while (zipEntry != null) {
            if (zipEntry.isDirectory) {
                zipEntry = zipInputStream.nextEntry
                continue
            }

            val newFile = File(folder, zipEntry.name)
            File(newFile.parent).mkdirs()

            FileOutputStream(newFile).use {
                zipInputStream.copyTo(it)
            }
            zipEntry = zipInputStream.nextEntry
        }

        zipInputStream.closeEntry()
    }
}

fun extractZip(zipFile: File, folder: File) = extractZip(FileInputStream(zipFile), folder)