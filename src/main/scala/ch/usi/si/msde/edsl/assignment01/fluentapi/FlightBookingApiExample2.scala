package ch.usi.si.msde.edsl.assignment01.fluentapi

import ch.usi.si.msde.edsl.assignment01.model.*
import java.time.LocalDate
import scala.language.unsafeNulls

object FlightBookingEx2 extends NLPFluentApi:

    val MXP = airport(named("Milano Malpensa"))(`with airport code`("MXP"))(`located in`("Milan"))
    val ZRH = airport(named("Zurich Airport"))(`with airport code`("ZRH"))(`located in`("Zurich"))
    
    val LX = airline(named("Swiss Airline"))(`with IATA code`("LX"))

    // Variables to easy reuse
    val A1 = seat('A')(1)
    val A2 = seat('A')(2)
    val B1 = seat('B')(1)
    val B2 = seat('B')(2)
    val C1 = seat('C')(1)
    val C2 = seat('C')(2)

    //Bonus
    val ecoClass = economyClass(`has seats`(A1, A2))
    val busClass = businessClass(`has seats`(B1, B2))
    val fClass = firstClass(`has seats`(C1, C2))

    // Bonus
    val glutenFreeMeal = medicalMeal(named("Gluten-Free Meal"))(`with meal IATA code`("GFML"))
    val babyMeal = childrenMeal(named("Baby meal"))(`with meal IATA code`("BBML"))

    val john = childPassenger(`named as`("John")("Doe"))
        .`years old`(14)    // Bonus
        .`without frequent flyer number`()
    val jane = adultPassenger(`named as`("Jane")("Doe"))
        .`years old`(35)    // Bonus
        .`owns frequent flyer number`(LX)(1234)

    val outboundFlight = pureFlight.from(MXP).to(ZRH)
        .`on date`(LocalDate.of(2026, 1, 1))
        .`operated by`(LX).`with operator flight number`(1234)
        .`offers special meals`(babyMeal, glutenFreeMeal)
        .`includes cabins`(ecoClass)

    val inboundFlight = pureFlight.from(ZRH).to(MXP)
        .`on date`(LocalDate.of(2026, 1, 10))
        .`operated by`(LX).`with operator flight number`(2345)
        .`includes cabins`(busClass, fClass)

    val outReservJohn = `reserved for`(john).`on flight`(outboundFlight).`at seat`(A1)
    val outReservJane = `reserved for`(jane).`on flight`(outboundFlight).`at seat`(A2)
    val inReservJohn = `reserved for`(john).`on flight`(inboundFlight).`at seat`(C1)
    val inReservJane = `reserved for`(jane).`on flight`(inboundFlight).`at seat`(C2)

    val outboundTrip = trip(outReservJohn, outReservJane)
    val inboundTrip = trip(inReservJohn, inReservJane)

    val book = booking(`with booking code`("LX9Z1Q"))
        .`involves passengers`(john, jane)
        .`contains trips`(outboundTrip, inboundTrip)

    val order1 = `ordered by`(john).on(outReservJohn).meals(babyMeal)  // childPassenger -> babyMeal
    val order2 = `ordered by`(jane).on(outReservJane).meals(glutenFreeMeal)