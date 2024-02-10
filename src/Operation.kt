
abstract class Operation(val size: Int, val cycles: Int) {
    @OptIn(ExperimentalUnsignedTypes::class)
    abstract fun bytes(): UByteArray
}

class LDr8r8(val r1: Reg8, val r2: Reg8) : Operation(1, 1) {
    @ExperimentalUnsignedTypes
    override fun bytes(): UByteArray {
        val opcode: Int = (1 shl 6) or (r1.bitmask shl 3) or r2.bitmask
        return ubyteArrayOf(opcode.toUByte())
    }
}

class LDr8n8(val r: Reg8, val n: UByte) : Operation(2, 2) {
    @ExperimentalUnsignedTypes
    override fun bytes(): UByteArray {
        val opcode: Int = (r.bitmask shl 3) or 6
        return ubyteArrayOf(opcode.toUByte(), n)
    }
}

class LDr8HL(val r: Reg8) : Operation(1, 2) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        val opcode: Int = (1 shl 6) or (r.bitmask shl 3) or 6
        return ubyteArrayOf(opcode.toUByte())
    }
}

class LDr8IX(val r: Reg8, val offset: Byte): Operation(3,5) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        val opcode: Int = (1 shl 6) or (r.bitmask shl 3) or 6
        return ubyteArrayOf(0xDDu, opcode.toUByte(), offset.toUByte())
    }
}

class LDr8IY(val r: Reg8, val offset: Byte): Operation(3,5) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        val opcode: Int = (1 shl 6) or (r.bitmask shl 3) or 6
        return ubyteArrayOf(0xFDu, opcode.toUByte(), offset.toUByte())
    }
}

class LDHLr8(val r: Reg8) : Operation(1, 2) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        val opcode: Int = (7 shl 4) or r.bitmask
        return ubyteArrayOf(opcode.toUByte())
    }
}

class LDIXr8(val offset: Byte, val r: Reg8) : Operation(3, 5) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        val opcode: Int = (7 shl 4) or r.bitmask
        return ubyteArrayOf(0xDDu, opcode.toUByte(), offset.toUByte())
    }
}

class LDIYr8(val offset: Byte, val r: Reg8) : Operation(3, 5) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        val opcode: Int = (7 shl 4) or r.bitmask
        return ubyteArrayOf(0xFDu, opcode.toUByte(), offset.toUByte())
    }
}

class LDHLn8(val n: UByte) : Operation(2, 3) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        return ubyteArrayOf(0x36u, n)
    }
}

class LDIXn8(val offset: Byte, val n: UByte) : Operation(4, 5) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        return ubyteArrayOf(0xDDu, 0x36u, offset.toUByte(), n)
    }
}

class LDIYn8(val offset: Byte, val n: UByte) : Operation(4, 5) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        return ubyteArrayOf(0xFDu, 0x36u, offset.toUByte(), n)
    }
}

class LDABC : Operation(1, 2) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        return ubyteArrayOf(0x0Au)
    }
}

class LDADE : Operation(1, 2) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        return ubyteArrayOf(0x1Au)
    }
}

class LDAMM(val addr: UShort) : Operation(3,4) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        return ubyteArrayOf(0x3Au, lowByte(addr), hiByte(addr))
    }
}

class LDMMA(val addr: UShort) : Operation(3,4) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        return ubyteArrayOf(0x32u, lowByte(addr), hiByte(addr))
    }
}

class LDBCA : Operation(1,2) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        return ubyteArrayOf(0x02u)
    }
}

class LDDEA : Operation(1,2) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        return ubyteArrayOf(0x12u)
    }
}

class LDAI : Operation(2, 2) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        return ubyteArrayOf(0xEDu, 0x57u)
    }
}

class LDAR : Operation(2, 2) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        return ubyteArrayOf(0xEDu, 0x5Fu)
    }
}

class LDIA : Operation(2, 2) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        return ubyteArrayOf(0xEDu, 0x47u)
    }
}

class LDRA : Operation(2, 2) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        return ubyteArrayOf(0xEDu, 0x4Fu)
    }
}

class CPr8(val r: Reg8) : Operation(1, 1) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        val opCode: Int = 0xB8 or r.bitmask
        return ubyteArrayOf(opCode.toUByte())
    }
}

class CPn8(val n: UByte) : Operation(2, 2) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        return ubyteArrayOf(0xFEu, n)
    }
}

class CPHL : Operation(1, 2) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        return ubyteArrayOf(0xBEu)
    }
}

sealed class CPxx(val prefix: UByte, val offset: Byte) : Operation(3, 5) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        return ubyteArrayOf(prefix, 0xBEu, offset.toUByte())
    }
    companion object {
        fun create(indX: IndX): CPxx {
            return if (indX.r == RegI.IX) CPIX(indX.offset) else CPIY(indX.offset)
        }
    }
    class CPIX(offset: Byte) : CPxx(0xDDu, offset)
    class CPIY(offset: Byte) : CPxx(0xFDu, offset)
}

class JRNZ(val offset: Byte) : Operation(2, 3) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        return ubyteArrayOf(0x20u, offset.toUByte())
    }
}

class JRZ(val offset: Byte) : Operation(2, 3) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        return ubyteArrayOf(0x28u, offset.toUByte())
    }
}

class JRC(val offset: Byte) : Operation(2, 3) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        return ubyteArrayOf(0x38u, offset.toUByte())
    }
}

class JRNC(val offset: Byte) : Operation(2, 3) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        return ubyteArrayOf(0x30u, offset.toUByte())
    }
}

class JPNZ(val addr: UShort) : Operation(3, 3) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        return ubyteArrayOf(0xC2u, lowByte(addr), hiByte(addr))
    }
}

class JPZ(val addr: UShort) : Operation(3, 3) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        return ubyteArrayOf(0xCAu, lowByte(addr), hiByte(addr))
    }
}

class JPC(val addr: UShort) : Operation(3, 3) {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun bytes(): UByteArray {
        return ubyteArrayOf(0xDAu, lowByte(addr), hiByte(addr))
    }
}

class JPNC(val addr: UShort) : Operation(3, 3) {
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
