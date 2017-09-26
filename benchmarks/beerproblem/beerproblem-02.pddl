(define (problem beerproblem-01)
    (:domain beerproblem)
    (:requirements :strips :typing)
    (:objects
        plane1 - vehicle
        plane2 - vehicle
        truck - vehicle
        pilsen - city
        prague - city
        ostrava - city
    )
    (:init
        (at plane1 prague)
        (at plane2 prague)
        (at truck pilsen)
        (beer_in pilsen)
        (path plane1 prague ostrava) 
        (path plane1 ostrava prague) 
        (path plane2 prague ostrava) 
        (path plane2 ostrava prague) 
        (path truck prague pilsen) 
        (path truck pilsen prague)  
    )
    (:goal (and
        (beer_in ostrava)
    ))
)

