all: makedir ipc-projections fmap-projections our-projections 
# big-projections

ipc-projections: \
	elevators \
	floortile \
	zenotravel \
	depots-ipc \
	driverlog-ipc \
	logistics-ipc \
	satellites-ipc \
	rovers-ipc

fmap-projections: \
	depots-ipc-fmap \
	driverlog-ipc-fmap \
	logistics-ipc-fmap \
	satellites-ipc-fmap \
	rovers-ipc-fmap \
	elevators-fmap 

our-projections: \
	beerproblem-generic \
	beerproblem-grounded \
	beerproblem-example \
	deconfliction \
	satellites \
	logistics \
	rovers \
	cities

big-projections: woodworking woodworking-fmap

makedir:
	mkdir -p projections

clean:
	rm -fr projections

#
# beerproblem-generic
#
beerproblem-generic=$(shell pwd)/ma-benchmarks/beerproblem-generic/beerproblem-generic

beerproblem-generic: $(beerproblem-generic).pddl $(beerproblem-generic).addl \
	beerproblem-generic-a2 

beerproblem-generic-%: $(beerproblem-generic)-%.pddl
	cd projections; ../agentify.sh $(beerproblem-generic).pddl $(beerproblem-generic).addl $^

#
# beerproblem-grounded
#
beerproblem-grounded=$(shell pwd)/ma-benchmarks/beerproblem-grounded/beerproblem-grounded

beerproblem-grounded: $(beerproblem-grounded).pddl $(beerproblem-grounded).addl \
	beerproblem-grounded-a2 

beerproblem-grounded-%: $(beerproblem-grounded)-%.pddl
	cd projections; ../agentify.sh $(beerproblem-grounded).pddl $(beerproblem-grounded).addl $^

#
# beerproblem-example
#
beerproblem-example=$(shell pwd)/ma-benchmarks/beerproblem-example/beerproblem-example

beerproblem-example: $(beerproblem-example).pddl $(beerproblem-example).addl \
	beerproblem-example-a2 

beerproblem-example-%: $(beerproblem-example)-%.pddl
	cd projections; ../agentify.sh $(beerproblem-example).pddl $(beerproblem-example).addl $^

#
# depots-ipc
#
depots-ipc=$(shell pwd)/ma-benchmarks/depots-ipc/depots-ipc
depots-ipc: $(depots-ipc).pddl $(depots-ipc).addl \
	depots-ipc-01 \
	depots-ipc-02 \
	depots-ipc-03 \
	depots-ipc-04 \
	depots-ipc-05 \
	depots-ipc-06 \
	depots-ipc-07 \
	depots-ipc-08 \
	depots-ipc-09 \
	depots-ipc-10 \
	depots-ipc-11 \
	depots-ipc-12 \
	depots-ipc-13 \
	depots-ipc-14 \
	depots-ipc-15 \
	depots-ipc-16 \
	depots-ipc-17 \
	depots-ipc-18 \
	depots-ipc-19 \
	depots-ipc-20
depots-ipc-%: $(depots-ipc)-%.pddl
	cd projections; ../agentify.sh $(depots-ipc).pddl $(depots-ipc).addl $^

#
# depots-ipc-fmap
#
depots-ipc-fmap=$(shell pwd)/ma-benchmarks/depots-ipc-fmap/depots-ipc-fmap
depots-ipc-fmap: $(depots-ipc-fmap).pddl $(depots-ipc-fmap).addl \
	depots-ipc-fmap-01 \
	depots-ipc-fmap-02 \
	depots-ipc-fmap-03 \
	depots-ipc-fmap-04 \
	depots-ipc-fmap-05 \
	depots-ipc-fmap-06 \
	depots-ipc-fmap-07 \
	depots-ipc-fmap-08 \
	depots-ipc-fmap-09 \
	depots-ipc-fmap-10 \
	depots-ipc-fmap-11 \
	depots-ipc-fmap-12 \
	depots-ipc-fmap-13 \
	depots-ipc-fmap-14 \
	depots-ipc-fmap-15 \
	depots-ipc-fmap-16 \
	depots-ipc-fmap-17 \
	depots-ipc-fmap-18 \
	depots-ipc-fmap-19 \
	depots-ipc-fmap-20
depots-ipc-fmap-%: $(depots-ipc-fmap)-%.pddl
	cd projections; ../agentify.sh $(depots-ipc-fmap).pddl $(depots-ipc-fmap).addl $^

