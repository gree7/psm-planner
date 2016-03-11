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
 green mauve white black red blue - acolour
 cherry oak mahogany - awood
 p0 p1 p2 p3 p4 p5 p6 p7 p8 p9 - part
 b0 b1 b2 b3 b4 - board
 s0 s1 s2 s3 s4 s5 s6 s7 s8 s9 s10 - aboardsize
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
 (= (colour p0) mauve)
 (unused p0)
 (= (goalsize p0) small)
 (available p0)
 (= (wood p0) cherry)
 (= (surface-condition p0) smooth)
 (= (treatment p0) colourfragments)
 (= (colour p1) natural)
 (unused p1)
 (= (goalsize p1) large)
 (not (available p1))
 (= (wood p1) unknown-wood)
 (= (surface-condition p1) smooth)
 (= (treatment p1) untreated)
 (= (colour p2) red)
 (unused p2)
 (= (goalsize p2) small)
 (available p2)
 (= (wood p2) oak)
 (= (surface-condition p2) rough)
 (= (treatment p2) colourfragments)
 (= (colour p3) black)
 (unused p3)
 (= (goalsize p3) medium)
 (available p3)
 (= (wood p3) mahogany)
 (= (surface-condition p3) verysmooth)
 (= (treatment p3) glazed)
 (= (colour p4) natural)
 (unused p4)
 (= (goalsize p4) large)
 (not (available p4))
 (= (wood p4) unknown-wood)
 (= (surface-condition p4) smooth)
 (= (treatment p4) untreated)
 (= (colour p5) natural)
 (unused p5)
 (= (goalsize p5) medium)
 (not (available p5))
 (= (wood p5) unknown-wood)
 (= (surface-condition p5) smooth)
 (= (treatment p5) untreated)
 (= (colour p6) natural)
 (unused p6)
 (= (goalsize p6) large)
 (not (available p6))
 (= (wood p6) unknown-wood)
 (= (surface-condition p6) smooth)
 (= (treatment p6) untreated)
 (= (colour p7) natural)
 (unused p7)
 (= (goalsize p7) large)
 (not (available p7))
 (= (wood p7) unknown-wood)
 (= (surface-condition p7) smooth)
 (= (treatment p7) untreated)
 (= (colour p8) natural)
 (unused p8)
 (= (goalsize p8) small)
 (not (available p8))
 (= (wood p8) unknown-wood)
 (= (surface-condition p8) smooth)
 (= (treatment p8) untreated)
 (= (colour p9) natural)
 (unused p9)
 (= (goalsize p9) medium)
 (not (available p9))
 (= (wood p9) unknown-wood)
 (= (surface-condition p9) smooth)
 (= (treatment p9) untreated)
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
 (= (boardsize-successor s5) s6)
 (= (boardsize-successor s6) s7)
 (= (boardsize-successor s7) s8)
 (= (boardsize-successor s8) s9)
 (= (boardsize-successor s9) s10)
 (not (has-colour grinder0 natural))
 (not (has-colour grinder0 green))
 (not (has-colour grinder0 mauve))
 (not (has-colour grinder0 white))
 (not (has-colour grinder0 black))
 (not (has-colour grinder0 red))
 (not (has-colour grinder0 blue))
 (not (has-colour glazer0 natural))
 (has-colour glazer0 green)
 (has-colour glazer0 mauve)
 (not (has-colour glazer0 white))
 (has-colour glazer0 black)
 (has-colour glazer0 red)
 (not (has-colour glazer0 blue))
 (not (has-colour immersion-varnisher0 natural))
 (not (has-colour immersion-varnisher0 green))
 (not (has-colour immersion-varnisher0 mauve))
 (has-colour immersion-varnisher0 white)
 (has-colour immersion-varnisher0 black)
 (has-colour immersion-varnisher0 red)
 (not (has-colour immersion-varnisher0 blue))
 (not (has-colour planer0 natural))
 (not (has-colour planer0 green))
 (not (has-colour planer0 mauve))
 (not (has-colour planer0 white))
 (not (has-colour planer0 black))
 (not (has-colour planer0 red))
 (not (has-colour planer0 blue))
 (not (has-colour highspeed-saw0 natural))
 (not (has-colour highspeed-saw0 green))
 (not (has-colour highspeed-saw0 mauve))
 (not (has-colour highspeed-saw0 white))
 (not (has-colour highspeed-saw0 black))
 (not (has-colour highspeed-saw0 red))
 (not (has-colour highspeed-saw0 blue))
 (not (has-colour spray-varnisher0 natural))
 (not (has-colour spray-varnisher0 green))
 (not (has-colour spray-varnisher0 mauve))
 (has-colour spray-varnisher0 white)
 (has-colour spray-varnisher0 black)
 (has-colour spray-varnisher0 red)
 (not (has-colour spray-varnisher0 blue))
 (not (has-colour saw0 natural))
 (not (has-colour saw0 green))
 (not (has-colour saw0 mauve))
 (not (has-colour saw0 white))
 (not (has-colour saw0 black))
 (not (has-colour saw0 red))
 (not (has-colour saw0 blue))
 (= (in-highspeed-saw highspeed-saw0) no-board)
 (= (boardsize b0) s9)
 (= (wood b0) cherry)
 (= (surface-condition b0) smooth)
 (available b0)
 (= (boardsize b1) s3)
 (= (wood b1) cherry)
 (= (surface-condition b1) rough)
 (available b1)
 (= (boardsize b2) s10)
 (= (wood b2) mahogany)
 (= (surface-condition b2) smooth)
 (available b2)
 (= (boardsize b3) s2)
 (= (wood b3) mahogany)
 (= (surface-condition b3) rough)
 (available b3)
 (= (boardsize b4) s2)
 (= (wood b4) oak)
 (= (surface-condition b4) rough)
 (available b4)
)
(:global-goal (and
 (available p0)
 (= (surface-condition p0) verysmooth)
 (= (treatment p0) glazed)
 (available p1)
 (= (wood p1) cherry)
 (= (surface-condition p1) verysmooth)
 (available p2)
 (= (colour p2) black)
 (= (wood p2) oak)
 (= (surface-condition p2) smooth)
 (= (treatment p2) varnished)
 (available p3)
 (= (colour p3) white)
 (= (wood p3) mahogany)
 (= (surface-condition p3) verysmooth)
 (= (treatment p3) varnished)
 (available p4)
 (= (colour p4) black)
 (= (treatment p4) glazed)
 (available p5)
 (= (wood p5) mahogany)
 (= (surface-condition p5) verysmooth)
 (available p6)
 (= (surface-condition p6) smooth)
 (= (treatment p6) glazed)
 (available p7)
 (= (colour p7) green)
 (= (wood p7) mahogany)
 (= (surface-condition p7) verysmooth)
 (= (treatment p7) glazed)
 (available p8)
 (= (colour p8) red)
 (= (wood p8) oak)
 (= (surface-condition p8) smooth)
 (available p9)
 (= (colour p9) mauve)
 (= (treatment p9) glazed)
))

)
