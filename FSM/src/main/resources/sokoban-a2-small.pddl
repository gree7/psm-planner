
;; .-2-
;; -#$-
;; 1---

(define (problem sokoban-a2-small)
  (:domain sokoban-sequential)
  (:requirements :strips :typing)
  (:objects
    dir-down - direction
    dir-left - direction
    dir-right - direction
    dir-up - direction
    player-01 - player
    player-02 - player
    pos-1-1 - location
    pos-1-2 - location
    pos-1-3 - location
    pos-1-4 - location
    pos-2-1 - location
    pos-2-2 - location
    pos-2-3 - location
    pos-2-4 - location
    pos-3-1 - location
    pos-3-2 - location
    pos-3-3 - location
    pos-3-4 - location
    stone-01 - stone
    stone-02 - stone
  )
  (:init
    (IS-GOAL pos-1-1)
    (IS-NONGOAL pos-1-2)
    (IS-NONGOAL pos-1-3)
    (IS-NONGOAL pos-2-1)
    (IS-NONGOAL pos-2-2)
    (IS-NONGOAL pos-2-3)
    (IS-NONGOAL pos-2-4)
    (IS-NONGOAL pos-3-1)
    (IS-NONGOAL pos-3-2)
    (IS-NONGOAL pos-3-3)
    (IS-NONGOAL pos-3-4)
    (MOVE-DIR pos-1-1 pos-1-2 dir-right)
    (MOVE-DIR pos-1-1 pos-2-1 dir-down)
    (MOVE-DIR pos-1-2 pos-1-1 dir-left)
    (MOVE-DIR pos-1-2 pos-1-3 dir-right)
    (MOVE-DIR pos-1-3 pos-1-2 dir-left)
    (MOVE-DIR pos-1-3 pos-2-3 dir-down)
    (MOVE-DIR pos-1-4 pos-1-3 dir-left)
    (MOVE-DIR pos-1-4 pos-2-4 dir-down)
    (MOVE-DIR pos-2-1 pos-1-1 dir-up)
    (MOVE-DIR pos-2-1 pos-3-1 dir-down)
    (MOVE-DIR pos-2-3 pos-1-3 dir-up)
    (MOVE-DIR pos-2-3 pos-3-3 dir-down)
    (MOVE-DIR pos-2-3 pos-2-4 dir-right)
    (MOVE-DIR pos-2-4 pos-2-3 dir-left)
    (MOVE-DIR pos-2-4 pos-1-4 dir-up)
    (MOVE-DIR pos-2-4 pos-3-4 dir-down)
    (MOVE-DIR pos-3-1 pos-2-1 dir-up)
    (MOVE-DIR pos-3-1 pos-3-2 dir-right)
    (MOVE-DIR pos-3-2 pos-3-1 dir-left)
    (MOVE-DIR pos-3-2 pos-3-3 dir-right)
    (MOVE-DIR pos-3-3 pos-2-3 dir-up)
    (MOVE-DIR pos-3-3 pos-3-2 dir-left)
    (MOVE-DIR pos-3-4 pos-3-3 dir-left)
    (MOVE-DIR pos-3-4 pos-2-4 dir-up)
    (at player-01 pos-3-1)
    (at player-02 pos-1-3)
    (at stone-01 pos-2-3)
    (clear pos-1-1)
    (clear pos-1-2)
    (clear pos-1-4)
    (clear pos-2-1)
    (clear pos-2-4)
    (clear pos-3-2)
    (clear pos-3-3)
    (clear pos-3-4)
  )
  (:goal (and
    (at-goal stone-01)
  ))
)