#
# driverlog-ipc
#
driverlog-ipc=$(shell pwd)/ma-benchmarks/driverlog-ipc/driverlog-ipc
driverlog-ipc: $(driverlog-ipc).pddl $(driverlog-ipc).addl \
	driverlog-ipc-01 \
	driverlog-ipc-02 \
	driverlog-ipc-03 \
	driverlog-ipc-04 \
	driverlog-ipc-05 \
	driverlog-ipc-06 \
	driverlog-ipc-07 \
	driverlog-ipc-08 \
	driverlog-ipc-09 \
	driverlog-ipc-10 \
	driverlog-ipc-11 \
	driverlog-ipc-12 \
	driverlog-ipc-13 \
	driverlog-ipc-14 \
	driverlog-ipc-15 \
	driverlog-ipc-16 \
	driverlog-ipc-17 \
	driverlog-ipc-18 \
	driverlog-ipc-19 \
	driverlog-ipc-20
driverlog-ipc-%: $(driverlog-ipc)-%.pddl
	cd projections; ../agentify.sh $(driverlog-ipc).pddl $(driverlog-ipc).addl $^

#
# driverlog-ipc-fmap
#
driverlog-ipc-fmap=$(shell pwd)/ma-benchmarks/driverlog-ipc-fmap/driverlog-ipc-fmap
driverlog-ipc-fmap: $(driverlog-ipc-fmap).pddl $(driverlog-ipc-fmap).addl \
	driverlog-ipc-fmap-01 \
	driverlog-ipc-fmap-02 \
	driverlog-ipc-fmap-03 \
	driverlog-ipc-fmap-04 \
	driverlog-ipc-fmap-05 \
	driverlog-ipc-fmap-06 \
	driverlog-ipc-fmap-07 \
	driverlog-ipc-fmap-08 \
	driverlog-ipc-fmap-09 \
	driverlog-ipc-fmap-10 \
	driverlog-ipc-fmap-11 \
	driverlog-ipc-fmap-12 \
	driverlog-ipc-fmap-13 \
	driverlog-ipc-fmap-14 \
	driverlog-ipc-fmap-15 \
	driverlog-ipc-fmap-16 \
	driverlog-ipc-fmap-17 \
	driverlog-ipc-fmap-18 \
	driverlog-ipc-fmap-19 \
	driverlog-ipc-fmap-20
driverlog-ipc-fmap-%: $(driverlog-ipc-fmap)-%.pddl
	cd projections; ../agentify.sh $(driverlog-ipc-fmap).pddl $(driverlog-ipc-fmap).addl $^

#
# logistics
#
logistics=$(shell pwd)/ma-benchmarks/logistics/logistics
logistics: $(logistics).pddl $(logistics).addl \
	logistics-a2 \
	logistics-a3 \
	logistics-a4 \
	logistics-a5 \
	logistics-a6
logistics-%: $(logistics)-%.pddl
	cd projections; ../agentify.sh $(logistics).pddl $(logistics).addl $^

#
# logistics-ipc
#
logistics-ipc=$(shell pwd)/ma-benchmarks/logistics-ipc/logistics-ipc
logistics-ipc: $(logistics-ipc).pddl $(logistics-ipc).addl \
	logistics-ipc-01 \
	logistics-ipc-02 \
	logistics-ipc-03 \
	logistics-ipc-04 \
	logistics-ipc-05 \
	logistics-ipc-06 \
	logistics-ipc-07 \
	logistics-ipc-08 \
	logistics-ipc-09 \
	logistics-ipc-10 \
	logistics-ipc-11 \
	logistics-ipc-12 \
	logistics-ipc-13 \
	logistics-ipc-14 \
	logistics-ipc-15 \
	logistics-ipc-16 \
	logistics-ipc-17 \
	logistics-ipc-18 \
	logistics-ipc-19 \
	logistics-ipc-20 \
	logistics-ipc-21 \
	logistics-ipc-22 \
	logistics-ipc-23 \
	logistics-ipc-24 \
	logistics-ipc-25 \
	logistics-ipc-26 \
	logistics-ipc-27 \
	logistics-ipc-28
logistics-ipc-%: $(logistics-ipc)-%.pddl
	cd projections; ../agentify.sh $(logistics-ipc).pddl $(logistics-ipc).addl $^

