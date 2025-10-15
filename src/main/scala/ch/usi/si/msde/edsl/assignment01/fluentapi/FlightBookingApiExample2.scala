package ch.usi.si.msde.edsl.assignment01.fluentapi

import ch.usi.si.msde.edsl.assignment01.model.*
import java.time.LocalDate
import scala.language.unsafeNulls

object FlightBookingEx2 extends NLPFluentApi:

    val MXP = airport(named("Milano Malpensa"))(`with airport code`("MXP"))(`in city`("Milan"))
    val ZRH = airport(named("Zurich Airport"))(`with airport code`("ZRH"))(`in city`("Zurich"))
    
    val LX = airline(named("Swiss Airline"))(`with IATA code`("LX"))

    val A1 = seat('A')(1)
    val A2 = seat('A')(2)
    val B1 = seat('B')(1)
    val B2 = seat('B')(2)
    val C1 = seat('C')(1)
    val C2 = seat('C')(2)

    val ecoClass = economyClass(`has seats`(A1, A2))
    val busClass = businessClass(`has seats`(B1, B2))
    val fClass = firstClass(`has seats`(C1, C2))

    val babyMeal = childrenMeal(named("Baby meal"))(`with meal IATA code`("CHML"))
    val glutenFreeMeal = medicalMeal(named("Gluten-Free Meal"))(`with meal IATA code`("GFML"))

    val john = childPassenger(passNamed("John")("Doe"))(5).noFrequentFlyer()
    val jane = adultPassenger(passNamed("Jane")("Doe"))(35).withFrequentFlyer(LX)(`ff code`(1234))

    val outboundFlight = `pure flight`
        .from(MXP).to(ZRH)
        .`on date`(LocalDate.of(2026, 1, 1))
        .operatedBy(LX).`with operator flight number`(`flight code`(1234))
        .`offers special meals`(babyMeal, glutenFreeMeal)
        .`includes cabins`(ecoClass)

    val inboundFlight = `pure flight`
        .from(ZRH).to(MXP)
        .`on date`(LocalDate.of(2026, 1, 10))
        .operatedBy(LX).`with operator flight number`(`flight code`(2345))
        .`offers special meals`(babyMeal)
        .`includes cabins`(busClass, fClass)

    val outReservJohn = reservedFor(john).onFlight(outboundFlight).atSeat(A1)
    val outReservJane = reservedFor(jane).onFlight(outboundFlight).atSeat(A2)
    val inReservJohn = reservedFor(john).onFlight(inboundFlight).atSeat(C1)
    val inReservJane = reservedFor(jane).onFlight(inboundFlight).atSeat(C2)

    val outboundTrip = trip(outReservJohn, outReservJane)
    val inboundTrip = trip(inReservJohn, inReservJane)

    val book = booking(`with booking code`("LX9Z1Q"))
        .`with passenger`(john, jane)
        .`contains trips`(outboundTrip, inboundTrip)

    val order1 = order(john).on(outReservJohn).meals(babyMeal)  // childPassenger -> babyMeal
    val order2 = order(jane).on(outReservJane).meals(glutenFreeMeal)
    val order3 = order(jane).on(inReservJohn).meals(babyMeal)