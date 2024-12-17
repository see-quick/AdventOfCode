package advent.of.code;

// ChronospatialComputer
public class Day17 {

    public static void main(String[] args) {
        // 3-bit computer
        //  3-bit numbers (i.e., 0-7)
        //  3 registers (i.e., A, B, C) -> they can hold whole integer
        //  8 instructions (i.e., 000, 001...) - opcode
        //  each instruction read 3-bit number (i.e., operand)
        //  IP (i.e., instruction pointer) -> position of program starts at `0`
        //      increases by 2 for each instruction is processed
        //  If the computer tries to read an opcode past the end of the program, it instead halts.
        //  Example:
        //      So, the program 0,1,2,3 would run the instruction whose
        //          opcode is 0 and
        //          pass it the operand 1,
        //          then run the instruction having opcode 2
        //          and pass it the operand 3,
        //          then halt.
        //  Operands:
        //      1. Literal operand  - value operand of '7' is number 7
        //      2. Combo operand    - value of a combo operand is:
        //          Combo operands 0 through 3 represent literal values 0 through 3.
        //          Combo operand 4 represents the value of register A.
        //          Combo operand 5 represents the value of register B.
        //          Combo operand 6 represents the value of register C.
        //          Combo operand 7 is reserved and will not appear in valid programs.
        //  Instructions:
        //      1.  adv (opcode 0) -> perform division
        //            (X/Y where
        //              X is value from register A
        //              and Y is found by raising 2 to the power of the instruction's combo operand)
        //              The result of the division operation is truncated to an integer and then written to the A register.
        //      2. bxl (opcode 1) -> bitwise XOR -> ^ -> register B and literal operand
        //      3. bst (opcode 2) -> combo operand % 8 -> then write to register B
        //      4. jnz (opcode 3) -> nothing if register A=0, otherwise it jumps by setting instruction pointer to value of its literal operand
        //                          if this instruction jumps, the instruction pointer is not increased by 2 after this instruction.
        //      5. bxc (opcode 4) -> bitwise XOR of register B and C -> stores to register B
        //      6. out (opcode 5) -> calculate combo operand % 8 -> output
        //      7. bdv (opcode 6) -> same as adv but store value to register B (numerator still read value from register A)
        //      8. cdv (opcode 7) -> same as adv but store value to register C (numerator ... from register A)

        //  Here are some examples of instruction operation:
        //      If register C contains 9, the program 2,6 would set register B to 1.
        //      If register A contains 10, the program 5,0,5,1,5,4 would output 0,1,2.
        //      If register A contains 2024, the program 0,1,5,4,3,0 would output 4,2,5,6,7,7,7,7,3,1,0 and leave 0 in register A.
        //      If register B contains 29, the program 1,7 would set register B to 26.
        //      If register B contains 2024 and register C contains 43690, the program 4,0 would set register B to 44354.

        // Initial register values

        // part 1
//        int A = 25986278, B = 0, C = 0;
//        int[] program = {2,4,1,4,7,5,4,1,1,4,5,5,0,3,3,0}; // The given program

//        System.out.println(runProgram(A, B, C, program));

        // part 2
        int[] program = {2,4,1,4,7,5,4,1,1,4,5,5,0,3,3,0};
        String targetOutput = "2,4,1,4,7,5,4,1,1,4,5,5,0,3,3,0";
        // Part II is too slow :) we should do some binary search probably? or maybe some Mathematical Reconstruction
        // i.e., A can be derived by working backwards from the desired output
        // If we can determine a relationship between the final output and the initial A
        // (such as treating the output as digits in some base and reconstructing A),
        // we can compute the initial A directly without brute-forcing but that's for tomorrow... :D
        for (int initialA = 1; initialA < Integer.MAX_VALUE; initialA++) {
            // up to 817306164 and did not find
//            System.out.println("Tried: " + initialA);
            if (runProgram(initialA, 0, 0, program).equals(targetOutput)) {
                System.out.println(initialA);
                break;
            }
        }
    }

    private static String runProgram(int A, int B, int C, int[] program) {
        StringBuilder output = new StringBuilder();
        // instruction program
        int ip = 0;

        while (ip < program.length) {
            int opcode = program[ip];
            if (ip + 1 >= program.length) break;
            int operand = program[ip + 1];
            int comboVal = comboVal(operand, A, B, C);

            switch (opcode) {
                case 0: // adv
                    A = (int)(A / Math.pow(2, comboVal));
                    ip += 2;
                    break;
                case 1: // bxl
                    B = B ^ operand;
                    ip += 2;
                    break;
                case 2: // bst
                    B = comboVal & 7;
                    ip += 2;
                    break;
                case 3: // jnz
                    if (A != 0) {
                        ip = operand;
                    } else {
                        ip += 2;
                    }
                    break;
                case 4: // bxc
                    B = B ^ C; // operand ignored
                    ip += 2;
                    break;
                case 5: // out
                    int val = comboVal & 7;
                    if (output.length() > 0) output.append(",");
                    output.append(val);
                    ip += 2;
                    break;
                case 6: // bdv
                    B = (int)(A / Math.pow(2, comboVal));
                    ip += 2;
                    break;
                case 7: // cdv
                    C = (int)(A / Math.pow(2, comboVal));
                    ip += 2;
                    break;
                default:
                    ip = program.length;
                    break;
            }
        }

        return output.toString();
    }

    private static int comboVal(int op, int A, int B, int C) {
        switch (op) {
            case 0: case 1: case 2: case 3: return op;
            case 4: return A;
            case 5: return B;
            case 6: return C;
            default: throw new IllegalArgumentException("Invalid combo operand");
        }
    }
}
