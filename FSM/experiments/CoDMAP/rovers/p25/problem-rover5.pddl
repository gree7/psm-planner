(define (problem roverprob3652) (:domain rover)
(:objects
	rover7store - store
	rover3store - store
	waypoint15 - waypoint
	rover4store - store
	rover9store - store
	high_res - mode
	waypoint18 - waypoint
	waypoint19 - waypoint
	low_res - mode
	rover5store - store
	objective1 - objective
	objective0 - objective
	objective3 - objective
	objective2 - objective
	objective5 - objective
	objective4 - objective
	waypoint10 - waypoint
	rover8store - store
	waypoint11 - waypoint
	waypoint12 - waypoint
	waypoint13 - waypoint
	camera2 - camera
	rover0store - store
	general - lander
	waypoint16 - waypoint
	camera7 - camera
	rover6store - store
	waypoint24 - waypoint
	rover2store - store
	waypoint21 - waypoint
	waypoint20 - waypoint
	waypoint23 - waypoint
	waypoint22 - waypoint
	camera4 - camera
	waypoint8 - waypoint
	waypoint9 - waypoint
	waypoint6 - waypoint
	waypoint7 - waypoint
	waypoint4 - waypoint
	waypoint5 - waypoint
	waypoint2 - waypoint
	waypoint3 - waypoint
	waypoint0 - waypoint
	waypoint1 - waypoint
	camera8 - camera
	camera9 - camera
	camera0 - camera
	camera1 - camera
	colour - mode
	camera3 - camera
	waypoint14 - waypoint
	camera5 - camera
	camera6 - camera
	waypoint17 - waypoint
	rover1store - store

	(:private
		rover5 - rover
	)
)
(:init
	(visible waypoint0 waypoint1)
	(visible waypoint1 waypoint0)
	(visible waypoint0 waypoint2)
	(visible waypoint2 waypoint0)
	(visible waypoint0 waypoint6)
	(visible waypoint6 waypoint0)
	(visible waypoint0 waypoint15)
	(visible waypoint15 waypoint0)
	(visible waypoint1 waypoint4)
	(visible waypoint4 waypoint1)
	(visible waypoint1 waypoint6)
	(visible waypoint6 waypoint1)
	(visible waypoint1 waypoint8)
	(visible waypoint8 waypoint1)
	(visible waypoint1 waypoint11)
	(visible waypoint11 waypoint1)
	(visible waypoint1 waypoint24)
	(visible waypoint24 waypoint1)
	(visible waypoint2 waypoint14)
	(visible waypoint14 waypoint2)
	(visible waypoint2 waypoint17)
	(visible waypoint17 waypoint2)
	(visible waypoint2 waypoint20)
	(visible waypoint20 waypoint2)
	(visible waypoint3 waypoint1)
	(visible waypoint1 waypoint3)
	(visible waypoint4 waypoint0)
	(visible waypoint0 waypoint4)
	(visible waypoint4 waypoint13)
	(visible waypoint13 waypoint4)
	(visible waypoint4 waypoint14)
	(visible waypoint14 waypoint4)
	(visible waypoint4 waypoint17)
	(visible waypoint17 waypoint4)
	(visible waypoint4 waypoint19)
	(visible waypoint19 waypoint4)
	(visible waypoint4 waypoint21)
	(visible waypoint21 waypoint4)
	(visible waypoint4 waypoint24)
	(visible waypoint24 waypoint4)
	(visible waypoint5 waypoint16)
	(visible waypoint16 waypoint5)
	(visible waypoint6 waypoint2)
	(visible waypoint2 waypoint6)
	(visible waypoint6 waypoint3)
	(visible waypoint3 waypoint6)
	(visible waypoint6 waypoint5)
	(visible waypoint5 waypoint6)
	(visible waypoint6 waypoint13)
	(visible waypoint13 waypoint6)
	(visible waypoint6 waypoint16)
	(visible waypoint16 waypoint6)
	(visible waypoint6 waypoint18)
	(visible waypoint18 waypoint6)
	(visible waypoint7 waypoint1)
	(visible waypoint1 waypoint7)
	(visible waypoint7 waypoint4)
	(visible waypoint4 waypoint7)
	(visible waypoint7 waypoint8)
	(visible waypoint8 waypoint7)
	(visible waypoint7 waypoint10)
	(visible waypoint10 waypoint7)
	(visible waypoint7 waypoint13)
	(visible waypoint13 waypoint7)
	(visible waypoint7 waypoint22)
	(visible waypoint22 waypoint7)
	(visible waypoint8 waypoint9)
	(visible waypoint9 waypoint8)
	(visible waypoint9 waypoint2)
	(visible waypoint2 waypoint9)
	(visible waypoint9 waypoint4)
	(visible waypoint4 waypoint9)
	(visible waypoint9 waypoint16)
	(visible waypoint16 waypoint9)
	(visible waypoint9 waypoint18)
	(visible waypoint18 waypoint9)
	(visible waypoint10 waypoint1)
	(visible waypoint1 waypoint10)
	(visible waypoint10 waypoint9)
	(visible waypoint9 waypoint10)
	(visible waypoint10 waypoint12)
	(visible waypoint12 waypoint10)
	(visible waypoint10 waypoint15)
	(visible waypoint15 waypoint10)
	(visible waypoint11 waypoint15)
	(visible waypoint15 waypoint11)
	(visible waypoint12 waypoint7)
	(visible waypoint7 waypoint12)
	(visible waypoint12 waypoint9)
	(visible waypoint9 waypoint12)
	(visible waypoint12 waypoint18)
	(visible waypoint18 waypoint12)
	(visible waypoint12 waypoint20)
	(visible waypoint20 waypoint12)
	(visible waypoint12 waypoint21)
	(visible waypoint21 waypoint12)
	(visible waypoint13 waypoint9)
	(visible waypoint9 waypoint13)
	(visible waypoint13 waypoint10)
	(visible waypoint10 waypoint13)
	(visible waypoint13 waypoint14)
	(visible waypoint14 waypoint13)
	(visible waypoint13 waypoint20)
	(visible waypoint20 waypoint13)
	(visible waypoint14 waypoint0)
	(visible waypoint0 waypoint14)
	(visible waypoint14 waypoint1)
	(visible waypoint1 waypoint14)
	(visible waypoint14 waypoint16)
	(visible waypoint16 waypoint14)
	(visible waypoint14 waypoint22)
	(visible waypoint22 waypoint14)
	(visible waypoint14 waypoint23)
	(visible waypoint23 waypoint14)
	(visible waypoint15 waypoint16)
	(visible waypoint16 waypoint15)
	(visible waypoint15 waypoint19)
	(visible waypoint19 waypoint15)
	(visible waypoint15 waypoint24)
	(visible waypoint24 waypoint15)
	(visible waypoint16 waypoint2)
	(visible waypoint2 waypoint16)
	(visible waypoint16 waypoint4)
	(visible waypoint4 waypoint16)
	(visible waypoint16 waypoint17)
	(visible waypoint17 waypoint16)
	(visible waypoint17 waypoint19)
	(visible waypoint19 waypoint17)
	(visible waypoint17 waypoint23)
	(visible waypoint23 waypoint17)
	(visible waypoint18 waypoint0)
	(visible waypoint0 waypoint18)
	(visible waypoint18 waypoint11)
	(visible waypoint11 waypoint18)
	(visible waypoint18 waypoint21)
	(visible waypoint21 waypoint18)
	(visible waypoint19 waypoint2)
	(visible waypoint2 waypoint19)
	(visible waypoint19 waypoint9)
	(visible waypoint9 waypoint19)
	(visible waypoint19 waypoint20)
	(visible waypoint20 waypoint19)
	(visible waypoint19 waypoint22)
	(visible waypoint22 waypoint19)
	(visible waypoint19 waypoint23)
	(visible waypoint23 waypoint19)
	(visible waypoint20 waypoint3)
	(visible waypoint3 waypoint20)
	(visible waypoint20 waypoint14)
	(visible waypoint14 waypoint20)
	(visible waypoint20 waypoint15)
	(visible waypoint15 waypoint20)
	(visible waypoint20 waypoint21)
	(visible waypoint21 waypoint20)
	(visible waypoint20 waypoint24)
	(visible waypoint24 waypoint20)
	(visible waypoint21 waypoint2)
	(visible waypoint2 waypoint21)
	(visible waypoint21 waypoint8)
	(visible waypoint8 waypoint21)
	(visible waypoint21 waypoint9)
	(visible waypoint9 waypoint21)
	(visible waypoint21 waypoint13)
	(visible waypoint13 waypoint21)
	(visible waypoint21 waypoint22)
	(visible waypoint22 waypoint21)
	(visible waypoint22 waypoint1)
	(visible waypoint1 waypoint22)
	(visible waypoint22 waypoint9)
	(visible waypoint9 waypoint22)
	(visible waypoint22 waypoint13)
	(visible waypoint13 waypoint22)
	(visible waypoint22 waypoint15)
	(visible waypoint15 waypoint22)
	(visible waypoint22 waypoint16)
	(visible waypoint16 waypoint22)
	(visible waypoint23 waypoint3)
	(visible waypoint3 waypoint23)
	(visible waypoint23 waypoint13)
	(visible waypoint13 waypoint23)
	(visible waypoint23 waypoint20)
	(visible waypoint20 waypoint23)
	(visible waypoint24 waypoint6)
	(visible waypoint6 waypoint24)
	(visible waypoint24 waypoint7)
	(visible waypoint7 waypoint24)
	(visible waypoint24 waypoint9)
	(visible waypoint9 waypoint24)
	(visible waypoint24 waypoint12)
	(visible waypoint12 waypoint24)
	(visible waypoint24 waypoint13)
	(visible waypoint13 waypoint24)
	(visible waypoint24 waypoint17)
	(visible waypoint17 waypoint24)
	(visible waypoint24 waypoint18)
	(visible waypoint18 waypoint24)
	(visible waypoint24 waypoint19)
	(visible waypoint19 waypoint24)
	(visible waypoint24 waypoint23)
	(visible waypoint23 waypoint24)
	(at_soil_sample waypoint1)
	(at_rock_sample waypoint1)
	(at_soil_sample waypoint2)
	(at_rock_sample waypoint2)
	(at_soil_sample waypoint3)
	(at_rock_sample waypoint3)
	(at_soil_sample waypoint4)
	(at_rock_sample waypoint4)
	(at_rock_sample waypoint5)
	(at_soil_sample waypoint6)
	(at_rock_sample waypoint6)
	(at_rock_sample waypoint7)
	(at_rock_sample waypoint9)
	(at_soil_sample waypoint10)
	(at_rock_sample waypoint10)
	(at_soil_sample waypoint11)
	(at_rock_sample waypoint11)
	(at_rock_sample waypoint12)
	(at_rock_sample waypoint13)
	(at_soil_sample waypoint14)
	(at_rock_sample waypoint14)
	(at_soil_sample waypoint15)
	(at_rock_sample waypoint15)
	(at_soil_sample waypoint16)
	(at_soil_sample waypoint17)
	(at_rock_sample waypoint19)
	(at_soil_sample waypoint20)
	(at_soil_sample waypoint21)
	(at_soil_sample waypoint22)
	(at_soil_sample waypoint23)
	(at_soil_sample waypoint24)
	(at_rock_sample waypoint24)
	(at_lander general waypoint9)
	(channel_free general)
	(empty rover0store)
	(empty rover1store)
	(empty rover2store)
	(empty rover3store)
	(empty rover4store)
	(at rover5 waypoint20)
	(available rover5)
	(store_of rover5store rover5)
	(empty rover5store)
	(equipped_for_rock_analysis rover5)
	(equipped_for_imaging rover5)
	(can_traverse rover5 waypoint20 waypoint2)
	(can_traverse rover5 waypoint2 waypoint20)
	(can_traverse rover5 waypoint20 waypoint3)
	(can_traverse rover5 waypoint3 waypoint20)
	(can_traverse rover5 waypoint20 waypoint12)
	(can_traverse rover5 waypoint12 waypoint20)
	(can_traverse rover5 waypoint20 waypoint13)
	(can_traverse rover5 waypoint13 waypoint20)
	(can_traverse rover5 waypoint20 waypoint14)
	(can_traverse rover5 waypoint14 waypoint20)
	(can_traverse rover5 waypoint20 waypoint15)
	(can_traverse rover5 waypoint15 waypoint20)
	(can_traverse rover5 waypoint20 waypoint21)
	(can_traverse rover5 waypoint21 waypoint20)
	(can_traverse rover5 waypoint20 waypoint23)
	(can_traverse rover5 waypoint23 waypoint20)
	(can_traverse rover5 waypoint2 waypoint0)
	(can_traverse rover5 waypoint0 waypoint2)
	(can_traverse rover5 waypoint2 waypoint9)
	(can_traverse rover5 waypoint9 waypoint2)
	(can_traverse rover5 waypoint2 waypoint16)
	(can_traverse rover5 waypoint16 waypoint2)
	(can_traverse rover5 waypoint3 waypoint1)
	(can_traverse rover5 waypoint1 waypoint3)
	(can_traverse rover5 waypoint3 waypoint6)
	(can_traverse rover5 waypoint6 waypoint3)
	(can_traverse rover5 waypoint12 waypoint10)
	(can_traverse rover5 waypoint10 waypoint12)
	(can_traverse rover5 waypoint12 waypoint18)
	(can_traverse rover5 waypoint18 waypoint12)
	(can_traverse rover5 waypoint12 waypoint24)
	(can_traverse rover5 waypoint24 waypoint12)
	(can_traverse rover5 waypoint13 waypoint4)
	(can_traverse rover5 waypoint4 waypoint13)
	(can_traverse rover5 waypoint13 waypoint7)
	(can_traverse rover5 waypoint7 waypoint13)
	(can_traverse rover5 waypoint13 waypoint22)
	(can_traverse rover5 waypoint22 waypoint13)
	(can_traverse rover5 waypoint15 waypoint11)
	(can_traverse rover5 waypoint11 waypoint15)
	(can_traverse rover5 waypoint23 waypoint17)
	(can_traverse rover5 waypoint17 waypoint23)
	(can_traverse rover5 waypoint23 waypoint19)
	(can_traverse rover5 waypoint19 waypoint23)
	(can_traverse rover5 waypoint9 waypoint8)
	(can_traverse rover5 waypoint8 waypoint9)
	(can_traverse rover5 waypoint16 waypoint5)
	(can_traverse rover5 waypoint5 waypoint16)
	(empty rover6store)
	(empty rover7store)
	(empty rover8store)
	(empty rover9store)
	(calibration_target camera0 objective2)
	(supports camera0 colour)
	(supports camera0 low_res)
	(on_board camera1 rover5)
	(calibration_target camera1 objective2)
	(supports camera1 colour)
	(supports camera1 high_res)
	(calibration_target camera2 objective3)
	(supports camera2 low_res)
	(calibration_target camera3 objective5)
	(supports camera3 colour)
	(supports camera3 high_res)
	(calibration_target camera4 objective0)
	(supports camera4 colour)
	(supports camera4 high_res)
	(supports camera4 low_res)
	(calibration_target camera5 objective2)
	(supports camera5 colour)
	(supports camera5 low_res)
	(calibration_target camera6 objective1)
	(supports camera6 colour)
	(supports camera6 low_res)
	(calibration_target camera7 objective2)
	(supports camera7 colour)
	(supports camera7 high_res)
	(calibration_target camera8 objective1)
	(supports camera8 colour)
	(calibration_target camera9 objective2)
	(supports camera9 colour)
	(visible_from objective0 waypoint0)
	(visible_from objective0 waypoint1)
	(visible_from objective0 waypoint2)
	(visible_from objective0 waypoint3)
	(visible_from objective0 waypoint4)
	(visible_from objective0 waypoint5)
	(visible_from objective0 waypoint6)
	(visible_from objective0 waypoint7)
	(visible_from objective0 waypoint8)
	(visible_from objective0 waypoint9)
	(visible_from objective0 waypoint10)
	(visible_from objective0 waypoint11)
	(visible_from objective0 waypoint12)
	(visible_from objective0 waypoint13)
	(visible_from objective0 waypoint14)
	(visible_from objective0 waypoint15)
	(visible_from objective0 waypoint16)
	(visible_from objective0 waypoint17)
	(visible_from objective0 waypoint18)
	(visible_from objective0 waypoint19)
	(visible_from objective0 waypoint20)
	(visible_from objective0 waypoint21)
	(visible_from objective0 waypoint22)
	(visible_from objective0 waypoint23)
	(visible_from objective0 waypoint24)
	(visible_from objective1 waypoint0)
	(visible_from objective1 waypoint1)
	(visible_from objective1 waypoint2)
	(visible_from objective1 waypoint3)
	(visible_from objective1 waypoint4)
	(visible_from objective1 waypoint5)
	(visible_from objective1 waypoint6)
	(visible_from objective1 waypoint7)
	(visible_from objective1 waypoint8)
	(visible_from objective1 waypoint9)
	(visible_from objective1 waypoint10)
	(visible_from objective1 waypoint11)
	(visible_from objective1 waypoint12)
	(visible_from objective1 waypoint13)
	(visible_from objective1 waypoint14)
	(visible_from objective1 waypoint15)
	(visible_from objective1 waypoint16)
	(visible_from objective1 waypoint17)
	(visible_from objective1 waypoint18)
	(visible_from objective1 waypoint19)
	(visible_from objective1 waypoint20)
	(visible_from objective1 waypoint21)
	(visible_from objective1 waypoint22)
	(visible_from objective1 waypoint23)
	(visible_from objective1 waypoint24)
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
	(visible_from objective2 waypoint15)
	(visible_from objective2 waypoint16)
	(visible_from objective2 waypoint17)
	(visible_from objective2 waypoint18)
	(visible_from objective2 waypoint19)
	(visible_from objective2 waypoint20)
	(visible_from objective2 waypoint21)
	(visible_from objective3 waypoint0)
	(visible_from objective3 waypoint1)
	(visible_from objective3 waypoint2)
	(visible_from objective3 waypoint3)
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
	(visible_from objective4 waypoint13)
	(visible_from objective4 waypoint14)
	(visible_from objective4 waypoint15)
	(visible_from objective4 waypoint16)
	(visible_from objective4 waypoint17)
	(visible_from objective5 waypoint0)
	(visible_from objective5 waypoint1)
	(visible_from objective5 waypoint2)
	(visible_from objective5 waypoint3)
	(visible_from objective5 waypoint4)
	(visible_from objective5 waypoint5)
	(visible_from objective5 waypoint6)
	(visible_from objective5 waypoint7)
	(visible_from objective5 waypoint8)
	(visible_from objective5 waypoint9)
	(visible_from objective5 waypoint10)
	(visible_from objective5 waypoint11)
)
(:goal
	(and
		(communicated_soil_data waypoint14)
		(communicated_soil_data waypoint21)
		(communicated_soil_data waypoint16)
		(communicated_rock_data waypoint9)
		(communicated_rock_data waypoint2)
		(communicated_rock_data waypoint10)
		(communicated_rock_data waypoint7)
		(communicated_image_data objective1 colour)
		(communicated_image_data objective1 high_res)
		(communicated_image_data objective2 colour)
	)
)
)