sealed class Reg8(val bitmask: Int) {
    data object A: Reg8(7)
    data object B: Reg8(0)
    data object C : Reg8(1)
    data object D: Reg8(2)
    data object E: Reg8(3)
    data object H: Reg8(4)
    data object L: Reg8(5)
}

sealed class Reg16 {
    data object AF : Reg16()
    data object BC : Reg16()
    data object DE : Reg16()
    data object HL : Reg16()
    data object SP : Reg16()
}

sealed class RegI {
    data object IX : RegI()
    data object IY : RegI()

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