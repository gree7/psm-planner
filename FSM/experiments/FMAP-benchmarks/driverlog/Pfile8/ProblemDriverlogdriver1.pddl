(define (problem DLOG-3-3-7)
(:domain driverlog)
(:objects
 driver1 driver2 driver3 - driver
 truck1 truck2 truck3 - truck
 package1 package2 package3 package4 package5 package6 package7 - obj
 s0 s1 s2 p1-0 p2-0 p2-1 - location
)
(:shared-data
    (empty ?v - truck)
    ((at ?d - driver) - (either location truck))
    ((pos ?t - truck) - location)
    ((in ?o - obj) - (either location truck)) - 
(either driver2 driver3)
)
(:init (myAgent driver1)
 (= (at driver1) s2)
 (= (at driver2) s0)
 (= (at driver3) s1)
 (= (pos truck1) s2)
 (empty truck1)
 (= (pos truck2) s2)
 (empty truck2)
 (= (pos truck3) s2)
 (empty truck3)
 (= (in package1) s0)
 (= (in package2) s1)
 (= (in package3) s0)
 (= (in package4) s0)
 (= (in package5) s1)
 (= (in package6) s2)
 (= (in package7) s2)
 (not (link s0 s0))
 (not (path s0 s0))
 (link s0 s1)
 (not (path s0 s1))
 (link s0 s2)
 (not (path s0 s2))
 (not (link s0 p1-0))
 (path s0 p1-0)
 (not (link s0 p2-0))
 (path s0 p2-0)
 (not (link s0 p2-1))
 (not (path s0 p2-1))
 (link s1 s0)
 (not (path s1 s0))
 (not (link s1 s1))
 (not (path s1 s1))
 (link s1 s2)
 (not (path s1 s2))
 (not (link s1 p1-0))
 (path s1 p1-0)
 (not (link s1 p2-0))
 (not (path s1 p2-0))
 (not (link s1 p2-1))
 (path s1 p2-1)
 (link s2 s0)
 (not (path s2 s0))
 (link s2 s1)
 (not (path s2 s1))
 (not (link s2 s2))
 (not (path s2 s2))
 (not (link s2 p1-0))
 (not (path s2 p1-0))
 (not (link s2 p2-0))
 (path s2 p2-0)
 (not (link s2 p2-1))
 (path s2 p2-1)
 (not (link p1-0 s0))
 (path p1-0 s0)
 (not (link p1-0 s1))
 (path p1-0 s1)
 (not (link p1-0 s2))
 (not (path p1-0 s2))
 (not (link p1-0 p1-0))
 (not (path p1-0 p1-0))
 (not (link p1-0 p2-0))
 (not (path p1-0 p2-0))
 (not (link p1-0 p2-1))
 (not (path p1-0 p2-1))
 (not (link p2-0 s0))
 (path p2-0 s0)
 (not (link p2-0 s1))
 (not (path p2-0 s1))
 (not (link p2-0 s2))
 (path p2-0 s2)
 (not (link p2-0 p1-0))
 (not (path p2-0 p1-0))
 (not (link p2-0 p2-0))
 (not (path p2-0 p2-0))
 (not (link p2-0 p2-1))
 (not (path p2-0 p2-1))
 (not (link p2-1 s0))
 (not (path p2-1 s0))
 (not (link p2-1 s1))
 (path p2-1 s1)
 (not (link p2-1 s2))
 (path p2-1 s2)
 (not (link p2-1 p1-0))
 (not (path p2-1 p1-0))
 (not (link p2-1 p2-0))
 (not (path p2-1 p2-0))
 (not (link p2-1 p2-1))
 (not (path p2-1 p2-1))
)
(:global-goal (and
 (= (at driver1) s2)
 (= (at driver2) s0)
 (= (pos truck2) s1)
 (= (pos truck3) s0)
 (= (in package1) s2)
 (= (in package2) s0)
 (= (in package3) s1)
 (= (in package4) s2)
 (= (in package5) s1)
 (= (in package6) s2)
 (= (in package7) s1)
))

)