#
# logistics-ipc-fmap
#
logistics-ipc-fmap=$(shell pwd)/ma-benchmarks/logistics-ipc-fmap/logistics-ipc-fmap
logistics-ipc-fmap: $(logistics-ipc-fmap).pddl $(logistics-ipc-fmap).addl \
	logistics-ipc-fmap-01 \
	logistics-ipc-fmap-02 \
	logistics-ipc-fmap-03 \
	logistics-ipc-fmap-04 \
	logistics-ipc-fmap-05 \
	logistics-ipc-fmap-06 \
	logistics-ipc-fmap-07 \
	logistics-ipc-fmap-08 \
	logistics-ipc-fmap-09 \
	logistics-ipc-fmap-10 \
	logistics-ipc-fmap-11 \
	logistics-ipc-fmap-12 \
	logistics-ipc-fmap-13 \
	logistics-ipc-fmap-14 \
	logistics-ipc-fmap-15 \
	logistics-ipc-fmap-16 \
	logistics-ipc-fmap-17 \
	logistics-ipc-fmap-18 \
	logistics-ipc-fmap-19 \
	logistics-ipc-fmap-20 \
	logistics-ipc-fmap-21 \
	logistics-ipc-fmap-22 \
	logistics-ipc-fmap-23 \
	logistics-ipc-fmap-24 \
	logistics-ipc-fmap-25 \
	logistics-ipc-fmap-26 \
	logistics-ipc-fmap-27 \
	logistics-ipc-fmap-28
logistics-ipc-fmap-%: $(logistics-ipc-fmap)-%.pddl
	cd projections; ../agentify.sh $(logistics-ipc-fmap).pddl $(logistics-ipc-fmap).addl $^

#
# deconfliction
#
deconfliction=$(shell pwd)/ma-benchmarks/deconfliction/deconfliction
deconfliction: $(deconfliction).pddl $(deconfliction).addl \
	deconfliction-a2 \
	deconfliction-a3 \
	deconfliction-a4 \
	deconfliction-a5 \
	deconfliction-a6
deconfliction-%: $(deconfliction)-%.pddl
	cd projections; ../agentify.sh $(deconfliction).pddl $(deconfliction).addl $^

#
# deconfliction with invariants
#
deconflictionInv=$(shell pwd)/ma-benchmarks/deconfliction-invariants/deconfliction-inv
deconflictionInv: $(deconflictionInv).pddl $(deconflictionInv).addl \
	deconflictionInv-a2 \
	deconflictionInv-a3 \
	deconflictionInv-a4 \
	deconflictionInv-a5 \
	deconflictionInv-a6
deconflictionInv-%: $(deconflictionInv)-%.pddl
	cd projections; ../agentify.sh $(deconflictionInv).pddl $(deconflictionInv).addl $^

#
# satellites
#
satellites=$(shell pwd)/ma-benchmarks/satellites/satellites
satellites: $(satellites).pddl $(satellites).addl \
	satellites-a02 \
	satellites-a03 \
	satellites-a04 \
	satellites-a05 \
	satellites-a06 \
	satellites-a08 \
	satellites-a10 \
	satellites-a12 \
	satellites-a14 \
	satellites-a16
satellites-%: $(satellites)-%.pddl
	cd projections; ../agentify.sh $(satellites).pddl $(satellites).addl $^

#
# satellites-ipc
#
satellites-ipc=$(shell pwd)/ma-benchmarks/satellites-ipc/satellites-ipc
satellites-ipc: $(satellites-ipc).pddl $(satellites-ipc).addl \
	satellites-ipc-a01 \
	satellites-ipc-a02 \
	satellites-ipc-a03 \
	satellites-ipc-a04 \
	satellites-ipc-a05 \
	satellites-ipc-a06 \
	satellites-ipc-a07 \
	satellites-ipc-a08 \
	satellites-ipc-a09 \
	satellites-ipc-a10 \
	satellites-ipc-a11 \
	satellites-ipc-a12 \
	satellites-ipc-a13 \
	satellites-ipc-a14 \
	satellites-ipc-a15 \
	satellites-ipc-a16 \
	satellites-ipc-a17 \
	satellites-ipc-a18 \
	satellites-ipc-a19 \
	satellites-ipc-a20
satellites-ipc-%: $(satellites-ipc)-%.pddl
	cd projections; ../agentify.sh $(satellites-ipc).pddl $(satellites-ipc).addl $^

