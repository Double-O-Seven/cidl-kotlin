package ch.leadrian.samp.cidl.antlr.visitor

import ch.leadrian.samp.cidl.antlr.CIDLBaseVisitor
import ch.leadrian.samp.cidl.antlr.CIDLParser
import ch.leadrian.samp.cidl.model.Attribute

class AttributeVisitor(
        private val constantExpressionVisitor: ConstantExpressionVisitor
) : CIDLBaseVisitor<Attribute>() {

    override fun visitAttribute(ctx: CIDLParser.AttributeContext): Attribute {
        return Attribute(
                name = ctx.attributeName().IDENT().text,
                value = ctx.constantExpression()?.let { constantExpressionVisitor.visit(it) }
        )
    }
}