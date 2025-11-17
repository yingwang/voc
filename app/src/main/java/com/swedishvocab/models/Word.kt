package com.swedishvocab.models

data class Word(
    val swedish: String,
    val english: String,
    val category: String
)

data class Dictionary(
    val words: List<Word>
)
