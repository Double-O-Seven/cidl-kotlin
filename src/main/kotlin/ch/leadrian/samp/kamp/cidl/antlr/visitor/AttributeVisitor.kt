package ch.leadrian.samp.kamp.cidl.antlr.visitor

import ch.leadrian.samp.kamp.cidl.antlr.CIDLBaseVisitor
import ch.leadrian.samp.kamp.cidl.antlr.CIDLParser
import ch.leadrian.samp.kamp.cidl.model.Attribute

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