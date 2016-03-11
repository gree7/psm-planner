(define (problem tires-a2)
    (:domain tires)
    (:requirements :strips :typing)
    (:objects

	mounter1 - mounter
	hander1 - hander

	tire0 - tire

    )
    (:init
	(not-handed tire0)
	(unmounted tire0)
	(can-use mounter1 tire0)
    )
    (:goal (and
	(mounted tire0)
    ))
)
