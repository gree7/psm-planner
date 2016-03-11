(define (problem convoy-1g-1i)
    (:domain convoy)
    (:requirements :adl :strips :typing)

    (:objects
         wwp1 - wp
         wwp3 - wp
         wwp4 - wp
         wwp5 - wp
         wp3 - wp
         wp4 - wp
         c - convoy
         g1 - guard
         i1 - infantry
         e1 - enemy)
    (:init
        (connected-w wwp1 wwp3) (connected-w wwp3 wwp1)
        (connected-w wwp3 wwp4) (connected-w wwp4 wwp3)
        (connected-w wwp4 wwp5) (connected-w wwp5 wwp4)

        (connected-f wwp1 wwp3) (connected-f wwp3 wwp1)
        (connected-f wwp3 wwp4) (connected-f wwp4 wwp3)
        (connected-f wwp4 wwp5) (connected-f wwp5 wwp4)
        (connected-f wwp1 wp4) (connected-f wp4 wwp1)
        (connected-f wp4 wp3) (connected-f wp3 wp4)

        (line-of-sight wp3 wwp3) (line-of-sight wwp3 wp3)
        (line-of-sight wp3 wwp4) (line-of-sight wwp4 wp3)
        (line-of-sight wwp5 wwp4) (line-of-sight wwp4 wwp5)
        (line-of-sight wwp5 wwp3) (line-of-sight wwp3 wwp5)

        (at c wwp1)
        (at g1 wwp1)
        (at i1 wwp1)
        (at e1 wp3)

        (active e1)

        (danger wwp3 e1)
        (danger wwp4 e1))
    (:goal
        (and (at c wwp4))))
