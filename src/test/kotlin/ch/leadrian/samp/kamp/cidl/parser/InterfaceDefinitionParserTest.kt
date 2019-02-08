package ch.leadrian.samp.kamp.cidl.parser

import ch.leadrian.samp.cidl.model.*
import ch.leadrian.samp.kamp.cidl.model.Attribute
import ch.leadrian.samp.kamp.cidl.model.Constant
import ch.leadrian.samp.kamp.cidl.model.Function
import ch.leadrian.samp.kamp.cidl.model.InterfaceDefinitionUnit
import ch.leadrian.samp.kamp.cidl.model.Parameter
import ch.leadrian.samp.kamp.cidl.model.Value
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import org.junit.jupiter.params.provider.ValueSource
import java.io.InputStream
import java.util.stream.Stream

internal class InterfaceDefinitionParserTest {

    @ParameterizedTest
    @ValueSource(strings = [
        "a_actor.idl",
        "a_http.idl",
        "a_objects.idl",
        "a_players.idl",
        "a_samp.idl",
        "a_vehicles.idl"
    ])
    fun shouldParseReferenceIDLs(idlResourceFileName: String) {
        getResourceAsStream(idlResourceFileName).use {
            val declarationsParser = InterfaceDefinitionParser()

            declarationsParser.parse(it)
        }
    }

    @ParameterizedTest
    @ArgumentsSource(IDLArgumentsProvider::class)
    fun shouldParseValidIDLString(idlString: String, expectedInterfaceDefinitionUnit: InterfaceDefinitionUnit) {
        idlString.byteInputStream().use {
            val declarationsParser = InterfaceDefinitionParser()

            val declarations = declarationsParser.parse(it)

            assertThat(declarations)
                    .isEqualTo(expectedInterfaceDefinitionUnit)
        }
    }

    private class IDLArgumentsProvider : ArgumentsProvider {

