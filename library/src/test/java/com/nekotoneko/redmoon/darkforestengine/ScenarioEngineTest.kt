package com.nekotoneko.redmoon.darkforestengine

import com.nekotoneko.redmoon.darkforestengine.conditions.ConditionsResolver
import com.nekotoneko.redmoon.darkforestengine.consequences.ConsequencesResolver
import com.nekotoneko.redmoon.darkforestengine.keys.Key
import com.nekotoneko.redmoon.darkforestengine.keys.KeyStore
import com.nekotoneko.redmoon.darkforestengine.scenario.Node
import com.nekotoneko.redmoon.darkforestengine.scenario.NodeGroup
import com.nekotoneko.redmoon.darkforestengine.scenario.ScenarioLoader
import com.nekotoneko.redmoon.darkforestengine.scenario.ScenarioViewer
import com.nhaarman.mockitokotlin2.*
import io.kotlintest.shouldThrow
import org.junit.Test

/**
 * @author Silwèk on 2019-08-13
 */
class ScenarioEngineTest {

    var keyStore: KeyStore = mock()
    var conditionsResolver: ConditionsResolver = mock()
    var consequencesResolver: ConsequencesResolver = mock()
    var scenarioLoader: ScenarioLoader = mock()
    var scenarioViewer: ScenarioViewer = mock()

    private fun buildEngine(scenarioId: String = "scenario0"): ScenarioEngine {
        keyStore = mock()
        conditionsResolver = mock()
        consequencesResolver = mock()
        scenarioLoader = mock()
        scenarioViewer = mock()
        return ScenarioEngine(
            scenarioId,
            keyStore = keyStore,
            conditionsResolver = conditionsResolver,
            consequencesResolver = consequencesResolver,
            scenarioLoader = scenarioLoader,
            scenarioViewer = scenarioViewer
        )
    }

    @Test
    fun startScenario() {
        val engine = buildEngine("scenarioId")
        engine.startScenario()
        verify(scenarioLoader).loadScenario(eq("scenarioId"), any())
    }

    @Test
    fun startScenario_loadSuccess() {
        val engine = spy(buildEngine())
        val nodes = listOf<Node>()
        val keys = listOf<Key>()
        whenever(scenarioLoader.loadScenario(any(), any())).then {
            val callback: (List<Node>, List<Key>) -> Unit = it.arguments[1] as (List<Node>, List<Key>) -> Unit
            callback(nodes, keys)
        }
        engine.startScenario()
        verify(engine).startScenario(nodes, keys)
    }

    @Test
    fun startScenario_displayFirstNode() {
        val engine = buildEngine()
        val firstNode = Node("node1", conditions = "", consequences = "", nextId = "")
        engine.startScenario(listOf(firstNode), listOf())
        verify(scenarioViewer).display(firstNode)
    }

    @Test
    fun startScenario_displayResuming() {
        val engine = buildEngine()
        val nodes = getTestCaseSimpleTwoNodes()
        val expectedNextNode = nodes[1]
        whenever(keyStore.getCurrentNode()).thenReturn(expectedNextNode.id)
        engine.startScenario(nodes, listOf())
        verify(scenarioViewer).display(expectedNextNode)
    }

    @Test
    fun startScenario_displayEmpty() {
        val engine = buildEngine()
        engine.startScenario(listOf(), listOf())
        verify(scenarioViewer).displayEnd()
    }

    @Test
    fun next_notStartedThrowError() {
        val engine = buildEngine()
        shouldThrow<IllegalStateException> { engine.next() }
    }

    @Test
    fun next_useCurrentNode() {
        val engine = spy(buildEngine())
        val currentNode = Node("node1", conditions = "", consequences = "", nextId = "")
        engine.startScenario(listOf(currentNode), listOf())
        engine.next()
        verify(engine).next(currentNode)
    }

    @Test
    fun next_node_noNext() {
        val engine = buildEngine()
        val node = Node("node1", conditions = "", consequences = "", nextId = "")
        engine.next(node)
        verify(scenarioViewer).displayEnd()
    }

    @Test
    fun next_node_hasConsequences() {
        val engine = buildEngine()
        val node = Node("node1", conditions = "", consequences = "aconsequence", nextId = "")
        engine.next(node)
        verify(consequencesResolver).resolve("aconsequence")
    }

    @Test
    fun next_node_simpleNext() {
        val engine = buildEngine()
        val nodes = getTestCaseSimpleTwoNodes()
        engine.startScenario(nodes, listOf())
        val currentNode = nodes[0]
        val expectedNextNode = nodes[1]
        println("expectedNextNode = $expectedNextNode")
        engine.next(currentNode)
        verify(scenarioViewer).display(expectedNextNode)
    }

    private fun getTestCaseSimpleTwoNodes(): List<Node> {
        return listOf(
            Node("node1", conditions = "", consequences = "", nextId = "node2"),
            Node("node2", conditions = "", consequences = "", nextId = "")
        )
    }

    @Test
    fun next_node_simpleNextQCM() {
        val engine = buildEngine()
        val nodes = getTestCaseSimpleQCM()
        engine.startScenario(nodes, listOf())
        val currentNode = nodes[0]
        val expectedNextNode: NodeGroup = nodes[1] as NodeGroup
        val expectedNextNodeChoices = expectedNextNode.nodes
        println("expectedNextNode = $expectedNextNode")
        engine.next(currentNode)
        verify(scenarioViewer).display(expectedNextNode, expectedNextNodeChoices)
    }

    private fun getTestCaseSimpleQCM(): List<Node> {
        return listOf(
            Node("node1", conditions = "", consequences = "", nextId = "node2"),
            NodeGroup(
                "node2", conditions = "", consequences = "", nextId = "",
                nodes = listOf(
                    Node("nodeA", conditions = "", consequences = "", nextId = ""),
                    Node("nodeB", conditions = "", consequences = "", nextId = ""),
                    Node("nodeC", conditions = "", consequences = "", nextId = "")
                )
            )
        )
    }

    @Test
    fun next_node_path() {
        val engine = buildEngine()
        val nodes = getTestCasePath()
        engine.startScenario(nodes, listOf())
        val currentNode = nodes[0]
        val expectedNextNode: NodeGroup = nodes[1] as NodeGroup
        val expectedNextNodeChoice = listOf(expectedNextNode.nodes[1])
        println("expectedNextNode = $expectedNextNode")
        whenever(conditionsResolver.resolve(eq("condition1"), any())).thenReturn(false)
        whenever(conditionsResolver.resolve(eq("condition2"), any())).thenReturn(true)
        whenever(conditionsResolver.resolve(eq("condition3"), any())).thenReturn(false)
        engine.next(currentNode)
        verify(scenarioViewer).display(expectedNextNode, expectedNextNodeChoice)
    }

    private fun getTestCasePath(): List<Node> {
        return listOf(
            Node("node1", conditions = "", consequences = "", nextId = "node2"),
            NodeGroup(
                "node2", conditions = "", consequences = "", nextId = "",
                nodes = listOf(
                    Node("nodeA", conditions = "condition1", consequences = "", nextId = ""),
                    Node("nodeB", conditions = "condition2", consequences = "", nextId = ""),
                    Node("nodeC", conditions = "condition3", consequences = "", nextId = "")
                )
            )
        )
    }
}