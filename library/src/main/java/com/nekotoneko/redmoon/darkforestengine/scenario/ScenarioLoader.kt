package com.nekotoneko.redmoon.darkforestengine.scenario

import com.nekotoneko.redmoon.darkforestengine.keys.Key

/**
 * @author Silwèk on 2019-08-13
 */
interface ScenarioLoader {
    fun loadScenario(
        scenarioId: String,
        onError: (() -> Unit)? = null,
        onLoad: (nodes: List<Node>, keys: List<Key>) -> Unit
    )
}