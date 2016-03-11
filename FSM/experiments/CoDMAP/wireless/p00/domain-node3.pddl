(define (domain wireless)
	(:requirements :strips :factored-privacy :typing)
(:types
	 node level message - object 
 	 base sensor - node 
 )
(:constants
	base  - base
	node1 node2 node3 node4 node5  - sensor
	Zero Low Normal High  - level
)
(:predicates
	(not-message-at ?m - message ?n - node)
	(not-message-data ?m - message ?s - sensor)
	(not-sending ?from - sensor ?to - node ?m - message)
	(neighbor ?n1 - node ?n2 - node)
	(has-data ?n - node ?s - sensor)
	(higher ?l1 - level ?l2 - level)
	(next ?l1 - level ?l2 - level)
	(is-message-at ?m - message ?n - node)
	(message-data ?m - message ?s - sensor)
	(sending ?from - sensor ?to - node ?m - message)

	(:private
		(energy ?s - sensor ?lv - level)
	)
)

(:action generate-data
	:parameters (?s - sensor ?e0 - level ?e1 - level)
	:precondition (and
		(energy ?s ?e0)
		(higher ?e0 Zero)
		(next ?e0 ?e1)
	)
	:effect (and
		(not (energy ?s ?e0))
		(energy ?s ?e1)
		(has-data ?s ?s)
	)
)


(:action add-to-message
	:parameters (?s - sensor ?s2 - sensor ?m - message)
	:precondition (and
		(has-data ?s ?s2)
		(is-message-at ?m ?s)
		(not-message-data ?m ?s2)
	)
	:effect (and
		(not (has-data ?s ?s2))
		(not (not-message-data ?m ?s2))
		(message-data ?m ?s2)
	)
)


(:action send-message
	:parameters (?s - sensor ?receiver - node ?m - message ?e0 - level ?e1 - level)
	:precondition (and
		(energy ?s ?e0)
		(higher ?e0 Zero)
		(next ?e0 ?e1)
		(neighbor ?s ?receiver)
		(is-message-at ?m ?s)
		(not-message-at ?m ?receiver)
		(not-sending ?s ?receiver ?m)
	)
	:effect (and
		(not (energy ?s ?e0))
		(not-message-at ?m ?s)
		(not (is-message-at ?m ?s))
		(energy ?s ?e1)
		(not (not-sending ?s ?receiver ?m))
		(sending ?s ?receiver ?m)
	)
)


(:action receive-message
	:parameters (?s - sensor ?sender - sensor ?m - message)
	:precondition (and
		(not-message-at ?m ?s)
		(sending ?sender ?s ?m)
	)
	:effect (and
		(not-sending ?sender ?s ?m)
		(not (sending ?sender ?s ?m))
		(not (not-message-at ?m ?s))
		(is-message-at ?m ?s)
	)
)


(:action get-data-from-message
	:parameters (?s - sensor ?s2 - sensor ?m - message)
	:precondition (and
		(is-message-at ?m ?s)
		(message-data ?m ?s2)
	)
	:effect (and
		(not-message-data ?m ?s2)
		(not (message-data ?m ?s2))
		(has-data ?s ?s2)
	)
)

)