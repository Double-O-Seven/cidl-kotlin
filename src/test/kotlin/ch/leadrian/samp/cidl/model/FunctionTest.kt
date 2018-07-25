package ch.leadrian.samp.cidl.model

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class FunctionTest {
    @Nested
    inner class HasAttributeTests {

        @Test
        fun givenFunctionHasAttributeItShouldReturnTrue() {
            val function = Function(
                    type = "int",
                    name = "GetPlayerName",
                    parameters = emptyList(),
                    attributes = listOf(Attribute(
                            name = "out"
                    ))
            )

            val hasAttribute = function.hasAttribute("out")

            Assertions.assertThat(hasAttribute)
                    .isTrue()
        }

        @Test
        fun givenFunctionDoesNotHaveAttributeItShouldReturnFalse() {
            val function = Function(
                    type = "int",
                    name = "GetPlayerName",
                    parameters = emptyList(),
                    attributes = listOf(Attribute(
                            name = "lol"
                    ))
            )

            val hasAttribute = function.hasAttribute("out")

            Assertions.assertThat(hasAttribute)
                    .isFalse()
        }
    }

    @Nested
    inner class GetAttributeTests {

        @Test
        fun shouldReturnAttribute() {
            val expectedAttribute = Attribute(
                    name = "badret",
                    value = Value(
                            type = "bool",
                            data = "true"
                    )
            )
            val function = Function(
                    type = "int",
                    name = "GetPlayerName",
                    parameters = emptyList(),
                    attributes = listOf(expectedAttribute)
            )

            val attribute = function.getAttribute("badret")

            Assertions.assertThat(attribute)
                    .isEqualTo(expectedAttribute)
        }

        @Test
        fun givenNoAttributeWithNameItShouldReturnAttributeWithDefaultValue() {
            val function = Function(
                    type = "int",
                    name = "GetPlayerName",
                    parameters = emptyList(),
                    attributes = listOf(Attribute(
                            name = "lol",
                            value = Value(
                                    type = "bool",
                                    data = "true"
                            )
                    ))
            )

            val attribute = function.getAttribute("badret", Value(type = "int", data = "123"))

            Assertions.assertThat(attribute)
                    .isEqualTo(Attribute(
                            name = "badret",
                            value = Value(type = "int", data = "123")
                    ))
        }
    }
}