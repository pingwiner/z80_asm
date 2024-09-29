interface Generator {
    fun ops(start: Int, offset: Int): List<Operation>
}

sealed class Condition<T : Operation>(val cmpOp: T) : Generator {

    open class EqCondition<T : Operation>(cmpOp: T) : Condition<T>(cmpOp) {
        override fun ops(start: Int, offset: Int): List<Operation> {
            if (offset <= 127) {
                return listOf(cmpOp, JR_NZ(offset.toByte()))
            } else {
                val targetAddr = (start + 3 + cmpOp.size + offset).toUShort()
                return listOf(cmpOp, JP_NZ(targetAddr))
            }
        }
    }

    open class NeqCondition<T : Operation>(cmpOp: T) : Condition<T>(cmpOp) {
        override fun ops(start: Int, offset: Int): List<Operation> {
            if (offset <= 127) {
                return listOf(cmpOp, JR_Z(offset.toByte()))
            } else {
                val targetAddr = (start + 3 + cmpOp.size + offset).toUShort()
                return listOf(cmpOp, JP_Z(targetAddr))
            }
        }
    }

    open class GtCondition<T : Operation>(cmpOp: T) : Condition<T>(cmpOp) {
        override fun ops(start: Int, offset: Int): List<Operation> {
            if (offset <= 125) {
                return listOf(cmpOp, JR_Z((offset + 2).toByte()), JR_C(offset.toByte()))
            } else {
                val targetAddr = (start + 3 * 2 + cmpOp.size + offset).toUShort()
                return listOf(cmpOp, JP_Z(targetAddr), JP_C(targetAddr))
            }
        }
    }

    open class GteCondition<T : Operation>(cmpOp: T) : Condition<T>(cmpOp) {
        override fun ops(start: Int, offset: Int): List<Operation> {
            if (offset <= 127) {
                return listOf(cmpOp, JR_C(offset.toByte()))
            } else {
                val targetAddr = (start + 3 + cmpOp.size + offset).toUShort()
                return listOf(cmpOp, JP_C(targetAddr))
            }
        }
    }

    open class LtCondition<T : Operation>(cmpOp: T) : Condition<T>(cmpOp) {
        override fun ops(start: Int, offset: Int): List<Operation> {
            if (offset <= 127) {
                return listOf(cmpOp, JR_NC(offset.toByte()))
            } else {
                val targetAddr = (start + 3 + cmpOp.size + offset).toUShort()
                return listOf(cmpOp, JP_NC(targetAddr))
            }
        }
    }

    // CP A, Reg8
    class EqReg8(r8: Reg8) : EqCondition<CP_r8>(CP_r8(r8))
    class NeqReg8(r8: Reg8) : NeqCondition<CP_r8>(CP_r8(r8))
    class GtReg8(r8: Reg8) : GtCondition<CP_r8>(CP_r8(r8))
    class GteReg8(r8: Reg8) : GteCondition<CP_r8>(CP_r8(r8))
    class LtReg8(r8: Reg8) : LtCondition<CP_r8>(CP_r8(r8))


    // CP A, N8

    class EqN8(val n: UByte) : EqCondition<CP_n8>(CP_n8(n))
    class NeqN8(val n: UByte) : NeqCondition<CP_n8>(CP_n8(n))
    class GtN8(val n: UByte) : GtCondition<CP_n8>(CP_n8(n))
    class GteN8(val n: UByte) : GteCondition<CP_n8>(CP_n8(n))
    class LtN8(val n: UByte) : LtCondition<CP_n8>(CP_n8(n))


    // CP A, [HL]

    class EqHL : EqCondition<CP_HL>(CP_HL())
    class NeqHL : NeqCondition<CP_HL>(CP_HL())
    class GtHL : GtCondition<CP_HL>(CP_HL())
    class GteHL : GteCondition<CP_HL>(CP_HL())
    class LtHL : LtCondition<CP_HL>(CP_HL())


    //CP A, [IX + d]
    //CP A, [IY + d]

    class EqIXY(indX: IndX) : EqCondition<CP_xx>(CP_xx(indX))
    class NeqIXY(indX: IndX) : NeqCondition<CP_xx>(CP_xx(indX))
    class GtIXY(indX: IndX) : GtCondition<CP_xx>(CP_xx(indX))
    class GteIXY(indX: IndX) : GteCondition<CP_xx>(CP_xx(indX))
    class LtIXY(indX: IndX) : LtCondition<CP_xx>(CP_xx(indX))
}

infix fun Reg8.A.eq(r8: Reg8): Condition<CP_r8> {
    return Condition.EqReg8(r8)
}

infix fun Reg8.A.neq(r8: Reg8): Condition<CP_r8> {
    return Condition.NeqReg8(r8)
}

infix fun Reg8.A.gt(r8: Reg8): Condition<CP_r8> {
    return Condition.GtReg8(r8)
}

infix fun Reg8.A.lt(r8: Reg8): Condition<CP_r8> {
    return Condition.LtReg8(r8)
}

infix fun Reg8.A.gte(r8: Reg8): Condition<CP_r8> {
    return Condition.GteReg8(r8)
}


infix fun Reg8.A.eq(n8: UByte): Condition<CP_n8> {
    return Condition.EqN8(n8)
}

infix fun Reg8.A.neq(n8: UByte): Condition<CP_n8> {
    return Condition.NeqN8(n8)
}

infix fun Reg8.A.gt(n8: UByte): Condition<CP_n8> {
    return Condition.GtN8(n8)
}

infix fun Reg8.A.lt(n8: UByte): Condition<CP_n8> {
    return Condition.LtN8(n8)
}

infix fun Reg8.A.gte(n8: UByte): Condition<CP_n8> {
    return Condition.GteN8(n8)
}


//CP A, [HL]

infix fun Reg8.A.eq(hl: Ind.HL): Condition<CP_HL> {
    return Condition.EqHL()
}

infix fun Reg8.A.neq(hl: Ind.HL): Condition<CP_HL> {
    return Condition.NeqHL()
}

infix fun Reg8.A.gt(hl: Ind.HL): Condition<CP_HL> {
    return Condition.GtHL()
}

infix fun Reg8.A.lt(hl: Ind.HL): Condition<CP_HL> {
    return Condition.LtHL()
}

infix fun Reg8.A.gte(hl: Ind.HL): Condition<CP_HL> {
    return Condition.GteHL()
}

//CP A, [IX + d]

infix fun Reg8.A.eq(indX: IndX): Condition<CP_xx> {
    return Condition.EqIXY(indX)
}

infix fun Reg8.A.neq(indX: IndX): Condition<CP_xx> {
    return Condition.NeqIXY(indX)
}

infix fun Reg8.A.gt(indX: IndX): Condition<CP_xx> {
    return Condition.GtIXY(indX)
}

infix fun Reg8.A.lt(indX: IndX): Condition<CP_xx> {
    return Condition.LtIXY(indX)
}

infix fun Reg8.A.gte(indX: IndX): Condition<CP_xx> {
    return Condition.GteIXY(indX)
}
