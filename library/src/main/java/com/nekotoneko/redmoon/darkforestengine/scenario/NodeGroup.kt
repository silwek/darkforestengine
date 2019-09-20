package com.nekotoneko.redmoon.darkforestengine.scenario

/**
 * @author Silwèk on 2019-08-13
 */

interface Group {
    var nodes: List<Node>
}

open class NodeGroup(
    id: String,
    conditions: String? = null,
    consequences: String? = null,
    nextId: String? = null,
    override var nodes: List<Node>
) : Node(id, conditions, consequences, nextId), Group