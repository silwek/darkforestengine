package com.nekotoneko.redmoon.darkforestengine.conditions

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsInstanceOf.instanceOf
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * @author Silwèk on 2019-08-17
 */
class DefaultConditionResolverTest {

    @Test
    fun getCondition_greaterThan() {
        val conditionResolver = DefaultConditionResolver()
        assertThat(conditionResolver.getCondition("eloquence>8"), instanceOf(GreaterThanCondition::class.java))
    }

    @Test
    fun getCondition_lowerThan() {
        val conditionResolver = DefaultConditionResolver()
        assertThat(conditionResolver.getCondition("potionQty<4"), instanceOf(LowerThanCondition::class.java))
    }

    @Test
    fun getCondition_equalsBoolean() {
        val conditionResolver = DefaultConditionResolver()
        assertThat(
            conditionResolver.getCondition("hasMightySwordOfTheChosenOne=false"),
            instanceOf(EqualsToCondition::class.java)
        )
        assertThat(
            conditionResolver.getCondition("hasMightySwordOfTheChosenOne=true"),
            instanceOf(EqualsToCondition::class.java)
        )
    }

    @Test
    fun getCondition_unknown() {
        val conditionResolver = DefaultConditionResolver()
        assertEquals(null, conditionResolver.getCondition("azearazrazr*azeaze"))
    }
}