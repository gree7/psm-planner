(define (problem strips-sat-x-1)
(:domain satellite)
(:objects
 satellite0 satellite1 satellite2 satellite3 satellite4 - satellite
 instrument0 instrument1 instrument2 instrument3 instrument4 instrument5 instrument6 instrument7 instrument8 instrument9 instrument10 - instrument
 infrared0 spectrograph1 infrared3 image4 image2 - mode
 star1 groundstation3 star4 star2 star0 planet5 star6 star7 phenomenon8 planet9 planet10 star11 star12 phenomenon13 phenomenon14 star15 star16 - direction
)
(:shared-data
  ((pointing ?s - satellite) - direction)
  (have_image ?d - direction ?m - mode) - (either satellite0 satellite1 satellite2 satellite3)
)
(:init (mySatellite satellite4)
 (power_avail satellite4)
 (not (power_on instrument0))
 (not (calibrated instrument0))
 (= (calibration_target instrument0) star1)
 (not (power_on instrument1))
 (not (calibrated instrument1))
 (= (calibration_target instrument1) groundstation3)
 (not (power_on instrument2))
 (not (calibrated instrument2))
 (= (calibration_target instrument2) groundstation3)
 (not (power_on instrument3))
 (not (calibrated instrument3))
 (= (calibration_target instrument3) star4)
 (not (power_on instrument4))
 (not (calibrated instrument4))
 (= (calibration_target instrument4) star2)
 (not (power_on instrument5))
 (not (calibrated instrument5))
 (= (calibration_target instrument5) star0)
 (not (power_on instrument6))
 (not (calibrated instrument6))
 (= (calibration_target instrument6) groundstation3)
 (not (power_on instrument7))
 (not (calibrated instrument7))
 (= (calibration_target instrument7) star4)
 (not (power_on instrument8))
 (not (calibrated instrument8))
 (= (calibration_target instrument8) star4)
 (not (power_on instrument9))
 (not (calibrated instrument9))
 (= (calibration_target instrument9) star2)
 (not (power_on instrument10))
 (not (calibrated instrument10))
 (= (calibration_target instrument10) star0)
 (not (have_image planet5 image4))
 (not (have_image star6 infrared3))
 (not (have_image star7 image4))
 (not (have_image phenomenon8 image4))
 (not (have_image planet9 infrared0))
 (not (have_image planet10 infrared3))
 (not (have_image star12 image4))
 (not (have_image phenomenon13 image4))
 (not (have_image phenomenon14 spectrograph1))
 (not (have_image star15 spectrograph1))
 (not (have_image star16 image2))
 (= (pointing satellite4) planet10)
 (on_board satellite4 instrument8)
 (on_board satellite4 instrument9)
 (on_board satellite4 instrument10)
 (not (on_board satellite4 instrument0))
 (not (on_board satellite4 instrument1))
 (not (on_board satellite4 instrument2))
 (not (on_board satellite4 instrument3))
 (not (on_board satellite4 instrument4))
 (not (on_board satellite4 instrument5))
 (not (on_board satellite4 instrument6))
 (not (on_board satellite4 instrument7))
 (supports instrument0 image4)
 (not (supports instrument0 infrared0))
 (not (supports instrument0 spectrograph1))
 (not (supports instrument0 infrared3))
 (not (supports instrument0 image2))
 (supports instrument1 infrared0)
 (supports instrument1 spectrograph1)
 (not (supports instrument1 infrared3))
 (not (supports instrument1 image4))
 (not (supports instrument1 image2))
 (supports instrument2 infrared0)
 (supports instrument2 image2)
 (not (supports instrument2 spectrograph1))
 (not (supports instrument2 infrared3))
 (not (supports instrument2 image4))
 (supports instrument3 infrared0)
 (supports instrument3 infrared3)
 (not (supports instrument3 spectrograph1))
 (not (supports instrument3 image4))
 (not (supports instrument3 image2))
 (supports instrument4 infrared0)
 (supports instrument4 spectrograph1)
 (supports instrument4 image4)
 (not (supports instrument4 infrared3))
 (not (supports instrument4 image2))
 (supports instrument5 infrared0)
 (supports instrument5 infrared3)
 (supports instrument5 image2)
 (not (supports instrument5 spectrograph1))
 (not (supports instrument5 image4))
 (supports instrument6 infrared0)
 (supports instrument6 infrared3)
 (not (supports instrument6 spectrograph1))
 (not (supports instrument6 image4))
 (not (supports instrument6 image2))
 (supports instrument7 spectrograph1)
 (supports instrument7 infrared3)
 (supports instrument7 image4)
 (not (supports instrument7 infrared0))
 (not (supports instrument7 image2))
 (supports instrument8 spectrograph1)
 (supports instrument8 image4)
 (not (supports instrument8 infrared0))
 (not (supports instrument8 infrared3))
 (not (supports instrument8 image2))
 (supports instrument9 infrared3)
 (not (supports instrument9 infrared0))
 (not (supports instrument9 spectrograph1))
 (not (supports instrument9 image4))
 (not (supports instrument9 image2))
 (supports instrument10 image4)
 (supports instrument10 image2)
 (not (supports instrument10 infrared0))
 (not (supports instrument10 spectrograph1))
 (not (supports instrument10 infrared3))
)
(:global-goal (and
 (= (pointing satellite4) planet9)
 (have_image planet5 image4)
 (have_image star6 infrared3)
 (have_image star7 image4)
 (have_image phenomenon8 image4)
 (have_image planet9 infrared0)
 (have_image planet10 infrared3)
 (have_image star12 image4)
 (have_image phenomenon13 image4)
 (have_image phenomenon14 spectrograph1)
 (have_image star15 spectrograph1)
 (have_image star16 image2)
))

)