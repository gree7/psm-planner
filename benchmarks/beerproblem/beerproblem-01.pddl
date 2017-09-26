(define (problem beerproblem-01)
    (:domain beerproblem)
    (:requirements :strips :typing)
    (:objects
        plane - vehicle
        truck - vehicle
        pilsen - city
        prague - city
        ostrava - city
    )
    (:init
        (at plane prague)
        (at truck pilsen)
        (beer_in pilsen)
        (path plane prague ostrava) 
        (path plane ostrava prague) 
        (path truck prague pilsen) 
        (path truck pilsen prague)  
    )
    (:goal (and
        (beer_in ostrava)
    ))
)

