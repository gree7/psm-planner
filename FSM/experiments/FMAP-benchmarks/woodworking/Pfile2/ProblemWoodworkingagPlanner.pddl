(define (problem wood-prob)
(:domain woodworking)
(:objects
 agPlanner agGrinder agVarnisher agSaw - agent
 grinder0 - grinder
 glazer0 - glazer
 immersion-varnisher0 - immersion-varnisher
 planer0 - planer
 highspeed-saw0 - highspeed-saw
 spray-varnisher0 - spray-varnisher
 saw0 - saw
 green white mauve - acolour
 cherry beech - awood
 p0 p1 p2 p3 - part
 b0 - board
 s0 s1 s2 s3 s4 s5 - aboardsize
)
(:shared-data
 (unused ?obj - part) (available ?obj - woodobj)
 (empty ?m - highspeed-saw) (is-smooth ?surface - surface)
 (has-colour ?machine - machine ?colour - acolour)
 ((surface-condition ?obj - woodobj) - surface)
 ((treatment ?obj - part) - treatmentstatus)
 ((colour ?obj - part) - acolour)
 ((wood ?obj - woodobj) - awood)
 ((boardsize ?board - board) - aboardsize)
 ((goalsize ?part - part) - apartsize)
 ((boardsize-successor ?size1 - aboardsize) - aboardsize)
 ((in-highspeed-saw ?m - highspeed-saw) - board)
 ((grind-treatment-change ?old - treatmentstatus) - treatmentstatus)
 - (either agGrinder agVarnisher agSaw))
(:init
 (= (colour p0) natural)
 (unused p0)
 (= (goalsize p0) medium)
 (available p0)
 (= (wood p0) cherry)
 (= (surface-condition p0) rough)
 (= (treatment p0) varnished)
 (= (colour p1) green)
 (unused p1)
 (= (goalsize p1) large)
 (available p1)
 (= (wood p1) cherry)
 (= (surface-condition p1) rough)
 (= (treatment p1) colourfragments)
 (= (colour p2) white)
 (unused p2)
 (= (goalsize p2) small)
 (available p2)
 (= (wood p2) cherry)
 (= (surface-condition p2) verysmooth)
 (= (treatment p2) glazed)
 (= (colour p3) natural)
 (unused p3)
 (= (goalsize p3) large)
 (not (available p3))
 (= (wood p3) unknown-wood)
 (= (surface-condition p3) smooth)
 (= (treatment p3) untreated)
 (= (grind-treatment-change varnished) colourfragments)
 (= (grind-treatment-change glazed) untreated)
 (= (grind-treatment-change untreated) untreated)
 (= (grind-treatment-change colourfragments) untreated)
 (is-smooth verysmooth)
 (is-smooth smooth)
 (not (is-smooth rough))
 (= (boardsize-successor s0) s1)
 (= (boardsize-successor s1) s2)
 (= (boardsize-successor s2) s3)
 (= (boardsize-successor s3) s4)
 (= (boardsize-successor s4) s5)
 (not (has-colour grinder0 natural))
 (not (has-colour grinder0 green))
 (not (has-colour grinder0 white))
 (not (has-colour grinder0 mauve))
 (not (has-colour glazer0 natural))
 (not (has-colour glazer0 green))
 (has-colour glazer0 white)
 (has-colour glazer0 mauve)
 (has-colour immersion-varnisher0 natural)
 (not (has-colour immersion-varnisher0 green))
 (has-colour immersion-varnisher0 white)
 (has-colour immersion-varnisher0 mauve)
 (not (has-colour planer0 natural))
 (not (has-colour planer0 green))
 (not (has-colour planer0 white))
 (not (has-colour planer0 mauve))
 (not (has-colour highspeed-saw0 natural))
 (not (has-colour highspeed-saw0 green))
 (not (has-colour highspeed-saw0 white))
 (not (has-colour highspeed-saw0 mauve))
 (has-colour spray-varnisher0 natural)
 (not (has-colour spray-varnisher0 green))
 (has-colour spray-varnisher0 white)
 (has-colour spray-varnisher0 mauve)
 (not (has-colour saw0 natural))
 (not (has-colour saw0 green))
 (not (has-colour saw0 white))
 (not (has-colour saw0 mauve))
 (= (in-highspeed-saw highspeed-saw0) no-board)
 (= (boardsize b0) s5)
 (= (wood b0) beech)
 (= (surface-condition b0) rough)
 (available b0)
)
(:global-goal (and
 (available p0)
 (= (colour p0) mauve)
 (= (wood p0) cherry)
 (= (surface-condition p0) smooth)
 (= (treatment p0) glazed)
 (available p1)
 (= (colour p1) natural)
 (= (wood p1) cherry)
 (= (surface-condition p1) verysmooth)
 (= (treatment p1) varnished)
 (available p2)
 (= (colour p2) mauve)
 (= (surface-condition p2) smooth)
 (available p3)
 (= (colour p3) white)
 (= (wood p3) beech)
))

)