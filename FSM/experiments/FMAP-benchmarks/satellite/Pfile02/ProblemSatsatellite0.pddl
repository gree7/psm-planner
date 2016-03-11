(define (problem strips-sat-x-1)
(:domain satellite)
(:objects
 satellite0 - satellite
 instrument0 instrument1 - instrument
 infrared0 infrared1 image2 - mode
 groundstation1 star0 groundstation2 planet3 planet4 phenomenon5 phenomenon6 star7 - direction
)
(:init (mySatellite satellite0)
 (power_avail satellite0)
 (not (power_on instrument0))
 (not (calibrated instrument0))
 (= (calibration_target instrument0) star0)
 (not (power_on instrument1))
 (not (calibrated instrument1))
 (= (calibration_target instrument1) groundstation2)
 (not (have_image planet3 infrared0))
 (not (have_image planet4 infrared0))
 (not (have_image phenomenon5 image2))
 (not (have_image phenomenon6 infrared0))
 (not (have_image star7 infrared0))
 (= (pointing satellite0) planet4)
 (on_board satellite0 instrument0)
 (on_board satellite0 instrument1)
 (supports instrument0 infrared0)
 (supports instrument0 infrared1)
 (not (supports instrument0 image2))
 (supports instrument1 infrared0)
 (supports instrument1 infrared1)
 (supports instrument1 image2)
)
(:global-goal (and
 (have_image planet3 infrared0)
 (have_image planet4 infrared0)
 (have_image phenomenon5 image2)
 (have_image phenomenon6 infrared0)
 (have_image star7 infrared0)
))

)
