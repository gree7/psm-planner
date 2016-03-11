(define (problem convoy-2g-2i)
    (:domain convoy)
    (:requirements :adl :strips :typing)

    (:objects
         wwp1 - wp
         wwp2 - wp
         wwp3 - wp
         wwp4 - wp
         wwp5 - wp
         wwp6 - wp
         wwp7 - wp
         wwp8 - wp
         wp1 - wp
         wp2 - wp
         wp3 - wp
         c - convoy
         g1 - guard
         g2 - guard
         i1 - infantry
         i2 - infantry
         e1 - enemy
         e2 - enemy)
    (:init
        (connected-w wwp1 wwp2) (connected-w wwp2 wwp1)
        (connected-w wwp2 wwp3) (connected-w wwp3 wwp2)
        (connected-w wwp3 wwp4) (connected-w wwp4 wwp3)
        (connected-w wwp4 wwp5) (connected-w wwp5 wwp4)
        (connected-w wwp4 wwp6) (connected-w wwp6 wwp4)
        (connected-w wwp2 wwp7) (connected-w wwp7 wwp2)
        (connected-w wwp7 wwp8) (connected-w wwp8 wwp7)

	(connected-f wwp1 wwp2) (connected-f wwp2 wwp1)
        (connected-f wwp2 wwp3) (connected-f wwp3 wwp2)
        (connected-f wwp3 wwp4) (connected-f wwp4 wwp3)
        (connected-f wwp4 wwp5) (connected-f wwp5 wwp4)
        (connected-f wwp4 wwp6) (connected-f wwp6 wwp4)
        (connected-f wwp2 wwp7) (connected-f wwp7 wwp2)
        (connected-f wwp7 wwp8) (connected-f wwp8 wwp7)
        (connected-f wwp2 wp1) (connected-f wp1 wwp2)
        (connected-f wwp4 wp1) (connected-f wp1 wwp4)
        (connected-f wp1 wp3) (connected-f wp3 wp1)
        (connected-f wwp5 wp2) (connected-f wp2 wwp5)
        (connected-f wwp6 wp2) (connected-f wp2 wwp6)

        (line-of-sight wwp8 wwp1) (line-of-sight wwp1 wwp8)
        (line-of-sight wwp8 wwp2) (line-of-sight wwp2 wwp8)
        (line-of-sight wwp8 wwp3) (line-of-sight wwp3 wwp8)
        (line-of-sight wp3 wwp3) (line-of-sight wwp3 wp3)
        (line-of-sight wp3 wwp4) (line-of-sight wwp4 wp3)
        (line-of-sight wwp6 wwp4) (line-of-sight wwp4 wwp6)
        (line-of-sight wwp6 wwp5) (line-of-sight wwp5 wwp6)

        (at c wwp1)
        (at g1 wwp1)
        (at g2 wwp1)
        (at i1 wwp1)
        (at i2 wwp5)
        (at e1 wp3)
        (at e2 wwp6)

        (active e1)
        (active e2)

        (danger wwp3 e1)
        (danger wwp4 e1)
        (danger wwp4 e2)
        (danger wwp5 e2))
    (:goal
        (and (at c wwp5))))
