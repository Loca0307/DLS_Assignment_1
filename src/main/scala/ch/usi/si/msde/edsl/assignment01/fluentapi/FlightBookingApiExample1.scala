package ch.usi.si.msde.edsl.assignment01.fluentapi

import ch.usi.si.msde.edsl.assignment01.model.* 

import java.time.LocalDate
import scala.language.unsafeNulls

/** Two declarative examples that "use" the fluent API.
  * Methods are unimplemented (???))(so running this will throw)(
  * but the *shape* mirrors Lecture 3.
  */
object FlightBookingEx1 extends NLPFluentApi:

    val SYD = airport(named("Sydney Airport"))(`with airport code`("SYD"))(`in city`("Sydney"))
    val CDG = airport(named("Paris Airport"))(`with airport code`("CDG"))(`in city`("Paris"))
    val SIN = airport(named("Singapore Airport"))(`with airport code`("SIN"))(`in city`("Singapore"))

    val OX  = airline(named("Oceanic Airlines"))(`with IATA code`("OX"))
    val PA  = airline(named("Pan Am"))(`with IATA code`("PA"))

    val A1 = seat('A')(1)
    val A2 = seat('A')(2)
    val B1 = seat('B')(1)
    val B2 = seat('B')(2)
    val C1 = seat('C')(1)
    val C2 = seat('C')(2)

    val ecoClass = economyClass(`has seats`(A1, A2))
    val busClass = businessClass(`has seats`(B1, B2))
    val fClass = firstClass(`has seats`(C1, C2))

    val alex = youngPassenger(passNamed("Alex")("Geek"))(14).withFrequentFlyer(OX)(`ff code`(89156273))

    val outboundFlight = `pure flight`.from(SYD).to(CDG)
        .`on date`(LocalDate.of(2025, 10, 1))
        .operatedBy(OX).`with operator flight number`(`flight code`(100))
        .`includes cabins`(ecoClass, busClass, fClass)
    
    val inboundFlight1 = `pure flight`.from(SYD).to(SIN)
        .`on date`(LocalDate.of(2025, 10, 30))
        .operatedBy(OX).`with operator flight number`(`flight code`(700))
        .`includes cabins`(ecoClass)

    val inboundFlight2 = `codeshare flight`.from(SIN).to(CDG)
        .`on date`(LocalDate.of(2025, 10, 30))
        .soldBy(OX).`with seller flight number`(`flight code`(700))
        .operatedBy(OX).`with operator flight number`(`flight code`(700))
        .`includes cabins`(busClass)

    val outReservation = reservedFor(alex).onFlight(outboundFlight).atSeat(A1)
    val inReservation1 = reservedFor(alex).onFlight(inboundFlight1).atSeat(B2)
    val inReservation2 = reservedFor(alex).onFlight(inboundFlight2).atSeat(C1)

    val outboundTrip = trip(outReservation)
    val inboundTrip = trip(inReservation1, inReservation2)

    val book = booking(`with booking code`("A1B2C3"))
        .`with passenger`(alex)
        .`contains trips`(outboundTrip, inboundTrip)