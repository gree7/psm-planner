(define (problem DLOG-3-3-6)
(:domain driverlog)
(:objects
 driver1 driver2 driver3 - driver
 truck1 truck2 truck3 - truck
 package1 package2 package3 package4 package5 package6 - obj
 s0 s1 s2 s3 s4 s5 s6 s7 s8 s9 p0-1 p1-2 p2-3 p2-5 p3-2 p3-7 p4-6 p4-7 p6-2 p6-5 p6-8 p7-9 p8-3 p8-5 p9-4 - location
)
(:shared-data
    (empty ?v - truck)
    ((at ?d - driver) - (either location truck))
    ((pos ?t - truck) - location)
    ((in ?o - obj) - (either location truck)) - 
(either driver1 driver2)
)
(:init (myAgent driver3)
 (= (at driver1) s9)
 (= (at driver2) s2)
 (= (at driver3) s2)
 (= (pos truck1) s2)
 (empty truck1)
 (= (pos truck2) s1)
 (empty truck2)
 (= (pos truck3) s8)
 (empty truck3)
 (= (in package1) s5)
 (= (in package2) s5)
 (= (in package3) s9)
 (= (in package4) s3)
 (= (in package5) s1)
 (= (in package6) s4)
 (not (link s0 s0))
 (not (path s0 s0))
 (link s0 s1)
 (not (path s0 s1))
 (link s0 s2)
 (not (path s0 s2))
 (link s0 s3)
 (not (path s0 s3))
 (not (link s0 s4))
 (not (path s0 s4))
 (link s0 s5)
 (not (path s0 s5))
 (not (link s0 s6))
 (not (path s0 s6))
 (link s0 s7)
 (not (path s0 s7))
 (link s0 s8)
 (not (path s0 s8))
 (not (link s0 s9))
 (not (path s0 s9))
 (not (link s0 p0-1))
 (path s0 p0-1)
 (not (link s0 p1-2))
 (not (path s0 p1-2))
 (not (link s0 p2-3))
 (not (path s0 p2-3))
 (not (link s0 p2-5))
 (not (path s0 p2-5))
 (not (link s0 p3-2))
 (not (path s0 p3-2))
 (not (link s0 p3-7))
 (not (path s0 p3-7))
 (not (link s0 p4-6))
 (not (path s0 p4-6))
 (not (link s0 p4-7))
 (not (path s0 p4-7))
 (not (link s0 p6-2))
 (not (path s0 p6-2))
 (not (link s0 p6-5))
 (not (path s0 p6-5))
 (not (link s0 p6-8))
 (not (path s0 p6-8))
 (not (link s0 p7-9))
 (not (path s0 p7-9))
 (not (link s0 p8-3))
 (not (path s0 p8-3))
 (not (link s0 p8-5))
 (not (path s0 p8-5))
 (not (link s0 p9-4))
 (not (path s0 p9-4))
 (link s1 s0)
 (not (path s1 s0))
 (not (link s1 s1))
 (not (path s1 s1))
 (not (link s1 s2))
 (not (path s1 s2))
 (link s1 s3)
 (not (path s1 s3))
 (link s1 s4)
 (not (path s1 s4))
 (link s1 s5)
 (not (path s1 s5))
 (link s1 s6)
 (not (path s1 s6))
 (link s1 s7)
 (not (path s1 s7))
 (link s1 s8)
 (not (path s1 s8))
 (not (link s1 s9))
 (not (path s1 s9))
 (not (link s1 p0-1))
 (path s1 p0-1)
 (not (link s1 p1-2))
 (path s1 p1-2)
 (not (link s1 p2-3))
 (not (path s1 p2-3))
 (not (link s1 p2-5))
 (not (path s1 p2-5))
 (not (link s1 p3-2))
 (not (path s1 p3-2))
 (not (link s1 p3-7))
 (not (path s1 p3-7))
 (not (link s1 p4-6))
 (not (path s1 p4-6))
 (not (link s1 p4-7))
 (not (path s1 p4-7))
 (not (link s1 p6-2))
 (not (path s1 p6-2))
 (not (link s1 p6-5))
 (not (path s1 p6-5))
 (not (link s1 p6-8))
 (not (path s1 p6-8))
 (not (link s1 p7-9))
 (not (path s1 p7-9))
 (not (link s1 p8-3))
 (not (path s1 p8-3))
 (not (link s1 p8-5))
 (not (path s1 p8-5))
 (not (link s1 p9-4))
 (not (path s1 p9-4))
 (link s2 s0)
 (not (path s2 s0))
 (not (link s2 s1))
 (not (path s2 s1))
 (not (link s2 s2))
 (not (path s2 s2))
 (link s2 s3)
 (not (path s2 s3))
 (not (link s2 s4))
 (not (path s2 s4))
 (not (link s2 s5))
 (not (path s2 s5))
 (not (link s2 s6))
 (not (path s2 s6))
 (not (link s2 s7))
 (not (path s2 s7))
 (not (link s2 s8))
 (not (path s2 s8))
 (not (link s2 s9))
 (not (path s2 s9))
 (not (link s2 p0-1))
 (not (path s2 p0-1))
 (not (link s2 p1-2))
 (path s2 p1-2)
 (not (link s2 p2-3))
 (path s2 p2-3)
 (not (link s2 p2-5))
 (path s2 p2-5)
 (not (link s2 p3-2))
 (not (path s2 p3-2))
 (not (link s2 p3-7))
 (not (path s2 p3-7))
 (not (link s2 p4-6))
 (not (path s2 p4-6))
 (not (link s2 p4-7))
 (not (path s2 p4-7))
 (not (link s2 p6-2))
 (path s2 p6-2)
 (not (link s2 p6-5))
 (not (path s2 p6-5))
 (not (link s2 p6-8))
 (not (path s2 p6-8))
 (not (link s2 p7-9))
 (not (path s2 p7-9))
 (not (link s2 p8-3))
 (not (path s2 p8-3))
 (not (link s2 p8-5))
 (not (path s2 p8-5))
 (not (link s2 p9-4))
 (not (path s2 p9-4))
 (link s3 s0)
 (not (path s3 s0))
 (link s3 s1)
 (not (path s3 s1))
 (link s3 s2)
 (not (path s3 s2))
 (not (link s3 s3))
 (not (path s3 s3))
 (not (link s3 s4))
 (not (path s3 s4))
 (not (link s3 s5))
 (not (path s3 s5))
 (link s3 s6)
 (not (path s3 s6))
 (not (link s3 s7))
 (not (path s3 s7))
 (not (link s3 s8))
 (not (path s3 s8))
 (link s3 s9)
 (not (path s3 s9))
 (not (link s3 p0-1))
 (not (path s3 p0-1))
 (not (link s3 p1-2))
 (not (path s3 p1-2))
 (not (link s3 p2-3))
 (path s3 p2-3)
 (not (link s3 p2-5))
 (not (path s3 p2-5))
 (not (link s3 p3-2))
 (not (path s3 p3-2))
 (not (link s3 p3-7))
 (path s3 p3-7)
 (not (link s3 p4-6))
 (not (path s3 p4-6))
 (not (link s3 p4-7))
 (not (path s3 p4-7))
 (not (link s3 p6-2))
 (not (path s3 p6-2))
 (not (link s3 p6-5))
 (not (path s3 p6-5))
 (not (link s3 p6-8))
 (not (path s3 p6-8))
 (not (link s3 p7-9))
 (not (path s3 p7-9))
 (not (link s3 p8-3))
 (path s3 p8-3)
 (not (link s3 p8-5))
 (not (path s3 p8-5))
 (not (link s3 p9-4))
 (not (path s3 p9-4))
 (not (link s4 s0))
 (not (path s4 s0))
 (link s4 s1)
 (not (path s4 s1))
 (not (link s4 s2))
 (not (path s4 s2))
 (not (link s4 s3))
 (not (path s4 s3))
 (not (link s4 s4))
 (not (path s4 s4))
 (link s4 s5)
 (not (path s4 s5))
 (link s4 s6)
 (not (path s4 s6))
 (not (link s4 s7))
 (not (path s4 s7))
 (link s4 s8)
 (not (path s4 s8))
 (link s4 s9)
 (not (path s4 s9))
 (not (link s4 p0-1))
 (not (path s4 p0-1))
 (not (link s4 p1-2))
 (not (path s4 p1-2))
 (not (link s4 p2-3))
 (not (path s4 p2-3))
 (not (link s4 p2-5))
 (not (path s4 p2-5))
 (not (link s4 p3-2))
 (not (path s4 p3-2))
 (not (link s4 p3-7))
 (not (path s4 p3-7))
 (not (link s4 p4-6))
 (path s4 p4-6)
 (not (link s4 p4-7))
 (path s4 p4-7)
 (not (link s4 p6-2))
 (not (path s4 p6-2))
 (not (link s4 p6-5))
 (not (path s4 p6-5))
 (not (link s4 p6-8))
 (not (path s4 p6-8))
 (not (link s4 p7-9))
 (not (path s4 p7-9))
 (not (link s4 p8-3))
 (not (path s4 p8-3))
 (not (link s4 p8-5))
 (not (path s4 p8-5))
 (not (link s4 p9-4))
 (path s4 p9-4)
 (link s5 s0)
 (not (path s5 s0))
 (link s5 s1)
 (not (path s5 s1))
 (not (link s5 s2))
 (not (path s5 s2))
 (not (link s5 s3))
 (not (path s5 s3))
 (link s5 s4)
 (not (path s5 s4))
 (not (link s5 s5))
 (not (path s5 s5))
 (link s5 s6)
 (not (path s5 s6))
 (not (link s5 s7))
 (not (path s5 s7))
 (not (link s5 s8))
 (not (path s5 s8))
 (not (link s5 s9))
 (not (path s5 s9))
 (not (link s5 p0-1))
 (not (path s5 p0-1))
 (not (link s5 p1-2))
 (not (path s5 p1-2))
 (not (link s5 p2-3))
 (not (path s5 p2-3))
 (not (link s5 p2-5))
 (path s5 p2-5)
 (not (link s5 p3-2))
 (not (path s5 p3-2))
 (not (link s5 p3-7))
 (not (path s5 p3-7))
 (not (link s5 p4-6))
 (not (path s5 p4-6))
 (not (link s5 p4-7))
 (not (path s5 p4-7))
 (not (link s5 p6-2))
 (not (path s5 p6-2))
 (not (link s5 p6-5))
 (path s5 p6-5)
 (not (link s5 p6-8))
 (not (path s5 p6-8))
 (not (link s5 p7-9))
 (not (path s5 p7-9))
 (not (link s5 p8-3))
 (not (path s5 p8-3))
 (not (link s5 p8-5))
 (path s5 p8-5)
 (not (link s5 p9-4))
 (not (path s5 p9-4))
 (not (link s6 s0))
 (not (path s6 s0))
 (link s6 s1)
 (not (path s6 s1))
 (not (link s6 s2))
 (not (path s6 s2))
 (link s6 s3)
 (not (path s6 s3))
 (link s6 s4)
 (not (path s6 s4))
 (link s6 s5)
 (not (path s6 s5))
 (not (link s6 s6))
 (not (path s6 s6))
 (link s6 s7)
 (not (path s6 s7))
 (link s6 s8)
 (not (path s6 s8))
 (not (link s6 s9))
 (not (path s6 s9))
 (not (link s6 p0-1))
 (not (path s6 p0-1))
 (not (link s6 p1-2))
 (not (path s6 p1-2))
 (not (link s6 p2-3))
 (not (path s6 p2-3))
 (not (link s6 p2-5))
 (not (path s6 p2-5))
 (not (link s6 p3-2))
 (not (path s6 p3-2))
 (not (link s6 p3-7))
 (not (path s6 p3-7))
 (not (link s6 p4-6))
 (path s6 p4-6)
 (not (link s6 p4-7))
 (not (path s6 p4-7))
 (not (link s6 p6-2))
 (path s6 p6-2)
 (not (link s6 p6-5))
 (path s6 p6-5)
 (not (link s6 p6-8))
 (path s6 p6-8)
 (not (link s6 p7-9))
 (not (path s6 p7-9))
 (not (link s6 p8-3))
 (not (path s6 p8-3))
 (not (link s6 p8-5))
 (not (path s6 p8-5))
 (not (link s6 p9-4))
 (not (path s6 p9-4))
 (link s7 s0)
 (not (path s7 s0))
 (link s7 s1)
 (not (path s7 s1))
 (not (link s7 s2))
 (not (path s7 s2))
 (not (link s7 s3))
 (not (path s7 s3))
 (not (link s7 s4))
 (not (path s7 s4))
 (not (link s7 s5))
 (not (path s7 s5))
 (link s7 s6)
 (not (path s7 s6))
 (not (link s7 s7))
 (not (path s7 s7))
 (link s7 s8)
 (not (path s7 s8))
 (link s7 s9)
 (not (path s7 s9))
 (not (link s7 p0-1))
 (not (path s7 p0-1))
 (not (link s7 p1-2))
 (not (path s7 p1-2))
 (not (link s7 p2-3))
 (not (path s7 p2-3))
 (not (link s7 p2-5))
 (not (path s7 p2-5))
 (not (link s7 p3-2))
 (not (path s7 p3-2))
 (not (link s7 p3-7))
 (path s7 p3-7)
 (not (link s7 p4-6))
 (not (path s7 p4-6))
 (not (link s7 p4-7))
 (path s7 p4-7)
 (not (link s7 p6-2))
 (not (path s7 p6-2))
 (not (link s7 p6-5))
 (not (path s7 p6-5))
 (not (link s7 p6-8))
 (not (path s7 p6-8))
 (not (link s7 p7-9))
 (path s7 p7-9)
 (not (link s7 p8-3))
 (not (path s7 p8-3))
 (not (link s7 p8-5))
 (not (path s7 p8-5))
 (not (link s7 p9-4))
 (not (path s7 p9-4))
 (link s8 s0)
 (not (path s8 s0))
 (link s8 s1)
 (not (path s8 s1))
 (not (link s8 s2))
 (not (path s8 s2))
 (not (link s8 s3))
 (not (path s8 s3))
 (link s8 s4)
 (not (path s8 s4))
 (not (link s8 s5))
 (not (path s8 s5))
 (link s8 s6)
 (not (path s8 s6))
 (link s8 s7)
 (not (path s8 s7))
 (not (link s8 s8))
 (not (path s8 s8))
 (link s8 s9)
 (not (path s8 s9))
 (not (link s8 p0-1))
 (not (path s8 p0-1))
 (not (link s8 p1-2))
 (not (path s8 p1-2))
 (not (link s8 p2-3))
 (not (path s8 p2-3))
 (not (link s8 p2-5))
 (not (path s8 p2-5))
 (not (link s8 p3-2))
 (not (path s8 p3-2))
 (not (link s8 p3-7))
 (not (path s8 p3-7))
 (not (link s8 p4-6))
 (not (path s8 p4-6))
 (not (link s8 p4-7))
 (not (path s8 p4-7))
 (not (link s8 p6-2))
 (not (path s8 p6-2))
 (not (link s8 p6-5))
 (not (path s8 p6-5))
 (not (link s8 p6-8))
 (path s8 p6-8)
 (not (link s8 p7-9))
 (not (path s8 p7-9))
 (not (link s8 p8-3))
 (path s8 p8-3)
 (not (link s8 p8-5))
 (path s8 p8-5)
 (not (link s8 p9-4))
 (not (path s8 p9-4))
 (not (link s9 s0))
 (not (path s9 s0))
 (not (link s9 s1))
 (not (path s9 s1))
 (not (link s9 s2))
 (not (path s9 s2))
 (link s9 s3)
 (not (path s9 s3))
 (link s9 s4)
 (not (path s9 s4))
 (not (link s9 s5))
 (not (path s9 s5))
 (not (link s9 s6))
 (not (path s9 s6))
 (link s9 s7)
 (not (path s9 s7))
 (link s9 s8)
 (not (path s9 s8))
 (not (link s9 s9))
 (not (path s9 s9))
 (not (link s9 p0-1))
 (not (path s9 p0-1))
 (not (link s9 p1-2))
 (not (path s9 p1-2))
 (not (link s9 p2-3))
 (not (path s9 p2-3))
 (not (link s9 p2-5))
 (not (path s9 p2-5))
 (not (link s9 p3-2))
 (not (path s9 p3-2))
 (not (link s9 p3-7))
 (not (path s9 p3-7))
 (not (link s9 p4-6))
 (not (path s9 p4-6))
 (not (link s9 p4-7))
 (not (path s9 p4-7))
 (not (link s9 p6-2))
 (not (path s9 p6-2))
 (not (link s9 p6-5))
 (not (path s9 p6-5))
 (not (link s9 p6-8))
 (not (path s9 p6-8))
 (not (link s9 p7-9))
 (path s9 p7-9)
 (not (link s9 p8-3))
 (not (path s9 p8-3))
 (not (link s9 p8-5))
 (not (path s9 p8-5))
 (not (link s9 p9-4))
 (path s9 p9-4)
 (not (link p0-1 s0))
 (path p0-1 s0)
 (not (link p0-1 s1))
 (path p0-1 s1)
 (not (link p0-1 s2))
 (not (path p0-1 s2))
 (not (link p0-1 s3))
 (not (path p0-1 s3))
 (not (link p0-1 s4))
 (not (path p0-1 s4))
 (not (link p0-1 s5))
 (not (path p0-1 s5))
 (not (link p0-1 s6))
 (not (path p0-1 s6))
 (not (link p0-1 s7))
 (not (path p0-1 s7))
 (not (link p0-1 s8))
 (not (path p0-1 s8))
 (not (link p0-1 s9))
 (not (path p0-1 s9))
 (not (link p0-1 p0-1))
 (not (path p0-1 p0-1))
 (not (link p0-1 p1-2))
 (not (path p0-1 p1-2))
 (not (link p0-1 p2-3))
 (not (path p0-1 p2-3))
 (not (link p0-1 p2-5))
 (not (path p0-1 p2-5))
 (not (link p0-1 p3-2))
 (not (path p0-1 p3-2))
 (not (link p0-1 p3-7))
 (not (path p0-1 p3-7))
 (not (link p0-1 p4-6))
 (not (path p0-1 p4-6))
 (not (link p0-1 p4-7))
 (not (path p0-1 p4-7))
 (not (link p0-1 p6-2))
 (not (path p0-1 p6-2))
 (not (link p0-1 p6-5))
 (not (path p0-1 p6-5))
 (not (link p0-1 p6-8))
 (not (path p0-1 p6-8))
 (not (link p0-1 p7-9))
 (not (path p0-1 p7-9))
 (not (link p0-1 p8-3))
 (not (path p0-1 p8-3))
 (not (link p0-1 p8-5))
 (not (path p0-1 p8-5))
 (not (link p0-1 p9-4))
 (not (path p0-1 p9-4))
 (not (link p1-2 s0))
 (not (path p1-2 s0))
 (not (link p1-2 s1))
 (path p1-2 s1)
 (not (link p1-2 s2))
 (path p1-2 s2)
 (not (link p1-2 s3))
 (not (path p1-2 s3))
 (not (link p1-2 s4))
 (not (path p1-2 s4))
 (not (link p1-2 s5))
 (not (path p1-2 s5))
 (not (link p1-2 s6))
 (not (path p1-2 s6))
 (not (link p1-2 s7))
 (not (path p1-2 s7))
 (not (link p1-2 s8))
 (not (path p1-2 s8))
 (not (link p1-2 s9))
 (not (path p1-2 s9))
 (not (link p1-2 p0-1))
 (not (path p1-2 p0-1))
 (not (link p1-2 p1-2))
 (not (path p1-2 p1-2))
 (not (link p1-2 p2-3))
 (not (path p1-2 p2-3))
 (not (link p1-2 p2-5))
 (not (path p1-2 p2-5))
 (not (link p1-2 p3-2))
 (not (path p1-2 p3-2))
 (not (link p1-2 p3-7))
 (not (path p1-2 p3-7))
 (not (link p1-2 p4-6))
 (not (path p1-2 p4-6))
 (not (link p1-2 p4-7))
 (not (path p1-2 p4-7))
 (not (link p1-2 p6-2))
 (not (path p1-2 p6-2))
 (not (link p1-2 p6-5))
 (not (path p1-2 p6-5))
 (not (link p1-2 p6-8))
 (not (path p1-2 p6-8))
 (not (link p1-2 p7-9))
 (not (path p1-2 p7-9))
 (not (link p1-2 p8-3))
 (not (path p1-2 p8-3))
 (not (link p1-2 p8-5))
 (not (path p1-2 p8-5))
 (not (link p1-2 p9-4))
 (not (path p1-2 p9-4))
 (not (link p2-3 s0))
 (not (path p2-3 s0))
 (not (link p2-3 s1))
 (not (path p2-3 s1))
 (not (link p2-3 s2))
 (path p2-3 s2)
 (not (link p2-3 s3))
 (path p2-3 s3)
 (not (link p2-3 s4))
 (not (path p2-3 s4))
 (not (link p2-3 s5))
 (not (path p2-3 s5))
 (not (link p2-3 s6))
 (not (path p2-3 s6))
 (not (link p2-3 s7))
 (not (path p2-3 s7))
 (not (link p2-3 s8))
 (not (path p2-3 s8))
 (not (link p2-3 s9))
 (not (path p2-3 s9))
 (not (link p2-3 p0-1))
 (not (path p2-3 p0-1))
 (not (link p2-3 p1-2))
 (not (path p2-3 p1-2))
 (not (link p2-3 p2-3))
 (not (path p2-3 p2-3))
 (not (link p2-3 p2-5))
 (not (path p2-3 p2-5))
 (not (link p2-3 p3-2))
 (not (path p2-3 p3-2))
 (not (link p2-3 p3-7))
 (not (path p2-3 p3-7))
 (not (link p2-3 p4-6))
 (not (path p2-3 p4-6))
 (not (link p2-3 p4-7))
 (not (path p2-3 p4-7))
 (not (link p2-3 p6-2))
 (not (path p2-3 p6-2))
 (not (link p2-3 p6-5))
 (not (path p2-3 p6-5))
 (not (link p2-3 p6-8))
 (not (path p2-3 p6-8))
 (not (link p2-3 p7-9))
 (not (path p2-3 p7-9))
 (not (link p2-3 p8-3))
 (not (path p2-3 p8-3))
 (not (link p2-3 p8-5))
 (not (path p2-3 p8-5))
 (not (link p2-3 p9-4))
 (not (path p2-3 p9-4))
 (not (link p2-5 s0))
 (not (path p2-5 s0))
 (not (link p2-5 s1))
 (not (path p2-5 s1))
 (not (link p2-5 s2))
 (path p2-5 s2)
 (not (link p2-5 s3))
 (not (path p2-5 s3))
 (not (link p2-5 s4))
 (not (path p2-5 s4))
 (not (link p2-5 s5))
 (path p2-5 s5)
 (not (link p2-5 s6))
 (not (path p2-5 s6))
 (not (link p2-5 s7))
 (not (path p2-5 s7))
 (not (link p2-5 s8))
 (not (path p2-5 s8))
 (not (link p2-5 s9))
 (not (path p2-5 s9))
 (not (link p2-5 p0-1))
 (not (path p2-5 p0-1))
 (not (link p2-5 p1-2))
 (not (path p2-5 p1-2))
 (not (link p2-5 p2-3))
 (not (path p2-5 p2-3))
 (not (link p2-5 p2-5))
 (not (path p2-5 p2-5))
 (not (link p2-5 p3-2))
 (not (path p2-5 p3-2))
 (not (link p2-5 p3-7))
 (not (path p2-5 p3-7))
 (not (link p2-5 p4-6))
 (not (path p2-5 p4-6))
 (not (link p2-5 p4-7))
 (not (path p2-5 p4-7))
 (not (link p2-5 p6-2))
 (not (path p2-5 p6-2))
 (not (link p2-5 p6-5))
 (not (path p2-5 p6-5))
 (not (link p2-5 p6-8))
 (not (path p2-5 p6-8))
 (not (link p2-5 p7-9))
 (not (path p2-5 p7-9))
 (not (link p2-5 p8-3))
 (not (path p2-5 p8-3))
 (not (link p2-5 p8-5))
 (not (path p2-5 p8-5))
 (not (link p2-5 p9-4))
 (not (path p2-5 p9-4))
 (not (link p3-2 s0))
 (not (path p3-2 s0))
 (not (link p3-2 s1))
 (not (path p3-2 s1))
 (not (link p3-2 s2))
 (not (path p3-2 s2))
 (not (link p3-2 s3))
 (not (path p3-2 s3))
 (not (link p3-2 s4))
 (not (path p3-2 s4))
 (not (link p3-2 s5))
 (not (path p3-2 s5))
 (not (link p3-2 s6))
 (not (path p3-2 s6))
 (not (link p3-2 s7))
 (not (path p3-2 s7))
 (not (link p3-2 s8))
 (not (path p3-2 s8))
 (not (link p3-2 s9))
 (not (path p3-2 s9))
 (not (link p3-2 p0-1))
 (not (path p3-2 p0-1))
 (not (link p3-2 p1-2))
 (not (path p3-2 p1-2))
 (not (link p3-2 p2-3))
 (not (path p3-2 p2-3))
 (not (link p3-2 p2-5))
 (not (path p3-2 p2-5))
 (not (link p3-2 p3-2))
 (not (path p3-2 p3-2))
 (not (link p3-2 p3-7))
 (not (path p3-2 p3-7))
 (not (link p3-2 p4-6))
 (not (path p3-2 p4-6))
 (not (link p3-2 p4-7))
 (not (path p3-2 p4-7))
 (not (link p3-2 p6-2))
 (not (path p3-2 p6-2))
 (not (link p3-2 p6-5))
 (not (path p3-2 p6-5))
 (not (link p3-2 p6-8))
 (not (path p3-2 p6-8))
 (not (link p3-2 p7-9))
 (not (path p3-2 p7-9))
 (not (link p3-2 p8-3))
 (not (path p3-2 p8-3))
 (not (link p3-2 p8-5))
 (not (path p3-2 p8-5))
 (not (link p3-2 p9-4))
 (not (path p3-2 p9-4))
 (not (link p3-7 s0))
 (not (path p3-7 s0))
 (not (link p3-7 s1))
 (not (path p3-7 s1))
 (not (link p3-7 s2))
 (not (path p3-7 s2))
 (not (link p3-7 s3))
 (path p3-7 s3)
 (not (link p3-7 s4))
 (not (path p3-7 s4))
 (not (link p3-7 s5))
 (not (path p3-7 s5))
 (not (link p3-7 s6))
 (not (path p3-7 s6))
 (not (link p3-7 s7))
 (path p3-7 s7)
 (not (link p3-7 s8))
 (not (path p3-7 s8))
 (not (link p3-7 s9))
 (not (path p3-7 s9))
 (not (link p3-7 p0-1))
 (not (path p3-7 p0-1))
 (not (link p3-7 p1-2))
 (not (path p3-7 p1-2))
 (not (link p3-7 p2-3))
 (not (path p3-7 p2-3))
 (not (link p3-7 p2-5))
 (not (path p3-7 p2-5))
 (not (link p3-7 p3-2))
 (not (path p3-7 p3-2))
 (not (link p3-7 p3-7))
 (not (path p3-7 p3-7))
 (not (link p3-7 p4-6))
 (not (path p3-7 p4-6))
 (not (link p3-7 p4-7))
 (not (path p3-7 p4-7))
 (not (link p3-7 p6-2))
 (not (path p3-7 p6-2))
 (not (link p3-7 p6-5))
 (not (path p3-7 p6-5))
 (not (link p3-7 p6-8))
 (not (path p3-7 p6-8))
 (not (link p3-7 p7-9))
 (not (path p3-7 p7-9))
 (not (link p3-7 p8-3))
 (not (path p3-7 p8-3))
 (not (link p3-7 p8-5))
 (not (path p3-7 p8-5))
 (not (link p3-7 p9-4))
 (not (path p3-7 p9-4))
 (not (link p4-6 s0))
 (not (path p4-6 s0))
 (not (link p4-6 s1))
 (not (path p4-6 s1))
 (not (link p4-6 s2))
 (not (path p4-6 s2))
 (not (link p4-6 s3))
 (not (path p4-6 s3))
 (not (link p4-6 s4))
 (path p4-6 s4)
 (not (link p4-6 s5))
 (not (path p4-6 s5))
 (not (link p4-6 s6))
 (path p4-6 s6)
 (not (link p4-6 s7))
 (not (path p4-6 s7))
 (not (link p4-6 s8))
 (not (path p4-6 s8))
 (not (link p4-6 s9))
 (not (path p4-6 s9))
 (not (link p4-6 p0-1))
 (not (path p4-6 p0-1))
 (not (link p4-6 p1-2))
 (not (path p4-6 p1-2))
 (not (link p4-6 p2-3))
 (not (path p4-6 p2-3))
 (not (link p4-6 p2-5))
 (not (path p4-6 p2-5))
 (not (link p4-6 p3-2))
 (not (path p4-6 p3-2))
 (not (link p4-6 p3-7))
 (not (path p4-6 p3-7))
 (not (link p4-6 p4-6))
 (not (path p4-6 p4-6))
 (not (link p4-6 p4-7))
 (not (path p4-6 p4-7))
 (not (link p4-6 p6-2))
 (not (path p4-6 p6-2))
 (not (link p4-6 p6-5))
 (not (path p4-6 p6-5))
 (not (link p4-6 p6-8))
 (not (path p4-6 p6-8))
 (not (link p4-6 p7-9))
 (not (path p4-6 p7-9))
 (not (link p4-6 p8-3))
 (not (path p4-6 p8-3))
 (not (link p4-6 p8-5))
 (not (path p4-6 p8-5))
 (not (link p4-6 p9-4))
 (not (path p4-6 p9-4))
 (not (link p4-7 s0))
 (not (path p4-7 s0))
 (not (link p4-7 s1))
 (not (path p4-7 s1))
 (not (link p4-7 s2))
 (not (path p4-7 s2))
 (not (link p4-7 s3))
 (not (path p4-7 s3))
 (not (link p4-7 s4))
 (path p4-7 s4)
 (not (link p4-7 s5))
 (not (path p4-7 s5))
 (not (link p4-7 s6))
 (not (path p4-7 s6))
 (not (link p4-7 s7))
 (path p4-7 s7)
 (not (link p4-7 s8))
 (not (path p4-7 s8))
 (not (link p4-7 s9))
 (not (path p4-7 s9))
 (not (link p4-7 p0-1))
 (not (path p4-7 p0-1))
 (not (link p4-7 p1-2))
 (not (path p4-7 p1-2))
 (not (link p4-7 p2-3))
 (not (path p4-7 p2-3))
 (not (link p4-7 p2-5))
 (not (path p4-7 p2-5))
 (not (link p4-7 p3-2))
 (not (path p4-7 p3-2))
 (not (link p4-7 p3-7))
 (not (path p4-7 p3-7))
 (not (link p4-7 p4-6))
 (not (path p4-7 p4-6))
 (not (link p4-7 p4-7))
 (not (path p4-7 p4-7))
 (not (link p4-7 p6-2))
 (not (path p4-7 p6-2))
 (not (link p4-7 p6-5))
 (not (path p4-7 p6-5))
 (not (link p4-7 p6-8))
 (not (path p4-7 p6-8))
 (not (link p4-7 p7-9))
 (not (path p4-7 p7-9))
 (not (link p4-7 p8-3))
 (not (path p4-7 p8-3))
 (not (link p4-7 p8-5))
 (not (path p4-7 p8-5))
 (not (link p4-7 p9-4))
 (not (path p4-7 p9-4))
 (not (link p6-2 s0))
 (not (path p6-2 s0))
 (not (link p6-2 s1))
 (not (path p6-2 s1))
 (not (link p6-2 s2))
 (path p6-2 s2)
 (not (link p6-2 s3))
 (not (path p6-2 s3))
 (not (link p6-2 s4))
 (not (path p6-2 s4))
 (not (link p6-2 s5))
 (not (path p6-2 s5))
 (not (link p6-2 s6))
 (path p6-2 s6)
 (not (link p6-2 s7))
 (not (path p6-2 s7))
 (not (link p6-2 s8))
 (not (path p6-2 s8))
 (not (link p6-2 s9))
 (not (path p6-2 s9))
 (not (link p6-2 p0-1))
 (not (path p6-2 p0-1))
 (not (link p6-2 p1-2))
 (not (path p6-2 p1-2))
 (not (link p6-2 p2-3))
 (not (path p6-2 p2-3))
 (not (link p6-2 p2-5))
 (not (path p6-2 p2-5))
 (not (link p6-2 p3-2))
 (not (path p6-2 p3-2))
 (not (link p6-2 p3-7))
 (not (path p6-2 p3-7))
 (not (link p6-2 p4-6))
 (not (path p6-2 p4-6))
 (not (link p6-2 p4-7))
 (not (path p6-2 p4-7))
 (not (link p6-2 p6-2))
 (not (path p6-2 p6-2))
 (not (link p6-2 p6-5))
 (not (path p6-2 p6-5))
 (not (link p6-2 p6-8))
 (not (path p6-2 p6-8))
 (not (link p6-2 p7-9))
 (not (path p6-2 p7-9))
 (not (link p6-2 p8-3))
 (not (path p6-2 p8-3))
 (not (link p6-2 p8-5))
 (not (path p6-2 p8-5))
 (not (link p6-2 p9-4))
 (not (path p6-2 p9-4))
 (not (link p6-5 s0))
 (not (path p6-5 s0))
 (not (link p6-5 s1))
 (not (path p6-5 s1))
 (not (link p6-5 s2))
 (not (path p6-5 s2))
 (not (link p6-5 s3))
 (not (path p6-5 s3))
 (not (link p6-5 s4))
 (not (path p6-5 s4))
 (not (link p6-5 s5))
 (path p6-5 s5)
 (not (link p6-5 s6))
 (path p6-5 s6)
 (not (link p6-5 s7))
 (not (path p6-5 s7))
 (not (link p6-5 s8))
 (not (path p6-5 s8))
 (not (link p6-5 s9))
 (not (path p6-5 s9))
 (not (link p6-5 p0-1))
 (not (path p6-5 p0-1))
 (not (link p6-5 p1-2))
 (not (path p6-5 p1-2))
 (not (link p6-5 p2-3))
 (not (path p6-5 p2-3))
 (not (link p6-5 p2-5))
 (not (path p6-5 p2-5))
 (not (link p6-5 p3-2))
 (not (path p6-5 p3-2))
 (not (link p6-5 p3-7))
 (not (path p6-5 p3-7))
 (not (link p6-5 p4-6))
 (not (path p6-5 p4-6))
 (not (link p6-5 p4-7))
 (not (path p6-5 p4-7))
 (not (link p6-5 p6-2))
 (not (path p6-5 p6-2))
 (not (link p6-5 p6-5))
 (not (path p6-5 p6-5))
 (not (link p6-5 p6-8))
 (not (path p6-5 p6-8))
 (not (link p6-5 p7-9))
 (not (path p6-5 p7-9))
 (not (link p6-5 p8-3))
 (not (path p6-5 p8-3))
 (not (link p6-5 p8-5))
 (not (path p6-5 p8-5))
 (not (link p6-5 p9-4))
 (not (path p6-5 p9-4))
 (not (link p6-8 s0))
 (not (path p6-8 s0))
 (not (link p6-8 s1))
 (not (path p6-8 s1))
 (not (link p6-8 s2))
 (not (path p6-8 s2))
 (not (link p6-8 s3))
 (not (path p6-8 s3))
 (not (link p6-8 s4))
 (not (path p6-8 s4))
 (not (link p6-8 s5))
 (not (path p6-8 s5))
 (not (link p6-8 s6))
 (path p6-8 s6)
 (not (link p6-8 s7))
 (not (path p6-8 s7))
 (not (link p6-8 s8))
 (path p6-8 s8)
 (not (link p6-8 s9))
 (not (path p6-8 s9))
 (not (link p6-8 p0-1))
 (not (path p6-8 p0-1))
 (not (link p6-8 p1-2))
 (not (path p6-8 p1-2))
 (not (link p6-8 p2-3))
 (not (path p6-8 p2-3))
 (not (link p6-8 p2-5))
 (not (path p6-8 p2-5))
 (not (link p6-8 p3-2))
 (not (path p6-8 p3-2))
 (not (link p6-8 p3-7))
 (not (path p6-8 p3-7))
 (not (link p6-8 p4-6))
 (not (path p6-8 p4-6))
 (not (link p6-8 p4-7))
 (not (path p6-8 p4-7))
 (not (link p6-8 p6-2))
 (not (path p6-8 p6-2))
 (not (link p6-8 p6-5))
 (not (path p6-8 p6-5))
 (not (link p6-8 p6-8))
 (not (path p6-8 p6-8))
 (not (link p6-8 p7-9))
 (not (path p6-8 p7-9))
 (not (link p6-8 p8-3))
 (not (path p6-8 p8-3))
 (not (link p6-8 p8-5))
 (not (path p6-8 p8-5))
 (not (link p6-8 p9-4))
 (not (path p6-8 p9-4))
 (not (link p7-9 s0))
 (not (path p7-9 s0))
 (not (link p7-9 s1))
 (not (path p7-9 s1))
 (not (link p7-9 s2))
 (not (path p7-9 s2))
 (not (link p7-9 s3))
 (not (path p7-9 s3))
 (not (link p7-9 s4))
 (not (path p7-9 s4))
 (not (link p7-9 s5))
 (not (path p7-9 s5))
 (not (link p7-9 s6))
 (not (path p7-9 s6))
 (not (link p7-9 s7))
 (path p7-9 s7)
 (not (link p7-9 s8))
 (not (path p7-9 s8))
 (not (link p7-9 s9))
 (path p7-9 s9)
 (not (link p7-9 p0-1))
 (not (path p7-9 p0-1))
 (not (link p7-9 p1-2))
 (not (path p7-9 p1-2))
 (not (link p7-9 p2-3))
 (not (path p7-9 p2-3))
 (not (link p7-9 p2-5))
 (not (path p7-9 p2-5))
 (not (link p7-9 p3-2))
 (not (path p7-9 p3-2))
 (not (link p7-9 p3-7))
 (not (path p7-9 p3-7))
 (not (link p7-9 p4-6))
 (not (path p7-9 p4-6))
 (not (link p7-9 p4-7))
 (not (path p7-9 p4-7))
 (not (link p7-9 p6-2))
 (not (path p7-9 p6-2))
 (not (link p7-9 p6-5))
 (not (path p7-9 p6-5))
 (not (link p7-9 p6-8))
 (not (path p7-9 p6-8))
 (not (link p7-9 p7-9))
 (not (path p7-9 p7-9))
 (not (link p7-9 p8-3))
 (not (path p7-9 p8-3))
 (not (link p7-9 p8-5))
 (not (path p7-9 p8-5))
 (not (link p7-9 p9-4))
 (not (path p7-9 p9-4))
 (not (link p8-3 s0))
 (not (path p8-3 s0))
 (not (link p8-3 s1))
 (not (path p8-3 s1))
 (not (link p8-3 s2))
 (not (path p8-3 s2))
 (not (link p8-3 s3))
 (path p8-3 s3)
 (not (link p8-3 s4))
 (not (path p8-3 s4))
 (not (link p8-3 s5))
 (not (path p8-3 s5))
 (not (link p8-3 s6))
 (not (path p8-3 s6))
 (not (link p8-3 s7))
 (not (path p8-3 s7))
 (not (link p8-3 s8))
 (path p8-3 s8)
 (not (link p8-3 s9))
 (not (path p8-3 s9))
 (not (link p8-3 p0-1))
 (not (path p8-3 p0-1))
 (not (link p8-3 p1-2))
 (not (path p8-3 p1-2))
 (not (link p8-3 p2-3))
 (not (path p8-3 p2-3))
 (not (link p8-3 p2-5))
 (not (path p8-3 p2-5))
 (not (link p8-3 p3-2))
 (not (path p8-3 p3-2))
 (not (link p8-3 p3-7))
 (not (path p8-3 p3-7))
 (not (link p8-3 p4-6))
 (not (path p8-3 p4-6))
 (not (link p8-3 p4-7))
 (not (path p8-3 p4-7))
 (not (link p8-3 p6-2))
 (not (path p8-3 p6-2))
 (not (link p8-3 p6-5))
 (not (path p8-3 p6-5))
 (not (link p8-3 p6-8))
 (not (path p8-3 p6-8))
 (not (link p8-3 p7-9))
 (not (path p8-3 p7-9))
 (not (link p8-3 p8-3))
 (not (path p8-3 p8-3))
 (not (link p8-3 p8-5))
 (not (path p8-3 p8-5))
 (not (link p8-3 p9-4))
 (not (path p8-3 p9-4))
 (not (link p8-5 s0))
 (not (path p8-5 s0))
 (not (link p8-5 s1))
 (not (path p8-5 s1))
 (not (link p8-5 s2))
 (not (path p8-5 s2))
 (not (link p8-5 s3))
 (not (path p8-5 s3))
 (not (link p8-5 s4))
 (not (path p8-5 s4))
 (not (link p8-5 s5))
 (path p8-5 s5)
 (not (link p8-5 s6))
 (not (path p8-5 s6))
 (not (link p8-5 s7))
 (not (path p8-5 s7))
 (not (link p8-5 s8))
 (path p8-5 s8)
 (not (link p8-5 s9))
 (not (path p8-5 s9))
 (not (link p8-5 p0-1))
 (not (path p8-5 p0-1))
 (not (link p8-5 p1-2))
 (not (path p8-5 p1-2))
 (not (link p8-5 p2-3))
 (not (path p8-5 p2-3))
 (not (link p8-5 p2-5))
 (not (path p8-5 p2-5))
 (not (link p8-5 p3-2))
 (not (path p8-5 p3-2))
 (not (link p8-5 p3-7))
 (not (path p8-5 p3-7))
 (not (link p8-5 p4-6))
 (not (path p8-5 p4-6))
 (not (link p8-5 p4-7))
 (not (path p8-5 p4-7))
 (not (link p8-5 p6-2))
 (not (path p8-5 p6-2))
 (not (link p8-5 p6-5))
 (not (path p8-5 p6-5))
 (not (link p8-5 p6-8))
 (not (path p8-5 p6-8))
 (not (link p8-5 p7-9))
 (not (path p8-5 p7-9))
 (not (link p8-5 p8-3))
 (not (path p8-5 p8-3))
 (not (link p8-5 p8-5))
 (not (path p8-5 p8-5))
 (not (link p8-5 p9-4))
 (not (path p8-5 p9-4))
 (not (link p9-4 s0))
 (not (path p9-4 s0))
 (not (link p9-4 s1))
 (not (path p9-4 s1))
 (not (link p9-4 s2))
 (not (path p9-4 s2))
 (not (link p9-4 s3))
 (not (path p9-4 s3))
 (not (link p9-4 s4))
 (path p9-4 s4)
 (not (link p9-4 s5))
 (not (path p9-4 s5))
 (not (link p9-4 s6))
 (not (path p9-4 s6))
 (not (link p9-4 s7))
 (not (path p9-4 s7))
 (not (link p9-4 s8))
 (not (path p9-4 s8))
 (not (link p9-4 s9))
 (path p9-4 s9)
 (not (link p9-4 p0-1))
 (not (path p9-4 p0-1))
 (not (link p9-4 p1-2))
 (not (path p9-4 p1-2))
 (not (link p9-4 p2-3))
 (not (path p9-4 p2-3))
 (not (link p9-4 p2-5))
 (not (path p9-4 p2-5))
 (not (link p9-4 p3-2))
 (not (path p9-4 p3-2))
 (not (link p9-4 p3-7))
 (not (path p9-4 p3-7))
 (not (link p9-4 p4-6))
 (not (path p9-4 p4-6))
 (not (link p9-4 p4-7))
 (not (path p9-4 p4-7))
 (not (link p9-4 p6-2))
 (not (path p9-4 p6-2))
 (not (link p9-4 p6-5))
 (not (path p9-4 p6-5))
 (not (link p9-4 p6-8))
 (not (path p9-4 p6-8))
 (not (link p9-4 p7-9))
 (not (path p9-4 p7-9))
 (not (link p9-4 p8-3))
 (not (path p9-4 p8-3))
 (not (link p9-4 p8-5))
 (not (path p9-4 p8-5))
 (not (link p9-4 p9-4))
 (not (path p9-4 p9-4))
)
(:global-goal (and
 (= (at driver3) s1)
 (= (pos truck1) s6)
 (= (pos truck3) s3)
 (= (in package1) s0)
 (= (in package2) s0)
 (= (in package3) s4)
 (= (in package4) s4)
 (= (in package5) s3)
 (= (in package6) s5)
))

)
