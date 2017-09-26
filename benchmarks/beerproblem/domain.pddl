(define (domain beerproblem)
    (:requirements :strips :typing)
    
    (:types
        vehicle city - object
    )

    (:predicates
        (at 
            ?vehicle  - vehicle
            ?city - city
        )
        (beer_in 
            ?in  - object
        )
        (path
            ?vehicle  - vehicle
            ?src - city
            ?dst - city
        )
    )

    (:action move
        :parameters 
        (
            ?veh - vehicle
            ?src - city
            ?dst - city
        )
        :precondition 
        (and 
            (at ?veh ?src)
            (path ?veh ?src ?dst)
        )
        :effect 
        (and 
            (at ?veh ?dst)
            (not (at ?veh ?src))
        )
    )

    (:action load
        :parameters 
        (
            ?vehicle - vehicle
            ?city - city
        )
        :precondition 
        (and
            (beer_in ?city)
            (at ?vehicle ?city)
        )
        :effect 
        (and
            (beer_in ?vehicle)
            (not (beer_in ?city))
        )
    )

    (:action unload
        :parameters 
        (
            ?vehicle - vehicle
            ?city - city
        )
        :precondition 
        (and
            (beer_in ?vehicle)
            (at ?vehicle ?city)
        )
        :effect 
        (and
            (beer_in ?city)
            (not (beer_in ?vehicle))
        )
    )

)

