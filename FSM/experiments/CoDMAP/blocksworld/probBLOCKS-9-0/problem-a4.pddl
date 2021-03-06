(define (problem BLOCKS-4-0) (:domain blocks)
(:objects
	a - block
	c - block
	b - block
	e - block
	d - block
	g - block
	f - block
	i - block
	h - block

	(:private
		a4 - agent
	)
)
(:init
	(handempty a4)
	(clear c)
	(clear f)
	(ontable c)
	(ontable b)
	(on f g)
	(on g e)
	(on e a)
	(on a i)
	(on i d)
	(on d h)
	(on h b)
)
(:goal
	(and
		(on g d)
		(on d b)
		(on b c)
		(on c a)
		(on a i)
		(on i f)
		(on f e)
		(on e h)
	)
)
)