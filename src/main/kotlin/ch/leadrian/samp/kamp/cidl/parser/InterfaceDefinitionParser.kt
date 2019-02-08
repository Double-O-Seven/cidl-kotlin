package ch.leadrian.samp.kamp.cidl.parser

import ch.leadrian.samp.cidl.antlr.CIDLLexer
import ch.leadrian.samp.cidl.antlr.CIDLParser
import ch.leadrian.samp.kamp.cidl.antlr.visitor.AttributeVisitor
import ch.leadrian.samp.kamp.cidl.antlr.visitor.ConstantDeclarationVisitor
import ch.leadrian.samp.kamp.cidl.antlr.visitor.ConstantExpressionVisitor
import ch.leadrian.samp.kamp.cidl.antlr.visitor.DeclarationsVisitor
import ch.leadrian.samp.kamp.cidl.antlr.visitor.FunctionDeclarationVisitor
import ch.leadrian.samp.kamp.cidl.antlr.visitor.KnownConstantsRegistry
import ch.leadrian.samp.kamp.cidl.antlr.visitor.ParameterVisitor
import ch.leadrian.samp.kamp.cidl.model.Constant
import ch.leadrian.samp.kamp.cidl.model.Function
import ch.leadrian.samp.kamp.cidl.model.InterfaceDefinitionUnit
import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CommonTokenStream
import java.io.InputStream

class InterfaceDefinitionParser {

    fun parse(inputStream: InputStream): InterfaceDefinitionUnit {
        val declarationsVisitor = getDeclarationsVisitor()
        val cidlParser = ANTLRInputStream(inputStream)
                .let { CIDLLexer(it) }
                .let { CommonTokenStream(it) }
                .let { CIDLParser(it) }
        return declarationsVisitor.visit(cidlParser.declarations())
    }

    fun parse(vararg sources: InterfaceDefinitionSource): InterfaceDefinitionUnit {
        val constants: MutableList<Constant> = mutableListOf()
        val functions: MutableList<Function> = mutableListOf()
        sources.forEach { source ->
            source.getInputStream().use {
                val declarations = parse(it)
                constants += declarations.constants
                functions += declarations.functions
            }
        }
        return InterfaceDefinitionUnit(constants, functions)
    }

    private fun getDeclarationsVisitor(): DeclarationsVisitor {
        val knownConstantsRegistry = KnownConstantsRegistry()
        val constantExpressionVisitor = ConstantExpressionVisitor(knownConstantsRegistry)
        val attributeVisitor = AttributeVisitor(constantExpressionVisitor)
        return DeclarationsVisitor(
                constantDeclarationVisitor = ConstantDeclarationVisitor(
                        constantExpressionVisitor = constantExpressionVisitor,
                        knownConstantsRegistry = knownConstantsRegistry
                ),
                functionDeclarationVisitor = FunctionDeclarationVisitor(
                        attributeVisitor = attributeVisitor,
                        parameterVisitor = ParameterVisitor(
                                attributeVisitor = attributeVisitor,
                                constantExpressionVisitor = constantExpressionVisitor
                        )
                )
        )
    }

}