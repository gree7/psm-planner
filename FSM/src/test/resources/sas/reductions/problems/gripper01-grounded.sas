begin_version
3
end_version
begin_metric
0
end_metric
7
begin_variable
var0
-1
4
Atom at(ball1, rooma)
Atom at(ball1, roomb)
Atom at(ball1, roomc)
<none of those>
end_variable
begin_variable
var1
-1
4
Atom at(ball2, rooma)
Atom at(ball2, roomb)
Atom at(ball2, roomc)
<none of those>
end_variable
begin_variable
var2
-1
4
Atom at(ball3, rooma)
Atom at(ball3, roomb)
Atom at(ball3, roomc)
<none of those>
end_variable
begin_variable
var3
-1
4
Atom at(ball4, rooma)
Atom at(ball4, roomb)
Atom at(ball4, roomc)
<none of those>
end_variable
begin_variable
var4
-1
4
Atom at(ball5, rooma)
Atom at(ball5, roomb)
Atom at(ball5, roomc)
<none of those>
end_variable
begin_variable
var5
-1
6
Atom carry(ball1, left)
Atom carry(ball2, left)
Atom carry(ball3, left)
Atom carry(ball4, left)
Atom carry(ball5, left)
Atom free(left)
end_variable
begin_variable
var6
-1
6
Atom carry(ball1, right)
Atom carry(ball2, right)
Atom carry(ball3, right)
Atom carry(ball4, right)
Atom carry(ball5, right)
Atom free(right)
end_variable
7
begin_mutex_group
5
0 0
0 1
0 2
5 0
6 0
end_mutex_group
begin_mutex_group
5
1 0
1 1
1 2
5 1
6 1
end_mutex_group
begin_mutex_group
5
2 0
2 1
2 2
5 2
6 2
end_mutex_group
begin_mutex_group
5
3 0
3 1
3 2
5 3
6 3
end_mutex_group
begin_mutex_group
5
4 0
4 1
4 2
5 4
6 4
end_mutex_group
begin_mutex_group
6
5 0
5 1
5 2
5 3
5 4
5 5
end_mutex_group
begin_mutex_group
6
6 0
6 1
6 2
6 3
6 4
6 5
end_mutex_group
begin_state
0
0
0
1
2
5
5
end_state
begin_goal
5
0 2
1 1
2 1
3 0
4 1
end_goal
60
begin_operator
drop ball1 rooma left
0
2
0 0 3 0
0 5 0 5
1
end_operator
begin_operator
drop ball1 rooma right
0
2
0 0 3 0
0 6 0 5
1
end_operator
begin_operator
drop ball1 roomb left
0
2
0 0 3 1
0 5 0 5
1
end_operator
begin_operator
drop ball1 roomb right
0
2
0 0 3 1
0 6 0 5
1
end_operator
begin_operator
drop ball1 roomc left
0
2
0 0 3 2
0 5 0 5
1
end_operator
begin_operator
drop ball1 roomc right
0
2
0 0 3 2
0 6 0 5
1
end_operator
begin_operator
drop ball2 rooma left
0
2
0 1 3 0
0 5 1 5
1
end_operator
begin_operator
drop ball2 rooma right
0
2
0 1 3 0
0 6 1 5
1
end_operator
begin_operator
drop ball2 roomb left
0
2
0 1 3 1
0 5 1 5
1
end_operator
begin_operator
drop ball2 roomb right
0
2
0 1 3 1
0 6 1 5
1
end_operator
begin_operator
drop ball2 roomc left
0
2
0 1 3 2
0 5 1 5
1
end_operator
begin_operator
drop ball2 roomc right
0
2
0 1 3 2
0 6 1 5
1
end_operator
begin_operator
drop ball3 rooma left
0
2
0 2 3 0
0 5 2 5
1
end_operator
begin_operator
drop ball3 rooma right
0
2
0 2 3 0
0 6 2 5
1
end_operator
begin_operator
drop ball3 roomb left
0
2
0 2 3 1
0 5 2 5
1
end_operator
begin_operator
drop ball3 roomb right
0
2
0 2 3 1
0 6 2 5
1
end_operator
begin_operator
drop ball3 roomc left
0
2
0 2 3 2
0 5 2 5
1
end_operator
begin_operator
drop ball3 roomc right
0
2
0 2 3 2
0 6 2 5
1
end_operator
begin_operator
drop ball4 rooma left
0
2
0 3 3 0
0 5 3 5
1
end_operator
begin_operator
drop ball4 rooma right
0
2
0 3 3 0
0 6 3 5
1
end_operator
begin_operator
drop ball4 roomb left
0
2
0 3 3 1
0 5 3 5
1
end_operator
begin_operator
drop ball4 roomb right
0
2
0 3 3 1
0 6 3 5
1
end_operator
begin_operator
drop ball4 roomc left
0
2
0 3 3 2
0 5 3 5
1
end_operator
begin_operator
drop ball4 roomc right
0
2
0 3 3 2
0 6 3 5
1
end_operator
begin_operator
drop ball5 rooma left
0
2
0 4 3 0
0 5 4 5
1
end_operator
begin_operator
drop ball5 rooma right
0
2
0 4 3 0
0 6 4 5
1
end_operator
begin_operator
drop ball5 roomb left
0
2
0 4 3 1
0 5 4 5
1
end_operator
begin_operator
drop ball5 roomb right
0
2
0 4 3 1
0 6 4 5
1
end_operator
begin_operator
drop ball5 roomc left
0
2
0 4 3 2
0 5 4 5
1
end_operator
begin_operator
drop ball5 roomc right
0
2
0 4 3 2
0 6 4 5
1
end_operator
begin_operator
pick ball1 rooma left
0
2
0 0 0 3
0 5 5 0
1
end_operator
begin_operator
pick ball1 rooma right
0
2
0 0 0 3
0 6 5 0
1
end_operator
begin_operator
pick ball1 roomb left
0
2
0 0 1 3
0 5 5 0
1
end_operator
begin_operator
pick ball1 roomb right
0
2
0 0 1 3
0 6 5 0
1
end_operator
begin_operator
pick ball1 roomc left
0
2
0 0 2 3
0 5 5 0
1
end_operator
begin_operator
pick ball1 roomc right
0
2
0 0 2 3
0 6 5 0
1
end_operator
begin_operator
pick ball2 rooma left
0
2
0 1 0 3
0 5 5 1
1
end_operator
begin_operator
pick ball2 rooma right
0
2
0 1 0 3
0 6 5 1
1
end_operator
begin_operator
pick ball2 roomb left
0
2
0 1 1 3
0 5 5 1
1
end_operator
begin_operator
pick ball2 roomb right
0
2
0 1 1 3
0 6 5 1
1
end_operator
begin_operator
pick ball2 roomc left
0
2
0 1 2 3
0 5 5 1
1
end_operator
begin_operator
pick ball2 roomc right
0
2
0 1 2 3
0 6 5 1
1
end_operator
begin_operator
pick ball3 rooma left
0
2
0 2 0 3
0 5 5 2
1
end_operator
begin_operator
pick ball3 rooma right
0
2
0 2 0 3
0 6 5 2
1
end_operator
begin_operator
pick ball3 roomb left
0
2
0 2 1 3
0 5 5 2
1
end_operator
begin_operator
pick ball3 roomb right
0
2
0 2 1 3
0 6 5 2
1
end_operator
begin_operator
pick ball3 roomc left
0
2
0 2 2 3
0 5 5 2
1
end_operator
begin_operator
pick ball3 roomc right
0
2
0 2 2 3
0 6 5 2
1
end_operator
begin_operator
pick ball4 rooma left
0
2
0 3 0 3
0 5 5 3
1
end_operator
begin_operator
pick ball4 rooma right
0
2
0 3 0 3
0 6 5 3
1
end_operator
begin_operator
pick ball4 roomb left
0
2
0 3 1 3
0 5 5 3
1
end_operator
begin_operator
pick ball4 roomb right
0
2
0 3 1 3
0 6 5 3
1
end_operator
begin_operator
pick ball4 roomc left
0
2
0 3 2 3
0 5 5 3
1
end_operator
begin_operator
pick ball4 roomc right
0
2
0 3 2 3
0 6 5 3
1
end_operator
begin_operator
pick ball5 rooma left
0
2
0 4 0 3
0 5 5 4
1
end_operator
begin_operator
pick ball5 rooma right
0
2
0 4 0 3
0 6 5 4
1
end_operator
begin_operator
pick ball5 roomb left
0
2
0 4 1 3
0 5 5 4
1
end_operator
begin_operator
pick ball5 roomb right
0
2
0 4 1 3
0 6 5 4
1
end_operator
begin_operator
pick ball5 roomc left
0
2
0 4 2 3
0 5 5 4
1
end_operator
begin_operator
pick ball5 roomc right
0
2
0 4 2 3
0 6 5 4
1
end_operator
0