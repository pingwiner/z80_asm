sealed class Reg8(val bitmask: Int) {
    data object A: Reg8(7)
    data object B: Reg8(0)
    data object C : Reg8(1)
    data object D: Reg8(2)
    data object E: Reg8(3)
    data object H: Reg8(4)
    data object L: Reg8(5)
}

sealed class Reg16(val bitmask: Int) {
    data object BC : Reg16(0)
    data object DE : Reg16(1)
    data object HL : Reg16(2)
    data object SP : Reg16(3)
}

sealed class RegI(val prefix: UByte) {
    data object IX : RegI(0xDDu)
    data object IY : RegI(0xFDu)

    infix operator fun RegI.plus(b: Byte): IndX {
        return IndX(this, b)
    }

    infix operator fun RegI.minus(b: Byte): IndX {
        return IndX(this, (-b).toByte())
    }

}

object I
object R

sealed class Ind {
    data object HL : Ind()
    data object BC : Ind()
    data object DE : Ind()
}

class IndX(val r: RegI, val offset: Byte)
class Address(val addr: UShort)

object Mem {
    operator fun get(r: Reg16.HL): Ind.HL {
        return Ind.HL
    }

    operator fun get(r: Reg16.BC): Ind.BC {
        return Ind.BC
    }

    operator fun get(r: Reg16.DE): Ind.DE {
        return Ind.DE
    }

    operator fun get(indX: IndX): IndX {
        return indX
    }

    operator fun get(addr: UShort): Address {
        return Address(addr)
    }
}

class Allocator(val startAddr: UInt) {
    var offset: UInt = 0u
    fun allocByte() : UShort {
        val result = startAddr + offset
        if (result > UShort.MAX_VALUE) {
            throw IndexOutOfBoundsException("Address space overflow")
        }
        offset++
        return result.toUShort()
    }
    fun allocWord() : UShort {
        val result = startAddr + offset
        if (result > UShort.MAX_VALUE) {
            throw IndexOutOfBoundsException("Address space overflow")
        }
        offset += 2u
        return result.toUShort()
    }

}