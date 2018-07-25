package ch.leadrian.samp.cidl.parser

import ch.leadrian.samp.cidl.CIDLLexer
import ch.leadrian.samp.cidl.CIDLParser
import ch.leadrian.samp.cidl.model.Declarations
import ch.leadrian.samp.cidl.visitor.*
import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CommonTokenStream
import java.io.InputStream

class DeclarationsParser {

    fun parse(inputStream: InputStream): Declarations {
        val declarationsVisitor = getDeclarationsVisitor()
        val cidlParser = ANTLRInputStream(inputStream)
                .let { CIDLLexer(it) }
                .let { CommonTokenStream(it) }
                .let { CIDLParser(it) }
        return declarationsVisitor.visit(cidlParser.declarations())
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