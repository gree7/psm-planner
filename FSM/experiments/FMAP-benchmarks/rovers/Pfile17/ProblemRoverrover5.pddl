(define (problem roverprob5624)
(:domain rover)
(:objects
 rover0 rover1 rover2 rover3 rover4 rover5 - rover
 waypoint0 waypoint1 waypoint2 waypoint3 waypoint4 waypoint5 waypoint6 waypoint7 waypoint8 waypoint9 waypoint10 waypoint11 waypoint12 waypoint13 waypoint14 - waypoint
 rover0store rover1store rover2store rover3store rover4store rover5store - store
 camera0 camera1 camera2 camera3 camera4 camera5 camera6 - camera
 colour high_res low_res - mode
 general - lander
 objective0 objective1 objective2 objective3 objective4 objective5 - objective
)
(:shared-data
  (communicated_soil_data ?w - waypoint)
  (communicated_rock_data ?w - waypoint)
  (communicated_image_data ?o - objective ?m - mode)
  (at_soil_sample ?w - waypoint)
  (at_rock_sample ?w - waypoint) -
(either rover0 rover1 rover2 rover3 rover4)
)
(:init (myRover rover5)
 (not (equipped_for_soil_analysis rover5))
 (equipped_for_rock_analysis rover5)
 (equipped_for_imaging rover5)
 (empty rover0store)
 (empty rover1store)
 (empty rover2store)
 (empty rover3store)
 (empty rover4store)
 (empty rover5store)
 (not (full rover0store))
 (not (full rover1store))
 (not (full rover2store))
 (not (full rover3store))
 (not (full rover4store))
 (not (full rover5store))
 (not (can_traverse rover5 waypoint0 waypoint0))
 (not (can_traverse rover5 waypoint0 waypoint1))
 (not (can_traverse rover5 waypoint0 waypoint2))
 (can_traverse rover5 waypoint0 waypoint3)
 (not (can_traverse rover5 waypoint0 waypoint4))
 (not (can_traverse rover5 waypoint0 waypoint5))
 (not (can_traverse rover5 waypoint0 waypoint6))
 (not (can_traverse rover5 waypoint0 waypoint7))
 (not (can_traverse rover5 waypoint0 waypoint8))
 (not (can_traverse rover5 waypoint0 waypoint9))
 (not (can_traverse rover5 waypoint0 waypoint10))
 (not (can_traverse rover5 waypoint0 waypoint11))
 (not (can_traverse rover5 waypoint0 waypoint12))
 (not (can_traverse rover5 waypoint0 waypoint13))
 (not (can_traverse rover5 waypoint0 waypoint14))
 (not (can_traverse rover5 waypoint1 waypoint0))
 (not (can_traverse rover5 waypoint1 waypoint1))
 (not (can_traverse rover5 waypoint1 waypoint2))
 (not (can_traverse rover5 waypoint1 waypoint3))
 (can_traverse rover5 waypoint1 waypoint4)
 (not (can_traverse rover5 waypoint1 waypoint5))
 (not (can_traverse rover5 waypoint1 waypoint6))
 (not (can_traverse rover5 waypoint1 waypoint7))
 (can_traverse rover5 waypoint1 waypoint8)
 (not (can_traverse rover5 waypoint1 waypoint9))
 (not (can_traverse rover5 waypoint1 waypoint10))
 (not (can_traverse rover5 waypoint1 waypoint11))
 (can_traverse rover5 waypoint1 waypoint12)
 (not (can_traverse rover5 waypoint1 waypoint13))
 (can_traverse rover5 waypoint1 waypoint14)
 (not (can_traverse rover5 waypoint2 waypoint0))
 (not (can_traverse rover5 waypoint2 waypoint1))
 (not (can_traverse rover5 waypoint2 waypoint2))
 (not (can_traverse rover5 waypoint2 waypoint3))
 (not (can_traverse rover5 waypoint2 waypoint4))
 (can_traverse rover5 waypoint2 waypoint5)
 (not (can_traverse rover5 waypoint2 waypoint6))
 (not (can_traverse rover5 waypoint2 waypoint7))
 (not (can_traverse rover5 waypoint2 waypoint8))
 (not (can_traverse rover5 waypoint2 waypoint9))
 (not (can_traverse rover5 waypoint2 waypoint10))
 (not (can_traverse rover5 waypoint2 waypoint11))
 (not (can_traverse rover5 waypoint2 waypoint12))
 (not (can_traverse rover5 waypoint2 waypoint13))
 (not (can_traverse rover5 waypoint2 waypoint14))
 (can_traverse rover5 waypoint3 waypoint0)
 (not (can_traverse rover5 waypoint3 waypoint1))
 (not (can_traverse rover5 waypoint3 waypoint2))
 (not (can_traverse rover5 waypoint3 waypoint3))
 (not (can_traverse rover5 waypoint3 waypoint4))
 (not (can_traverse rover5 waypoint3 waypoint5))
 (can_traverse rover5 waypoint3 waypoint6)
 (not (can_traverse rover5 waypoint3 waypoint7))
 (can_traverse rover5 waypoint3 waypoint8)
 (not (can_traverse rover5 waypoint3 waypoint9))
 (not (can_traverse rover5 waypoint3 waypoint10))
 (not (can_traverse rover5 waypoint3 waypoint11))
 (not (can_traverse rover5 waypoint3 waypoint12))
 (not (can_traverse rover5 waypoint3 waypoint13))
 (not (can_traverse rover5 waypoint3 waypoint14))
 (not (can_traverse rover5 waypoint4 waypoint0))
 (can_traverse rover5 waypoint4 waypoint1)
 (not (can_traverse rover5 waypoint4 waypoint2))
 (not (can_traverse rover5 waypoint4 waypoint3))
 (not (can_traverse rover5 waypoint4 waypoint4))
 (not (can_traverse rover5 waypoint4 waypoint5))
 (not (can_traverse rover5 waypoint4 waypoint6))
 (not (can_traverse rover5 waypoint4 waypoint7))
 (not (can_traverse rover5 waypoint4 waypoint8))
 (not (can_traverse rover5 waypoint4 waypoint9))
 (not (can_traverse rover5 waypoint4 waypoint10))
 (not (can_traverse rover5 waypoint4 waypoint11))
 (not (can_traverse rover5 waypoint4 waypoint12))
 (not (can_traverse rover5 waypoint4 waypoint13))
 (not (can_traverse rover5 waypoint4 waypoint14))
 (not (can_traverse rover5 waypoint5 waypoint0))
 (not (can_traverse rover5 waypoint5 waypoint1))
 (can_traverse rover5 waypoint5 waypoint2)
 (not (can_traverse rover5 waypoint5 waypoint3))
 (not (can_traverse rover5 waypoint5 waypoint4))
 (not (can_traverse rover5 waypoint5 waypoint5))
 (not (can_traverse rover5 waypoint5 waypoint6))
 (not (can_traverse rover5 waypoint5 waypoint7))
 (can_traverse rover5 waypoint5 waypoint8)
 (not (can_traverse rover5 waypoint5 waypoint9))
 (not (can_traverse rover5 waypoint5 waypoint10))
 (not (can_traverse rover5 waypoint5 waypoint11))
 (not (can_traverse rover5 waypoint5 waypoint12))
 (can_traverse rover5 waypoint5 waypoint13)
 (not (can_traverse rover5 waypoint5 waypoint14))
 (not (can_traverse rover5 waypoint6 waypoint0))
 (not (can_traverse rover5 waypoint6 waypoint1))
 (not (can_traverse rover5 waypoint6 waypoint2))
 (can_traverse rover5 waypoint6 waypoint3)
 (not (can_traverse rover5 waypoint6 waypoint4))
 (not (can_traverse rover5 waypoint6 waypoint5))
 (not (can_traverse rover5 waypoint6 waypoint6))
 (not (can_traverse rover5 waypoint6 waypoint7))
 (not (can_traverse rover5 waypoint6 waypoint8))
 (not (can_traverse rover5 waypoint6 waypoint9))
 (not (can_traverse rover5 waypoint6 waypoint10))
 (not (can_traverse rover5 waypoint6 waypoint11))
 (not (can_traverse rover5 waypoint6 waypoint12))
 (not (can_traverse rover5 waypoint6 waypoint13))
 (not (can_traverse rover5 waypoint6 waypoint14))
 (not (can_traverse rover5 waypoint7 waypoint0))
 (not (can_traverse rover5 waypoint7 waypoint1))
 (not (can_traverse rover5 waypoint7 waypoint2))
 (not (can_traverse rover5 waypoint7 waypoint3))
 (not (can_traverse rover5 waypoint7 waypoint4))
 (not (can_traverse rover5 waypoint7 waypoint5))
 (not (can_traverse rover5 waypoint7 waypoint6))
 (not (can_traverse rover5 waypoint7 waypoint7))
 (can_traverse rover5 waypoint7 waypoint8)
 (not (can_traverse rover5 waypoint7 waypoint9))
 (not (can_traverse rover5 waypoint7 waypoint10))
 (not (can_traverse rover5 waypoint7 waypoint11))
 (not (can_traverse rover5 waypoint7 waypoint12))
 (not (can_traverse rover5 waypoint7 waypoint13))
 (not (can_traverse rover5 waypoint7 waypoint14))
 (not (can_traverse rover5 waypoint8 waypoint0))
 (can_traverse rover5 waypoint8 waypoint1)
 (not (can_traverse rover5 waypoint8 waypoint2))
 (can_traverse rover5 waypoint8 waypoint3)
 (not (can_traverse rover5 waypoint8 waypoint4))
 (can_traverse rover5 waypoint8 waypoint5)
 (not (can_traverse rover5 waypoint8 waypoint6))
 (can_traverse rover5 waypoint8 waypoint7)
 (not (can_traverse rover5 waypoint8 waypoint8))
 (can_traverse rover5 waypoint8 waypoint9)
 (can_traverse rover5 waypoint8 waypoint10)
 (can_traverse rover5 waypoint8 waypoint11)
 (not (can_traverse rover5 waypoint8 waypoint12))
 (not (can_traverse rover5 waypoint8 waypoint13))
 (not (can_traverse rover5 waypoint8 waypoint14))
 (not (can_traverse rover5 waypoint9 waypoint0))
 (not (can_traverse rover5 waypoint9 waypoint1))
 (not (can_traverse rover5 waypoint9 waypoint2))
 (not (can_traverse rover5 waypoint9 waypoint3))
 (not (can_traverse rover5 waypoint9 waypoint4))
 (not (can_traverse rover5 waypoint9 waypoint5))
 (not (can_traverse rover5 waypoint9 waypoint6))
 (not (can_traverse rover5 waypoint9 waypoint7))
 (can_traverse rover5 waypoint9 waypoint8)
 (not (can_traverse rover5 waypoint9 waypoint9))
 (not (can_traverse rover5 waypoint9 waypoint10))
 (not (can_traverse rover5 waypoint9 waypoint11))
 (not (can_traverse rover5 waypoint9 waypoint12))
 (not (can_traverse rover5 waypoint9 waypoint13))
 (not (can_traverse rover5 waypoint9 waypoint14))
 (not (can_traverse rover5 waypoint10 waypoint0))
 (not (can_traverse rover5 waypoint10 waypoint1))
 (not (can_traverse rover5 waypoint10 waypoint2))
 (not (can_traverse rover5 waypoint10 waypoint3))
 (not (can_traverse rover5 waypoint10 waypoint4))
 (not (can_traverse rover5 waypoint10 waypoint5))
 (not (can_traverse rover5 waypoint10 waypoint6))
 (not (can_traverse rover5 waypoint10 waypoint7))
 (can_traverse rover5 waypoint10 waypoint8)
 (not (can_traverse rover5 waypoint10 waypoint9))
 (not (can_traverse rover5 waypoint10 waypoint10))
 (not (can_traverse rover5 waypoint10 waypoint11))
 (not (can_traverse rover5 waypoint10 waypoint12))
 (not (can_traverse rover5 waypoint10 waypoint13))
 (not (can_traverse rover5 waypoint10 waypoint14))
 (not (can_traverse rover5 waypoint11 waypoint0))
 (not (can_traverse rover5 waypoint11 waypoint1))
 (not (can_traverse rover5 waypoint11 waypoint2))
 (not (can_traverse rover5 waypoint11 waypoint3))
 (not (can_traverse rover5 waypoint11 waypoint4))
 (not (can_traverse rover5 waypoint11 waypoint5))
 (not (can_traverse rover5 waypoint11 waypoint6))
 (not (can_traverse rover5 waypoint11 waypoint7))
 (can_traverse rover5 waypoint11 waypoint8)
 (not (can_traverse rover5 waypoint11 waypoint9))
 (not (can_traverse rover5 waypoint11 waypoint10))
 (not (can_traverse rover5 waypoint11 waypoint11))
 (not (can_traverse rover5 waypoint11 waypoint12))
 (not (can_traverse rover5 waypoint11 waypoint13))
 (not (can_traverse rover5 waypoint11 waypoint14))
 (not (can_traverse rover5 waypoint12 waypoint0))
 (can_traverse rover5 waypoint12 waypoint1)
 (not (can_traverse rover5 waypoint12 waypoint2))
 (not (can_traverse rover5 waypoint12 waypoint3))
 (not (can_traverse rover5 waypoint12 waypoint4))
 (not (can_traverse rover5 waypoint12 waypoint5))
 (not (can_traverse rover5 waypoint12 waypoint6))
 (not (can_traverse rover5 waypoint12 waypoint7))
 (not (can_traverse rover5 waypoint12 waypoint8))
 (not (can_traverse rover5 waypoint12 waypoint9))
 (not (can_traverse rover5 waypoint12 waypoint10))
 (not (can_traverse rover5 waypoint12 waypoint11))
 (not (can_traverse rover5 waypoint12 waypoint12))
 (not (can_traverse rover5 waypoint12 waypoint13))
 (not (can_traverse rover5 waypoint12 waypoint14))
 (not (can_traverse rover5 waypoint13 waypoint0))
 (not (can_traverse rover5 waypoint13 waypoint1))
 (not (can_traverse rover5 waypoint13 waypoint2))
 (not (can_traverse rover5 waypoint13 waypoint3))
 (not (can_traverse rover5 waypoint13 waypoint4))
 (can_traverse rover5 waypoint13 waypoint5)
 (not (can_traverse rover5 waypoint13 waypoint6))
 (not (can_traverse rover5 waypoint13 waypoint7))
 (not (can_traverse rover5 waypoint13 waypoint8))
 (not (can_traverse rover5 waypoint13 waypoint9))
 (not (can_traverse rover5 waypoint13 waypoint10))
 (not (can_traverse rover5 waypoint13 waypoint11))
 (not (can_traverse rover5 waypoint13 waypoint12))
 (not (can_traverse rover5 waypoint13 waypoint13))
 (not (can_traverse rover5 waypoint13 waypoint14))
 (not (can_traverse rover5 waypoint14 waypoint0))
 (can_traverse rover5 waypoint14 waypoint1)
 (not (can_traverse rover5 waypoint14 waypoint2))
 (not (can_traverse rover5 waypoint14 waypoint3))
 (not (can_traverse rover5 waypoint14 waypoint4))
 (not (can_traverse rover5 waypoint14 waypoint5))
 (not (can_traverse rover5 waypoint14 waypoint6))
 (not (can_traverse rover5 waypoint14 waypoint7))
 (not (can_traverse rover5 waypoint14 waypoint8))
 (not (can_traverse rover5 waypoint14 waypoint9))
 (not (can_traverse rover5 waypoint14 waypoint10))
 (not (can_traverse rover5 waypoint14 waypoint11))
 (not (can_traverse rover5 waypoint14 waypoint12))
 (not (can_traverse rover5 waypoint14 waypoint13))
 (not (can_traverse rover5 waypoint14 waypoint14))
 (not (have_rock_analysis rover5 waypoint0))
 (not (have_rock_analysis rover5 waypoint1))
 (not (have_rock_analysis rover5 waypoint2))
 (not (have_rock_analysis rover5 waypoint3))
 (not (have_rock_analysis rover5 waypoint4))
 (not (have_rock_analysis rover5 waypoint5))
 (not (have_rock_analysis rover5 waypoint6))
 (not (have_rock_analysis rover5 waypoint7))
 (not (have_rock_analysis rover5 waypoint8))
 (not (have_rock_analysis rover5 waypoint9))
 (not (have_rock_analysis rover5 waypoint10))
 (not (have_rock_analysis rover5 waypoint11))
 (not (have_rock_analysis rover5 waypoint12))
 (not (have_rock_analysis rover5 waypoint13))
 (not (have_rock_analysis rover5 waypoint14))
 (not (have_soil_analysis rover5 waypoint0))
 (not (have_soil_analysis rover5 waypoint1))
 (not (have_soil_analysis rover5 waypoint2))
 (not (have_soil_analysis rover5 waypoint3))
 (not (have_soil_analysis rover5 waypoint4))
 (not (have_soil_analysis rover5 waypoint5))
 (not (have_soil_analysis rover5 waypoint6))
 (not (have_soil_analysis rover5 waypoint7))
 (not (have_soil_analysis rover5 waypoint8))
 (not (have_soil_analysis rover5 waypoint9))
 (not (have_soil_analysis rover5 waypoint10))
 (not (have_soil_analysis rover5 waypoint11))
 (not (have_soil_analysis rover5 waypoint12))
 (not (have_soil_analysis rover5 waypoint13))
 (not (have_soil_analysis rover5 waypoint14))
 (not (visible waypoint0 waypoint0))
 (visible waypoint0 waypoint1)
 (not (visible waypoint0 waypoint2))
 (visible waypoint0 waypoint3)
 (not (visible waypoint0 waypoint4))
 (not (visible waypoint0 waypoint5))
 (visible waypoint0 waypoint6)
 (not (visible waypoint0 waypoint7))
 (not (visible waypoint0 waypoint8))
 (not (visible waypoint0 waypoint9))
 (visible waypoint0 waypoint10)
 (not (visible waypoint0 waypoint11))
 (visible waypoint0 waypoint12)
 (visible waypoint0 waypoint13)
 (not (visible waypoint0 waypoint14))
 (visible waypoint1 waypoint0)
 (not (visible waypoint1 waypoint1))
 (not (visible waypoint1 waypoint2))
 (not (visible waypoint1 waypoint3))
 (visible waypoint1 waypoint4)
 (visible waypoint1 waypoint5)
 (not (visible waypoint1 waypoint6))
 (not (visible waypoint1 waypoint7))
 (visible waypoint1 waypoint8)
 (not (visible waypoint1 waypoint9))
 (not (visible waypoint1 waypoint10))
 (visible waypoint1 waypoint11)
 (visible waypoint1 waypoint12)
 (not (visible waypoint1 waypoint13))
 (visible waypoint1 waypoint14)
 (not (visible waypoint2 waypoint0))
 (not (visible waypoint2 waypoint1))
 (not (visible waypoint2 waypoint2))
 (visible waypoint2 waypoint3)
 (visible waypoint2 waypoint4)
 (visible waypoint2 waypoint5)
 (visible waypoint2 waypoint6)
 (not (visible waypoint2 waypoint7))
 (visible waypoint2 waypoint8)
 (visible waypoint2 waypoint9)
 (not (visible waypoint2 waypoint10))
 (visible waypoint2 waypoint11)
 (not (visible waypoint2 waypoint12))
 (not (visible waypoint2 waypoint13))
 (visible waypoint2 waypoint14)
 (visible waypoint3 waypoint0)
 (not (visible waypoint3 waypoint1))
 (visible waypoint3 waypoint2)
 (not (visible waypoint3 waypoint3))
 (not (visible waypoint3 waypoint4))
 (visible waypoint3 waypoint5)
 (visible waypoint3 waypoint6)
 (visible waypoint3 waypoint7)
 (visible waypoint3 waypoint8)
 (not (visible waypoint3 waypoint9))
 (visible waypoint3 waypoint10)
 (visible waypoint3 waypoint11)
 (not (visible waypoint3 waypoint12))
 (visible waypoint3 waypoint13)
 (visible waypoint3 waypoint14)
 (not (visible waypoint4 waypoint0))
 (visible waypoint4 waypoint1)
 (visible waypoint4 waypoint2)
 (not (visible waypoint4 waypoint3))
 (not (visible waypoint4 waypoint4))
 (not (visible waypoint4 waypoint5))
 (visible waypoint4 waypoint6)
 (not (visible waypoint4 waypoint7))
 (not (visible waypoint4 waypoint8))
 (not (visible waypoint4 waypoint9))
 (visible waypoint4 waypoint10)
 (visible waypoint4 waypoint11)
 (not (visible waypoint4 waypoint12))
 (not (visible waypoint4 waypoint13))
 (visible waypoint4 waypoint14)
 (not (visible waypoint5 waypoint0))
 (visible waypoint5 waypoint1)
 (visible waypoint5 waypoint2)
 (visible waypoint5 waypoint3)
 (not (visible waypoint5 waypoint4))
 (not (visible waypoint5 waypoint5))
 (visible waypoint5 waypoint6)
 (not (visible waypoint5 waypoint7))
 (visible waypoint5 waypoint8)
 (not (visible waypoint5 waypoint9))
 (not (visible waypoint5 waypoint10))
 (not (visible waypoint5 waypoint11))
 (visible waypoint5 waypoint12)
 (visible waypoint5 waypoint13)
 (visible waypoint5 waypoint14)
 (visible waypoint6 waypoint0)
 (not (visible waypoint6 waypoint1))
 (visible waypoint6 waypoint2)
 (visible waypoint6 waypoint3)
 (visible waypoint6 waypoint4)
 (visible waypoint6 waypoint5)
 (not (visible waypoint6 waypoint6))
 (visible waypoint6 waypoint7)
 (not (visible waypoint6 waypoint8))
 (not (visible waypoint6 waypoint9))
 (visible waypoint6 waypoint10)
 (not (visible waypoint6 waypoint11))
 (visible waypoint6 waypoint12)
 (visible waypoint6 waypoint13)
 (not (visible waypoint6 waypoint14))
 (not (visible waypoint7 waypoint0))
 (not (visible waypoint7 waypoint1))
 (not (visible waypoint7 waypoint2))
 (visible waypoint7 waypoint3)
 (not (visible waypoint7 waypoint4))
 (not (visible waypoint7 waypoint5))
 (visible waypoint7 waypoint6)
 (not (visible waypoint7 waypoint7))
 (visible waypoint7 waypoint8)
 (visible waypoint7 waypoint9)
 (not (visible waypoint7 waypoint10))
 (not (visible waypoint7 waypoint11))
 (visible waypoint7 waypoint12)
 (not (visible waypoint7 waypoint13))
 (not (visible waypoint7 waypoint14))
 (not (visible waypoint8 waypoint0))
 (visible waypoint8 waypoint1)
 (visible waypoint8 waypoint2)
 (visible waypoint8 waypoint3)
 (not (visible waypoint8 waypoint4))
 (visible waypoint8 waypoint5)
 (not (visible waypoint8 waypoint6))
 (visible waypoint8 waypoint7)
 (not (visible waypoint8 waypoint8))
 (visible waypoint8 waypoint9)
 (visible waypoint8 waypoint10)
 (visible waypoint8 waypoint11)
 (not (visible waypoint8 waypoint12))
 (not (visible waypoint8 waypoint13))
 (not (visible waypoint8 waypoint14))
 (not (visible waypoint9 waypoint0))
 (not (visible waypoint9 waypoint1))
 (visible waypoint9 waypoint2)
 (not (visible waypoint9 waypoint3))
 (not (visible waypoint9 waypoint4))
 (not (visible waypoint9 waypoint5))
 (not (visible waypoint9 waypoint6))
 (visible waypoint9 waypoint7)
 (visible waypoint9 waypoint8)
 (not (visible waypoint9 waypoint9))
 (not (visible waypoint9 waypoint10))
 (visible waypoint9 waypoint11)
 (not (visible waypoint9 waypoint12))
 (not (visible waypoint9 waypoint13))
 (visible waypoint9 waypoint14)
 (visible waypoint10 waypoint0)
 (not (visible waypoint10 waypoint1))
 (not (visible waypoint10 waypoint2))
 (visible waypoint10 waypoint3)
 (visible waypoint10 waypoint4)
 (not (visible waypoint10 waypoint5))
 (visible waypoint10 waypoint6)
 (not (visible waypoint10 waypoint7))
 (visible waypoint10 waypoint8)
 (not (visible waypoint10 waypoint9))
 (not (visible waypoint10 waypoint10))
 (visible waypoint10 waypoint11)
 (not (visible waypoint10 waypoint12))
 (not (visible waypoint10 waypoint13))
 (visible waypoint10 waypoint14)
 (not (visible waypoint11 waypoint0))
 (visible waypoint11 waypoint1)
 (visible waypoint11 waypoint2)
 (visible waypoint11 waypoint3)
 (visible waypoint11 waypoint4)
 (not (visible waypoint11 waypoint5))
 (not (visible waypoint11 waypoint6))
 (not (visible waypoint11 waypoint7))
 (visible waypoint11 waypoint8)
 (visible waypoint11 waypoint9)
 (visible waypoint11 waypoint10)
 (not (visible waypoint11 waypoint11))
 (not (visible waypoint11 waypoint12))
 (not (visible waypoint11 waypoint13))
 (not (visible waypoint11 waypoint14))
 (visible waypoint12 waypoint0)
 (visible waypoint12 waypoint1)
 (not (visible waypoint12 waypoint2))
 (not (visible waypoint12 waypoint3))
 (not (visible waypoint12 waypoint4))
 (visible waypoint12 waypoint5)
 (visible waypoint12 waypoint6)
 (visible waypoint12 waypoint7)
 (not (visible waypoint12 waypoint8))
 (not (visible waypoint12 waypoint9))
 (not (visible waypoint12 waypoint10))
 (not (visible waypoint12 waypoint11))
 (not (visible waypoint12 waypoint12))
 (not (visible waypoint12 waypoint13))
 (not (visible waypoint12 waypoint14))
 (visible waypoint13 waypoint0)
 (not (visible waypoint13 waypoint1))
 (not (visible waypoint13 waypoint2))
 (visible waypoint13 waypoint3)
 (not (visible waypoint13 waypoint4))
 (visible waypoint13 waypoint5)
 (visible waypoint13 waypoint6)
 (not (visible waypoint13 waypoint7))
 (not (visible waypoint13 waypoint8))
 (not (visible waypoint13 waypoint9))
 (not (visible waypoint13 waypoint10))
 (not (visible waypoint13 waypoint11))
 (not (visible waypoint13 waypoint12))
 (not (visible waypoint13 waypoint13))
 (visible waypoint13 waypoint14)
 (not (visible waypoint14 waypoint0))
 (visible waypoint14 waypoint1)
 (visible waypoint14 waypoint2)
 (visible waypoint14 waypoint3)
 (visible waypoint14 waypoint4)
 (visible waypoint14 waypoint5)
 (not (visible waypoint14 waypoint6))
 (not (visible waypoint14 waypoint7))
 (not (visible waypoint14 waypoint8))
 (visible waypoint14 waypoint9)
 (visible waypoint14 waypoint10)
 (not (visible waypoint14 waypoint11))
 (not (visible waypoint14 waypoint12))
 (visible waypoint14 waypoint13)
 (not (visible waypoint14 waypoint14))
 (not (communicated_soil_data waypoint0))
 (not (communicated_soil_data waypoint1))
 (not (communicated_soil_data waypoint2))
 (not (communicated_soil_data waypoint3))
 (not (communicated_soil_data waypoint4))
 (not (communicated_soil_data waypoint5))
 (not (communicated_soil_data waypoint6))
 (not (communicated_soil_data waypoint7))
 (not (communicated_soil_data waypoint8))
 (not (communicated_soil_data waypoint9))
 (not (communicated_soil_data waypoint10))
 (not (communicated_soil_data waypoint11))
 (not (communicated_soil_data waypoint12))
 (not (communicated_soil_data waypoint13))
 (not (communicated_soil_data waypoint14))
 (not (communicated_rock_data waypoint0))
 (not (communicated_rock_data waypoint1))
 (not (communicated_rock_data waypoint2))
 (not (communicated_rock_data waypoint3))
 (not (communicated_rock_data waypoint4))
 (not (communicated_rock_data waypoint5))
 (not (communicated_rock_data waypoint6))
 (not (communicated_rock_data waypoint7))
 (not (communicated_rock_data waypoint8))
 (not (communicated_rock_data waypoint9))
 (not (communicated_rock_data waypoint10))
 (not (communicated_rock_data waypoint11))
 (not (communicated_rock_data waypoint12))
 (not (communicated_rock_data waypoint13))
 (not (communicated_rock_data waypoint14))
 (at_soil_sample waypoint0)
 (not (at_soil_sample waypoint1))
 (at_soil_sample waypoint2)
 (at_soil_sample waypoint3)
 (at_soil_sample waypoint4)
 (at_soil_sample waypoint5)
 (not (at_soil_sample waypoint6))
 (not (at_soil_sample waypoint7))
 (not (at_soil_sample waypoint8))
 (at_soil_sample waypoint9)
 (not (at_soil_sample waypoint10))
 (not (at_soil_sample waypoint11))
 (not (at_soil_sample waypoint12))
 (at_soil_sample waypoint13)
 (at_soil_sample waypoint14)
 (not (at_rock_sample waypoint0))
 (at_rock_sample waypoint1)
 (at_rock_sample waypoint2)
 (at_rock_sample waypoint3)
 (at_rock_sample waypoint4)
 (at_rock_sample waypoint5)
 (at_rock_sample waypoint6)
 (at_rock_sample waypoint7)
 (not (at_rock_sample waypoint8))
 (at_rock_sample waypoint9)
 (at_rock_sample waypoint10)
 (at_rock_sample waypoint11)
 (at_rock_sample waypoint12)
 (at_rock_sample waypoint13)
 (at_rock_sample waypoint14)
 (not (calibrated camera0 rover5))
 (not (calibrated camera1 rover5))
 (not (calibrated camera2 rover5))
 (not (calibrated camera3 rover5))
 (not (calibrated camera4 rover5))
 (not (calibrated camera5 rover5))
 (not (calibrated camera6 rover5))
 (not (supports camera0 colour))
 (supports camera0 high_res)
 (supports camera0 low_res)
 (supports camera1 colour)
 (not (supports camera1 high_res))
 (not (supports camera1 low_res))
 (supports camera2 colour)
 (supports camera2 high_res)
 (not (supports camera2 low_res))
 (supports camera3 colour)
 (not (supports camera3 high_res))
 (not (supports camera3 low_res))
 (supports camera4 colour)
 (supports camera4 high_res)
 (supports camera4 low_res)
 (not (supports camera5 colour))
 (not (supports camera5 high_res))
 (supports camera5 low_res)
 (supports camera6 colour)
 (supports camera6 high_res)
 (not (supports camera6 low_res))
 (not (have_image rover5 objective0 colour))
 (not (have_image rover5 objective0 high_res))
 (not (have_image rover5 objective0 low_res))
 (not (have_image rover5 objective1 colour))
 (not (have_image rover5 objective1 high_res))
 (not (have_image rover5 objective1 low_res))
 (not (have_image rover5 objective2 colour))
 (not (have_image rover5 objective2 high_res))
 (not (have_image rover5 objective2 low_res))
 (not (have_image rover5 objective3 colour))
 (not (have_image rover5 objective3 high_res))
 (not (have_image rover5 objective3 low_res))
 (not (have_image rover5 objective4 colour))
 (not (have_image rover5 objective4 high_res))
 (not (have_image rover5 objective4 low_res))
 (not (have_image rover5 objective5 colour))
 (not (have_image rover5 objective5 high_res))
 (not (have_image rover5 objective5 low_res))
 (not (communicated_image_data objective0 colour))
 (not (communicated_image_data objective0 high_res))
 (not (communicated_image_data objective0 low_res))
 (not (communicated_image_data objective1 colour))
 (not (communicated_image_data objective1 high_res))
 (not (communicated_image_data objective1 low_res))
 (not (communicated_image_data objective2 colour))
 (not (communicated_image_data objective2 high_res))
 (not (communicated_image_data objective2 low_res))
 (not (communicated_image_data objective3 colour))
 (not (communicated_image_data objective3 high_res))
 (not (communicated_image_data objective3 low_res))
 (not (communicated_image_data objective4 colour))
 (not (communicated_image_data objective4 high_res))
 (not (communicated_image_data objective4 low_res))
 (not (communicated_image_data objective5 colour))
 (not (communicated_image_data objective5 high_res))
 (not (communicated_image_data objective5 low_res))
 (visible_from objective0 waypoint0)
 (visible_from objective0 waypoint1)
 (visible_from objective0 waypoint2)
 (visible_from objective0 waypoint3)
 (not (visible_from objective0 waypoint4))
 (not (visible_from objective0 waypoint5))
 (not (visible_from objective0 waypoint6))
 (not (visible_from objective0 waypoint7))
 (not (visible_from objective0 waypoint8))
 (not (visible_from objective0 waypoint9))
 (not (visible_from objective0 waypoint10))
 (not (visible_from objective0 waypoint11))
 (not (visible_from objective0 waypoint12))
 (not (visible_from objective0 waypoint13))
 (not (visible_from objective0 waypoint14))
 (visible_from objective1 waypoint0)
 (visible_from objective1 waypoint1)
 (not (visible_from objective1 waypoint2))
 (not (visible_from objective1 waypoint3))
 (not (visible_from objective1 waypoint4))
 (not (visible_from objective1 waypoint5))
 (not (visible_from objective1 waypoint6))
 (not (visible_from objective1 waypoint7))
 (not (visible_from objective1 waypoint8))
 (not (visible_from objective1 waypoint9))
 (not (visible_from objective1 waypoint10))
 (not (visible_from objective1 waypoint11))
 (not (visible_from objective1 waypoint12))
 (not (visible_from objective1 waypoint13))
 (not (visible_from objective1 waypoint14))
 (visible_from objective2 waypoint0)
 (visible_from objective2 waypoint1)
 (visible_from objective2 waypoint2)
 (visible_from objective2 waypoint3)
 (visible_from objective2 waypoint4)
 (visible_from objective2 waypoint5)
 (visible_from objective2 waypoint6)
 (visible_from objective2 waypoint7)
 (visible_from objective2 waypoint8)
 (visible_from objective2 waypoint9)
 (visible_from objective2 waypoint10)
 (visible_from objective2 waypoint11)
 (visible_from objective2 waypoint12)
 (visible_from objective2 waypoint13)
 (visible_from objective2 waypoint14)
 (visible_from objective3 waypoint0)
 (visible_from objective3 waypoint1)
 (visible_from objective3 waypoint2)
 (visible_from objective3 waypoint3)
 (visible_from objective3 waypoint4)
 (visible_from objective3 waypoint5)
 (not (visible_from objective3 waypoint6))
 (not (visible_from objective3 waypoint7))
 (not (visible_from objective3 waypoint8))
 (not (visible_from objective3 waypoint9))
 (not (visible_from objective3 waypoint10))
 (not (visible_from objective3 waypoint11))
 (not (visible_from objective3 waypoint12))
 (not (visible_from objective3 waypoint13))
 (not (visible_from objective3 waypoint14))
 (visible_from objective4 waypoint0)
 (visible_from objective4 waypoint1)
 (visible_from objective4 waypoint2)
 (visible_from objective4 waypoint3)
 (visible_from objective4 waypoint4)
 (visible_from objective4 waypoint5)
 (visible_from objective4 waypoint6)
 (visible_from objective4 waypoint7)
 (visible_from objective4 waypoint8)
 (visible_from objective4 waypoint9)
 (visible_from objective4 waypoint10)
 (visible_from objective4 waypoint11)
 (visible_from objective4 waypoint12)
 (not (visible_from objective4 waypoint13))
 (not (visible_from objective4 waypoint14))
 (visible_from objective5 waypoint0)
 (visible_from objective5 waypoint1)
 (not (visible_from objective5 waypoint2))
 (not (visible_from objective5 waypoint3))
 (not (visible_from objective5 waypoint4))
 (not (visible_from objective5 waypoint5))
 (not (visible_from objective5 waypoint6))
 (not (visible_from objective5 waypoint7))
 (not (visible_from objective5 waypoint8))
 (not (visible_from objective5 waypoint9))
 (not (visible_from objective5 waypoint10))
 (not (visible_from objective5 waypoint11))
 (not (visible_from objective5 waypoint12))
 (not (visible_from objective5 waypoint13))
 (not (visible_from objective5 waypoint14))
 (= (at rover5) waypoint8)
 (= (at_lander general) waypoint13)
 (= (store_of rover0store) rover0)
 (= (store_of rover1store) rover1)
 (= (store_of rover2store) rover2)
 (= (store_of rover3store) rover3)
 (= (store_of rover4store) rover4)
 (= (store_of rover5store) rover5)
 (= (calibration_target camera0) objective2)
 (= (calibration_target camera1) objective2)
 (= (calibration_target camera2) objective1)
 (= (calibration_target camera3) objective5)
 (= (calibration_target camera4) objective3)
 (= (calibration_target camera5) objective0)
 (= (calibration_target camera6) objective5)
 (= (on_board camera0) rover4)
 (= (on_board camera1) rover3)
 (= (on_board camera2) rover5)
 (= (on_board camera3) rover3)
 (= (on_board camera4) rover5)
 (= (on_board camera5) rover1)
 (= (on_board camera6) rover2)
)
(:global-goal (and
 (communicated_soil_data waypoint14)
 (communicated_soil_data waypoint5)
 (communicated_soil_data waypoint2)
 (communicated_soil_data waypoint3)
 (communicated_rock_data waypoint3)
 (communicated_rock_data waypoint5)
 (communicated_rock_data waypoint12)
 (communicated_rock_data waypoint9)
 (communicated_image_data objective2 colour)
 (communicated_image_data objective2 low_res)
 (communicated_image_data objective3 colour)
 (communicated_image_data objective5 colour)
 (communicated_image_data objective4 colour)
))

)