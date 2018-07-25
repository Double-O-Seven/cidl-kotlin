package ch.leadrian.samp.cidl.visitor

import ch.leadrian.samp.cidl.CIDLBaseVisitor
import ch.leadrian.samp.cidl.CIDLParser
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