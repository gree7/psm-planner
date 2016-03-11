(define (problem logistics-a3)
    (:domain logistics)
    (:requirements :strips :typing)
    (:objects
        package1 - PACKAGE

        airplane1 - AIRPLANE

        pgh - CITY
        ny - CITY

        pgh-truck - TRUCK
        ny-truck - TRUCK

        pgh-po - LOCATION
        ny-po - LOCATION

        pgh-airport - AIRPORT
        ny-airport - AIRPORT
    )
    (:init
        (in-city pgh-po pgh)
        (in-city pgh-airport pgh)

        (in-city ny-po ny)
        (in-city ny-airport ny)

        (att package1 pgh-po)

        (at airplane1 pgh-airport)

        (at pgh-truck pgh-po)
        (in-city pgh-truck pgh)
        (at ny-truck ny-po)
        (in-city ny-truck ny)
    )
    (:goal (and
        (att package1 ny-po)
    ))
)
