(define (problem strips-sat-x-1)
(:domain satellite)
(:objects
 satellite0 satellite1 satellite2 satellite3 - satellite
 instrument0 instrument1 instrument2 instrument3 instrument4 instrument5 instrument6 instrument7 instrument8 instrument9 - instrument
 thermograph2 image0 thermograph1 spectrograph3 - mode
 star2 groundstation1 star0 star3 star4 phenomenon5 star6 star7 phenomenon8 phenomenon9 star10 planet11 phenomenon12 phenomenon13 phenomenon14 - direction
)
(:shared-data
  ((pointing ?s - satellite) - direction)
  (have_image ?d - direction ?m - mode) - (either satellite0 satellite2 satellite3)
)
(:init (mySatellite satellite1)
 (power_avail satellite1)
 (not (power_on instrument0))
 (not (calibrated instrument0))
 (= (calibration_target instrument0) star3)
 (not (power_on instrument1))
 (not (calibrated instrument1))
 (= (calibration_target instrument1) star2)
 (not (power_on instrument2))
 (not (calibrated instrument2))
 (= (calibration_target instrument2) star4)
 (not (power_on instrument3))
 (not (calibrated instrument3))
 (= (calibration_target instrument3) groundstation1)
 (not (power_on instrument4))
 (not (calibrated instrument4))
 (= (calibration_target instrument4) star4)
 (not (power_on instrument5))
 (not (calibrated instrument5))
 (= (calibration_target instrument5) star0)
 (not (power_on instrument6))
 (not (calibrated instrument6))
 (= (calibration_target instrument6) star3)
 (not (power_on instrument7))
 (not (calibrated instrument7))
 (= (calibration_target instrument7) star0)
 (not (power_on instrument8))
 (not (calibrated instrument8))
 (= (calibration_target instrument8) star3)
 (not (power_on instrument9))
 (not (calibrated instrument9))
 (= (calibration_target instrument9) star4)
 (not (have_image phenomenon5 thermograph1))
 (not (have_image star6 thermograph1))
 (not (have_image star7 spectrograph3))
 (not (have_image phenomenon8 image0))
 (not (have_image phenomenon9 image0))
 (not (have_image star10 spectrograph3))
 (not (have_image planet11 thermograph2))
 (not (have_image phenomenon12 image0))
 (not (have_image phenomenon13 thermograph1))
 (not (have_image phenomenon14 thermograph2))
 (= (pointing satellite1) star4)
 (on_board satellite1 instrument3)
 (on_board satellite1 instrument4)
 (on_board satellite1 instrument5)
 (not (on_board satellite1 instrument0))
 (not (on_board satellite1 instrument1))
 (not (on_board satellite1 instrument2))
 (not (on_board satellite1 instrument6))
 (not (on_board satellite1 instrument7))
 (not (on_board satellite1 instrument8))
 (not (on_board satellite1 instrument9))
 (supports instrument0 image0)
 (supports instrument0 thermograph1)
 (not (supports instrument0 thermograph2))
 (not (supports instrument0 spectrograph3))
 (supports instrument1 thermograph2)
 (supports instrument1 thermograph1)
 (supports instrument1 spectrograph3)
 (not (supports instrument1 image0))
 (supports instrument2 spectrograph3)
 (not (supports instrument2 thermograph2))
 (not (supports instrument2 image0))
 (not (supports instrument2 thermograph1))
 (supports instrument3 thermograph2)
 (supports instrument3 image0)
 (not (supports instrument3 thermograph1))
 (not (supports instrument3 spectrograph3))
 (supports instrument4 thermograph1)
 (not (supports instrument4 thermograph2))
 (not (supports instrument4 image0))
 (not (supports instrument4 spectrograph3))
 (supports instrument5 thermograph2)
 (supports instrument5 thermograph1)
 (supports instrument5 spectrograph3)
 (not (supports instrument5 image0))
 (supports instrument6 thermograph2)
 (supports instrument6 thermograph1)
 (not (supports instrument6 image0))
 (not (supports instrument6 spectrograph3))
 (supports instrument7 thermograph2)
 (supports instrument7 image0)
 (supports instrument7 thermograph1)
 (not (supports instrument7 spectrograph3))
 (supports instrument8 image0)
 (not (supports instrument8 thermograph2))
 (not (supports instrument8 thermograph1))
 (not (supports instrument8 spectrograph3))
 (supports instrument9 image0)
 (supports instrument9 thermograph1)
 (supports instrument9 spectrograph3)
 (not (supports instrument9 thermograph2))
)
(:global-goal (and
 (have_image phenomenon5 thermograph1)
 (have_image star6 thermograph1)
 (have_image star7 spectrograph3)
 (have_image phenomenon8 image0)
 (have_image phenomenon9 image0)
 (have_image star10 spectrograph3)
 (have_image planet11 thermograph2)
 (have_image phenomenon12 image0)
 (have_image phenomenon13 thermograph1)
 (have_image phenomenon14 thermograph2)
))

)
