(define (problem depotprob4398)
(:domain depot)
(:objects
 depot0 depot1 - depot
 distributor0 distributor1 - distributor
 truck0 truck1 truck2 truck3 - truck
 crate0 crate1 crate2 crate3 crate4 crate5 - crate
 pallet0 pallet1 pallet2 pallet3 pallet4 pallet5 pallet6 pallet7 - pallet
 hoist0 hoist1 hoist2 hoist3 hoist4 hoist5 hoist6 hoist7 - hoist
)
(:shared-data
  (clear ?x - (either surface hoist))
  ((at ?t - truck) - place)
  ((pos ?c - crate) - (either place truck))
  ((on ?c - crate) - (either surface hoist truck)) - 
(either depot0 depot1 distributor0 distributor1 truck0 truck1 truck2)
)
(:init
 (myAgent truck3)
 (= (pos crate0) depot1)
 (clear crate0)
 (= (on crate0) pallet4)
 (= (pos crate1) depot1)
 (not (clear crate1))
 (= (on crate1) pallet1)
 (= (pos crate2) depot0)
 (not (clear crate2))
 (= (on crate2) pallet0)
 (= (pos crate3) depot1)
 (clear crate3)
 (= (on crate3) crate1)
 (= (pos crate4) distributor0)
 (clear crate4)
 (= (on crate4) pallet2)
 (= (pos crate5) depot0)
 (clear crate5)
 (= (on crate5) crate2)
 (= (at truck0) depot1)
 (= (at truck1) depot1)
 (= (at truck2) depot0)
 (= (at truck3) distributor1)
 (= (located hoist0) depot0)
 (clear hoist0)
 (= (located hoist1) depot1)
 (clear hoist1)
 (= (located hoist2) distributor0)
 (clear hoist2)
 (= (located hoist3) distributor1)
 (clear hoist3)
 (= (located hoist4) distributor1)
 (clear hoist4)
 (= (located hoist5) depot1)
 (clear hoist5)
 (= (located hoist6) depot1)
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
 (clear pallet3)
 (= (placed pallet4) depot1)
 (not (clear pallet4))
 (= (placed pallet5) distributor1)
 (clear pallet5)
 (= (placed pallet6) depot1)
 (clear pallet6)
 (= (placed pallet7) distributor0)
 (clear pallet7)
)
(:global-goal (and
 (= (on crate0) pallet3)
 (= (on crate2) pallet1)
 (= (on crate3) pallet0)
 (= (on crate4) crate3)
 (= (on crate5) pallet2)
))

)
