package ch.leadrian.samp.kamp.cidl.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class ParameterTest {

    @Nested
    inner class HasAttributeTests {

        @Test
        fun givenParameterHasAttributeItShouldReturnTrue() {
            val parameter = Parameter(
                    type = "int",
                    name = "playerid",
                    attributes = listOf(Attribute(
                            name = "out"
                    ))
            )

            val hasAttribute = parameter.hasAttribute("out")

            assertThat(hasAttribute)
                    .isTrue()
        }

        @Test
        fun givenParameterDoesNotHaveAttributeItShouldReturnFalse() {
            val parameter = Parameter(
                    type = "int",
                    name = "playerid",
                    attributes = listOf(Attribute(
                            name = "lol"
                    ))
            )

            val hasAttribute = parameter.hasAttribute("out")

            assertThat(hasAttribute)
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
            val parameter = Parameter(
                    type = "int",
                    name = "playerid",
                    attributes = listOf(expectedAttribute)
            )

            val attribute = parameter.getAttribute("badret")

            assertThat(attribute)
                    .isEqualTo(expectedAttribute)
        }

        @Test
        fun givenNoAttributeWithNameItShouldReturnAttributeWithDefaultValue() {
            val parameter = Parameter(
                    type = "int",
                    name = "playerid",
                    attributes = listOf(Attribute(
                            name = "lol",
                            value = Value(
                                    type = "bool",
                                    data = "true"
                            )
                    ))
            )

            val attribute = parameter.getAttribute("badret", Value(type = "int", data = "123"))

            assertThat(attribute)
                    .isEqualTo(Attribute(
                            name = "badret",
                            value = Value(type = "int", data = "123")
                    ))
        }
    }
}