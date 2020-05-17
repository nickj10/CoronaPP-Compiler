MOVE $fp, $sp
SUB $sp, $sp, 4 #Reserve memory for <b>
SUB $sp, $sp, 4 #Reserve memory for <a>
SUB $sp, $sp, 4 #Reserve memory for <count>
SUB $sp, $sp, 4 #Reserve memory for <prev>
SUB $sp, $sp, 4 #Reserve memory for <i>
LI $t0, 7
SW $t0, -8($fp) #Save var <count>
LI $t0, 0
SW $t0, -4($fp) #Save var <a>
LI $t0, 1
SW $t0, 0($fp) #Save var <b>
LI $t0, 1
SW $t0, -16($fp) #Save var <i>
LW $t0, -16($fp) #Load var <i>
LW $t1, -8($fp) #Load var <count>
while0
bgt $t0, $t1, end_while0
   LW $t2, -4($fp) #Load var <a>
   LW $t3, 0($fp) #Load var <b>
   ADD $t4, $t2, $t3
   SW $t0, -12($fp) #Save var <prev>
   LW $t0, 0($fp) #Load var <b>
   SW $t0, -4($fp) #Save var <a>
   LW $t0, -12($fp) #Load var <prev>
   SW $t0, 0($fp) #Save var <b>
   LW $t0, -16($fp) #Load var <i>
   LI $t2, 1
   ADD $t3, $t0, $t2
   SW $t1, -16($fp) #Save var <i>
   j while
end_while0
