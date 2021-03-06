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
	n - block

	(:private
		a2 - agent
	)
)
(:init
	(handempty a2)
	(clear g)
	(clear c)
	(clear i)
	(clear h)
	(clear n)
	(ontable j)
	(ontable e)
	(ontable m)
	(ontable b)
	(ontable n)
	(on g j)
	(on c e)
	(on i d)
	(on d l)
	(on l m)
	(on h f)
	(on f a)
	(on a k)
	(on k b)
)
(:goal
	(and
		(on j d)
		(on d b)
		(on b h)
		(on h m)
		(on m k)
		(on k f)
		(on f g)
		(on g a)
		(on a i)
		(on i e)
		(on e l)
		(on l n)
		(on n c)
	)
)
)