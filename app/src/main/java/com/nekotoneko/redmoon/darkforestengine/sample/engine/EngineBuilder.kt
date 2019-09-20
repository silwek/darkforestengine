package com.nekotoneko.redmoon.darkforestengine.sample.engine

import android.content.Context
import com.nekotoneko.redmoon.darkforestengine.ScenarioEngine
import com.nekotoneko.redmoon.darkforestengine.conditions.ConditionsResolver
import com.nekotoneko.redmoon.darkforestengine.conditions.DefaultConditionResolver
import com.nekotoneko.redmoon.darkforestengine.consequences.ConsequencesResolver
import com.nekotoneko.redmoon.darkforestengine.consequences.DefaultConsequenceExecutor
import com.nekotoneko.redmoon.darkforestengine.scenario.ScenarioViewer
import com.nekotoneko.redmoon.darkforestengine.sample.storyloader.StoryRepository
import com.nekotoneko.redmoon.darkforestengine.sample.storysave.EngineKeysStore
import com.nekotoneko.redmoon.darkforestengine.sample.storysave.SaveRepository

/**
 * @author Silwèk on 2019-08-14
 */
class EngineBuilder {

    fun create(context: Context, scenarioId: String, scenarioViewer: ScenarioViewer): StoryEngine {
        val saveRepository = SaveRepository(
            context.getSharedPreferences(
                SaveRepository.DEFAULT_PREFS,
                Context.MODE_PRIVATE
            )
        )
        val keysStorage = EngineKeysStore(scenarioId, saveRepository)
        val conditions = ConditionsResolver()
        conditions.addResolver(DefaultConditionResolver())
        val consequences = ConsequencesResolver()
        consequences.addExecutor(DefaultConsequenceExecutor(keysStorage))
        val loader = StoryRepository(context.assets)
        val scenarioEngine = ScenarioEngine(
            scenarioId,
            keyStore = keysStorage,
            conditionsResolver = conditions,
            consequencesResolver = consequences,
            scenarioLoader = loader,
            scenarioViewer = scenarioViewer
        )

        return StoryEngine(scenarioEngine,saveRepository)
    }

}