#
# satellites-ipc-fmap
#
satellites-ipc-fmap=$(shell pwd)/ma-benchmarks/satellites-ipc-fmap/satellites-ipc-fmap
satellites-ipc-fmap: $(satellites-ipc-fmap).pddl $(satellites-ipc-fmap).addl \
	satellites-ipc-fmap-a01 \
	satellites-ipc-fmap-a02 \
	satellites-ipc-fmap-a03 \
	satellites-ipc-fmap-a04 \
	satellites-ipc-fmap-a05 \
	satellites-ipc-fmap-a06 \
	satellites-ipc-fmap-a07 \
	satellites-ipc-fmap-a08 \
	satellites-ipc-fmap-a09 \
	satellites-ipc-fmap-a10 \
	satellites-ipc-fmap-a11 \
	satellites-ipc-fmap-a12 \
	satellites-ipc-fmap-a13 \
	satellites-ipc-fmap-a14 \
	satellites-ipc-fmap-a15 \
	satellites-ipc-fmap-a16 \
	satellites-ipc-fmap-a17 \
	satellites-ipc-fmap-a18 \
	satellites-ipc-fmap-a19 \
	satellites-ipc-fmap-a20
satellites-ipc-fmap-%: $(satellites-ipc-fmap)-%.pddl
	cd projections; ../agentify.sh $(satellites-ipc-fmap).pddl $(satellites-ipc-fmap).addl $^

#
# rovers
#
rovers=$(shell pwd)/ma-benchmarks/rovers/rovers
rovers: $(rovers).pddl $(rovers).addl \
	rovers-a1 \
	rovers-a2 \
	rovers-a3 \
	rovers-a4
rovers-%: $(rovers)-%.pddl
	cd projections; ../agentify.sh $(rovers).pddl $(rovers).addl $^

#
# rovers - IPC
#
rovers-ipc=$(shell pwd)/ma-benchmarks/rovers-ipc/rovers-ipc
rovers-ipc: $(rovers-ipc).pddl $(rovers-ipc).addl \
	rovers-ipc-p01 \
	rovers-ipc-p02 \
	rovers-ipc-p03 \
	rovers-ipc-p04 \
	rovers-ipc-p05 \
	rovers-ipc-p06 \
	rovers-ipc-p07 \
	rovers-ipc-p08 \
	rovers-ipc-p09 \
	rovers-ipc-p10 \
	rovers-ipc-p11 \
	rovers-ipc-p12 \
	rovers-ipc-p13 \
	rovers-ipc-p14 \
	rovers-ipc-p15 \
	rovers-ipc-p16 \
	rovers-ipc-p17 \
	rovers-ipc-p18 \
	rovers-ipc-p19 \
	rovers-ipc-p20
rovers-ipc-%: $(rovers-ipc)-%.pddl
	cd projections; ../agentify.sh $(rovers-ipc).pddl $(rovers-ipc).addl $^

#
# rovers - IPC
#
rovers-ipc-fmap=$(shell pwd)/ma-benchmarks/rovers-ipc-fmap/rovers-ipc-fmap
rovers-ipc-fmap: $(rovers-ipc-fmap).pddl $(rovers-ipc-fmap).addl \
	rovers-ipc-fmap-p01 \
	rovers-ipc-fmap-p02 \
	rovers-ipc-fmap-p03 \
	rovers-ipc-fmap-p04 \
	rovers-ipc-fmap-p05 \
	rovers-ipc-fmap-p06 \
	rovers-ipc-fmap-p07 \
	rovers-ipc-fmap-p08 \
	rovers-ipc-fmap-p09 \
	rovers-ipc-fmap-p10 \
	rovers-ipc-fmap-p11 \
	rovers-ipc-fmap-p12 \
	rovers-ipc-fmap-p13 \
	rovers-ipc-fmap-p14 \
	rovers-ipc-fmap-p15 \
	rovers-ipc-fmap-p16 \
	rovers-ipc-fmap-p17 \
	rovers-ipc-fmap-p18 \
	rovers-ipc-fmap-p19 \
	rovers-ipc-fmap-p20
rovers-ipc-fmap-%: $(rovers-ipc-fmap)-%.pddl
	cd projections; ../agentify.sh $(rovers-ipc-fmap).pddl $(rovers-ipc-fmap).addl $^

#
# cities
#
cities=$(shell pwd)/ma-benchmarks/cities/cities
cities: $(cities).pddl $(cities).addl \
	cities-a2 \
	cities-a3 \
	cities-a4 \
	cities-a7 \
	cities-a9
cities-%: $(cities)-%.pddl
	cd projections; ../agentify.sh $(cities).pddl $(cities).addl $^

