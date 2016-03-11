(define (problem ZTRAVEL-5-10) (:domain zeno-travel)
(:objects
	person10 - person
	fl6 - flevel
	person2 - person
	person3 - person
	city8 - city
	city9 - city
	person6 - person
	person7 - person
	person4 - person
	person5 - person
	city2 - city
	city3 - city
	city0 - city
	city1 - city
	city6 - city
	person8 - person
	city4 - city
	city5 - city
	person9 - person
	fl1 - flevel
	fl0 - flevel
	fl3 - flevel
	fl2 - flevel
	fl5 - flevel
	fl4 - flevel
	city7 - city
	person1 - person

	(:private
		plane2 - aircraft
	)
)
(:init
	(at plane2 city2)
	(fuel-level plane2 fl6)
	(at person1 city9)
	(at person2 city1)
	(at person3 city0)
	(at person4 city9)
	(at person5 city6)
	(at person6 city0)
	(at person7 city7)
	(at person8 city6)
	(at person9 city4)
	(at person10 city7)
	(next fl0 fl1)
	(next fl1 fl2)
	(next fl2 fl3)
	(next fl3 fl4)
	(next fl4 fl5)
	(next fl5 fl6)
)
(:goal
	(and
		(at person2 city8)
		(at person3 city2)
		(at person4 city7)
		(at person5 city1)
		(at person6 city6)
		(at person7 city5)
		(at person8 city1)
		(at person9 city5)
		(at person10 city9)
	)
)
)