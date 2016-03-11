(define (problem logistics-7-0)
(:domain logistics)
(:objects
 apn1 - airplane
 apt3 apt2 apt1 - airport
 pos3 pos2 pos1 - location
 cit3 cit2 cit1 - city
 tru3 tru2 tru1 - truck
 obj33 obj32 obj31 obj23 obj22 obj21 obj13 obj12 obj11 - package
)
(:shared-data
    ((in ?pkg - package) - (either place agent)) - 
(either apn1 tru3 tru2)
)
(:init (myAgent tru1)
 (= (at tru1) pos1)
 (= (in obj33) pos3)
 (= (in obj32) pos3)
 (= (in obj31) pos3)
 (= (in obj23) pos2)
 (= (in obj22) pos2)
 (= (in obj21) pos2)
 (= (in obj13) pos1)
 (= (in obj12) pos1)
 (= (in obj11) pos1)
 (in-city apt3 cit3)
 (not (in-city apt3 cit2))
 (not (in-city apt3 cit1))
 (not (in-city apt2 cit3))
 (in-city apt2 cit2)
 (not (in-city apt2 cit1))
 (not (in-city apt1 cit3))
 (not (in-city apt1 cit2))
 (in-city apt1 cit1)
 (in-city pos3 cit3)
 (not (in-city pos3 cit2))
 (not (in-city pos3 cit1))
 (not (in-city pos2 cit3))
 (in-city pos2 cit2)
 (not (in-city pos2 cit1))
 (not (in-city pos1 cit3))
 (not (in-city pos1 cit2))
 (in-city pos1 cit1)
)
(:global-goal (and
 (= (in obj22) pos2)
 (= (in obj33) apt1)
 (= (in obj12) pos2)
 (= (in obj13) apt3)
 (= (in obj31) apt2)
 (= (in obj23) apt1)
 (= (in obj32) pos1)
))

)
