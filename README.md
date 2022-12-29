## 2-Pass-Mips-Assembly-Parser

This program emulates a 2-Pass MIPS assembly parser that reads in files written in MIPS Assembly, translates them to machine code (Binary) and then displays the result to the screen.

Input: Assembly source code  
Output: Machine code  

Translation has 2 phases:
1. Analysis: Understand the meaning of source string.
2. Synthesis: Output the equivalent target binary string.


&nbsp;  
**Assembler Challenges - Labels**  
We want to read 1 assembly instruction and directly output its encoded machine instruction.

How to assemble:  
&emsp;&emsp;beq $0, $1, label  
&emsp;&emsp;...  
&emsp;&emsp;label: add $22, $10, $31

**Problem:** To encode beq we need the memory address of label, but we haven’t encountered this label yet! Fix?

**Solution**  
Pass 1:  
• Group tokens into instructions, verifying instructions are valid.  
• Keep track of the memory address (starting at 0x0) each instruction will be given when loaded into memory.  
• Build a symbol table for (label, address) pairs (use map).  
NB: multiple labels may have the same address.  

Pass 2:  
• Translate each instructions into machine code.  
• If a label is encountered, look up associated address - compute branch offset if necessary.  
• Output translated, assembled MIPS to the screen.
