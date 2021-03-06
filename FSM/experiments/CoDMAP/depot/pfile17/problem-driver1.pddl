(define (problem depotprob6587) (:domain depot)
(:objects
	distributor1 - distributor
	distributor0 - distributor
	depot0 - depot
	depot1 - depot
	pallet5 - pallet
	pallet4 - pallet
	pallet7 - pallet
	pallet6 - pallet
	pallet1 - pallet
	pallet0 - pallet
	pallet3 - pallet
	pallet2 - pallet
	crate9 - crate
	crate8 - crate
	truck1 - truck
	truck0 - truck
	truck3 - truck
	truck2 - truck
	crate5 - crate
	crate4 - crate
	crate7 - crate
	crate6 - crate
	crate1 - crate
	crate0 - crate
	crate3 - crate
	crate2 - crate

	(:private
		driver1 - driver
	)
)
(:init
	(driving driver1 truck1)
	(at pallet0 depot0)
	(clear pallet0)
	(at pallet1 depot1)
	(clear crate4)
	(at pallet2 distributor0)
	(clear crate9)
	(at pallet3 distributor1)
	(clear crate7)
	(at pallet4 distributor0)
	(clear crate2)
	(at pallet5 distributor1)
	(clear crate1)
	(at pallet6 depot0)
	(clear crate3)
	(at pallet7 distributor1)
	(clear crate8)
	(at truck0 depot1)
	(at truck1 distributor1)
	(at truck2 depot1)
	(at truck3 depot0)
	(at crate0 distributor1)
	(on crate0 pallet3)
	(at crate1 distributor1)
	(on crate1 pallet5)
	(at crate2 distributor0)
	(on crate2 pallet4)
	(at crate3 depot0)
	(on crate3 pallet6)
	(at crate4 depot1)
	(on crate4 pallet1)
	(at crate5 distributor1)
	(on crate5 crate0)
	(at crate6 distributor1)
	(on crate6 pallet7)
	(at crate7 distributor1)
	(on crate7 crate5)
	(at crate8 distributor1)
	(on crate8 crate6)
	(at crate9 distributor0)
	(on crate9 pallet2)
)
(:goal
	(and
		(on crate1 pallet7)
		(on crate2 pallet4)
		(on crate3 crate8)
		(on crate4 pallet0)
		(on crate6 pallet1)
		(on crate7 crate3)
		(on crate8 pallet6)
	)
)
)