package ru.inforion.lab403.common.json.interfaces

import com.google.gson.JsonDeserializer
import com.google.gson.JsonSerializer

interface JsonSerde<T> : JsonSerializer<T>, JsonDeserializer<T>