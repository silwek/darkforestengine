package com.nekotoneko.redmoon.darkforestengine.sample.storyloader

import com.squareup.moshi.JsonClass

/**
 * @author Silwèk on 2019-08-17
 */
@JsonClass(generateAdapter = true)
data class StoryJson(
    val id: String,
    val label: String? = null,
    val keys: List<KeyJson>? = null,
    val nodes: List<SceneJson>? = null
)

@JsonClass(generateAdapter = true)
data class KeyJson(
    val id: String,
    val type: String
)

@JsonClass(generateAdapter = true)
data class SceneJson(
    val id: String,
    val nextId: String? = null,
    val type: String? = null,
    val conditions: String? = null,
    val consequences: String? = null,
    val content: SceneContentJson? = null,
    val nodes: List<SceneJson>? = null
)

@JsonClass(generateAdapter = true)
data class SceneContentJson(
    val text: String? = null
)