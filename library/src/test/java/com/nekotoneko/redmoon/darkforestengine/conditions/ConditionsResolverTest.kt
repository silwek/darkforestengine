package com.nekotoneko.redmoon.darkforestengine.conditions

import com.nekotoneko.redmoon.darkforestengine.keys.KeyBoolean
import com.nekotoneko.redmoon.darkforestengine.keys.KeyInt
import org.junit.Assert
import org.junit.Test

/**
 * @author Silwèk on 2019-08-13
 */
class ConditionsResolverTest {

    @Test
    fun resolve_integrationTest() {
        val conditionsResolver = ConditionsResolver()
        conditionsResolver.addResolver(DefaultConditionResolver())
        val progress = getProgress()

        Assert.assertEquals(true, conditionsResolver.resolve("wisdom>5", progress))
        Assert.assertEquals(false, conditionsResolver.resolve("wisdom>5&eloquence>11", progress))
        Assert.assertEquals(
            true,
            conditionsResolver.resolve(
                "potionQty>0&(wisdom>5&(eloquence>11|hasMightySwordOfTheChosenOne=true))",
                progress
            )
        )
        Assert.assertEquals(
            true,
            conditionsResolver.resolve(
                "(potionQty>0&wisdom>5)|(eloquence>11|hasMightySwordOfTheChosenOne=false)",
                progress
            )
        )
        Assert.assertEquals(
            true,
            conditionsResolver.resolve(
                "hasMightySwordOfTheChosenOne=false&wisdom=7|eloquence=10",
                progress
            )
        )
        Assert.assertEquals(
            false,
            conditionsResolver.resolve(
                "hasMightySwordOfTheChosenOne=false&(wisdom=7|eloquence=10)",
                progress
            )
        )
    }


    private fun getProgress() = listOf(
        KeyInt("wisdom", value = 7, defaultValue = 3),
        KeyInt("eloquence", value = 10, defaultValue = 5),
        KeyBoolean(
            "hasMightySwordOfTheChosenOne",
            value = true,
            defaultValue = false
        ),
        KeyInt("potionQty", value = null, defaultValue = 3)
    ).associateBy({ it.id }, { it })
}