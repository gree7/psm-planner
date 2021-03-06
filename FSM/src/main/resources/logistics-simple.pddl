;; logistics domain
;;
;; logistics-typed-length: strips + simple types
;;    based on logistics-strips-length.
;; Tue Dec  1 16:10:25 EST 1998 Henry Kautz

(define (domain logistics-simple)
  (:requirements :strips :typing)
  (:types
      package location vehicle city - object
      truck airplane - vehicle
      airport - location)

  (:predicates
        (at ?veh - vehicle ?location - location)
        (att ?package - package ?location - location)
        (in ?package - package ?vehicle - vehicle)
        (in-city ?loc-or-truck - (either location truck) ?citys - city))

  (:action load-truck
    :parameters
         (?obj - package
          ?truck - truck
          ?loc - location)
    :precondition
        (and 	(at ?truck ?loc)
            (att ?obj ?loc))
    :effect
        (and 	(not (att ?obj ?loc))
            (in ?obj ?truck)))

  (:action load-airplane
    :parameters
        (?obj - package
         ?airplane - airplane
         ?loc - airport)
    :precondition
        (and
            (att ?obj ?loc)
            (at ?airplane ?loc))
    :effect
           (and 	(not (att ?obj ?loc))
            (in ?obj ?airplane)))

  (:action unload-truck
    :parameters
        (?obj - package
         ?truck - truck
         ?loc - location)
    :precondition
        (and    (at ?truck ?loc)
            (in ?obj ?truck))
    :effect
        (and	(not (in ?obj ?truck))
            (att ?obj ?loc)))

  (:action unload-airplane
    :parameters
        (?obj - package
         ?airplane - airplane
         ?loc - airport)
    :precondition
        (and	(in ?obj ?airplane)
            (at ?airplane ?loc))
    :effect
        (and
            (not (in ?obj ?airplane))
            (att ?obj ?loc)))

  (:action drive-truck
    :parameters
        (?truck - truck
         ?loc-from - location
         ?loc-to - location
         ?city - city)
    :precondition
        (and 	(at ?truck ?loc-from)
            (in-city ?truck ?city)
            (in-city ?loc-from ?city)
            (in-city ?loc-to ?city))
    :effect
        (and 	(not (at ?truck ?loc-from))
            (at ?truck ?loc-to)))

  (:action fly-airplane
    :parameters
        (?airplane - airplane
         ?loc-from - airport
         ?loc-to - airport)
    :precondition
        (at ?airplane ?loc-from)
    :effect
        (and 	(not (at ?airplane ?loc-from))
        (at ?airplane ?loc-to)))
)
