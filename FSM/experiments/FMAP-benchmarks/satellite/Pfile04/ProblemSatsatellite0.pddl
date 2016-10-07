(define (problem strips-sat-x-1)
(:domain satellite)
(:objects
 satellite0 satellite1 - satellite
 instrument0 instrument1 instrument2 - instrument
 infrared0 infrared1 thermograph2 - mode
 groundstation1 star0 star2 planet3 star4 planet5 star6 star7 phenomenon8 phenomenon9 - direction
)
(:shared-data
  ((pointing ?s - satellite) - direction)
  (have_image ?d - direction ?m - mode) - satellite1
)
(:init (mySatellite satellite0)
 (power_avail satellite0)
 (not (power_on instrument0))
 (not (calibrated instrument0))
 (= (calibration_target instrument0) star0)
 (not (power_on instrument1))
 (not (calibrated instrument1))
 (= (calibration_target instrument1) star2)
 (not (power_on instrument2))
 (not (calibrated instrument2))
 (= (calibration_target instrument2) star2)
 (not (have_image planet3 infrared1))
 (not (have_image star4 infrared1))
 (not (have_image planet5 thermograph2))
 (not (have_image star6 infrared1))
 (not (have_image star7 infrared0))
 (not (have_image phenomenon8 thermograph2))
 (not (have_image phenomenon9 infrared0))
 (= (pointing satellite0) star6)
 (on_board satellite0 instrument0)
 (not (on_board satellite0 instrument1))
 (not (on_board satellite0 instrument2))
 (supports instrument0 infrared0)
 (supports instrument0 thermograph2)
 (not (supports instrument0 infrared1))
 (supports instrument1 infrared0)
 (supports instrument1 infrared1)
 (supports instrument1 thermograph2)
 (supports instrument2 infrared1)
 (supports instrument2 thermograph2)
 (not (supports instrument2 infrared0))
)
(:global-goal (and
 (= (pointing satellite1) planet5)
 (have_image planet3 infrared1)
 (have_image star4 infrared1)
 (have_image planet5 thermograph2)
 (have_image star6 infrared1)
 (have_image star7 infrared0)
 (have_image phenomenon8 thermograph2)
 (have_image phenomenon9 infrared0)
))

)