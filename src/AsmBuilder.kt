import java.security.InvalidParameterException

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
        addOp(LD_r8_r8(r1, r2))
    }

    fun ld(r1: Reg8, imm: UByte) {
        addOp(LD_r8_n8(r1, imm))
    }

    fun ld(r1: Reg8, imm: Byte) {
        ld(r1, imm.toUByte())
    }


    fun ld(r1: Reg8, ind: Ind.HL) {
        addOp(LD_r8_HL(r1))
    }

    fun ld(r1: Reg8.A, ind: Ind.BC) {
        addOp(LD_A_BC())
    }

    fun ld(r1: Reg8.A, ind: Ind.DE) {
        addOp(LD_A_DE())
    }

    fun ld(r1: Reg8, indirect: IndX) {
        if (indirect.r == RegI.IX) {
            addOp(LD_r8_IX(r1, indirect.offset))
        } else {
            addOp(LD_r8_IY(r1, indirect.offset))
        }
    }

    fun ld(ind: Ind.HL, r1: Reg8) {
        addOp(LD_HL_r8(r1))
    }

    fun ld(ind: Ind.BC, r1: Reg8.A) {
        addOp(LD_BC_A())
    }

    fun ld(ind: Ind.DE, r1: Reg8.A) {
        addOp(LD_DE_A())
    }

    fun ld(indirect: IndX, r1: Reg8) {
        if (indirect.r == RegI.IX) {
            addOp(LD_IX_r8(indirect.offset, r1))
        } else {
            addOp(LD_IY_r8(indirect.offset, r1))
        }
    }

    fun ld(ind: Ind.HL, imm: UByte) {
        addOp(LD_HL_n8(imm))
    }

    fun ld(ind: Ind.HL, imm: Byte) {
        ld(ind, imm.toUByte())
    }

    fun ld(indX: IndX, imm: UByte) {
        if (indX.r == RegI.IX) {
            addOp(LD_IX_n8(indX.offset, imm))
        } else {
            addOp(LD_IY_n8(indX.offset, imm))
        }
    }

    fun ld(indX: IndX, imm: Byte) {
        ld(indX, imm.toUByte())
    }

    fun ld(r1: Reg8.A, address: Address) {
        addOp(LD_A_MM(address.addr))
    }

    fun ld(address: Address, r1: Reg8.A) {
        addOp(LD_MM_A(address.addr))
    }

    fun ld(r1: Reg8.A, r2: I) {
        addOp(LD_A_I())
    }

    fun ld(r1: Reg8.A, r2: R) {
        addOp(LD_A_R())
    }

    fun ld(r1: I, r2: Reg8.A) {
        addOp(LD_I_A())
    }

    fun ld(r1: R, r2: Reg8.A) {
        addOp(LD_R_A())
    }

    fun ld(r1: Reg8.A, ub: ByteVar) {
        addOp(LD_A_MM(ub.address))
    }

    fun ld(ub: ByteVar, r1: Reg8.A) {
        addOp(LD_MM_A(ub.address))
    }

    fun ld(hl: Reg16.HL, w: WordVar) {
        addOp(LD_HL_MM(w.address))
    }

    fun ld(w: WordVar, hl: Reg16.HL) {
        addOp(LD_MM_HL(w.address))
    }

    fun ld(r: Reg16, imm: UShort) {
        addOp(LD_r16_n16(r, imm))
    }

    fun ld(r: RegI, imm: UShort) {
        addOp(LD_rI_n16(r, imm))
    }

    fun ld(hl: Reg16.HL, addr: Address) {
        addOp(LD_HL_MM(addr.addr))
    }

    fun ld(r: Reg16, addr: Address) {
        addOp(LD_r16_MM(r, addr.addr))
    }

    fun ld(r: Reg16, w: WordVar) {
        addOp(LD_r16_MM(r, w.address))
    }

    fun ld(addr: Address, r: Reg16) {
        addOp(LD_MM_r16(addr.addr, r))
    }

    fun ld(w: WordVar, r: Reg16) {
        addOp(LD_MM_r16(w.address, r))
    }

    fun ld(r: RegI, addr: Address) {
        addOp(LD_rI_MM(r, addr.addr))
    }

    fun ld(r: RegI, w: WordVar) {
        addOp(LD_rI_MM(r, w.address))
    }

    fun ld(addr: Address, r: RegI) {
        addOp(LD_MM_rI(addr.addr, r))
    }

    fun ld(w: WordVar, r: RegI) {
        addOp(LD_MM_rI(w.address, r))
    }

    fun ld(sp: Reg16.SP, hl: Reg16.HL) {
        addOp(LD_SP_HL())
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

    fun loop(b: Reg8.B, body: AsmBuilder.() -> Unit) {
        val startAddr = org + this.size
        val builder = AsmBuilder(startAddr).applyBody(body)
        val offset = builder.size + 2
        if (offset < 129) {
            builder.addOp(DJNZ((-offset).toByte()))
        } else {
            throw InvalidParameterException("Jump range is too long")
        }
        ops.addAll(builder.ops)
    }

    fun dup(count: Int, body: AsmBuilder.() -> Unit) {
        val startAddr = org
        for (i in 0..<count) {
            val builder = AsmBuilder(startAddr + this.size).applyBody(body)
            ops.addAll(builder.ops)
        }
    }

    fun db(vararg bytes: Byte) {
        for (b in bytes) {
            addOp(DB(b.toUByte()))
        }
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    fun db(vararg bytes: UByte) {
        for (b in bytes) {
            addOp(DB(b))
        }
    }

    fun db(s: String) {
        for (c in s) {
            addOp(DB(c.code.toUByte()))
        }
    }

}

class IfBlock(val condition: Generator, val startAddr: Int, val builder: AsmBuilder)

fun asm(org: Int = 0, initializer: AsmBuilder.() -> Unit): AsmBuilder {
    return AsmBuilder(org).apply(initializer)
}

class ByteVar(allocator: Allocator) {
    val address: UShort = allocator.allocByte()
}
class WordVar(allocator: Allocator) {
    val address: UShort = allocator.allocWord()
}
