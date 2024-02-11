interface Generator {
    fun ops(start: Int, offset: Int): List<Operation>
}

sealed class Condition<T : Operation>(val cmpOp: T) : Generator {

    open class EqCondition<T : Operation>(cmpOp: T) : Condition<T>(cmpOp) {
        override fun ops(start: Int, offset: Int): List<Operation> {
            if (offset <= 127) {
                return listOf(cmpOp, JRNZ(offset.toByte()))
            } else {
                val targetAddr = (start + 3 + cmpOp.size + offset).toUShort()
                return listOf(cmpOp, JPNZ(targetAddr))
            }
        }
    }

    open class NeqCondition<T : Operation>(cmpOp: T) : Condition<T>(cmpOp) {
        override fun ops(start: Int, offset: Int): List<Operation> {
            if (offset <= 127) {
                return listOf(cmpOp, JRZ(offset.toByte()))
            } else {
                val targetAddr = (start + 3 + cmpOp.size + offset).toUShort()
                return listOf(cmpOp, JPZ(targetAddr))
            }
        }
    }

    open class GtCondition<T : Operation>(cmpOp: T) : Condition<T>(cmpOp) {
        override fun ops(start: Int, offset: Int): List<Operation> {
            if (offset <= 125) {
                return listOf(cmpOp, JRZ((offset + 2).toByte()), JRC(offset.toByte()))
            } else {
                val targetAddr = (start + 3 * 2 + cmpOp.size + offset).toUShort()
                return listOf(cmpOp, JPZ(targetAddr), JPC(targetAddr))
            }
        }
    }

    open class GteCondition<T : Operation>(cmpOp: T) : Condition<T>(cmpOp) {
        override fun ops(start: Int, offset: Int): List<Operation> {
            if (offset <= 127) {
                return listOf(cmpOp, JRC(offset.toByte()))
            } else {
                val targetAddr = (start + 3 + cmpOp.size + offset).toUShort()
                return listOf(cmpOp, JPC(targetAddr))
            }
        }
    }

    open class LtCondition<T : Operation>(cmpOp: T) : Condition<T>(cmpOp) {
        override fun ops(start: Int, offset: Int): List<Operation> {
            if (offset <= 127) {
                return listOf(cmpOp, JRNC(offset.toByte()))
            } else {
                val targetAddr = (start + 3 + cmpOp.size + offset).toUShort()
                return listOf(cmpOp, JPNC(targetAddr))
            }
        }
    }

    // CP A, Reg8
    class EqReg8(r8: Reg8) : EqCondition<CPr8>(CPr8(r8))
    class NeqReg8(r8: Reg8) : NeqCondition<CPr8>(CPr8(r8))
    class GtReg8(r8: Reg8) : GtCondition<CPr8>(CPr8(r8))
    class GteReg8(r8: Reg8) : GteCondition<CPr8>(CPr8(r8))
    class LtReg8(r8: Reg8) : LtCondition<CPr8>(CPr8(r8))


    // CP A, N8

    class EqN8(val n: UByte) : EqCondition<CPn8>(CPn8(n))
    class NeqN8(val n: UByte) : NeqCondition<CPn8>(CPn8(n))
    class GtN8(val n: UByte) : GtCondition<CPn8>(CPn8(n))
    class GteN8(val n: UByte) : GteCondition<CPn8>(CPn8(n))
    class LtN8(val n: UByte) : LtCondition<CPn8>(CPn8(n))


    // CP A, [HL]

    class EqHL : EqCondition<CPHL>(CPHL())
    class NeqHL : NeqCondition<CPHL>(CPHL())
    class GtHL : GtCondition<CPHL>(CPHL())
    class GteHL : GteCondition<CPHL>(CPHL())
    class LtHL : LtCondition<CPHL>(CPHL())


    //CP A, [IX + d]
    //CP A, [IY + d]

    class EqIXY(indX: IndX) : EqCondition<CPxx>(CPxx(indX))
    class NeqIXY(indX: IndX) : NeqCondition<CPxx>(CPxx(indX))
    class GtIXY(indX: IndX) : GtCondition<CPxx>(CPxx(indX))
    class GteIXY(indX: IndX) : GteCondition<CPxx>(CPxx(indX))
    class LtIXY(indX: IndX) : LtCondition<CPxx>(CPxx(indX))
}

infix fun Reg8.A.eq(r8: Reg8): Condition<CPr8> {
    return Condition.EqReg8(r8)
}

infix fun Reg8.A.neq(r8: Reg8): Condition<CPr8> {
    return Condition.NeqReg8(r8)
}

infix fun Reg8.A.gt(r8: Reg8): Condition<CPr8> {
    return Condition.GtReg8(r8)
}

infix fun Reg8.A.lt(r8: Reg8): Condition<CPr8> {
    return Condition.LtReg8(r8)
}

infix fun Reg8.A.gte(r8: Reg8): Condition<CPr8> {
    return Condition.GteReg8(r8)
}


infix fun Reg8.A.eq(n8: UByte): Condition<CPn8> {
    return Condition.EqN8(n8)
}

infix fun Reg8.A.neq(n8: UByte): Condition<CPn8> {
    return Condition.NeqN8(n8)
}

infix fun Reg8.A.gt(n8: UByte): Condition<CPn8> {
    return Condition.GtN8(n8)
}

infix fun Reg8.A.lt(n8: UByte): Condition<CPn8> {
    return Condition.LtN8(n8)
}

infix fun Reg8.A.gte(n8: UByte): Condition<CPn8> {
    return Condition.GteN8(n8)
}


//CP A, [HL]

infix fun Reg8.A.eq(hl: Ind.HL): Condition<CPHL> {
    return Condition.EqHL()
}

infix fun Reg8.A.neq(hl: Ind.HL): Condition<CPHL> {
    return Condition.NeqHL()
}

infix fun Reg8.A.gt(hl: Ind.HL): Condition<CPHL> {
    return Condition.GtHL()
}

infix fun Reg8.A.lt(hl: Ind.HL): Condition<CPHL> {
    return Condition.LtHL()
}

infix fun Reg8.A.gte(hl: Ind.HL): Condition<CPHL> {
    return Condition.GteHL()
}

//CP A, [IX + d]

infix fun Reg8.A.eq(indX: IndX): Condition<CPxx> {
    return Condition.EqIXY(indX)
}

infix fun Reg8.A.neq(indX: IndX): Condition<CPxx> {
    return Condition.NeqIXY(indX)
}

infix fun Reg8.A.gt(indX: IndX): Condition<CPxx> {
    return Condition.GtIXY(indX)
}

infix fun Reg8.A.lt(indX: IndX): Condition<CPxx> {
    return Condition.LtIXY(indX)
}

infix fun Reg8.A.gte(indX: IndX): Condition<CPxx> {
    return Condition.GteIXY(indX)
}
