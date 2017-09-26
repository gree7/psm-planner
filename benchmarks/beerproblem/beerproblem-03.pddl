(define (problem beerproblem-01)
    (:domain beerproblem)
    (:requirements :strips :typing)
    (:objects
        plane plane2 - vehicle
        truck truck2 - vehicle
        pilsen pilsen2 - city
        prague prague2 - city
        ostrava ostrava2 - city
    )
    (:init
        (at plane prague)
        (at truck pilsen)
        (beer_in pilsen)
        (path plane prague ostrava) 
        (path plane ostrava prague) 
        (path truck prague pilsen) 
        (path truck pilsen prague)  

        (at plane2 prague2)
        (at truck2 pilsen2)
        (beer_in pilsen2)
        (path plane2 prague2 ostrava2) 
        (path plane2 ostrava2 prague2) 
        (path truck2 prague2 pilsen2) 
        (path truck2 pilsen2 prague2)  
    )
    (:goal (and
        (beer_in ostrava)
        (beer_in ostrava2)
    ))
)

