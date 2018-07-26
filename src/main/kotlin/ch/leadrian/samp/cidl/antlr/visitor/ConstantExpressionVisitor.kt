package ch.leadrian.samp.cidl.antlr.visitor

import ch.leadrian.samp.cidl.antlr.CIDLBaseVisitor
import ch.leadrian.samp.cidl.antlr.CIDLParser
import ch.leadrian.samp.cidl.exception.UnknownConstantReferenceException
import ch.leadrian.samp.cidl.model.Types
import ch.leadrian.samp.cidl.model.Value

class ConstantExpressionVisitor(
        private val knownConstantsRegistry: KnownConstantsRegistry
) : CIDLBaseVisitor<Value>() {

    override fun visitBool(ctx: CIDLParser.BoolContext): Value {
        return Value(type = Types.BOOL, data = ctx.text)
    }

    override fun visitString(ctx: CIDLParser.StringContext): Value {
        return Value(type = Types.STRING, data = ctx.STRING().text)
    }

    override fun visitInteger(ctx: CIDLParser.IntegerContext): Value {
        return Value(type = Types.INT, data = ctx.INT().text)
    }

    override fun visitCharacter(ctx: CIDLParser.CharacterContext): Value {
        return Value(type = Types.CHAR, data = ctx.text)
    }

    override fun visitOctal(ctx: CIDLParser.OctalContext): Value {
        return Value(type = Types.INT, data = ctx.OCT().text)
    }

    override fun visitHexadecimal(ctx: CIDLParser.HexadecimalContext): Value {
        return Value(type = Types.INT, data = ctx.HEX().text)
    }

    override fun visitDecimal(ctx: CIDLParser.DecimalContext): Value {
        return Value(type = Types.FLOAT, data = ctx.DECIMAL().text)
    }

    override fun visitConstantReference(ctx: CIDLParser.ConstantReferenceContext): Value {
        val constantName = ctx.IDENT().text
        val constant = knownConstantsRegistry[constantName]
                ?: throw UnknownConstantReferenceException(constantName, ctx.text)
        return Value(type = constant.type, data = constantName)
    }

}