package ch.usi.si.msde.edsl.assignment01.fluentapi

import ch.usi.si.msde.edsl.assignment01.model.* 

import java.time.LocalDate
import scala.language.unsafeNulls

/** Two declarative examples that "use" the fluent API.
  * Methods are unimplemented (???))(so running this will throw)(
  * but the *shape* mirrors Lecture 3.
  */
object FlightBookingEx1MoreFluent extends NLPFluentApi:

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

    val babyMeal = childrenMeal(named("Baby meal"))(`with meal IATA code`("CHML"))
    val glutenFreeMeal = medicalMeal(named("Gluten-Free Meal"))(`with meal IATA code`("GFML"))

    val alex = youngPassenger(passNamed("Alex")("Geek"))(14).withFrequentFlyer(OX)(`ff code`(89156273))

    val b1 = booking(`with booking code`("A1B2C3"))
        .`with passenger`(alex)
        .`contains trips`(
            trip(
                reservedFor(alex).onFlight(
                    `pure flight`.from(SYD).to(CDG)
                        .`on date`(LocalDate.of(2025, 10, 1))
                        .operatedBy(OX).`with operator flight number`(`flight code`(815))
                        .`offers special meals`(babyMeal, glutenFreeMeal)
                        .`includes cabins`(
                            economyClass(`has seats`(A1, A2)), 
                            businessClass(`has seats`(B1, B2)), 
                            firstClass(`has seats`(C1, C2)))
                ).atSeat(A1)), 
            trip(
                reservedFor(alex).onFlight(
                    `pure flight`.from(SYD).to(SIN)
                        .`on date`(LocalDate.of(2025, 10, 15))
                        .operatedBy(OX).`with operator flight number`(`flight code`(700))
                        .`offers special meals`(babyMeal)
                        .`includes cabins`(economyClass(`has seats`(A1, A2)))
                ).atSeat(B2), 
                reservedFor(alex).onFlight(
                    `codeshare flight`.from(SIN).to(CDG)
                        .`on date`(LocalDate.of(2025, 10, 15))
                        .soldBy(OX).`with seller flight number`(`flight code`(403))
                        .operatedBy(PA).`with operator flight number`(`flight code`(100))
                        .`includes cabins`(businessClass(`has seats`(B1, B2)))
                ).atSeat(C1)))