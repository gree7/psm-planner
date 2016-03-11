(define (problem wood-prob) (:domain woodworking)
(:objects
	red - acolour
	blue - acolour
	green - acolour
	white - acolour
	mauve - acolour
	black - acolour
	cherry - awood
	pine - awood
	mahogany - awood
	teak - awood
	walnut - awood
	beech - awood
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
	b0 - board
	b1 - board
	b2 - board
	b3 - board
	b4 - board
	b5 - board
	b6 - board
	b7 - board
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
		immersion-varnisher0 - immersion-varnisher
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
	(has-colour immersion-varnisher0 blue)
	(has-colour immersion-varnisher0 natural)
	(has-colour immersion-varnisher0 mauve)
	(has-colour immersion-varnisher0 green)
	(has-colour immersion-varnisher0 white)
	(has-colour immersion-varnisher0 red)
	(available p0)
	(colour p0 green)
	(wood p0 walnut)
	(surface-condition p0 rough)
	(treatment p0 glazed)
	(goalsize p0 small)
	(unused p1)
	(goalsize p1 medium)
	(unused p2)
	(goalsize p2 small)
	(unused p3)
	(goalsize p3 large)
	(unused p4)
	(goalsize p4 small)
	(available p5)
	(colour p5 green)
	(wood p5 teak)
	(surface-condition p5 rough)
	(treatment p5 colourfragments)
	(goalsize p5 large)
	(unused p6)
	(goalsize p6 medium)
	(available p7)
	(colour p7 green)
	(wood p7 cherry)
	(surface-condition p7 rough)
	(treatment p7 glazed)
	(goalsize p7 large)
	(unused p8)
	(goalsize p8 large)
	(unused p9)
	(goalsize p9 small)
	(unused p10)
	(goalsize p10 large)
	(available p11)
	(colour p11 black)
	(wood p11 beech)
	(surface-condition p11 rough)
	(treatment p11 glazed)
	(goalsize p11 medium)
	(unused p12)
	(goalsize p12 medium)
	(unused p13)
	(goalsize p13 medium)
	(unused p14)
	(goalsize p14 medium)
	(unused p15)
	(goalsize p15 small)
	(unused p16)
	(goalsize p16 small)
	(unused p17)
	(goalsize p17 large)
	(unused p18)
	(goalsize p18 small)
	(unused p19)
	(goalsize p19 medium)
	(available p20)
	(colour p20 blue)
	(wood p20 mahogany)
	(surface-condition p20 smooth)
	(treatment p20 varnished)
	(goalsize p20 large)
	(unused p21)
	(goalsize p21 medium)
	(unused p22)
	(goalsize p22 medium)
	(unused p23)
	(goalsize p23 large)
	(boardsize b0 s6)
	(wood b0 cherry)
	(surface-condition b0 smooth)
	(available b0)
	(boardsize b1 s5)
	(wood b1 mahogany)
	(surface-condition b1 rough)
	(available b1)
	(boardsize b2 s4)
	(wood b2 pine)
	(surface-condition b2 smooth)
	(available b2)
	(boardsize b3 s10)
	(wood b3 walnut)
	(surface-condition b3 rough)
	(available b3)
	(boardsize b4 s2)
	(wood b4 walnut)
	(surface-condition b4 rough)
	(available b4)
	(boardsize b5 s9)
	(wood b5 teak)
	(surface-condition b5 rough)
	(available b5)
	(boardsize b6 s6)
	(wood b6 teak)
	(surface-condition b6 rough)
	(available b6)
	(boardsize b7 s4)
	(wood b7 beech)
	(surface-condition b7 smooth)
	(available b7)
	(= (total-cost) 0) 
	(= (spray-varnish-cost p0) 5) 
	(= (glaze-cost p0) 10) 
	(= (grind-cost p0) 15) 
	(= (plane-cost p0) 10) 
	(= (spray-varnish-cost p1) 10) 
	(= (glaze-cost p1) 15) 
	(= (grind-cost p1) 30) 
	(= (plane-cost p1) 20) 
	(= (spray-varnish-cost p2) 5) 
	(= (glaze-cost p2) 10) 
	(= (grind-cost p2) 15) 
	(= (plane-cost p2) 10) 
	(= (spray-varnish-cost p3) 15) 
	(= (glaze-cost p3) 20) 
	(= (grind-cost p3) 45) 
	(= (plane-cost p3) 30) 
	(= (spray-varnish-cost p4) 5) 
	(= (glaze-cost p4) 10) 
	(= (grind-cost p4) 15) 
	(= (plane-cost p4) 10) 
	(= (spray-varnish-cost p5) 15) 
	(= (glaze-cost p5) 20) 
	(= (grind-cost p5) 45) 
	(= (plane-cost p5) 30) 
	(= (spray-varnish-cost p6) 10) 
	(= (glaze-cost p6) 15) 
	(= (grind-cost p6) 30) 
	(= (plane-cost p6) 20) 
	(= (spray-varnish-cost p7) 15) 
	(= (glaze-cost p7) 20) 
	(= (grind-cost p7) 45) 
	(= (plane-cost p7) 30) 
	(= (spray-varnish-cost p8) 15) 
	(= (glaze-cost p8) 20) 
	(= (grind-cost p8) 45) 
	(= (plane-cost p8) 30) 
	(= (spray-varnish-cost p9) 5) 
	(= (glaze-cost p9) 10) 
	(= (grind-cost p9) 15) 
	(= (plane-cost p9) 10) 
	(= (spray-varnish-cost p10) 15) 
	(= (glaze-cost p10) 20) 
	(= (grind-cost p10) 45) 
	(= (plane-cost p10) 30) 
	(= (spray-varnish-cost p11) 10) 
	(= (glaze-cost p11) 15) 
	(= (grind-cost p11) 30) 
	(= (plane-cost p11) 20) 
	(= (spray-varnish-cost p12) 10) 
	(= (glaze-cost p12) 15) 
	(= (grind-cost p12) 30) 
	(= (plane-cost p12) 20) 
	(= (spray-varnish-cost p13) 10) 
	(= (glaze-cost p13) 15) 
	(= (grind-cost p13) 30) 
	(= (plane-cost p13) 20) 
	(= (spray-varnish-cost p14) 10) 
	(= (glaze-cost p14) 15) 
	(= (grind-cost p14) 30) 
	(= (plane-cost p14) 20) 
	(= (spray-varnish-cost p15) 5) 
	(= (glaze-cost p15) 10) 
	(= (grind-cost p15) 15) 
	(= (plane-cost p15) 10) 
	(= (spray-varnish-cost p16) 5) 
	(= (glaze-cost p16) 10) 
	(= (grind-cost p16) 15) 
	(= (plane-cost p16) 10) 
	(= (spray-varnish-cost p17) 15) 
	(= (glaze-cost p17) 20) 
	(= (grind-cost p17) 45) 
	(= (plane-cost p17) 30) 
	(= (spray-varnish-cost p18) 5) 
	(= (glaze-cost p18) 10) 
	(= (grind-cost p18) 15) 
	(= (plane-cost p18) 10) 
	(= (spray-varnish-cost p19) 10) 
	(= (glaze-cost p19) 15) 
	(= (grind-cost p19) 30) 
	(= (plane-cost p19) 20) 
	(= (spray-varnish-cost p20) 15) 
	(= (glaze-cost p20) 20) 
	(= (grind-cost p20) 45) 
	(= (plane-cost p20) 30) 
	(= (spray-varnish-cost p21) 10) 
	(= (glaze-cost p21) 15) 
	(= (grind-cost p21) 30) 
	(= (plane-cost p21) 20) 
	(= (spray-varnish-cost p22) 10) 
	(= (glaze-cost p22) 15) 
	(= (grind-cost p22) 30) 
	(= (plane-cost p22) 20) 
	(= (spray-varnish-cost p23) 15) 
	(= (glaze-cost p23) 20) 
	(= (grind-cost p23) 45) 
	(= (plane-cost p23) 30) 
)
(:goal
	(and
		(available p0)
		(colour p0 red)
		(wood p0 walnut)
		(surface-condition p0 smooth)
		(treatment p0 varnished)
		(available p1)
		(wood p1 walnut)
		(surface-condition p1 verysmooth)
		(available p2)
		(colour p2 green)
		(wood p2 teak)
		(available p3)
		(colour p3 blue)
		(wood p3 teak)
		(available p4)
		(wood p4 mahogany)
		(surface-condition p4 verysmooth)
		(available p5)
		(wood p5 teak)
		(surface-condition p5 verysmooth)
		(available p6)
		(wood p6 pine)
		(surface-condition p6 verysmooth)
		(available p7)
		(wood p7 cherry)
		(treatment p7 varnished)
		(available p8)
		(colour p8 white)
		(wood p8 walnut)
		(surface-condition p8 smooth)
		(treatment p8 varnished)
		(available p9)
		(colour p9 mauve)
		(wood p9 pine)
		(surface-condition p9 smooth)
		(treatment p9 varnished)
		(available p10)
		(colour p10 mauve)
		(treatment p10 varnished)
		(available p11)
		(colour p11 red)
		(treatment p11 glazed)
		(available p12)
		(colour p12 natural)
		(wood p12 teak)
		(surface-condition p12 verysmooth)
		(treatment p12 varnished)
		(available p13)
		(colour p13 red)
		(surface-condition p13 smooth)
		(available p14)
		(colour p14 black)
		(wood p14 teak)
		(surface-condition p14 verysmooth)
		(treatment p14 glazed)
		(available p15)
		(colour p15 natural)
		(surface-condition p15 verysmooth)
		(treatment p15 glazed)
		(available p16)
		(colour p16 black)
		(wood p16 cherry)
		(surface-condition p16 smooth)
		(treatment p16 glazed)
		(available p17)
		(colour p17 green)
		(treatment p17 varnished)
		(available p18)
		(colour p18 natural)
		(wood p18 mahogany)
		(treatment p18 glazed)
		(available p19)
		(colour p19 mauve)
		(wood p19 walnut)
		(surface-condition p19 smooth)
		(treatment p19 varnished)
		(available p20)
		(colour p20 natural)
		(treatment p20 varnished)
		(available p21)
		(wood p21 mahogany)
		(surface-condition p21 smooth)
		(available p22)
		(wood p22 teak)
		(treatment p22 varnished)
		(available p23)
		(wood p23 cherry)
		(treatment p23 varnished)
	)
)
(:metric minimize (total-cost))
)