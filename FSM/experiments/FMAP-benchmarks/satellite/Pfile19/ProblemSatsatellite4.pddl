(define (problem strips-sat-x-1)
(:domain satellite)
(:objects
 satellite0 satellite1 satellite2 satellite3 satellite4 - satellite
 instrument0 instrument1 instrument2 instrument3 instrument4 instrument5 instrument6 instrument7 instrument8 instrument9 instrument10 instrument11 instrument12 instrument13 instrument14 instrument15 instrument16 instrument17 instrument18 instrument19 instrument20 instrument21 instrument22 instrument23 instrument24 - instrument
 image4 spectrograph6 infrared2 image5 thermograph1 infrared7 spectrograph0 infrared3 - mode
 star1 groundstation4 star2 star3 groundstation0 phenomenon5 star6 phenomenon7 phenomenon8 planet9 star10 phenomenon11 star12 phenomenon13 star14 planet15 star16 planet17 star18 star19 phenomenon20 planet21 planet22 star23 phenomenon24 - direction
)
(:shared-data
  ((pointing ?s - satellite) - direction)
  (have_image ?d - direction ?m - mode) - (either satellite0 satellite1 satellite2 satellite3)
)
(:init (mySatellite satellite4)
 (power_avail satellite4)
 (not (power_on instrument0))
 (not (calibrated instrument0))
 (= (calibration_target instrument0) groundstation0)
 (not (power_on instrument1))
 (not (calibrated instrument1))
 (= (calibration_target instrument1) star3)
 (not (power_on instrument2))
 (not (calibrated instrument2))
 (= (calibration_target instrument2) star1)
 (not (power_on instrument3))
 (not (calibrated instrument3))
 (= (calibration_target instrument3) star1)
 (not (power_on instrument4))
 (not (calibrated instrument4))
 (= (calibration_target instrument4) star3)
 (not (power_on instrument5))
 (not (calibrated instrument5))
 (= (calibration_target instrument5) groundstation4)
 (not (power_on instrument6))
 (not (calibrated instrument6))
 (= (calibration_target instrument6) star3)
 (not (power_on instrument7))
 (not (calibrated instrument7))
 (= (calibration_target instrument7) star2)
 (not (power_on instrument8))
 (not (calibrated instrument8))
 (= (calibration_target instrument8) star1)
 (not (power_on instrument9))
 (not (calibrated instrument9))
 (= (calibration_target instrument9) star3)
 (not (power_on instrument10))
 (not (calibrated instrument10))
 (= (calibration_target instrument10) groundstation4)
 (not (power_on instrument11))
 (not (calibrated instrument11))
 (= (calibration_target instrument11) star2)
 (not (power_on instrument12))
 (not (calibrated instrument12))
 (= (calibration_target instrument12) star3)
 (not (power_on instrument13))
 (not (calibrated instrument13))
 (= (calibration_target instrument13) star2)
 (not (power_on instrument14))
 (not (calibrated instrument14))
 (= (calibration_target instrument14) groundstation4)
 (not (power_on instrument15))
 (not (calibrated instrument15))
 (= (calibration_target instrument15) star1)
 (not (power_on instrument16))
 (not (calibrated instrument16))
 (= (calibration_target instrument16) star2)
 (not (power_on instrument17))
 (not (calibrated instrument17))
 (= (calibration_target instrument17) groundstation0)
 (not (power_on instrument18))
 (not (calibrated instrument18))
 (= (calibration_target instrument18) star2)
 (not (power_on instrument19))
 (not (calibrated instrument19))
 (= (calibration_target instrument19) star3)
 (not (power_on instrument20))
 (not (calibrated instrument20))
 (= (calibration_target instrument20) star2)
 (not (power_on instrument21))
 (not (calibrated instrument21))
 (= (calibration_target instrument21) groundstation4)
 (not (power_on instrument22))
 (not (calibrated instrument22))
 (= (calibration_target instrument22) star2)
 (not (power_on instrument23))
 (not (calibrated instrument23))
 (= (calibration_target instrument23) star3)
 (not (power_on instrument24))
 (not (calibrated instrument24))
 (= (calibration_target instrument24) groundstation0)
 (not (have_image phenomenon5 infrared7))
 (not (have_image phenomenon5 image4))
 (not (have_image phenomenon7 thermograph1))
 (not (have_image planet9 spectrograph0))
 (not (have_image planet9 spectrograph6))
 (not (have_image star10 infrared3))
 (not (have_image star10 spectrograph6))
 (not (have_image phenomenon11 infrared2))
 (not (have_image star12 spectrograph6))
 (not (have_image star12 thermograph1))
 (not (have_image phenomenon13 infrared7))
 (not (have_image phenomenon13 infrared2))
 (not (have_image star14 infrared2))
 (not (have_image planet15 infrared2))
 (not (have_image star16 image4))
 (not (have_image planet17 image5))
 (not (have_image planet17 image4))
 (not (have_image star18 infrared2))
 (not (have_image star19 infrared3))
 (not (have_image star19 thermograph1))
 (not (have_image phenomenon20 spectrograph0))
 (not (have_image planet21 infrared3))
 (not (have_image planet21 image5))
 (not (have_image planet22 infrared2))
 (not (have_image star23 infrared2))
 (not (have_image phenomenon24 spectrograph6))
 (not (have_image phenomenon24 image5))
 (= (pointing satellite4) star14)
 (on_board satellite4 instrument22)
 (on_board satellite4 instrument23)
 (on_board satellite4 instrument24)
 (not (on_board satellite4 instrument0))
 (not (on_board satellite4 instrument1))
 (not (on_board satellite4 instrument2))
 (not (on_board satellite4 instrument3))
 (not (on_board satellite4 instrument4))
 (not (on_board satellite4 instrument5))
 (not (on_board satellite4 instrument6))
 (not (on_board satellite4 instrument7))
 (not (on_board satellite4 instrument8))
 (not (on_board satellite4 instrument9))
 (not (on_board satellite4 instrument10))
 (not (on_board satellite4 instrument11))
 (not (on_board satellite4 instrument12))
 (not (on_board satellite4 instrument13))
 (not (on_board satellite4 instrument14))
 (not (on_board satellite4 instrument15))
 (not (on_board satellite4 instrument16))
 (not (on_board satellite4 instrument17))
 (not (on_board satellite4 instrument18))
 (not (on_board satellite4 instrument19))
 (not (on_board satellite4 instrument20))
 (not (on_board satellite4 instrument21))
 (supports instrument0 image4)
 (supports instrument0 infrared2)
 (not (supports instrument0 spectrograph6))
 (not (supports instrument0 image5))
 (not (supports instrument0 thermograph1))
 (not (supports instrument0 infrared7))
 (not (supports instrument0 spectrograph0))
 (not (supports instrument0 infrared3))
 (supports instrument1 spectrograph0)
 (not (supports instrument1 image4))
 (not (supports instrument1 spectrograph6))
 (not (supports instrument1 infrared2))
 (not (supports instrument1 image5))
 (not (supports instrument1 thermograph1))
 (not (supports instrument1 infrared7))
 (not (supports instrument1 infrared3))
 (supports instrument2 thermograph1)
 (supports instrument2 spectrograph0)
 (supports instrument2 infrared3)
 (not (supports instrument2 image4))
 (not (supports instrument2 spectrograph6))
 (not (supports instrument2 infrared2))
 (not (supports instrument2 image5))
 (not (supports instrument2 infrared7))
 (supports instrument3 image5)
 (supports instrument3 thermograph1)
 (not (supports instrument3 image4))
 (not (supports instrument3 spectrograph6))
 (not (supports instrument3 infrared2))
 (not (supports instrument3 infrared7))
 (not (supports instrument3 spectrograph0))
 (not (supports instrument3 infrared3))
 (supports instrument4 spectrograph0)
 (not (supports instrument4 image4))
 (not (supports instrument4 spectrograph6))
 (not (supports instrument4 infrared2))
 (not (supports instrument4 image5))
 (not (supports instrument4 thermograph1))
 (not (supports instrument4 infrared7))
 (not (supports instrument4 infrared3))
 (supports instrument5 spectrograph6)
 (supports instrument5 thermograph1)
 (supports instrument5 spectrograph0)
 (not (supports instrument5 image4))
 (not (supports instrument5 infrared2))
 (not (supports instrument5 image5))
 (not (supports instrument5 infrared7))
 (not (supports instrument5 infrared3))
 (supports instrument6 image5)
 (supports instrument6 infrared7)
 (not (supports instrument6 image4))
 (not (supports instrument6 spectrograph6))
 (not (supports instrument6 infrared2))
 (not (supports instrument6 thermograph1))
 (not (supports instrument6 spectrograph0))
 (not (supports instrument6 infrared3))
 (supports instrument7 spectrograph6)
 (supports instrument7 thermograph1)
 (supports instrument7 spectrograph0)
 (not (supports instrument7 image4))
 (not (supports instrument7 infrared2))
 (not (supports instrument7 image5))
 (not (supports instrument7 infrared7))
 (not (supports instrument7 infrared3))
 (supports instrument8 infrared7)
 (supports instrument8 infrared3)
 (not (supports instrument8 image4))
 (not (supports instrument8 spectrograph6))
 (not (supports instrument8 infrared2))
 (not (supports instrument8 image5))
 (not (supports instrument8 thermograph1))
 (not (supports instrument8 spectrograph0))
 (supports instrument9 spectrograph0)
 (not (supports instrument9 image4))
 (not (supports instrument9 spectrograph6))
 (not (supports instrument9 infrared2))
 (not (supports instrument9 image5))
 (not (supports instrument9 thermograph1))
 (not (supports instrument9 infrared7))
 (not (supports instrument9 infrared3))
 (supports instrument10 image4)
 (supports instrument10 image5)
 (supports instrument10 infrared7)
 (not (supports instrument10 spectrograph6))
 (not (supports instrument10 infrared2))
 (not (supports instrument10 thermograph1))
 (not (supports instrument10 spectrograph0))
 (not (supports instrument10 infrared3))
 (supports instrument11 infrared2)
 (not (supports instrument11 image4))
 (not (supports instrument11 spectrograph6))
 (not (supports instrument11 image5))
 (not (supports instrument11 thermograph1))
 (not (supports instrument11 infrared7))
 (not (supports instrument11 spectrograph0))
 (not (supports instrument11 infrared3))
 (supports instrument12 thermograph1)
 (not (supports instrument12 image4))
 (not (supports instrument12 spectrograph6))
 (not (supports instrument12 infrared2))
 (not (supports instrument12 image5))
 (not (supports instrument12 infrared7))
 (not (supports instrument12 spectrograph0))
 (not (supports instrument12 infrared3))
 (supports instrument13 infrared3)
 (not (supports instrument13 image4))
 (not (supports instrument13 spectrograph6))
 (not (supports instrument13 infrared2))
 (not (supports instrument13 image5))
 (not (supports instrument13 thermograph1))
 (not (supports instrument13 infrared7))
 (not (supports instrument13 spectrograph0))
 (supports instrument14 infrared2)
 (supports instrument14 thermograph1)
 (not (supports instrument14 image4))
 (not (supports instrument14 spectrograph6))
 (not (supports instrument14 image5))
 (not (supports instrument14 infrared7))
 (not (supports instrument14 spectrograph0))
 (not (supports instrument14 infrared3))
 (supports instrument15 infrared2)
 (not (supports instrument15 image4))
 (not (supports instrument15 spectrograph6))
 (not (supports instrument15 image5))
 (not (supports instrument15 thermograph1))
 (not (supports instrument15 infrared7))
 (not (supports instrument15 spectrograph0))
 (not (supports instrument15 infrared3))
 (supports instrument16 image4)
 (supports instrument16 spectrograph6)
 (not (supports instrument16 infrared2))
 (not (supports instrument16 image5))
 (not (supports instrument16 thermograph1))
 (not (supports instrument16 infrared7))
 (not (supports instrument16 spectrograph0))
 (not (supports instrument16 infrared3))
 (supports instrument17 image4)
 (supports instrument17 image5)
 (supports instrument17 infrared7)
 (not (supports instrument17 spectrograph6))
 (not (supports instrument17 infrared2))
 (not (supports instrument17 thermograph1))
 (not (supports instrument17 spectrograph0))
 (not (supports instrument17 infrared3))
 (supports instrument18 image4)
 (supports instrument18 spectrograph6)
 (not (supports instrument18 infrared2))
 (not (supports instrument18 image5))
 (not (supports instrument18 thermograph1))
 (not (supports instrument18 infrared7))
 (not (supports instrument18 spectrograph0))
 (not (supports instrument18 infrared3))
 (supports instrument19 spectrograph6)
 (supports instrument19 infrared7)
 (supports instrument19 infrared3)
 (not (supports instrument19 image4))
 (not (supports instrument19 infrared2))
 (not (supports instrument19 image5))
 (not (supports instrument19 thermograph1))
 (not (supports instrument19 spectrograph0))
 (supports instrument20 infrared2)
 (supports instrument20 infrared3)
 (not (supports instrument20 image4))
 (not (supports instrument20 spectrograph6))
 (not (supports instrument20 image5))
 (not (supports instrument20 thermograph1))
 (not (supports instrument20 infrared7))
 (not (supports instrument20 spectrograph0))
 (supports instrument21 infrared2)
 (supports instrument21 thermograph1)
 (not (supports instrument21 image4))
 (not (supports instrument21 spectrograph6))
 (not (supports instrument21 image5))
 (not (supports instrument21 infrared7))
 (not (supports instrument21 spectrograph0))
 (not (supports instrument21 infrared3))
 (supports instrument22 image5)
 (supports instrument22 thermograph1)
 (not (supports instrument22 image4))
 (not (supports instrument22 spectrograph6))
 (not (supports instrument22 infrared2))
 (not (supports instrument22 infrared7))
 (not (supports instrument22 spectrograph0))
 (not (supports instrument22 infrared3))
 (supports instrument23 thermograph1)
 (supports instrument23 infrared7)
 (not (supports instrument23 image4))
 (not (supports instrument23 spectrograph6))
 (not (supports instrument23 infrared2))
 (not (supports instrument23 image5))
 (not (supports instrument23 spectrograph0))
 (not (supports instrument23 infrared3))
 (supports instrument24 spectrograph0)
 (supports instrument24 infrared3)
 (not (supports instrument24 image4))
 (not (supports instrument24 spectrograph6))
 (not (supports instrument24 infrared2))
 (not (supports instrument24 image5))
 (not (supports instrument24 thermograph1))
 (not (supports instrument24 infrared7))
)
(:global-goal (and
 (= (pointing satellite0) planet17)
 (have_image phenomenon5 infrared7)
 (have_image phenomenon5 image4)
 (have_image phenomenon7 thermograph1)
 (have_image planet9 spectrograph0)
 (have_image planet9 spectrograph6)
 (have_image star10 infrared3)
 (have_image star10 spectrograph6)
 (have_image phenomenon11 infrared2)
 (have_image star12 spectrograph6)
 (have_image star12 thermograph1)
 (have_image phenomenon13 infrared7)
 (have_image phenomenon13 infrared2)
 (have_image star14 infrared2)
 (have_image planet15 infrared2)
 (have_image star16 image4)
 (have_image planet17 image5)
 (have_image planet17 image4)
 (have_image star18 infrared2)
 (have_image star19 infrared3)
 (have_image star19 thermograph1)
 (have_image phenomenon20 spectrograph0)
 (have_image planet21 infrared3)
 (have_image planet21 image5)
 (have_image planet22 infrared2)
 (have_image star23 infrared2)
 (have_image phenomenon24 spectrograph6)
 (have_image phenomenon24 image5)
))

)