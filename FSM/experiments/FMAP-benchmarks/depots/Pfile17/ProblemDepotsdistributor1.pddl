(define (problem depotprob6587)
(:domain depot)
(:objects
 depot0 depot1 - depot
 distributor0 distributor1 - distributor
 truck0 truck1 truck2 truck3 - truck
 crate0 crate1 crate2 crate3 crate4 crate5 crate6 crate7 crate8 crate9 - crate
 pallet0 pallet1 pallet2 pallet3 pallet4 pallet5 pallet6 pallet7 - pallet
 hoist0 hoist1 hoist2 hoist3 hoist4 hoist5 hoist6 hoist7 - hoist
)
(:shared-data
  (clear ?x - (either surface hoist))
  ((at ?t - truck) - place)
  ((pos ?c - crate) - (either place truck))
  ((on ?c - crate) - (either surface hoist truck)) - 
(either depot0 depot1 distributor0 truck0 truck1 truck2 truck3)
)
(:init
 (myAgent distributor1)
 (= (pos crate0) distributor1)
 (not (clear crate0))
 (= (on crate0) pallet3)
 (= (pos crate1) distributor1)
 (clear crate1)
 (= (on crate1) pallet5)
 (= (pos crate2) distributor0)
 (clear crate2)
 (= (on crate2) pallet4)
 (= (pos crate3) depot0)
 (clear crate3)
 (= (on crate3) pallet6)
 (= (pos crate4) depot1)
 (clear crate4)
 (= (on crate4) pallet1)
 (= (pos crate5) distributor1)
 (not (clear crate5))
 (= (on crate5) crate0)
 (= (pos crate6) distributor1)
 (not (clear crate6))
 (= (on crate6) pallet7)
 (= (pos crate7) distributor1)
 (clear crate7)
 (= (on crate7) crate5)
 (= (pos crate8) distributor1)
 (clear crate8)
 (= (on crate8) crate6)
 (= (pos crate9) distributor0)
 (clear crate9)
 (= (on crate9) pallet2)
 (= (at truck0) depot1)
 (= (at truck1) distributor1)
 (= (at truck2) depot1)
 (= (at truck3) depot0)
 (= (located hoist0) depot0)
 (clear hoist0)
 (= (located hoist1) depot1)
 (clear hoist1)
 (= (located hoist2) distributor0)
 (clear hoist2)
 (= (located hoist3) distributor1)
 (clear hoist3)
 (= (located hoist4) depot0)
 (clear hoist4)
 (= (located hoist5) depot0)
 (clear hoist5)
 (= (located hoist6) depot0)
 (clear hoist6)
 (= (located hoist7) distributor1)
 (clear hoist7)
 (= (placed pallet0) depot0)
 (clear pallet0)
 (= (placed pallet1) depot1)
 (not (clear pallet1))
 (= (placed pallet2) distributor0)
 (not (clear pallet2))
 (= (placed pallet3) distributor1)
 (not (clear pallet3))
 (= (placed pallet4) distributor0)
 (not (clear pallet4))
 (= (placed pallet5) distributor1)
 (not (clear pallet5))
 (= (placed pallet6) depot0)
 (not (clear pallet6))
 (= (placed pallet7) distributor1)
 (not (clear pallet7))
)
(:global-goal (and
 (= (on crate1) pallet7)
 (= (on crate2) pallet4)
 (= (on crate3) crate8)
 (= (on crate4) pallet0)
 (= (on crate6) pallet1)
 (= (on crate7) crate3)
 (= (on crate8) pallet6)
))

)
