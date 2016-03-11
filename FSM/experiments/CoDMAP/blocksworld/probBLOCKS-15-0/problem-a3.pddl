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
	k - block
	j - block
	m - block
	l - block
	o - block
	n - block

	(:private
		a3 - agent
	)
)
(:init
	(handempty a3)
	(clear e)
	(clear m)
	(clear b)
	(clear f)
	(clear i)
	(ontable g)
	(ontable n)
	(ontable o)
	(ontable k)
	(ontable h)
	(on e j)
	(on j d)
	(on d l)
	(on l c)
	(on c g)
	(on m n)
	(on b a)
	(on a o)
	(on f k)
	(on i h)
)
(:goal
	(and
		(on g o)
		(on o h)
		(on h k)
		(on k m)
		(on m f)
		(on f e)
		(on e a)
		(on a b)
		(on b l)
		(on l j)
		(on j d)
		(on d n)
		(on n i)
		(on i c)
	)
)
)