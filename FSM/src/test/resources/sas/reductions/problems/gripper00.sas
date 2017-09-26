begin_version
3
end_version
begin_metric
0
end_metric
3
begin_variable
var0
-1
3
Atom at(ball1, rooma)
Atom at(ball1, roomb)
<none of those>
end_variable
begin_variable
var1
-1
2
Atom carry(ball1, left)
Atom free(left)
end_variable
begin_variable
var2
-1
2
Atom carry(ball1, right)
Atom free(right)
end_variable
3
begin_mutex_group
4
0 0
0 1
1 0
2 0
end_mutex_group
begin_mutex_group
2
1 0
1 1
end_mutex_group
begin_mutex_group
2
2 0
2 1
end_mutex_group
begin_state
0
1
1
end_state
begin_goal
1
0 1
end_goal
8
begin_operator
drop ball1 rooma left
0
2
0 0 -1 0
0 1 0 1
1
end_operator
begin_operator
drop ball1 rooma right
0
2
0 0 -1 0
0 2 0 1
1
end_operator
begin_operator
drop ball1 roomb left
0
2
0 0 -1 1
0 1 0 1
1
end_operator
begin_operator
drop ball1 roomb right
0
2
0 0 -1 1
0 2 0 1
1
end_operator
begin_operator
pick ball1 rooma left
0
2
0 0 0 2
0 1 1 0
1
end_operator
begin_operator
pick ball1 rooma right
0
2
0 0 0 2
0 2 1 0
1
end_operator
begin_operator
pick ball1 roomb left
0
2
0 0 1 2
0 1 1 0
1
end_operator
begin_operator
pick ball1 roomb right
0
2
0 0 1 2
0 2 1 0
1
end_operator
0