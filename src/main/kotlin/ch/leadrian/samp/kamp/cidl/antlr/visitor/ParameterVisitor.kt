package ch.leadrian.samp.kamp.cidl.antlr.visitor

import ch.leadrian.samp.cidl.antlr.CIDLBaseVisitor
import ch.leadrian.samp.cidl.antlr.CIDLParser
import ch.leadrian.samp.kamp.cidl.exception.TypeMismatchException
import ch.leadrian.samp.kamp.cidl.model.Parameter
import ch.leadrian.samp.kamp.cidl.model.Value

class ParameterVisitor(
        private val attributeVisitor: AttributeVisitor,
        private val constantExpressionVisitor: ConstantExpressionVisitor
) : CIDLBaseVisitor<Parameter>() {

    override fun visitParameter(ctx: CIDLParser.ParameterContext): Parameter {
        val expectedType = ctx.typeName().IDENT().text
        val defaultValue = visitConstantExpression(ctx, expectedType)
        val attributes = visitAttributes(ctx)
        return Parameter(
                type = expectedType,
                name = ctx.parameterName().IDENT().text,
                defaultValue = defaultValue,
                attributes = attributes
        )
    }

    private fun visitConstantExpression(ctx: CIDLParser.ParameterContext, expectedType: String): Value? =
            ctx
                    .constantExpression()
                    ?.let { constantExpressionVisitor.visit(it) }
                    ?.apply { checkType(expectedType, this, ctx) }

    private fun visitAttributes(ctx: CIDLParser.ParameterContext) =
            ctx
                    .attributes()
                    ?.attributeList()
                    ?.attribute()
                    ?.map { attributeVisitor.visit(it) }
                    .orEmpty()

    private fun checkType(expectedType: String, value: Value, ctx: CIDLParser.ParameterContext) {
        if (expectedType != value.type) {
            throw TypeMismatchException(
                    expectedType = expectedType,
                    foundType = value.type,
                    expression = ctx.text
            )
        }
    }
}