package com.example.infiltrados.services

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class WordPair(val word1: String, val word2: String)

object WordLoader {
    fun loadWordPairs(context: Context, spanish: Boolean): List<WordPair> {
        val fileName = if (spanish) "wordsEs.json" else "wordsEn.json"
        val json = context.assets.open(fileName).bufferedReader().use { it.readText() }
        val type = object : TypeToken<List<WordPair>>() {}.type
        return Gson().fromJson(json, type)
    }
}
