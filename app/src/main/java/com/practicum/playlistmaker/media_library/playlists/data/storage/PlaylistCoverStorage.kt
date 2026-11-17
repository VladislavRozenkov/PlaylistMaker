package com.practicum.playlistmaker.media_library.playlists.data.storage

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import java.io.File
import java.io.IOException
import java.util.UUID

class PlaylistCoverStorage(
    private val context: Context
) {

    fun saveCover(uriString: String): String {
        val uri = Uri.parse(uriString)

        val directory = File(
            context.filesDir,
            "playlist_covers"
        )

        if (!directory.exists() && !directory.mkdirs()) {
            throw IOException("Не удалось создать папку обложек")
        }

        val extension = context.contentResolver.getType(uri)
            ?.let {
                MimeTypeMap.getSingleton()
                    .getExtensionFromMimeType(it)
            } ?: "img"

        val file = File(
            directory,
            "${UUID.randomUUID()}.$extension"
        )

        try {
            val inputStream =
                context.contentResolver.openInputStream(uri)
                    ?: throw IOException("Не удалось открыть изображение")

            inputStream.use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
        } catch (error: Exception) {
            file.delete()
            throw error
        }

        return file.absolutePath
    }
}