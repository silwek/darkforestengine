package com.nekotoneko.redmoon.darkforestengine.scenario

/**
 * @author Silwèk on 2019-08-13
 */
interface ScenarioViewer {
    fun display(node: Node)
    fun display(node: Node, choices: List<Node>)
    fun displayEnd()
}