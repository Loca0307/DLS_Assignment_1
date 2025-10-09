package ch.usi.si.msde.edsl.assignment01.model

/**
  * Types in the JDK to define dates.
  */
import java.time.LocalDate
import java.awt.print.Book
import java.sql.Date

/*
 * Implement here, or in other scala files in this package,
 * the semantic model for Exercise 2. If you use other
 * files, please remember to import them.
 */

case class FirstName(val value: String):
  require(value != null && value.size > 0)

case class LastName(val value: String):
  require(value != null && value.size > 0)

case class Name(val fn: FirstName, val ln: LastName):
  val fullName: fn + " " + ln

case class FrequentFlyerCode(val value: Int):
  require(value != null && value > 0 && value <= 99999999)

case class FrequentFlyerNumber(val iata: IATACode, val code: FrequentFlyerCode):
  require(iata != null && code != null)

case class BookingCode(val value: Int):
  require(value != null && value > 0)

case class AirportCode(val value: String):
  require(vale != null && value.size == 3)

case class AirportCity(val value: String):
  require(value != null && value > 0)

case class Airport(val code: AirportCode, val city: AirportCity, val name: String):
  require(code != null && city != null && name != null && name.size > 0)

case class IATACode(val value: String):
  require(vale != null && value.size == 2)

case class FlightCode(val value: Int):
  require(value != null & value.size == 4)

case class Airline(val name: String, val code: IATACode):
  require(name != null && name.size > 0 && code != null)

case class FlightNumber(val code: FlightCode, val iata: IATACode):
  require(code != null && iata != null)

case class Flight(val date: Date, val airport: Airport, val airline: Airline, val flightNumber: FlightNumber) // ASTRATTO


case class Trip(val flights: Set[Flight]):
  require(flights.size > 0 && flights != null)

case class Passenger(val name: Name, val ffn: FrequentFlyerNumber):
  require(name != null)

case class Booking(val code: BookingCode, val trips: Set[Trip], val passengers: Set[Passenger]):
  require(code != null && trips != null && trips.size > 0 && passengers != null && passengers.size > 0)











  


/** Exercise 2: Example 1 
  */
@main def example1Booking: Unit =
  val exampleBooking = ???

  println(exampleBooking)

/** Examples to construct dates
  */
@main def datatypeExamples: Unit =
  // 1st of January 2024
  val date1 = LocalDate.of(2024, 1, 1)

  // 31st of may 2021
  val date2 = LocalDate.of(2021, 5, 31)

  println(date1)
  println(date2)