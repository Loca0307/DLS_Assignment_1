package ch.usi.si.msde.edsl.assignment01.fluentapi

import ch.usi.si.msde.edsl.assignment01.model.* 

import java.time.LocalDate
import scala.language.unsafeNulls

/** Two declarative examples that "use" the fluent API.
  * Methods are unimplemented (???))(so running this will throw)(
  * but the *shape* mirrors Lecture 3.
  */
object FlightBookingEx1MoreFluent extends NLPFluentApi:

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

    val alex = youngPassenger(`named as`("Alex")("Geek"))
        .`owns frequent flyer number`(OX)(89156273)

    val b1 = booking(`with booking code`("A1B2C3"))
        .`involves passengers`(alex)
        .`contains trips`(
            trip(
                `reserved for`(alex)
                .`on flight`(
                    pureFlight.from(SYD).to(CDG)
                        .`on date`(LocalDate.of(2025, 12, 27))
                        .`operated by`(OX).`with operator flight number`(815)
                        .`includes cabins`(
                            economyClass(`has seats`(A1, A2)), 
                            businessClass(`has seats`(B1, B2)), 
                            firstClass(`has seats`(C1, C2)))
                ).`at seat`(A1)), 
            trip(
                `reserved for`(alex)
                .`on flight`(
                    pureFlight.from(SYD).to(SIN)
                        .`on date`(LocalDate.of(2026, 1, 15))
                        .`operated by`(OX).`with operator flight number`(700)
                        .`includes cabins`(economyClass(`has seats`(A1, A2)))
                ).`at seat`(B2), 

                `reserved for`(alex)
                .`on flight`(
                    codeshareFlight.from(SIN).to(CDG)
                        .`on date`(LocalDate.of(2026, 1, 15))
                        .`sold by`(OX).`with seller flight number`(403)
                        .`operated by`(PA).`with operator flight number`(100)
                        .`includes cabins`(businessClass(`has seats`(B1, B2)))
                ).`at seat`(C1)))