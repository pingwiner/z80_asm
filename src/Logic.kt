sealed class Condition {
    abstract fun ops(start: Int, offset: Int): List<Operation>

    class EqReg8(val r8: Reg8) : Condition() {
        override fun ops(start: Int, offset: Int): List<Operation> {
            if (offset <= 127) {
                return listOf(CPr8(r8), JRNZ(offset.toByte()))
            } else {
                return listOf(CPr8(r8), JPNZ((start + 4 + offset).toUShort()))
            }
        }
    }

    class NeqReg8(val r8: Reg8) : Condition() {
        override fun ops(start: Int, offset: Int): List<Operation> {
            if (offset <= 127) {
                return listOf(CPr8(r8), JRZ(offset.toByte()))
            } else {
                return listOf(CPr8(r8), JPZ((start + 4 + offset).toUShort()))
            }
        }
    }

    class GtReg8(val r8: Reg8) : Condition() {
        override fun ops(start: Int, offset: Int): List<Operation> {
            if (offset <= 125) {
                return listOf(CPr8(r8), JRZ((offset + 2).toByte()), JRC(offset.toByte()))
            } else {
                val targetAddr = (start + 7 + offset).toUShort()
                return listOf(CPr8(r8), JPZ(targetAddr), JPC(targetAddr))
            }
        }
    }

    class LtReg8(val r8: Reg8) : Condition() {
        override fun ops(start: Int, offset: Int): List<Operation> {
            if (offset <= 127) {
                return listOf(CPr8(r8), JRNC(offset.toByte()))
            } else {
                val targetAddr = (start + 4 + offset).toUShort()
                return listOf(CPr8(r8), JPNC(targetAddr))
            }
        }
    }

}

infix fun Reg8.A.eq(r8: Reg8): Condition {
    return Condition.EqReg8(r8)
}

infix fun Reg8.A.neq(r8: Reg8): Condition {
    return Condition.NeqReg8(r8)
}

infix fun Reg8.A.gt(r8: Reg8): Condition {
    return Condition.GtReg8(r8)
}

infix fun Reg8.A.lt(r8: Reg8): Condition {
    return Condition.LtReg8(r8)
}
