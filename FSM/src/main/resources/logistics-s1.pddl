(define (problem logistics-s1)
    (:domain logistics-simple)
    (:requirements :strips :typing)
    (:objects
        beer - PACKAGE

        plane1 - AIRPLANE
	moravia - CITY

        prague - AIRPORT
        brno - AIRPORT

        truck1 - TRUCK

        ostrava - LOCATION
    )
    (:init
        (in-city ostrava moravia)
        (in-city brno moravia)

	(in-city truck1 moravia)

        (att beer prague)

        (at plane1 prague)

        (at truck1 brno)
    )
    (:goal (and
        (att beer ostrava)
    ))
)
