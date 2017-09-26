(define (problem beerproblem-01)
    (:domain beerproblem)
    (:requirements :strips :typing)
    (:objects
        plane - vehicle
        truck - vehicle
	truck2 - vehicle
        pilsen - city
        prague - city
        ostrava - city
	beroun - city
    )
    (:init
        (at plane prague)
        (at truck pilsen)
        (at truck2 pilsen)
        (beer_in pilsen)
        (path plane prague ostrava) 
        (path plane ostrava prague) 
        (path truck prague pilsen) 
        (path truck pilsen prague)  
        (path truck2 prague pilsen) 
        (path truck2 pilsen prague)  
    )
    (:goal (and
        (beer_in ostrava)
    ))
)

