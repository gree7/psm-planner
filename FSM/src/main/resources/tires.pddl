(define (domain tires)
  (:requirements :strips :typing)
  (:types
	tire agents - object
	hander mounter - agents)

  (:predicates
	(handed ?t - tire)
	(not-handed ?t - tire)
	(unmounted ?t - tire)
	(mounted ?t - tire)
	(can-use ?a - mounter ?t - tire)
	(used ?a - mounter ?t - tire) 
        (isAgent ?a - hander))


  (:action hand-tire
   :parameters
 	(?t - tire
	 ?hander - hander)
   :precondition
	(and (not-handed ?t))
   :effect
	(and (handed ?t) (not (not-handed ?t))))


  (:action mount-tire
   :parameters
 	(?t - tire
	 ?mounter - mounter)
   :precondition
   	(and (handed ?t) (can-use ?mounter ?t))
   :effect
        (and (used ?mounter ?t)))


  (:action fixed
   :parameters
 	(?t - tire
	 ?mounter - mounter)
   :precondition
 	(and (unmounted ?t) (used ?mounter ?t))
   :effect
 	(and (mounted ?t) (not (unmounted ?t))))
)
