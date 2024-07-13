package com.example.catlistapp.profile.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.example.catlistapp.networking.serialization.AppJson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.StandardCharsets

class ProfileDataSerializer : Serializer<ProfileData> {

    override val defaultValue: ProfileData = ProfileData.EMPTY

    override suspend fun readFrom(input: InputStream): ProfileData {
        val text = String(input.readBytes(), charset = StandardCharsets.UTF_8)
        return try {
            AppJson.decodeFromString<ProfileData>(text)
        } catch (error: SerializationException) {
            throw CorruptionException(message = "Unable to deserialize file.", cause = error)
        }
    }

    override suspend fun writeTo(t: ProfileData, output: OutputStream) {
        val text = AppJson.encodeToString(t)
        withContext(Dispatchers.IO) {
            output.write(text.toByteArray(charset = StandardCharsets.UTF_8))
        }
    }
}