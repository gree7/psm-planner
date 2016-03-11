(define (problem wood-prob) (:domain woodworking)
(:objects
	red - acolour
	green - acolour
	blue - acolour
	white - acolour
	black - acolour
	mauve - acolour
	pine - awood
	beech - awood
	teak - awood
	walnut - awood
	oak - awood
	mahogany - awood
	cherry - awood
	p0 - part
	p1 - part
	p2 - part
	p3 - part
	p4 - part
	p5 - part
	p6 - part
	p7 - part
	p8 - part
	p9 - part
	p10 - part
	p11 - part
	p12 - part
	p13 - part
	p14 - part
	p15 - part
	p16 - part
	p17 - part
	p18 - part
	p19 - part
	p20 - part
	p21 - part
	p22 - part
	p23 - part
	p24 - part
	p25 - part
	p26 - part
	b0 - board
	b1 - board
	b2 - board
	b3 - board
	b4 - board
	b5 - board
	b6 - board
	b7 - board
	b8 - board
	b9 - board
	b10 - board
	s0 - aboardsize
	s1 - aboardsize
	s2 - aboardsize
	s3 - aboardsize
	s4 - aboardsize
	s5 - aboardsize
	s6 - aboardsize
	s7 - aboardsize
	s8 - aboardsize
	s9 - aboardsize
	s10 - aboardsize

	(:private
		glazer0 - glazer
	)
)
(:init
	(is-smooth smooth)
	(is-smooth verysmooth)
	(boardsize-successor s0 s1)
	(boardsize-successor s1 s2)
	(boardsize-successor s2 s3)
	(boardsize-successor s3 s4)
	(boardsize-successor s4 s5)
	(boardsize-successor s5 s6)
	(boardsize-successor s6 s7)
	(boardsize-successor s7 s8)
	(boardsize-successor s8 s9)
	(boardsize-successor s9 s10)
	(has-colour glazer0 blue)
	(has-colour glazer0 natural)
	(has-colour glazer0 mauve)
	(has-colour glazer0 green)
	(has-colour glazer0 black)
	(has-colour glazer0 white)
	(has-colour glazer0 red)
	(unused p0)
	(goalsize p0 small)
	(unused p1)
	(goalsize p1 medium)
	(unused p2)
	(goalsize p2 large)
	(unused p3)
	(goalsize p3 medium)
	(available p4)
	(colour p4 black)
	(wood p4 beech)
	(surface-condition p4 verysmooth)
	(treatment p4 glazed)
	(goalsize p4 large)
	(unused p5)
	(goalsize p5 large)
	(unused p6)
	(goalsize p6 large)
	(unused p7)
	(goalsize p7 small)
	(unused p8)
	(goalsize p8 large)
	(unused p9)
	(goalsize p9 large)
	(unused p10)
	(goalsize p10 large)
	(unused p11)
	(goalsize p11 small)
	(unused p12)
	(goalsize p12 medium)
	(unused p13)
	(goalsize p13 medium)
	(unused p14)
	(goalsize p14 small)
	(unused p15)
	(goalsize p15 medium)
	(unused p16)
	(goalsize p16 small)
	(available p17)
	(colour p17 blue)
	(wood p17 teak)
	(surface-condition p17 smooth)
	(treatment p17 varnished)
	(goalsize p17 medium)
	(unused p18)
	(goalsize p18 large)
	(unused p19)
	(goalsize p19 large)
	(unused p20)
	(goalsize p20 large)
	(unused p21)
	(goalsize p21 small)
	(unused p22)
	(goalsize p22 small)
	(unused p23)
	(goalsize p23 small)
	(unused p24)
	(goalsize p24 medium)
	(unused p25)
	(goalsize p25 medium)
	(unused p26)
	(goalsize p26 medium)
	(boardsize b0 s6)
	(wood b0 cherry)
	(surface-condition b0 rough)
	(available b0)
	(boardsize b1 s10)
	(wood b1 mahogany)
	(surface-condition b1 rough)
	(available b1)
	(boardsize b2 s7)
	(wood b2 mahogany)
	(surface-condition b2 rough)
	(available b2)
	(boardsize b3 s5)
	(wood b3 oak)
	(surface-condition b3 rough)
	(available b3)
	(boardsize b4 s2)
	(wood b4 oak)
	(surface-condition b4 rough)
	(available b4)
	(boardsize b5 s10)
	(wood b5 pine)
	(surface-condition b5 smooth)
	(available b5)
	(boardsize b6 s6)
	(wood b6 pine)
	(surface-condition b6 rough)
	(available b6)
	(boardsize b7 s7)
	(wood b7 walnut)
	(surface-condition b7 smooth)
	(available b7)
	(boardsize b8 s7)
	(wood b8 teak)
	(surface-condition b8 rough)
	(available b8)
	(boardsize b9 s9)
	(wood b9 beech)
	(surface-condition b9 rough)
	(available b9)
	(boardsize b10 s4)
	(wood b10 beech)
	(surface-condition b10 smooth)
	(available b10)
	(= (total-cost) 0) 
	(= (spray-varnish-cost p0) 5) 
	(= (glaze-cost p0) 10) 
	(= (grind-cost p0) 15) 
	(= (plane-cost p0) 10) 
	(= (spray-varnish-cost p1) 10) 
	(= (glaze-cost p1) 15) 
	(= (grind-cost p1) 30) 
	(= (plane-cost p1) 20) 
	(= (spray-varnish-cost p2) 15) 
	(= (glaze-cost p2) 20) 
	(= (grind-cost p2) 45) 
	(= (plane-cost p2) 30) 
	(= (spray-varnish-cost p3) 10) 
	(= (glaze-cost p3) 15) 
	(= (grind-cost p3) 30) 
	(= (plane-cost p3) 20) 
	(= (spray-varnish-cost p4) 15) 
	(= (glaze-cost p4) 20) 
	(= (grind-cost p4) 45) 
	(= (plane-cost p4) 30) 
	(= (spray-varnish-cost p5) 15) 
	(= (glaze-cost p5) 20) 
	(= (grind-cost p5) 45) 
	(= (plane-cost p5) 30) 
	(= (spray-varnish-cost p6) 15) 
	(= (glaze-cost p6) 20) 
	(= (grind-cost p6) 45) 
	(= (plane-cost p6) 30) 
	(= (spray-varnish-cost p7) 5) 
	(= (glaze-cost p7) 10) 
	(= (grind-cost p7) 15) 
	(= (plane-cost p7) 10) 
	(= (spray-varnish-cost p8) 15) 
	(= (glaze-cost p8) 20) 
	(= (grind-cost p8) 45) 
	(= (plane-cost p8) 30) 
	(= (spray-varnish-cost p9) 15) 
	(= (glaze-cost p9) 20) 
	(= (grind-cost p9) 45) 
	(= (plane-cost p9) 30) 
	(= (spray-varnish-cost p10) 15) 
	(= (glaze-cost p10) 20) 
	(= (grind-cost p10) 45) 
	(= (plane-cost p10) 30) 
	(= (spray-varnish-cost p11) 5) 
	(= (glaze-cost p11) 10) 
	(= (grind-cost p11) 15) 
	(= (plane-cost p11) 10) 
	(= (spray-varnish-cost p12) 10) 
	(= (glaze-cost p12) 15) 
	(= (grind-cost p12) 30) 
	(= (plane-cost p12) 20) 
	(= (spray-varnish-cost p13) 10) 
	(= (glaze-cost p13) 15) 
	(= (grind-cost p13) 30) 
	(= (plane-cost p13) 20) 
	(= (spray-varnish-cost p14) 5) 
	(= (glaze-cost p14) 10) 
	(= (grind-cost p14) 15) 
	(= (plane-cost p14) 10) 
	(= (spray-varnish-cost p15) 10) 
	(= (glaze-cost p15) 15) 
	(= (grind-cost p15) 30) 
	(= (plane-cost p15) 20) 
	(= (spray-varnish-cost p16) 5) 
	(= (glaze-cost p16) 10) 
	(= (grind-cost p16) 15) 
	(= (plane-cost p16) 10) 
	(= (spray-varnish-cost p17) 10) 
	(= (glaze-cost p17) 15) 
	(= (grind-cost p17) 30) 
	(= (plane-cost p17) 20) 
	(= (spray-varnish-cost p18) 15) 
	(= (glaze-cost p18) 20) 
	(= (grind-cost p18) 45) 
	(= (plane-cost p18) 30) 
	(= (spray-varnish-cost p19) 15) 
	(= (glaze-cost p19) 20) 
	(= (grind-cost p19) 45) 
	(= (plane-cost p19) 30) 
	(= (spray-varnish-cost p20) 15) 
	(= (glaze-cost p20) 20) 
	(= (grind-cost p20) 45) 
	(= (plane-cost p20) 30) 
	(= (spray-varnish-cost p21) 5) 
	(= (glaze-cost p21) 10) 
	(= (grind-cost p21) 15) 
	(= (plane-cost p21) 10) 
	(= (spray-varnish-cost p22) 5) 
	(= (glaze-cost p22) 10) 
	(= (grind-cost p22) 15) 
	(= (plane-cost p22) 10) 
	(= (spray-varnish-cost p23) 5) 
	(= (glaze-cost p23) 10) 
	(= (grind-cost p23) 15) 
	(= (plane-cost p23) 10) 
	(= (spray-varnish-cost p24) 10) 
	(= (glaze-cost p24) 15) 
	(= (grind-cost p24) 30) 
	(= (plane-cost p24) 20) 
	(= (spray-varnish-cost p25) 10) 
	(= (glaze-cost p25) 15) 
	(= (grind-cost p25) 30) 
	(= (plane-cost p25) 20) 
	(= (spray-varnish-cost p26) 10) 
	(= (glaze-cost p26) 15) 
	(= (grind-cost p26) 30) 
	(= (plane-cost p26) 20) 
)
(:goal
	(and
		(available p0)
		(colour p0 mauve)
		(treatment p0 varnished)
		(available p1)
		(colour p1 mauve)
		(surface-condition p1 smooth)
		(available p2)
		(wood p2 mahogany)
		(surface-condition p2 smooth)
		(available p3)
		(colour p3 black)
		(wood p3 teak)
		(surface-condition p3 smooth)
		(treatment p3 varnished)
		(available p4)
		(colour p4 green)
		(wood p4 beech)
		(surface-condition p4 smooth)
		(treatment p4 glazed)
		(available p5)
		(colour p5 black)
		(wood p5 walnut)
		(surface-condition p5 smooth)
		(treatment p5 varnished)
		(available p6)
		(colour p6 natural)
		(wood p6 pine)
		(surface-condition p6 smooth)
		(treatment p6 varnished)
		(available p7)
		(surface-condition p7 verysmooth)
		(treatment p7 varnished)
		(available p8)
		(colour p8 blue)
		(wood p8 beech)
		(available p9)
		(colour p9 mauve)
		(wood p9 pine)
		(surface-condition p9 verysmooth)
		(treatment p9 varnished)
		(available p10)
		(wood p10 pine)
		(treatment p10 glazed)
		(available p11)
		(wood p11 mahogany)
		(treatment p11 glazed)
		(available p12)
		(colour p12 blue)
		(wood p12 oak)
		(available p13)
		(colour p13 mauve)
		(surface-condition p13 verysmooth)
		(available p14)
		(colour p14 natural)
		(surface-condition p14 verysmooth)
		(available p15)
		(colour p15 blue)
		(surface-condition p15 smooth)
		(available p16)
		(colour p16 natural)
		(surface-condition p16 smooth)
		(treatment p16 varnished)
		(available p17)
		(colour p17 white)
		(wood p17 teak)
		(treatment p17 varnished)
		(available p18)
		(wood p18 mahogany)
		(treatment p18 glazed)
		(available p19)
		(wood p19 mahogany)
		(surface-condition p19 verysmooth)
		(available p20)
		(colour p20 blue)
		(treatment p20 varnished)
		(available p21)
		(colour p21 red)
		(wood p21 oak)
		(available p22)
		(colour p22 black)
		(wood p22 oak)
		(surface-condition p22 smooth)
		(treatment p22 glazed)
		(available p23)
		(colour p23 white)
		(surface-condition p23 verysmooth)
		(available p24)
		(wood p24 pine)
		(surface-condition p24 verysmooth)
		(available p25)
		(wood p25 cherry)
		(treatment p25 varnished)
		(available p26)
		(colour p26 mauve)
		(surface-condition p26 verysmooth)
	)
)
(:metric minimize (total-cost))
)