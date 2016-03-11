(define (problem os-sequencedstrips-p22_1)
(:domain openstacks)
(:objects
 n0 n1 n2 n3 n4 n5 n6 n7 n8 n9 n10 n11 n12 n13 n14 n15 n16 n17 n18 n19 n20 n21 n22 - count
 manufacturer manager - agent)
(:shared-data
  (waiting ?o - order) (started ?o - order) (shipped ?o - order)
  (made ?p - product) ((stacks-avail) - count)
  ((next-count ?s) - count) - manufacturer)
(:init
 (= (next-count n0) n1)
 (= (stacks-avail) n0)
 (= (next-count n1) n2)
 (= (next-count n2) n3)
 (= (next-count n3) n4)
 (= (next-count n4) n5)
 (= (next-count n5) n6)
 (= (next-count n6) n7)
 (= (next-count n7) n8)
 (= (next-count n8) n9)
 (= (next-count n9) n10)
 (= (next-count n10) n11)
 (= (next-count n11) n12)
 (= (next-count n12) n13)
 (= (next-count n13) n14)
 (= (next-count n14) n15)
 (= (next-count n15) n16)
 (= (next-count n16) n17)
 (= (next-count n17) n18)
 (= (next-count n18) n19)
 (= (next-count n19) n20)
 (= (next-count n20) n21)
 (= (next-count n21) n22)
 (waiting o1)
 (not (started o1))
 (not (shipped o1))
 (not (includes o1 p1))
 (not (includes o1 p2))
 (not (includes o1 p3))
 (not (includes o1 p4))
 (not (includes o1 p5))
 (not (includes o1 p6))
 (not (includes o1 p7))
 (not (includes o1 p8))
 (includes o1 p9)
 (not (includes o1 p10))
 (not (includes o1 p11))
 (not (includes o1 p12))
 (not (includes o1 p13))
 (not (includes o1 p14))
 (not (includes o1 p15))
 (not (includes o1 p16))
 (not (includes o1 p17))
 (not (includes o1 p18))
 (not (includes o1 p19))
 (not (includes o1 p20))
 (not (includes o1 p21))
 (not (includes o1 p22))
 (waiting o2)
 (not (started o2))
 (not (shipped o2))
 (not (includes o2 p1))
 (not (includes o2 p2))
 (not (includes o2 p3))
 (not (includes o2 p4))
 (not (includes o2 p5))
 (not (includes o2 p6))
 (not (includes o2 p7))
 (not (includes o2 p8))
 (not (includes o2 p9))
 (not (includes o2 p10))
 (not (includes o2 p11))
 (not (includes o2 p12))
 (not (includes o2 p13))
 (not (includes o2 p14))
 (not (includes o2 p15))
 (not (includes o2 p16))
 (not (includes o2 p17))
 (not (includes o2 p18))
 (includes o2 p19)
 (not (includes o2 p20))
 (not (includes o2 p21))
 (not (includes o2 p22))
 (waiting o3)
 (not (started o3))
 (not (shipped o3))
 (not (includes o3 p1))
 (not (includes o3 p2))
 (not (includes o3 p3))
 (not (includes o3 p4))
 (not (includes o3 p5))
 (not (includes o3 p6))
 (includes o3 p7)
 (not (includes o3 p8))
 (not (includes o3 p9))
 (not (includes o3 p10))
 (not (includes o3 p11))
 (not (includes o3 p12))
 (not (includes o3 p13))
 (not (includes o3 p14))
 (not (includes o3 p15))
 (not (includes o3 p16))
 (not (includes o3 p17))
 (not (includes o3 p18))
 (not (includes o3 p19))
 (not (includes o3 p20))
 (includes o3 p21)
 (not (includes o3 p22))
 (waiting o4)
 (not (started o4))
 (not (shipped o4))
 (not (includes o4 p1))
 (not (includes o4 p2))
 (not (includes o4 p3))
 (not (includes o4 p4))
 (not (includes o4 p5))
 (not (includes o4 p6))
 (not (includes o4 p7))
 (not (includes o4 p8))
 (not (includes o4 p9))
 (includes o4 p10)
 (not (includes o4 p11))
 (not (includes o4 p12))
 (not (includes o4 p13))
 (not (includes o4 p14))
 (not (includes o4 p15))
 (not (includes o4 p16))
 (not (includes o4 p17))
 (not (includes o4 p18))
 (not (includes o4 p19))
 (not (includes o4 p20))
 (not (includes o4 p21))
 (not (includes o4 p22))
 (waiting o5)
 (not (started o5))
 (not (shipped o5))
 (not (includes o5 p1))
 (not (includes o5 p2))
 (not (includes o5 p3))
 (not (includes o5 p4))
 (not (includes o5 p5))
 (not (includes o5 p6))
 (not (includes o5 p7))
 (not (includes o5 p8))
 (not (includes o5 p9))
 (not (includes o5 p10))
 (not (includes o5 p11))
 (not (includes o5 p12))
 (not (includes o5 p13))
 (not (includes o5 p14))
 (includes o5 p15)
 (not (includes o5 p16))
 (not (includes o5 p17))
 (not (includes o5 p18))
 (includes o5 p19)
 (not (includes o5 p20))
 (not (includes o5 p21))
 (not (includes o5 p22))
 (waiting o6)
 (not (started o6))
 (not (shipped o6))
 (not (includes o6 p1))
 (not (includes o6 p2))
 (not (includes o6 p3))
 (not (includes o6 p4))
 (not (includes o6 p5))
 (not (includes o6 p6))
 (not (includes o6 p7))
 (not (includes o6 p8))
 (not (includes o6 p9))
 (not (includes o6 p10))
 (not (includes o6 p11))
 (includes o6 p12)
 (not (includes o6 p13))
 (not (includes o6 p14))
 (not (includes o6 p15))
 (not (includes o6 p16))
 (not (includes o6 p17))
 (not (includes o6 p18))
 (not (includes o6 p19))
 (not (includes o6 p20))
 (not (includes o6 p21))
 (not (includes o6 p22))
 (waiting o7)
 (not (started o7))
 (not (shipped o7))
 (not (includes o7 p1))
 (not (includes o7 p2))
 (not (includes o7 p3))
 (not (includes o7 p4))
 (not (includes o7 p5))
 (not (includes o7 p6))
 (not (includes o7 p7))
 (not (includes o7 p8))
 (not (includes o7 p9))
 (not (includes o7 p10))
 (not (includes o7 p11))
 (includes o7 p12)
 (not (includes o7 p13))
 (not (includes o7 p14))
 (not (includes o7 p15))
 (not (includes o7 p16))
 (not (includes o7 p17))
 (not (includes o7 p18))
 (not (includes o7 p19))
 (not (includes o7 p20))
 (not (includes o7 p21))
 (not (includes o7 p22))
 (waiting o8)
 (not (started o8))
 (not (shipped o8))
 (not (includes o8 p1))
 (not (includes o8 p2))
 (not (includes o8 p3))
 (not (includes o8 p4))
 (not (includes o8 p5))
 (not (includes o8 p6))
 (not (includes o8 p7))
 (includes o8 p8)
 (not (includes o8 p9))
 (not (includes o8 p10))
 (not (includes o8 p11))
 (includes o8 p12)
 (not (includes o8 p13))
 (not (includes o8 p14))
 (includes o8 p15)
 (not (includes o8 p16))
 (not (includes o8 p17))
 (not (includes o8 p18))
 (not (includes o8 p19))
 (not (includes o8 p20))
 (not (includes o8 p21))
 (not (includes o8 p22))
 (waiting o9)
 (not (started o9))
 (not (shipped o9))
 (not (includes o9 p1))
 (not (includes o9 p2))
 (includes o9 p3)
 (not (includes o9 p4))
 (not (includes o9 p5))
 (not (includes o9 p6))
 (not (includes o9 p7))
 (not (includes o9 p8))
 (not (includes o9 p9))
 (not (includes o9 p10))
 (includes o9 p11)
 (not (includes o9 p12))
 (not (includes o9 p13))
 (not (includes o9 p14))
 (not (includes o9 p15))
 (not (includes o9 p16))
 (not (includes o9 p17))
 (includes o9 p18)
 (not (includes o9 p19))
 (not (includes o9 p20))
 (not (includes o9 p21))
 (not (includes o9 p22))
 (waiting o10)
 (not (started o10))
 (not (shipped o10))
 (not (includes o10 p1))
 (not (includes o10 p2))
 (not (includes o10 p3))
 (includes o10 p4)
 (not (includes o10 p5))
 (not (includes o10 p6))
 (not (includes o10 p7))
 (not (includes o10 p8))
 (not (includes o10 p9))
 (not (includes o10 p10))
 (not (includes o10 p11))
 (not (includes o10 p12))
 (not (includes o10 p13))
 (not (includes o10 p14))
 (not (includes o10 p15))
 (not (includes o10 p16))
 (not (includes o10 p17))
 (not (includes o10 p18))
 (not (includes o10 p19))
 (not (includes o10 p20))
 (not (includes o10 p21))
 (not (includes o10 p22))
 (waiting o11)
 (not (started o11))
 (not (shipped o11))
 (not (includes o11 p1))
 (not (includes o11 p2))
 (not (includes o11 p3))
 (not (includes o11 p4))
 (not (includes o11 p5))
 (not (includes o11 p6))
 (not (includes o11 p7))
 (not (includes o11 p8))
 (not (includes o11 p9))
 (not (includes o11 p10))
 (not (includes o11 p11))
 (not (includes o11 p12))
 (not (includes o11 p13))
 (includes o11 p14)
 (not (includes o11 p15))
 (not (includes o11 p16))
 (not (includes o11 p17))
 (not (includes o11 p18))
 (not (includes o11 p19))
 (not (includes o11 p20))
 (not (includes o11 p21))
 (not (includes o11 p22))
 (waiting o12)
 (not (started o12))
 (not (shipped o12))
 (not (includes o12 p1))
 (not (includes o12 p2))
 (includes o12 p3)
 (not (includes o12 p4))
 (not (includes o12 p5))
 (not (includes o12 p6))
 (not (includes o12 p7))
 (not (includes o12 p8))
 (not (includes o12 p9))
 (not (includes o12 p10))
 (not (includes o12 p11))
 (not (includes o12 p12))
 (not (includes o12 p13))
 (not (includes o12 p14))
 (not (includes o12 p15))
 (not (includes o12 p16))
 (not (includes o12 p17))
 (not (includes o12 p18))
 (not (includes o12 p19))
 (not (includes o12 p20))
 (not (includes o12 p21))
 (not (includes o12 p22))
 (waiting o13)
 (not (started o13))
 (not (shipped o13))
 (not (includes o13 p1))
 (not (includes o13 p2))
 (includes o13 p3)
 (not (includes o13 p4))
 (not (includes o13 p5))
 (includes o13 p6)
 (includes o13 p7)
 (includes o13 p8)
 (includes o13 p9)
 (not (includes o13 p10))
 (not (includes o13 p11))
 (not (includes o13 p12))
 (includes o13 p13)
 (not (includes o13 p14))
 (not (includes o13 p15))
 (not (includes o13 p16))
 (not (includes o13 p17))
 (not (includes o13 p18))
 (not (includes o13 p19))
 (not (includes o13 p20))
 (not (includes o13 p21))
 (not (includes o13 p22))
 (waiting o14)
 (not (started o14))
 (not (shipped o14))
 (not (includes o14 p1))
 (not (includes o14 p2))
 (not (includes o14 p3))
 (not (includes o14 p4))
 (not (includes o14 p5))
 (not (includes o14 p6))
 (not (includes o14 p7))
 (not (includes o14 p8))
 (not (includes o14 p9))
 (includes o14 p10)
 (not (includes o14 p11))
 (not (includes o14 p12))
 (not (includes o14 p13))
 (not (includes o14 p14))
 (not (includes o14 p15))
 (not (includes o14 p16))
 (not (includes o14 p17))
 (not (includes o14 p18))
 (not (includes o14 p19))
 (not (includes o14 p20))
 (not (includes o14 p21))
 (includes o14 p22)
 (waiting o15)
 (not (started o15))
 (not (shipped o15))
 (includes o15 p1)
 (not (includes o15 p2))
 (not (includes o15 p3))
 (not (includes o15 p4))
 (not (includes o15 p5))
 (not (includes o15 p6))
 (not (includes o15 p7))
 (not (includes o15 p8))
 (not (includes o15 p9))
 (not (includes o15 p10))
 (not (includes o15 p11))
 (not (includes o15 p12))
 (not (includes o15 p13))
 (not (includes o15 p14))
 (not (includes o15 p15))
 (includes o15 p16)
 (includes o15 p17)
 (not (includes o15 p18))
 (not (includes o15 p19))
 (not (includes o15 p20))
 (not (includes o15 p21))
 (not (includes o15 p22))
 (waiting o16)
 (not (started o16))
 (not (shipped o16))
 (not (includes o16 p1))
 (not (includes o16 p2))
 (not (includes o16 p3))
 (not (includes o16 p4))
 (not (includes o16 p5))
 (not (includes o16 p6))
 (not (includes o16 p7))
 (not (includes o16 p8))
 (not (includes o16 p9))
 (not (includes o16 p10))
 (includes o16 p11)
 (not (includes o16 p12))
 (not (includes o16 p13))
 (not (includes o16 p14))
 (not (includes o16 p15))
 (not (includes o16 p16))
 (not (includes o16 p17))
 (not (includes o16 p18))
 (not (includes o16 p19))
 (not (includes o16 p20))
 (not (includes o16 p21))
 (not (includes o16 p22))
 (waiting o17)
 (not (started o17))
 (not (shipped o17))
 (not (includes o17 p1))
 (not (includes o17 p2))
 (not (includes o17 p3))
 (not (includes o17 p4))
 (not (includes o17 p5))
 (not (includes o17 p6))
 (not (includes o17 p7))
 (not (includes o17 p8))
 (not (includes o17 p9))
 (not (includes o17 p10))
 (not (includes o17 p11))
 (not (includes o17 p12))
 (not (includes o17 p13))
 (not (includes o17 p14))
 (includes o17 p15)
 (not (includes o17 p16))
 (not (includes o17 p17))
 (not (includes o17 p18))
 (not (includes o17 p19))
 (not (includes o17 p20))
 (not (includes o17 p21))
 (not (includes o17 p22))
 (waiting o18)
 (not (started o18))
 (not (shipped o18))
 (not (includes o18 p1))
 (not (includes o18 p2))
 (not (includes o18 p3))
 (not (includes o18 p4))
 (not (includes o18 p5))
 (not (includes o18 p6))
 (not (includes o18 p7))
 (not (includes o18 p8))
 (not (includes o18 p9))
 (not (includes o18 p10))
 (not (includes o18 p11))
 (not (includes o18 p12))
 (not (includes o18 p13))
 (not (includes o18 p14))
 (not (includes o18 p15))
 (not (includes o18 p16))
 (includes o18 p17)
 (not (includes o18 p18))
 (not (includes o18 p19))
 (not (includes o18 p20))
 (not (includes o18 p21))
 (not (includes o18 p22))
 (waiting o19)
 (not (started o19))
 (not (shipped o19))
 (not (includes o19 p1))
 (not (includes o19 p2))
 (not (includes o19 p3))
 (not (includes o19 p4))
 (not (includes o19 p5))
 (not (includes o19 p6))
 (not (includes o19 p7))
 (not (includes o19 p8))
 (not (includes o19 p9))
 (not (includes o19 p10))
 (not (includes o19 p11))
 (not (includes o19 p12))
 (not (includes o19 p13))
 (not (includes o19 p14))
 (not (includes o19 p15))
 (not (includes o19 p16))
 (not (includes o19 p17))
 (not (includes o19 p18))
 (not (includes o19 p19))
 (includes o19 p20)
 (not (includes o19 p21))
 (not (includes o19 p22))
 (waiting o20)
 (not (started o20))
 (not (shipped o20))
 (not (includes o20 p1))
 (includes o20 p2)
 (not (includes o20 p3))
 (not (includes o20 p4))
 (includes o20 p5)
 (not (includes o20 p6))
 (not (includes o20 p7))
 (includes o20 p8)
 (not (includes o20 p9))
 (not (includes o20 p10))
 (not (includes o20 p11))
 (not (includes o20 p12))
 (not (includes o20 p13))
 (not (includes o20 p14))
 (not (includes o20 p15))
 (not (includes o20 p16))
 (not (includes o20 p17))
 (not (includes o20 p18))
 (not (includes o20 p19))
 (not (includes o20 p20))
 (not (includes o20 p21))
 (not (includes o20 p22))
 (waiting o21)
 (not (started o21))
 (not (shipped o21))
 (not (includes o21 p1))
 (not (includes o21 p2))
 (not (includes o21 p3))
 (not (includes o21 p4))
 (not (includes o21 p5))
 (not (includes o21 p6))
 (not (includes o21 p7))
 (not (includes o21 p8))
 (not (includes o21 p9))
 (not (includes o21 p10))
 (not (includes o21 p11))
 (not (includes o21 p12))
 (not (includes o21 p13))
 (includes o21 p14)
 (not (includes o21 p15))
 (not (includes o21 p16))
 (not (includes o21 p17))
 (not (includes o21 p18))
 (not (includes o21 p19))
 (not (includes o21 p20))
 (not (includes o21 p21))
 (not (includes o21 p22))
 (waiting o22)
 (not (started o22))
 (not (shipped o22))
 (not (includes o22 p1))
 (not (includes o22 p2))
 (not (includes o22 p3))
 (not (includes o22 p4))
 (not (includes o22 p5))
 (not (includes o22 p6))
 (not (includes o22 p7))
 (not (includes o22 p8))
 (not (includes o22 p9))
 (not (includes o22 p10))
 (not (includes o22 p11))
 (not (includes o22 p12))
 (not (includes o22 p13))
 (includes o22 p14)
 (not (includes o22 p15))
 (not (includes o22 p16))
 (not (includes o22 p17))
 (not (includes o22 p18))
 (not (includes o22 p19))
 (not (includes o22 p20))
 (not (includes o22 p21))
 (not (includes o22 p22))
 (not (made p1))
 (not (made p2))
 (not (made p3))
 (not (made p4))
 (not (made p5))
 (not (made p6))
 (not (made p7))
 (not (made p8))
 (not (made p9))
 (not (made p10))
 (not (made p11))
 (not (made p12))
 (not (made p13))
 (not (made p14))
 (not (made p15))
 (not (made p16))
 (not (made p17))
 (not (made p18))
 (not (made p19))
 (not (made p20))
 (not (made p21))
 (not (made p22))
)
(:global-goal (and
 (shipped o1)
 (shipped o2)
 (shipped o3)
 (shipped o4)
 (shipped o5)
 (shipped o6)
 (shipped o7)
 (shipped o8)
 (shipped o9)
 (shipped o10)
 (shipped o11)
 (shipped o12)
 (shipped o13)
 (shipped o14)
 (shipped o15)
 (shipped o16)
 (shipped o17)
 (shipped o18)
 (shipped o19)
 (shipped o20)
 (shipped o21)
 (shipped o22)
))

)
