(define (problem strips-sat-x-1)
(:domain satellite)
(:objects
 satellite0 satellite1 satellite2 satellite3 satellite4 satellite5 satellite6 satellite7 satellite8 satellite9 satellite10 satellite11 - satellite
 instrument0 instrument1 instrument2 instrument3 instrument4 instrument5 instrument6 instrument7 instrument8 instrument9 instrument10 instrument11 instrument12 instrument13 instrument14 instrument15 instrument16 instrument17 instrument18 instrument19 instrument20 instrument21 instrument22 instrument23 - instrument
 infrared3 image0 thermograph1 image4 infrared2 - mode
 groundstation2 groundstation4 star1 groundstation0 star3 planet5 planet6 planet7 star8 phenomenon9 star10 planet11 planet12 planet13 phenomenon14 phenomenon15 planet16 phenomenon17 star18 phenomenon19 phenomenon20 star21 star22 phenomenon23 phenomenon24 - direction
)
(:shared-data
  ((pointing ?s - satellite) - direction)
  (have_image ?d - direction ?m - mode) - (either satellite0 satellite1 satellite2 satellite3 satellite4 satellite5 satellite6 satellite8 satellite9 satellite10 satellite11)
)
(:init (mySatellite satellite7)
 (power_avail satellite7)
 (not (power_on instrument0))
 (not (calibrated instrument0))
 (= (calibration_target instrument0) groundstation0)
 (not (power_on instrument1))
 (not (calibrated instrument1))
 (= (calibration_target instrument1) star3)
 (not (power_on instrument2))
 (not (calibrated instrument2))
 (= (calibration_target instrument2) groundstation0)
 (not (power_on instrument3))
 (not (calibrated instrument3))
 (= (calibration_target instrument3) groundstation4)
 (not (power_on instrument4))
 (not (calibrated instrument4))
 (= (calibration_target instrument4) groundstation2)
 (not (power_on instrument5))
 (not (calibrated instrument5))
 (= (calibration_target instrument5) groundstation4)
 (not (power_on instrument6))
 (not (calibrated instrument6))
 (= (calibration_target instrument6) groundstation2)
 (not (power_on instrument7))
 (not (calibrated instrument7))
 (= (calibration_target instrument7) groundstation0)
 (not (power_on instrument8))
 (not (calibrated instrument8))
 (= (calibration_target instrument8) groundstation4)
 (not (power_on instrument9))
 (not (calibrated instrument9))
 (= (calibration_target instrument9) star1)
 (not (power_on instrument10))
 (not (calibrated instrument10))
 (= (calibration_target instrument10) star3)
 (not (power_on instrument11))
 (not (calibrated instrument11))
 (= (calibration_target instrument11) star1)
 (not (power_on instrument12))
 (not (calibrated instrument12))
 (= (calibration_target instrument12) groundstation0)
 (not (power_on instrument13))
 (not (calibrated instrument13))
 (= (calibration_target instrument13) star1)
 (not (power_on instrument14))
 (not (calibrated instrument14))
 (= (calibration_target instrument14) groundstation2)
 (not (power_on instrument15))
 (not (calibrated instrument15))
 (= (calibration_target instrument15) groundstation4)
 (not (power_on instrument16))
 (not (calibrated instrument16))
 (= (calibration_target instrument16) star1)
 (not (power_on instrument17))
 (not (calibrated instrument17))
 (= (calibration_target instrument17) groundstation0)
 (not (power_on instrument18))
 (not (calibrated instrument18))
 (= (calibration_target instrument18) star1)
 (not (power_on instrument19))
 (not (calibrated instrument19))
 (= (calibration_target instrument19) groundstation4)
 (not (power_on instrument20))
 (not (calibrated instrument20))
 (= (calibration_target instrument20) star1)
 (not (power_on instrument21))
 (not (calibrated instrument21))
 (= (calibration_target instrument21) star1)
 (not (power_on instrument22))
 (not (calibrated instrument22))
 (= (calibration_target instrument22) groundstation0)
 (not (power_on instrument23))
 (not (calibrated instrument23))
 (= (calibration_target instrument23) star3)
 (not (have_image planet5 image0))
 (not (have_image planet6 image4))
 (not (have_image planet7 image4))
 (not (have_image phenomenon9 image4))
 (not (have_image star10 thermograph1))
 (not (have_image planet11 image4))
 (not (have_image planet12 thermograph1))
 (not (have_image planet13 infrared3))
 (not (have_image phenomenon14 infrared2))
 (not (have_image phenomenon15 infrared2))
 (not (have_image planet16 infrared2))
 (not (have_image phenomenon17 thermograph1))
 (not (have_image star18 image4))
 (not (have_image star21 thermograph1))
 (not (have_image star22 image4))
 (not (have_image phenomenon23 infrared3))
 (not (have_image phenomenon24 infrared3))
 (= (pointing satellite7) planet12)
 (on_board satellite7 instrument15)
 (not (on_board satellite7 instrument0))
 (not (on_board satellite7 instrument1))
 (not (on_board satellite7 instrument2))
 (not (on_board satellite7 instrument3))
 (not (on_board satellite7 instrument4))
 (not (on_board satellite7 instrument5))
 (not (on_board satellite7 instrument6))
 (not (on_board satellite7 instrument7))
 (not (on_board satellite7 instrument8))
 (not (on_board satellite7 instrument9))
 (not (on_board satellite7 instrument10))
 (not (on_board satellite7 instrument11))
 (not (on_board satellite7 instrument12))
 (not (on_board satellite7 instrument13))
 (not (on_board satellite7 instrument14))
 (not (on_board satellite7 instrument16))
 (not (on_board satellite7 instrument17))
 (not (on_board satellite7 instrument18))
 (not (on_board satellite7 instrument19))
 (not (on_board satellite7 instrument20))
 (not (on_board satellite7 instrument21))
 (not (on_board satellite7 instrument22))
 (not (on_board satellite7 instrument23))
 (supports instrument0 infrared3)
 (not (supports instrument0 image0))
 (not (supports instrument0 thermograph1))
 (not (supports instrument0 image4))
 (not (supports instrument0 infrared2))
 (supports instrument1 image0)
 (supports instrument1 infrared2)
 (not (supports instrument1 infrared3))
 (not (supports instrument1 thermograph1))
 (not (supports instrument1 image4))
 (supports instrument2 image0)
 (supports instrument2 thermograph1)
 (not (supports instrument2 infrared3))
 (not (supports instrument2 image4))
 (not (supports instrument2 infrared2))
 (supports instrument3 infrared3)
 (supports instrument3 infrared2)
 (not (supports instrument3 image0))
 (not (supports instrument3 thermograph1))
 (not (supports instrument3 image4))
 (supports instrument4 infrared3)
 (supports instrument4 thermograph1)
 (supports instrument4 infrared2)
 (not (supports instrument4 image0))
 (not (supports instrument4 image4))
 (supports instrument5 thermograph1)
 (not (supports instrument5 infrared3))
 (not (supports instrument5 image0))
 (not (supports instrument5 image4))
 (not (supports instrument5 infrared2))
 (supports instrument6 image0)
 (supports instrument6 infrared2)
 (not (supports instrument6 infrared3))
 (not (supports instrument6 thermograph1))
 (not (supports instrument6 image4))
 (supports instrument7 infrared3)
 (supports instrument7 image0)
 (not (supports instrument7 thermograph1))
 (not (supports instrument7 image4))
 (not (supports instrument7 infrared2))
 (supports instrument8 image0)
 (supports instrument8 image4)
 (supports instrument8 infrared2)
 (not (supports instrument8 infrared3))
 (not (supports instrument8 thermograph1))
 (supports instrument9 infrared3)
 (not (supports instrument9 image0))
 (not (supports instrument9 thermograph1))
 (not (supports instrument9 image4))
 (not (supports instrument9 infrared2))
 (supports instrument10 image0)
 (supports instrument10 image4)
 (not (supports instrument10 infrared3))
 (not (supports instrument10 thermograph1))
 (not (supports instrument10 infrared2))
 (supports instrument11 infrared2)
 (not (supports instrument11 infrared3))
 (not (supports instrument11 image0))
 (not (supports instrument11 thermograph1))
 (not (supports instrument11 image4))
 (supports instrument12 image4)
 (not (supports instrument12 infrared3))
 (not (supports instrument12 image0))
 (not (supports instrument12 thermograph1))
 (not (supports instrument12 infrared2))
 (supports instrument13 image4)
 (not (supports instrument13 infrared3))
 (not (supports instrument13 image0))
 (not (supports instrument13 thermograph1))
 (not (supports instrument13 infrared2))
 (supports instrument14 thermograph1)
 (supports instrument14 infrared2)
 (not (supports instrument14 infrared3))
 (not (supports instrument14 image0))
 (not (supports instrument14 image4))
 (supports instrument15 image0)
 (supports instrument15 thermograph1)
 (not (supports instrument15 infrared3))
 (not (supports instrument15 image4))
 (not (supports instrument15 infrared2))
 (supports instrument16 image0)
 (supports instrument16 infrared2)
 (not (supports instrument16 infrared3))
 (not (supports instrument16 thermograph1))
 (not (supports instrument16 image4))
 (supports instrument17 infrared3)
 (not (supports instrument17 image0))
 (not (supports instrument17 thermograph1))
 (not (supports instrument17 image4))
 (not (supports instrument17 infrared2))
 (supports instrument18 thermograph1)
 (supports instrument18 image4)
 (supports instrument18 infrared2)
 (not (supports instrument18 infrared3))
 (not (supports instrument18 image0))
 (supports instrument19 infrared3)
 (not (supports instrument19 image0))
 (not (supports instrument19 thermograph1))
 (not (supports instrument19 image4))
 (not (supports instrument19 infrared2))
 (supports instrument20 infrared2)
 (not (supports instrument20 infrared3))
 (not (supports instrument20 image0))
 (not (supports instrument20 thermograph1))
 (not (supports instrument20 image4))
 (supports instrument21 image0)
 (supports instrument21 thermograph1)
 (not (supports instrument21 infrared3))
 (not (supports instrument21 image4))
 (not (supports instrument21 infrared2))
 (supports instrument22 thermograph1)
 (not (supports instrument22 infrared3))
 (not (supports instrument22 image0))
 (not (supports instrument22 image4))
 (not (supports instrument22 infrared2))
 (supports instrument23 thermograph1)
 (supports instrument23 image4)
 (supports instrument23 infrared2)
 (not (supports instrument23 infrared3))
 (not (supports instrument23 image0))
)
(:global-goal (and
 (= (pointing satellite1) star22)
 (= (pointing satellite4) phenomenon20)
 (= (pointing satellite8) planet16)
 (have_image planet5 image0)
 (have_image planet6 image4)
 (have_image planet7 image4)
 (have_image phenomenon9 image4)
 (have_image star10 thermograph1)
 (have_image planet11 image4)
 (have_image planet12 thermograph1)
 (have_image planet13 infrared3)
 (have_image phenomenon14 infrared2)
 (have_image phenomenon15 infrared2)
 (have_image planet16 infrared2)
 (have_image phenomenon17 thermograph1)
 (have_image star18 image4)
 (have_image star21 thermograph1)
 (have_image star22 image4)
 (have_image phenomenon23 infrared3)
 (have_image phenomenon24 infrared3)
))

)
