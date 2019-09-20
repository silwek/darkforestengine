package com.nekotoneko.redmoon.darkforestengine.sample.models

import com.nekotoneko.redmoon.darkforestengine.scenario.Node

/**
 * @author Silwèk on 2019-08-17
 */
open class Scene(
    id: String,
    conditions: String? = null,
    consequences: String? = null,
    nextId: String? = null,
    var content: SceneContent? = null
) : Node(id, conditions, consequences, nextId) {
    override fun toString(): String {
        return "Scene(content=$content) ${super.toString()}"
    }
}