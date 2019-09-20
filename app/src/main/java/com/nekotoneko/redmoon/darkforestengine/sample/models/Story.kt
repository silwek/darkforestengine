package com.nekotoneko.redmoon.darkforestengine.sample.models

import com.nekotoneko.redmoon.darkforestengine.keys.Key

/**
 * @author Silwèk on 2019-08-17
 */
class Story(
    var id: String,
    var label: String? = null,
    var scenes: List<Scene>? = null,
    var keys: List<Key>? = null
)