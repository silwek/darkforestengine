package com.nekotoneko.redmoon.darkforestengine.sample.models

import com.nekotoneko.redmoon.darkforestengine.scenario.Group
import com.nekotoneko.redmoon.darkforestengine.scenario.Node

/**
 * @author Silwèk on 2019-08-17
 */
class SceneGroup(
    id: String,
    conditions: String? = null,
    consequences: String? = null,
    nextId: String? = null,
    content: SceneContent? = null,
    var type:String = USER_CHOICES,
    override var nodes: List<Node>
) : Scene(id, conditions, consequences, nextId, content), Group{
    companion object{
        val USER_CHOICES:String = "userChoices"
        val STORY_CROSSROADS:String = "storyCrossroads"
    }
}