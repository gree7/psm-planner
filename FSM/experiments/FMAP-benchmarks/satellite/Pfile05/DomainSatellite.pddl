(define (domain satellite)
(:requirements :typing :equality :fluents)
(:types agent direction instrument mode - object
        satellite - agent)
(:predicates
  (power_avail ?s - satellite)
  (mySatellite ?s - satellite)
  (power_on ?i - instrument)
  (calibrated ?i - instrument)
  (have_image ?d - direction ?m - mode)
  (on_board ?s - satellite ?i - instrument)
  (supports ?i - instrument ?m - mode))
(:functions
  (pointing ?s - satellite) - direction
  (calibration_target ?i - instrument) - direction)
(:action turn_to
 :parameters (?s - satellite ?d_new - direction ?d_prev - direction)
 :precondition (and (mySatellite ?s) (= (pointing ?s) ?d_prev))
 :effect (and (assign (pointing ?s) ?d_new)))
(:action switch_on
 :parameters (?i - instrument ?s - satellite)
 :precondition (and (mySatellite ?s) (on_board ?s ?i) (power_avail ?s))
 :effect (and (power_on ?i) (not (calibrated ?i)) (not (power_avail ?s))))
(:action switch_off
 :parameters (?i - instrument ?s - satellite)
 :precondition (and (mySatellite ?s) (on_board ?s ?i) (power_on ?i))
 :effect (and (not (power_on ?i)) (power_avail ?s)))
(:action calibrate
 :parameters (?s - satellite ?i - instrument ?d - direction)
 :precondition (and (mySatellite ?s) (on_board ?s ?i) (= (pointing ?s) ?d)
	             (= (calibration_target ?i) ?d) (power_on ?i))
 :effect (calibrated ?i))
(:action take_image
 :parameters (?s - satellite ?d - direction ?i - instrument ?m - mode)
 :precondition (and (mySatellite ?s) (calibrated ?i) (on_board ?s ?i)
               (supports ?i ?m) (power_on ?i) (= (pointing ?s) ?d))
 :effect (have_image ?d ?m)))
