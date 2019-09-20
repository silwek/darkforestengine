package com.nekotoneko.redmoon.darkforestengine.scenario

/**
 * @author Silwèk on 2019-08-13
 */
open class Node(
    var id: String,
    var conditions: String? = null,
    var consequences: String? = null,
    var nextId: String? = null
) {
    override fun equals(other: Any?): Boolean {
        return other is Node && other.id == this.id
    }

    override fun toString(): String {
        return "Node(id='$id', conditions=$conditions, consequences=$consequences, nextId=$nextId)"
    }


}