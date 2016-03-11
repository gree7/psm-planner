(define (problem strips-sat-x-1)
(:domain satellite)
(:objects
 satellite0 satellite1 satellite2 satellite3 satellite4 satellite5 satellite6 satellite7 - satellite
 instrument0 instrument1 instrument2 instrument3 instrument4 instrument5 instrument6 instrument7 instrument8 instrument9 instrument10 instrument11 instrument12 instrument13 instrument14 instrument15 instrument16 instrument17 instrument18 - instrument
 image1 infrared0 thermograph3 spectrograph2 thermograph4 - mode
 star3 groundstation0 groundstation2 star1 star4 phenomenon5 planet6 planet7 star8 phenomenon9 phenomenon10 planet11 star12 star13 planet14 star15 phenomenon16 planet17 star18 star19 planet20 planet21 planet22 planet23 planet24 - direction
)
(:shared-data
  ((pointing ?s - satellite) - direction)
  (have_image ?d - direction ?m - mode) - (either satellite0 satellite1 satellite2 satellite3 satellite4 satellite5 satellite7)
)
(:init (mySatellite satellite6)
 (= (pointing satellite0) star19)
 (power_avail satellite6)
 (not (power_on instrument0))
 (not (calibrated instrument0))
 (= (calibration_target instrument0) groundstation0)
 (not (power_on instrument1))
 (not (calibrated instrument1))
 (= (calibration_target instrument1) star3)
 (not (power_on instrument2))
 (not (calibrated instrument2))
 (= (calibration_target instrument2) star4)
 (not (power_on instrument3))
 (not (calibrated instrument3))
 (= (calibration_target instrument3) groundstation2)
 (not (power_on instrument4))
 (not (calibrated instrument4))
 (= (calibration_target instrument4) star1)
 (not (power_on instrument5))
 (not (calibrated instrument5))
 (= (calibration_target instrument5) groundstation2)
 (not (power_on instrument6))
 (not (calibrated instrument6))
 (= (calibration_target instrument6) groundstation2)
 (not (power_on instrument7))
 (not (calibrated instrument7))
 (= (calibration_target instrument7) star3)
 (not (power_on instrument8))
 (not (calibrated instrument8))
 (= (calibration_target instrument8) groundstation2)
 (not (power_on instrument9))
 (not (calibrated instrument9))
 (= (calibration_target instrument9) star3)
 (not (power_on instrument10))
 (not (calibrated instrument10))
 (= (calibration_target instrument10) groundstation0)
 (not (power_on instrument11))
 (not (calibrated instrument11))
 (= (calibration_target instrument11) groundstation0)
 (not (power_on instrument12))
 (not (calibrated instrument12))
 (= (calibration_target instrument12) star1)
 (not (power_on instrument13))
 (not (calibrated instrument13))
 (= (calibration_target instrument13) star3)
 (not (power_on instrument14))
 (not (calibrated instrument14))
 (= (calibration_target instrument14) groundstation2)
 (not (power_on instrument15))
 (not (calibrated instrument15))
 (= (calibration_target instrument15) groundstation0)
 (not (power_on instrument16))
 (not (calibrated instrument16))
 (= (calibration_target instrument16) groundstation2)
 (not (power_on instrument17))
 (not (calibrated instrument17))
 (= (calibration_target instrument17) star1)
 (not (power_on instrument18))
 (not (calibrated instrument18))
 (= (calibration_target instrument18) star4)
 (not (have_image phenomenon5 spectrograph2))
 (not (have_image planet6 spectrograph2))
 (not (have_image planet7 infrared0))
 (not (have_image phenomenon9 infrared0))
 (not (have_image phenomenon10 image1))
 (not (have_image planet11 image1))
 (not (have_image star12 thermograph3))
 (not (have_image star13 thermograph3))
 (not (have_image planet14 thermograph4))
 (not (have_image star15 thermograph4))
 (not (have_image phenomenon16 image1))
 (not (have_image planet17 thermograph3))
 (not (have_image star18 image1))
 (not (have_image planet20 image1))
 (not (have_image planet21 infrared0))
 (not (have_image planet22 image1))
 (not (have_image planet23 thermograph3))
 (not (have_image planet24 infrared0))
 (= (pointing satellite6) planet17)
 (on_board satellite6 instrument13)
 (on_board satellite6 instrument14)
 (on_board satellite6 instrument15)
 (not (on_board satellite6 instrument0))
 (not (on_board satellite6 instrument1))
 (not (on_board satellite6 instrument2))
 (not (on_board satellite6 instrument3))
 (not (on_board satellite6 instrument4))
 (not (on_board satellite6 instrument5))
 (not (on_board satellite6 instrument6))
 (not (on_board satellite6 instrument7))
 (not (on_board satellite6 instrument8))
 (not (on_board satellite6 instrument9))
 (not (on_board satellite6 instrument10))
 (not (on_board satellite6 instrument11))
 (not (on_board satellite6 instrument12))
 (not (on_board satellite6 instrument16))
 (not (on_board satellite6 instrument17))
 (not (on_board satellite6 instrument18))
 (supports instrument0 image1)
 (supports instrument0 thermograph4)
 (not (supports instrument0 infrared0))
 (not (supports instrument0 thermograph3))
 (not (supports instrument0 spectrograph2))
 (supports instrument1 thermograph3)
 (supports instrument1 spectrograph2)
 (not (supports instrument1 image1))
 (not (supports instrument1 infrared0))
 (not (supports instrument1 thermograph4))
 (supports instrument2 spectrograph2)
 (not (supports instrument2 image1))
 (not (supports instrument2 infrared0))
 (not (supports instrument2 thermograph3))
 (not (supports instrument2 thermograph4))
 (supports instrument3 image1)
 (supports instrument3 spectrograph2)
 (not (supports instrument3 infrared0))
 (not (supports instrument3 thermograph3))
 (not (supports instrument3 thermograph4))
 (supports instrument4 thermograph3)
 (supports instrument4 spectrograph2)
 (supports instrument4 thermograph4)
 (not (supports instrument4 image1))
 (not (supports instrument4 infrared0))
 (supports instrument5 image1)
 (supports instrument5 infrared0)
 (supports instrument5 thermograph3)
 (not (supports instrument5 spectrograph2))
 (not (supports instrument5 thermograph4))
 (supports instrument6 infrared0)
 (supports instrument6 spectrograph2)
 (not (supports instrument6 image1))
 (not (supports instrument6 thermograph3))
 (not (supports instrument6 thermograph4))
 (supports instrument7 thermograph3)
 (supports instrument7 spectrograph2)
 (not (supports instrument7 image1))
 (not (supports instrument7 infrared0))
 (not (supports instrument7 thermograph4))
 (supports instrument8 image1)
 (not (supports instrument8 infrared0))
 (not (supports instrument8 thermograph3))
 (not (supports instrument8 spectrograph2))
 (not (supports instrument8 thermograph4))
 (supports instrument9 infrared0)
 (not (supports instrument9 image1))
 (not (supports instrument9 thermograph3))
 (not (supports instrument9 spectrograph2))
 (not (supports instrument9 thermograph4))
 (supports instrument10 infrared0)
 (supports instrument10 spectrograph2)
 (supports instrument10 thermograph4)
 (not (supports instrument10 image1))
 (not (supports instrument10 thermograph3))
 (supports instrument11 infrared0)
 (not (supports instrument11 image1))
 (not (supports instrument11 thermograph3))
 (not (supports instrument11 spectrograph2))
 (not (supports instrument11 thermograph4))
 (supports instrument12 infrared0)
 (not (supports instrument12 image1))
 (not (supports instrument12 thermograph3))
 (not (supports instrument12 spectrograph2))
 (not (supports instrument12 thermograph4))
 (supports instrument13 infrared0)
 (supports instrument13 thermograph3)
 (not (supports instrument13 image1))
 (not (supports instrument13 spectrograph2))
 (not (supports instrument13 thermograph4))
 (supports instrument14 spectrograph2)
 (not (supports instrument14 image1))
 (not (supports instrument14 infrared0))
 (not (supports instrument14 thermograph3))
 (not (supports instrument14 thermograph4))
 (supports instrument15 thermograph4)
 (not (supports instrument15 image1))
 (not (supports instrument15 infrared0))
 (not (supports instrument15 thermograph3))
 (not (supports instrument15 spectrograph2))
 (supports instrument16 thermograph4)
 (not (supports instrument16 image1))
 (not (supports instrument16 infrared0))
 (not (supports instrument16 thermograph3))
 (not (supports instrument16 spectrograph2))
 (supports instrument17 spectrograph2)
 (not (supports instrument17 image1))
 (not (supports instrument17 infrared0))
 (not (supports instrument17 thermograph3))
 (not (supports instrument17 thermograph4))
 (supports instrument18 thermograph4)
 (not (supports instrument18 image1))
 (not (supports instrument18 infrared0))
 (not (supports instrument18 thermograph3))
 (not (supports instrument18 spectrograph2))
)
(:global-goal (and
 (= (pointing satellite0) star19)
 (= (pointing satellite1) planet22)
 (= (pointing satellite2) star13)
 (= (pointing satellite3) planet14)
 (= (pointing satellite5) planet24)
 (= (pointing satellite7) star3)
 (have_image phenomenon5 spectrograph2)
 (have_image planet6 spectrograph2)
 (have_image planet7 infrared0)
 (have_image phenomenon9 infrared0)
 (have_image phenomenon10 image1)
 (have_image planet11 image1)
 (have_image star12 thermograph3)
 (have_image star13 thermograph3)
 (have_image planet14 thermograph4)
 (have_image star15 thermograph4)
 (have_image phenomenon16 image1)
 (have_image planet17 thermograph3)
 (have_image star18 image1)
 (have_image planet20 image1)
 (have_image planet21 infrared0)
 (have_image planet22 image1)
 (have_image planet23 thermograph3)
 (have_image planet24 infrared0)
))

)
