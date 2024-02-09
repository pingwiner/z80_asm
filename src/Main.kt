
import Reg8.*
import Reg16.*
import RegI.*
import RegI.IX.minus
import RegI.IX.plus

@OptIn(ExperimentalUnsignedTypes::class)
fun main() {

    val code = asm {
        //ld(A, B)
        //ld(A, 12)
        //ld(B, Mem[HL])

        If(A gt B) {
            ld(A, 1)
        }
        If(A lt B) {
            ld(A, 2)
        }
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
    }

    println("${code.opCount} commands, ${code.cycles} machine cycles")

    val binary = code.build()
    println("${binary.size} bytes total")

    binary.dump()
}