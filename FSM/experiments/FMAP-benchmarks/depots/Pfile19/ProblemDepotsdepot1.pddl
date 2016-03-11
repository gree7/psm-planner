(define (problem depotprob6178)
(:domain depot)
(:objects
 depot0 depot1 depot2 depot3 - depot
 distributor0 distributor1 distributor2 distributor3 - distributor
 truck0 truck1 truck2 truck3 - truck
 crate0 crate1 crate2 crate3 crate4 crate5 crate6 crate7 - crate
 pallet0 pallet1 pallet2 pallet3 pallet4 pallet5 pallet6 pallet7 pallet8 pallet9 - pallet
 hoist0 hoist1 hoist2 hoist3 hoist4 hoist5 hoist6 hoist7 - hoist
)
(:shared-data
  (clear ?x - (either surface hoist))
  ((at ?t - truck) - place)
  ((pos ?c - crate) - (either place truck))
  ((on ?c - crate) - (either surface hoist truck)) - 
(either depot0 depot2 depot3 distributor0 distributor1 distributor2 distributor3 truck0 truck1 truck2 truck3)
)
(:init
 (myAgent depot1)
 (= (pos crate0) depot3)
 (clear crate0)
 (= (on crate0) pallet9)
 (= (pos crate1) depot1)
 (clear crate1)
 (= (on crate1) pallet1)
 (= (pos crate2) distributor0)
 (clear crate2)
 (= (on crate2) pallet4)
 (= (pos crate3) distributor1)
 (not (clear crate3))
 (= (on crate3) pallet5)
 (= (pos crate4) distributor2)
 (clear crate4)
 (= (on crate4) pallet8)
 (= (pos crate5) distributor1)
 (clear crate5)
 (= (on crate5) crate3)
 (= (pos crate6) depot0)
 (clear crate6)
 (= (on crate6) pallet0)
 (= (pos crate7) depot3)
 (clear crate7)
 (= (on crate7) pallet3)
 (= (at truck0) depot0)
 (= (at truck1) distributor0)
 (= (at truck2) depot2)
 (= (at truck3) distributor3)
 (= (located hoist0) depot0)
 (clear hoist0)
 (= (located hoist1) depot1)
 (clear hoist1)
 (= (located hoist2) depot2)
 (clear hoist2)
 (= (located hoist3) depot3)
 (clear hoist3)
 (= (located hoist4) distributor0)
 (clear hoist4)
 (= (located hoist5) distributor1)
 (clear hoist5)
 (= (located hoist6) distributor2)
 (clear hoist6)
 (= (located hoist7) distributor3)
 (clear hoist7)
 (= (placed pallet0) depot0)
 (not (clear pallet0))
 (= (placed pallet1) depot1)
 (not (clear pallet1))
 (= (placed pallet2) depot2)
 (clear pallet2)
 (= (placed pallet3) depot3)
 (not (clear pallet3))
 (= (placed pallet4) distributor0)
 (not (clear pallet4))
 (= (placed pallet5) distributor1)
 (not (clear pallet5))
 (= (placed pallet6) distributor2)
 (clear pallet6)
 (= (placed pallet7) distributor3)
 (clear pallet7)
 (= (placed pallet8) distributor2)
 (not (clear pallet8))
 (= (placed pallet9) depot3)
 (not (clear pallet9))
)
(:global-goal (and
 (= (on crate0) pallet6)
 (= (on crate1) pallet8)
 (= (on crate3) crate1)
 (= (on crate4) pallet5)
 (= (on crate5) crate7)
 (= (on crate6) pallet4)
 (= (on crate7) crate4)
))

)