#
# elevators
#
elevators=$(shell pwd)/ma-benchmarks/elevators/elevators
elevators: $(elevators).pddl $(elevators).addl \
	elevators-p01 \
	elevators-p02 \
	elevators-p03 \
	elevators-p04 \
	elevators-p05 \
	elevators-p06 \
	elevators-p07 \
	elevators-p08 \
	elevators-p09 \
	elevators-p10
elevators-%: $(elevators)-%.pddl
	cd projections; ../agentify.sh $(elevators).pddl $(elevators).addl $^

#
# elevators-fmap
#
elevators-fmap=$(shell pwd)/ma-benchmarks/elevators-fmap/elevators-fmap
elevators-fmap: $(elevators-fmap).pddl $(elevators-fmap).addl \
	elevators-fmap-p01 \
	elevators-fmap-p02 \
	elevators-fmap-p03 \
	elevators-fmap-p04 \
	elevators-fmap-p05 \
	elevators-fmap-p06 \
	elevators-fmap-p07 \
	elevators-fmap-p08 \
	elevators-fmap-p09 \
	elevators-fmap-p10
elevators-fmap-%: $(elevators-fmap)-%.pddl
	cd projections; ../agentify.sh $(elevators-fmap).pddl $(elevators-fmap).addl $^

#
# floortile
#
floortile=$(shell pwd)/ma-benchmarks/floortile/floortile
floortile: $(floortile).pddl $(floortile).addl \
	floortile-001 \
	floortile-002 \
	floortile-003 \
	floortile-004 \
	floortile-005
floortile-%: $(floortile)-%.pddl
	cd projections; ../agentify.sh $(floortile).pddl $(floortile).addl $^

#
# woodworking
#
woodworking=$(shell pwd)/ma-benchmarks/woodworking/woodworking
woodworking: $(woodworking).pddl $(woodworking).addl \
	woodworking-p01 \
	woodworking-p02 \
	woodworking-p03 \
	woodworking-p04 \
	woodworking-p05 \
	woodworking-p06 \
	woodworking-p07 \
	woodworking-p08 \
	woodworking-p09 \
	woodworking-p10
woodworking-%: $(woodworking)-%.pddl
	cd projections; ../agentify.sh $(woodworking).pddl $(woodworking).addl $^

#
# woodworking-fmap
#
woodworking-fmap=$(shell pwd)/ma-benchmarks/woodworking-fmap/woodworking-fmap
woodworking-fmap: $(woodworking-fmap).pddl $(woodworking-fmap).addl \
	woodworking-fmap-p01 \
	woodworking-fmap-p02 \
	woodworking-fmap-p03 \
	woodworking-fmap-p04 \
	woodworking-fmap-p05 \
	woodworking-fmap-p06 \
	woodworking-fmap-p07 \
	woodworking-fmap-p08 \
	woodworking-fmap-p09 \
	woodworking-fmap-p10
woodworking-fmap-%: $(woodworking-fmap)-%.pddl
	cd projections; ../agentify.sh $(woodworking-fmap).pddl $(woodworking-fmap).addl $^

#
# zenotravel
#
zenotravel=$(shell pwd)/ma-benchmarks/zenotravel/zenotravel
zenotravel: $(zenotravel).pddl $(zenotravel).addl \
	zenotravel-p01 \
	zenotravel-p02 \
	zenotravel-p03 \
	zenotravel-p04 \
	zenotravel-p05 \
	zenotravel-p06 \
	zenotravel-p07 \
	zenotravel-p08 \
	zenotravel-p09 \
	zenotravel-p10 \
	zenotravel-p11 \
	zenotravel-p12 \
	zenotravel-p13 \
	zenotravel-p14 \
	zenotravel-p15 \
	zenotravel-p16 \
	zenotravel-p17 \
	zenotravel-p18 \
	zenotravel-p19 \
	zenotravel-p20
zenotravel-%: $(zenotravel)-%.pddl
	cd projections; ../agentify.sh $(zenotravel).pddl $(zenotravel).addl $^

#
# zenotravel-city
#
zenotravel-city=$(shell pwd)/ma-benchmarks/zenotravel-city/zenotravel-city
zenotravel-city: $(zenotravel-city).pddl $(zenotravel-city).addl \
	zenotravel-city-p01 \
	zenotravel-city-p02 \
	zenotravel-city-p03 \
	zenotravel-city-p04 \
	zenotravel-city-p05
zenotravel-city-%: $(zenotravel-city)-%.pddl
	cd projections; ../agentify.sh $(zenotravel-city).pddl $(zenotravel-city).addl $^
