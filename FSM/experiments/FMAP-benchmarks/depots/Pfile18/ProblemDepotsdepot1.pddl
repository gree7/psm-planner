(define (problem depotprob1916)
(:domain depot)
(:objects
 depot0 depot1 - depot
 distributor0 distributor1 - distributor
 truck0 truck1 truck2 truck3 - truck
 crate0 crate1 crate2 crate3 crate4 crate5 crate6 crate7 crate8 crate9 crate10 crate11 crate12 crate13 crate14 - crate
 pallet0 pallet1 pallet2 pallet3 pallet4 pallet5 pallet6 pallet7 - pallet
 hoist0 hoist1 hoist2 hoist3 hoist4 hoist5 hoist6 hoist7 - hoist
)
(:shared-data
  (clear ?x - (either surface hoist))
  ((at ?t - truck) - place)
  ((pos ?c - crate) - (either place truck))
  ((on ?c - crate) - (either surface hoist truck)) - 
(either depot0 distributor0 distributor1 truck0 truck1 truck2 truck3)
)
(:init
 (myAgent depot1)
 (= (pos crate0) depot0)
 (not (clear crate0))
 (= (on crate0) pallet0)
 (= (pos crate1) depot1)
 (not (clear crate1))
 (= (on crate1) pallet1)
 (= (pos crate2) distributor0)
 (not (clear crate2))
 (= (on crate2) pallet2)
 (= (pos crate3) distributor1)
 (clear crate3)
 (= (on crate3) pallet6)
 (= (pos crate4) depot0)
 (not (clear crate4))
 (= (on crate4) crate0)
 (= (pos crate5) depot1)
 (clear crate5)
 (= (on crate5) pallet7)
 (= (pos crate6) distributor0)
 (not (clear crate6))
 (= (on crate6) pallet5)
 (= (pos crate7) depot0)
 (not (clear crate7))
 (= (on crate7) crate4)
 (= (pos crate8) distributor0)
 (clear crate8)
 (= (on crate8) crate6)
 (= (pos crate9) distributor1)
 (not (clear crate9))
 (= (on crate9) pallet3)
 (= (pos crate10) distributor1)
 (clear crate10)
 (= (on crate10) crate9)
 (= (pos crate11) depot1)
 (clear crate11)
 (= (on crate11) crate1)
 (= (pos crate12) distributor0)
 (not (clear crate12))
 (= (on crate12) crate2)
 (= (pos crate13) depot0)
 (clear crate13)
 (= (on crate13) crate7)
 (= (pos crate14) distributor0)
 (clear crate14)
 (= (on crate14) crate12)
 (= (at truck0) depot1)
 (= (at truck1) distributor0)
 (= (at truck2) depot0)
 (= (at truck3) depot1)
 (= (located hoist0) depot0)
 (clear hoist0)
 (= (located hoist1) depot1)
 (clear hoist1)
 (= (located hoist2) distributor0)
 (clear hoist2)
 (= (located hoist3) distributor1)
 (clear hoist3)
 (= (located hoist4) distributor0)
 (clear hoist4)
 (= (located hoist5) depot0)
 (clear hoist5)
 (= (located hoist6) distributor0)
 (clear hoist6)
 (= (located hoist7) distributor1)
 (clear hoist7)
 (= (placed pallet0) depot0)
 (not (clear pallet0))
 (= (placed pallet1) depot1)
 (not (clear pallet1))
 (= (placed pallet2) distributor0)
 (not (clear pallet2))
 (= (placed pallet3) distributor1)
 (not (clear pallet3))
 (= (placed pallet4) depot0)
 (clear pallet4)
 (= (placed pallet5) distributor0)
 (not (clear pallet5))
 (= (placed pallet6) distributor1)
 (not (clear pallet6))
 (= (placed pallet7) depot1)
 (not (clear pallet7))
)
(:global-goal (and
 (= (on crate0) crate10)
 (= (on crate1) pallet6)
 (= (on crate2) crate12)
 (= (on crate4) pallet4)
 (= (on crate5) pallet2)
 (= (on crate6) pallet7)
 (= (on crate8) crate4)
 (= (on crate9) crate1)
 (= (on crate10) pallet1)
 (= (on crate11) pallet5)
 (= (on crate12) crate5)
 (= (on crate13) pallet3)
 (= (on crate14) pallet0)
))

)
