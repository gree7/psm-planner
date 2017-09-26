begin_version
3
end_version
begin_metric
1
end_metric
13
begin_variable
var0
-1
2
Atom clear(tile_0-1)
NegatedAtom clear(tile_0-1)
end_variable
begin_variable
var1
-1
2
Atom clear(tile_0-2)
<none of those>
end_variable
begin_variable
var2
-1
2
Atom clear(tile_0-3)
<none of those>
end_variable
begin_variable
var3
-1
3
Atom clear(tile_1-1)
Atom painted(tile_1-1, white)
<none of those>
end_variable
begin_variable
var4
-1
3
Atom clear(tile_1-2)
Atom painted(tile_1-2, black)
<none of those>
end_variable
begin_variable
var5
-1
3
Atom clear(tile_1-3)
Atom painted(tile_1-3, white)
<none of those>
end_variable
begin_variable
var6
-1
3
Atom clear(tile_2-1)
Atom painted(tile_2-1, black)
<none of those>
end_variable
begin_variable
var7
-1
3
Atom clear(tile_2-2)
Atom painted(tile_2-2, white)
<none of those>
end_variable
begin_variable
var8
-1
3
Atom clear(tile_2-3)
Atom painted(tile_2-3, black)
<none of those>
end_variable
begin_variable
var9
-1
2
Atom painted(tile_0-1, black)
NegatedAtom painted(tile_0-1, black)
end_variable
begin_variable
var10
-1
2
Atom painted(tile_0-1, white)
NegatedAtom painted(tile_0-1, white)
end_variable
begin_variable
var11
-1
9
Atom robot-at(robot1, tile_0-1)
Atom robot-at(robot1, tile_0-2)
Atom robot-at(robot1, tile_0-3)
Atom robot-at(robot1, tile_1-1)
Atom robot-at(robot1, tile_1-2)
Atom robot-at(robot1, tile_1-3)
Atom robot-at(robot1, tile_2-1)
Atom robot-at(robot1, tile_2-2)
Atom robot-at(robot1, tile_2-3)
end_variable
begin_variable
var12
-1
9
Atom robot-at(robot2, tile_0-1)
Atom robot-at(robot2, tile_0-2)
Atom robot-at(robot2, tile_0-3)
Atom robot-at(robot2, tile_1-1)
Atom robot-at(robot2, tile_1-2)
Atom robot-at(robot2, tile_1-3)
Atom robot-at(robot2, tile_2-1)
Atom robot-at(robot2, tile_2-2)
Atom robot-at(robot2, tile_2-3)
end_variable
16
begin_mutex_group
3
1 0
11 1
12 1
end_mutex_group
begin_mutex_group
3
2 0
11 2
12 2
end_mutex_group
begin_mutex_group
4
3 0
3 1
11 3
12 3
end_mutex_group
begin_mutex_group
3
3 0
11 3
12 3
end_mutex_group
begin_mutex_group
4
4 0
4 1
11 4
12 4
end_mutex_group
begin_mutex_group
3
4 0
11 4
12 4
end_mutex_group
begin_mutex_group
4
5 0
5 1
11 5
12 5
end_mutex_group
begin_mutex_group
3
5 0
11 5
12 5
end_mutex_group
begin_mutex_group
4
6 0
6 1
11 6
12 6
end_mutex_group
begin_mutex_group
3
6 0
11 6
12 6
end_mutex_group
begin_mutex_group
4
7 0
7 1
11 7
12 7
end_mutex_group
begin_mutex_group
3
7 0
11 7
12 7
end_mutex_group
begin_mutex_group
4
8 0
8 1
11 8
12 8
end_mutex_group
begin_mutex_group
3
8 0
11 8
12 8
end_mutex_group
begin_mutex_group
9
11 0
11 1
11 2
11 3
11 4
11 5
11 6
11 7
11 8
end_mutex_group
begin_mutex_group
9
12 0
12 1
12 2
12 3
12 4
12 5
12 6
12 7
12 8
end_mutex_group
begin_state
0
0
0
0
0
0
0
2
0
1
1
0
7
end_state
begin_goal
6
3 1
4 1
5 1
6 1
7 1
8 1
end_goal
70
begin_operator
down robot1 tile_1-1 tile_0-1
0
3
0 0 0 1
0 3 2 0
0 11 3 0
1
end_operator
begin_operator
down robot1 tile_1-2 tile_0-2
0
3
0 1 0 1
0 4 2 0
0 11 4 1
1
end_operator
begin_operator
down robot1 tile_1-3 tile_0-3
0
3
0 2 0 1
0 5 2 0
0 11 5 2
1
end_operator
begin_operator
down robot1 tile_2-1 tile_1-1
0
3
0 3 0 2
0 6 2 0
0 11 6 3
1
end_operator
begin_operator
down robot1 tile_2-2 tile_1-2
0
3
0 4 0 2
0 7 2 0
0 11 7 4
1
end_operator
begin_operator
down robot1 tile_2-3 tile_1-3
0
3
0 5 0 2
0 8 2 0
0 11 8 5
1
end_operator
begin_operator
down robot2 tile_1-1 tile_0-1
0
3
0 0 0 1
0 3 2 0
0 12 3 0
1
end_operator
begin_operator
down robot2 tile_1-2 tile_0-2
0
3
0 1 0 1
0 4 2 0
0 12 4 1
1
end_operator
begin_operator
down robot2 tile_1-3 tile_0-3
0
3
0 2 0 1
0 5 2 0
0 12 5 2
1
end_operator
begin_operator
down robot2 tile_2-1 tile_1-1
0
3
0 3 0 2
0 6 2 0
0 12 6 3
1
end_operator
begin_operator
down robot2 tile_2-2 tile_1-2
0
3
0 4 0 2
0 7 2 0
0 12 7 4
1
end_operator
begin_operator
down robot2 tile_2-3 tile_1-3
0
3
0 5 0 2
0 8 2 0
0 12 8 5
1
end_operator
begin_operator
left robot1 tile_0-2 tile_0-1
0
3
0 0 0 1
0 1 1 0
0 11 1 0
1
end_operator
begin_operator
left robot1 tile_0-3 tile_0-2
0
3
0 1 0 1
0 2 1 0
0 11 2 1
1
end_operator
begin_operator
left robot1 tile_1-2 tile_1-1
0
3
0 3 0 2
0 4 2 0
0 11 4 3
1
end_operator
begin_operator
left robot1 tile_1-3 tile_1-2
0
3
0 4 0 2
0 5 2 0
0 11 5 4
1
end_operator
begin_operator
left robot1 tile_2-2 tile_2-1
0
3
0 6 0 2
0 7 2 0
0 11 7 6
1
end_operator
begin_operator
left robot1 tile_2-3 tile_2-2
0
3
0 7 0 2
0 8 2 0
0 11 8 7
1
end_operator
begin_operator
left robot2 tile_0-2 tile_0-1
0
3
0 0 0 1
0 1 1 0
0 12 1 0
1
end_operator
begin_operator
left robot2 tile_0-3 tile_0-2
0
3
0 1 0 1
0 2 1 0
0 12 2 1
1
end_operator
begin_operator
left robot2 tile_1-2 tile_1-1
0
3
0 3 0 2
0 4 2 0
0 12 4 3
1
end_operator
begin_operator
left robot2 tile_1-3 tile_1-2
0
3
0 4 0 2
0 5 2 0
0 12 5 4
1
end_operator
begin_operator
left robot2 tile_2-2 tile_2-1
0
3
0 6 0 2
0 7 2 0
0 12 7 6
1
end_operator
begin_operator
left robot2 tile_2-3 tile_2-2
0
3
0 7 0 2
0 8 2 0
0 12 8 7
1
end_operator
begin_operator
paint-down robot1 tile_0-1 tile_1-1 black
1
11 3
2
0 0 0 1
0 9 -1 0
2
end_operator
begin_operator
paint-down robot1 tile_0-1 tile_1-1 white
1
11 3
2
0 0 0 1
0 10 -1 0
2
end_operator
begin_operator
paint-down robot1 tile_1-1 tile_2-1 white
1
11 6
1
0 3 0 1
2
end_operator
begin_operator
paint-down robot1 tile_1-2 tile_2-2 black
1
11 7
1
0 4 0 1
2
end_operator
begin_operator
paint-down robot1 tile_1-3 tile_2-3 white
1
11 8
1
0 5 0 1
2
end_operator
begin_operator
paint-down robot2 tile_0-1 tile_1-1 black
1
12 3
2
0 0 0 1
0 9 -1 0
2
end_operator
begin_operator
paint-down robot2 tile_0-1 tile_1-1 white
1
12 3
2
0 0 0 1
0 10 -1 0
2
end_operator
begin_operator
paint-down robot2 tile_1-1 tile_2-1 white
1
12 6
1
0 3 0 1
2
end_operator
begin_operator
paint-down robot2 tile_1-2 tile_2-2 black
1
12 7
1
0 4 0 1
2
end_operator
begin_operator
paint-down robot2 tile_1-3 tile_2-3 white
1
12 8
1
0 5 0 1
2
end_operator
begin_operator
paint-up robot1 tile_1-1 tile_0-1 white
1
11 0
1
0 3 0 1
2
end_operator
begin_operator
paint-up robot1 tile_1-2 tile_0-2 black
1
11 1
1
0 4 0 1
2
end_operator
begin_operator
paint-up robot1 tile_1-3 tile_0-3 white
1
11 2
1
0 5 0 1
2
end_operator
begin_operator
paint-up robot1 tile_2-1 tile_1-1 black
1
11 3
1
0 6 0 1
2
end_operator
begin_operator
paint-up robot1 tile_2-2 tile_1-2 white
1
11 4
1
0 7 0 1
2
end_operator
begin_operator
paint-up robot1 tile_2-3 tile_1-3 black
1
11 5
1
0 8 0 1
2
end_operator
begin_operator
paint-up robot2 tile_1-1 tile_0-1 white
1
12 0
1
0 3 0 1
2
end_operator
begin_operator
paint-up robot2 tile_1-2 tile_0-2 black
1
12 1
1
0 4 0 1
2
end_operator
begin_operator
paint-up robot2 tile_1-3 tile_0-3 white
1
12 2
1
0 5 0 1
2
end_operator
begin_operator
paint-up robot2 tile_2-1 tile_1-1 black
1
12 3
1
0 6 0 1
2
end_operator
begin_operator
paint-up robot2 tile_2-2 tile_1-2 white
1
12 4
1
0 7 0 1
2
end_operator
begin_operator
paint-up robot2 tile_2-3 tile_1-3 black
1
12 5
1
0 8 0 1
2
end_operator
begin_operator
right robot1 tile_0-1 tile_0-2
0
3
0 0 -1 0
0 1 0 1
0 11 0 1
1
end_operator
begin_operator
right robot1 tile_0-2 tile_0-3
0
3
0 1 1 0
0 2 0 1
0 11 1 2
1
end_operator
begin_operator
right robot1 tile_1-1 tile_1-2
0
3
0 3 2 0
0 4 0 2
0 11 3 4
1
end_operator
begin_operator
right robot1 tile_1-2 tile_1-3
0
3
0 4 2 0
0 5 0 2
0 11 4 5
1
end_operator
begin_operator
right robot1 tile_2-1 tile_2-2
0
3
0 6 2 0
0 7 0 2
0 11 6 7
1
end_operator
begin_operator
right robot1 tile_2-2 tile_2-3
0
3
0 7 2 0
0 8 0 2
0 11 7 8
1
end_operator
begin_operator
right robot2 tile_0-1 tile_0-2
0
3
0 0 -1 0
0 1 0 1
0 12 0 1
1
end_operator
begin_operator
right robot2 tile_0-2 tile_0-3
0
3
0 1 1 0
0 2 0 1
0 12 1 2
1
end_operator
begin_operator
right robot2 tile_1-1 tile_1-2
0
3
0 3 2 0
0 4 0 2
0 12 3 4
1
end_operator
begin_operator
right robot2 tile_1-2 tile_1-3
0
3
0 4 2 0
0 5 0 2
0 12 4 5
1
end_operator
begin_operator
right robot2 tile_2-1 tile_2-2
0
3
0 6 2 0
0 7 0 2
0 12 6 7
1
end_operator
begin_operator
right robot2 tile_2-2 tile_2-3
0
3
0 7 2 0
0 8 0 2
0 12 7 8
1
end_operator
begin_operator
up robot1 tile_0-1 tile_1-1
0
3
0 0 -1 0
0 3 0 2
0 11 0 3
3
end_operator
begin_operator
up robot1 tile_0-2 tile_1-2
0
3
0 1 1 0
0 4 0 2
0 11 1 4
3
end_operator
begin_operator
up robot1 tile_0-3 tile_1-3
0
3
0 2 1 0
0 5 0 2
0 11 2 5
3
end_operator
begin_operator
up robot1 tile_1-1 tile_2-1
0
3
0 3 2 0
0 6 0 2
0 11 3 6
3
end_operator
begin_operator
up robot1 tile_1-2 tile_2-2
0
3
0 4 2 0
0 7 0 2
0 11 4 7
3
end_operator
begin_operator
up robot1 tile_1-3 tile_2-3
0
3
0 5 2 0
0 8 0 2
0 11 5 8
3
end_operator
begin_operator
up robot2 tile_0-1 tile_1-1
0
3
0 0 -1 0
0 3 0 2
0 12 0 3
3
end_operator
begin_operator
up robot2 tile_0-2 tile_1-2
0
3
0 1 1 0
0 4 0 2
0 12 1 4
3
end_operator
begin_operator
up robot2 tile_0-3 tile_1-3
0
3
0 2 1 0
0 5 0 2
0 12 2 5
3
end_operator
begin_operator
up robot2 tile_1-1 tile_2-1
0
3
0 3 2 0
0 6 0 2
0 12 3 6
3
end_operator
begin_operator
up robot2 tile_1-2 tile_2-2
0
3
0 4 2 0
0 7 0 2
0 12 4 7
3
end_operator
begin_operator
up robot2 tile_1-3 tile_2-3
0
3
0 5 2 0
0 8 0 2
0 12 5 8
3
end_operator
0