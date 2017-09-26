(define (problem beerproblem-01)
    (:domain beerproblem)
    (:requirements :strips :typing)
    (:objects
        truck truck2 - vehicle
        pilsen pilsen2 - city
        prague prague2 - city
    )
    (:init
        (at truck pilsen)
        (beer_in pilsen)
        (path truck prague pilsen) 
        (path truck pilsen prague)  

        (at truck2 pilsen2)
        (beer_in pilsen2)
        (path truck2 prague2 pilsen2) 
        (path truck2 pilsen2 prague2)  
    )
    (:goal (and
        (beer_in prague)
        (beer_in prague2)
    ))
)

