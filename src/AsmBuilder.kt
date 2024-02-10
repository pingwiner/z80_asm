
class AsmBuilder(val org: Int) {
    private val ops = mutableListOf<Operation>()
    var lastIfBlock: IfBlock? = null

    fun applyBody(body: AsmBuilder.() -> Unit): AsmBuilder {
        apply(body)
        applyLastIfBlock(0)
        return this
    }

    private fun addOp(operation: Operation) {
        if (lastIfBlock != null) {
            applyLastIfBlock(0)
            lastIfBlock = null
        }
        ops.add(operation)
    }

    private fun addIf(ifBlock: IfBlock) {
        if (lastIfBlock != null) {
            applyLastIfBlock(0)
        }
        lastIfBlock = ifBlock
    }

    private fun addElse(body: AsmBuilder.() -> Unit) {
        val tmp = AsmBuilder(0).applyBody(body)
        val elseBlockSize = tmp.size
        applyLastIfBlock(elseBlockSize)
        lastIfBlock = null
        val startAddr = org + this.size
        val builder = AsmBuilder(startAddr).applyBody(body)
        ops.addAll(builder.ops)
    }

    private fun applyLastIfBlock(elseBlockSize: Int) {
        lastIfBlock?.let {
            val lastJumpSize = if(elseBlockSize == 0) 0
                else if (elseBlockSize < 128) 2
                else 3
            ops.addAll(it.condition.ops(it.startAddr, it.builder.size + lastJumpSize))
            ops.addAll(it.builder.ops)
            if (elseBlockSize > 0) {
                if (elseBlockSize < 128) {
                    ops.add(JR(elseBlockSize.toByte()))
                } else {
                    ops.add(
                        JP( (this.org + this.size + elseBlockSize).toUShort() )
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    fun build(): UByteArray {
        val arraySize = ops.sumOf {it.size}
        val result = UByteArray(arraySize)
        var i = 0
        for (op in ops) {
            val opBytes = op.bytes()
            for (b in opBytes) {
                result[i++] = b
            }
        }
        return result
    }

    val opCount: Int
        get() = ops.size

    val cycles: Int
        get() = ops.sumOf { it.cycles }

    val size: Int
        get() = ops.sumOf { it.size }

    fun ld(r1: Reg8, r2: Reg8) {
        addOp(LDr8r8(r1, r2))
    }

    fun ld(r1: Reg8, imm: UByte) {
        addOp(LDr8n8(r1, imm))
    }

    fun ld(r1: Reg8, imm: Byte) {
        ld(r1, imm.toUByte())
    }


    fun ld(r1: Reg8, ind: Ind.HL) {
        addOp(LDr8HL(r1))
    }

    fun ld(r1: Reg8.A, ind: Ind.BC) {
        addOp(LDABC())
    }

    fun ld(r1: Reg8.A, ind: Ind.DE) {
        addOp(LDADE())
    }

    fun ld(r1: Reg8, indirect: IndX) {
        if (indirect.r == RegI.IX) {
            addOp(LDr8IX(r1, indirect.offset))
        } else {
            addOp(LDr8IY(r1, indirect.offset))
        }
    }

    fun ld(ind: Ind.HL, r1: Reg8) {
        addOp(LDHLr8(r1))
    }

    fun ld(ind: Ind.BC, r1: Reg8.A) {
        addOp(LDBCA())
    }

    fun ld(ind: Ind.DE, r1: Reg8.A) {
        addOp(LDDEA())
    }

    fun ld(indirect: IndX, r1: Reg8) {
        if (indirect.r == RegI.IX) {
            addOp(LDIXr8(indirect.offset, r1))
        } else {
            addOp(LDIYr8(indirect.offset, r1))
        }
    }

    fun ld(ind: Ind.HL, imm: UByte) {
        addOp(LDHLn8(imm))
    }

    fun ld(ind: Ind.HL, imm: Byte) {
        ld(ind, imm.toUByte())
    }

    fun ld(indX: IndX, imm: UByte) {
        if (indX.r == RegI.IX) {
            addOp(LDIXn8(indX.offset, imm))
        } else {
            addOp(LDIYn8(indX.offset, imm))
        }
    }

    fun ld(indX: IndX, imm: Byte) {
        ld(indX, imm.toUByte())
    }

    fun ld(r1: Reg8.A, address: Address) {
        addOp(LDAMM(address.addr))
    }

    fun ld(address: Address, r1: Reg8.A) {
        addOp(LDMMA(address.addr))
    }

    fun ld(r1: Reg8.A, r2: I) {
        addOp(LDAI())
    }

    fun ld(r1: Reg8.A, r2: R) {
        addOp(LDAR())
    }

    fun ld(r1: I, r2: Reg8.A) {
        addOp(LDIA())
    }

    fun ld(r1: R, r2: Reg8.A) {
        addOp(LDRA())
    }

    fun If(condition: Generator, body: AsmBuilder.() -> Unit) {
        val startAddr = org + this.size
        val builder = AsmBuilder(startAddr).applyBody(body)
        val ifBlock = IfBlock(condition, startAddr, builder)
        addIf(ifBlock)
    }

    infix fun Unit.Else(body: AsmBuilder.() -> Unit) {
        addElse(body)
    }

    fun nop() {
        addOp(NOP())
    }
}

class IfBlock(val condition: Generator, val startAddr: Int, val builder: AsmBuilder)

fun asm(org: Int = 0, initializer: AsmBuilder.() -> Unit): AsmBuilder {
    return AsmBuilder(org).apply(initializer)
}


