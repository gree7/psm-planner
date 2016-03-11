(define (problem logistics-13-1) (:domain logistics)
(:objects
	obj41 - package
	apt3 - airport
	apt2 - airport
	apt1 - airport
	apt5 - airport
	apt4 - airport
	pos4 - location
	pos5 - location
	pos2 - location
	pos3 - location
	pos1 - location
	obj21 - package
	obj22 - package
	obj23 - package
	obj33 - package
	obj32 - package
	obj31 - package
	obj42 - package
	obj43 - package
	obj53 - package
	obj52 - package
	obj11 - package
	obj51 - package
	obj13 - package
	obj12 - package

	(:private
		tru5 - truck
		cit5 - city
	)
)
(:init
	(at obj11 pos1)
	(at obj12 pos1)
	(at obj13 pos1)
	(at obj21 pos2)
	(at obj22 pos2)
	(at obj23 pos2)
	(at obj31 pos3)
	(at obj32 pos3)
	(at obj33 pos3)
	(at obj41 pos4)
	(at obj42 pos4)
	(at obj43 pos4)
	(at tru5 pos5)
	(at obj51 pos5)
	(at obj52 pos5)
	(at obj53 pos5)
	(in-city tru5 pos5 cit5)
	(in-city tru5 apt5 cit5)
)
(:goal
	(and
		(at obj12 pos2)
		(at obj53 apt2)
		(at obj51 pos4)
		(at obj11 pos1)
		(at obj23 apt1)
		(at obj13 pos5)
		(at obj52 apt4)
		(at obj42 pos3)
		(at obj22 pos1)
		(at obj33 apt4)
		(at obj21 apt2)
		(at obj41 pos4)
		(at obj31 pos3)
	)
)
)