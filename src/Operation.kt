
abstract class Operation(val size: Int, val cycles: Int) {
    @OptIn(ExperimentalUnsignedTypes::class)
    abstract fun bytes(): UByteArray
}

class LD_r8_r8(val r1: Reg8, val r2: Reg8) : Operation(1, 1) {
    @ExperimentalUnsignedTypes
    override fun bytes(): UByteArray {
        val opcode: Int = (1 shl 6) or (r1.bitmask shl 3) or r2.bitmask
        return ubyteArrayOf(opcode.toUByte())
    }
}

class LD_r8_n8(val r: Reg8, val n: UByte) : Operation(2, 2) {
    @ExperimentalUnsignedTypes
    override fun bytes(): UByteArray {
        val opcode: Int = (r.bitmask shl 3) or 6
        return ubyteArrayOf(opcode.toUByte(), n)
    }
}

class LD_r8_HL(val r: Reg8) : Operation(1, 2) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        val opcode: Int = (1 shl 6) or (r.bitmask shl 3) or 6
        return ubyteArrayOf(opcode.toUByte())
    }
}

class LD_r8_IX(val r: Reg8, val offset: Byte): Operation(3,5) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        val opcode: Int = (1 shl 6) or (r.bitmask shl 3) or 6
        return ubyteArrayOf(0xDDu, opcode.toUByte(), offset.toUByte())
    }
}

class LD_r8_IY(val r: Reg8, val offset: Byte): Operation(3,5) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        val opcode: Int = (1 shl 6) or (r.bitmask shl 3) or 6
        return ubyteArrayOf(0xFDu, opcode.toUByte(), offset.toUByte())
    }
}

class LD_HL_r8(val r: Reg8) : Operation(1, 2) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        val opcode: Int = (7 shl 4) or r.bitmask
        return ubyteArrayOf(opcode.toUByte())
    }
}

class LD_IX_r8(val offset: Byte, val r: Reg8) : Operation(3, 5) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        val opcode: Int = (7 shl 4) or r.bitmask
        return ubyteArrayOf(0xDDu, opcode.toUByte(), offset.toUByte())
    }
}

class LD_IY_r8(val offset: Byte, val r: Reg8) : Operation(3, 5) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        val opcode: Int = (7 shl 4) or r.bitmask
        return ubyteArrayOf(0xFDu, opcode.toUByte(), offset.toUByte())
    }
}

class LD_HL_n8(val n: UByte) : Operation(2, 3) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        return ubyteArrayOf(0x36u, n)
    }
}

class LD_IX_n8(val offset: Byte, val n: UByte) : Operation(4, 5) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        return ubyteArrayOf(0xDDu, 0x36u, offset.toUByte(), n)
    }
}

class LD_IY_n8(val offset: Byte, val n: UByte) : Operation(4, 5) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        return ubyteArrayOf(0xFDu, 0x36u, offset.toUByte(), n)
    }
}

class LD_A_BC : Operation(1, 2) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        return ubyteArrayOf(0x0Au)
    }
}

class LD_A_DE : Operation(1, 2) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        return ubyteArrayOf(0x1Au)
    }
}

class LD_A_MM(val addr: UShort) : Operation(3,4) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        return ubyteArrayOf(0x3Au, lowByte(addr), hiByte(addr))
    }
}

class LD_MM_A(val addr: UShort) : Operation(3,4) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        return ubyteArrayOf(0x32u, lowByte(addr), hiByte(addr))
    }
}

class LD_HL_MM(val addr: UShort) : Operation(3,5) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        return ubyteArrayOf(0x2Au, lowByte(addr), hiByte(addr))
    }
}

class LD_MM_HL(val addr: UShort) : Operation(3,5) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        return ubyteArrayOf(0x22u, lowByte(addr), hiByte(addr))
    }
}

class LD_BC_A : Operation(1,2) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        return ubyteArrayOf(0x02u)
    }
}

class LD_DE_A : Operation(1,2) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        return ubyteArrayOf(0x12u)
    }
}

class LD_A_I : Operation(2, 2) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        return ubyteArrayOf(0xEDu, 0x57u)
    }
}

class LD_A_R : Operation(2, 2) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        return ubyteArrayOf(0xEDu, 0x5Fu)
    }
}

class LD_I_A : Operation(2, 2) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        return ubyteArrayOf(0xEDu, 0x47u)
    }
}