        override fun provideArguments(context: ExtensionContext?): Stream<IDLArguments> =
                Stream.of(
                        IDLArguments(
                                idlString = """
                                    [native] int CreateActor(int modelid, float x, float y, float z, float rotation);

                                    const int HTTP_ERROR_MALFORMED_RESPONSE = 6;

                                    [native] bool GetObjectRot(int objectid, [out] float rotX, [out] float rotY, [out] float rotZ);
                                    """.trimIndent(),
                                expectedInterfaceDefinitionUnit = InterfaceDefinitionUnit(
                                        constants = listOf(
                                                Constant(
                                                        type = "int",
                                                        name = "HTTP_ERROR_MALFORMED_RESPONSE",
                                                        value = Value(
                                                                type = "int",
                                                                data = "6"
                                                        )
                                                )
                                        ),
                                        functions = listOf(
                                                Function(
                                                        type = "int",
                                                        name = "CreateActor",
                                                        attributes = listOf(Attribute("native")),
                                                        parameters = listOf(
                                                                Parameter(
                                                                        type = "int",
                                                                        name = "modelid"
                                                                ),
                                                                Parameter(
                                                                        type = "float",
                                                                        name = "x"
                                                                ),
                                                                Parameter(
                                                                        type = "float",
                                                                        name = "y"
                                                                ),
                                                                Parameter(
                                                                        type = "float",
                                                                        name = "z"
                                                                ),
                                                                Parameter(
                                                                        type = "float",
                                                                        name = "rotation"
                                                                )
                                                        )
                                                ),
                                                Function(
                                                        type = "bool",
                                                        name = "GetObjectRot",
                                                        parameters = listOf(
                                                                Parameter(
                                                                        type = "int",
                                                                        name = "objectid"
                                                                ),
                                                                Parameter(
                                                                        type = "float",
                                                                        name = "rotX",
                                                                        attributes = listOf(Attribute(name = "out"))
                                                                ),
                                                                Parameter(
                                                                        type = "float",
                                                                        name = "rotY",
                                                                        attributes = listOf(Attribute(name = "out"))
                                                                ),
                                                                Parameter(
                                                                        type = "float",
                                                                        name = "rotZ",
                                                                        attributes = listOf(Attribute(name = "out"))
                                                                )
                                                        ),
                                                        attributes = listOf(Attribute(name = "native"))
                                                )
                                        )
                                )
                        ),
                        IDLArguments(
                                idlString = "// [native] int CreateActor(int modelid, float x, float y, float z, float rotation);",
                                expectedInterfaceDefinitionUnit = InterfaceDefinitionUnit()
                        ),
                        IDLArguments(
                                idlString = """
                                    /*
                                    const int HTTP_ERROR_MALFORMED_RESPONSE = 6;
                                    */
                                    """.trimIndent(),
                                expectedInterfaceDefinitionUnit = InterfaceDefinitionUnit()
                        ),
                        IDLArguments(
                                idlString = "// const int HTTP_ERROR_MALFORMED_RESPONSE = 6;",
                                expectedInterfaceDefinitionUnit = InterfaceDefinitionUnit()
                        ),
                        IDLArguments(
                                idlString = """
                                    /*
                                    [native] int CreateActor(int modelid, float x, float y, float z, float rotation);
                                    */
                                    """.trimIndent(),
                                expectedInterfaceDefinitionUnit = InterfaceDefinitionUnit()
                        ),
                        IDLArguments(
                                idlString = "// [native] int CreateActor(int modelid, float x, float y, float z, float rotation);",
                                expectedInterfaceDefinitionUnit = InterfaceDefinitionUnit()
                        ),
                        IDLArguments(
                                idlString = """
                                    /*
                                    [native] int CreateActor(int modelid, float x, float y, float z, float rotation);
                                    */
                                    """.trimIndent(),
                                expectedInterfaceDefinitionUnit = InterfaceDefinitionUnit()
                        ),
                        IDLArguments(
                                idlString = """
                                    // [native] bool GetObjectRot(int objectid, [out] float rotX, [out] float rotY, [out] float rotZ);
                                    [native] int CreateActor(
                                        int modelid,
                                        float x,
                                        float y,
                                        float z,
                                        float rotation
                                    );
                                    """.trimIndent(),
                                expectedInterfaceDefinitionUnit = InterfaceDefinitionUnit(
                                        functions = listOf(Function(
                                                type = "int",
                                                name = "CreateActor",
                                                attributes = listOf(Attribute("native")),
                                                parameters = listOf(
                                                        Parameter(
                                                                type = "int",
                                                                name = "modelid"
                                                        ),
                                                        Parameter(
                                                                type = "float",
                                                                name = "x"
                                                        ),
                                                        Parameter(
                                                                type = "float",
                                                                name = "y"
                                                        ),
                                                        Parameter(
                                                                type = "float",
                                                                name = "z"
                                                        ),
                                                        Parameter(
                                                                type = "float",
                                                                name = "rotation"
                                                        )
                                                )
                                        ))
                                )
                        ),
                        IDLArguments(
                                idlString = """
                                    [native] bool HTTP(int index,
                                       int type,
                                       string url,
                                       string data,
                                       [in, bind("OnHTTPResponse")] string callback);
                                    """.trimIndent(),
                                expectedInterfaceDefinitionUnit = InterfaceDefinitionUnit(
                                        functions = listOf(Function(
                                                type = "bool",
                                                name = "HTTP",
                                                attributes = listOf(Attribute("native")),
                                                parameters = listOf(
                                                        Parameter(
                                                                type = "int",
                                                                name = "index"
                                                        ),
                                                        Parameter(
                                                                type = "int",
                                                                name = "type"
                                                        ),
                                                        Parameter(
                                                                type = "string",
                                                                name = "url"
                                                        ),
                                                        Parameter(
                                                                type = "string",
                                                                name = "data"
                                                        ),
                                                        Parameter(
                                                                type = "string",
                                                                name = "callback",
                                                                attributes = listOf(
                                                                        Attribute("in"),
                                                                        Attribute(
                                                                                name = "bind",
                                                                                value = Value(
                                                                                        type = "string",
                                                                                        data = "\"OnHTTPResponse\""
                                                                                )
                                                                        )
                                                                )
                                                        )
                                                )
                                        ))
                                )
                        )
                )

    }

    private class IDLArguments(
            val idlString: String,
            val expectedInterfaceDefinitionUnit: InterfaceDefinitionUnit
    ) : Arguments {

        override fun get(): Array<Any> = arrayOf(idlString, expectedInterfaceDefinitionUnit)

    }

    private fun getResourceAsStream(resourceName: String): InputStream =
            this::class.java.getResourceAsStream(resourceName)

}