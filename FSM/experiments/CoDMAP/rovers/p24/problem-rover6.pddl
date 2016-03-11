(define (problem roverprob1234) (:domain rover)
(:objects
	rover7store - store
	rover3store - store
	rover4store - store
	high_res - mode
	waypoint27 - waypoint
	camera8 - camera
	low_res - mode
	camera0 - camera
	rover5store - store
	objective1 - objective
	objective0 - objective
	objective3 - objective
	objective2 - objective
	objective5 - objective
	objective4 - objective
	objective7 - objective
	objective6 - objective
	waypoint36 - waypoint
	waypoint37 - waypoint
	waypoint34 - waypoint
	waypoint35 - waypoint
	waypoint32 - waypoint
	waypoint33 - waypoint
	waypoint30 - waypoint
	waypoint31 - waypoint
	waypoint12 - waypoint
	waypoint38 - waypoint
	waypoint39 - waypoint
	camera1 - camera
	camera3 - camera
	camera2 - camera
	rover0store - store
	general - lander
	camera6 - camera
	camera7 - camera
	rover6store - store
	waypoint25 - waypoint
	waypoint24 - waypoint
	rover2store - store
	waypoint26 - waypoint
	waypoint21 - waypoint
	waypoint20 - waypoint
	waypoint23 - waypoint
	waypoint22 - waypoint
	waypoint29 - waypoint
	waypoint28 - waypoint
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
	waypoint18 - waypoint
	waypoint19 - waypoint
	camera5 - camera
	waypoint10 - waypoint
	waypoint11 - waypoint
	colour - mode
	waypoint13 - waypoint
	waypoint14 - waypoint
	waypoint15 - waypoint
	waypoint16 - waypoint
	waypoint17 - waypoint
	rover1store - store

	(:private
		rover6 - rover
	)
)
(:init
	(visible waypoint0 waypoint12)
	(visible waypoint12 waypoint0)
	(visible waypoint0 waypoint15)
	(visible waypoint15 waypoint0)
	(visible waypoint0 waypoint16)
	(visible waypoint16 waypoint0)
	(visible waypoint0 waypoint22)
	(visible waypoint22 waypoint0)
	(visible waypoint0 waypoint27)
	(visible waypoint27 waypoint0)
	(visible waypoint0 waypoint34)
	(visible waypoint34 waypoint0)
	(visible waypoint1 waypoint21)
	(visible waypoint21 waypoint1)
	(visible waypoint1 waypoint36)
	(visible waypoint36 waypoint1)
	(visible waypoint2 waypoint1)
	(visible waypoint1 waypoint2)
	(visible waypoint2 waypoint6)
	(visible waypoint6 waypoint2)
	(visible waypoint2 waypoint28)
	(visible waypoint28 waypoint2)
	(visible waypoint2 waypoint29)
	(visible waypoint29 waypoint2)
	(visible waypoint3 waypoint8)
	(visible waypoint8 waypoint3)
	(visible waypoint3 waypoint15)
	(visible waypoint15 waypoint3)
	(visible waypoint3 waypoint19)
	(visible waypoint19 waypoint3)
	(visible waypoint3 waypoint23)
	(visible waypoint23 waypoint3)
	(visible waypoint3 waypoint24)
	(visible waypoint24 waypoint3)
	(visible waypoint4 waypoint5)
	(visible waypoint5 waypoint4)
	(visible waypoint4 waypoint9)
	(visible waypoint9 waypoint4)
	(visible waypoint4 waypoint10)
	(visible waypoint10 waypoint4)
	(visible waypoint4 waypoint11)
	(visible waypoint11 waypoint4)
	(visible waypoint4 waypoint13)
	(visible waypoint13 waypoint4)
	(visible waypoint4 waypoint19)
	(visible waypoint19 waypoint4)
	(visible waypoint4 waypoint30)
	(visible waypoint30 waypoint4)
	(visible waypoint5 waypoint26)
	(visible waypoint26 waypoint5)
	(visible waypoint5 waypoint39)
	(visible waypoint39 waypoint5)
	(visible waypoint6 waypoint10)
	(visible waypoint10 waypoint6)
	(visible waypoint6 waypoint12)
	(visible waypoint12 waypoint6)
	(visible waypoint6 waypoint29)
	(visible waypoint29 waypoint6)
	(visible waypoint7 waypoint4)
	(visible waypoint4 waypoint7)
	(visible waypoint7 waypoint8)
	(visible waypoint8 waypoint7)
	(visible waypoint7 waypoint10)
	(visible waypoint10 waypoint7)
	(visible waypoint7 waypoint11)
	(visible waypoint11 waypoint7)
	(visible waypoint7 waypoint13)
	(visible waypoint13 waypoint7)
	(visible waypoint7 waypoint14)
	(visible waypoint14 waypoint7)
	(visible waypoint8 waypoint4)
	(visible waypoint4 waypoint8)
	(visible waypoint8 waypoint9)
	(visible waypoint9 waypoint8)
	(visible waypoint8 waypoint11)
	(visible waypoint11 waypoint8)
	(visible waypoint8 waypoint13)
	(visible waypoint13 waypoint8)
	(visible waypoint8 waypoint37)
	(visible waypoint37 waypoint8)
	(visible waypoint8 waypoint39)
	(visible waypoint39 waypoint8)
	(visible waypoint9 waypoint2)
	(visible waypoint2 waypoint9)
	(visible waypoint9 waypoint18)
	(visible waypoint18 waypoint9)
	(visible waypoint9 waypoint21)
	(visible waypoint21 waypoint9)
	(visible waypoint9 waypoint25)
	(visible waypoint25 waypoint9)
	(visible waypoint9 waypoint26)
	(visible waypoint26 waypoint9)
	(visible waypoint10 waypoint0)
	(visible waypoint0 waypoint10)
	(visible waypoint12 waypoint11)
	(visible waypoint11 waypoint12)
	(visible waypoint12 waypoint16)
	(visible waypoint16 waypoint12)
	(visible waypoint12 waypoint28)
	(visible waypoint28 waypoint12)
	(visible waypoint12 waypoint31)
	(visible waypoint31 waypoint12)
	(visible waypoint12 waypoint38)
	(visible waypoint38 waypoint12)
	(visible waypoint13 waypoint1)
	(visible waypoint1 waypoint13)
	(visible waypoint13 waypoint6)
	(visible waypoint6 waypoint13)
	(visible waypoint13 waypoint9)
	(visible waypoint9 waypoint13)
	(visible waypoint13 waypoint15)
	(visible waypoint15 waypoint13)
	(visible waypoint13 waypoint33)
	(visible waypoint33 waypoint13)
	(visible waypoint14 waypoint2)
	(visible waypoint2 waypoint14)
	(visible waypoint14 waypoint12)
	(visible waypoint12 waypoint14)
	(visible waypoint15 waypoint11)
	(visible waypoint11 waypoint15)
	(visible waypoint15 waypoint14)
	(visible waypoint14 waypoint15)
	(visible waypoint15 waypoint25)
	(visible waypoint25 waypoint15)
	(visible waypoint16 waypoint1)
	(visible waypoint1 waypoint16)
	(visible waypoint16 waypoint7)
	(visible waypoint7 waypoint16)
	(visible waypoint16 waypoint19)
	(visible waypoint19 waypoint16)
	(visible waypoint16 waypoint24)
	(visible waypoint24 waypoint16)
	(visible waypoint16 waypoint35)
	(visible waypoint35 waypoint16)
	(visible waypoint17 waypoint5)
	(visible waypoint5 waypoint17)
	(visible waypoint17 waypoint6)
	(visible waypoint6 waypoint17)
	(visible waypoint17 waypoint16)
	(visible waypoint16 waypoint17)
	(visible waypoint17 waypoint19)
	(visible waypoint19 waypoint17)
	(visible waypoint17 waypoint20)
	(visible waypoint20 waypoint17)
	(visible waypoint18 waypoint1)
	(visible waypoint1 waypoint18)
	(visible waypoint18 waypoint4)
	(visible waypoint4 waypoint18)
	(visible waypoint18 waypoint11)
	(visible waypoint11 waypoint18)
	(visible waypoint18 waypoint23)
	(visible waypoint23 waypoint18)
	(visible waypoint18 waypoint30)
	(visible waypoint30 waypoint18)
	(visible waypoint18 waypoint32)
	(visible waypoint32 waypoint18)
	(visible waypoint19 waypoint1)
	(visible waypoint1 waypoint19)
	(visible waypoint19 waypoint6)
	(visible waypoint6 waypoint19)
	(visible waypoint19 waypoint11)
	(visible waypoint11 waypoint19)
	(visible waypoint19 waypoint12)
	(visible waypoint12 waypoint19)
	(visible waypoint19 waypoint15)
	(visible waypoint15 waypoint19)
	(visible waypoint19 waypoint28)
	(visible waypoint28 waypoint19)
	(visible waypoint19 waypoint37)
	(visible waypoint37 waypoint19)
	(visible waypoint20 waypoint1)
	(visible waypoint1 waypoint20)
	(visible waypoint20 waypoint6)
	(visible waypoint6 waypoint20)
	(visible waypoint20 waypoint11)
	(visible waypoint11 waypoint20)
	(visible waypoint20 waypoint23)
	(visible waypoint23 waypoint20)
	(visible waypoint20 waypoint31)
	(visible waypoint31 waypoint20)
	(visible waypoint21 waypoint0)
	(visible waypoint0 waypoint21)
	(visible waypoint21 waypoint11)
	(visible waypoint11 waypoint21)
	(visible waypoint21 waypoint30)
	(visible waypoint30 waypoint21)
	(visible waypoint21 waypoint33)
	(visible waypoint33 waypoint21)
	(visible waypoint22 waypoint14)
	(visible waypoint14 waypoint22)
	(visible waypoint22 waypoint18)
	(visible waypoint18 waypoint22)
	(visible waypoint22 waypoint21)
	(visible waypoint21 waypoint22)
	(visible waypoint22 waypoint29)
	(visible waypoint29 waypoint22)
	(visible waypoint23 waypoint0)
	(visible waypoint0 waypoint23)
	(visible waypoint23 waypoint2)
	(visible waypoint2 waypoint23)
	(visible waypoint23 waypoint13)
	(visible waypoint13 waypoint23)
	(visible waypoint23 waypoint15)
	(visible waypoint15 waypoint23)
	(visible waypoint23 waypoint24)
	(visible waypoint24 waypoint23)
	(visible waypoint23 waypoint26)
	(visible waypoint26 waypoint23)
	(visible waypoint23 waypoint39)
	(visible waypoint39 waypoint23)
	(visible waypoint24 waypoint1)
	(visible waypoint1 waypoint24)
	(visible waypoint24 waypoint6)
	(visible waypoint6 waypoint24)
	(visible waypoint24 waypoint14)
	(visible waypoint14 waypoint24)
	(visible waypoint24 waypoint19)
	(visible waypoint19 waypoint24)
	(visible waypoint24 waypoint28)
	(visible waypoint28 waypoint24)
	(visible waypoint24 waypoint35)
	(visible waypoint35 waypoint24)
	(visible waypoint24 waypoint38)
	(visible waypoint38 waypoint24)
	(visible waypoint25 waypoint0)
	(visible waypoint0 waypoint25)
	(visible waypoint25 waypoint5)
	(visible waypoint5 waypoint25)
	(visible waypoint25 waypoint10)
	(visible waypoint10 waypoint25)
	(visible waypoint25 waypoint14)
	(visible waypoint14 waypoint25)
	(visible waypoint25 waypoint34)
	(visible waypoint34 waypoint25)
	(visible waypoint25 waypoint37)
	(visible waypoint37 waypoint25)
	(visible waypoint26 waypoint2)
	(visible waypoint2 waypoint26)
	(visible waypoint26 waypoint4)
	(visible waypoint4 waypoint26)
	(visible waypoint26 waypoint27)
	(visible waypoint27 waypoint26)
	(visible waypoint26 waypoint34)
	(visible waypoint34 waypoint26)
	(visible waypoint26 waypoint35)
	(visible waypoint35 waypoint26)
	(visible waypoint27 waypoint11)
	(visible waypoint11 waypoint27)
	(visible waypoint27 waypoint19)
	(visible waypoint19 waypoint27)
	(visible waypoint27 waypoint20)
	(visible waypoint20 waypoint27)
	(visible waypoint27 waypoint29)
	(visible waypoint29 waypoint27)
	(visible waypoint27 waypoint35)
	(visible waypoint35 waypoint27)
	(visible waypoint27 waypoint37)
	(visible waypoint37 waypoint27)
	(visible waypoint28 waypoint6)
	(visible waypoint6 waypoint28)
	(visible waypoint28 waypoint26)
	(visible waypoint26 waypoint28)
	(visible waypoint28 waypoint29)
	(visible waypoint29 waypoint28)
	(visible waypoint29 waypoint1)
	(visible waypoint1 waypoint29)
	(visible waypoint29 waypoint9)
	(visible waypoint9 waypoint29)
	(visible waypoint29 waypoint13)
	(visible waypoint13 waypoint29)
	(visible waypoint29 waypoint25)
	(visible waypoint25 waypoint29)
	(visible waypoint30 waypoint6)
	(visible waypoint6 waypoint30)
	(visible waypoint30 waypoint7)
	(visible waypoint7 waypoint30)
	(visible waypoint30 waypoint19)
	(visible waypoint19 waypoint30)
	(visible waypoint30 waypoint25)
	(visible waypoint25 waypoint30)
	(visible waypoint30 waypoint37)
	(visible waypoint37 waypoint30)
	(visible waypoint31 waypoint10)
	(visible waypoint10 waypoint31)
	(visible waypoint31 waypoint11)
	(visible waypoint11 waypoint31)
	(visible waypoint31 waypoint15)
	(visible waypoint15 waypoint31)
	(visible waypoint31 waypoint16)
	(visible waypoint16 waypoint31)
	(visible waypoint31 waypoint19)
	(visible waypoint19 waypoint31)
	(visible waypoint31 waypoint25)
	(visible waypoint25 waypoint31)
	(visible waypoint31 waypoint37)
	(visible waypoint37 waypoint31)
	(visible waypoint31 waypoint38)
	(visible waypoint38 waypoint31)
	(visible waypoint32 waypoint5)
	(visible waypoint5 waypoint32)
	(visible waypoint32 waypoint24)
	(visible waypoint24 waypoint32)
	(visible waypoint32 waypoint26)
	(visible waypoint26 waypoint32)
	(visible waypoint32 waypoint29)
	(visible waypoint29 waypoint32)
	(visible waypoint33 waypoint0)
	(visible waypoint0 waypoint33)
	(visible waypoint33 waypoint11)
	(visible waypoint11 waypoint33)
	(visible waypoint33 waypoint23)
	(visible waypoint23 waypoint33)
	(visible waypoint33 waypoint27)
	(visible waypoint27 waypoint33)
	(visible waypoint33 waypoint35)
	(visible waypoint35 waypoint33)
	(visible waypoint33 waypoint38)
	(visible waypoint38 waypoint33)
	(visible waypoint33 waypoint39)
	(visible waypoint39 waypoint33)
	(visible waypoint34 waypoint13)
	(visible waypoint13 waypoint34)
	(visible waypoint34 waypoint17)
	(visible waypoint17 waypoint34)
	(visible waypoint34 waypoint24)
	(visible waypoint24 waypoint34)
	(visible waypoint34 waypoint38)
	(visible waypoint38 waypoint34)
	(visible waypoint35 waypoint8)
	(visible waypoint8 waypoint35)
	(visible waypoint35 waypoint29)
	(visible waypoint29 waypoint35)
	(visible waypoint36 waypoint5)
	(visible waypoint5 waypoint36)
	(visible waypoint36 waypoint35)
	(visible waypoint35 waypoint36)
	(visible waypoint37 waypoint4)
	(visible waypoint4 waypoint37)
	(visible waypoint37 waypoint16)
	(visible waypoint16 waypoint37)
	(visible waypoint37 waypoint24)
	(visible waypoint24 waypoint37)
	(visible waypoint37 waypoint35)
	(visible waypoint35 waypoint37)
	(visible waypoint38 waypoint4)
	(visible waypoint4 waypoint38)
	(visible waypoint38 waypoint6)
	(visible waypoint6 waypoint38)
	(visible waypoint38 waypoint8)
	(visible waypoint8 waypoint38)
	(visible waypoint38 waypoint20)
	(visible waypoint20 waypoint38)
	(visible waypoint38 waypoint29)
	(visible waypoint29 waypoint38)
	(visible waypoint39 waypoint3)
	(visible waypoint3 waypoint39)
	(visible waypoint39 waypoint7)
	(visible waypoint7 waypoint39)
	(visible waypoint39 waypoint35)
	(visible waypoint35 waypoint39)
	(at_rock_sample waypoint0)
	(at_soil_sample waypoint1)
	(at_soil_sample waypoint2)
	(at_soil_sample waypoint3)
	(at_rock_sample waypoint3)
	(at_rock_sample waypoint4)
	(at_soil_sample waypoint5)
	(at_soil_sample waypoint7)
	(at_rock_sample waypoint7)
	(at_soil_sample waypoint8)
	(at_soil_sample waypoint10)
	(at_soil_sample waypoint11)
	(at_rock_sample waypoint11)
	(at_soil_sample waypoint12)
	(at_rock_sample waypoint12)
	(at_soil_sample waypoint13)
	(at_soil_sample waypoint14)
	(at_rock_sample waypoint15)
	(at_soil_sample waypoint18)
	(at_rock_sample waypoint18)
	(at_soil_sample waypoint20)
	(at_rock_sample waypoint20)
	(at_soil_sample waypoint21)
	(at_rock_sample waypoint21)
	(at_soil_sample waypoint22)
	(at_soil_sample waypoint24)
	(at_soil_sample waypoint26)
	(at_soil_sample waypoint27)
	(at_soil_sample waypoint30)
	(at_rock_sample waypoint31)
	(at_rock_sample waypoint32)
	(at_rock_sample waypoint33)
	(at_soil_sample waypoint34)
	(at_rock_sample waypoint34)
	(at_soil_sample waypoint36)
	(at_rock_sample waypoint36)
	(at_rock_sample waypoint37)
	(at_rock_sample waypoint38)
	(at_soil_sample waypoint39)
	(at_lander general waypoint3)
	(channel_free general)
	(empty rover0store)
	(empty rover1store)
	(empty rover2store)
	(empty rover3store)
	(empty rover4store)
	(empty rover5store)
	(at rover6 waypoint26)
	(available rover6)
	(store_of rover6store rover6)
	(empty rover6store)
	(equipped_for_rock_analysis rover6)
	(equipped_for_imaging rover6)
	(can_traverse rover6 waypoint26 waypoint2)
	(can_traverse rover6 waypoint2 waypoint26)
	(can_traverse rover6 waypoint26 waypoint4)
	(can_traverse rover6 waypoint4 waypoint26)
	(can_traverse rover6 waypoint26 waypoint5)
	(can_traverse rover6 waypoint5 waypoint26)
	(can_traverse rover6 waypoint26 waypoint23)
	(can_traverse rover6 waypoint23 waypoint26)
	(can_traverse rover6 waypoint26 waypoint27)
	(can_traverse rover6 waypoint27 waypoint26)
	(can_traverse rover6 waypoint26 waypoint35)
	(can_traverse rover6 waypoint35 waypoint26)
	(can_traverse rover6 waypoint2 waypoint6)
	(can_traverse rover6 waypoint6 waypoint2)
	(can_traverse rover6 waypoint2 waypoint14)
	(can_traverse rover6 waypoint14 waypoint2)
	(can_traverse rover6 waypoint2 waypoint28)
	(can_traverse rover6 waypoint28 waypoint2)
	(can_traverse rover6 waypoint2 waypoint29)
	(can_traverse rover6 waypoint29 waypoint2)
	(can_traverse rover6 waypoint4 waypoint8)
	(can_traverse rover6 waypoint8 waypoint4)
	(can_traverse rover6 waypoint4 waypoint9)
	(can_traverse rover6 waypoint9 waypoint4)
	(can_traverse rover6 waypoint4 waypoint10)
	(can_traverse rover6 waypoint10 waypoint4)
	(can_traverse rover6 waypoint4 waypoint11)
	(can_traverse rover6 waypoint11 waypoint4)
	(can_traverse rover6 waypoint4 waypoint13)
	(can_traverse rover6 waypoint13 waypoint4)
	(can_traverse rover6 waypoint4 waypoint18)
	(can_traverse rover6 waypoint18 waypoint4)
	(can_traverse rover6 waypoint4 waypoint37)
	(can_traverse rover6 waypoint37 waypoint4)
	(can_traverse rover6 waypoint4 waypoint38)
	(can_traverse rover6 waypoint38 waypoint4)
	(can_traverse rover6 waypoint5 waypoint17)
	(can_traverse rover6 waypoint17 waypoint5)
	(can_traverse rover6 waypoint5 waypoint25)
	(can_traverse rover6 waypoint25 waypoint5)
	(can_traverse rover6 waypoint5 waypoint39)
	(can_traverse rover6 waypoint39 waypoint5)
	(can_traverse rover6 waypoint23 waypoint0)
	(can_traverse rover6 waypoint0 waypoint23)
	(can_traverse rover6 waypoint23 waypoint15)
	(can_traverse rover6 waypoint15 waypoint23)
	(can_traverse rover6 waypoint23 waypoint20)
	(can_traverse rover6 waypoint20 waypoint23)
	(can_traverse rover6 waypoint23 waypoint24)
	(can_traverse rover6 waypoint24 waypoint23)
	(can_traverse rover6 waypoint23 waypoint33)
	(can_traverse rover6 waypoint33 waypoint23)
	(can_traverse rover6 waypoint6 waypoint19)
	(can_traverse rover6 waypoint19 waypoint6)
	(can_traverse rover6 waypoint6 waypoint30)
	(can_traverse rover6 waypoint30 waypoint6)
	(can_traverse rover6 waypoint14 waypoint7)
	(can_traverse rover6 waypoint7 waypoint14)
	(can_traverse rover6 waypoint14 waypoint12)
	(can_traverse rover6 waypoint12 waypoint14)
	(can_traverse rover6 waypoint14 waypoint22)
	(can_traverse rover6 waypoint22 waypoint14)
	(can_traverse rover6 waypoint29 waypoint1)
	(can_traverse rover6 waypoint1 waypoint29)
	(can_traverse rover6 waypoint8 waypoint3)
	(can_traverse rover6 waypoint3 waypoint8)
	(can_traverse rover6 waypoint9 waypoint21)
	(can_traverse rover6 waypoint21 waypoint9)
	(can_traverse rover6 waypoint10 waypoint31)
	(can_traverse rover6 waypoint31 waypoint10)
	(can_traverse rover6 waypoint13 waypoint34)
	(can_traverse rover6 waypoint34 waypoint13)
	(can_traverse rover6 waypoint18 waypoint32)
	(can_traverse rover6 waypoint32 waypoint18)
	(can_traverse rover6 waypoint0 waypoint16)
	(can_traverse rover6 waypoint16 waypoint0)
	(empty rover7store)
	(calibration_target camera0 objective7)
	(supports camera0 colour)
	(calibration_target camera1 objective2)
	(supports camera1 colour)
	(supports camera1 high_res)
	(supports camera1 low_res)
	(on_board camera2 rover6)
	(calibration_target camera2 objective6)
	(supports camera2 colour)
	(supports camera2 low_res)
	(calibration_target camera3 objective0)
	(supports camera3 colour)
	(calibration_target camera4 objective6)
	(supports camera4 colour)
	(calibration_target camera5 objective4)
	(supports camera5 colour)
	(supports camera5 low_res)
	(calibration_target camera6 objective1)
	(supports camera6 colour)
	(calibration_target camera7 objective7)
	(supports camera7 low_res)
	(calibration_target camera8 objective2)
	(supports camera8 colour)
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
	(visible_from objective0 waypoint25)
	(visible_from objective0 waypoint26)
	(visible_from objective0 waypoint27)
	(visible_from objective0 waypoint28)
	(visible_from objective0 waypoint29)
	(visible_from objective0 waypoint30)
	(visible_from objective0 waypoint31)
	(visible_from objective0 waypoint32)
	(visible_from objective0 waypoint33)
	(visible_from objective0 waypoint34)
	(visible_from objective0 waypoint35)
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
	(visible_from objective1 waypoint25)
	(visible_from objective1 waypoint26)
	(visible_from objective1 waypoint27)
	(visible_from objective1 waypoint28)
	(visible_from objective1 waypoint29)
	(visible_from objective1 waypoint30)
	(visible_from objective1 waypoint31)
	(visible_from objective1 waypoint32)
	(visible_from objective1 waypoint33)
	(visible_from objective1 waypoint34)
	(visible_from objective1 waypoint35)
	(visible_from objective1 waypoint36)
	(visible_from objective1 waypoint37)
	(visible_from objective1 waypoint38)
	(visible_from objective1 waypoint39)
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
	(visible_from objective2 waypoint22)
	(visible_from objective2 waypoint23)
	(visible_from objective2 waypoint24)
	(visible_from objective3 waypoint0)
	(visible_from objective3 waypoint1)
	(visible_from objective3 waypoint2)
	(visible_from objective3 waypoint3)
	(visible_from objective3 waypoint4)
	(visible_from objective3 waypoint5)
	(visible_from objective3 waypoint6)
	(visible_from objective3 waypoint7)
	(visible_from objective3 waypoint8)
	(visible_from objective3 waypoint9)
	(visible_from objective3 waypoint10)
	(visible_from objective3 waypoint11)
	(visible_from objective3 waypoint12)
	(visible_from objective3 waypoint13)
	(visible_from objective3 waypoint14)
	(visible_from objective3 waypoint15)
	(visible_from objective3 waypoint16)
	(visible_from objective3 waypoint17)
	(visible_from objective3 waypoint18)
	(visible_from objective3 waypoint19)
	(visible_from objective3 waypoint20)
	(visible_from objective3 waypoint21)
	(visible_from objective3 waypoint22)
	(visible_from objective3 waypoint23)
	(visible_from objective3 waypoint24)
	(visible_from objective3 waypoint25)
	(visible_from objective3 waypoint26)
	(visible_from objective3 waypoint27)
	(visible_from objective3 waypoint28)
	(visible_from objective3 waypoint29)
	(visible_from objective3 waypoint30)
	(visible_from objective3 waypoint31)
	(visible_from objective3 waypoint32)
	(visible_from objective3 waypoint33)
	(visible_from objective3 waypoint34)
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
	(visible_from objective4 waypoint18)
	(visible_from objective4 waypoint19)
	(visible_from objective4 waypoint20)
	(visible_from objective4 waypoint21)
	(visible_from objective4 waypoint22)
	(visible_from objective4 waypoint23)
	(visible_from objective4 waypoint24)
	(visible_from objective4 waypoint25)
	(visible_from objective4 waypoint26)
	(visible_from objective4 waypoint27)
	(visible_from objective4 waypoint28)
	(visible_from objective4 waypoint29)
	(visible_from objective4 waypoint30)
	(visible_from objective4 waypoint31)
	(visible_from objective4 waypoint32)
	(visible_from objective4 waypoint33)
	(visible_from objective4 waypoint34)
	(visible_from objective4 waypoint35)
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
	(visible_from objective5 waypoint12)
	(visible_from objective5 waypoint13)
	(visible_from objective5 waypoint14)
	(visible_from objective6 waypoint0)
	(visible_from objective6 waypoint1)
	(visible_from objective6 waypoint2)
	(visible_from objective6 waypoint3)
	(visible_from objective6 waypoint4)
	(visible_from objective6 waypoint5)
	(visible_from objective6 waypoint6)
	(visible_from objective6 waypoint7)
	(visible_from objective6 waypoint8)
	(visible_from objective6 waypoint9)
	(visible_from objective6 waypoint10)
	(visible_from objective6 waypoint11)
	(visible_from objective6 waypoint12)
	(visible_from objective6 waypoint13)
	(visible_from objective6 waypoint14)
	(visible_from objective6 waypoint15)
	(visible_from objective6 waypoint16)
	(visible_from objective6 waypoint17)
	(visible_from objective6 waypoint18)
	(visible_from objective6 waypoint19)
	(visible_from objective6 waypoint20)
	(visible_from objective6 waypoint21)
	(visible_from objective6 waypoint22)
	(visible_from objective6 waypoint23)
	(visible_from objective6 waypoint24)
	(visible_from objective6 waypoint25)
	(visible_from objective6 waypoint26)
	(visible_from objective6 waypoint27)
	(visible_from objective6 waypoint28)
	(visible_from objective6 waypoint29)
	(visible_from objective6 waypoint30)
	(visible_from objective6 waypoint31)
	(visible_from objective6 waypoint32)
	(visible_from objective6 waypoint33)
	(visible_from objective6 waypoint34)
	(visible_from objective7 waypoint0)
	(visible_from objective7 waypoint1)
	(visible_from objective7 waypoint2)
	(visible_from objective7 waypoint3)
	(visible_from objective7 waypoint4)
	(visible_from objective7 waypoint5)
	(visible_from objective7 waypoint6)
	(visible_from objective7 waypoint7)
	(visible_from objective7 waypoint8)
	(visible_from objective7 waypoint9)
	(visible_from objective7 waypoint10)
	(visible_from objective7 waypoint11)
	(visible_from objective7 waypoint12)
	(visible_from objective7 waypoint13)
	(visible_from objective7 waypoint14)
	(visible_from objective7 waypoint15)
	(visible_from objective7 waypoint16)
	(visible_from objective7 waypoint17)
	(visible_from objective7 waypoint18)
	(visible_from objective7 waypoint19)
	(visible_from objective7 waypoint20)
	(visible_from objective7 waypoint21)
	(visible_from objective7 waypoint22)
	(visible_from objective7 waypoint23)
)
(:goal
	(and
		(communicated_soil_data waypoint10)
		(communicated_soil_data waypoint39)
		(communicated_soil_data waypoint34)
		(communicated_soil_data waypoint11)
		(communicated_soil_data waypoint7)
		(communicated_soil_data waypoint12)
		(communicated_soil_data waypoint14)
		(communicated_soil_data waypoint36)
		(communicated_soil_data waypoint22)
		(communicated_rock_data waypoint4)
		(communicated_rock_data waypoint18)
		(communicated_rock_data waypoint36)
		(communicated_rock_data waypoint34)
		(communicated_rock_data waypoint3)
		(communicated_rock_data waypoint37)
		(communicated_rock_data waypoint20)
		(communicated_rock_data waypoint0)
		(communicated_rock_data waypoint32)
		(communicated_rock_data waypoint21)
		(communicated_image_data objective4 colour)
		(communicated_image_data objective1 low_res)
	)
)
)