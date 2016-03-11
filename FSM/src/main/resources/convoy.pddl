(define (domain convoy)
  (:requirements :strips :adl :typing)
  (:types
      wp wunit unit - object
      convoy guard - wunit
      infantry enemy wunit - unit
  )

  (:predicates 	(connected-f ?wp1 - wp ?wp2 - wp)
		(connected-w ?wp1 - wp ?wp2 - wp)
		(line-of-sight ?wp1 - wp ?wp2 - wp)
		(at ?u - unit ?wp - wp)
		(active ?e - enemy)
		(guarding ?g - guard)
		(danger ?wp - wp ?e - enemy)
		(guarded ?wp - wp ?g - guard)
  )

  (:action move-c
    :parameters (?c - convoy ?from - wp ?to - wp ?g - guard)
    :precondition (and (at ?c ?from) (connected-w ?from ?to) (guarded ?to ?g))
    :effect (and (at ?c ?to) (not (at ?c ?from)))
   )

  (:action move-g
    :parameters (?g - guard ?from - wp ?to - wp)
    :precondition (and (at ?g ?from) (connected-w ?from ?to) (not (guarding ?g)) (forall (?e - enemy) (not (and (danger ?to ?e) (active ?e)))))
    :effect (and (at ?g ?to) (not (at ?g ?from)))
   )

  (:action move-i
    :parameters (?i - infantry ?from - wp ?to - wp)
    :precondition (and (at ?i ?from) (connected-f ?from ?to) (forall (?e - enemy) (not (and (danger ?to ?e) (active ?e))))) 
    :effect (and (at ?i ?to) (not (at ?i ?from)))
   )

  (:action move-attack
    :parameters (?i - infantry ?from - wp ?to - wp ?e - enemy)
    :precondition (and (at ?i ?from) (at ?e ?to) (connected-f ?from ?to))
    :effect (and (at ?i ?to) (not (at ?i ?from)) (not (active ?e)))
   )

  (:action guard
    :parameters (?g - guard ?from - wp ?to - wp)
    :precondition (and (at ?g ?from) (line-of-sight ?from ?to) (not (guarding ?g)) (forall (?e - enemy) (not (and (danger ?to ?e) (active ?e))))) 
    :effect (and (guarded ?to ?g) (guarding ?g))
   )

  (:action unguard
    :parameters (?g - guard ?wp - wp)
    :precondition (and (guarding ?g) (guarded ?wp ?g)) 
    :effect (and (not (guarded ?wp ?g)) (not (guarding ?g)))
   )
)
  