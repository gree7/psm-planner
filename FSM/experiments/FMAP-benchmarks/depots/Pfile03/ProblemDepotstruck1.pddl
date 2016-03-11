(define (problem depotprob1935)
(:domain depot)
(:objects
 depot0 - depot
 distributor0 distributor1 - distributor
 truck0 truck1 - truck
 crate0 crate1 crate2 crate3 crate4 crate5 - crate
 pallet0 pallet1 pallet2 - pallet
 hoist0 hoist1 hoist2 - hoist
)
(:shared-data
  (clear ?x - (either surface hoist))
  ((at ?t - truck) - place)
  ((pos ?c - crate) - (either place truck))
  ((on ?c - crate) - (either surface hoist truck)) - 
(either depot0 distributor0 distributor1 truck0)
)
(:init
 (myAgent truck1)
 (= (pos crate0) distributor0)
 (not (clear crate0))
 (= (on crate0) pallet1)
 (= (pos crate1) depot0)
 (clear crate1)
 (= (on crate1) pallet0)
 (= (pos crate2) distributor1)
 (not (clear crate2))
 (= (on crate2) pallet2)
 (= (pos crate3) distributor0)
 (not (clear crate3))
 (= (on crate3) crate0)
 (= (pos crate4) distributor0)
 (clear crate4)
 (= (on crate4) crate3)
 (= (pos crate5) distributor1)
 (clear crate5)
 (= (on crate5) crate2)
 (= (at truck0) depot0)
 (= (at truck1) distributor0)
 (= (located hoist0) depot0)
 (clear hoist0)
 (= (located hoist1) distributor0)
 (clear hoist1)
 (= (located hoist2) distributor1)
 (clear hoist2)
 (= (placed pallet0) depot0)
 (not (clear pallet0))
 (= (placed pallet1) distributor0)
 (not (clear pallet1))
 (= (placed pallet2) distributor1)
 (not (clear pallet2))
)
(:global-goal (and
 (= (on crate0) crate1)
 (= (on crate1) pallet2)
 (= (on crate2) pallet0)
 (= (on crate3) crate2)
 (= (on crate4) pallet1)
 (= (on crate5) crate0)
))

)
