(define (problem logistics-lin-a6)
    (:domain logistics)
    (:requirements :strips :typing)
    (:objects
        package1 - PACKAGE

        a - CITY
        a-truck - TRUCK

        b - CITY
        b-truck - TRUCK

        c - CITY
        c-truck - TRUCK

        d - CITY
        d-truck - TRUCK

        e - CITY
        e-truck - TRUCK

        f - CITY
        f-truck - TRUCK

        a-po - LOCATION
        ab-po - LOCATION
	bc-po - LOCATION
	cd-po - LOCATION
        de-po - LOCATION
        ef-po - LOCATION
        f-po - LOCATION
    )
    (:init
        (in-city a-po a)
        (in-city ab-po a)
        (in-city ab-po b)
        (in-city bc-po b)
        (in-city bc-po c)
        (in-city cd-po c)
        (in-city cd-po d)
        (in-city de-po d)
        (in-city de-po e)
        (in-city ef-po e)
        (in-city ef-po f)
        (in-city f-po f)

        (att package1 a-po)

        (at a-truck a-po)
        (in-city a-truck a)
        (at b-truck ab-po)
        (in-city b-truck b)
        (at c-truck bc-po)
        (in-city c-truck c)
        (at d-truck cd-po)
        (in-city d-truck d)
        (at e-truck de-po)
        (in-city e-truck e)
        (at f-truck ef-po)
        (in-city f-truck f)
    )
    (:goal (and
        (att package1 f-po)
    ))
)
