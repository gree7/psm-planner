(define (problem strips-sat-x-1)
(:domain satellite)
(:objects
 satellite0 satellite1 satellite2 satellite3 satellite4 - satellite
 instrument0 instrument1 instrument2 instrument3 instrument4 instrument5 instrument6 instrument7 instrument8 - instrument
 image4 thermograph1 thermograph0 thermograph2 image3 - mode
 groundstation2 star1 star4 star0 groundstation3 phenomenon5 planet6 planet7 planet8 planet9 planet10 planet11 phenomenon12 planet13 star14 planet15 planet16 planet17 phenomenon18 star19 planet20 star21 star22 planet23 planet24 planet25 star26 phenomenon27 planet28 planet29 - direction
)
(:shared-data
  ((pointing ?s - satellite) - direction)
  (have_image ?d - direction ?m - mode) - (either satellite1 satellite2 satellite3 satellite4)
)
(:init (mySatellite satellite0)
 (power_avail satellite0)
 (not (power_on instrument0))
 (not (calibrated instrument0))
 (= (calibration_target instrument0) groundstation3)
 (not (power_on instrument1))
 (not (calibrated instrument1))
 (= (calibration_target instrument1) groundstation3)
 (not (power_on instrument2))
 (not (calibrated instrument2))
 (= (calibration_target instrument2) groundstation3)
 (not (power_on instrument3))
 (not (calibrated instrument3))
 (= (calibration_target instrument3) star1)
 (not (power_on instrument4))
 (not (calibrated instrument4))
 (= (calibration_target instrument4) groundstation3)
 (not (power_on instrument5))
 (not (calibrated instrument5))
 (= (calibration_target instrument5) groundstation3)
 (not (power_on instrument6))
 (not (calibrated instrument6))
 (= (calibration_target instrument6) star4)
 (not (power_on instrument7))
 (not (calibrated instrument7))
 (= (calibration_target instrument7) star0)
 (not (power_on instrument8))
 (not (calibrated instrument8))
 (= (calibration_target instrument8) groundstation3)
 (not (have_image phenomenon5 thermograph1))
 (not (have_image planet6 image4))
 (not (have_image planet7 image3))
 (not (have_image planet8 image3))
 (not (have_image planet9 thermograph0))
 (not (have_image planet10 thermograph1))
 (not (have_image planet11 thermograph2))
 (not (have_image phenomenon12 image3))
 (not (have_image planet13 thermograph1))
 (not (have_image star14 image3))
 (not (have_image planet15 thermograph0))
 (not (have_image planet16 image3))
 (not (have_image planet17 image4))
 (not (have_image phenomenon18 image3))
 (not (have_image star19 thermograph0))
 (not (have_image star21 thermograph1))
 (not (have_image star22 image4))
 (not (have_image planet23 thermograph1))
 (not (have_image planet24 thermograph2))
 (not (have_image planet25 thermograph1))
 (not (have_image star26 thermograph0))
 (not (have_image phenomenon27 thermograph1))
 (not (have_image planet28 thermograph2))
 (not (have_image planet29 thermograph0))
 (= (pointing satellite0) star19)
 (on_board satellite0 instrument0)
 (on_board satellite0 instrument1)
 (not (on_board satellite0 instrument2))
 (not (on_board satellite0 instrument3))
 (not (on_board satellite0 instrument4))
 (not (on_board satellite0 instrument5))
 (not (on_board satellite0 instrument6))
 (not (on_board satellite0 instrument7))
 (not (on_board satellite0 instrument8))
 (supports instrument0 image4)
 (not (supports instrument0 thermograph1))
 (not (supports instrument0 thermograph0))
 (not (supports instrument0 thermograph2))
 (not (supports instrument0 image3))
 (supports instrument1 image4)
 (supports instrument1 thermograph1)
 (not (supports instrument1 thermograph0))
 (not (supports instrument1 thermograph2))
 (not (supports instrument1 image3))
 (supports instrument2 image4)
 (supports instrument2 thermograph0)
 (supports instrument2 thermograph2)
 (not (supports instrument2 thermograph1))
 (not (supports instrument2 image3))
 (supports instrument3 image4)
 (supports instrument3 image3)
 (not (supports instrument3 thermograph1))
 (not (supports instrument3 thermograph0))
 (not (supports instrument3 thermograph2))
 (supports instrument4 image3)
 (not (supports instrument4 image4))
 (not (supports instrument4 thermograph1))
 (not (supports instrument4 thermograph0))
 (not (supports instrument4 thermograph2))
 (supports instrument5 image4)
 (supports instrument5 thermograph1)
 (not (supports instrument5 thermograph0))
 (not (supports instrument5 thermograph2))
 (not (supports instrument5 image3))
 (supports instrument6 thermograph1)
 (supports instrument6 thermograph0)
 (supports instrument6 image3)
 (not (supports instrument6 image4))
 (not (supports instrument6 thermograph2))
 (supports instrument7 thermograph0)
 (supports instrument7 thermograph2)
 (not (supports instrument7 image4))
 (not (supports instrument7 thermograph1))
 (not (supports instrument7 image3))
 (supports instrument8 thermograph2)
 (supports instrument8 image3)
 (not (supports instrument8 image4))
 (not (supports instrument8 thermograph1))
 (not (supports instrument8 thermograph0))
)
(:global-goal (and
 (= (pointing satellite1) phenomenon5)
 (= (pointing satellite2) planet11)
 (= (pointing satellite4) planet11)
 (have_image phenomenon5 thermograph1)
 (have_image planet6 image4)
 (have_image planet7 image3)
 (have_image planet8 image3)
 (have_image planet9 thermograph0)
 (have_image planet10 thermograph1)
 (have_image planet11 thermograph2)
 (have_image phenomenon12 image3)
 (have_image planet13 thermograph1)
 (have_image star14 image3)
 (have_image planet15 thermograph0)
 (have_image planet16 image3)
 (have_image planet17 image4)
 (have_image phenomenon18 image3)
 (have_image star19 thermograph0)
 (have_image star21 thermograph1)
 (have_image star22 image4)
 (have_image planet23 thermograph1)
 (have_image planet24 thermograph2)
 (have_image planet25 thermograph1)
 (have_image star26 thermograph0)
 (have_image phenomenon27 thermograph1)
 (have_image planet28 thermograph2)
 (have_image planet29 thermograph0)
))

)
