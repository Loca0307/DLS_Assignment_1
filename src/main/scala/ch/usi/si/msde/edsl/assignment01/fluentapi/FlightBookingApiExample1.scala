package ch.usi.si.msde.edsl.assignment01.fluentapi

import ch.usi.si.msde.edsl.assignment01.model.* 

import java.time.LocalDate
import scala.language.unsafeNulls

/** Two declarative examples that "use" the fluent API.
  * Methods are unimplemented (???))(so running this will throw)(
  * but the *shape* mirrors Lecture 3.
  */
object FlightBookingEx1 extends NLPFluentApi:

    val SYD = airport(named("Sydney Airport"))(`with airport code`("SYD"))(`located in`("Sydney"))
    val CDG = airport(named("Paris Airport"))(`with airport code`("CDG"))(`located in`("Paris"))
    val SIN = airport(named("Singapore Airport"))(`with airport code`("SIN"))(`located in`("Singapore"))

    val OX  = airline(named("Oceanic Airlines"))(`with IATA code`("OX"))
    val PA  = airline(named("Pan Am"))(`with IATA code`("PA"))

    // Variables to easy reuse
    val A1 = seat('A')(1)
    val A2 = seat('A')(2)
    val B1 = seat('B')(1)
    val B2 = seat('B')(2)
    val C1 = seat('C')(1)
    val C2 = seat('C')(2)

    val ecoClass = economyClass(`has seats`(A1, A2))
    val busClass = businessClass(`has seats`(B1, B2))
    val fClass = firstClass(`has seats`(C1, C2))

    val alex = youngPassenger(`named as`("Alex")("Geek"))   // Due to bonus ex., Passenger is abstract
        .`with frequent flyer number`(OX)(89156273)

    val outboundFlight = pureFlight.from(SYD).to(CDG)
        .`on date`(LocalDate.of(2025, 12, 27))
        .`operated by`(OX).`with operator flight number`(815)  // OX0815
        .`includes cabins`(ecoClass, busClass, fClass)
    
    val inboundFlight1 = pureFlight.from(SYD).to(SIN)    // In this API, a pure flight only needs operatedBy(...) (soldBy should be deducted)
        .`on date`(LocalDate.of(2026, 1, 15))
        .`operated by`(OX).`with operator flight number`(700)  // OX0700
        .`includes cabins`(ecoClass)

    val inboundFlight2 = codeshareFlight.from(SIN).to(CDG)
        .`on date`(LocalDate.of(2026, 1, 15))
        .`sold by`(OX).`with seller flight number`(403)        // OX0403
        .`operated by`(PA).`with operator flight number`(100)  // PA0100
        .`includes cabins`(busClass)

    val outReservation = `reserved for`(alex).`on flight`(outboundFlight).`at seat`(A1)
    val inReservation1 = `reserved for`(alex).`on flight`(inboundFlight1).`at seat`(B2)
    val inReservation2 = `reserved for`(alex).`on flight`(inboundFlight2).`at seat`(C1)

    val outboundTrip = trip(outReservation)
    val inboundTrip = trip(inReservation1, inReservation2)

    val book = booking(`with booking code`("A1B2C3"))
        .`with passengers`(alex)
        .`contains trips`(outboundTrip, inboundTrip)