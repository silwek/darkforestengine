package com.nekotoneko.redmoon.darkforestengine.sample.engine

import com.nekotoneko.redmoon.darkforestengine.ScenarioEngine
import com.nekotoneko.redmoon.darkforestengine.sample.storysave.SaveRepository

/**
 * @author Silwèk on 2019-08-18
 */
class StoryEngine(
    val scenarioEngine: ScenarioEngine,
    val saveRepository: SaveRepository
)