(define (problem elevators-sequencedstrips-p12_4_2)
(:domain elevators)
(:objects
 n0 n1 n2 n3 n4 n5 n6 n7 n8 n9 n10 n11 n12 - count
 p0 p1 p2 p3 - passenger
 fast0 fast1 - fast-elevator
 slow0-0 slow1-0 slow2-0 - slow-elevator
)
(:shared-data
  ((at ?person - passenger) - (either count elevator)) - (either fast0 fast1 slow0-0 slow2-0)
)
(:init (myAgent slow1-0)
 (= (next n0) n1)
 (= (next n1) n2)
 (= (next n2) n3)
 (= (next n3) n4)
 (= (next n4) n5)
 (= (next n5) n6)
 (= (next n6) n7)
 (= (next n7) n8)
 (= (next n8) n9)
 (= (next n9) n10)
 (= (next n10) n11)
 (= (next n11) n12)
 (above n0 n1)
 (above n0 n2)
 (above n0 n3)
 (above n0 n4)
 (above n0 n5)
 (above n0 n6)
 (above n0 n7)
 (above n0 n8)
 (above n0 n9)
 (above n0 n10)
 (above n0 n11)
 (above n0 n12)
 (above n1 n2)
 (above n1 n3)
 (above n1 n4)
 (above n1 n5)
 (above n1 n6)
 (above n1 n7)
 (above n1 n8)
 (above n1 n9)
 (above n1 n10)
 (above n1 n11)
 (above n1 n12)
 (above n2 n3)
 (above n2 n4)
 (above n2 n5)
 (above n2 n6)
 (above n2 n7)
 (above n2 n8)
 (above n2 n9)
 (above n2 n10)
 (above n2 n11)
 (above n2 n12)
 (above n3 n4)
 (above n3 n5)
 (above n3 n6)
 (above n3 n7)
 (above n3 n8)
 (above n3 n9)
 (above n3 n10)
 (above n3 n11)
 (above n3 n12)
 (above n4 n5)
 (above n4 n6)
 (above n4 n7)
 (above n4 n8)
 (above n4 n9)
 (above n4 n10)
 (above n4 n11)
 (above n4 n12)
 (above n5 n6)
 (above n5 n7)
 (above n5 n8)
 (above n5 n9)
 (above n5 n10)
 (above n5 n11)
 (above n5 n12)
 (above n6 n7)
 (above n6 n8)
 (above n6 n9)
 (above n6 n10)
 (above n6 n11)
 (above n6 n12)
 (above n7 n8)
 (above n7 n9)
 (above n7 n10)
 (above n7 n11)
 (above n7 n12)
 (above n8 n9)
 (above n8 n10)
 (above n8 n11)
 (above n8 n12)
 (above n9 n10)
 (above n9 n11)
 (above n9 n12)
 (above n10 n11)
 (above n10 n12)
 (above n11 n12)
 (= (lift-at slow1-0) n6)
 (= (passengers slow1-0) n0)
 (can-hold slow1-0 n1)
 (can-hold slow1-0 n2)
 (reachable-floor slow1-0 n4)
 (reachable-floor slow1-0 n5)
 (reachable-floor slow1-0 n6)
 (reachable-floor slow1-0 n7)
 (reachable-floor slow1-0 n8)
 (= (at p0) n2)
 (= (at p1) n6)
 (= (at p2) n9)
 (= (at p3) n9)
)
(:global-goal (and
 (= (at p0) n7)
 (= (at p1) n10)
 (= (at p2) n5)
 (= (at p3) n4)
))

)
