(define (problem logistics-22-0)
(:domain logistics)
(:objects
 apn2 apn1 - airplane
 apt8 apt7 apt6 apt5 apt4 apt3 apt2 apt1 - airport
 pos1 pos2 pos3 pos4 pos5 pos6 pos7 pos8 - location
 cit8 cit7 cit6 cit5 cit4 cit3 cit2 cit1 - city
 tru8 tru7 tru6 tru5 tru4 tru3 tru2 tru1 - truck
 obj83 obj82 obj81 obj73 obj72 obj71 obj63 obj62 obj61 obj53 obj52 obj51 obj43 obj42 obj41 obj33 obj32 obj31 obj23 obj22 obj21 obj13 obj12 obj11 - package
)
(:shared-data
    ((in ?pkg - package) - (either place agent)) - 
(either apn2 apn1 tru8 tru6 tru5 tru4 tru3 tru2 tru1)
)
(:init (myAgent tru7)
 (= (at tru7) pos7)
 (= (in obj83) pos8)
 (= (in obj82) pos8)
 (= (in obj81) pos8)
 (= (in obj73) pos7)
 (= (in obj72) pos7)
 (= (in obj71) pos7)
 (= (in obj63) pos6)
 (= (in obj62) pos6)
 (= (in obj61) pos6)
 (= (in obj53) pos5)
 (= (in obj52) pos5)
 (= (in obj51) pos5)
 (= (in obj43) pos4)
 (= (in obj42) pos4)
 (= (in obj41) pos4)
 (= (in obj33) pos3)
 (= (in obj32) pos3)
 (= (in obj31) pos3)
 (= (in obj23) pos2)
 (= (in obj22) pos2)
 (= (in obj21) pos2)
 (= (in obj13) pos1)
 (= (in obj12) pos1)
 (= (in obj11) pos1)
 (in-city apt8 cit8)
 (not (in-city apt8 cit7))
 (not (in-city apt8 cit6))
 (not (in-city apt8 cit5))
 (not (in-city apt8 cit4))
 (not (in-city apt8 cit3))
 (not (in-city apt8 cit2))
 (not (in-city apt8 cit1))
 (not (in-city apt7 cit8))
 (in-city apt7 cit7)
 (not (in-city apt7 cit6))
 (not (in-city apt7 cit5))
 (not (in-city apt7 cit4))
 (not (in-city apt7 cit3))
 (not (in-city apt7 cit2))
 (not (in-city apt7 cit1))
 (not (in-city apt6 cit8))
 (not (in-city apt6 cit7))
 (in-city apt6 cit6)
 (not (in-city apt6 cit5))
 (not (in-city apt6 cit4))
 (not (in-city apt6 cit3))
 (not (in-city apt6 cit2))
 (not (in-city apt6 cit1))
 (not (in-city apt5 cit8))
 (not (in-city apt5 cit7))
 (not (in-city apt5 cit6))
 (in-city apt5 cit5)
 (not (in-city apt5 cit4))
 (not (in-city apt5 cit3))
 (not (in-city apt5 cit2))
 (not (in-city apt5 cit1))
 (not (in-city apt4 cit8))
 (not (in-city apt4 cit7))
 (not (in-city apt4 cit6))
 (not (in-city apt4 cit5))
 (in-city apt4 cit4)
 (not (in-city apt4 cit3))
 (not (in-city apt4 cit2))
 (not (in-city apt4 cit1))
 (not (in-city apt3 cit8))
 (not (in-city apt3 cit7))
 (not (in-city apt3 cit6))
 (not (in-city apt3 cit5))
 (not (in-city apt3 cit4))
 (in-city apt3 cit3)
 (not (in-city apt3 cit2))
 (not (in-city apt3 cit1))
 (not (in-city apt2 cit8))
 (not (in-city apt2 cit7))
 (not (in-city apt2 cit6))
 (not (in-city apt2 cit5))
 (not (in-city apt2 cit4))
 (not (in-city apt2 cit3))
 (in-city apt2 cit2)
 (not (in-city apt2 cit1))
 (not (in-city apt1 cit8))
 (not (in-city apt1 cit7))
 (not (in-city apt1 cit6))
 (not (in-city apt1 cit5))
 (not (in-city apt1 cit4))
 (not (in-city apt1 cit3))
 (not (in-city apt1 cit2))
 (in-city apt1 cit1)
 (not (in-city pos1 cit8))
 (not (in-city pos1 cit7))
 (not (in-city pos1 cit6))
 (not (in-city pos1 cit5))
 (not (in-city pos1 cit4))
 (not (in-city pos1 cit3))
 (not (in-city pos1 cit2))
 (in-city pos1 cit1)
 (not (in-city pos2 cit8))
 (not (in-city pos2 cit7))
 (not (in-city pos2 cit6))
 (not (in-city pos2 cit5))
 (not (in-city pos2 cit4))
 (not (in-city pos2 cit3))
 (in-city pos2 cit2)
 (not (in-city pos2 cit1))
 (not (in-city pos3 cit8))
 (not (in-city pos3 cit7))
 (not (in-city pos3 cit6))
 (not (in-city pos3 cit5))
 (not (in-city pos3 cit4))
 (in-city pos3 cit3)
 (not (in-city pos3 cit2))
 (not (in-city pos3 cit1))
 (not (in-city pos4 cit8))
 (not (in-city pos4 cit7))
 (not (in-city pos4 cit6))
 (not (in-city pos4 cit5))
 (in-city pos4 cit4)
 (not (in-city pos4 cit3))
 (not (in-city pos4 cit2))
 (not (in-city pos4 cit1))
 (not (in-city pos5 cit8))
 (not (in-city pos5 cit7))
 (not (in-city pos5 cit6))
 (in-city pos5 cit5)
 (not (in-city pos5 cit4))
 (not (in-city pos5 cit3))
 (not (in-city pos5 cit2))
 (not (in-city pos5 cit1))
 (not (in-city pos6 cit8))
 (not (in-city pos6 cit7))
 (in-city pos6 cit6)
 (not (in-city pos6 cit5))
 (not (in-city pos6 cit4))
 (not (in-city pos6 cit3))
 (not (in-city pos6 cit2))
 (not (in-city pos6 cit1))
 (not (in-city pos7 cit8))
 (in-city pos7 cit7)
 (not (in-city pos7 cit6))
 (not (in-city pos7 cit5))
 (not (in-city pos7 cit4))
 (not (in-city pos7 cit3))
 (not (in-city pos7 cit2))
 (not (in-city pos7 cit1))
 (in-city pos8 cit8)
 (not (in-city pos8 cit7))
 (not (in-city pos8 cit6))
 (not (in-city pos8 cit5))
 (not (in-city pos8 cit4))
 (not (in-city pos8 cit3))
 (not (in-city pos8 cit2))
 (not (in-city pos8 cit1))
)
(:global-goal (and
 (= (in obj12) apt2)
 (= (in obj71) pos3)
 (= (in obj31) pos8)
 (= (in obj11) apt8)
 (= (in obj41) apt6)
 (= (in obj22) apt6)
 (= (in obj43) pos4)
 (= (in obj82) apt3)
 (= (in obj61) pos7)
 (= (in obj21) apt6)
 (= (in obj13) pos2)
 (= (in obj23) pos2)
 (= (in obj33) pos1)
 (= (in obj52) pos3)
 (= (in obj42) pos7)
 (= (in obj32) apt5)
 (= (in obj72) apt7)
 (= (in obj51) apt6)
 (= (in obj73) apt1)
 (= (in obj83) apt6)
 (= (in obj53) pos5)
 (= (in obj62) apt5)
))

)
