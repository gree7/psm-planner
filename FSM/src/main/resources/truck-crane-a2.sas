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
Atom box-at(a)
Atom box-at(b)
Atom box-at-truck()
end_variable
begin_variable
var1
-1
2
Atom truck-at(a)
Atom truck-at(b)
end_variable
begin_variable
var2
-1
2
Atom truck-started()
<none of those>
end_variable
2
begin_mutex_group
3
0 0
0 1
0 2
end_mutex_group
begin_mutex_group
2
1 0
1 1
end_mutex_group
begin_state
0
0
1
end_state
begin_goal
1
0 1
end_goal
7
begin_operator
load-crane a
1
1 0
1
0 0 0 2
0
end_operator
begin_operator
load-crane b
1
1 1
1
0 0 1 2
0
end_operator
begin_operator
move-truck a b
1
2 0
1
0 1 0 1
0
end_operator
begin_operator
move-truck b a
1
2 0
1
0 1 1 0
0
end_operator
begin_operator
start-truck 
0
1
0 2 -1 0
0
end_operator
begin_operator
unload-crane a
1
1 0
1
0 0 2 0
0
end_operator
begin_operator
unload-crane b
1
1 1
1
0 0 2 1
0
end_operator
0
