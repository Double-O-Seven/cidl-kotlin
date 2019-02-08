package ch.leadrian.samp.kamp.cidl.antlr.visitor

import ch.leadrian.samp.cidl.antlr.CIDLBaseVisitor
import ch.leadrian.samp.cidl.antlr.CIDLParser
import ch.leadrian.samp.kamp.cidl.exception.DuplicateConstantDeclarationException
import ch.leadrian.samp.kamp.cidl.exception.TypeMismatchException
import ch.leadrian.samp.kamp.cidl.model.Constant
import ch.leadrian.samp.kamp.cidl.model.Value

class ConstantDeclarationVisitor(
        private val knownConstantsRegistry: KnownConstantsRegistry,
        private val constantExpressionVisitor: ConstantExpressionVisitor
) : CIDLBaseVisitor<Constant>() {

    override fun visitConstantDeclaration(ctx: CIDLParser.ConstantDeclarationContext): Constant {
        val value = constantExpressionVisitor.visit(ctx.constantExpression())
        val type = ctx.typeName().IDENT().text
        checkType(type, value, ctx)
        val constantName = ctx.constantName().IDENT().text
        checkDuplicateDeclaration(constantName, ctx)
        val constant = Constant(
                type = type,
                name = constantName,
                value = value
        )
        knownConstantsRegistry.register(constant)
        return constant
    }

    private fun checkType(expectedType: String, value: Value, ctx: CIDLParser.ConstantDeclarationContext) {
        if (expectedType != value.type) {
            throw TypeMismatchException(
                    expectedType = expectedType,
                    foundType = value.type,
                    expression = ctx.text
            )
        }
    }

    private fun checkDuplicateDeclaration(name: String, ctx: CIDLParser.ConstantDeclarationContext) {
        if (knownConstantsRegistry.isKnown(name)) {
            throw DuplicateConstantDeclarationException(name, ctx.text)
        }
    }
}