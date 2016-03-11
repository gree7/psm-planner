(define (problem satellite0)
(:domain satellite)
(:objects
 satellite0 satellite1 - satellite
 instrument0 instrument1 instrument2 instrument3 - instrument
 image1 infrared0 spectrograph2 - mode
 star1 star2 star0 star3 star4 phenomenon5 phenomenon6 phenomenon7 - direction
)
(:init
 (power_avail satellite0)
 (calibration_target instrument0 star1)
 (calibration_target instrument1 star2)
 (calibration_target instrument2 star0)
 (calibration_target instrument3 star0)
 (pointing satellite0 star4)
 (pointing satellite1 star0)
 (on_board satellite0 instrument0)
 (on_board satellite0 instrument1)
 (on_board satellite0 instrument2)
 (supports instrument0 infrared0)
 (supports instrument0 spectrograph2)
 (supports instrument1 image1)
 (supports instrument2 image1)
 (supports instrument2 infrared0)
 (supports instrument3 image1)
 (supports instrument3 spectrograph2)
)
(:goal (and
 (pointing satellite0 phenomenon5)
 (have_image star3 infrared0)
 (have_image star4 spectrograph2)
 (have_image phenomenon5 spectrograph2)
 (have_image phenomenon7 spectrograph2)
))
)
