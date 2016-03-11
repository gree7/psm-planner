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
 mauve white black red blue green - acolour
 pine oak mahogany - awood
 p0 p1 p2 p3 p4 p5 p6 p7 p8 p9 p10 - part
 b0 b1 b2 - board
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
 - (either agPlanner agGrinder agVarnisher))
(:init
 (= (colour p0) green)
 (unused p0)
 (= (goalsize p0) small)
 (available p0)
 (= (wood p0) mahogany)
 (= (surface-condition p0) smooth)
 (= (treatment p0) varnished)
 (= (colour p1) green)
 (unused p1)
 (= (goalsize p1) small)
 (available p1)
 (= (wood p1) pine)
 (= (surface-condition p1) smooth)
 (= (treatment p1) colourfragments)
 (= (colour p2) red)
 (unused p2)
 (= (goalsize p2) medium)
 (available p2)
 (= (wood p2) pine)
 (= (surface-condition p2) rough)
 (= (treatment p2) varnished)
 (= (colour p3) natural)
 (unused p3)
 (= (goalsize p3) small)
 (not (available p3))
 (= (wood p3) unknown-wood)
 (= (surface-condition p3) smooth)
 (= (treatment p3) untreated)
 (= (colour p4) natural)
 (unused p4)
 (= (goalsize p4) medium)
 (not (available p4))
 (= (wood p4) unknown-wood)
 (= (surface-condition p4) smooth)
 (= (treatment p4) untreated)
 (= (colour p5) blue)
 (unused p5)
 (= (goalsize p5) medium)
 (available p5)
 (= (wood p5) oak)
 (= (surface-condition p5) smooth)
 (= (treatment p5) colourfragments)
 (= (colour p6) green)
 (unused p6)
 (= (goalsize p6) medium)
 (available p6)
 (= (wood p6) oak)
 (= (surface-condition p6) verysmooth)
 (= (treatment p6) glazed)
 (= (colour p7) black)
 (unused p7)
 (= (goalsize p7) medium)
 (available p7)
 (= (wood p7) oak)
 (= (surface-condition p7) verysmooth)
 (= (treatment p7) colourfragments)
 (= (colour p8) natural)
 (unused p8)
 (= (goalsize p8) large)
 (not (available p8))
 (= (wood p8) unknown-wood)
 (= (surface-condition p8) smooth)
 (= (treatment p8) untreated)
 (= (colour p9) natural)
 (unused p9)
 (= (goalsize p9) large)
 (not (available p9))
 (= (wood p9) unknown-wood)
 (= (surface-condition p9) smooth)
 (= (treatment p9) untreated)
 (= (colour p10) white)
 (unused p10)
 (= (goalsize p10) small)
 (available p10)
 (= (wood p10) pine)
 (= (surface-condition p10) rough)
 (= (treatment p10) varnished)
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
 (not (has-colour grinder0 mauve))
 (not (has-colour grinder0 white))
 (not (has-colour grinder0 black))
 (not (has-colour grinder0 red))
 (not (has-colour grinder0 blue))
 (not (has-colour grinder0 green))
 (not (has-colour glazer0 natural))
 (not (has-colour glazer0 mauve))
 (has-colour glazer0 white)
 (not (has-colour glazer0 black))
 (not (has-colour glazer0 red))
 (not (has-colour glazer0 blue))
 (not (has-colour glazer0 green))
 (has-colour immersion-varnisher0 natural)
 (has-colour immersion-varnisher0 mauve)
 (not (has-colour immersion-varnisher0 white))
 (not (has-colour immersion-varnisher0 black))
 (has-colour immersion-varnisher0 red)
 (has-colour immersion-varnisher0 blue)
 (not (has-colour immersion-varnisher0 green))
 (not (has-colour planer0 natural))
 (not (has-colour planer0 mauve))
 (not (has-colour planer0 white))
 (not (has-colour planer0 black))
 (not (has-colour planer0 red))
 (not (has-colour planer0 blue))
 (not (has-colour planer0 green))
 (not (has-colour highspeed-saw0 natural))
 (not (has-colour highspeed-saw0 mauve))
 (not (has-colour highspeed-saw0 white))
 (not (has-colour highspeed-saw0 black))
 (not (has-colour highspeed-saw0 red))
 (not (has-colour highspeed-saw0 blue))
 (not (has-colour highspeed-saw0 green))
 (has-colour spray-varnisher0 natural)
 (has-colour spray-varnisher0 mauve)
 (not (has-colour spray-varnisher0 white))
 (not (has-colour spray-varnisher0 black))
 (has-colour spray-varnisher0 red)
 (has-colour spray-varnisher0 blue)
 (not (has-colour spray-varnisher0 green))
 (not (has-colour saw0 natural))
 (not (has-colour saw0 mauve))
 (not (has-colour saw0 white))
 (not (has-colour saw0 black))
 (not (has-colour saw0 red))
 (not (has-colour saw0 blue))
 (not (has-colour saw0 green))
 (= (in-highspeed-saw highspeed-saw0) no-board)
 (= (boardsize b0) s5)
 (= (wood b0) mahogany)
 (= (surface-condition b0) smooth)
 (available b0)
 (= (boardsize b1) s5)
 (= (wood b1) oak)
 (= (surface-condition b1) rough)
 (available b1)
 (= (boardsize b2) s5)
 (= (wood b2) pine)
 (= (surface-condition b2) rough)
 (available b2)
)
(:global-goal (and
 (available p0)
 (= (colour p0) white)
 (= (wood p0) mahogany)
 (= (surface-condition p0) verysmooth)
 (= (treatment p0) glazed)
 (available p1)
 (= (colour p1) natural)
 (= (wood p1) pine)
 (= (surface-condition p1) verysmooth)
 (= (treatment p1) varnished)
 (available p2)
 (= (colour p2) blue)
 (= (wood p2) pine)
 (= (surface-condition p2) verysmooth)
 (= (treatment p2) varnished)
 (available p3)
 (= (wood p3) mahogany)
 (= (surface-condition p3) smooth)
 (= (treatment p3) varnished)
 (available p4)
 (= (wood p4) mahogany)
 (= (treatment p4) varnished)
 (available p5)
 (= (wood p5) oak)
 (= (surface-condition p5) verysmooth)
 (= (treatment p5) varnished)
 (available p6)
 (= (colour p6) red)
 (= (wood p6) oak)
 (= (treatment p6) varnished)
 (available p7)
 (= (wood p7) oak)
 (= (treatment p7) varnished)
 (available p8)
 (= (colour p8) natural)
 (= (wood p8) pine)
 (= (surface-condition p8) verysmooth)
 (= (treatment p8) varnished)
 (available p9)
 (= (colour p9) natural)
 (= (treatment p9) varnished)
 (available p10)
 (= (colour p10) mauve)
 (= (wood p10) pine)
 (= (surface-condition p10) verysmooth)
 (= (treatment p10) varnished)
))

)
