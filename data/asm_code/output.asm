SUB $sp, $sp, 4
SUB $sp, $sp, 4
SUB $sp, $sp, 4
SUB $sp, $sp, 4
LW $t0, -12($fp)
LW $t1, -4($fp)
beq $t0, $t1, L0
L0
LW $t2, -8($fp)
LW $t3, 0($fp)
SUB $t4, $t2, $t3
SW $t0, -12($fp)
LW $t0, -8($fp)
LW $t2, 0($fp)
ADD $t3, $t0, $t2
SW $t1, -12($fp)
