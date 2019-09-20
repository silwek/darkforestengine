package com.nekotoneko.redmoon.darkforestengine

import com.nekotoneko.redmoon.darkforestengine.conditions.ConditionsResolver
import com.nekotoneko.redmoon.darkforestengine.consequences.ConsequencesResolver
import com.nekotoneko.redmoon.darkforestengine.keys.Key
import com.nekotoneko.redmoon.darkforestengine.keys.KeyStore
import com.nekotoneko.redmoon.darkforestengine.scenario.Group
import com.nekotoneko.redmoon.darkforestengine.scenario.Node
import com.nekotoneko.redmoon.darkforestengine.scenario.ScenarioLoader
import com.nekotoneko.redmoon.darkforestengine.scenario.ScenarioViewer

/**
 * @author Silwèk on 2019-08-12
 */
class ScenarioEngine(
    private val scenarioId: String,
    private val scenarioLoader: ScenarioLoader,
    private val keyStore: KeyStore,
    private val scenarioViewer: ScenarioViewer,
    private val conditionsResolver: ConditionsResolver,
    private val consequencesResolver: ConsequencesResolver
) {
    private var currentNode: Node? = null
    private var nodes: Map<String, Node>? = null

    fun startScenario() {
        scenarioLoader.loadScenario(scenarioId) { nodes, keys ->
            startScenario(nodes, keys)
        }
    }

    fun startScenario(nodes: List<Node>, keys: List<Key>) {
        keyStore.initKeys(keys)
        this.nodes = nodes.associateBy({ it.id }, { it })
        val resumingId = keyStore.getCurrentNode()
        resumingId?.let {
            currentNode = this.nodes?.get(resumingId)
        }
        if (currentNode == null) {
            currentNode = nodes.getOrNull(0)
            keyStore.clearProgress()
        }

        if (currentNode == null)
            displayEnd()
        else
            currentNode?.let { displayValid(it) }
    }

    fun next() {
        if (currentNode == null) {
            throw IllegalStateException("next() called before startScenario() finish. Make sure to call startScenario() to display first node before calling next()")
        }
        currentNode?.let {
            next(it)
        }
    }

    fun next(node: Node) {
        node.consequences?.let { consequences ->
            consequencesResolver.resolve(consequences)
        }
        val nextId = node.nextId
        if (nextId.isNullOrBlank()) {
            displayEnd()
        } else {
            nodes?.let { nodes ->
                if (nodes.contains(nextId)) {
                    displayValid(nodes[nextId])
                } else {
                    throw EngineError("missing next node $nextId")
                }
            }
        }
    }

    private fun displayValid(node: Node?) {
        if (node == null) {
            displayEnd()
            return
        }
        val progress = keyStore.getProgress()
        if (node.conditions?.isNotBlank() == true) {
            val isValid = conditionsResolver.resolve(node.conditions ?: "", progress)
            if (!isValid) {
                displayEnd()
                return
            }
        }
        if (node is Group) {
            val choices: List<Node> = node.nodes.filter { choice ->
                return@filter if (choice.conditions?.isNotBlank() == true) {
                    val result = conditionsResolver.resolve(choice.conditions ?: "", progress)
                    result
                } else
                    true
            }
            display(node, choices)
        } else {
            display(node)
        }
    }


    private fun display(node: Node) {
        currentNode = node
        keyStore.saveCurrentNode(nodeId = node.id)
        scenarioViewer.display(node)
    }

    private fun display(node: Node, choices: List<Node>) {
        currentNode = node
        keyStore.saveCurrentNode(nodeId = node.id)
        scenarioViewer.display(node, choices)
    }

    private fun displayEnd() {
        currentNode = null
        keyStore.saveCurrentNode(nodeId = null)
        keyStore.setEnded()
        scenarioViewer.displayEnd()
    }

    class EngineError(message: String) : Throwable(message)
}