class LD_R_A : Operation(2, 2) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        return ubyteArrayOf(0xEDu, 0x4Fu)
    }
}

class LD_r16_n16(val r: Reg16, val imm: UShort) : Operation(3,2) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        val opCode = ((r.bitmask shl 4) or 1).toUByte()
        return ubyteArrayOf(opCode, lowByte(imm), hiByte(imm))
    }
}

class LD_rI_n16(val r: RegI, val imm: UShort) : Operation(4,4) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        return ubyteArrayOf(r.prefix, 0x21u, lowByte(imm), hiByte(imm))
    }
}

class LD_r16_MM(val r: Reg16, val addr: UShort) : Operation(4,6){
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        var prefix: UByte = 0xEDu
        val opCode = ((r.bitmask shl 4) or 0x4B).toUByte()
        return ubyteArrayOf(prefix, opCode, lowByte(addr), hiByte(addr))
    }
}

class LD_rI_MM(val r: RegI, val addr: UShort) : Operation(4,6){
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        val opCode: UByte = 0x2Au
        return ubyteArrayOf(r.prefix, opCode, lowByte(addr), hiByte(addr))
    }
}

class LD_MM_r16(val addr: UShort, val r: Reg16) : Operation(4,6){
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        var prefix: UByte = 0xEDu
        val opCode = ((r.bitmask shl 4) or 0x43).toUByte()
        return ubyteArrayOf(prefix, opCode, lowByte(addr), hiByte(addr))
    }
}

class LD_MM_rI(val addr: UShort, val r: RegI) : Operation(4,6){
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        val opCode: UByte = 0x22u
        return ubyteArrayOf(r.prefix, opCode, lowByte(addr), hiByte(addr))
    }
}

class LD_SP_HL: Operation(1, 1) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        return ubyteArrayOf(0xF9u)
    }
}

class CP_r8(val r: Reg8) : Operation(1, 1) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        val opCode: Int = 0xB8 or r.bitmask
        return ubyteArrayOf(opCode.toUByte())
    }
}

class CP_n8(val n: UByte) : Operation(2, 2) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        return ubyteArrayOf(0xFEu, n)
    }
}

class CP_HL : Operation(1, 2) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        return ubyteArrayOf(0xBEu)
    }
}

class CP_xx(indX: IndX) : Operation(3, 5) {
    val prefix: UByte = indX.r.prefix
    val offset: Byte = indX.offset

    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        return ubyteArrayOf(prefix, 0xBEu, offset.toUByte())
    }
}

class JR_NZ(val offset: Byte) : Operation(2, 3) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        return ubyteArrayOf(0x20u, offset.toUByte())
    }
}

class JR_Z(val offset: Byte) : Operation(2, 3) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        return ubyteArrayOf(0x28u, offset.toUByte())
    }
}

class JR_C(val offset: Byte) : Operation(2, 3) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        return ubyteArrayOf(0x38u, offset.toUByte())
    }
}

class JR_NC(val offset: Byte) : Operation(2, 3) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        return ubyteArrayOf(0x30u, offset.toUByte())
    }
}

class JP_NZ(val addr: UShort) : Operation(3, 3) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        return ubyteArrayOf(0xC2u, lowByte(addr), hiByte(addr))
    }
}

class JP_Z(val addr: UShort) : Operation(3, 3) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        return ubyteArrayOf(0xCAu, lowByte(addr), hiByte(addr))
    }
}

class JP_C(val addr: UShort) : Operation(3, 3) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        return ubyteArrayOf(0xDAu, lowByte(addr), hiByte(addr))
    }
}

class JP_NC(val addr: UShort) : Operation(3, 3) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        return ubyteArrayOf(0xD2u, lowByte(addr), hiByte(addr))
    }
}

class JR(val offset: Byte) : Operation(2,3) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        return ubyteArrayOf(0x18u, offset.toUByte())
    }
}

class JP(val addr: UShort) : Operation(3,3) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        return ubyteArrayOf(0xC3u, lowByte(addr), hiByte(addr))
    }
}

class NOP : Operation(1, 1) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        return ubyteArrayOf(0u)
    }
}

class DJNZ(val offset: Byte): Operation(2,3) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        return ubyteArrayOf(0x10u, offset.toUByte())
    }
}

class DB(val b: UByte): Operation(1, 0) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        return ubyteArrayOf(b)
    }

}