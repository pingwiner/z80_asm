sealed class Condition {
    abstract fun ops(start: Int, offset: Int): List<Operation>

// CP A, Reg8

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

    class GteReg8(val r8: Reg8) : Condition() {
        override fun ops(start: Int, offset: Int): List<Operation> {
            if (offset <= 127) {
                return listOf(CPr8(r8), JRC(offset.toByte()))
            } else {
                val targetAddr = (start + 4 + offset).toUShort()
                return listOf(CPr8(r8), JPC(targetAddr))
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

// CP A, N8

    class EqN8(val n: UByte) : Condition() {
        override fun ops(start: Int, offset: Int): List<Operation> {
            if (offset <= 127) {
                return listOf(CPn8(n), JRNZ(offset.toByte()))
            } else {
                return listOf(CPn8(n), JPNZ((start + 5 + offset).toUShort()))
            }
        }
    }

    class NeqN8(val n: UByte) : Condition() {
        override fun ops(start: Int, offset: Int): List<Operation> {
            if (offset <= 127) {
                return listOf(CPn8(n), JRZ(offset.toByte()))
            } else {
                return listOf(CPn8(n), JPZ((start + 5 + offset).toUShort()))
            }
        }
    }

    class GtN8(val n: UByte) : Condition() {
        override fun ops(start: Int, offset: Int): List<Operation> {
            if (offset <= 125) {
                return listOf(CPn8(n), JRZ((offset + 2).toByte()), JRC(offset.toByte()))
            } else {
                val targetAddr = (start + 8 + offset).toUShort()
                return listOf(CPn8(n), JPZ(targetAddr), JPC(targetAddr))
            }
        }
    }

    class GteN8(val n: UByte) : Condition() {
        override fun ops(start: Int, offset: Int): List<Operation> {
            if (offset <= 127) {
                return listOf(CPn8(n), JRC(offset.toByte()))
            } else {
                val targetAddr = (start + 5 + offset).toUShort()
                return listOf(CPn8(n), JPC(targetAddr))
            }
        }
    }

    class LtN8(val n: UByte) : Condition() {
        override fun ops(start: Int, offset: Int): List<Operation> {
            if (offset <= 127) {
                return listOf(CPn8(n), JRNC(offset.toByte()))
            } else {
                val targetAddr = (start + 5 + offset).toUShort()
                return listOf(CPn8(n), JPNC(targetAddr))
            }
        }
    }

    class EqHL : Condition() {
        override fun ops(start: Int, offset: Int): List<Operation> {
            if (offset <= 127) {
                return listOf(CPHL(), JRNZ(offset.toByte()))
            } else {
                return listOf(CPHL(), JPNZ((start + 4 + offset).toUShort()))
            }
        }
    }

    class NeqHL : Condition() {
        override fun ops(start: Int, offset: Int): List<Operation> {
            if (offset <= 127) {
                return listOf(CPHL(), JRZ(offset.toByte()))
            } else {
                return listOf(CPHL(), JPZ((start + 4 + offset).toUShort()))
            }
        }
    }

    class GtHL : Condition() {
        override fun ops(start: Int, offset: Int): List<Operation> {
            if (offset <= 125) {
                return listOf(CPHL(), JRZ((offset + 2).toByte()), JRC(offset.toByte()))
            } else {
                val targetAddr = (start + 7 + offset).toUShort()
                return listOf(CPHL(), JPZ(targetAddr), JPC(targetAddr))
            }
        }
    }

    class GteHL : Condition() {
        override fun ops(start: Int, offset: Int): List<Operation> {
            if (offset <= 127) {
                return listOf(CPHL(), JRC(offset.toByte()))
            } else {
                val targetAddr = (start + 4 + offset).toUShort()
                return listOf(CPHL(), JPC(targetAddr))
            }
        }
    }

    class LtHL : Condition() {
        override fun ops(start: Int, offset: Int): List<Operation> {
            if (offset <= 127) {
                return listOf(CPHL(), JRNC(offset.toByte()))
            } else {
                val targetAddr = (start + 4 + offset).toUShort()
                return listOf(CPHL(), JPNC(targetAddr))
            }
        }
    }


    abstract class ConditionXY(val indX: IndX) : Condition() {
        val cmpOp = if (indX.r == RegI.IX) CPIX(indX.offset) else CPIY(indX.offset)
    }

    class EqIXY(indX: IndX) : ConditionXY(indX) {
        override fun ops(start: Int, offset: Int): List<Operation> {
            if (offset <= 127) {
                return listOf(cmpOp, JRNZ(offset.toByte()))
            } else {
                return listOf(cmpOp, JPNZ((start + 3 + cmpOp.size + offset).toUShort()))
            }
        }
    }

    class NeqIXY(indX: IndX) : ConditionXY(indX) {
        override fun ops(start: Int, offset: Int): List<Operation> {
            if (offset <= 127) {
                return listOf(cmpOp, JRZ(offset.toByte()))
            } else {
                return listOf(cmpOp, JPZ((start + 3 + cmpOp.size + offset).toUShort()))
            }
        }
    }

    class GtIXY(indX: IndX) : ConditionXY(indX) {
        override fun ops(start: Int, offset: Int): List<Operation> {
            if (offset <= 125) {
                return listOf(cmpOp, JRZ((offset + 2).toByte()), JRC(offset.toByte()))
            } else {
                val targetAddr = (start + 6 + cmpOp.size + offset).toUShort()
                return listOf(cmpOp, JPZ(targetAddr), JPC(targetAddr))
            }
        }
    }

    class GteIXY(indX: IndX) : ConditionXY(indX) {
        override fun ops(start: Int, offset: Int): List<Operation> {
            if (offset <= 127) {
                return listOf(cmpOp, JRC(offset.toByte()))
            } else {
                val targetAddr = (start + 3 + cmpOp.size + offset).toUShort()
                return listOf(cmpOp, JPC(targetAddr))
            }
        }
    }

    class LtIXY(indX: IndX) : ConditionXY(indX) {
        override fun ops(start: Int, offset: Int): List<Operation> {
            if (offset <= 127) {
                return listOf(cmpOp, JRNC(offset.toByte()))
            } else {
                val targetAddr = (start + 3 + cmpOp.size + offset).toUShort()
                return listOf(cmpOp, JPNC(targetAddr))
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

infix fun Reg8.A.gte(r8: Reg8): Condition {
    return Condition.GteReg8(r8)
}


infix fun Reg8.A.eq(n8: UByte): Condition {
    return Condition.EqN8(n8)
}

infix fun Reg8.A.neq(n8: UByte): Condition {
    return Condition.NeqN8(n8)
}

infix fun Reg8.A.gt(n8: UByte): Condition {
    return Condition.GtN8(n8)
}

infix fun Reg8.A.lt(n8: UByte): Condition {
    return Condition.LtN8(n8)
}

infix fun Reg8.A.gte(n8: UByte): Condition {
    return Condition.GteN8(n8)
}


//CP A, [HL]

infix fun Reg8.A.eq(hl: Ind.HL): Condition {
    return Condition.EqHL()
}

infix fun Reg8.A.neq(hl: Ind.HL): Condition {
    return Condition.NeqHL()
}

infix fun Reg8.A.gt(hl: Ind.HL): Condition {
    return Condition.GtHL()
}

infix fun Reg8.A.lt(hl: Ind.HL): Condition {
    return Condition.LtHL()
}

infix fun Reg8.A.gte(hl: Ind.HL): Condition {
    return Condition.GteHL()
}

//CP A, [IX + d]

infix fun Reg8.A.eq(indX: IndX): Condition {
    return Condition.EqIXY(indX)
}

infix fun Reg8.A.neq(indX: IndX): Condition {
    return Condition.NeqIXY(indX)
}

infix fun Reg8.A.gt(indX: IndX): Condition {
    return Condition.GtIXY(indX)
}

infix fun Reg8.A.lt(indX: IndX): Condition {
    return Condition.LtIXY(indX)
}

infix fun Reg8.A.gte(indX: IndX): Condition {
    return Condition.GteIXY(indX)
}
