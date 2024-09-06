
import Reg8.*
import Reg16.*
import RegI.*
import RegI.IX.minus
import RegI.IX.plus

@OptIn(ExperimentalUnsignedTypes::class)
fun main() {
    val allocator = Allocator(0xB000u)
    var a1 = ByteVar(allocator)
    var w1 = WordVar(allocator)

    val code = asm {

        //ld(A, B)
        //ld(A, 12)
        //ld(B, Mem[HL])

        ld(HL, 0x1234u)

        nop()
        nop()
        nop()
/*
        ld(Mem[HL], A)
        ld(Mem[IX + 3], A)
        ld(Mem[IY - 127], H)
        ld(Mem[HL], -25)
        ld(Mem[IX + 3], 3)
        ld(Mem[IY - 8], -5)
        ld(A, Mem[BC])
        ld(A, Mem[DE])
        ld(A, Mem[0x1234u])
        ld(Mem[0x1234u], A)
        ld(Mem[BC], A)
        ld(Mem[DE], A)
        ld(A, I)
        ld(A, R)
        ld(I, A)
        ld(R, A)
*/
        ld(BC, w1)
    }

    println("${code.opCount} commands, ${code.cycles} machine cycles")

    val binary = code.build()
    println("${binary.size} bytes total")

    binary.dump()
}