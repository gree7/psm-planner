begin_version
3
end_version
begin_metric
0
end_metric
21
begin_variable
var0
-1
9
Atom at-segment(airplane_cfbeg, seg_pp_0_60)
Atom at-segment(airplane_cfbeg, seg_ppdoor_0_40)
Atom at-segment(airplane_cfbeg, seg_rw_0_400)
Atom at-segment(airplane_cfbeg, seg_rwe_0_50)
Atom at-segment(airplane_cfbeg, seg_twe1_0_200)
Atom at-segment(airplane_cfbeg, seg_twe2_0_50)
Atom at-segment(airplane_cfbeg, seg_twe3_0_50)
Atom at-segment(airplane_cfbeg, seg_twe4_0_50)
Atom at-segment(airplane_cfbeg, seg_tww1_0_200)
end_variable
begin_variable
var1
-1
2
Atom blocked(seg_pp_0_60, airplane_cfbeg)
NegatedAtom blocked(seg_pp_0_60, airplane_cfbeg)
end_variable
begin_variable
var2
-1
2
Atom blocked(seg_ppdoor_0_40, airplane_cfbeg)
NegatedAtom blocked(seg_ppdoor_0_40, airplane_cfbeg)
end_variable
begin_variable
var3
-1
2
Atom blocked(seg_rwe_0_50, airplane_cfbeg)
NegatedAtom blocked(seg_rwe_0_50, airplane_cfbeg)
end_variable
begin_variable
var4
-1
2
Atom blocked(seg_twe1_0_200, airplane_cfbeg)
NegatedAtom blocked(seg_twe1_0_200, airplane_cfbeg)
end_variable
begin_variable
var5
-1
2
Atom blocked(seg_twe2_0_50, airplane_cfbeg)
NegatedAtom blocked(seg_twe2_0_50, airplane_cfbeg)
end_variable
begin_variable
var6
-1
2
Atom blocked(seg_twe3_0_50, airplane_cfbeg)
NegatedAtom blocked(seg_twe3_0_50, airplane_cfbeg)
end_variable
begin_variable
var7
-1
2
Atom blocked(seg_twe4_0_50, airplane_cfbeg)
NegatedAtom blocked(seg_twe4_0_50, airplane_cfbeg)
end_variable
begin_variable
var8
-1
2
Atom blocked(seg_tww1_0_200, airplane_cfbeg)
NegatedAtom blocked(seg_tww1_0_200, airplane_cfbeg)
end_variable
begin_variable
var9
-1
2
Atom blocked(seg_tww2_0_50, airplane_cfbeg)
NegatedAtom blocked(seg_tww2_0_50, airplane_cfbeg)
end_variable
begin_variable
var10
-1
2
Atom facing(airplane_cfbeg, north)
Atom facing(airplane_cfbeg, south)
end_variable
begin_variable
var11
-1
2
Atom is-moving(airplane_cfbeg)
Atom is-parked(airplane_cfbeg, seg_pp_0_60)
end_variable
begin_variable
var12
-1
2
Atom occupied(seg_pp_0_60)
NegatedAtom occupied(seg_pp_0_60)
end_variable
begin_variable
var13
-1
2
Atom occupied(seg_ppdoor_0_40)
NegatedAtom occupied(seg_ppdoor_0_40)
end_variable
begin_variable
var14
-1
2
Atom occupied(seg_rw_0_400)
NegatedAtom occupied(seg_rw_0_400)
end_variable
begin_variable
var15
-1
2
Atom occupied(seg_rwe_0_50)
NegatedAtom occupied(seg_rwe_0_50)
end_variable
begin_variable
var16
-1
2
Atom occupied(seg_twe1_0_200)
NegatedAtom occupied(seg_twe1_0_200)
end_variable
begin_variable
var17
-1
2
Atom occupied(seg_twe2_0_50)
NegatedAtom occupied(seg_twe2_0_50)
end_variable
begin_variable
var18
-1
2
Atom occupied(seg_twe3_0_50)
NegatedAtom occupied(seg_twe3_0_50)
end_variable
begin_variable
var19
-1
2
Atom occupied(seg_twe4_0_50)
NegatedAtom occupied(seg_twe4_0_50)
end_variable
begin_variable
var20
-1
2
Atom occupied(seg_tww1_0_200)
NegatedAtom occupied(seg_tww1_0_200)
end_variable
18
begin_mutex_group
9
0 0
0 1
0 2
0 3
0 4
0 5
0 6
0 7
0 8
end_mutex_group
begin_mutex_group
1
11 0
end_mutex_group
begin_mutex_group
2
10 0
10 1
end_mutex_group
begin_mutex_group
2
11 0
11 1
end_mutex_group
begin_mutex_group
2
16 1
0 4
end_mutex_group
begin_mutex_group
2
15 1
0 3
end_mutex_group
begin_mutex_group
2
14 1
0 2
end_mutex_group
begin_mutex_group
2
9 0
0 4
end_mutex_group
begin_mutex_group
2
3 1
0 3
end_mutex_group
begin_mutex_group
2
17 1
0 5
end_mutex_group
begin_mutex_group
2
20 1
0 8
end_mutex_group
begin_mutex_group
2
19 1
0 7
end_mutex_group
begin_mutex_group
2
18 1
0 6
end_mutex_group
begin_mutex_group
2
13 1
0 1
end_mutex_group
begin_mutex_group
2
8 0
1 0
end_mutex_group
begin_mutex_group
2
12 1
0 0
end_mutex_group
begin_mutex_group
2
10 0
0 4
end_mutex_group
begin_mutex_group
2
10 0
9 1
end_mutex_group
begin_state
8
1
1
1
1
1
1
1
0
0
0
0
1
1
1
1
1
1
1
1
0
end_state
begin_goal
1
11 1
end_goal
12
begin_operator
move_seg_pp_0_60_seg_ppdoor_0_40_north_north_medium airplane_cfbeg
2
10 0
11 0
4
0 0 0 1
0 2 1 0
0 12 0 1
0 13 1 0
1
end_operator
begin_operator
move_seg_ppdoor_0_40_seg_pp_0_60_south_south_medium airplane_cfbeg
2
10 1
11 0
6
0 0 1 0
0 1 1 0
0 4 -1 1
0 8 -1 1
0 12 1 0
0 13 0 1
1
end_operator
begin_operator
move_seg_ppdoor_0_40_seg_tww1_0_200_north_south_medium airplane_cfbeg
2
11 0
16 1
7
0 0 1 8
0 1 -1 1
0 4 -1 0
0 8 1 0
0 10 0 1
0 13 0 1
0 20 1 0
1
end_operator
begin_operator
move_seg_rwe_0_50_seg_rw_0_400_south_south_medium airplane_cfbeg
2
10 1
11 0
4
0 0 3 2
0 7 -1 1
0 14 1 0
0 15 0 1
1
end_operator
begin_operator
move_seg_twe1_0_200_seg_twe2_0_50_south_south_medium airplane_cfbeg
2
10 1
11 0
6
0 0 4 5
0 2 -1 1
0 5 1 0
0 8 -1 1
0 16 0 1
0 17 1 0
1
end_operator
begin_operator
move_seg_twe2_0_50_seg_twe3_0_50_south_south_medium airplane_cfbeg
2
10 1
11 0
5
0 0 5 6
0 4 -1 1
0 6 1 0
0 17 0 1
0 18 1 0
1
end_operator
begin_operator
move_seg_twe3_0_50_seg_twe4_0_50_south_south_medium airplane_cfbeg
2
10 1
11 0
5
0 0 6 7
0 5 -1 1
0 7 1 0
0 18 0 1
0 19 1 0
1
end_operator
begin_operator
move_seg_twe4_0_50_seg_rwe_0_50_south_south_medium airplane_cfbeg
2
10 1
11 0
5
0 0 7 3
0 3 1 0
0 6 -1 1
0 15 1 0
0 19 0 1
1
end_operator
begin_operator
move_seg_tww1_0_200_seg_ppdoor_0_40_north_south_medium airplane_cfbeg
2
11 0
16 1
7
0 0 8 1
0 2 1 0
0 4 -1 0
0 9 0 1
0 10 0 1
0 13 1 0
0 20 0 1
1
end_operator
begin_operator
move_seg_tww1_0_200_seg_twe1_0_200_north_south_medium airplane_cfbeg
2
11 0
13 1
7
0 0 8 4
0 2 -1 0
0 4 1 0
0 9 0 1
0 10 0 1
0 16 1 0
0 20 0 1
1
end_operator
begin_operator
park_seg_pp_0_60_north airplane_cfbeg
2
0 0
10 0
1
0 11 0 1
1
end_operator
begin_operator
park_seg_pp_0_60_south airplane_cfbeg
2
0 0
10 1
2
0 2 -1 1
0 11 0 1
1
end_operator
0